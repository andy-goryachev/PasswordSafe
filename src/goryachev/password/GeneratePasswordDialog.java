// Copyright © 2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.cryptoswing.CPasswordField;
import goryachev.i18n.Menus;
import goryachev.password.prompts.Tx;
import goryachev.swing.CButton;
import goryachev.swing.CCheckBox;
import goryachev.swing.CComboBox;
import goryachev.swing.CDialog;
import goryachev.swing.CPanel;
import goryachev.swing.CTextField;
import goryachev.swing.XAction;
import java.awt.Component;
import javax.swing.JSeparator;


/**
 * Generate Password Dialog.
 */
public class GeneratePasswordDialog
	extends CDialog
{
	protected final String LATIN = "Latin";
	protected final String CYRILLIC = "Cyrillic";
	
	protected final XAction generateAction = new XAction(this::onGenerate);
	
	protected final CComboBox alphabetField;
	protected final CTextField includeField = new CTextField();
	protected final CCheckBox uppercaseField = new CCheckBox("uppercase");
	protected final CCheckBox lowercaseField = new CCheckBox("lowercase");
	protected final CCheckBox digitsField = new CCheckBox("digits");
	protected final CComboBox lengthField;
	protected final CPasswordField passwordField = new CPasswordField();
	protected final CCheckBox showField = new CCheckBox("show password");
	
	
	// TODO position under or over the parent, or cascade
	public GeneratePasswordDialog(Component parent)
	{
		super(parent, "GeneratePasswordDialog", true);

		setTitle(Tx.GeneratePassword);
		setCloseOnEscape();
		setMinimumSize(600, 400);
		setSize(600, 400);
		
		alphabetField = new CComboBox
		(
			LATIN,
			CYRILLIC
		);
		
		lowercaseField.setSelected(true);
		
		showField.setSelected(true);
		
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
		p.row(0, p.label("Include Symbols:"));
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
		p.row(0, 3, passwordField);
		p.nextRow();
//		p.row(0, 3, new JSeparator(JSeparator.HORIZONTAL));
		p.row(1, showField);
		
		CButton cancelButton = new CButton(Menus.Cancel, closeDialogAction);
		CButton generateButton = new CButton("Generate", generateAction);
		CButton okButton = new CButton("OK", generateAction, true); // FIX
		
		p.buttonPanel().addButton(cancelButton);
		p.buttonPanel().addButton(generateButton);
		p.buttonPanel().addButton(okButton);
		
		setCenter(p);
	}
	
	
	protected void onGenerate()
	{
		// TODO
	}
}
