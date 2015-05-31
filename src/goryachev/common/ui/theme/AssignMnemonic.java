// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CButtonPanel3;
import goryachev.common.util.CKit;
import goryachev.common.util.CUnique;
import goryachev.common.util.SB;
import java.awt.Component;
import java.awt.Container;
import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class AssignMnemonic
{
	public static void assign(Component x)
	{
		if(x instanceof JMenuBar)
		{
			assignMenuBar((JMenuBar)x);
		}
		else if(x instanceof JMenu)
		{
			assignMenu((JMenu)x);
		}
		else if(x instanceof JPopupMenu)
		{
			assignPopupMenu((JPopupMenu)x);
		}
		else if(x instanceof CButtonPanel3) 
		{
			assignButtonPanel((Container)x);
		}
		else if(x instanceof Container)
		{
			Container c = (Container)x;
			int sz = c.getComponentCount();
			for(int i=0; i<sz; i++)
			{
				Component ch = c.getComponent(i);
				assign(ch);
			}
		}
	}
	
	
	private static void assignMenuBar(JMenuBar mb)
	{
		CUnique<Character> u = new CUnique();
		
		int ct = mb.getMenuCount();
		for(int i=0; i<ct; i++)
		{
			JMenu m = mb.getMenu(i);
			if(m != null)
			{
				assign(m, u);
			}
		}
	}
	
	
	private static void assignMenu(JMenu mb)
	{
		CUnique<Character> u = new CUnique();
		
		int ct = mb.getMenuComponentCount();
		for(int i=0; i<ct; i++)
		{
			Component m = mb.getMenuComponent(i);
			if(m instanceof JMenuItem)
			{
				assign((JMenuItem)m, u);
			}
		}
	}
	
	
	private static void assignPopupMenu(JPopupMenu mb)
	{
		CUnique<Character> u = new CUnique();
		
		int ct = mb.getComponentCount();
		for(int i=0; i<ct; i++)
		{
			Component m = mb.getComponent(i);
			if(m instanceof JMenuItem)
			{
				assign((JMenuItem)m, u);
			}
		}
	}
	
	
	private static void assignButtonPanel(Container p)
	{
		CUnique<Character> u = new CUnique();
		
		int ct = p.getComponentCount();
		for(int i=0; i<ct; i++)
		{
			Component m = p.getComponent(i);
			if(m instanceof AbstractButton)
			{
				assignButton((AbstractButton)m, u);
			}
		}
	}
	

	private static void assign(JMenuItem m, CUnique<Character> u)
	{
		String text = m.getText();
		int mnem = identifyMnemonic(text, u);
		if(mnem >= 0)
		{
			m.setMnemonic(mnem);
		}
	}
	
	
	private static void assignButton(AbstractButton b, CUnique<Character> u)
	{
		String text = b.getText();
		int mnem = identifyMnemonic(text, u);
		if(mnem >= 0)
		{
			b.setMnemonic(mnem);
		}
	}


	private static int identifyMnemonic(String text, CUnique<Character> u)
	{
		if(text != null)
		{
			if(CKit.startsWithIgnoreCase(text, "<html>"))
			{
				// FIX problem: does not underline mnemonic char if html (bug in java)
				text = stripHtmlTags(text);
			}
			
			for(int i=0; i<text.length(); i++)
			{
				char c = text.charAt(i);
				if(Character.isLetterOrDigit(c))
				{
					char up = Character.toUpperCase(c);
					if(!u.contains(up))
					{
						u.put(up);
						return c;
					}
				}
			}
		}
		
		return -1;
	}


	// removes html tags <...>, expecting well formed html 
	private static String stripHtmlTags(String text)
	{
		int sz = text.length();
		SB sb = new SB(sz);
		boolean tag = false;
		
		for(int i=0; i<sz; i++)
		{
			char c = text.charAt(i);
			if(tag)
			{
				if(c == '>')
				{
					tag = false;
				}
			}
			else
			{
				if(c == '<')
				{
					tag = true;
				}
				else
				{
					sb.append(c);
				}
			}
		}
		
		return sb.toString();
	}
}
