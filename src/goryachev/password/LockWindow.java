// Copyright © 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.swing.AppFrame;
import goryachev.common.swing.Application;
import goryachev.common.swing.UI;
import goryachev.crypto.EntropyGatherer;
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
		EntropyGatherer.start();
		
		UI.whenAncestorOfFocusedComponent(this, KeyEvent.VK_ESCAPE, closeAction);
	}
}
