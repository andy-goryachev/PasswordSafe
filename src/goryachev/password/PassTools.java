// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.crypto.ui.SecureTextField;


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
				verifyField.clear();
			}
		}
		finally
		{
			Crypto.zero(pw);
		}
	}
}
