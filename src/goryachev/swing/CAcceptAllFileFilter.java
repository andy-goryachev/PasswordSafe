// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.i18n.TXT;
import java.io.File;
import javax.swing.filechooser.FileFilter;


public class CAcceptAllFileFilter
	extends FileFilter
{
	public boolean accept(File f)
	{
		return true;
	}


	public String getDescription()
	{
		return TXT.get("CAcceptAllFileFilter.accept all files", "All Files");
	}
}
