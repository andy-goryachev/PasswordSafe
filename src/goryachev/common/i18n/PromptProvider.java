// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.i18n;

public interface PromptProvider
{
	public void setLanguage(CLanguage language);


	public String getPrompt(String id, String master);
}
