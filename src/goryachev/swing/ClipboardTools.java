// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.log.Log;
import goryachev.i18n.Menus;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;


public class ClipboardTools
{
	protected static final Log log = Log.get("ClipboardTools");
	
	
	public static Clipboard getClipboard()
	{
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}


	public static String readString()
	{
		try
		{
			Clipboard c = getClipboard();
			if(c.isDataFlavorAvailable(DataFlavor.stringFlavor))
			{
				return (String)c.getData(DataFlavor.stringFlavor);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		return null;
	}


	public static void copy(String s)
	{
		StringSelection sel = new StringSelection(s);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
	}


	public static CAction copyAction(final String s)
	{
		return new CAction(Menus.Copy)
		{
			public void action() throws Exception
			{
				ClipboardTools.copy(s);
			}
		};
	}
}
