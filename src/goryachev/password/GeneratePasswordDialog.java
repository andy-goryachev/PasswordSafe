// Copyright © 2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.util.CKit;
import goryachev.common.util.Parsers;
import goryachev.crypto.OpaqueChars;
import goryachev.cryptoswing.CPasswordField;
import goryachev.cryptoswing.SecureTextField;
import goryachev.i18n.Menus;
import goryachev.password.PasswordGenerator.Alphabet;
import goryachev.password.prompts.Tx;
import goryachev.swing.CButton;
import goryachev.swing.CCheckBox;
import goryachev.swing.CComboBox;
import goryachev.swing.CDialog;
import goryachev.swing.CPanel;
import goryachev.swing.CTextField;
import goryachev.swing.ChangeMonitor;
import goryachev.swing.UI;
import goryachev.swing.XAction;
import java.awt.Component;
import java.util.function.Consumer;


/**
 * Generate Password Dialog.
 */
public class GeneratePasswordDialog
	extends CDialog
{
	protected final static String LATIN = "Latin";
	protected final static String CYRILLIC = "Cyrillic";
	protected final int MONITOR_DELAY = 300;
	
	protected final XAction generateAction = new XAction(this::onGenerate);
	protected final XAction okAction = new XAction(this::onOK);
	
	protected final Consumer<OpaqueChars> callback;
	protected final CComboBox alphabetField;
	protected final CTextField includeField = new CTextField();
	protected final CCheckBox uppercaseField = new CCheckBox("uppercase");
	protected final CCheckBox lowercaseField = new CCheckBox("lowercase");
	protected final CCheckBox digitsField = new CCheckBox("digits");
	protected final CComboBox lengthField;
	protected final SecureTextField clearPassField;
	protected final CPasswordField passwordField = new CPasswordField();
	protected final CCheckBox hidePassField = new CCheckBox(Tx.HidePasswordInThisDialog);
	private PasswordGenerator generator;
	private OpaqueChars password;
	
	
	// TODO position under or over the parent, or cascade
	public GeneratePasswordDialog(Component parent, boolean hidePassword, Consumer<OpaqueChars> callback)
	{
		super(parent, "GeneratePasswordDialog", true);
		this.callback = callback;

		setTitle(Tx.GeneratePassword);
		setCloseOnEscape();
		setMinimumSize(600, 400);
		setSize(600, 400);
		
		alphabetField = new CComboBox
		(
			LATIN,
			CYRILLIC
		);
		
		uppercaseField.setSelected(true);
		lowercaseField.setSelected(true);
		digitsField.setEnabled(true);
		
		hidePassField.setSelected(hidePassword);
		hidePassField.addItemListener((ev) ->
		{
			onHide();
		});
		
		lengthField = new CComboBox
		(
			8,
			9,
			10,
			11,
			12,
			13,
			14,
			15,
			16,
			24,
			32,
			48,
			64
		);
		lengthField.setEditable(true);
		lengthField.select(String.valueOf(PasswordGenerator.DEFALT_LENGTH));
		
		clearPassField = new SecureTextField();
		UI.disableEditingSetCaretVisible(clearPassField);
		UI.installDefaultPopupMenu(clearPassField);
		
		UI.disableEditingSetCaretVisible(passwordField);
		
		ChangeMonitor mon = new ChangeMonitor(MONITOR_DELAY, this::onGenerate);
		mon.listenAll
		(
			alphabetField,
			includeField,
			lowercaseField,
			digitsField,
			uppercaseField,
			lengthField
		);
		
		updateLayout();
		
		updateActions();
		
		UI.later(() ->
		{
			onGenerate();
		});
	}
	
	
	protected void updateLayout()
	{
		CPanel p = new CPanel();
		p.setGaps(5);
		p.setBorder();
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.PREFERRED,
			CPanel.FILL
		);

		p.row(0, p.label("Alphabet:"));
		p.row(1, alphabetField);
		p.nextRow();
		p.row(0, p.label("Must Include:"));
		p.row(1, 2, includeField);
		p.nextRow();
		p.row(1, uppercaseField);
		p.nextRow();
		p.row(1, lowercaseField);
		p.nextRow();
		p.row(1, digitsField);
		p.nextRow();
		p.row(0, p.label("Length:"));
		p.row(1, lengthField);
//		p.nextRow();
//		p.row(0, 3, new JSeparator(JSeparator.HORIZONTAL));
		p.nextRow();
		p.row(0, p.label("Generated Password:"));
		p.nextRow();
		if(hidePassField.isSelected())
		{
			p.row(0, 3, passwordField);
		}
		else
		{
			p.row(0, 3, clearPassField);	
		}
		p.nextRow();
//		p.row(0, 3, new JSeparator(JSeparator.HORIZONTAL));
		p.row(1, hidePassField);
		
		CButton cancelButton = new CButton(Menus.Cancel, closeDialogAction);
		CButton generateButton = new CButton("Re-generate", generateAction);
		CButton okButton = new CButton("OK", okAction, true); // FIX
		
		p.buttonPanel().addButton(cancelButton);
		p.buttonPanel().addButton(generateButton);
		p.buttonPanel().addButton(okButton);
		
		setCenter(p);
	}
	
	
	protected void onHide()
	{
		updateLayout();
	}
	
	
	protected void updateActions()
	{
		boolean idle = (generator == null);
		boolean hasPassword = (password != null);
		
		generateAction.setEnabled(idle);
		okAction.setEnabled(idle && hasPassword);
	}
	
	
	protected PasswordGenerator.Alphabet getAlphabet()
	{
		String s = CKit.toStringOrNull(alphabetField.getSelectedItem());
		if(s != null)
		{
			switch(s)
			{
			case LATIN:
				return Alphabet.LATIN;
			case CYRILLIC:
				return Alphabet.CYRILLIC;
			}
		}
		return Alphabet.LATIN;
	}
	
	
	protected int getLength()
	{
		// TODO error?
		String s = lengthField.getSelectedString();
		return Parsers.parseInt(s, PasswordGenerator.DEFALT_LENGTH);
	}
	
	
	protected void onGenerate()
	{
		if(generator != null)
		{
			generator.cancel();
		}
		
		PasswordGenerator.Alphabet alphabet = getAlphabet();
		int len = getLength();
		
		generator = new PasswordGenerator(this::onPasswordGenerated);
		generator.setAlphabet(alphabet);
		generator.setUppercase(uppercaseField.isSelected());
		generator.setLowercase(lowercaseField.isSelected());
		generator.setDigits(digitsField.isSelected());
		generator.setMustInclude(includeField.getText());
		generator.setLength(len);
		generator.generate();
				
		updateActions();
	}


	protected void onPasswordGenerated(PasswordGenerator gen, OpaqueChars pw)
	{
		UI.later(() ->
		{
			if(generator != gen)
			{
				return;
			}
			
			generator = null;
			password = pw;
			
			passwordField.setPassword(pw);
			clearPassField.setText(pw);
			
			updateActions();
		});
	}
	
	
	protected void onOK()
	{
		callback.accept(password);
		
		close();
	}
}
