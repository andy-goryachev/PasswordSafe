// Copyright © 2010-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options;
import java.awt.Component;


public abstract class ComponentOption
{
	public abstract String toProperty();
	
	public abstract void parseProperty(String s);
	
	//
	
	public ComponentOption(String key, Component c)
	{
		// TODO
	}
}
