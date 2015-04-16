// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CFocusTraversalPolicy;
import goryachev.common.ui.CMenu;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.CUndoManager;
import goryachev.common.ui.ContactSupport;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.ui.dialogs.CheckForUpdatesDialog;
import goryachev.common.util.CBrowser;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import goryachev.crypto.EntropyGatherer;
import goryachev.crypto.OpaqueChars;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.password.ui.ActivityMonitor;
import goryachev.password.ui.ClipboardHandler;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JMenuBar;


// TODO recentFiles
public class MainWindow
	extends AppFrame
{
	public final CAction aboutAction = new CAction() { public void action() { About.about(MainWindow.this); } };
	public final CAction changeDataPassAction = new CAction() { public void action() { onChangeDatabasePassword(); } };
	public final CAction checkForUpdatesAction = new CAction() { public void action() { actionCheckForUpdates(); } };
	public final CAction clearClipboardAction = new CAction() { public void action() { onClearClipboard(); } };
	public final CAction createDatabaseAction = new CAction() { public void action() { createDatabase(); } };
	public final CAction exitAction = new CAction() { public void action() { onExit(); } };
	public final CAction focusAddAction = new CAction() { public void action() { focusAddButton(); } };
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
	
	public static final String KEY_LAST_FOLDER = "last.folder";
	public final CPanel panel;
	public final ListTab listTab;
	private File file;
	private boolean modified;
	private ActivityMonitor activityMonitor;
	

	public MainWindow()
	{
		super("MainWindow");
		
		activityMonitor = new ActivityMonitor(Preferences.lockTimeoutOption.get())
		{
			protected void onNoActivity()
			{
				lock();
			}
		};
		activityMonitor.start();
		
		setIcon(Application.getIcon());
		setTitle(Application.getTitle());
		
		listTab = new ListTab();
		
		addButton = new CButton(TXT.get("MainWindow.toolbar.add entry", "Add"), listTab.addAction);
		saveButton = new CButton(Menus.Save, saveAction);
		clearButton = new CButton(TXT.get("MainWindow.toolbar.clear clipboard", "Clear"), TXT.get("MainWindow.toolbar.clear clipboard.tooltip", "Clear sensitive data from the clipboard"), clearClipboardAction);
		lockButton = new CButton(TXT.get("MainWindow.toolbar.lock program", "Lock"), TXT.get("MainWindow.toolbar.lock.tooltip", "Lock the program"), lockAction);
		exitButton = new CButton(Menus.Exit, exitAction);
		
		panel = new CPanel();
		panel.setNorth(createToolbar());
		panel.setCenter(listTab);
		panel.setSouth(createStatusBar(true));
		
		setJMenuBar(createMenu());
		
		UI.whenAncestorOfFocusedComponent(panel, KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK, listTab.focusFilterAction); // FIX key stroke
		
		setContent(panel);
		setSize(700, 500);
		
		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
		tp.add(listTab.passPanel.nameField);
		tp.add(listTab.passPanel.usernameField);
		tp.add(listTab.passPanel.copyUserButton);
		tp.add(listTab.passPanel.passField);
		tp.add(listTab.passPanel.copyPassButton);
		tp.add(listTab.table);
		tp.add(addButton);
		tp.add(saveButton);
		tp.add(clearButton);
		tp.add(lockButton);
		tp.add(exitButton);
		tp.apply(this);
		
		UI.whenFocused(listTab.table, KeyEvent.VK_TAB, focusAddAction);
		EntropyGatherer.start();
	}
	

	public void onWindowOpened()
	{
		listTab.actionFocusFilter();
	}

	
	private CToolBar createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.setPreferredSize(null);
		t.setBorder(new CBorder(2, 0, 2, 0));
		
		t.add(addButton);
		t.add(saveButton);
		t.space(10);
		t.add(listTab.filter.getComponent());
		t.fill();
		t.add(clearButton);
		t.add(lockButton);
		t.add(exitButton);
		return t;
	}
	
	
	private JMenuBar createMenu()
	{
		JMenuBar mb = Theme.menubar();
		mb.setBorder(null);
		CMenu m;

		// file
		mb.add(m = new CMenu(Menus.File));
		m.add(new CMenuItem(TXT.get("MainWindow.menu.new data file", "New Database"), createDatabaseAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Open, Accelerators.OPEN, openAction));
		m.add(new CMenuItem(Menus.Save, Accelerators.SAVE, saveAction));
		m.add(new CMenuItem(Menus.SaveAs, Accelerators.SAVE_AS, saveAsAction));
		m.addSeparator();
		m.add(new CMenuItem(TXT.get("MainWindow.menu.lock database", "Lock"), lockAction));
		m.add(new CMenuItem(TXT.get("MainWindow.menu.change database passphrase", "Change Database Passphrase"), changeDataPassAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Preferences, Accelerators.PREFERENCES, preferencesAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Exit, exitAction));
		
		// edit
		mb.add(m = new CMenu(Menus.Edit));
		m.add(new CMenuItem(Menus.Undo, Accelerators.UNDO, CUndoManager.undoAction));
		m.add(new CMenuItem(Menus.Redo, Accelerators.REDO, CUndoManager.redoAction));
		m.addSeparator();
		m.add(new CMenuItem(TXT.get("MainWindow.menu.copy username","Copy User Name to Clipboard"), listTab.passPanel.copyUsernameAction));
		m.add(new CMenuItem(TXT.get("MainWindow.menu.copy password","Copy Password to Clipboard"), listTab.passPanel.copyPasswordAction));
		m.add(new CMenuItem(TXT.get("MainWindow.menu.clear clipboard","Clear Clipboard"), clearClipboardAction));
		m.addSeparator();
		m.add(new CMenuItem(TXT.get("MainWindow.menu.delete an entry","Delete Entry"), listTab.deleteAction));
		
		// help
		mb.add(m = new CMenu(Menus.Help));
		m.add(new CMenuItem(Menus.ContactSupport, ContactSupport.action));
		m.add(new CMenuItem(Menus.CheckForUpdates, checkForUpdatesAction));
		m.addSeparator();
		m.add(new CMenuItem(TXT.get("MainWindow.menu.about passwords", "Mandatory XKCD Reference"), xkcdAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.License, Application.licenseAction()));
		m.add(new CMenuItem(Menus.OpenSourceLicenses, OpenSourceLicenses.action));
		m.add(new CMenuItem(Menus.About, aboutAction));
		
		return mb;
	}
	
	
	public File getFile()
	{
		return file;
	}
	
	
	public void setFile(File f)
	{
		file = f;
		Preferences.dataFileOption.set(f);
	}
	
	
	public void actionPreferences()
	{
		Preferences.openPreferences(this);
	}
	
	
	public boolean onWindowClosing()
	{
		onExit();
		return false;
	}
	
	
	protected void updateTitle()
	{
		SB sb = new SB();
		sb.a(Application.getTitle());
		if(getFile() != null)
		{
			sb.a(" - ");
			sb.a(getFile().getAbsolutePath());
			
			if(isModified())
			{
				sb.a(" *");
			}
		}
		
		setTitle(sb.toString());
	}
	

	public static MainWindow get(Component c)
	{
		return UI.getAncestorOfClass(MainWindow.class, c);
	}
	
	
	public void setModified(boolean on)
	{
		modified = on;
		updateTitle();
	}
	
	
	public boolean isModified()
	{
		return modified;
	}
	
	
	public void refresh(PassEntry en)
	{
		listTab.refresh(en);
	}
	
	
	public void saveIfModified()
	{
		if(isModified())
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
				setModified(false);
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
				CFileChooser fc = new CFileChooser(this, KEY_LAST_FOLDER);
				fc.setDialogType(CFileChooser.SAVE_DIALOG);
				fc.setFileFilter(Styles.createFileFilter());
				fc.setCurrentDirectory(getFile());
				fc.setSelectedFile(getFile());

				File f = fc.openFileChooser();
				if(f != null)
				{
					f = CKit.ensureExtension(f, PasswordSafeApp.EXTENSION);
					
					if(f.exists())
					{
						int rv = Dialogs.choice
						(
							this, 
							TXT.get("MainWindow.save.overwrite file?", "Overwrite?"), 
							TXT.get("MainWindow.save.file exists", "File {0} exists.  Do you want to overwrite it?", f), 
							new String[] 
							{
								Menus.Overwrite, 
								Menus.Cancel
							}
						);
						if(rv != 0)
						{
							return;
						}
					}
					
					setFile(f);
					PasswordSafeApp.save(df, f);
					
					Preferences.dataFileOption.set(f);

					setModified(false);
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
		
		if(isModified())
		{
			displaySaving();
			
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
		
		new LockWindow(f).open();
		
		close();
	}
	
	
	public void openFile()
	{
		saveIfModified();
			
		CFileChooser fc = new CFileChooser(this, KEY_LAST_FOLDER);
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
		setFile(f);
		listTab.setDataFile(df);
		updateTitle();
	}
	
	
	protected void createDatabase()
	{
		saveIfModified();
		new CreateDataFileDialog(this).open();
	}
	
	
	protected void onClearClipboard()
	{
		ClipboardHandler.clear();
	}

	
	protected void onExit()
	{
		GlobalSettings.storeAll();
		
		displaySaving();
		
		UI.later(new Runnable()
		{
			public void run()
			{
				saveIfModified();
				Application.exit();
			}
		});
	}
	
	
	protected void displaySaving()
	{
		JLabel t = new JLabel(TXT.get("MainWindow.saving database", "Saving database..."));
		//t.setIcon(Theme.waitIcon(32));
		t.setHorizontalAlignment(JLabel.CENTER);
		//t.setVerticalTextPosition(JLabel.TOP);
		
		setJMenuBar(null);
		panel.setNorth(null);
		panel.setCenter(t);
		validate();
		repaint();
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
			setModified(true);
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
}
