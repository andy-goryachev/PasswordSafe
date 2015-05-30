// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CFocusTraversalPolicy;
import goryachev.common.ui.CPanel3;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CTextField;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.util.TXT;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.crypto.ui.MatchLabel;
import goryachev.crypto.ui.OnScreenKeyboard;
import goryachev.crypto.ui.SecureTextField;
import goryachev.password.prompts.Tx;
import goryachev.password.ui.PasswordVerifier2;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class AddEntryDialog extends CDialog
{
	public final CAction addAction = new CAction() { public void action() { onAdd(); } };
	protected CTextField nameField;
	protected CTextField usernameField;
	protected CPasswordField passField;
	protected SecureTextField clearPassField;
	protected CPasswordField verifyField;
	protected MatchLabel matchField;
	protected JCheckBox hidePassField;
	protected final PasswordVerifier2 verifier;
	protected JTextArea notesField;
	protected CScrollPane scroll;
	protected JLabel notesLabel;
	private OnScreenKeyboard keyboard;
	private DataFile dataFile;
	private PassEntry entry;
	private boolean showPassword;


	public AddEntryDialog(JComponent parent, DataFile df)
	{
		super(parent, "AddEntryDialog", true);

		this.dataFile = df;

		setTitle(Tx.AddEntry);
		setMinimumSize(500, 400);

		nameField = new CTextField();

		usernameField = new CTextField();

		clearPassField = new SecureTextField();

		passField = new CPasswordField();

		verifyField = new CPasswordField();

		matchField = new MatchLabel();

		hidePassField = new JCheckBox(Tx.HidePasswordInThisDialog);
		hidePassField.setBorder(null);
		hidePassField.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				onHide();
			}
		});

		verifier = new PasswordVerifier2(clearPassField, passField, verifyField, hidePassField, matchField)
		{
			protected void onPasswordsMatch(boolean have, boolean match)
			{
				// FIX allow empty password
				addAction.setEnabled(have && match);
			}
		};

		keyboard = Styles.createKeyboard();

		notesField = new JTextArea();
		notesField.setWrapStyleWord(true);
		notesField.setLineWrap(true);
		notesField.setFont(Theme.plainFont());
		notesField.setBorder(Theme.BORDER_FIELD);

		scroll = new CScrollPane(notesField, false);

		notesLabel = new JLabel(TXT.get("AddEntryDialog.label.notes", "Notes:"));
		notesLabel.setVerticalAlignment(JLabel.TOP);
		notesLabel.setHorizontalAlignment(JLabel.RIGHT);

		setShowPassword(true);
		hidePassField.setSelected(false);

		addAction.setEnabled(true);
	}


	protected void setShowPassword(boolean on)
	{
		this.showPassword = on;

		CPanel3 p = new CPanel3();
		p.setGaps(5);
		p.setBorder(new CBorder(0, 0, 10, 0));
		p.addColumns(CPanel3.PREFERRED, CPanel3.PREFERRED, CPanel3.FILL, CPanel3.PREFERRED);

		p.row(0, p.label(TXT.get("AddEntryDialog.label.name", "Name:")));
		p.row(1, 3, nameField);
		p.nextRow();
		p.row(0, p.label(TXT.get("AddEntryDialog.label.user", "User name:")));
		p.row(1, 3, usernameField);
		p.nextRow();
		p.row(0, p.label(TXT.get("AddEntryDialog.label.password", "Password:")));
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
			p.row(0, p.label(TXT.get("AddEntryDialog.label.verify password", "Verify:")));
			p.row(1, 2, verifyField);
			p.row(3, matchField);
		}
		p.nextRow();
		p.row(1, keyboard);
		p.nextRow();
		p.row(1, 3, hidePassField);
		p.nextFillRow();
		p.row(0, notesLabel);
		p.row(1, 4, scroll);

		CButton addButton = new CButton(Menus.OK, addAction, true);
		CButton cancelButton = new CButton(Menus.Cancel, closeAction);

		p.buttonPanel().addButton(cancelButton);
		p.buttonPanel().addButton(addButton);

		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
		tp.add(nameField);
		tp.add(usernameField);
		tp.add(passField);
		if(!showPassword)
		{
			tp.add(verifyField);
		}
		tp.add(notesField);
		if(!showPassword)
		{
			tp.add(hidePassField);
		}
		tp.add(addButton);
		tp.add(cancelButton);
		tp.apply(this);

		setContent(p);

		validate();
		repaint();
	}


	protected void onHide()
	{
		boolean on = !hidePassField.isSelected();
		setShowPassword(on);
		// TODO carry password
	}


	public void focus()
	{
		nameField.requestFocusInWindow();
	}


	protected void onAdd()
	{
		try
		{
			OpaqueChars pass = verifier.getPassword();

			entry = dataFile.addEntry();
			entry.setName(nameField.getText());
			entry.setUserName(usernameField.getText());
			entry.setPassword(pass);
			entry.setNotes(notesField.getText());

			MainWindow w = MainWindow.get(this);
			w.setModified(true);
			close();
		}
		catch(Exception e)
		{
			Dialogs.error(this, e.getMessage());
		}
	}


	public PassEntry openDialog()
	{
		open();
		return entry;
	}
}
