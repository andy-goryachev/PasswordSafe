// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package test.password;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.crypto.OpaqueChars;
import goryachev.password.data.v2.DataFormatV2;
import java.security.SecureRandom;


public class TestDataFormatV2
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	@Test
	public void test() throws Exception
	{
		SecureRandom r = new SecureRandom();
		DataFormatV2 f = new DataFormatV2();
		
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
			
			t(f, d, pw, r);
		}
	}
	
	
	protected void t(DataFormatV2 f, byte[] d, OpaqueChars pw, SecureRandom r) throws Exception
	{
		TF.print("d");
		TF.list(d);

		byte[] enc = f.encrypt(d, pw, r);

		TF.print("enc");
		TF.list(enc);

		byte[] dec = f.decrypt(enc, pw);
		
		TF.print("dec");
		TF.list(dec);
		
		TF.eq(d, dec);
	}
}
