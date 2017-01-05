// Copyright © 2015-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;


/** capable of storing global settings */
public interface HasSettings
{
	public void restoreSettings(String key, UISettings settings);

	
	public void storeSettings(String key, UISettings settings);
}
