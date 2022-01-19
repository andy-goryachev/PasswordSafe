// Copyright © 2005-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CSystem;
import goryachev.common.util.Copyright;
import goryachev.common.util.ProductInfo;
import goryachev.crypto.OpaqueChars;
import goryachev.crypto.swing.EntropyGathererSwing;
import goryachev.i18n.TXT;
import goryachev.password.data.DataFile;
import goryachev.password.data.DataFormat;
import goryachev.password.data.v1.DataFormatV1;
import goryachev.password.data.v2.DataFormatV2;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.password.prompts.Prompts;
import goryachev.password.ui.ClipboardHandler;
import goryachev.swing.Application;
import goryachev.swing.Dialogs;
import goryachev.swing.dialogs.license.StandardLicense;
import java.io.File;
import java.security.SecureRandom;
import javax.swing.ImageIcon;


public class PasswordSafeApp
	extends Application
{
	public static final String TITLE = "PasswørdSafe";
	public static final String EXTENSION = ".safe";
	public static final String PRODUCT_URL = "http://goryachev.com/products/password-safe";
	public static final String UPDATE_URL = PRODUCT_URL + "/version";
	protected static final Log log = Log.get("PasswordSafeApp");
	private static SecureRandom random;

	
	public PasswordSafeApp()
	{
		super("PasswordSafe", Version.VERSION, Copyright.COPYRIGHT);
	}
	

	public static void main(String[] args)
	{
		new PasswordSafeApp().start();
	}
	
	
	protected void initApplication() throws Exception
	{
		ProductInfo.setWebSite(PRODUCT_URL);
	}
	
	
	protected void initI18N() throws Exception
	{
		Prompts.init();
	}
	
	
	protected void initLogger()
	{
		// TODO
//		Log.initConsole();
		
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
			random = EntropyGathererSwing.init();
			
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
	
	
	public static DataFile loadDataFile(File file, OpaqueChars pass) throws Exception
	{
		byte[] enc = CKit.readBytes(file, Integer.MAX_VALUE);
		
		try
		{
			return new DataFormatV2().load(enc, pass);
		}
		catch(Exception e)
		{
			if(e.getMessage() == DataFormatV2.ERROR_WRONG_SIGNATURE)
			{
				// fall back to V1
				return new DataFormatV1().load(enc, pass);
			}
			else
			{
				throw e;
			}
		}
	}


	public static void save(DataFile df, File file) throws Exception
	{
		DataFormat format = new DataFormatV2();
		
		byte[] b = format.save(df, random);
		
		// write to temp file
		File f = File.createTempFile("PasswordSafe-", ".tmp", file.getParentFile());
		CKit.write(b, f);
		
		// delete old version and rename
		file.delete();
		
		// finish
		if(f.renameTo(file) == false)
		{
			throw new Exception(TXT.get("DataFile.failed to rename", "Unable to replace existing file {0}.", file));
		}
	}
}
