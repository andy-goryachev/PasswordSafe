// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.i18n.Menus;
import goryachev.common.i18n.TXT;
import goryachev.swing.Appearance;
import goryachev.swing.Dialogs;
import goryachev.swing.dialogs.options.OptionTreeBuilder;
import goryachev.swing.dialogs.options.ThemeOptionEditor;
import goryachev.swing.options.FileOption;
import goryachev.swing.options.IntegerOption;
import goryachev.swing.options.LongOption;
import goryachev.swing.options.edit.IntegerOptionEditor;
import goryachev.swing.options.edit.KeyBindingsEditor;
import javax.swing.JFrame;


public class Preferences
{
	public static final LongOption licenseAcceptedOption = new LongOption("license.accepted", -1);
	public static final FileOption dataFileOption = new FileOption("data.file");
	public static final IntegerOption lockTimeoutOption = new IntegerOption("lock.timeout2", 5);

	
	public static void openPreferences(JFrame parent)
	{
		OptionTreeBuilder b = new OptionTreeBuilder();
		
		// appearance
		b.addChild(Menus.Appearance);
		{
			// theme
			b.addChild(Menus.Theme);
			{
				b.addOption(new ThemeOptionEditor());
			}
			b.end();
			
			b.addOption(Menus.InterfaceLanguage, Appearance.getLanguageEditor());
			b.setRestartRequired();
		}
		b.end();
		
		// security
		b.addChild(TXT.get("Preferences.group.security", "Security"));
		b.addOption(TXT.get("Preferences.security.lock timeout in minutes", "Lock timeout (minutes)"), new IntegerOptionEditor(lockTimeoutOption));
		b.setRestartRequired();
		b.end();
		
		// keys
		b.addChild(Menus.Keys);
		b.addOption(new KeyBindingsEditor());
		b.setRestartRequired();
		b.end();
		
		Dialogs.openOptions(parent, Menus.Preferences, b.getRoot(), "Preferences", 700, 500);
	}
}
