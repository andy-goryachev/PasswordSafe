// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.swing.AppFrame;
import goryachev.swing.Application;
import goryachev.swing.UI;
import java.awt.event.KeyEvent;
import java.io.File;


public class LockWindow
	extends AppFrame
{
	private LockPanel lockPanel;
	
	
	public LockWindow(File f)
	{
		super("LockWindow");
		
		lockPanel = new LockPanel(f);
		
		setContent(lockPanel);
		setMinimumSize(700, 420);
		setSize(700, 420);
		
		setTitle(Application.getTitle() + " " + Application.getVersion());
		
		UI.whenAncestorOfFocusedComponent(this, KeyEvent.VK_ESCAPE, closeAction);
	}
}
