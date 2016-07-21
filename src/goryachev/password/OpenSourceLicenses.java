// Copyright © 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.dialogs.license.MultiPageDialog;
import goryachev.common.ui.img.jhlabs.PixelUtils;
import goryachev.common.ui.img.mortennobel.Lanczos3Filter;
import goryachev.common.util.CKit;
import goryachev.common.util.Menus;
import java.awt.Component;
import org.bouncycastle.LICENSE;


public class OpenSourceLicenses
{
	public static final CAction action = new CAction() { public void action() { actionLicense(getSourceWindow()); } };

	
	private static void licenses(MultiPageDialog d)
	{
		d.addPage("BouncyCastle", LICENSE.licenseText);
		
		d.addPage("java-image-scaling", CKit.readStringQuiet(Lanczos3Filter.class, "license.txt"));
		
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
