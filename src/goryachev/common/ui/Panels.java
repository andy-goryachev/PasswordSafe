// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


public class Panels
{
	public static JLabel iconField(Icon ic)
	{
		JLabel t = iconField();
		t.setIcon(ic);
		return t;
	}
	
	
	public static JLabel iconField()
	{
		JLabel t = new JLabel();
		t.setVerticalAlignment(JLabel.TOP);
		t.setBorder(new CBorder(20));
		t.setOpaque(true);
		t.setBackground(UI.mix(Theme.textFG(), 0.04, Theme.textBG()));
		return t;
	}
	
	
	public static CTextPane textPane()
	{
		CTextPane t = new CTextPane();
		t.setBorder(new CBorder(4));
		t.setFont(Theme.plainFont());
		t.setEditable(false);
		t.setScrollableTracksViewportWidth(true);
		return t;
	}
	
	
	public static CTextPane textPane(String text)
	{
		CTextPane t = textPane();
		t.setText0(text);
		return t;
	}
	
	
	public static CScrollPane scrollText(String text)
	{
		CTextPane t = textPane();
		t.setText(text);
		t.setCaretPosition(0);
		
		return scroll(t);
	}
	
	
	public static CHtmlPane textHtml()
	{
		CHtmlPane t = new CHtmlPane();
		t.setEditable(false);
		return t;
	}
	
	
	public static CHtmlPane textHtml(String html)
	{
		CHtmlPane t = textHtml();
		t.setText(html);
		t.setCaretPosition(0);
		return t;
	}
	
	
	public static CScrollPane scrollHtml(String html)
	{
		CHtmlPane t = textHtml(html);
		return scroll(t);
	}
	
	
	public static CScrollPane scroll(Component c)
	{
		CScrollPane p = new CScrollPane(c, false);
		p.setBackground2(Theme.textBG());
		return p;
	}
	
	
	public static CScrollPane scroll(Component c, boolean horScrollBar)
	{
		CScrollPane p = new CScrollPane(c, horScrollBar);
		p.setBackground2(Theme.textBG());
		return p;
	}
	
	
	public static CTextPane textPane(Document d)
	{
		CTextPane t = new CTextPane();
		t.setDocument(d);
		t.setOpaque(false);
		t.setEditable(false);
		t.setBorder(new CBorder(4));
		t.setScrollableTracksViewportWidth(true);
		t.setCaretPosition(0);
		
		return t;
	}
	
	
	public static JTextComponent textComponent(String s)
	{
		if(CKit.startsWithIgnoreCase(s, "<html>"))
		{
			return textHtml(s);
		}
		else
		{
			return textPane(s);
		}
	}
	
	
	public static CScrollPane scrollDocument(Document d)
	{
		CTextPane t = textPane(d);
		return scroll(t);
	}
	
	
	public static CTextArea textArea(String message, boolean wrap)
	{
		CTextArea t = textArea(wrap);
		t.setText0(message);
		return t;
	}
	
	
	public static CTextArea textArea(boolean wrap)
	{
		CTextArea t = new CTextArea();
		t.setMinimumSize(new Dimension(20, 1));
		if(wrap)
		{
			t.setWrapStyleWord(true);
			t.setLineWrap(true);
		}
		t.setOpaque(false);
		t.setEditable(false);
		t.setFont(Theme.plainFont());
		t.setBorder(new CBorder(4));
		return t;
	}
	
	
	
//	@Deprecated // FIX
//	public static JTextArea createTextArea(String message)
//	{
//		JTextArea t = new JTextArea(message);
//		t.setWrapStyleWord(true);
//		t.setLineWrap(true);
//		t.setOpaque(false);
//		t.setEditable(false);
//		t.setFont(Theme.plainFont());
//		return t;
//	}
//	
//	
//	public static CPanel createInfoPanel(String title, ImageIcon icon, String text)
//	{
//		JTextArea t = createTextArea(text);
//		return createContentPanel(title, icon, t);
//	}
//	
//	
//	public static CPanel createHtmlPanel(String title, ImageIcon icon, String html)
//	{
//		CHtmlPane t = new CHtmlPane();
//		t.setOpaque(false);
//		t.setText(html);
//		
//		return createContentPanel(title, icon, t);
//	}
//	
//
//	public static CPanel createContentPanel(String title, ImageIcon icon, JComponent content)
//	{
//		JLabel titleLabel = new JLabel(title);
//		titleLabel.setFont(Theme.titleFont());
//		titleLabel.setBackground(Theme.panelBG().brighter());
//		titleLabel.setOpaque(true);
//		titleLabel.setBorder(new CBorder(0, 0, 1, 0, Color.gray, 20));
//
//		JLabel iconLabel = new JLabel(icon);
//		iconLabel.setBorder(new CBorder(10, 0, 0, 10));
//		iconLabel.setPreferredSize(new Dimension(96, 96));
//
//		content.setBorder(new CBorder(10, 10, 0, 10));
//
//		CPanel p = new CPanel();
//		p.setNorth(titleLabel);
//		p.setWest(iconLabel);
//		p.setCenter(content);
//		return p;
//	}
	
	
//	public CTextArea textArea()
//	{
//		CTextArea t = new CTextArea();
//		t.setLineWrap(true);
//		t.setWrapStyleWord(true);
//		t.setFont(Theme.plainFont());
//		t.setBorder(Theme.BORDER_FIELD);
//		return t;
//	}
}
