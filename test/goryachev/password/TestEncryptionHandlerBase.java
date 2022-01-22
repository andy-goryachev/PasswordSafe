// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.memsafecrypto.CByteArray;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.password.data.IEncryptionHandler;
import goryachev.password.data.v2.EncryptionHandlerV2;
import java.security.SecureRandom;


public class TestEncryptionHandlerBase
{
	public void test(IEncryptionHandler h) throws Exception
	{
		SecureRandom r = new SecureRandom();
		
		for(int i=0; i<5; i++)
		{
			byte[] d = new byte[r.nextInt(128000)];
			r.nextBytes(d);
			
			char[] cs = new char[r.nextInt(133)];
			for(int j=0; j<cs.length; j++)
			{
				char c = (char)(' ' + r.nextInt(100));
				cs[j] = c;
			}
			OpaqueChars pw = new OpaqueChars(cs);
			
			t(h, d, pw, r);
		}
	}
	
	
	protected void t(IEncryptionHandler h, byte[] payload, OpaqueChars pw, SecureRandom r) throws Exception
	{
		TF.print("payload");
		TF.list(payload);
		
		CByteArray b = CByteArray.readOnly(payload);
		byte[] enc = h.encrypt(r, pw, b);

		TF.print("enc");
		TF.list(enc);

		CByteArray db2 = h.decrypt(enc, pw);
		byte[] dec = db2.toByteArray();
		
		TF.print("dec");
		TF.list(dec);
		
		TF.eq(payload, dec);
	}
}
