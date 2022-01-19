// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.log.Log;
import goryachev.common.util.NetTools;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;


public class CBrowser
{
	protected static final Log log = Log.get("CBrowser");
	
	
	public static void openLink(String uri) throws Exception
	{
		URI u = NetTools.parseURI(uri);
		openLink(u);
	}
	
	
	public static void openLinkQuiet(String uri)
	{
		try
		{
			openLink(uri);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	public static void openLink(URL url) throws Exception
	{
		openLink(url.toURI());
	}


	public static void openLinkQuiet(URL url)
	{
		try
		{
			openLink(url);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}


	public static void openLink(URI uri) throws Exception
	{
		Desktop.getDesktop().browse(uri);
	}
}
