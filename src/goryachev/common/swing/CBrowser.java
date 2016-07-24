// Copyright © 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.swing;
import goryachev.common.util.Log;
import goryachev.common.util.NetTools;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;


public class CBrowser
{
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
			Log.fail(e);
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
			Log.fail(e);
		}
	}


	public static void openLink(URI uri) throws Exception
	{
		Desktop.getDesktop().browse(uri);
	}
}
