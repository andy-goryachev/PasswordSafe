// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.i18n.Menus;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.memsafecryptoswing.CPasswordField;
import goryachev.memsafecryptoswing.MatchLabel;
import goryachev.memsafecryptoswing.OnScreenKeyboard;
import goryachev.memsafecryptoswing.SecureTextField;
import goryachev.password.prompts.Tx;
import goryachev.password.ui.PasswordVerifier2;
import goryachev.swing.CButton;
import goryachev.swing.CCheckBox;
import goryachev.swing.CDialog;
import goryachev.swing.CFocusTraversalPolicy;
import goryachev.swing.CPanel;
import goryachev.swing.Dialogs;
import goryachev.swing.GlobalSettings;
import goryachev.swing.UI;
import goryachev.swing.XAction;
import java.awt.Component;


public class ChangePasswordDialog
	extends CDialog
{
	public final XAction commitAction = new XAction(this::onCommit);
	public final XAction generateAction = new XAction(this::generatePassword);
	protected final CPasswordField passField;
	protected final SecureTextField clearPassField;
	protected final CPasswordField verifyField;
	protected final MatchLabel matchField;
	protected final CCheckBox hidePassField;
	protected final PasswordVerifier2 verifier;
	protected final OnScreenKeyboard keyboard;
	private OpaqueChars entered;
	private boolean showPassword;

	
	public ChangePasswordDialog(Component parent)
	{
		super(parent, "ChangePasswordDialog", true);

		setTitle(Tx.ChangePassword);
		setCloseOnEscape();
		setMinimumSize(600, 300);
		setSize(600, 300);

		clearPassField = new SecureTextField();
		UI.installDefaultPopupMenu(clearPassField);
		
		passField = new CPasswordField();

		verifyField = new CPasswordField();

		hidePassField = new CCheckBox(Tx.HidePasswordInThisDialog);
		hidePassField.setBorder(null);
		GlobalSettings.setKey(hidePassField, "ChangePasswordDialog.hide");
		hidePassField.addItemListener((ev) ->
		{
			onHide();
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
		
		CButton generateButton = new CButton("Generate Password", generateAction);
		CButton cancelButton = new CButton(Menus.Cancel, closeDialogAction);
		CButton changeButton = new CButton(Tx.Change, commitAction, true);

		p.buttonPanel().addButton(generateButton);
		p.buttonPanel().fill();
		p.buttonPanel().addButton(cancelButton);
		p.buttonPanel().addButton(changeButton);
		
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
	
	
	protected void generatePassword()
	{
		boolean hide = hidePassField.isSelected();
		new GeneratePasswordDialog(this, hide, this::setPassword).open();
	}
	
	
	protected void setPassword(OpaqueChars pw)
	{
		clearPassField.setText(pw);
		passField.setPassword(pw);
		verifyField.setPassword(pw);
	}
}
