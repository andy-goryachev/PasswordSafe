// Copyright © 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.i18n.TXT;
import goryachev.swing.Accelerator;
import java.awt.event.KeyEvent;


public class Accelerators
{
	public static final String MAIN_MENU = TXT.get("Accelerators.category.menu.File","Main Menu: File");
	public static final String MENU_EDIT = TXT.get("Accelerators.category.menu.Edit","Main Menu: Edit");
	
	
	public static Accelerator OPEN = new Accelerator
	(
		"open",
		MAIN_MENU,
		TXT.get("Accelerators.open","Open"),
		null
	);
	
	public static Accelerator SAVE = new Accelerator
	(
		"save",
		MAIN_MENU,
		TXT.get("Accelerators.save","Save"),
		KeyEvent.VK_S, 
		true
	);
	
	public static Accelerator SAVE_AS = new Accelerator
	(
		"save.as",
		MAIN_MENU,
		TXT.get("Accelerators.save.as","Save As"),
		null
	);
	
	public static Accelerator PREFERENCES = new Accelerator
	(
		"preferences",
		MAIN_MENU,
		TXT.get("Accelerators.preferences","Preferences"),
		null
	);
	
	public static Accelerator UNDO = new Accelerator
	(
		"undo",
		MENU_EDIT,
		TXT.get("Accelerators.undo","Undo"),
		KeyEvent.VK_Z,
		true
	);
	
	public static Accelerator REDO = new Accelerator
	(
		"redo",
		MENU_EDIT,
		TXT.get("Accelerators.redo","Redo"),
		KeyEvent.VK_Y,
		true
	);
}
