// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.UI;
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
