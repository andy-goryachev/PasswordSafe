// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.Appearance;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel3;
import goryachev.common.ui.InfoField;
import goryachev.common.ui.theme.AssignMnemonic;
import goryachev.common.util.CKit;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CList;
import goryachev.common.util.CLookup;
import goryachev.common.util.CSorter;
import goryachev.common.util.HasPrompts;
import goryachev.common.util.TXT;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Icon;
import javax.swing.JLabel;


public class LanguagePage
	extends CPanel3
	implements HasPrompts
{
	public final JLabel logoField;
	public final CComboBox languageField;
	public final JLabel selectLanguageLabel;
	public final InfoField infoField;
	protected boolean handleEvents = true;
	protected CLookup languages = new CLookup();
	
	
	public LanguagePage()
	{
		super(false);

		setName("LanguagePage");
		
		logoField = new JLabel();
		
		infoField = new InfoField();
		
		languageField = new CComboBox(getLanguages());
		languageField.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				languageUpdated();
			}
		});
		
		addColumns
		(
			CPanel3.PREFERRED,
			CPanel3.FILL
		);

		row(1, logoField);
		nextRow();
		row(0, 2, infoField);
		nextRow();
		row(0, selectLanguageLabel = label(" "));
		row(1, languageField);
		
		languageField.setSelectedItem(Appearance.getLanguage().getLocalName());
		TXT.registerListener(this);
		
		updatePrompts();
		TXT.registerListener(this);
	}
	
	
	public void setLogo(Icon icon)
	{
		logoField.setIcon(icon);
	}
	
	
	public void setInfo(String s)
	{
		infoField.setText(s);
	}

	
	public void updatePrompts()
	{
		selectLanguageLabel.setText(TXT.get("LanguagePanel.select language", "Select Language:"));
		
		AssignMnemonic.assign(this);
	}

	
	protected void languageUpdated()
	{
		if(handleEvents)
		{
			String s = (String)languageField.getSelectedItem();
			CLanguage la = (CLanguage)languages.lookup(s);
			if(la != null)
			{
				if(CKit.notEquals(la, TXT.getLanguage()))
				{
					handleEvents = false;
					
					Object x = languageField.getSelectedItem();
					languageField.replaceAll(getLanguages());
					languageField.setSelectedItem(x);
					
					Appearance.setLanguage(la);
					TXT.setLanguage(la);
					
					handleEvents = true;
				}
			}
		}
	}
	
	
	protected CList<String> getLanguages()
	{
		languages = new CLookup();
		
		CList<String> names = new CList();
		for(CLanguage la: Appearance.getSupportedLanguages())
		{
			String name = la.getLocalName();
			try
			{
				languages.add(la, name);
				names.add(name);
			}
			catch(Exception e)
			{ }
		}
		
		CSorter.sort(names);
		return names;
	}
	

	public Object getLanguage()
	{
		return languageField.getSelectedItem();
	}
}
