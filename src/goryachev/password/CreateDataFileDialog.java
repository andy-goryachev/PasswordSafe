// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CButtonPanel;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CFocusTraversalPolicy;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CProgressField;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.InfoField;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.ui.CPasswordField;
import goryachev.crypto.ui.MatchLabel;
import goryachev.crypto.ui.OnScreenKeyboard;
import goryachev.password.ui.PasswordVerifier2;
import java.awt.Component;
import java.io.File;
import javax.swing.JTextField;


// FIX change to panel, open in a frame or a dialog...
public class CreateDataFileDialog
	extends CDialog
{
	public final CAction browseAction = new CAction() { public void action() { onBrowse(); } };
	private final JTextField fileField;
	private final CPasswordField passField;
	private final CPasswordField verifyField;
	private final MatchLabel matchField;
	private final PasswordVerifier2 verifier;
	private final OnScreenKeyboard keyboard;
	private final CProgressField progressField;
	protected boolean created;
	
	
	public CreateDataFileDialog(Component parent)
	{
		super(parent, "CreateDataFileDialog", true);
		
		setTitle(TXT.get("CreateDataFileDialog.title.create file", "Create Password Database"));
		setMinimumSize(750, 500);
		
		fileField = new JTextField();
		
		passField = new CPasswordField();

		verifyField = new CPasswordField();
		
		matchField = new MatchLabel();

		verifier = new PasswordVerifier2(null, passField, verifyField, null, matchField)
		{
			protected void onPasswordsMatch(boolean have, boolean match)
			{
				okAction.setEnabled(have && match);
			}
		};
		
		progressField = new CProgressField(16);
		
		keyboard = Styles.createKeyboard();
		
		CButton browseButton = new CButton(Menus.Browse, browseAction);
		
		okAction.setEnabled(false);
		CButton okButton = new CButton(TXT.get("CreateDataFileDialog.button.create file", "Create"), okAction);
		okButton.setHighlight(Theme.buttonHighlight());
		
		CButton cancelButton = new CButton(Menus.Cancel, closeAction);
		
		CPanel p = new CPanel();
		p.setLayout
		(
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL,
				CPanel.PREFERRED
			},
			new double[]
			{
				CPanel.PREFERRED,
				10,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL
			},
			10, 5
		);
		int ix = 0;
		p.add(1, ix, 3, ix, createInfoField());
		ix++;
		ix++;
		p.add(0, ix, p.label(TXT.get("CreateDataFileDialog.file", "File:")));
		p.add(1, ix, 2, ix, fileField);
		p.add(3, ix, browseButton);
		ix++;
		p.add(0, ix, p.label(TXT.get("CreateDataFileDialog.label.passphrase", "Passphrase:")));
		p.add(1, ix, 3, ix, passField);
		ix++;
		p.add(0, ix, p.label(TXT.get("CreateDataFileDialog.label.verify password", "Verify:")));
		p.add(1, ix, 3, ix, verifyField);
		p.add(3, ix, matchField);
		ix++;
		p.add(1, ix, keyboard);
		ix++;
		p.add(1, ix, 3, ix, progressField);
	
		p.setSouth(new CButtonPanel(10, cancelButton, okButton));
	
		setContent(p);

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
		progressField.setAnimation(on);
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
	

	protected void onOk()
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
				
				if(Dialogs.confirm(this, "File exists", TXT.get("CreateDataFileDialog.err.overwrite", "Do you want t overwrite existing file {0}?", f)) == false)
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
					Log.err(e);
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

	
	protected void openFile()
	{
		CFileChooser fc = new CFileChooser(this, MainWindow.KEY_LAST_FOLDER);
		fc.setDialogType(CFileChooser.CUSTOM_DIALOG);
		fc.setApproveButtonText(Menus.Create);
		fc.setFileFilter(Styles.createFileFilter());
		File f = fc.openFileChooser();
		if(f == null)
		{
			close();
			return;
		}
		else
		{
			f = CKit.ensureExtension(f, PasswordSafeApp.EXTENSION);
			
			if(f.exists())
			{
				if(Dialogs.confirm(this, TXT.get("MainWindow.err.file exists.title", "File Exists"), TXT.get("MainWindow.err.file exists", "File {0} exists, do you want to overwrite it?", f)) == false)
				{
					openFile();
					return;
				}
			}
			
			fileField.setText(f.getAbsolutePath());
		}
	}
	
	
	protected void onBrowse()
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
