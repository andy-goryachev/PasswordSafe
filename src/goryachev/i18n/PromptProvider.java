// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.i18n;

public interface PromptProvider
{
	public void setLanguage(CLanguage language);


	public String getPrompt(String id, String master);
}
