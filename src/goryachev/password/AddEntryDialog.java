// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.util.CKit;
import goryachev.crypto.OpaqueChars;
import goryachev.cryptoswing.CPasswordField;
import goryachev.cryptoswing.MatchLabel;
import goryachev.cryptoswing.OnScreenKeyboard;
import goryachev.cryptoswing.SecureTextField;
import goryachev.i18n.Menus;
import goryachev.i18n.TXT;
import goryachev.password.data.DataFile;
import goryachev.password.data.PassEntry;
import goryachev.password.prompts.Tx;
import goryachev.password.ui.PasswordVerifier2;
import goryachev.swing.CBorder;
import goryachev.swing.CButton;
import goryachev.swing.CCheckBox;
import goryachev.swing.CDialog;
import goryachev.swing.CFocusTraversalPolicy;
import goryachev.swing.CPanel;
import goryachev.swing.CScrollPane;
import goryachev.swing.CTextArea;
import goryachev.swing.CTextField;
import goryachev.swing.Dialogs;
import goryachev.swing.GlobalSettings;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.XAction;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComponent;
import javax.swing.JLabel;


public class AddEntryDialog
	extends CDialog
{
	public final XAction addAction = new XAction(this::onAdd);
	public final XAction generateAction = new XAction(this::onGeneratePassword);
	protected final CTextField nameField;
	protected final CTextField usernameField;
	protected final SecureTextField clearPassField;
	protected final CPasswordField passField;
	protected final CPasswordField verifyField;
	protected final MatchLabel matchField;
	protected final CCheckBox hidePassField;
	protected final PasswordVerifier2 verifier;
	protected final CTextArea notesField;
	protected final CScrollPane scroll;
	protected final JLabel notesLabel;
	private final OnScreenKeyboard keyboard;
	private DataFile dataFile;
	private PassEntry entry;
	private boolean showPassword;
	protected final CButton cancelButton = new CButton(Menus.Cancel, closeDialogAction);
	protected final CButton addButton = new CButton("Add", addAction, true);
	protected final CButton generateButton = new CButton("Generate Password", generateAction);


	public AddEntryDialog(JComponent parent, DataFile df)
	{
		super(parent, "AddEntryDialog", true);

		this.dataFile = df;

		setTitle(Tx.AddEntry);
		setMinimumSize(700, 470);

		nameField = new CTextField();

		usernameField = new CTextField();

		clearPassField = new SecureTextField();
		UI.installDefaultPopupMenu(clearPassField);

		passField = new CPasswordField();

		verifyField = new CPasswordField();

		matchField = new MatchLabel();

		hidePassField = new CCheckBox(Tx.HidePasswordInThisDialog);
		GlobalSettings.setKey(hidePassField, "AddEntryDialog.hide");
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
			protected void onPasswordUpdate()
			{
				updateActions();
			}
		};

		keyboard = Styles.createKeyboard();

		notesField = new CTextArea();
		notesField.setWrapStyleWord(true);
		notesField.setLineWrap(true);
		notesField.setFont(Theme.monospacedFont());

		scroll = new CScrollPane(notesField, false);
		scroll.setBorder(Theme.fieldBorder());

		notesLabel = new JLabel(TXT.get("AddEntryDialog.label.notes", "Notes:"));
		notesLabel.setVerticalAlignment(JLabel.TOP);
		notesLabel.setHorizontalAlignment(JLabel.RIGHT);

		setShowPassword(true);
		hidePassField.setSelected(false);
		
		updateActions();
	}
	
	
	protected void updateActions()
	{
		boolean on;
		if(verifier.hasData())
		{
			on = verifier.hasMatch();
		}
		else
		{
			on = true;
		}
		
		addAction.setEnabled(on); 
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
		p.setBorder();
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.PREFERRED,
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED
		);

		p.row(0, p.label(TXT.get("AddEntryDialog.label.name", "Name:")));
		p.row(1, 4, nameField);
		p.nextRow();
		p.row(0, p.label(TXT.get("AddEntryDialog.label.user", "User name:")));
		p.row(1, 4, usernameField);
		p.nextRow();
		p.row(0, p.label(TXT.get("AddEntryDialog.label.password", "Password:")));
		if(showPassword)
		{
			p.row(1, 4, clearPassField);
		}
		else
		{
			p.row(1, 3, passField);
			p.nextRow();
			p.row(0, p.label(TXT.get("AddEntryDialog.label.verify password", "Verify:")));
			p.row(1, 3, verifyField);
			p.row(4, matchField);
		}
		
		p.nextRow();
		p.row(1, 2, keyboard);
		
		p.nextRow();
//		p.row(1, generateButton);
		p.row(1, 4, hidePassField);
		
		p.nextFillRow();
		p.row(0, notesLabel);
		p.row(1, 4, scroll);

		// focus
		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
		tp.add(nameField);
		tp.add(usernameField);
		tp.add(passField);
		if(!showPassword)
		{
			tp.add(verifyField);
		}
		tp.add(generateButton);
		tp.add(notesField);
		if(!showPassword)
		{
			tp.add(hidePassField);
		}
		tp.add(addButton);
		tp.add(cancelButton);
		tp.apply(this);

		// buttons
		p.buttonPanel().setBorder(new CBorder(5, 0, 0, 0));
		p.buttonPanel().addButton(generateButton);
		p.buttonPanel().fill();
		p.buttonPanel().addButton(cancelButton);
		p.buttonPanel().addButton(addButton);

		setCenter(p);
		
		UI.validateAndRepaint(this);
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
			String name = nameField.getText();
			if(CKit.isBlank(name))
			{
				name = TXT.get("AddEntryDialog.unnamed entry", "<Unnamed>");
			}
			
			OpaqueChars pass = verifier.getPassword();

			entry = dataFile.addEntry();
			entry.setName(name);
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
	
	
	protected void onGeneratePassword()
	{
		boolean hidePass = hidePassField.isSelected();
		new GeneratePasswordDialog(this, hidePass, this::onPasswordGenerated).open();
	}
	
	
	protected void onPasswordGenerated(OpaqueChars pw)
	{
		clearPassField.setText(pw);
		passField.setPassword(pw);
		verifyField.setPassword(pw);
	}
}
