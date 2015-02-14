// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.Appearance;
import goryachev.common.ui.Menus;
import goryachev.common.ui.dialogs.options.COptionDialog;
import goryachev.common.ui.dialogs.options.OptionTreeBuilder;
import goryachev.common.ui.options.FileOption;
import goryachev.common.ui.options.IntegerOption;
import goryachev.common.ui.options.LongOption;
import goryachev.common.ui.options.edit.IntegerOptionEditor;
import goryachev.common.ui.options.edit.KeyBindingsEditor;
import goryachev.common.util.TXT;
import javax.swing.JFrame;


public class Preferences
{
	public static final LongOption licenseAcceptedOption = new LongOption("license.accepted", -1);
	public static final FileOption dataFileOption = new FileOption("data.file");
	public static final IntegerOption lockTimeoutOption = new IntegerOption("lock.timeout", 15*60000);

	
	public static void openPreferences(JFrame parent)
	{
		OptionTreeBuilder b = new OptionTreeBuilder();
		
		// appearance
		b.addChild(Menus.Appearance);
		b.addOption(Menus.InterfaceLanguage, Appearance.getLanguageEditor());
		b.setRestartRequired();
		b.end();
		
		// security
		b.addChild(TXT.get("Preferences.group.security", "Security"));
		b.addOption(TXT.get("Preferences.security.lock timeout", "Lock timeout (milliseconds)"), new IntegerOptionEditor(lockTimeoutOption));
		b.end();
		
		// keys
		b.addChild(Menus.Keys);
		b.addOption(new KeyBindingsEditor());
		b.setRestartRequired();
		b.end();
		
		new COptionDialog(parent, Menus.Preferences, b.getRoot(), "Preferences").open();
	}
}
