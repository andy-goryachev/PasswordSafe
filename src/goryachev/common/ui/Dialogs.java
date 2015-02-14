// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.StandardDialog;
import goryachev.common.ui.dialogs.options.COptionDialog;
import goryachev.common.ui.dialogs.options.OptionTreeNode;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.common.util.UserException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;


/** Standard dialogs - replacement for JOptionPane */
public class Dialogs
{
	public static enum DiscardChanges
	{
		CANCEL,
		DISCARD,
		SAVE
	}
	
	//
	
	public static void startupError(ImageIcon icon, String title, Object exceptionOrMessage)
	{
		JFrame parent = UI.constructInvisibleFrame(icon, title);
		error(parent, title, exceptionOrMessage);
		Application.exit();
	}
	
	
	public static void err(Component parent, Object exceptionOrMessage)
	{
		error(parent, exceptionOrMessage);
	}
	
	
	public static void error(Component parent, Object exceptionOrMessage)
	{
		if(exceptionOrMessage instanceof UserException)
		{
			String title = TXT.get("Dialogs.error.sorry","Sorry");
			error(parent, title, exceptionOrMessage);
		}
		else
		{
			String title = TXT.get("Dialogs.error.unexpected","Unexpected Error");
			error(parent, title, exceptionOrMessage);
		}
	}
	
	
	public static void error(Component parent, String title, Object exceptionOrMessage)
	{
		StandardDialog d = constructDialog(parent, CIcons.Error96, title, exceptionOrMessage);

		// can't get a pointer
		//d.addButton(new CButton(TXT.get("Dialogs.error.button.copy","Copy to Clipboard"), d.copyToClipboardAction));
		d.addButton(new CButton(Menus.OK, d.closeAction));
		d.setButtonHighlight();
		d.setDefaultButton();
		d.open();
	}
	
	
	public static void info(String title, String message)
	{
		info(CFocusMonitor.getLastWindow(), title, message);
	}
	
	
	public static void info(Component parent, String title, String message)
	{
		StandardDialog d = constructDialog(parent, CIcons.Info96, title, message);
		
		d.addButton(new CButton(Menus.OK, d.closeAction));
		d.setButtonHighlight();
		d.setDefaultButton();
		d.open();
	}
	

	public static void warn(Component parent, String title, String message)
	{
		StandardDialog d = constructDialog(parent, CIcons.Warning96, title, message);
		
		d.addButton(new CButton(Menus.OK, d.closeAction));
		d.setButtonHighlight();
		d.setDefaultButton();
		d.open();
	}
	
	
	@Deprecated // use confirm2
	public static boolean confirm(Component parent, String title, String message)
	{
		final StandardDialog d = constructDialog(parent, CIcons.Question96, title, message);
		final AtomicBoolean confirmed = new AtomicBoolean();
		
		CAction okAction = new CAction()
		{
			public void action()
			{
				confirmed.set(true);
				d.close();
			}
		};
		
		d.addButton(new CButton(Menus.Cancel, d.closeAction));
		d.addButton(new CButton(Menus.OK, okAction));
		d.setButtonHighlight();
		d.setDefaultButton();
		d.open();
		
		return confirmed.get();
	}
	
	
	public static boolean confirm2(Component parent, String title, String message, String confirmButton)
	{
		final StandardDialog d = constructDialog(parent, CIcons.Question96, title, message);
		final AtomicBoolean confirmed = new AtomicBoolean();
		
		CAction okAction = new CAction()
		{
			public void action()
			{
				confirmed.set(true);
				d.close();
			}
		};
		
		d.addButton(new CButton(confirmButton, okAction));
		d.setButtonHighlight();
		d.setDefaultButton();
		d.addButton(new CButton(Menus.Cancel, d.closeAction));
		d.open();
		
		return confirmed.get();
	}

	
	@Deprecated // use ChoiceDialog, please
	public static int choice(Component parent, String title, String message, String[] choices)
	{
		final StandardDialog d = constructDialog(parent, CIcons.Question96, title, message);
		final AtomicInteger result = new AtomicInteger(-1);
		
		int ix = 0;
		for(String choice: choices)
		{
			final int rv = ix;
			CAction a = new CAction()
			{
				public void action()
				{
					result.set(rv);
					d.close();
				}
			};
			
			d.addButton(new CButton(choice, a));
			
			ix++;
		}
		
		d.setButtonHighlight();
		d.setDefaultButton();
		d.open();
		
		return result.get();
	}
	
	
	public static StandardDialog constructDialog(Component parent, Icon icon, String title, Object exceptionOrMessage)
	{
		StandardDialog d = new StandardDialog(parent);
		
		boolean error;
		if(exceptionOrMessage instanceof Throwable)
		{
			Throwable err = (Throwable)exceptionOrMessage;
			if(!(err instanceof UserException))
			{
				Log.err(err);
			}
			
			d.setTextError(err);
			error = true;
		}
		else if(exceptionOrMessage != null)
		{
			String s = exceptionOrMessage.toString();
			if(CKit.startsWithIgnoreCase(s, "<html>"))
			{
				d.setTextHtml(s);
			}
			else
			{
				d.setTextPlain(s);
			}
			error = false;
		}
		
		d.setLogo(icon);
		
		String ti = (title == null ? Application.getTitle() : title + " - " + Application.getTitle());
		d.setTitle(ti);
		
		d.setSize(550, 300);
		//d.setMinimumSize(500, 300);		
		return d;
	}

	
	protected static Window lastWindow()
	{
		return CFocusMonitor.getLastWindow();
	}
	
	
	@Deprecated // FIX
	public static JTextArea createTextArea(String message)
	{
		JTextArea t = new JTextArea(message);
		t.setWrapStyleWord(true);
		t.setLineWrap(true);
		t.setOpaque(false);
		t.setEditable(false);
		t.setFont(Theme.plainFont());
		return t;
	}
	
	
	public static CPanel createInfoPanel(String title, ImageIcon icon, String text)
	{
		JTextArea t = createTextArea(text);
		return createContentPanel(title, icon, t);
	}
	
	
	public static CPanel createHtmlPanel(String title, ImageIcon icon, String html)
	{
		CHtmlPane t = new CHtmlPane();
		t.setOpaque(false);
		t.setText(html);
		
		return createContentPanel(title, icon, t);
	}
	

