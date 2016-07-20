// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.EAXBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;


/** 
 * In memory encryption mechanism to hide values stored in memory.
 * Used for Secret* and Opaque* classes.
 * Does not provide a lot of security if an attacker has the access to the RAM,
 * but helps in cases when only a swap file or RAM snapshot is available to an attacker.
 */ 
public class MemCrypt
{
	public static final int NONCE_SIZE_BYTES = 8;
	public static final int MAC_SIZE_BITS = 64;
	private static final byte[] ZERO_BYTE_ARRAY = new byte[0];

	
	public static final byte[] encrypt(byte[] data) throws Exception
	{
		EAXBlockCipher cipher = new EAXBlockCipher(new AESFastEngine());
		
		byte[] nonce = new byte[NONCE_SIZE_BYTES];
		new SecureRandom().nextBytes(nonce);

		byte[] key = generateKey();
		KeyParameter kp = new KeyParameter(key);
		try
		{
			AEADParameters par = new AEADParameters(kp, MAC_SIZE_BITS, nonce, ZERO_BYTE_ARRAY);
			cipher.init(true, par);
			
			int sz = cipher.getOutputSize(data.length);
			byte[] out = new byte[NONCE_SIZE_BYTES + sz];
			System.arraycopy(nonce, 0, out, 0, NONCE_SIZE_BYTES);

			int off = NONCE_SIZE_BYTES;
			off += cipher.processBytes(data, 0, data.length, out, off);
			cipher.doFinal(out, off);
			return out;
		}
		finally
		{
			Crypto.zero(kp);
			Crypto.zero(key);
		}
	}
	

	public static final byte[] decrypt(byte[] data) throws Exception
	{
		EAXBlockCipher cipher = new EAXBlockCipher(new AESFastEngine());
		
		byte[] nonce = new byte[NONCE_SIZE_BYTES];
		System.arraycopy(data, 0, nonce, 0, NONCE_SIZE_BYTES);
		
		byte[] key = generateKey();
		KeyParameter kp = new KeyParameter(key);
		try
		{
			AEADParameters par = new AEADParameters(kp, MAC_SIZE_BITS, nonce, ZERO_BYTE_ARRAY);
			cipher.init(false, par);
			
			int sz = cipher.getOutputSize(data.length - NONCE_SIZE_BYTES);
			byte[] out = new byte[sz];
			
			int off = cipher.processBytes(data, NONCE_SIZE_BYTES, data.length - NONCE_SIZE_BYTES, out, 0);
			cipher.doFinal(out, off);
			return out;
		}
		finally
		{
			Crypto.zero(kp);
			Crypto.zero(key);
		}
	}
	
	
	/** it is expected this key will not change for duration of the program */
	private static final byte[] generateKey() throws Exception
	{
		SHA256Digest d = new SHA256Digest();
		update(d, Object.class.hashCode());
		update(d, String.class.hashCode());
		update(d, Integer.class.hashCode());
		update(d, Class.class.hashCode());
		update(d, ClassLoader.class.hashCode());
		byte[] b = new byte[d.getDigestSize()];
		d.doFinal(b, 0);
		return b;
	}


	private static final void update(Digest d, int x)
	{
		d.update((byte)(x >>> 24));
		d.update((byte)(x >>> 16));
		d.update((byte)(x >>>  8));
		d.update((byte)(x       ));
	}
}
