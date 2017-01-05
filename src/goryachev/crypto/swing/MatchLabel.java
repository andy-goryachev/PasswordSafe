// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import goryachev.swing.icons.CIcons;
import javax.swing.Icon;
import javax.swing.JLabel;


public class MatchLabel
	extends JLabel
{
	public static final Icon ICON_EMPTY = CIcons.Empty16;
	public static final Icon ICON_MATCH = CIcons.Success16;
	public static final Icon ICON_NO_MATCH = CIcons.Error16;
	
	
	public MatchLabel()
	{
		setMatch(null);
	}
	
	
	public void setMatch(Boolean on)
	{
		if(on == null)
		{
			setIcon(CIcons.Empty16);
		}
		else if(Boolean.TRUE.equals(on))
		{
			setIcon(ICON_MATCH);
		}
		else
		{
			setIcon(ICON_NO_MATCH);
		}
	}
}
