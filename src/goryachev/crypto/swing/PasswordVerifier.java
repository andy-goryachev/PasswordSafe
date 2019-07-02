// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import goryachev.common.i18n.TXT;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.swing.InputTracker;
import java.util.Arrays;


public abstract class PasswordVerifier
	extends InputTracker
{
	protected abstract void onPasswordsMatch(boolean havePassword, boolean match);
	
	//
	
	private final CPasswordField field1;
	private final CPasswordField field2;
	private final MatchLabel matchField;
	
	
	public PasswordVerifier(CPasswordField f1, CPasswordField f2, MatchLabel m)
	{
		this.field1 = f1;
		this.field2 = f2;
		this.matchField = m;
		
		add(f1);
		add(f2);
	}
	

	public void onInputEvent()
	{
		verify();
	}
	
	
	public void verify()
	{
		char[] p1 = null;
		char[] p2 = null;
		Boolean match = null;
		boolean have = false;
		
		try
		{
			p1 = field1.getPassword();
			p2 = field2.getPassword();
			
			if((p1.length != 0) || (p2.length != 0))
			{
				have = Arrays.equals(p1, p2);
				match = have;
			}
		}
		finally
		{
			Crypto.zero(p1);
			Crypto.zero(p2);
		}
		
		updateMatchLabel(have, match);
	}

	
	protected void updateMatchLabel(boolean have, Boolean match)
	{
		matchField.setMatch(match);
		
		onPasswordsMatch(have, Boolean.TRUE.equals(match));
	}
	
	
	public final OpaqueChars getPassword() throws Exception
	{
		OpaqueChars p1 = field1.getOpaquePassword();
		OpaqueChars p2 = field2.getOpaquePassword();
		
		char[] cs1 = null;
		char[] cs2 = null;
		try
		{
			cs1 = p1.getChars();
			cs2 = p2.getChars();
			
			if(!Arrays.equals(cs1, cs2))
			{
				throw new Exception(TXT.get("PasswordVerifier.err.no match", "Passwords don't match"));
			}
			
			p2.clear();
			return p1;
		}
		finally
		{
			Crypto.zero(cs1);
			Crypto.zero(cs2);
		}
	}


	public void clear()
	{
		field1.clear();
		field2.clear();
	}
}
