// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.crypto.EntropyGatherer;
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
		setMinimumSize(600, 420);
		setSize(600, 420);
		
		setTitle(Application.getTitle() + " " + Application.getVersion());
		EntropyGatherer.start();
	}
}
