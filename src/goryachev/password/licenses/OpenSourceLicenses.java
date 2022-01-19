// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.password.licenses;
import goryachev.common.util.CKit;
import goryachev.i18n.Menus;
import goryachev.swing.Application;
import goryachev.swing.CAction;
import goryachev.swing.dialogs.license.MultiPageDialog;
import goryachev.swing.img.jhlabs.PixelUtils;
import goryachev.swing.img.mortennobel.Lanczos3Filter;
import java.awt.Component;
import org.bouncycastle.LICENSE;


public class OpenSourceLicenses
{
	public static final CAction openDialogAction = new CAction() { public void action() { actionLicense(getSourceWindow()); } };

	
	private static void licenses(MultiPageDialog d)
	{
		d.addPage("BouncyCastle", LICENSE.licenseText);
		
		d.addPage("Java Image Scaling", CKit.readStringQuiet(Lanczos3Filter.class, "license.txt"));
		
		d.addPage("JH Labs Image Filters", CKit.readStringQuiet(PixelUtils.class, "License.txt"));
	}

	
	public static void actionLicense(Component parent)
	{
		MultiPageDialog d = new MultiPageDialog(parent, "OpenSourceLicenses");

		d.buttonPanel().addButton(Menus.OK, d.closeDialogAction, true);
		
		d.setTitle(Menus.OpenSourceLicenses + " - " + Application.getTitle());
		d.setSize(800, 500);
		d.split.setDividerLocation(300);
		
		licenses(d);
		
		d.autoResizeSplit();
		
		d.open();
	}
}
