// Copyright © 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.Application;
import goryachev.common.ui.dialogs.AboutDialog;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.common.util.ProductInfo;
import goryachev.common.util.TXT;
import goryachev.password.img.PasswordSafeIcons;
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
