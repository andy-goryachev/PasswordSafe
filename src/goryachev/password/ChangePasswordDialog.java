// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CButtonPanel;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CFocusTraversalPolicy;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.Menus;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.crypto.ui.MatchLabel;
import goryachev.crypto.ui.OnScreenKeyboard;
import goryachev.crypto.ui.SecureTextField;
import goryachev.password.prompts.Tx;
import goryachev.password.ui.PasswordVerifier2;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;


public class ChangePasswordDialog
	extends CDialog
{
	public final CAction commitAction = new CAction() { public void action() { onCommit(); } };
	protected CPasswordField passField;
	protected SecureTextField clearPassField;
	protected CPasswordField verifyField;
	protected MatchLabel matchField;
	protected JCheckBox hidePassField;
	protected final PasswordVerifier2 verifier;
	protected OnScreenKeyboard keyboard;
	protected CButton changeButton;
	protected CButton cancelButton;
	private OpaqueChars entered;
	private boolean showPassword;

	
	public ChangePasswordDialog(Component parent)
	{
		super(parent, "ChangePasswordDialog", true);

		setTitle(Tx.ChangePassword);

		clearPassField = new SecureTextField();
		
		passField = new CPasswordField();

		verifyField = new CPasswordField();

		hidePassField = new JCheckBox(Tx.HidePasswordInThisDialog);
		hidePassField.setBorder(null);
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
			protected void onPasswordsMatch(boolean have, boolean match)
            {
				commitAction.setEnabled(have && match);
            }
		};
		
		keyboard = Styles.createKeyboard();
		
		changeButton = new CButton(Tx.Change, commitAction);
		changeButton.setHighlight();
		
		cancelButton = new CButton(Menus.Cancel, closeAction);
		
		setShowPassword(true);
		hidePassField.setSelected(false);

		CButtonPanel bp = new CButtonPanel(10, cancelButton, changeButton);
		getContentPanel().setSouth(bp);
		
		commitAction.setEnabled(false);
		
		pack();
	}
	

	public void onWindowClosed()
	{
		verifier.clear();
	}

	
	protected void setShowPassword(boolean on)
	{
		this.showPassword = on;
		
		CPanel p = new CPanel();
		p.setBorder(new CBorder(0, 0, 10, 0));
		p.setLayout
		(
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL,
				CPanel.PREFERRED
			},
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL
			},
			10, 5
		);
		
		int ix = 0;
		p.add(0, ix, p.label(Tx.PasswordForm));
		if(showPassword)
		{
			p.add(1, ix, 2, ix, clearPassField);
		}
		else
		{
			p.add(1, ix, 2, ix, passField);
		}
		if(!showPassword)
		{
			ix++;
			p.getTableLayout().insertRow(ix, CPanel.PREFERRED);
			p.add(0, ix, p.label(Tx.VerifyForm));
			p.add(1, ix, 2, ix, verifyField);
			p.add(3, ix, matchField);
		}
		ix++;
		p.add(1, ix, keyboard);
		ix++;
		p.add(1, ix, 3, ix, hidePassField);
		
		setContent(p);
		
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