	public static CPanel createContentPanel(String title, ImageIcon icon, JComponent content)
	{
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(Theme.titleFont());
		titleLabel.setBackground(Theme.panelBG().brighter());
		titleLabel.setOpaque(true);
		titleLabel.setBorder(new CBorder(0, 0, 1, 0, Color.gray, 20));

		JLabel iconLabel = new JLabel(icon);
		iconLabel.setBorder(new CBorder(10, 0, 0, 10));
		iconLabel.setPreferredSize(new Dimension(96, 96));

		content.setBorder(new CBorder(10, 10, 0, 10));

		CPanel p = new CPanel();
		p.setNorth(titleLabel);
		p.setWest(iconLabel);
		p.setCenter(content);
		return p;
	}
	

	public static void openOptions(Component parent, String title, OptionTreeNode root, String id, int width, int height)
    {
		COptionDialog d = new COptionDialog(parent, title, root, id);
		d.setSize(width, height);
		d.expandTree();
		d.open();
    }
	
	
	public static DiscardChanges discardChanges(Component parent)
	{
		int rv = Dialogs.choice
		(
			parent, 
			TXT.get("Dialogs.discard changes.title", "Discard changes?"), 
			TXT.get("Dialogs.discard changes.message", "The changes you've made will be lost.  Do you want to save the changes?"),
			new String[] { Menus.Save, Menus.DiscardChanges, Menus.Cancel }
		);
		switch(rv)
		{
		case 0:
			return DiscardChanges.SAVE;
		case 1:
			return DiscardChanges.DISCARD;
		default:
			return DiscardChanges.CANCEL;
		}
	}


	/** returns true if it's ok to overwrite */
	public static boolean checkFileExistsOverwrite(Component parent, File f)
	{
		if(f.exists())
		{
			ChoiceDialog d = new ChoiceDialog
			(
				parent,
				TXT.get("Dialogs.file exists.title", "File Exists"),
				TXT.get("Dialogs.file exists.message", "File {0} exists.  Do you want to overwrite it?", f)
			);
			d.addButton(Menus.Overwrite, 1, Theme.alternativeButtonHighlight());
			d.addButton(Menus.Cancel, 0, true);
			int rv = d.openChoiceDialog();
			switch(rv)
			{
			case 0:
			case -1:
				return false;
			}
		}
		return true;
	}
}
