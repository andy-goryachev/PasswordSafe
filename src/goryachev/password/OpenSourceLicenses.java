// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CButtonPanel;
import goryachev.common.ui.Menus;
import goryachev.common.ui.dialogs.license.MultiPageDialog;
import goryachev.common.util.CKit;
import goryachev.common.util.img.jhlabs.PixelUtils;
import goryachev.common.util.img.mortennobel.Lanczos3Filter;
import info.clearthought.layout.TableLayout;
import java.awt.Component;
import org.bouncycastle.LICENSE;


public class OpenSourceLicenses
{
	public static final CAction action = new CAction() { public void action() { actionLicense(getSourceWindow()); } };

	
	private static void licenses(MultiPageDialog d)
	{
		d.addPage("BouncyCastle", LICENSE.licenseText);
		
		d.addPage("Clearthought Table Layout", CKit.readStringQuiet(TableLayout.class, "License.txt"));
		
		d.addPage("java-image-scaling", CKit.readStringQuiet(Lanczos3Filter.class, "license.txt"));
		
		d.addPage("JH Labs Image Filters", CKit.readStringQuiet(PixelUtils.class, "License.txt"));
	}

	
	public static void actionLicense(Component parent)
	{
		MultiPageDialog d = new MultiPageDialog(parent, "OpenSourceLicenses");

		CButtonPanel bp = new CButtonPanel(10);
		bp.setBorder(new CBorder(10));
		bp.add(new CButton(Menus.OK, d.closeAction, true));
		d.panel.setSouth(bp);
		
		d.setTitle(Menus.OpenSourceLicenses + " - " + Application.getTitle());
		d.setSize(900, 500);
		
		licenses(d);
		
		d.autoResizeSplit();
		d.open();
	}
}
