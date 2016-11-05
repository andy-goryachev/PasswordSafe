// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password.prompts;
import goryachev.common.i18n.CLanguageCode;
import goryachev.common.i18n.LocalizedPromptsProvider;
import goryachev.common.i18n.TXT;
import goryachev.common.util.Log;
import goryachev.swing.Appearance;


public class Prompts
{
	public static final CLanguageCode[] LANGUAGES =
	{
		CLanguageCode.EnglishUS,
		CLanguageCode.Czech,
		CLanguageCode.French,
		CLanguageCode.German,
		CLanguageCode.HebrewIW,
		CLanguageCode.Japanese,
		CLanguageCode.Russian,
		CLanguageCode.ChineseSimplified
	};
	
	
	public static void init()
	{
		Appearance.setSupportedLanguages(LANGUAGES);

		try
		{
			TXT.setPromptProvider(LocalizedPromptsProvider.loadLocalResource(Prompts.class, "PasswordSafe"));
		}
		catch(Exception e)
		{
			Log.ex(e);
		}		
		
		TXT.setLanguage(Appearance.getLanguage());
		TXT.checkTestMode();
	}
}
