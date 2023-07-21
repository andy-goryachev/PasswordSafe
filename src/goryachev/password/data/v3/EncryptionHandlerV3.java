// Copyright © 2020-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data.v3;
import goryachev.common.io.DReader;
import goryachev.common.io.DWriter;
import goryachev.common.util.CKit;
import goryachev.memsafecrypto.CByteArray;
import goryachev.memsafecrypto.Crypto;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.memsafecrypto.bc.Blake2bDigest;
import goryachev.memsafecrypto.bc.SCrypt;
import goryachev.memsafecrypto.salsa.XSalsaTools;
import goryachev.password.data.IEncryptionHandler;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;


/**
 * Persists data with Scrypt + XSalsa20Poly1305.
 * 
 * Format:
 * 
 *    long SIGNATURE;
 *    int N;              // scrypt N parameter
 *    int R;              // scrypt R parameter
 *    int P;              // scrypt P parameter
 *    byte[] nonce;       // nonce
 *    int payloadSize;
 *    byte[] payload;     // payloadSize bytes of the payload
 *                        // payload is encrypted with XSalsa20Poly1305 cipher
 *                        // scrypt salt is a Blake2b hash of nonce
 *
 */
public final class EncryptionHandlerV3
	implements IEncryptionHandler
{
	public static final long SIGNATURE = 0x1DEA_2022_0122_1455L;
	
	public static final int KEY_SIZE_BYTES = 256/8;
	public static final int NONCE_SIZE_BYTES = 256/8;
	public static final int SCRYPT_N = 16384;
	public static final int SCRYPT_R = 8;
	public static final int SCRYPT_P = 32;
	
	/** enforce encrypted block size in 4k increments to mask the length of the payload */ 
	public static final int BLOCK_LENGTH = 4096;
	public static final int HEADER_SIZE = 8 + (4 * 3) + NONCE_SIZE_BYTES;
	
	public static final String ERROR_INVALID_FORMAT = "ERROR_INVALID_FORMAT";
	public static final String ERROR_WRONG_SIGNATURE = "ERROR_WRONG_SIGNATURE";
	
	
	/**
	 * Encrypts payload byte array with a key derived from the supplied password.
	 * Returns the byte array formatted according to the specification.  
	 */
	public final byte[] encrypt(SecureRandom random, OpaqueChars pass, CByteArray payload) throws Exception
	{
		int containerSize = (((payload.length() + 4) / BLOCK_LENGTH) + 1) * BLOCK_LENGTH;
		CByteArray container = new CByteArray(containerSize);
		try
		{
			container.setInt(0, payload.length());
			container.copyFrom(payload, 0, payload.length(), 4);
			
			int n = SCRYPT_N;
			int r = SCRYPT_R;
			int p = SCRYPT_P;
	
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DWriter out = new DWriter(bout);
			try
			{
				// header
				out.writeLong(SIGNATURE);
				out.writeInt(n);
				out.writeInt(r);
				out.writeInt(p);
		
				// the same nonce is being used for both scrypt and EAX encryption
				byte[] nonce = new byte[NONCE_SIZE_BYTES];
				random.nextBytes(nonce);
				out.write(nonce);
			
				// salt = hashed nonce
				CByteArray salt = hash(nonce);
	
				CByteArray pw = null;
				try
				{
					pw = (pass == null ? new CByteArray(0) : pass.getCByteArray());
					
					// generate key with scrypt
					// P - passphrase
					// S - salt
					// N - cpu/memory cost
					// r - block mix size parameter
					// p - parallelization parameter
					CByteArray key = SCrypt.generate(pw, salt, n, r, p, KEY_SIZE_BYTES);
					try
					{
						CByteArray enc = XSalsaTools.encryptXSalsa20Poly1305(key, CByteArray.readOnly(nonce), container);
						out.write(enc.toByteArray());
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
			
			return bout.toByteArray();
		}
		finally
		{
			container.zero();
		}
	}
	
	
	/**
	 * Attempts to decrypt the supplied byte array using the specified passphrase.
	 * Returns the decrypted data or throws an exception. 
	 */
	public final CByteArray decrypt(byte[] encrypted, OpaqueChars pass) throws Exception
	{
		if(pass == null)
		{
			return null;
		}
		else if(encrypted == null)
		{
			return null;
		}
		else if(encrypted.length == 0)
		{
			return null;
		}
		
		DReader rd = new DReader(encrypted);
		try
		{			
			// header
			long ver = rd.readLong();
			if(ver != SIGNATURE)
			{
				throw new Exception(ERROR_WRONG_SIGNATURE);
			}
			
			int n = rd.readInt();
			check(n, 1, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
			
			int r = rd.readInt();
			check(r, 1, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
			
			int p = rd.readInt();
			check(p, 1, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
			
			byte[] nonce = new byte[NONCE_SIZE_BYTES];
			CKit.readFully(rd, nonce);

			// salt = hashed nonce
			CByteArray salt = hash(nonce);

			CByteArray pw = null;
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
					CByteArray input = CByteArray.readOnly(encrypted, HEADER_SIZE, encrypted.length - HEADER_SIZE);
					
					CByteArray decrypted =  XSalsaTools.decryptXSalsa20Poly1305(key, CByteArray.readOnly(nonce), input);
					try
					{
						int len = decrypted.getInt(0);
						check(len, 0, Integer.MAX_VALUE, ERROR_INVALID_FORMAT);
						
						return CByteArray.readOnly(decrypted, 4, len); 
					}
					finally
					{
						decrypted.zero();
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
			CKit.close(rd);
		}
	}
	

	private static void check(int value, int min, int max, String err) throws Exception
	{
		if(value < min)
		{
			throw new Exception(err);
		}
		else if(value > max)
		{
			throw new Exception(err);
		}
	}
	
	
	private static CByteArray hash(byte[] b)
	{
		Blake2bDigest d = new Blake2bDigest(NONCE_SIZE_BYTES * 8);
		d.update(b, 0, b.length);
		
		CByteArray out = new CByteArray(NONCE_SIZE_BYTES);
		d.doFinal(out, 0);
		return out;
	}
}
