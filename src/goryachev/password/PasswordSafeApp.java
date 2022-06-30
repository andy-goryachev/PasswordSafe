// Copyright © 2005-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CSystem;
import goryachev.i18n.TXT;
import goryachev.memsafecryptoswing.EntropyGathererSwing;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.password.prompts.Prompts;
import goryachev.password.ui.ClipboardHandler;
import goryachev.swing.Application;
import goryachev.swing.Dialogs;
import goryachev.swing.dialogs.license.StandardLicense;
import java.io.File;
import javax.swing.ImageIcon;


public class PasswordSafeApp
	extends Application
{
	public static final String TITLE = "PasswørdSafe";
	public static final String EXTENSION = ".safe";
	protected static final Log log = Log.get("PasswordSafeApp");

	
	public PasswordSafeApp()
	{
		super("PasswordSafe", Version.VERSION, Version.COPYRIGHT);
	}
	

	public static void main(String[] args)
	{
		new PasswordSafeApp().start();
	}
	
	
	protected void initApplication() throws Exception
	{
	}
	
	
	protected void initI18N() throws Exception
	{
		Prompts.init();
	}
	
	
	protected void initLogger()
	{
		Log.initConsoleForDebug();
		
//		File folder = new File(getDefaultSettingsDirectory(), "logs");
//		folder.mkdirs();
//		
//		try
//		{
//			Log.writeStartupLog(new File(folder, "startup.log"));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		LogWriter wr = new FileLogWriter("file", new File(folder, "error.log"), 5000000L, 5);
//		wr.setAsync(true);
//		Log.addErrorWriter(wr);
	}
	

	public String getAppTitle()
	{
		return TITLE;
	}


	public boolean exiting()
	{
		ClipboardHandler.clearConditionally();
		return true;
	}


	public ImageIcon getAppIcon()
	{
		return PasswordSafeIcons.Application;
	}

	
	protected void failed(String message)
	{
		Dialogs.startupError(Application.getIcon(), TXT.get("PasswordSafeApp.err.title", "Startup Failure"), message);
		System.exit(-1);
	}
	
	
	public static File getDefaultDataFile()
	{
		File f = new File(getUserHome(), CSystem.getUserName() + EXTENSION);
		try
		{
			return f.getCanonicalFile();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		return f;
	}
	

	public static StandardLicense getLicense()
	{
		return new StandardLicense();
	}


	public void openMainWindow()
	{
		try
		{
			EntropyGathererSwing.start();
			
			// check license
			long time = Preferences.licenseAcceptedOption.get();
			long YEAR_2000 = 946713600000L;
			boolean accepted = (time > YEAR_2000);
			
			File f = Preferences.dataFileOption.get();
			
			if((!accepted) || (f == null))
			{
				new WelcomeWizard().open();
			}
			else
			{
				MainWindow w = new MainWindow();
				w.lock(f);
				w.open();
			}
		}
		catch(Exception e)
		{
			log.error(e); // TODO fail
			failed(CKit.stackTrace(e));
		}
	}
}
