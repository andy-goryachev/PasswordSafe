// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data.v2;
import goryachev.common.util.CKit;
import goryachev.crypto.SecretByteArrayOutputStream;
import goryachev.crypto.eax.EAXDecryptStream;
import goryachev.crypto.eax.EAXEncryptStream;
import goryachev.memsafecrypto.CByteArray;
import goryachev.memsafecrypto.Crypto;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.memsafecrypto.bc.SCrypt;
import goryachev.password.data.DataFile;
import goryachev.password.data.DataFormat;
import goryachev.password.data.DataTools;
import goryachev.password.data.PassEntry;
import goryachev.password.data.PassException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;


/**
 * File format based on scrypt password-based key derivation function and
 * AES cipher in EAX authenticated encryption mode.
 * 
 * Format:
 * 
 *    long SIGNATURE;
 *    int N;              // scrypt N parameter
 *    int R;              // scrypt R parameter
 *    int P;              // scrypt P parameter
 *    byte[] nonce;       // nonce used as scrypt salt as well as for EAX encryption
 *    int payloadSize;
 *    byte[] payload;     // payloadSize bytes of the payload
 * 
 * Scrypt parameters (N=16384, R=8, P=32) have been empirically selected to produce a balanced
 * price/performance ratio, resulting in 16MB RAM and approximately 3 second processing 
 * on a 2.7 GHz CPU.
 */
