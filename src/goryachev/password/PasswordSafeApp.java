// Copyright (c) 2005-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.Application;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.dialogs.license.StandardLicense;
import goryachev.common.util.CKit;
import goryachev.common.util.CSystem;
import goryachev.common.util.Log;
import goryachev.common.util.ProductInfo;
import goryachev.common.util.TXT;
import goryachev.common.util.log.FileLogWriter;
import goryachev.common.util.log.LogWriter;
import goryachev.crypto.OpaqueChars;
import goryachev.password.data.DataFile;
import goryachev.password.data.DataFormat;
import goryachev.password.data.v1.DataFormatV1;
import goryachev.password.data.v2.DataFormatV2;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.password.prompts.Prompts;
import goryachev.password.ui.ClipboardHandler;
import java.io.File;
import javax.swing.ImageIcon;


public class PasswordSafeApp
	extends Application
{
	public static final String VERSION   = "3.02.41";
	public static final String COPYRIGHT = "copyright © 2015 andy goryachev";	
	public static final String TITLE = "PasswørdSafe";
	public static final String EXTENSION = ".safe";
	public static final String PRODUCT_URL = "http://goryachev.com/products/password-safe";
	public static final String UPDATE_URL = PRODUCT_URL + "/version";
	
	//
	
	public static void main(String[] args)
	{
		new PasswordSafeApp().start();
	}
	

	public PasswordSafeApp()
	{
		super("PasswordSafe", VERSION, COPYRIGHT);
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
		Log.initConsole();
		
		File folder = new File(getDefaultSettingsDirectory(), "logs");
		folder.mkdirs();
		
		try
		{
			Log.writeStartupLog(new File(folder, "startup.log"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		LogWriter wr = new FileLogWriter("file", new File(folder, "error.log"), 5000000L, 5);
		wr.setAsync(true);
		Log.addErrorWriter(wr);
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
		File f = new File(getDefaultSettingsDirectory(), CSystem.getUserName() + EXTENSION);
		try
		{
			return f.getCanonicalFile();
		}
		catch(Exception e)
		{
			Log.err(e);
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
				new LockWindow(f).open();
			}
		}
		catch(Exception e)
		{
			Log.err(e);
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
		
		byte[] b = format.save(df);
		
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
