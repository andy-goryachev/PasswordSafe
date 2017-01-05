// Copyright © 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.i18n;


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
