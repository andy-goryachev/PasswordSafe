// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CButtonPanel;
import goryachev.common.ui.CFocusTraversalPolicy;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CProgressField;
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
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JTextField;


public class LockPanel
	extends CPanel
{
	public final CAction browseAction = new CAction() { public void action() { onBrowse(); } };
	public final CAction createAction = new CAction() { public void action() { onCreate(); } };
	public final CAction exitAction = new CAction() { public void action() { onExit(); } };
	public final CAction okAction = new CAction() { public void action() { onOk(); } };
	protected JTextField fileField;
	protected CProgressField progressField;
	private CPasswordField passField;
	protected OnScreenKeyboard keyboard;
	private boolean progress;
	
	
	public LockPanel(File f)
	{
		fileField = new JTextField(f.getAbsolutePath());
		
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
		
		CButtonPanel bp = new CButtonPanel(10, createButton, exitButton, okButton);
		
		setBorder(new CBorder(20));
		setLayout
		(
			new double[]
			{
				PREFERRED,
				PREFERRED,
				FILL,
				PREFERRED,
				PREFERRED
			},
			new double[]
			{
				PREFERRED,
				20,
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED
			},
			10, 5
		);
		
		int ix = 0;
		add(1, ix, Styles.logo());
		ix++;
		ix++;
		add(0, ix, label(TXT.get("LockPanel.open existing file", "Open File:")));
		add(1, ix, 2, ix, fileField);
		add(3, ix, browseButton);
		ix++;
		add(0, ix, label(TXT.get("LockPanel.passphrase", "Passphrase:")));
		add(1, ix, 3, ix, passField);
		ix++;
		add(1, ix, keyboard);
		ix++;
		add(1, ix, 2, ix, progressField);
		ix++;
		add(1, ix, 3, ix, bp);
		
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
	}
	
	
	protected void onAddNotify()
	{
		passField.requestFocusInWindow();
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
