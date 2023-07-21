// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.dialogs;
import goryachev.swing.Theme;
import javax.swing.JLabel;


public class AnimatedWaitPanel
	extends JLabel
{
	public AnimatedWaitPanel(int size)
	{
		super(Theme.waitIcon(size), JLabel.CENTER);
		setOpaque(true);
		setBackground(Theme.TEXT_BG);
		setIconTextGap(size/2);
	}
	
	
	public AnimatedWaitPanel(int size, String text)
	{
		this(size);
		setText(text);
	}
}
