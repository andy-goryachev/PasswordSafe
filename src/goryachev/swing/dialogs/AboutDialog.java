// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.dialogs;
import goryachev.common.i18n.TXT;
import goryachev.swing.Application;
import goryachev.swing.CDialog;
import goryachev.swing.CHtmlPane;
import goryachev.swing.CTextPane;
import goryachev.swing.Dialogs;
import goryachev.swing.Panels;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.text.Document;


public class AboutDialog
	extends CDialog
{
	protected final JLabel iconField;
	protected final MouseAdapter clickHandler;
	
	
	public AboutDialog(Component parent)
	{
		super(parent, "AboutDialog", true);
		setTitle(TXT.get("AboutDialog.title.PRODUCT VERSION", "About {0} {1}", Application.getTitle(), Application.getVersion()));
		Dialogs.size(this);
		borderless();
		setCloseOnEscape();
		
		iconField = Panels.iconField();

		clickHandler = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ev) { close(); }
		};
		panel().addMouseListener(clickHandler);
		iconField.addMouseListener(clickHandler);
	}
	

	public void setLogo(Icon icon)
	{
		iconField.setIcon(icon);
		panel().setLeading(iconField);
	}
	
	
	public void setLogoComponent(Component c)
	{
		panel().setLeading(c);
		c.addMouseListener(clickHandler);
	}
	
	
	public void setTextHtml(String html)
	{
		CHtmlPane t = Panels.textHtml(html);
		t.setBorder(20);
		panel().setCenter(Panels.scroll(t));
	}
	
	
	public void setDocument(Document d)
	{
		CTextPane t = Panels.textPane(d);
		t.setBorder(20);
		panel().setCenter(Panels.scroll(t));
	}
}
