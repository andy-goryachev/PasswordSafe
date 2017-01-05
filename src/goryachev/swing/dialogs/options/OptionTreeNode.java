// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.dialogs.options;
import goryachev.common.util.CList;
import goryachev.swing.options.OptionEditorInterface;
import goryachev.swing.table.CTreeNode2;
import javax.swing.ImageIcon;


public class OptionTreeNode
	extends CTreeNode2
{
	private ImageIcon icon;
	private String name;
	private CList<OptionEntry> entries = new CList();
	
	
	public OptionTreeNode(ImageIcon icon, String name)
	{
		super(name);
		this.icon = icon;
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public ImageIcon getIcon()
	{
		return icon;
	}
	

	public OptionEntry addSection(String s)
	{
		OptionEntry en = new OptionEntry(s, null, true);
		entries.add(en);
		return en;
	}
	

	public OptionEntry addOption(String name, OptionEditorInterface ed)
	{
		OptionEntry en = new OptionEntry(name, ed, false);
		entries.add(en);
		return en;
	}
	
	
	public void addOptionEntry(OptionEntry en)
	{
		entries.add(new OptionEntry(en.getName(), en.getEditor(), en.isSection()));
	}


	public OptionEntry[] getOptionEntries()
	{
		return entries.toArray(new OptionEntry[entries.size()]);
	}
	
	
	public int getOptionEntryCount()
	{
		return entries.size();
	}
	
	
	public OptionEntry getLastEntry()
	{
		int n = entries.size() - 1;
		if(n >= 0)
		{
			return entries.get(n);
		}
		return null;
	}
}
