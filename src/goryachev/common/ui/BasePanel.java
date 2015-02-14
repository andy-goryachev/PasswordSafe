// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.UserException;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTextArea;


/** Universal CPanel with commonly used UI capabilities such as toolbar and button panel, created upon demand. */
// TODO StandardDialogPanel, pick one
public class BasePanel
	extends CPanel
{
	protected CToolBar toolbar;
	protected CButtonPanel buttons;


	public BasePanel()
	{
		setBackground(Theme.textBG());
	}
	
	
	public BasePanel(Component c)
	{
		setCenter(c);
	}
	
	
	public CToolBar toolbar()
	{
		if(toolbar == null)
		{
			toolbar = createToolBar();
			setNorth(toolbar);
		}
		return toolbar;
	}
	
	
	protected CToolBar createToolBar()
	{
		return Theme.toolbar();
	}
	
	
	public CButtonPanel buttons()
	{
		if(buttons == null)
		{
			buttons = createButtonPanel();
			setSouth(buttons);
		}
		return buttons;
	}
	
	
	protected CButtonPanel createButtonPanel()
	{
		CButtonPanel b = new CButtonPanel();
		b.setBorder(Theme.BORDER_10);
		return b;
	}
	
	
	public void validateRepaint()
	{
		validate();
		repaint();
	}
	
	
	public InfoField setCenterInfo(String s)
	{
		return setCenterInfo(s, null);
	}
	
	
	public InfoField setCenterInfo(String s, Color c)
	{
		InfoField t = new InfoField(s);
		t.setBorder(Theme.BORDER_10);
		if(c != null)
		{
			t.setBackground(c);
			t.setOpaque(true);
		}
		
		setCenter(t);
		validateRepaint();
		return t;
	}


	public JTextArea setCenterTextArea()
	{
		JTextArea t = new JTextArea();
		t.setBorder(new CBorder(4));
		t.setFont(Theme.plainFont());
		t.setEditable(false);
		t.setWrapStyleWord(true);
		t.setLineWrap(true);
		
		CScrollPane scroll = new CScrollPane(t, false);
		setCenter(scroll);
		validateRepaint();
		return t;
	}
	
	
	public CTextPane setCenterTextPane()
	{
		CTextPane t = new CTextPane();
		t.setBorder(new CBorder(4));
		t.setFont(Theme.plainFont());
		t.setEditable(false);
		t.setScrollableTracksViewportWidth(true);
		
		CScrollPane scroll = new CScrollPane(t, false);
		setCenter(scroll);
		validateRepaint();
		return t;
	}
	
	
	public JTextArea setCenterText(String text)
	{
		JTextArea t = setCenterTextArea();
		t.setBorder(Theme.BORDER_10);
		t.setEditable(false);
		t.setText(text);
		t.setCaretPosition(0);
		return t;
	}
	
	
	public JTextArea setCenterError(Throwable e)
	{
		String msg;
		// TODO handle interrupted/cancelled exception?
		if(e instanceof UserException)
		{
			msg = e.getMessage();
		}
		else
		{
			msg = CKit.stackTrace(e);
		}
		
		return setCenterText(msg);
	}

	
	public CHtmlPane setCenterHtml()
	{
		CHtmlPane t = new CHtmlPane();
		t.setEditable(false);
		
		CScrollPane scroll = new CScrollPane(t, false);
		setCenter(scroll);
		validateRepaint();
		return t;
	}
	
	
	public CHtmlPane setCenterHtml(String html)
	{
		CHtmlPane t = setCenterHtml();
		t.setText0(html);
		return t;
	}
	

	public CTextPane setCenterCTextPane()
	{
		CTextPane t = new CTextPane();
		t.setEditable(false);
		t.setScrollableTracksViewportWidth(true);
		
		CScrollPane scroll = new CScrollPane(t, false);
		setCenter(scroll);
		validateRepaint();
		return t;
	}
	
	
	public JLabel setIcon(Icon ic)
	{
		JLabel t = new JLabel(ic);
		t.setBorder(new CBorder(10));
		t.setVerticalAlignment(JLabel.TOP);
		setWest(t);
		validateRepaint();
		return t;
	}
}
