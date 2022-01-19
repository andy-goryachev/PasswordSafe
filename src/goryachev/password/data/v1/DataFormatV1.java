// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data.v1;
import goryachev.common.util.CKit;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.SecretByteArrayInputStream;
import goryachev.crypto.SecretByteArrayOutputStream;
import goryachev.crypto.XCipherInputStream;
import goryachev.crypto.XCipherOutputStream;
import goryachev.password.data.DataFile;
import goryachev.password.data.DataFormat;
import goryachev.password.data.DataTools;
import goryachev.password.data.PassEntry;
import goryachev.password.data.PassException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;


/*
 * This is an old version of the format, modeled after PasswordSafe, 
 * with the exception of removing the cleartext hash of the stretched key.
 * 
	long SIGNATURE;
	byte[SALT_LENGTH] salt;
	byte[IV_LENGTH] iv;
	int iterationCount; // > 256
	int keySize;        // size of encrypted data key
	byte[keySize] key;  // encrypted data key (encrypted with PBKDF-derived key)
	// the following is encrypted with data key
	byte[HASH_LENGTH] hash2;  // sha-512 of PBKDF-derived key
	
	// with this:
	int entryCount;     // >= 0
	{  
		byte id;        // ID_*
		byte type;      // TYPE_*
		int size;       // size in bytes
		byte[size];
	} [entryCount];
	
	int eof;        // END
	byte[] digest;  // sha-512 of the plaintext values of everything preceding
*/
public final class DataFormatV1
	implements DataFormat
{
	public static final long SIGNATURE_V1 = 0x1DEA201111111014L;
	public static final String ALGORITHM_HMAC = "HmacSHA512";
	public static final String ALGORITHM_PRNG = "SHA1PRNG";
	public static final String ALGORITHM_HASH = "SHA-512";
	public static final int SALT_LENGTH = 256;
	public static final int KDF_ITERATION_COUNT = 4096;
	public static final int IV_LENGTH = 128/8;
	public static final int KEY_LENGTH_BYTES = 256/8;
	public static final int AES_BLOCK_SIZE_BYTES = 128/8;
	public static final int HASH_LENGTH = 512/8;
	public static final int END = 0xEEEEEEED;
	public static final Charset ENCODING = Charset.forName("UTF-8");
	public static final int MAX_STRING_LENGTH = 0x7fffff;
	
	
	public DataFormatV1()
	{
	}
	
	
	public byte[] save(DataFile df, SecureRandom r) throws Exception
	{
		byte[] salt = null;
		byte[] iv = null;
		byte[] dataKey = null;
		byte[] pkey = null;
		byte[] encryptedKey = null;
		
		try
		{
//			SecureRandom r = SecureRandom.getInstance(ALGORITHM_PRNG);
			salt = new byte[SALT_LENGTH];
			r.nextBytes(salt);
			
			iv = new byte[IV_LENGTH];
			r.nextBytes(iv);
			
			dataKey = new byte[KEY_LENGTH_BYTES];
			r.nextBytes(dataKey);
	
			pkey = deriveKey(salt, KDF_ITERATION_COUNT, df.getPassword());
			byte[] hash = sha512(pkey); 
	
			encryptedKey = encrypt(pkey, dataKey);
			
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			OutputStream out = ba;
			try
			{
				MessageDigest md = MessageDigest.getInstance(ALGORITHM_HASH);
				
				writeLong(out, md, SIGNATURE_V1);
				
				writeBytes(out, md, salt);
				
				writeBytes(out, md, iv);
				
				writeInt(out, md, KDF_ITERATION_COUNT);
				
				writeInt(out, md, encryptedKey.length);
				writeBytes(out, md, encryptedKey);
				
				// write encrypted data from here on 
				out = createOutputStream(dataKey, iv, out);
				
				writeBytes(out, md, hash);
				
				PassEntry[] entries = df.getEntries();
				int sz = entries.length;
				writeInt(out, md, sz);
				
				for(int i=0; i<sz; i++)
				{
					PassEntry en = entries[i];
					
					Byte[] keys = en.getKeys();
					writeInt(out, md, keys.length);
					for(int j=0; j<keys.length; j++)
					{
						Byte id = keys[j]; 
						writeByte(out, md, id);
						
						Object x = en.getValue(id);
						writeObject(out, md, x);
					}
				}
				
				writeInt(out, md, END);
				
				byte[] hmac = md.digest();
				out.write(hmac);
			}
			finally
			{
				CKit.close(out);
			}
			
			return ba.toByteArray();
		}
		finally
		{
			Crypto.zero(dataKey);
			Crypto.zero(pkey);
			Crypto.zero(encryptedKey);
			Crypto.zero(iv);
			Crypto.zero(salt);
		}
	}
	
	
	public DataFile load(byte[] data, OpaqueChars password) throws Exception
	{
		byte[] pkey = null;
		byte[] hash = null;
		byte[] encryptedKey = null;
		byte[] decryptedKey = null;
		byte[] hash2 = null;
		
		DataFile df = new DataFile();
		df.setPassword(password);
		
		InputStream in = new ByteArrayInputStream(data);
		try
		{
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_HASH);

			long sig = readLong(in, md);
			if(SIGNATURE_V1 != sig)
			{
				throw new PassException(PassException.WRONG_SIGNATURE);
			}
						
			byte[] salt = new byte[SALT_LENGTH];
			readFully(in, md, salt);
			
			byte[] iv = new byte[IV_LENGTH];
			readFully(in, md, iv);
			
			int iterations = readInt(in, md);
			if(iterations <= 256)
			{
				throw new PassException(PassException.CORRUPTED);
			}
			
			pkey = deriveKey(salt, iterations, password);
			hash = sha512(pkey);
			
			int len = readInt(in, md);
			// TODO check range
			
			encryptedKey = new byte[len];
			readFully(in, md, encryptedKey);
			
			// read encrypted data from here on

			try
			{
				decryptedKey = decrypt(pkey, encryptedKey);

				in = createInputStream(decryptedKey, iv, in);
			}
			catch(Exception e)
			{
				throw new PassException(PassException.WRONG_PASSWORD, e);
			}
			
			hash2 = new byte[HASH_LENGTH];
			readFully(in, md, hash2);
			if(!Arrays.equals(hash, hash2))
			{
				throw new PassException(PassException.WRONG_PASSWORD);
			}
			
			int sz = readInt(in, md);
			if(sz < 0)
			{
				throw new PassException(PassException.CORRUPTED);
			}
			
			for(int i=0; i<sz; i++)
			{
				PassEntry en = df.addEntry();
				int keyCount = readInt(in, md);
				for(int j=0; j<keyCount; j++)
				{
					byte id = (byte)readByte(in, md);
					Object x = readObject(in, md);
					en.putValue(id, x);
				}
			}
			
			int end = readInt(in, md);
			if(end != END)
			{
				throw new PassException(PassException.CORRUPTED);
			}
			
			byte[] hmac = new byte[HASH_LENGTH];
			readFully(in, null, hmac);
			
			byte[] hmac2 = md.digest();
			if(!Arrays.equals(hmac, hmac2))
			{
				throw new PassException(PassException.CORRUPTED);
			}
			
			df.setPassword(password);
			return df;
		}
		finally
		{
			Crypto.zero(decryptedKey);
			Crypto.zero(encryptedKey);
			Crypto.zero(pkey);
			Crypto.zero(hash);
			Crypto.zero(hash2);
			
			CKit.close(in);
		}
	}
	
	
	private static void writeChars(OutputStream out, MessageDigest md, char[] cs) throws Exception
	{
		int sz = cs.length;
		if(sz > MAX_STRING_LENGTH)
		{
			throw new PassException(PassException.STRING_TOO_LONG);
		}
			
		writeInt(out, md, sz + sz); // 2 bytes per char
		for(int i=0; i<sz; i++)
		{
			int d = cs[i];
			writeByte(out, md, d >>> 8);
			writeByte(out, md, d);
		}
	}
	
	
	private static void readChars(InputStream in, MessageDigest md, char[] cs) throws Exception
	{
		int sz = cs.length;
		for(int i=0; i<sz; i++)
		{
			int c = readByte(in, md) << 8;
			c |= readByte(in, md);
			cs[i] = (char)c;
		}
	}
	
	
	private static void writeObject(OutputStream out, MessageDigest md, Object x) throws Exception
	{
		if(x == null)
		{
			writeByte(out, md, PassEntry.TYPE_NULL);
			writeInt(out, md, 0);
		}
		else if(x instanceof String)
		{
			writeByte(out, md, PassEntry.TYPE_STRING);
			char[] cs = null;
			try
			{
				cs = ((String)x).toCharArray();
				writeChars(out, md, cs);
			}
			finally
			{
				Crypto.zero(cs);
			}
		}
		else if(x instanceof OpaqueChars)
		{
			writeByte(out, md, PassEntry.TYPE_OPAQUE);
			char[] cs = null;
			try
			{
				cs = ((OpaqueChars)x).getChars();
				writeChars(out, md, cs);
			}
			finally
			{
				Crypto.zero(cs);
			}
		}
		else
		{
			throw new Exception("unsupported type: " + x.getClass());
		}
	}
	
	
	private static Object readObject(InputStream in, MessageDigest md) throws Exception
	{
		int type = readByte(in, md);
		int sz = readInt(in, md);
		if(sz < 0)
		{
			throw new PassException(PassException.CORRUPTED);
		}
		
		switch(type)
		{
		case PassEntry.TYPE_NULL:
			return null;
			
		case PassEntry.TYPE_STRING:
			char[] cs = null;
			try
			{
				cs = DataTools.constructCharArray(sz);
				readChars(in, md, cs);
				return new String(cs);
			}
			finally
			{
				Crypto.zero(cs);
			}
			
		case PassEntry.TYPE_OPAQUE:
			char[] ocs = null;
			try
			{
				ocs = DataTools.constructCharArray(sz);
				readChars(in, md, ocs);
				OpaqueChars op = new OpaqueChars();
				op.set(ocs);
				return op;
			}
			finally
			{
				Crypto.zero(ocs);
			}
			
		default:
			throw new PassException(PassException.CORRUPTED);
		}
	}
	
	
	private static int readByte(InputStream in, MessageDigest md) throws Exception
	{
		int c = in.read();
		if(c < 0)
		{
			throw new EOFException();
		}
		
		md.update((byte)c);
		return c & 0xff;
	}
	
	
	private static void writeByte(OutputStream out, MessageDigest md, int b) throws Exception
	{
		out.write(b);
		md.update((byte)b);
	}
	
	
	private static int readInt(InputStream in, MessageDigest md) throws Exception
	{
		int d = (readByte(in, md) << 24);
		d |= (readByte(in, md) << 16);
		d |= (readByte(in, md) << 8);
		d |= readByte(in, md);
		return d;
	}
	
	
	private static void writeInt(OutputStream out, MessageDigest md, int d) throws Exception
	{
		writeByte(out, md, d >>> 24);
		writeByte(out, md, d >>> 16);
		writeByte(out, md, d >>>  8);
		writeByte(out, md, d);
	}
	
	
	private static long readLong(InputStream in, MessageDigest md) throws Exception
	{
		long d = ((long)readByte(in, md) << 56);
		d |= ((long)readByte(in, md) << 48);
		d |= ((long)readByte(in, md) << 40);
		d |= ((long)readByte(in, md) << 32);
		d |= ((long)readByte(in, md) << 24);
		d |= ((long)readByte(in, md) << 16);
		d |= ((long)readByte(in, md) << 8);
		d |= readByte(in, md);
		return d;
	}
		
	
	private static void writeLong(OutputStream out, MessageDigest md, long d) throws Exception
	{
		writeByte(out, md, (byte)(d >>> 56));
		writeByte(out, md, (byte)(d >>> 48));
		writeByte(out, md, (byte)(d >>> 40));
		writeByte(out, md, (byte)(d >>> 32));
		writeByte(out, md, (byte)(d >>> 24));
		writeByte(out, md, (byte)(d >>> 16));
		writeByte(out, md, (byte)(d >>>  8));
		writeByte(out, md, (byte)d);
	}


	private static void readFully(InputStream in, MessageDigest md, byte b[]) throws Exception
	{
		int len = b.length;
		int off = 0;
		int rd = 0;
		while(rd < len)
		{
			int count = in.read(b, off + rd, len - rd);
			if(count < 0)
			{
				throw new EOFException();
			}
			rd += count;
		}
		
		if(md != null)
		{
			md.update(b);
		}
	}
	
	
	private static void writeBytes(OutputStream out, MessageDigest md, byte[] b) throws Exception
	{
		out.write(b);
		md.update(b);
	}

	
	private static byte[] sha512(byte[] b) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance(ALGORITHM_HASH);
		md.update(b);
		return md.digest();
	}
	
	
	private static final byte[] deriveKey(byte[] salt, int iterations, OpaqueChars password) throws Exception
	{
		PBKDF2 kdf = new PBKDF2(ALGORITHM_HMAC, salt, iterations, KEY_LENGTH_BYTES);
		byte[] b = null;
		try
		{
			b = password.getBytes();
			if(b == null)
			{
				return null;
			}
			else if(b.length == 0)
			{
				return null;
			}
			
			return kdf.deriveKey(b);
		}
		finally
		{
			Crypto.zero(b);
		}
	}
	
	
	private static final byte[] encrypt(byte[] key, byte[] data) throws Exception
	{
		PaddedBufferedBlockCipher c = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
		CipherParameters p = new KeyParameter(key);
		try
		{
			SecretByteArrayOutputStream ba = new SecretByteArrayOutputStream();
			try
			{
				XCipherOutputStream out = new XCipherOutputStream(c, p, ba);
				out.write(data);
				out.close();
		
				byte[] b = ba.toByteArray();
				return b;
			}
			finally
			{
				CKit.close(ba);
			}
		}
		finally
		{
			Crypto.zero(p);
		}
	}
	

	private static final byte[] decrypt(byte[] key, byte[] data) throws Exception
	{
		PaddedBufferedBlockCipher c = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
		CipherParameters p = new KeyParameter(key);
		try
		{
			SecretByteArrayInputStream is = new SecretByteArrayInputStream(data);
			XCipherInputStream in = new XCipherInputStream(c, p, is);
			try
			{
				SecretByteArrayOutputStream out = new SecretByteArrayOutputStream();
				try
				{
					byte[] buf = new byte[AES_BLOCK_SIZE_BYTES];
					int rd;
					while((rd = in.read(buf)) >= 0)
					{
						if(rd > 0)
						{
							out.write(buf, 0, rd);
						}
					}
					
					byte[] b = out.toByteArray();
					return b;
				}
				finally
				{
					CKit.close(out);
				}
			}
			finally
			{
				CKit.close(in);
			}
		}
		finally
		{
			Crypto.zero(p);
		}
	}
	
	
	private static final InputStream createInputStream(byte[] key, byte[] iv, InputStream is)
	{
		PaddedBufferedBlockCipher c = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
		if(iv.length != c.getBlockSize())
		{
			throw new Error("invalid block size");
		}
		CipherParameters p = new ParametersWithIV(new KeyParameter(key), iv);
		return new XCipherInputStream(c, p, is);
	}
	

	private static final OutputStream createOutputStream(byte[] key, byte[] iv, OutputStream os)
	{
		PaddedBufferedBlockCipher c = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
		if(iv.length != c.getBlockSize())
		{
			throw new Error("invalid block size");
		}
		CipherParameters p = new ParametersWithIV(new KeyParameter(key), iv);
		return new XCipherOutputStream(c, p, os);
	}
}
