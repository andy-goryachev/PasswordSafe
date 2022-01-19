// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.password.prompts;
import goryachev.common.log.Log;
import goryachev.i18n.CLanguageCode;
import goryachev.i18n.LocalizedPromptsProvider;
import goryachev.i18n.TXT;
import goryachev.swing.Appearance;


public class Prompts
{
	protected static final Log log = Log.get("Prompts");
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
			log.error(e);
		}		
		
		TXT.setLanguage(Appearance.getLanguage());
		TXT.checkTestMode();
	}
}
