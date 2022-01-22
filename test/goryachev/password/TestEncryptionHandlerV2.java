// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.password.data.v2.EncryptionHandlerV2;


public class TestEncryptionHandlerV2
	extends TestEncryptionHandlerBase
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	@Test
	public void test() throws Exception
	{
		test(new EncryptionHandlerV2());
	}
}
