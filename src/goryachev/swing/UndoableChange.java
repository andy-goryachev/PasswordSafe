// Copyright © 2008-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;


public interface UndoableChange
{
	public void change();
	
	public void undoChange();
}
