// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.i18n.Menus;
import goryachev.common.i18n.TXT;
import goryachev.common.util.Log;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.swing.CPasswordField;
import goryachev.password.data.PassEntry;
import goryachev.password.ui.ClipboardHandler;
import goryachev.swing.CAction;
import goryachev.swing.CBorder;
import goryachev.swing.CButton;
import goryachev.swing.CMenuItem;
import goryachev.swing.CPanel;
import goryachev.swing.CPopupMenu;
import goryachev.swing.CPopupMenuController;
import goryachev.swing.CScrollPane;
import goryachev.swing.CTextArea;
import goryachev.swing.CTextField;
import goryachev.swing.CUndoManager;
import goryachev.swing.InputTracker;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.text.CEditorKit;
import java.awt.Color;
import javax.swing.JPopupMenu;


public class PassEditor
	extends CPanel
{
	public final CAction changePasswordAction = new CAction() { public void action() { actionChangePassword(); } };
	public final CAction copyPasswordAction = new CAction() { public void action() { actionCopyPassword(); } };
	public final CAction copyUsernameAction = new CAction() { public void action() { actionCopyUsername(); } };
	public final CTextField nameField;
	public final CTextField usernameField;
	public final CButton copyUserButton;
	public final CPasswordField passField;
	public final CButton copyPassButton;
	public final CButton editPassButton;
	public final CTextArea notesField;
	public final CScrollPane scroll;
	public final Color userColor = new Color(0x8500c8);
	public final Color passColor = new Color(0x00cc00);
	protected final ListTab parent;
	private PassEntry entry;
	protected InputTracker tracker;

	
	public PassEditor(ListTab p)
	{
		this.parent = p;
		
		tracker = new InputTracker()
		{
			public void onInputEvent()
			{
				refresh();
			}
		};
		
		nameField = new CTextField();
		tracker.add(nameField);
		
		usernameField = new CTextField();
		tracker.add(usernameField);
		new CPopupMenuController(usernameField)
		{
			public JPopupMenu constructPopupMenu()
			{
				return createUserFieldPopup();
			}
		};
		
		passField = new CPasswordField();
		passField.setEditable(false);
		new CPopupMenuController(passField)
		{
			public JPopupMenu constructPopupMenu()
			{
				return createPassFieldPopup();
			}
		};
		
		notesField = new CTextArea();
		notesField.setFont(Theme.monospacedFont());
		notesField.setWrapStyleWord(true);
		notesField.setLineWrap(true);
		tracker.add(notesField);
		
		CUndoManager.monitor
		(
			nameField,
			usernameField,
			notesField
		);
		
		copyUserButton = new CButton(TXT.get("PassEditor.button.copy username", "Copy User Name"), TXT.get("PassEditor.button tooltip.copy u", "Copy username to clipboard"), copyUsernameAction, userColor);
		
		copyPassButton = new CButton(TXT.get("PassEditor.button.copy password", "Copy Password"), TXT.get("PassEditor.button tooltip.copy p", "Copy password to clipboard"), copyPasswordAction, passColor);
		
		editPassButton = new CButton(TXT.get("PassEditor.button.change password", "Change Password"), TXT.get("PassEditor.button tooltip.change password", "Change password"), changePasswordAction);

		scroll = new CScrollPane(notesField, false);
		scroll.setBorder(Theme.fieldBorder());
		
		setGaps(5, 5);
		addColumns(PREFERRED, PREFERRED, FILL, PREFERRED, PREFERRED);
		
		row(0, label(TXT.get("PassEditor.label.name", "Name:")));
		row(1, 4, nameField);
		nextRow();
		row(0, label(TXT.get("PassEditor.label.user", "User name:")));
		row(1, 3, usernameField);
		row(4, copyUserButton);
		nextRow();
		row(0, label(TXT.get("PassEditor.label.pw", "Password:")));
		row(1, 3, passField);
		row(4, copyPassButton);
		nextRow();
		row(1, editPassButton);
		nextFillRow();
		row(0, labelTopAligned(TXT.get("PassEditor.label.notes", "Notes:")));
		row(1, 4, scroll);
		
		setBorder(new CBorder(5, 5, 0, 5));
		
		updateActions();
	}


	protected JPopupMenu createUserFieldPopup()
	{
		CPopupMenu m = new CPopupMenu();
		m.add(new CMenuItem(copyUserButton.getText(), copyUsernameAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Cut, CEditorKit.cutAction));
		m.add(new CMenuItem(Menus.Copy, CEditorKit.copyAction));
		m.add(new CMenuItem(Menus.Paste, CEditorKit.pasteAction));
		return m;
	}
	
	
	protected JPopupMenu createPassFieldPopup()
	{
		CPopupMenu m = new CPopupMenu();
		m.add(new CMenuItem(copyPassButton.getText(), copyUsernameAction));
		return m;
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
		parent.refresh(entry);
	}
	
	
	protected void actionCopyPassword()
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
				Log.ex(e);
			}
			finally
			{
				Crypto.zero(cs);
				s = null;
			}
		}
	}
	
	
	protected void actionChangePassword()
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
	
	
	protected void actionCopyUsername()
	{
		ClipboardHandler.copy(usernameField.getText());
	}
	

	public void focus()
	{
		nameField.requestFocusInWindow();
	}
}
