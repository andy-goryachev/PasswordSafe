// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto;
import goryachev.common.util.CField;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.Rex;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.signers.RSADigestSigner;


/** Collection of simple operations related to cryptography */
public class Crypto
{
	private static final CField<int[]> BIGINTEGER_MAG = new CField(BigInteger.class, "mag");
	private static final CField<char[]> STRING_VALUE = new CField(String.class, "value");
	
	
	public static void zero(CipherParameters p)
	{
		try
		{
			if(p != null)
			{
				if(p instanceof KeyParameter)
				{
					zero(((KeyParameter)p).getKey());
				}
				else if(p instanceof ParametersWithIV)
				{
					zero(((ParametersWithIV)p).getParameters());
				}
				else
				{
					// should not see this in production
					throw new Rex("unknown " + p.getClass());
				}
			}
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}
	
	
	public static final void zero(OpaqueMemObject x)
	{
		try
		{
			if(x != null)
			{
				x.clear();
			}
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}

	
	public static final void zero(byte[] b)
	{
		try
		{
			if(b != null)
			{
				Arrays.fill(b, (byte)0);
			}
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}
	
	
	public static final void zero(char[] b)
	{
		try
		{
			if(b != null)
			{
				Arrays.fill(b, '\u0000');
			}
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}
	
	
	/** 
	 * destroys char[] array inside of a String.  
	 * this method uses reflection and may or may not work.
	 * this method must be used against strings created via String(char[]) constuctor
	 * to avoid messing up the jvm.
	 */
	public static final void zero(String s)
	{
		try
		{
			if(s != null)
			{
				char[] v = STRING_VALUE.get(s);
				if(v != null)
				{
					Arrays.fill(v, '\u0000');
				}
				
				// this will ensure NPE if the code tries to reuse the destroyed string
				STRING_VALUE.set(s, null);
			}
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}
	
	
	/** 
	 * destroys int[] array inside of a BigInteger.  
	 * this method uses reflection and may or may not work.
	 * this method must be used against objects created via explicit constructor
	 * to avoid messing up the jvm.
	 */
	public static final void zero(BigInteger x)
	{
		try
		{
			if(x != null)
			{
				int[] v = BIGINTEGER_MAG.get(x);
				if(v != null)
				{
					Arrays.fill(v, '\u0000');
				}
				
				BIGINTEGER_MAG.set(x, null);
			}
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}
	
	
	public static RSAPublicKey toRSAPublicKey(RSAPublicKeySpec k)
	{
		return new RSAPublicKey(k.getModulus(), k.getPublicExponent());
	}
	
	
	public static byte[] toByteArray(RSAPublicKeySpec spec) throws Exception
	{
		RSAPublicKey k = toRSAPublicKey(spec);
		return k.getEncoded();
	}
	
	
	public static RSAPrivateKey toRSAPrivateKey(RSAPrivateCrtKeyParameters k) throws Exception
	{
		// hope this is correct
		return new RSAPrivateKey
		(
			k.getModulus(), 
			k.getPublicExponent(),
			k.getExponent(),
			k.getP(),
			k.getQ(),
			k.getDP(),
			k.getDQ(),
			k.getQInv()
		);
	}
	

	public static RSAPublicKey toRSAPublicKey(RSAKeyParameters k) throws Exception
	{
		return new RSAPublicKey(k.getModulus(), k.getExponent());
	}


	public static byte[] toByteArray(RSAKeyParameters spec) throws Exception
	{
		if(spec instanceof RSAPrivateCrtKeyParameters)
		{
			RSAPrivateKey k = toRSAPrivateKey((RSAPrivateCrtKeyParameters)spec);
			return k.getEncoded();
		}
		else
		{
			RSAPublicKey k = toRSAPublicKey(spec);
			return k.getEncoded();
		}
	}
	
	
	public static APrivateKey getRSAPrivateKey(byte[] b) throws Exception
	{
		ASN1InputStream in = new ASN1InputStream(b);
		try
		{
			ASN1Primitive x = in.readObject();
			RSAPrivateKey k = RSAPrivateKey.getInstance(x);
	
			return new APrivateKey(new RSAPrivateCrtKeyParameters
			(
				k.getModulus(), 
				k.getPublicExponent(),
				k.getPrivateExponent(),
				k.getPrime1(),
				k.getPrime2(),
				k.getExponent1(),
				k.getExponent2(),
				k.getCoefficient()
			));
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	public static APublicKey getRSAPublicKey(byte[] b) throws Exception
	{
		ASN1InputStream in = new ASN1InputStream(b);
		try
		{
			ASN1Primitive x = in.readObject();
			RSAPublicKey k = RSAPublicKey.getInstance(x);
	
			return new APublicKey(new RSAKeyParameters(false, k.getModulus(), k.getPublicExponent()));
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	public static CipherParameters getCipherParameters(AKey k) throws Exception
	{
		Object x = k.getKey();
		if(x instanceof CipherParameters)
		{
			return (CipherParameters)x;
		}
		else
		{
			throw new Exception("no CipherParameters in " + CKit.simpleName(x));
		}
	}
	
	
	public static byte[] toByteArray(Object x) throws Exception
	{
		if(x instanceof APublicKey)
		{
			return toByteArray(((APublicKey)x).getKey());
		}
		if(x instanceof APrivateKey)
		{
			return toByteArray(((APrivateKey)x).getKey());
		}
		else if(x instanceof ASN1Object)
		{
			return ((ASN1Object)x).getEncoded();
		}
		else if(x instanceof RSAPublicKeySpec)
		{
			return toByteArray((RSAPublicKeySpec)x);
		}
		else if(x instanceof RSAKeyParameters)
		{
			return toByteArray((RSAKeyParameters)x);
		}
		else
		{
			throw new Exception("don't know how to convert " + CKit.simpleName(x) + " to byte[]");
		}
	}

	
	/** 
	 * Generates RSA key pair with the specified key size in bits and strength.
	 * See http://stackoverflow.com/questions/3087049/bouncy-castle-rsa-keypair-generation-using-lightweight-api
	 * suggested strength = 80000
	 * keySizeBits = 4096
	 */
	public static AKeyPair createRSAKeyPair(int keySizeBits, int strength) throws Exception
	{
		BigInteger publicExponent = BigInteger.valueOf(0x10001);
		SecureRandom rnd = new SecureRandom();
		RSAKeyGenerationParameters p = new RSAKeyGenerationParameters(publicExponent, rnd, keySizeBits, strength);
		
		RSAKeyPairGenerator g = new RSAKeyPairGenerator();
		g.init(p);

		AsymmetricCipherKeyPair kp = g.generateKeyPair();
		RSAPrivateCrtKeyParameters pri = (RSAPrivateCrtKeyParameters)kp.getPrivate();
		RSAKeyParameters pub = (RSAKeyParameters)kp.getPublic();
		
		return new AKeyPair(new APrivateKey(pri), new APublicKey(pub));
	}
	
	
	public static void verifySignatureSHA256(APublicKey publicKey, byte[] payload, byte[] sig) throws Exception
	{
		CipherParameters pub = getCipherParameters(publicKey);
		RSADigestSigner signer = new RSADigestSigner(new SHA256Digest());
		signer.init(false, pub);
		signer.update(payload, 0, payload.length);
		if(!signer.verifySignature(sig))
		{
			throw new Exception("failed signature verification");
		}
	}
	
	
	public static byte[] generateSignatureSHA256(APrivateKey privateKey, byte[] payload) throws Exception
	{
		CipherParameters pub = getCipherParameters(privateKey);
		RSADigestSigner signer = new RSADigestSigner(new SHA256Digest());
		signer.init(true, pub);
		signer.update(payload, 0, payload.length);
		return signer.generateSignature();
	}
	
	
	public static byte[] encryptKeyRSA(AKey encryptionKey, ASecretKey toBeEncrypted) throws Exception
	{
		PKCS1Encoding rsa = new PKCS1Encoding(new RSAEngine());
		rsa.init(true, getCipherParameters(encryptionKey));
		
		byte[] k = toBeEncrypted.toByteArray();
		try
		{
			byte[] encrypted = rsa.processBlock(k, 0, k.length);
			return encrypted;
		}
		finally
		{
			Crypto.zero(k);
		}
	}
	
	
	public static ASecretKey decryptKeyRSA(AKey encryptionKey, byte[] b) throws Exception
	{
		PKCS1Encoding rsa = new PKCS1Encoding(new RSAEngine());
		rsa.init(false, getCipherParameters(encryptionKey));
		
		byte[] decrypted = rsa.processBlock(b, 0, b.length);
		try
		{
			return new ASecretKey(decrypted);
		}
		finally
		{
			Crypto.zero(decrypted);
		}
	}
}
