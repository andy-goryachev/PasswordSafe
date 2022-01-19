// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.io.WebReader;
import goryachev.common.log.Log;
import goryachev.common.util.DotSeparatedVersion;
import goryachev.common.util.UserException;
import goryachev.i18n.TXT;
import java.io.FileNotFoundException;
import java.net.URL;


/**
 * Retrieves the content with the specified URL as a UTF-8 string
 */ 
public class CheckForUpdate
{
	protected static final Log log = Log.get("CheckForUpdate");
	private final String url;
	private final WebReader reader;
	private volatile String siteVersion;
	
	
	public CheckForUpdate(String url)
	{
		this.url = url;
		this.reader = new WebReader();
	}
	
	
	public void readWeb() throws Exception
	{
		try
		{
			siteVersion = reader.readString(url);
		}
		catch(FileNotFoundException e)
		{
			log.error(e);
			URL u = new URL(url);
			throw new UserException(TXT.get("CheckForUpdate.err.server problem", "Sorry, there seems to be a problem contacting server [{0}]", u.getHost()));
		}
	}
	
	
	public String getLatestVersion()
	{
		return siteVersion;
	}
	

	public boolean isUpdateAvailable(String currentVersion)
	{
		try
		{
			if(DotSeparatedVersion.compare(siteVersion, currentVersion) > 0)
			{
				return true;
			}
		}
		catch(Exception e)
		{ }
		
		return false;
	}
}
