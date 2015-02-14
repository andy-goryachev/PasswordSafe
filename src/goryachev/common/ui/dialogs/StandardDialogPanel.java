// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CButtonPanel;
import goryachev.common.ui.CHtmlPane;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CTextPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.CKit;
import goryachev.common.util.UserException;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.Document;


// TODO or BasePanel, make up your mind
public class StandardDialogPanel
	extends CPanel
{
	public static final CBorder LINE_BORDER = new CBorder(0, 0, 1, 0, Theme.panelBG().darker());

	public final CPanel panel;
	public final CPanel contentPanel;
	public final JLabel iconField;
	protected CButtonPanel buttons;
	private JButton defaultButton;

	
	public StandardDialogPanel()
	{
		contentPanel = new CPanel();
		//contentPanel.setOpaque(false);
		contentPanel.setBackground(Theme.textBG());
		
		iconField = new JLabel();
		iconField.setVerticalAlignment(JLabel.TOP);
		iconField.setBorder(new CBorder(20)); //20, 40, 20, 40));
		iconField.setOpaque(true);
		iconField.setBackground(UI.mix(10, Theme.textFG(), Theme.textBG()));
		
		panel = new CPanel();
		panel.setLayout
		(
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.FILL
			},
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL
			}
		);
		panel.add(1, 2, 1, 3, contentPanel);
		
		setCenter(panel);
	}
	
	
	public void setIcon(Icon ic)
	{
		iconField.setIcon(ic);
		
		panel.add(0, 2, 0, 3, iconField);
		validate();
		repaint();
	}
	
	
	public void setIconComponent(Component c)
	{
		panel.add(0, 2, 0, 3, c);
		validate();
		repaint();
	}
	
	
	public void setContent(JComponent c)
	{
		contentPanel.setCenter(c);
		validate();
		repaint();
	}
	
	
	public void setTextPlain(String plainText)
	{
		JTextArea t = createTextArea(plainText, true);
		
		CScrollPane scroll = new CScrollPane(t, true);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		
		setContent(scroll);
	}
	
	
	public void setTextDocument(Document d)
	{
		CTextPane t = new CTextPane();
		t.setDocument(d);
		t.setOpaque(false);
		t.setEditable(false);
		t.setBorder(new CBorder(20));
		t.setScrollableTracksViewportWidth(true);
		
		CScrollPane scroll = new CScrollPane(t, false);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		
		setContent(scroll);
	}


	public void setTextHtml(String html)
	{
		CHtmlPane t = new CHtmlPane();
		t.setOpaque(false);
		t.setText(html);
		t.setBorder(new CBorder(20));

		CScrollPane scroll = new CScrollPane(t, false);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		setContent(scroll);
	}
	
	
	public void setText(String s)
	{
		if(CKit.startsWithIgnoreCase(s, "<html>"))
		{
			setTextHtml(s);
		}
		else
		{
			setTextPlain(s);
		}
	}
	
	
	public void setTextError(Throwable e)
	{
		boolean wrap = false;
		String msg = null;
		if(e instanceof UserException)
		{
			msg = e.getMessage();
			wrap = true;
		}
		
		if(CKit.isBlank(msg))
		{
			msg = CKit.stackTrace(e);
		}
		
		JTextArea t = createTextArea(msg, false);
		t.setLineWrap(wrap);
		if(wrap)
		{
			t.setWrapStyleWord(true);
		}
		
		CScrollPane scroll = new CScrollPane(t, true);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		setContent(scroll);
	}


	public static JTextArea createTextArea(String message, boolean wrap)
	{
		JTextArea t = new JTextArea(message);
		t.setMinimumSize(new Dimension(20, 1));
		if(wrap)
		{
			t.setWrapStyleWord(true);
			t.setLineWrap(true);
		}
		t.setOpaque(false);
		t.setEditable(false);
		t.setFont(Theme.plainFont());
		t.setBorder(new CBorder(20));
		return t;
	}
	
	
	public CButtonPanel buttons()
	{
		if(buttons == null)
		{
			buttons = new CButtonPanel();
			buttons.setBorder(new CBorder(1, 0, 0, 0, Theme.panelBG().darker(), 10));
			
			panel.setSouth(buttons);
			validate();
			repaint();
		}
		return buttons;
	}
	
	
	public void addButton(JButton b)
	{
		buttons().add(b, 0);
	}
	
	
	public void addButton(String text, Action a)
	{
		addButton(new CButton(text, a));
	}
	
	
	public void addButton(Action a)
	{
		addButton(new CButton(a));
	}
	
	
	public void setButtonHighlight()
	{
		((CButton)(buttons().getLastButton())).setHighlight();
	}
	
	
	public void addButtonSpace()
	{
		buttons().addSpace();
	}
	
	
	public void setIconQuestion()
	{
		setIcon(CIcons.Question96);
	}
	
	
	public void setIconWarning()
	{
		setIcon(CIcons.Warning96);
	}
	
	
	public void setIconInfo()
	{
		setIcon(CIcons.Info96);
	}
	
	
	public void setIconSuccess()
	{
		setIcon(CIcons.Success96);
	}
	
	
	public void setIconCancelled()
	{
		setIcon(CIcons.Cancelled96);
	}
}
