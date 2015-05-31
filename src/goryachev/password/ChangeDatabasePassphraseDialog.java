// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CCheckBox;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CFocusTraversalPolicy;
import goryachev.common.ui.CPanel3;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.Menus;
import goryachev.common.ui.UI;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import goryachev.common.util.UserException;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.crypto.ui.MatchLabel;
import goryachev.crypto.ui.OnScreenKeyboard;
import goryachev.crypto.ui.SecureTextField;
import goryachev.password.ui.PasswordVerifier2;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class ChangeDatabasePassphraseDialog
	extends CDialog
{
	public final CAction commitAction = new CAction() { public void action() { onCommit(); } };
	protected final CPasswordField oldPassField;
	protected final CPasswordField passField;
	protected final SecureTextField clearPassField;
	protected final CPasswordField verifyField;
	protected final MatchLabel matchField;
	protected final CCheckBox hidePassField;
	protected final PasswordVerifier2 verifier;
	protected OnScreenKeyboard keyboard;
	protected CButton changeButton;
	protected CButton cancelButton;
	private OpaqueChars entered;
	private OpaqueChars oldPass;
	private boolean showPassword;

	
	public ChangeDatabasePassphraseDialog(Component parent, OpaqueChars oldPass)
	{
		super(parent, "ChangeDatabasePassphraseDialog", true);
		setCloseOnEscape();
		
		this.oldPass = oldPass;

		setTitle(TXT.get("ChangeDatabasePassphraseDialog.title", "Change Database Passphrase"));
		setMinimumSize(500, 400);
		
		oldPassField = new CPasswordField();

		clearPassField = new SecureTextField();
		UI.installDefaultPopupMenu(clearPassField);
		
		passField = new CPasswordField();

		verifyField = new CPasswordField();

		hidePassField = new CCheckBox(TXT.get("ChangeDatabasePassphraseDialog.check.hide passphrase", "hide passphrase in this dialog"));
		hidePassField.setBorder(null);
		GlobalSettings.setKey(hidePassField, "ChangeDatabasePassphraseDialog.hide");
		hidePassField.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				onHide();
			}
		});
		
		matchField = new MatchLabel();

		verifier = new PasswordVerifier2(clearPassField, passField, verifyField, hidePassField, matchField)
		{
			protected void onPasswordUpdate()
			{
				updateActions();
			}
		};

		keyboard = Styles.createKeyboard();
		
		changeButton = new CButton(TXT.get("ChangeDatabasePassphraseDialog.button.change password", "Change"), commitAction, true);
		
		cancelButton = new CButton(Menus.Cancel, closeDialogAction);
		
		setShowPassword(true);
		hidePassField.setSelected(false);

		buttonPanel().addButton(cancelButton);
		buttonPanel().addButton(changeButton);
		
		updateActions();
	}
	
	
	protected void updateActions()
	{
		commitAction.setEnabled(verifier.hasData() && verifier.hasMatch());
	}
	

	public void onWindowClosed()
	{
		verifier.clear();
	}

	
	protected void setShowPassword(boolean on)
	{
		this.showPassword = on;
		
		PassTools.copyPassword(clearPassField, passField, verifyField, on);
		
		CPanel3 p = new CPanel3();
		p.setGaps(10, 5);
		p.addColumns
		(
			CPanel3.PREFERRED,
			CPanel3.PREFERRED,
			CPanel3.FILL,
			CPanel3.PREFERRED
		);
		
		p.row(0, p.label(TXT.get("ChangeDatabasePassphraseDialog.label.old passphrase", "Old Passphrase:")));
		p.row(1, 3, oldPassField);
		p.nextRow();
		p.row(0, p.label(TXT.get("ChangeDatabasePassphraseDialog.label.passphrase", "Passphrase:")));
		if(showPassword)
		{
			p.row(1, 3, clearPassField);
		}
		else
		{
			p.row(1, 2, passField);
		}
		
		if(!showPassword)
		{
			p.nextRow();
			p.row(0, p.label(TXT.get("ChangeDatabasePassphraseDialog.label.verify password", "Verify:")));
			p.row(1, 2, verifyField);
			p.row(3, matchField);
		}
		p.nextRow();
		p.row(1, keyboard);
		p.nextRow();
		p.row(1, 3, hidePassField);
		
		panel().setCenter(p);
		
		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
		tp.add(oldPassField);
		if(showPassword)
		{
			tp.add(clearPassField);
		}
		else
		{
			tp.add(passField);
			tp.add(verifyField);
		}
		tp.add(changeButton);
		tp.add(cancelButton);
		tp.apply(this);
				
		validate();
		repaint();
	}
	
	
	protected void onHide()
	{
		boolean on = !hidePassField.isSelected();
		setShowPassword(on);
	}
	
	
	protected void onCommit()
	{
		try
		{
			// let's slow this down
			CKit.sleep(500);
			
			OpaqueChars pw = oldPassField.getOpaquePassword();
			if(!oldPass.sameAs(pw))
			{
				throw new UserException(TXT.get("ChangeDatabasePassphraseDialog.err.old passphrase is incorrect", "Old passphrase you have entered is incorrect."));
			}
				
			entered = verifier.getPassword();
			close();
		}
		catch(Exception e)
		{
			Dialogs.error(this, e.getMessage());
		}
	}
	
	
	public OpaqueChars getEnteredPassword()
	{
		return entered;
	}
}
