// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.util.CKit;
import goryachev.i18n.Menus;
import goryachev.i18n.TXT;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.memsafecryptoswing.CPasswordField;
import goryachev.memsafecryptoswing.OnScreenKeyboard;
import goryachev.password.data.DataFile;
import goryachev.password.data.DataIO;
import goryachev.swing.Application;
import goryachev.swing.BackgroundThread;
import goryachev.swing.CButton;
import goryachev.swing.CFocusTraversalPolicy;
import goryachev.swing.CPanel;
import goryachev.swing.CProgressField;
import goryachev.swing.CTextField;
import goryachev.swing.Dialogs;
import goryachev.swing.InputTracker;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.XAction;
import goryachev.swing.dialogs.CFileChooser;
import java.awt.event.KeyEvent;
import java.io.File;


public class LockPanel
	extends CPanel
{
	public final XAction browseAction = new XAction(this::onBrowse);
	public final XAction createAction = new XAction(this::onCreate);
	public final XAction exitAction = new XAction(this::onExit);
	public final XAction okAction = new XAction(this::onOk);
	protected final MainWindow mainWindow;
	protected CTextField fileField;
	protected CProgressField progressField;
	private CPasswordField passField;
	protected OnScreenKeyboard keyboard;
	private boolean progress;
	
	
	public LockPanel(MainWindow w, File f)
	{
		this.mainWindow = w;
			
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
		okButton.setHighlight(Theme.AFFIRM_BUTTON_COLOR);
		
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
		UI.whenAncestorOfFocusedComponent(this, KeyEvent.VK_ESCAPE, mainWindow.closeAction);
		
		UI.later(() ->
		{
			checkDebugPassword();
		});
	}
	
	
	protected final void checkDebugPassword()
	{
		String pw = System.getProperty("password");
		if(CKit.isNotBlank(pw))
		{
			System.setProperty("password", "");
			
			if(pw.length() <= 1)
			{
				char[] cs = pw.toCharArray();
				passField.setPassword(new OpaqueChars(cs));
				onOk();
			}
		}
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
					df = DataIO.loadDataFile(file, pass);
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
		mainWindow.unlock(file, df);
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
