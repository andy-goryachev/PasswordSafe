// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.crypto.OpaqueChars;
import goryachev.cryptoswing.CPasswordField;
import goryachev.cryptoswing.MatchLabel;
import goryachev.cryptoswing.OnScreenKeyboard;
import goryachev.i18n.Menus;
import goryachev.i18n.TXT;
import goryachev.password.data.DataFile;
import goryachev.password.ui.PasswordVerifier2;
import goryachev.swing.BackgroundThread;
import goryachev.swing.CAction;
import goryachev.swing.CButton;
import goryachev.swing.CDialog;
import goryachev.swing.CFocusTraversalPolicy;
import goryachev.swing.CPanel;
import goryachev.swing.CProgressField;
import goryachev.swing.CTextField;
import goryachev.swing.Dialogs;
import goryachev.swing.InfoField;
import goryachev.swing.Theme;
import goryachev.swing.dialogs.CFileChooser;
import java.awt.Component;
import java.io.File;


public class CreateDataFileDialog
	extends CDialog
{
	protected static final Log log = Log.get("CreateDataFileDialog");
	public final CAction createAction = new CAction() { public void action() { actionCreate(); } };
	public final CAction browseAction = new CAction() { public void action() { actionBrowse(); } };
	private final CTextField fileField;
	private final CPasswordField passField;
	private final CPasswordField verifyField;
	private final MatchLabel matchField;
	private final PasswordVerifier2 verifier;
	private final OnScreenKeyboard keyboard;
	private final CProgressField progressField;
	protected boolean created;
	protected boolean progress;
	
	
	public CreateDataFileDialog(Component parent)
	{
		super(parent, "CreateDataFileDialog", true);
		
		setTitle(TXT.get("CreateDataFileDialog.title.create file", "Create Password Database"));
		setMinimumSize(750, 450);
		
		fileField = new CTextField();
		
		passField = new CPasswordField();

		verifyField = new CPasswordField();
		
		matchField = new MatchLabel();

		verifier = new PasswordVerifier2(null, passField, verifyField, null, matchField)
		{
			protected void onPasswordUpdate()
			{
				updateActions();
			}
		};
		
		progressField = new CProgressField(16);
		
		keyboard = Styles.createKeyboard();
		
		CButton browseButton = new CButton(Menus.Browse, browseAction);
		CButton okButton = new CButton(TXT.get("CreateDataFileDialog.button.create file", "Create"), createAction, true);
		CButton cancelButton = new CButton(Menus.Cancel, closeDialogAction);
		
		CPanel p = panel();
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED
		);
		p.setColumnMinimumSize(3, Theme.minimumButtonWidth());

		p.setGaps(10, 5);
		p.row(1, 2, createInfoField());
		p.nextRow(10);
		p.nextRow();
		p.row(0, p.label(TXT.get("CreateDataFileDialog.file", "File:")));
		p.row(1, 2, fileField);
		p.row(3, browseButton);
		p.nextRow();
		p.row(0, p.label(TXT.get("CreateDataFileDialog.label.passphrase", "Passphrase:")));
		p.row(1, 2, passField);
		p.nextRow();
		p.row(0, p.label(TXT.get("CreateDataFileDialog.label.verify password", "Verify:")));
		p.row(1, 2, verifyField);
		p.row(3, matchField);
		p.nextRow();
		p.row(1, keyboard);
		p.nextRow();
		p.add(1, 2, progressField);
	
		p.buttonPanel().addButton(cancelButton);
		p.buttonPanel().addButton(okButton);
	
		String path = PasswordSafeApp.getDefaultDataFile().getAbsolutePath();
		fileField.setText(path);

		CFocusTraversalPolicy tp = new CFocusTraversalPolicy();
		tp.add(fileField);
		tp.add(browseButton);
		tp.add(passField);
		tp.add(verifyField);
		tp.add(okButton);
		tp.add(cancelButton);
		if(CKit.isBlank(path))
		{
			tp.setDefaultComponent(fileField);
		}
		else
		{
			tp.setDefaultComponent(passField);
		}
		tp.apply(this);
		
		verifier.verify();	
		
		updateActions();
	}


	public boolean closeOnEscape()
	{
		return false;
	}


	public void onWindowOpened()
	{
		passField.requestFocusInWindow();
	}


	private InfoField createInfoField()
	{
		SB sb = new SB();

		sb.a("<html>");
		sb.a(TXT.get("CreateDataFileDialog.info.1", "Please enter a passphrase for this file."));
		sb.a("<br><br>");
		sb.a(TXT.get("CreateDataFileDialog.info.2", "It is important that you choose a good, long passphrase &mdash; the longer, the better."));
		sb.a("  ");
		sb.a(TXT.get("CreateDataFileDialog.info.3", "You should avoid choosing one that contains only a single word that can be found in the dictionary, or a combination of a few such words."));
		sb.a("  ");
		sb.a(TXT.get("CreateDataFileDialog.info.4", "It is better to contain words that are unlikely to be seen together in an existing text, or contain words with unusual combination of upper and lower case letters, numbers, or special characters."));
		sb.a("<br><br>");
		sb.a(TXT.get("CreateDataFileDialog.info.5", "You may also use onscreen keyboard to thwart key loggers that might be present on your system."));
		
		return new InfoField(sb.toString());
	}
	
	
	protected void setProgress(boolean on)
	{
		progress = on;
		updateActions();
	}
	
	
	protected void updateActions()
	{
		createAction.setEnabled(verifier.hasData() && verifier.hasMatch());

		progressField.setAnimation(progress);
		
		boolean en = !progress;
		createAction.setEnabled(en && verifier.hasData() && verifier.hasMatch());
		browseAction.setEnabled(en);
		fileField.setEditable(en);
		passField.setEditable(en);
		verifyField.setEditable(en);
	}
	
	
	public File getFile()
	{
		String fname = fileField.getText();
		return new File(fname);
	}
	
	
	public boolean isCreated()
	{
		return created;
	}
	

	protected void actionCreate()
	{
		try
		{
			final File f = getFile();
			if(f.exists())
			{
				if(f.isDirectory())
				{
					Dialogs.error(this, "File exists", TXT.get("CreateDataFileDialog.err.dir", "{0} is a directory.", f));
					return;
				}
				
				if(!Dialogs.checkFileExistsOverwrite(this, f))
				{
					return;
				}
			}
			
			setProgress(true);

			final OpaqueChars pass = passField.getOpaquePassword();
			
			new BackgroundThread("creating")
			{
				private DataFile df;
				
				public void process() throws Throwable
				{
					df = new DataFile();
					df.setPassword(pass);
					PasswordSafeApp.save(df, f);
				}
	
				public void success()
				{
					setProgress(false);
					close();
					created = true;
				}
	
				public void onError(Throwable e)
				{
					setProgress(false);
					log.error(e);
					backToPassword();
				}
			}.start();
		}
		catch(Exception e)
		{
			Dialogs.error(this, e);
		}
	}
	
	
	protected void backToPassword()
	{
		passField.requestFocusInWindow();
		passField.selectAll();
	}

	
	protected void actionBrowse()
	{
		File f = new File(fileField.getText());
		CFileChooser fc = new CFileChooser(this, null);
		fc.setDialogTitle(TXT.get("CreateDataFileDialog.title.create", "Choose File"));
		fc.setApproveButtonText(TXT.get("CreateDataFileDialog.button.select file", "Select"));
		fc.setDialogType(CFileChooser.CUSTOM_DIALOG);
		fc.setCurrentDirectory(f.getParentFile());
		fc.setSelectedFile(f);
		fc.setFileFilter(Styles.createFileFilter());
		f = fc.openFileChooser();
		if(f != null)
		{
			fileField.setText(f.getAbsolutePath());
		}
	}
}
