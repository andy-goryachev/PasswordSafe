// Copyright © 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.i18n.Menus;
import goryachev.common.i18n.TXT;
import goryachev.common.util.HasPrompts;
import goryachev.common.util.ProductInfo;
import goryachev.common.util.SB;
import goryachev.crypto.EntropyGatherer;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.swing.Application;
import goryachev.swing.CAction;
import goryachev.swing.CButton;
import goryachev.swing.CPanel;
import goryachev.swing.CheckForUpdate;
import goryachev.swing.GlobalSettings;
import goryachev.swing.Panels;
import goryachev.swing.Theme;
import goryachev.swing.dialogs.CFileChooser;
import goryachev.swing.dialogs.license.StandardLicense;
import goryachev.swing.icons.CIcons;
import goryachev.swing.text.CDocumentBuilder;
import goryachev.swing.theme.AssignMnemonic;
import goryachev.swing.wizard.LanguagePage;
import goryachev.swing.wizard.ProcessPanel;
import goryachev.swing.wizard.WelcomePrompts;
import goryachev.swing.wizard.WizardFrame2;
import java.io.File;
import javax.swing.JLabel;


public class WelcomeWizard
	extends WizardFrame2
	implements HasPrompts
{
	public final CAction acceptLicenseAction = new CAction() { public void action() { actionAcceptLicense(); }};
	public final CAction showLicenseAction = new CAction() { public void action() { actionShowLicense(); }};
	public final CAction showOptionsAction = new CAction() { public void action() { actionShowOptions(); }};
	public final CAction createNewAction = new CAction() { public void action() { actionCreateNew(); } };
	public final CAction openExistingAction = new CAction() { public void action() { actionOpenExisting(); } };

	public final CheckForUpdate checkForUpdate;
	protected LanguagePage languagePage;
	
	
	public WelcomeWizard()
	{
		super("WelcomeWizard");
		setMinimumSize(500, 500);
		
		checkForUpdate = new CheckForUpdate(PasswordSafeApp.UPDATE_URL + "?" + Application.getVersion());
		
		showOptionsAction.setEnabled(false);
				
		showLanguagePage();
		
		updatePrompts();
		TXT.registerListener(this);
		EntropyGatherer.start();
	}
	
	
	protected void showLanguagePage()
	{
		languagePage = new LanguagePage();
		languagePage.setBorder(10);
		languagePage.setGaps(10, 20);
		languagePage.setLogo(PasswordSafeIcons.Logo);
		languagePage.buttonPanel().addButton(showLicenseAction, true);
		setCard(languagePage, null);
	}
	
	
	public void updatePrompts()
	{
		SB sb = new SB();
		sb.a("<html><b>");
		sb.a(WelcomePrompts.thankYou());
		sb.a("</b><br><br>");
		sb.a(TXT.get("WelcomeDialog.APP is a storage", "{0} is a password storage program.  It stores your passwords and short notes in an encrypted file, allowing you to remember only one passphrase instead of many different username/password combinations.", Application.getTitle()));
		languagePage.setInfo(sb.toString());
		
		setTitle(WelcomePrompts.welcomeToApp());
		
		showLicenseAction.setText(WelcomePrompts.next());
		
		closeAction.setText(WelcomePrompts.close());
		backAction.setText(WelcomePrompts.back());
		acceptLicenseAction.setText(WelcomePrompts.acceptLicense());
		showOptionsAction.setText(WelcomePrompts.next());
		createNewAction.setText(TXT.get("WelcomeDialog.button.create new database", "Create"));
		openExistingAction.setText(TXT.get("WelcomeDialog.button.open existing database", "Open"));
		
		AssignMnemonic.assign(this);
	}
	
	
	protected void actionShowLicense()
	{
		StandardLicense lic = new StandardLicense();
		lic.setMilitaryClause(true);
		
		CPanel p = new CPanel();
		p.setCenter(Panels.scrollDocument(lic.getDocument()));
		
		p.buttonPanel().addButton(closeAction);
		p.buttonPanel().addButton(backAction);
		p.buttonPanel().addButton(acceptLicenseAction, Theme.DESTRUCTIVE_BUTTON_COLOR);
		p.buttonPanel().addButton(showOptionsAction, true);
		p.buttonPanel().setBorder(10);
		
		setCard(p, WelcomePrompts.licenseAgreement());
	}
	
	
	protected void actionAcceptLicense()
	{
		Preferences.licenseAcceptedOption.set(System.currentTimeMillis());
		GlobalSettings.save();
		
		showOptionsAction.setEnabled(true);
		
		// go to the next page
		actionShowOptions();
	}
	
	
	protected void actionShowOptions()
	{
		ProcessPanel pp = new ProcessPanel(createNewAction, null, openExistingAction)
		{
			protected void execute() throws Exception
			{
				checkForUpdate.readWeb();
				comfortSleep(700);
			}
			
			protected void onSuccess()
			{
				if(checkForUpdate.isUpdateAvailable(Application.getVersion()))
				{
					CDocumentBuilder b = new CDocumentBuilder();
					b.a(TXT.get("InstallationPage.update", "A new version of {0} has been released.  Please visit our web site to obtain the updated version.", Application.getTitle()));
					b.nl();
					b.link(ProductInfo.getWebSite());
					setDocument(b.getDocument());
					setStatusIcon(CIcons.Info32);
				}
				else
				{
					setText(TXT.get("WelcomeWizard.latest version", "You've got the latest version."));
				}
			}
		};
		
		String info = TXT.get("WelcomeDialog.info.1", "Unless you already have an existing password database, you need to create a new file which will hold the encrypted database.");
		
		String check = 
			TXT.get("WelcomeDialog.check.1", "To make sure that you're protected by the latest security updates, {0} will make a one time attempt to detect if a new version is available.", Application.getTitle()) +
			"\n\n" +
			TXT.get("WelcomeDialog.check.2", "You can always check for updates from the main menu by selecting {0} -> {1}.", Menus.Help, Menus.CheckForUpdates);
		
		CPanel p = new CPanel();
		p.setBorder();
		p.setGaps(10);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL
		);
		p.setColumnMinimumSize(0, Theme.minimumButtonWidth());
		
		p.row(0, 2, p.info(info));
		p.nextRow();
		p.row(0, new CButton(createNewAction, true));
		p.row(1, new JLabel(TXT.get("WelcomeDialog.create new database", "Create new password database")));
		p.nextRow();
		p.row(0, new CButton(openExistingAction, Theme.DESTRUCTIVE_BUTTON_COLOR));
		p.row(1, new JLabel(TXT.get("WelcomeDialog.open existing database", "Open existing password database")));
		p.nextRow(10);
		p.nextRow();
		p.row(0, 2, p.info(check));
		p.nextFillRow();
		p.row(1, pp);
		
		setCard(p, null);
		
		pp.start();
		pp.setText(TXT.get("WelcomeWizard.checking for updates", "Checking for updates..."));
	}
	
	
	protected void actionCreateNew()
	{
		CreateDataFileDialog d = new CreateDataFileDialog(this);
		d.open();
		if(d.isCreated())
		{			
			File f = d.getFile();
			openMainWindow(f);
		}
	}
	
	
	protected void actionOpenExisting()
	{
		File f = Preferences.dataFileOption.get();
		if(f == null)
		{
			f = PasswordSafeApp.getDefaultDataFile();
		}
		
		CFileChooser fc = new CFileChooser(this, MainWindow.KEY_LAST_FOLDER);
		fc.setDialogType(CFileChooser.OPEN_DIALOG);
		fc.setFileFilter(Styles.createFileFilter());
		
		if(f != null)
		{
			fc.setCurrentDirectory(f);
			fc.setSelectedFile(f);
		}
		
		f = fc.openFileChooser();
		if(f != null)
		{
			openMainWindow(f);
		}
	}
	
	
	protected void openMainWindow(File f)
	{
		new LockWindow(f).open();
		close();
	}
}
