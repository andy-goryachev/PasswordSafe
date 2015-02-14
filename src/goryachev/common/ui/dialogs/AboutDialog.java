// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs;
import goryachev.common.ui.Application;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Menus;
import goryachev.common.util.TXT;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.text.Document;


public class AboutDialog
    extends CDialog
{
	public final StandardDialogPanel panel;
	
	
	public AboutDialog(Component parent)
	{
		super(parent, "AboutDialog", true);
		setTitle(TXT.get("AboutDialog.title.PRODUCT VERSION", "About {0} {1}", Application.getTitle(), Application.getVersion()));
		setMinimumSize(550, 300);
		setSize(550, 400);

		panel = new StandardDialogPanel();
		
		MouseAdapter ma = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ev) { close(); }
		};
		panel.addMouseListener(ma);
		panel.iconField.addMouseListener(ma);
		
		CButton okButton = new CButton(Menus.OK, closeAction);
		okButton.setHighlight();

		CPanel p = new CPanel();
		p.setCenter(panel);

		setContentPanel(p);
	}
	

	public void setLogo(Icon icon)
	{
		panel.setIcon(icon);
	}
	
	
	public void setLogoComponent(Component c)
	{
		panel.setIconComponent(c);
	}
	
	
	public void setTextHtml(String html)
	{
		panel.setTextHtml(html);
	}
	
	
	public void setDocument(Document d)
	{
		panel.setTextDocument(d);
	}
}
