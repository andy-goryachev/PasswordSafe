// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.memsafecrypto.Crypto;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.memsafecryptoswing.CPasswordField;
import goryachev.memsafecryptoswing.SecureTextField;


public class PassTools
{
	/** carries over password when the user toggles 'hide password on this dialog' checkbox */
	public static void copyPassword(SecureTextField clearField, CPasswordField passField, CPasswordField verifyField, boolean showClear)
	{
		OpaqueChars pw = null;
		try
		{
			if(showClear)
			{
				// copy from passField
				pw = passField.getOpaquePassword();
				clearField.setText(pw);
			}
			else
			{
				// copy from clearField
				pw = clearField.getOpaquePassword();
				passField.setPassword(pw);
				verifyField.setPassword(pw);
			}
		}
		finally
		{
			Crypto.zero(pw);
		}
	}
}
