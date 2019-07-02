// Copyright © 2009-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.i18n.Menus;
import goryachev.common.i18n.TXT;
import goryachev.common.util.CKit;
import goryachev.crypto.OpaqueChars;
import goryachev.password.data.DataFile;
import goryachev.password.data.PassEntry;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.password.licenses.OpenSourceLicenses;
import goryachev.password.ui.ClipboardHandler;
import goryachev.swing.Application;
import goryachev.swing.CAction;
import goryachev.swing.CBorder;
import goryachev.swing.CBrowser;
import goryachev.swing.CButton;
import goryachev.swing.CFocusTraversalPolicy;
import goryachev.swing.CMenuBar;
import goryachev.swing.CPanel;
import goryachev.swing.CToolBar;
import goryachev.swing.CUndoManager;
import goryachev.swing.ContactSupport;
import goryachev.swing.Dialogs;
import goryachev.swing.GlobalSettings;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.XAction;
import goryachev.swing.dialogs.CFileChooser;
import goryachev.swing.dialogs.CheckForUpdatesDialog;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;


public class MainPanel
	extends CPanel
{
	public final CAction aboutAction = new CAction() { public void action() { About.about(MainPanel.this); } };
	public final CAction changeDataPassAction = new CAction() { public void action() { onChangeDatabasePassword(); } };
	public final CAction checkForUpdatesAction = new CAction() { public void action() { actionCheckForUpdates(); } };
	public final CAction clearClipboardAction = new CAction() { public void action() { onClearClipboard(); } };
	public final CAction createDatabaseAction = new CAction() { public void action() { actionCreateDatabase(); } };
	public final XAction exitAction = new XAction(this::onExit);
	public final XAction focusAddAction = new XAction(this::focusAddButton);
	public final CAction lockAction = new CAction() { public void action() { lock(); } };
	public final CAction openAction = new CAction() { public void action() { openFile(); } };
	public final CAction preferencesAction = new CAction(PasswordSafeIcons.Options) { public void action() { actionPreferences(); } };
	public final CAction saveAction = new CAction() { public void action() { saveFile(); } };
	public final CAction saveAsAction = new CAction() { public void action() { saveFileAs(); } };
	public final CAction xkcdAction = new CAction() { public void action() { actionXkcd(); } };
	public final CButton addButton;
	public final CButton saveButton;
	public final CButton clearButton;
	public final CButton lockButton;
	public final CButton exitButton;
	
	public final MainWindow mainWindow;
	public final ListTab listTab;
	

	public MainPanel(MainWindow w)
	{
		mainWindow = w;
		
		listTab = new ListTab();
		
		addButton = new CButton(TXT.get("MainWindow.toolbar.add entry", "Add"), listTab.addEntryAction);
		saveButton = new CButton(Menus.Save, saveAction);
		clearButton = new CButton(TXT.get("MainWindow.toolbar.clear clipboard", "Clear"), TXT.get("MainWindow.toolbar.clear clipboard.tooltip", "Clear sensitive data from the clipboard"), clearClipboardAction);
		lockButton = new CButton(TXT.get("MainWindow.toolbar.lock program", "Lock"), TXT.get("MainWindow.toolbar.lock.tooltip", "Lock the program"), lockAction);
		exitButton = new CButton(Menus.Exit, exitAction);
		
		setNorth(createToolbar());
		setCenter(listTab);
		
		UI.whenAncestorOfFocusedComponent(this, KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK, listTab.focusFilterAction); // FIX key stroke
		
		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
		tp.add(listTab.passEditor.nameField);
		tp.add(listTab.passEditor.usernameField);
		tp.add(listTab.passEditor.copyUserButton);
		tp.add(listTab.passEditor.passField);
		tp.add(listTab.passEditor.copyPassButton);
		tp.add(listTab.table);
		tp.add(addButton);
		tp.add(saveButton);
		tp.add(clearButton);
		tp.add(lockButton);
		tp.add(exitButton);
		tp.apply(this);
		
		UI.whenFocused(listTab.table, KeyEvent.VK_TAB, focusAddAction);
	}
	

	public void onExit()
	{
		mainWindow.onExit();
	}

	
	public CToolBar createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.setPreferredSize(null);
		t.setBorder(new CBorder(1, 0, 0, 0));
		
		t.add(sz(addButton));
		t.add(sz(saveButton));
		t.space(10);
		t.add(listTab.filter.getComponent());
		t.fill();
		t.add(sz(clearButton));
		t.add(sz(lockButton));
		t.add(sz(exitButton));
		return t;
	}
	
	
	protected CButton sz(CButton b)
	{
		Dimension d = b.getPreferredSize();
		int min = Theme.minimumButtonWidth();
		if(d.width < min)
		{
			d.width = min;
		}
		b.setPreferredSize(d);
		return b;
	}
	
	
	public CMenuBar createMenu()
	{
		CMenuBar m = Theme.menubar();
		// file
		m.menu(Menus.File);
		m.item(TXT.get("MainWindow.menu.new data file", "New Database"), createDatabaseAction);
		m.separator();
		m.item(Menus.Open, Accelerators.OPEN, openAction);
		m.item(Menus.Save, Accelerators.SAVE, saveAction);
		m.item(Menus.SaveAs, Accelerators.SAVE_AS, saveAsAction);
		m.separator();
		m.item(TXT.get("MainWindow.menu.lock database", "Lock"), lockAction);
		m.item(TXT.get("MainWindow.menu.change database passphrase", "Change Database Passphrase"), changeDataPassAction);
		m.separator();
		m.item(Menus.Preferences, Accelerators.PREFERENCES, preferencesAction);
		m.separator();
		m.item(Menus.Exit, exitAction);
		// edit
		m.menu(Menus.Edit);
		m.item(Menus.Undo, Accelerators.UNDO, CUndoManager.globalUndoAction);
		m.item(Menus.Redo, Accelerators.REDO, CUndoManager.globalRedoAction);
		m.separator();
		m.item(TXT.get("MainWindow.menu.copy username","Copy User Name to Clipboard"), listTab.passEditor.copyUsernameAction);
		m.item(TXT.get("MainWindow.menu.copy password","Copy Password to Clipboard"), listTab.passEditor.copyPasswordAction);
		m.item(TXT.get("MainWindow.menu.clear clipboard","Clear Clipboard"), clearClipboardAction);
		m.separator();
		m.item(TXT.get("MainWindow.menu.delete an entry","Delete Entry"), listTab.deleteEntryAction);
		// help
		m.menu(Menus.Help);
		m.item(Menus.ContactSupport, ContactSupport.action);
		m.item(Menus.CheckForUpdates, checkForUpdatesAction);
		m.separator();
		m.item(TXT.get("MainWindow.menu.about passwords", "Mandatory XKCD Reference"), xkcdAction);
		m.separator();
		m.item(Menus.License, Application.licenseAction());
		m.item(Menus.OpenSourceLicenses, OpenSourceLicenses.openDialogAction);
		m.item(Menus.About, aboutAction);
		return m;
	}
	
	
	public File getFile()
	{
		return mainWindow.getFile();
	}
	
	
