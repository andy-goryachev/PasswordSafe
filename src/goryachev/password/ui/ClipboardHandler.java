// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password.ui;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


public class ClipboardHandler
{
	private static boolean clipboardContainsSensitiveData;
	
	private static ClipboardOwner monitor = new ClipboardOwner()
	{
		public void lostOwnership(Clipboard c, Transferable t)
		{
			setClipboardContainsSensitiveData(false);
		}
	};
	
	
	//
	
	
	public static void copy(String s)
	{
		setClipboardContainsSensitiveData(true);
		copyString(s);
	}
	
	
	public static void setClipboardContainsSensitiveData(boolean on)
	{
		clipboardContainsSensitiveData = on;
	}
	
	
	private static void copyString(String s)
	{
		StringSelection sel = new StringSelection(s);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, monitor);
	}


	public static void clearConditionally()
	{
		if(clipboardContainsSensitiveData)
		{
			copyString("");
		}
	}
	
	
	public static void clear()
	{
		copyString("");
	}
}
