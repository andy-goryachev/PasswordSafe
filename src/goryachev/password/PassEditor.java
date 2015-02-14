// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CUndoManager;
import goryachev.common.ui.HorizontalLayoutPanel;
import goryachev.common.ui.InputTracker;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.password.ui.ClipboardHandler;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class PassEditor
	extends CPanel
{
	public final CAction copyPasswordAction = new CAction() { public void action() { copyPassword(); } };
	public final CAction copyUsernameAction = new CAction() { public void action() { copyUsername(); } };
	public final CAction editPasswordAction = new CAction() { public void action() { editPassword(); } };
	public final JTextField nameField;
	public final JTextField usernameField;
	public final CButton copyUserButton;
	public final CPasswordField passField;
	public final CButton copyPassButton;
	public final CButton editPassButton;
	public final JTextArea notesField;
	private PassEntry entry;
	protected InputTracker tracker;

	
	public PassEditor()
	{
		tracker = new InputTracker()
		{
			public void onInputEvent()
			{
				refresh();
			}
		};
		
		nameField = new JTextField();
		tracker.add(nameField);
		
		usernameField = new JTextField();
		tracker.add(usernameField);
		
		passField = new CPasswordField();
		passField.setEditable(false);
		
		notesField = new JTextArea();
		notesField.setFont(Theme.monospacedFont());
		notesField.setBorder(Theme.BORDER_FIELD);
		notesField.setWrapStyleWord(true);
		notesField.setLineWrap(true);
		tracker.add(notesField);
		
		CUndoManager.monitor
		(
			nameField,
			usernameField,
			notesField
		);
		
		copyUserButton = new CButton(TXT.get("PassEditor.button.copy username", "Copy User Name"), TXT.get("PassEditor.button tooltip.copy u", "Copy username to clipboard"), copyUsernameAction);
		copyUserButton.setHighlight(new Color(0x8500c8));
		
		copyPassButton = new CButton(TXT.get("PassEditor.button.copy password", "Copy Password"), TXT.get("PassEditor.button tooltip.copy p", "Copy password to clipboard"), copyPasswordAction);
		copyPassButton.setHighlight(new Color(0x00cc00));
		
		editPassButton = new CButton(TXT.get("PassEditor.button.change password", "Change Password"), TXT.get("PassEditor.button tooltip.change password", "Change password"), editPasswordAction);

		CScrollPane scroll = new CScrollPane(notesField, false);
		
		HorizontalLayoutPanel bp = new HorizontalLayoutPanel();
		bp.add(editPassButton);
		
		setLayout
		(
			new double[]
			{
				PREFERRED,
				FILL,
				PREFERRED,
				PREFERRED
			},
			new double[]
			{
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED,
				FILL
			},
			10, 5
		);
		
		JLabel la;
		int ix = 0;
		add(0, ix, label(TXT.get("PassEditor.label.name", "Name:")));
		add(1, ix, 3, ix, nameField);
		ix++;
		add(0, ix, label(TXT.get("PassEditor.label.user", "User name:")));
		add(1, ix, 2, ix, usernameField);
		add(3, ix, copyUserButton);
		ix++;
		add(0, ix, label(TXT.get("PassEditor.label.pw", "Password:")));
		add(1, ix, 2, ix, passField);
		add(3, ix, copyPassButton);
		ix++;
		add(1, ix, bp);
		ix++;
		add(0, ix, la = new JLabel(TXT.get("PassEditor.label.notes", "Notes:")));
		ix++;
		add(0, ix, 3, ix, scroll);
		la.setVerticalAlignment(JLabel.TOP);
		
		setBorder(new CBorder(5, 5, 0, 5));
		
		updateActions();
	}
	
	
	public void setEntry(PassEntry en)
	{
		this.entry = en;
		
		if(entry != null)
		{
			tracker.setEnabled(false);
			
			nameField.setText(en.getName());
			usernameField.setText(en.getUsername());
			passField.setText("***************");
			
			notesField.setText(en.getNotes());
			notesField.setCaretPosition(0);
			
			tracker.setEnabled(true);
		}
		
		CUndoManager.clear
		(
			nameField,
			usernameField,
			notesField
		);
		
		updateActions();
	}
	
	
	protected void updateActions()
	{
		boolean on = (entry != null);
		copyPasswordAction.setEnabled(on);
		copyUsernameAction.setEnabled(on);
	}
	
	
	protected void refresh()
	{
		entry.setName(nameField.getText());
		entry.setUserName(usernameField.getText());
		entry.setNotes(notesField.getText());
		modified();
	}
	
	
	public void modified()
	{
		MainWindow w = MainWindow.get(this);
		w.setModified(true);
		w.refresh(entry);
	}
	
	
	protected void copyPassword()
	{
		if(entry != null)
		{
			char[] cs = null;
			String s = null;
			try
			{
				OpaqueChars op = entry.getPassword();
				if(op == null)
				{
					ClipboardHandler.copy("");
					UI.beep();
					return;
				}
				else
				{
					cs = op.getChars();
	
					// unavoidable - can't copy to clipboard without converting to a string
					s = new String(cs); 
					ClipboardHandler.copy(s);
				}
			}
			catch(Exception e)
			{
				Log.err(e);
			}
			finally
			{
				Crypto.zero(cs);
				s = null;
			}
		}
	}
	
	
	protected void editPassword()
	{
		ChangePasswordDialog d = new ChangePasswordDialog(this);
		d.open();
		OpaqueChars pass = d.getEnteredPassword();
		if(pass != null)
		{
			entry.setPassword(pass);
			modified();
		}
	}
	
	
	protected void copyUsername()
	{
		ClipboardHandler.copy(usernameField.getText());
	}
	

	public void focus()
	{
		nameField.requestFocusInWindow();
	}
}
