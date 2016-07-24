// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password.prompts;
import goryachev.common.swing.Appearance;
import goryachev.common.util.CLanguageCode;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.common.util.lz.LocalizedPromptsProvider;


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
			Log.fail(e);
		}		
		
		TXT.setLanguage(Appearance.getLanguage());
		TXT.checkTestMode();
	}
}