//	public void setFile(File f)
//	{
//		file = f;
//		Preferences.dataFileOption.set(f);
//	}
	
	
	public void actionPreferences()
	{
		Preferences.openPreferences(this);
	}
	

	public static MainPanel get(Component c)
	{
		return UI.getAncestorOfClass(MainPanel.class, c);
	}
	
	
	public void refresh(PassEntry en)
	{
		listTab.refresh(en);
	}
	
	
	public void saveIfModified()
	{
		if(mainWindow.isModified())
		{
			saveFile();
		}
	}
	
	
	public void saveFile()
	{
		try
		{
			DataFile df = listTab.getDataFile();
			if(df != null)
			{
				PasswordSafeApp.save(df, getFile());
				mainWindow.setModified(false);
			}
		}
		catch(Exception e)
		{
			Dialogs.error(this, e);
		}
	}
	
	
	public void saveFileAs()
	{
		try
		{
			DataFile df = listTab.getDataFile();
			if(df != null)
			{
				CFileChooser fc = new CFileChooser(this, MainWindow.KEY_LAST_FOLDER);
				fc.setDialogType(CFileChooser.SAVE_DIALOG);
				fc.setFileFilter(Styles.createFileFilter());
				fc.setCurrentDirectory(getFile());
				fc.setSelectedFile(getFile());

				File f = fc.openFileChooser();
				if(f != null)
				{
					f = CKit.ensureExtension(f, PasswordSafeApp.EXTENSION);
					
					if(Dialogs.checkFileExistsOverwrite(this, f) == false)
					{
						return;
					}
					
					mainWindow.setFile(f);
					PasswordSafeApp.save(df, f);
					
					Preferences.dataFileOption.set(f);

					mainWindow.setModified(false);
				}
			}
		}
		catch(Exception e)
		{
			Dialogs.error(this, e);
		}
	}
	
	
	protected void lock()
	{
		ClipboardHandler.clearConditionally();
		GlobalSettings.storeAll();
		
		if(mainWindow.isModified())
		{
			mainWindow.displaySaving();
			
			UI.later(new Runnable()
			{
				public void run()
				{
					saveFile();			
					lock(getFile());
				}
			});
		}
		else
		{
			lock(getFile());
		}
	}
	
	
	public void lock(File f)
	{
		listTab.purgeSecrets();
		ClipboardHandler.clear();
		
		mainWindow.lock(f);
	}
	
	
	public void openFile()
	{
		saveIfModified();
			
		CFileChooser fc = new CFileChooser(this, MainWindow.KEY_LAST_FOLDER);
		fc.setDialogType(CFileChooser.OPEN_DIALOG);
		fc.setFileFilter(Styles.createFileFilter());
		
		DataFile df = listTab.getDataFile();
		if(df != null)
		{
			fc.setCurrentDirectory(getFile());
			fc.setSelectedFile(getFile());
		}
		
		File f = fc.openFileChooser();
		if(f != null)
		{
			lock(f);
		}
	}
	

	public void setDataFile(File f, DataFile df)
	{
		mainWindow.setFile(f);
		listTab.setDataFile(df);
		mainWindow.updateTitle();
	}
	
	
	protected void actionCreateDatabase()
	{
		saveIfModified();
		new CreateDataFileDialog(this).open();
	}
	
	
	protected void onClearClipboard()
	{
		ClipboardHandler.clear();
	}

	
	protected void onChangeDatabasePassword()
	{
		DataFile df = listTab.getDataFile();
		ChangeDatabasePassphraseDialog d = new ChangeDatabasePassphraseDialog(this, df.getPassword());
		d.open();
		OpaqueChars pass = d.getEnteredPassword(); 
		if(pass != null)
		{
			df.setPassword(pass);
			mainWindow.setModified(true);
		}
	}
	
	
	protected void actionXkcd()
	{
		CBrowser.openLinkQuiet("http://xkcd.com/936/");
	}
	
	
	protected void focusAddButton()
	{
		addButton.requestFocusInWindow();
	}
	
	
	protected void actionCheckForUpdates()
	{
		new CheckForUpdatesDialog(this, PasswordSafeApp.UPDATE_URL + "?menu").open();
	}
	
	
	public void purgeSecrets()
	{
		listTab.purgeSecrets();
	}
}
