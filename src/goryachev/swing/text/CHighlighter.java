// Copyright © 2010-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.text;
import java.awt.Color;
import javax.swing.text.DefaultHighlighter;


// I don't want to write "DefaultHighlighter.DefaultHighlightPainter"
public class CHighlighter
	extends DefaultHighlighter.DefaultHighlightPainter
{
	public CHighlighter(Color c)
	{
		super(c);
	}


	public CHighlighter(int r, int g, int b)
	{
		super(new Color(r,g,b));
	}
}
