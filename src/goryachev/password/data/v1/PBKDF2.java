// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data.v1;
import goryachev.crypto.Crypto;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
<pre>Password-based Key Derivation Function 
RFC 2898 
http://www.ietf.org/rfc/rfc2898.txt
 
5.2 PBKDF2

   PBKDF2 applies a pseudorandom function (see Appendix B.1 for an
   example) to derive keys. The length of the derived key is essentially
   unbounded. (However, the maximum effective search space for the
   derived key may be limited by the structure of the underlying
   pseudorandom function. See Appendix B.1 for further discussion.)
   PBKDF2 is recommended for new applications.

   PBKDF2 (P, S, c, dkLen)

   Options:        PRF        underlying pseudorandom function (hLen
                              denotes the length in octets of the
                              pseudorandom function output)

   Input:          P          password, an octet string
                   S          salt, an octet string
                   c          iteration count, a positive integer
                   dkLen      intended length in octets of the derived
                              key, a positive integer, at most
                              (2^32 - 1) * hLen

   Output:         DK         derived key, a dkLen-octet string

   Steps:

      1. If dkLen > (2^32 - 1) * hLen, output "derived key too long" and
         stop.

      2. Let l be the number of hLen-octet blocks in the derived key,
         rounding up, and let r be the number of octets in the last
         block:

                   l = CEIL (dkLen / hLen) ,
                   r = dkLen - (l - 1) * hLen .

         Here, CEIL (x) is the "ceiling" function, i.e. the smallest
         integer greater than, or equal to, x.

      3. For each block of the derived key apply the function F defined
         below to the password P, the salt S, the iteration count c, and
         the block index to compute the block:

                   T_1 = F (P, S, c, 1) ,
                   T_2 = F (P, S, c, 2) ,
                   ...
                   T_l = F (P, S, c, l) ,

         where the function F is defined as the exclusive-or sum of the
         first c iterates of the underlying pseudorandom function PRF
         applied to the password P and the concatenation of the salt S
         and the block index i:

                   F (P, S, c, i) = U_1 \xor U_2 \xor ... \xor U_c

         where

                   U_1 = PRF (P, S || INT (i)) ,
                   U_2 = PRF (P, U_1) ,
                   ...
                   U_c = PRF (P, U_{c-1}) .

         Here, INT (i) is a four-octet encoding of the integer i, most
         significant octet first.

      4. Concatenate the blocks and extract the first dkLen octets to
         produce a derived key DK:

                   DK = T_1 || T_2 ||  ...  || T_l<0..r-1>

      5. Output the derived key DK.

   Note. The construction of the function F follows a "belt-and-
   suspenders" approach. The iterates U_i are computed recursively to
   remove a degree of parallelism from an opponent; they are exclusive-
   ored together to reduce concerns about the recursion degenerating
   into a small set of values.</pre>
*/
public class PBKDF2
{
	private String mac;
	private byte[] salt;
	private int count;
	private int dkLen;
	private Mac prf;
	
	
	/**
	 * mac: "HmacSHA1", "HmacSHA512"
	 */
	public PBKDF2(String mac, byte[] salt, int count, int keyLengthInBytes)
	{
		this.mac = mac;
		this.salt = salt;
		this.count = count;
		this.dkLen = keyLengthInBytes;
	}
	
	
	public byte[] deriveKey(byte[] password) throws Exception
	{
		prf = Mac.getInstance(mac);
		prf.init(new SecretKeySpec(password, mac));

		int hLen = prf.getMacLength();
		int l = Math.max(dkLen, hLen);
		int r = dkLen - (l - 1) * hLen;
		byte T[] = new byte[l * hLen];
		int ti_offset = 0;
		for(int i=1; i<=l; i++)
		{
			F(T, ti_offset, i);
			ti_offset += hLen;
		}

		if(r < hLen)
		{
			byte DK[] = new byte[dkLen];
			try
			{
				System.arraycopy(T, 0, DK, 0, dkLen);
				return DK;
			}
			finally
			{
				Crypto.zero(T);
			}
		}
		
		return T;
	}


	private void F(byte[] dest, int offset, int blockIndex)
	{
		int hLen = prf.getMacLength();
		byte U_r[] = new byte[hLen];
		byte U_i[] = new byte[salt.length + 4];
		
		System.arraycopy(salt, 0, U_i, 0, salt.length);
		INT(U_i, salt.length, blockIndex);
		
		for(int i=0; i<count; i++)
		{
			U_i = prf.doFinal(U_i);
			xor(U_r, U_i);
		}

		System.arraycopy(U_r, 0, dest, offset, hLen);
		Crypto.zero(U_r);
	}


	private static void xor(byte[] dst, byte[] src)
	{
		for(int i=0; i<dst.length; i++)
		{
			dst[i] ^= src[i];
		}
	}


	private static void INT(byte[] dst, int offset, int x)
	{
		dst[offset++] = (byte)(x >>> 24);
		dst[offset++] = (byte)(x >>> 16);
		dst[offset++] = (byte)(x >>>  8);
		dst[offset  ] = (byte)(x);
	}
}
