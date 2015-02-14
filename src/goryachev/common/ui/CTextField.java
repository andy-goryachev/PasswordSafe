// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.JTextField;
import javax.swing.text.Document;


public class CTextField
	extends JTextField
{
	public CTextField()
	{
	}
	
	
	public CTextField(int columns)
	{
		super(columns);
	}
	
	
	public CTextField(String text)
	{
		super(text);
	}
	
	
	public CTextField(String text, int columns)
	{
		super(text, columns);
	}


	public CTextField(Document d, String text, int columns)
	{
		super(d, text, columns);
	}
	
	
	public void clear()
	{
		setText(null);
	}
}
