// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password.ui;
import goryachev.common.ui.InputTracker;
import goryachev.common.util.TXT;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.crypto.ui.MatchLabel;
import goryachev.crypto.ui.SecretDocument;
import goryachev.crypto.ui.SecureTextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import javax.swing.JCheckBox;


public abstract class PasswordVerifier2
	extends InputTracker
{
	protected abstract void onPasswordsMatch(boolean havePassword, boolean match);
	
	//
	
	private final SecureTextField clearPassword;
	private final CPasswordField field1;
	private final CPasswordField field2;
	private final JCheckBox hideField;
	private final MatchLabel matchField;
	
	
	public PasswordVerifier2(SecureTextField clearPassword, CPasswordField f1, CPasswordField f2, JCheckBox hideField, MatchLabel m)
	{
		this.clearPassword = clearPassword;
		this.field1 = f1;
		this.field2 = f2;
		this.hideField = hideField;
		this.matchField = m;
		
		add(f1);
		add(f2);
		if(clearPassword != null)
		{
			add(clearPassword);
		}
		
		if(hideField != null)
		{
			hideField.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent ev)
				{
					verify();
				}
			});
		}
	}
	

	public void onInputEvent()
	{
		verify();
	}
	
	
	protected boolean isClearPassword()
	{
		return ((clearPassword != null) && (hideField != null) && (!hideField.isSelected()));
	}
	
	
	public void verify()
	{
		if(isClearPassword())
		{
			boolean have = (clearPassword.getDocument().getLength() > 0);
			updateMatchLabel(have, true);
		}
		else
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
	}

	
	protected void updateMatchLabel(boolean have, Boolean match)
	{
		matchField.setMatch(match);
		
		onPasswordsMatch(have, Boolean.TRUE.equals(match));
	}
	
	
	public final OpaqueChars getPassword() throws Exception
	{
		if(isClearPassword())
		{
			SecretDocument d = clearPassword.getSecretDocument();
			return d.getOpaqueChars();
		}
		else
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
					throw new Exception(TXT.get("PasswordVerifier2.err.no match", "Passwords don't match"));
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
	}


	public void clear()
	{
		field1.clear();
		field2.clear();
		
		if(clearPassword != null)
		{
			clearPassword.getSecretDocument().clear();
		}
	}
}
