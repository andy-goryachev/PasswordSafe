// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.i18n.TXT;
import goryachev.common.util.ProductInfo;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.swing.Application;
import goryachev.swing.dialogs.AboutDialog;
import goryachev.swing.text.CDocumentBuilder;
import java.awt.Component;


public class About
{
	public static void about(Component parent)
	{
		CDocumentBuilder b = new CDocumentBuilder();
		b.a(TXT.get("About.product", "Simple, secure, portable password storage.")).nl(2);
		
		b.a(TXT.get("About.version", "Version")).sp().a(Application.getVersion()).nl();
		b.a(Application.getCopyright()).nl(2);
		
		b.link(ProductInfo.getWebSite());
		
		AboutDialog d = new AboutDialog(parent);
		d.setLogo(PasswordSafeIcons.Logo);
		d.setDocument(b.getDocument());
		d.open();
	}
}