public final class DataFormatV2
	implements DataFormat
{
	public static final long SIGNATURE_V2 = 0x1DEA201312111148L;
	public static final int SCRYPT_N = 16384;
	public static final int SCRYPT_R = 8;
	public static final int SCRYPT_P = 32;
	public static final int KEY_SIZE_BYTES = 256/8;
	public static final int NONCE_SIZE_BYTES = 512/8;
	public static final String ERROR_WRONG_SIGNATURE = "ERROR_WRONG_SIGNATURE";
	public static final String ERROR_INVALID_FORMAT = "ERROR_INVALID_FORMAT";
	
	
	/**
	 * Encrypts payload byte array with a key derived from the supplied password.
	 * Returns the byte array formatted according to the specification.  
	 */
	public final byte[] encrypt(byte[] payload, OpaqueChars pass, SecureRandom random) throws Exception
	{
		int n = SCRYPT_N;
		int r = SCRYPT_R;
		int p = SCRYPT_P;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			// header
			DataTools.writeLong(out, SIGNATURE_V2);
			DataTools.writeInt(out, n);
			DataTools.writeInt(out, r);
			DataTools.writeInt(out, p);
	
			// the same nonce is being used for both scrypt and EAX encryption
			byte[] nonce = new byte[NONCE_SIZE_BYTES];
			random.nextBytes(nonce);
			out.write(nonce);
		
			CByteArray pw = null;
			CByteArray salt = CByteArray.readOnly(nonce); // reuse nonce as salt

			try
			{
				// generate key with scrypt
				// P - passphrase
				// S - salt
				// N - cpu/memory cost
				// r - block mix size parameter
				// p - parallelization parameter
				pw = pass.getCByteArray();
				
				CByteArray key = SCrypt.generate(pw, salt, n, r, p, KEY_SIZE_BYTES);
				try
				{
					// WARNING: leaks secret via byte[]
					byte[] bkey = key.toByteArray();
					try
					{
						EAXEncryptStream es = new EAXEncryptStream(bkey, nonce, null, out);
						try
						{
							DataTools.writeInt(es, payload.length);
							es.write(payload);
						}
						finally
						{
							CKit.close(es);
						}
					}
					finally
					{
						Crypto.zero(bkey);
					}
				}
				finally
				{
					Crypto.zero(key);
				}
			}
			finally
			{
				Crypto.zero(pw);
			}
		}
		finally
		{
			CKit.close(out);
		}
		
		return out.toByteArray();
	}
	
	
	/**
	 * Attempts to decrypt the supplied byte array using the specified passphrase.
	 * Returns the decrypted data or throws an exception. 
	 */
	public final byte[] decrypt(byte[] encrypted, OpaqueChars pass) throws Exception
	{
		ByteArrayInputStream in = new ByteArrayInputStream(encrypted);
		try
		{			
			// header
			long ver = DataTools.readLong(in);
			if(ver != SIGNATURE_V2)
			{
				throw new Exception(ERROR_WRONG_SIGNATURE);
			}
			
			int n = DataTools.readInt(in);
			DataTools.check(n, 1, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
			
			int r = DataTools.readInt(in);
			DataTools.check(r, 1, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
			
			int p = DataTools.readInt(in);
			DataTools.check(p, 1, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
			
			byte[] nonce = new byte[NONCE_SIZE_BYTES];
			CKit.readFully(in, nonce);

			CByteArray pw = null;
			CByteArray salt = CByteArray.readOnly(nonce); // reuse nonce as salt

			try
			{
				// generate key with scrypt
				// P - passphrase
				// S - salt
				// N - cpu/memory cost
				// r - block mix size parameter
				// p - parallelization parameter
				pw = pass.getCByteArray();
				
				CByteArray key = SCrypt.generate(pw, salt, n, r, p, KEY_SIZE_BYTES);
				try
				{
					// WARNING: leaks secret via byte[]
					byte[] bkey = key.toByteArray();
					try
					{
						EAXDecryptStream ds = new EAXDecryptStream(bkey, nonce, null, in);
						try
						{
							int len = DataTools.readInt(ds);
							DataTools.check(len, 0, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
							
							byte[] decrypted = new byte[len];
							CKit.readFully(ds, decrypted);
							return decrypted;
						}
						finally
						{
							CKit.close(ds);
						}
					}
					finally
					{
						Crypto.zero(bkey);
					}
				}
				finally
				{
					Crypto.zero(key);
				}
			}
			finally
			{
				Crypto.zero(pw);
			}
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	/**
	 * Serializes the data object to a byte array and then encrypts it. 
	 */
	public final byte[] save(DataFile df, SecureRandom r) throws Exception
	{
		OpaqueChars passphrase = df.getPassword();
		
		SecretByteArrayOutputStream out = new SecretByteArrayOutputStream();
		try
		{
			PassEntry[] entries = df.getEntries();
			int sz = entries.length;
			DataTools.writeInt(out, sz);
			
			for(int i=0; i<sz; i++)
			{
				PassEntry en = entries[i];
				Byte[] keys = en.getKeys();
				
				DataTools.writeInt(out, keys.length);
				
				for(int j=0; j<keys.length; j++)
				{
					Byte id = keys[j]; 
					DataTools.writeByte(out, id);
					
					Object x = en.getValue(id);
					DataTools.writeObject(out, x);
				}
			}
			
			byte[] payload = out.toByteArray();
			try
			{
				// using entropy collected from the user mouse/keyboard
				// as well as provided by the jvm
//				SecureRandom r = EntropyGathererSwing.getSecureRandom();
				
				return encrypt(payload, passphrase, r);
			}
			finally
			{
				Crypto.zero(payload);
			}
		}
		finally
		{
			CKit.close(out);
		}
	}
	

	/**
	 * Attempts to decrypt the supplied data and deserialize the DataFile object.
	 */
	public final DataFile load(byte[] encrypted, OpaqueChars passphrase) throws Exception
	{
		byte[] dec = decrypt(encrypted, passphrase);
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(dec);
			int sz = DataTools.readInt(in);
			if(sz < 0)
			{
				throw new PassException(PassException.Error.CORRUPTED);
			}
			
			DataFile df = new DataFile();
			df.setPassword(passphrase);
			
			for(int i=0; i<sz; i++)
			{
				PassEntry en = df.addEntry();
				int keyCount = DataTools.readInt(in);
				
				for(int j=0; j<keyCount; j++)
				{
					byte id = (byte)DataTools.readByte(in);
					Object x = DataTools.readObject(in);
					en.putValue(id, x);
				}
			}
			
			return df;
		}
		finally
		{
			Crypto.zero(dec);
		}
	}
}
