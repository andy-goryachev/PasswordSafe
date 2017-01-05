// Copyright © 2008-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options;
import goryachev.common.i18n.CLanguage;


public class LanguageOption
	extends COption<CLanguage>
{
	public LanguageOption(String id)
	{
		super(id);
	}


	public CLanguage defaultValue()
	{
		return CLanguage.getDefault();
	}


	public String toString()
	{
		return get().getName();
	}


	public String toProperty(CLanguage lang)
	{
		return lang.getID();
	}


	public CLanguage parseProperty(String s)
	{
		return CLanguage.parse(s);
	}
}
