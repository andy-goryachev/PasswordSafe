// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.Application;
import goryachev.common.ui.BasePanel;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CheckForUpdate;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.Theme;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.ui.dialogs.license.StandardLicense;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.common.ui.theme.AssignMnemonic;
import goryachev.common.ui.wizard.LanguagePage;
import goryachev.common.ui.wizard.ProcessPanel;
import goryachev.common.ui.wizard.WelcomePrompts;
import goryachev.common.ui.wizard.WizardFrame2;
import goryachev.common.util.HasPrompts;
import goryachev.common.util.ProductInfo;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import goryachev.crypto.EntropyGatherer;
import goryachev.password.img.PasswordSafeIcons;
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
		languagePage.setLogo(PasswordSafeIcons.Logo);
		languagePage.buttons().add(new CButton(showLicenseAction, true));
		setCard(languagePage, null);
	}
	
	
	public void updatePrompts()
	{
		SB sb = new SB();
		sb.a("<html><b>");
		sb.a(WelcomePrompts.thankYou());
		sb.a("</b><br><br>");
		sb.a(TXT.get("WelcomeDialog.APP is a storage", "{0} is a password storage program.  It stores your passwords and short notes in an encrypted file, allowing you to remember only one passphrase instead of all different username/password combinations.", Application.getTitle()));
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
		
		BasePanel p = new BasePanel();
		p.setCenterTextArea().setDocument(lic.getDocument());
		p.buttons().add(new CButton(closeAction));
		p.buttons().add(new CButton(backAction));
		p.buttons().add(new CButton(acceptLicenseAction, Theme.alternativeButtonHighlight()));
		p.buttons().add(new CButton(showOptionsAction, true));
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
		
		CPanel p = new CPanel();
		p.setBorder(new CBorder(20));
		p.setLayout
		(
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.FILL
			},
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
			},
			10, 10
		);
		int ix = 0;
		p.add(0, ix, 1, ix, p.info(info));
		ix++;
		p.add(0, ix, new CButton(createNewAction, true));
		p.add(1, ix, new JLabel(TXT.get("WelcomeDialog.create new database", "Create new password database")));
		ix++;
		p.add(0, ix, new CButton(openExistingAction, Theme.alternativeButtonHighlight()));
		p.add(1, ix, new JLabel(TXT.get("WelcomeDialog.open existing database", "Open existing password database")));
		
		CPanel mp = new CPanel();
		mp.setNorth(p);
		mp.setCenter(pp);
		setCard(mp, null);
		
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
