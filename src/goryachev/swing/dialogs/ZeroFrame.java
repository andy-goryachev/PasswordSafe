// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.dialogs;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class ZeroFrame
	extends JFrame
{
	public ZeroFrame(ImageIcon icon, String title)
	{
		if(icon != null)
		{
			setIconImage(icon.getImage());
		}
		setUndecorated(true);
		setSize(0,0);
		setTitle(title);
		setVisible(true);
	}
}
