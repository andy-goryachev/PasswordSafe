// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.test.TF;


public class TestAll
{
	public static void main(String[] args)
	{
		TF.run
		(
			TestEncryptionHandlers.class
		);
	}
}
