// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.common.util.CKit;
import goryachev.swing.Accelerator;
import goryachev.swing.KeyNames;
import javax.swing.KeyStroke;


public class KeyBindingEntry
{
	public final Accelerator accelerator;
	private KeyStroke key;
	private KeyStroke original;


	public KeyBindingEntry(Accelerator a, KeyStroke k)
	{
		this.accelerator = a;
		this.key = k;
		this.original = k;
	}


	public KeyStroke getKey()
	{
		return key;
	}
	
	
	public void setKey(KeyStroke k)
	{
		this.key = k;
	}
	
	
	public boolean isModified()
	{
		return CKit.notEquals(key, original);
	}
	
	
	public String getKeyName()
	{
		return KeyNames.getKeyName(key);
	}
	
	
	public String getCommand()
	{
		return accelerator.getName();
	}
	
	
	public void commit()
	{
		if(isModified())
		{
			accelerator.setKeyStroke(key);
		}
	}
}