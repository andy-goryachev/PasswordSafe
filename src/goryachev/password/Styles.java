// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.cryptoswing.OnScreenKeyboard;
import goryachev.i18n.TXT;
import goryachev.password.img.PasswordSafeIcons;
import goryachev.swing.CExtensionFileFilter;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;


public class Styles
{
	public static FileFilter createFileFilter()
	{
		String ext = PasswordSafeApp.EXTENSION;
		return new CExtensionFileFilter(TXT.get("Styles.file filter.PASSWORDSAFE files (EXTENSION)", "{0} files ({1})", PasswordSafeApp.TITLE, "*" + ext), ext);
	}


	public static JLabel logo()
	{
		JLabel t = new JLabel(PasswordSafeIcons.Logo);
		t.setHorizontalAlignment(JLabel.LEADING);
		return t;
	}


	public static OnScreenKeyboard createKeyboard()
	{
		return OnScreenKeyboard.create();
	}
}
