// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.i18n;


public class NullPseudoLocalization
	extends PseudoLocalization
{
	public String getPrompt(String id, String master)
	{
		return master;
	}


	public String pseudoLocalize(String s)
	{
		return s;
	}
}
