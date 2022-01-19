// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.swing.CPasswordField;
import goryachev.crypto.swing.MatchLabel;
import goryachev.crypto.swing.OnScreenKeyboard;
import goryachev.crypto.swing.SecureTextField;
import goryachev.i18n.Menus;
import goryachev.password.prompts.Tx;
import goryachev.password.ui.PasswordVerifier2;
import goryachev.swing.CAction;
import goryachev.swing.CBorder;
import goryachev.swing.CButton;
import goryachev.swing.CCheckBox;
import goryachev.swing.CDialog;
import goryachev.swing.CFocusTraversalPolicy;
import goryachev.swing.CPanel;
import goryachev.swing.Dialogs;
import goryachev.swing.GlobalSettings;
import goryachev.swing.UI;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class ChangePasswordDialog
	extends CDialog
{
	public final CAction commitAction = new CAction() { public void action() { onCommit(); } };
	protected CPasswordField passField;
	protected SecureTextField clearPassField;
	protected CPasswordField verifyField;
	protected MatchLabel matchField;
	protected CCheckBox hidePassField;
	protected final PasswordVerifier2 verifier;
	protected OnScreenKeyboard keyboard;
	private OpaqueChars entered;
	private boolean showPassword;

	
	public ChangePasswordDialog(Component parent)
	{
		super(parent, "ChangePasswordDialog", true);

		setTitle(Tx.ChangePassword);
		setCloseOnEscape();

		clearPassField = new SecureTextField();
		UI.installDefaultPopupMenu(clearPassField);
		
		passField = new CPasswordField();

		verifyField = new CPasswordField();

		hidePassField = new CCheckBox(Tx.HidePasswordInThisDialog);
		hidePassField.setBorder(null);
		GlobalSettings.setKey(hidePassField, "ChangePasswordDialog.hide");
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
		
		setShowPassword(true);
		hidePassField.setSelected(false);

		updateActions();
		
		setMinimumSize(500, 300);
		setSize(500, 300);
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
		
		CPanel p = new CPanel();
		p.setBorder(new CBorder(0, 0, 10, 0));
		p.setGaps(5);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED
		);
		
		p.row(0, p.label(Tx.PasswordForm));
		if(showPassword)
		{
			p.row(1, 2, clearPassField);
		}
		else
		{
			p.row(1, 2, passField);
		}
		
		if(!showPassword)
		{
			p.nextRow();
			p.row(0, p.label(Tx.VerifyForm));
			p.row(1, 2, verifyField);
			p.row(3, matchField);
		}
		
		p.nextRow();
		p.row(1, keyboard);
		p.nextRow();
		p.add(1, 3, hidePassField);
		
		CButton cancelButton = new CButton(Menus.Cancel, closeDialogAction);
		CButton changeButton = new CButton(Tx.Change, commitAction, true);

		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
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
		
		p.buttonPanel().addButton(cancelButton);
		p.buttonPanel().addButton(changeButton);
		
		panel().setCenter(p);
				
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
