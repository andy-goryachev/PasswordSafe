// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CFocusTraversalPolicy;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CProgressField;
import goryachev.common.ui.CTextField;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.InputTracker;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.util.TXT;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.crypto.ui.OnScreenKeyboard;
import goryachev.password.data.DataFile;
import java.awt.event.KeyEvent;
import java.io.File;


public class LockPanel
	extends CPanel
{
	public final CAction browseAction = new CAction() { public void action() { onBrowse(); } };
	public final CAction createAction = new CAction() { public void action() { onCreate(); } };
	public final CAction exitAction = new CAction() { public void action() { onExit(); } };
	public final CAction okAction = new CAction() { public void action() { onOk(); } };
	protected CTextField fileField;
	protected CProgressField progressField;
	private CPasswordField passField;
	protected OnScreenKeyboard keyboard;
	private boolean progress;
	
	
	public LockPanel(File f)
	{
		fileField = new CTextField(f.getAbsolutePath());
		
		passField = new CPasswordField();
		new InputTracker(passField)
		{
			public void onInputEvent()
			{
				updateActions();
			}
		};
		
		keyboard = Styles.createKeyboard();
		
		progressField = new CProgressField(16);
		
		CButton okButton = new CButton(Menus.OK, okAction);
		okButton.setHighlight(Theme.buttonHighlight());
		
		CButton exitButton = new CButton(Menus.Exit, exitAction);
		CButton createButton = new CButton(TXT.get("LockPanel.create new data file", "Create New"), createAction);
		CButton browseButton = new CButton(Menus.Browse, browseAction);
		
		setBorder(10);
		setGaps(10, 5);
		addColumns
		(
			PREFERRED,
			PREFERRED,
			FILL,
			PREFERRED
		);
		setColumnMinimumSize(3, Theme.minimumButtonWidth());
		
		nextRow(10);
		row(1, Styles.logo());
		nextRow(20);
		nextRow();
		row(0, label(TXT.get("LockPanel.open existing file", "Open File:")));
		row(1, 2, fileField);
		row(3, browseButton);
		nextRow();
		row(0, label(TXT.get("LockPanel.passphrase", "Passphrase:")));
		row(1, 3, passField);
		nextRow();
		row(1, keyboard);
		nextRow();
		row(1, 2, progressField);
		
		buttonPanel().addButton(createButton);
		buttonPanel().space(10);
		buttonPanel().addButton(exitButton);
		buttonPanel().addButton(okButton);
		
		UI.whenInFocusedWindow(passField, KeyEvent.VK_ENTER, okAction);
		
		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
		tp.add(passField);
		tp.add(okButton);
		tp.add(exitButton);
		tp.add(createButton);
		tp.add(fileField);
		tp.add(browseButton);
		tp.apply(this);
		
		updateActions();
		
		UI.focusLater(passField);
	}
	
	
	protected void setProgress(boolean on)
	{
		progressField.setAnimation(on);
		progress = on;
		updateActions();
	}
	
	
	protected void updateActions()
	{
		boolean en = !progress;
		boolean hasPassword = passField.hasText();
		
		okAction.setEnabled(en && hasPassword);
		browseAction.setEnabled(en);
		createAction.setEnabled(en);

		fileField.setEditable(en);
		passField.setEditable(en);
		keyboard.setEnabled(en);
	}
	
	
	protected void onOk()
	{
		try
		{
			final File file = new File(fileField.getText());
			if(!file.exists())
			{
				Dialogs.warn
				(
					this, 
					TXT.get("LockPanel.file not found.title", "File Not Found"),
					TXT.get("LockPanel.file not found", "File not found: {0}", file)
				);
				return;
			}
			
			setProgress(true);
			final OpaqueChars pass = passField.getOpaquePassword();
			
			new BackgroundThread("opening")
			{
				private DataFile df;
				
				public void process() throws Throwable
				{
					df = PasswordSafeApp.loadDataFile(file, pass);
				}
	
				public void success()
				{
					setProgress(false);
					open(file, df);
				}
	
				public void onError(Throwable e)
				{
					setProgress(false);
					failedToOpen(e);
				}
			}.start();
		}
		catch(Exception e)
		{
			Dialogs.error(this, e);
		}
	}
	
	
	protected void failedToOpen(Throwable e)
	{
		Dialogs.error(this, TXT.get("LockPanel.unlock error", "Unable to decrypt the database"));
		
		passField.requestFocusInWindow();
		passField.selectAll();
	}
	
	
	protected void open(File file, DataFile df)
	{
		MainWindow w = new MainWindow();
		//w.unlock();
		w.setDataFile(file, df);
		w.open();
		
		AppFrame.getAppFrame(this).close();
	}
	
	
	protected void onExit()
	{
		Application.exit();
	}
	
	
	protected void onBrowse()
	{
		File f = new File(fileField.getText());
		
		CFileChooser fc = new CFileChooser(this, null);
		fc.setDialogType(CFileChooser.OPEN_DIALOG);
		fc.setCurrentDirectory(f.getParentFile());
		fc.setSelectedFile(f);
		fc.setFileFilter(Styles.createFileFilter());
		f = fc.openFileChooser();
		if(f != null)
		{
			fileField.setText(f.getAbsolutePath());
			passField.setText(null);
			passField.requestFocusInWindow();
		}
	}
	
	
	public void focus()
	{
		passField.requestFocusInWindow();
	}
	
	
	protected void onCreate()
	{
		CreateDataFileDialog d = new CreateDataFileDialog(this);
		d.open();
		if(d.isCreated())
		{
			fileField.setText(d.getFile().getAbsolutePath());
			UI.focusLater(passField);
		}
	}
}
