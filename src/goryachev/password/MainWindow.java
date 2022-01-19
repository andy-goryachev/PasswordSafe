// Copyright © 2009-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.i18n.TXT;
import goryachev.password.ui.ActivityMonitor;
import goryachev.password.ui.ClipboardHandler;
import goryachev.swing.AppFrame;
import goryachev.swing.Application;
import goryachev.swing.CButton;
import goryachev.swing.CPanel;
import goryachev.swing.GlobalSettings;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JLabel;


// TODO recentFiles
// TODO incognito mode
public class MainWindow
	extends AppFrame
{
	public static final String KEY_LAST_FOLDER = "last.folder";

	private File file;
	private boolean modified;
	private ActivityMonitor activityMonitor;
	private MainPanel mainPanel;
	

	public MainWindow()
	{
		super("MainWindow");
		
		int min = Preferences.lockTimeoutOption.get();
		int ms = (int)CKit.minutesToMilliseconds(min);
		
		activityMonitor = new ActivityMonitor(this, ms)
		{
			protected void onNoActivity()
			{
				lock();
			}
		};
		activityMonitor.start();
		
		setIcon(Application.getIcon());
		setTitle(Application.getTitle());
		setSize(700, 500);
		setMinimumSize(500, 300);
	}
	
	
	protected CButton sz(CButton b)
	{
		Dimension d = b.getPreferredSize();
		int min = Theme.minimumButtonWidth();
		if(d.width < min)
		{
			d.width = min;
		}
		b.setPreferredSize(d);
		return b;
	}
	
	
	public File getFile()
	{
		return file;
	}
	
	
	public void setFile(File f)
	{
		this.file = f;
		Preferences.dataFileOption.set(f);
	}
	
	
	public void actionPreferences()
	{
		Preferences.openPreferences(this);
	}
	
	
	public boolean onWindowClosing()
	{
		onExit();
		return false;
	}
	
	
	protected void onExit()
	{
		GlobalSettings.storeAll();
		
		displaySaving();
		
		UI.later(() ->
		{
			if(mainPanel != null)
			{
				mainPanel.saveIfModified();
			}
			Application.exit();
		});
	}
	
	
	public void updateTitle()
	{
		SB sb = new SB();
		sb.a(Application.getTitle());
		if(getFile() == null)
		{
			sb.a("  ");
			sb.a(Version.VERSION);
		}
		else
		{
			sb.a(" - ");
			sb.a(getFile().getAbsolutePath());
			
			if(isModified())
			{
				sb.a(" *");
			}
		}
		
		setTitle(sb.toString());
	}
	
	
	public void setModified(boolean on)
	{
		modified = on;
		updateTitle();
	}
	
	
	public boolean isModified()
	{
		return modified;
	}
	
	
	protected void lock()
	{
		ClipboardHandler.clearConditionally();
		GlobalSettings.storeAll();
		
		if(isModified())
		{
			displaySaving();
			
			UI.later(() ->
			{
				mainPanel.saveFile();			
				lock(getFile());
			});
		}
		else
		{
			lock(getFile());
		}
	}
	
	
	protected void purgeSecrets()
	{
		if(mainPanel != null)
		{
			mainPanel.purgeSecrets();
		}
		
		ClipboardHandler.clearConditionally();		
	}
	

	public static MainWindow get(Component c)
	{
		return UI.getAncestorOfClass(MainWindow.class, c);
	}


	public void lock(File f)
	{
		purgeSecrets();

		setJMenuBar(null);
		setContent(new LockPanel(this, f));
		updateTitle();
		validate();
		repaint();
	}
	
	
	public MainPanel setMainPanel()
	{
		mainPanel = new MainPanel(this);
		
		CPanel p = new CPanel();
		p.setNorth(mainPanel.createToolbar());
		p.setCenter(mainPanel);
		p.setSouth(createStatusBar(true));

		setJMenuBar(mainPanel.createMenu());
		setContent(p);
		validate();
		repaint();
		
		UI.later(() -> mainPanel.listTab.actionFocusFilter());
		
		return mainPanel;
	}
	
	
	public void displaySaving()
	{
		JLabel t = new JLabel(TXT.get("MainWindow.saving database", "Saving database..."));
		t.setHorizontalAlignment(JLabel.CENTER);
		
		setJMenuBar(null);
		setContent(t);
		
		validate();
		repaint();
	}
}
