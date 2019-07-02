// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import goryachev.common.i18n.CLanguageCode;


// http://en.wikipedia.org/wiki/Keyboard_layout
public class KeyboardLayouts
{
	public static final CLanguageCode[] SUPPORTED_LAYOUTS =
	{
		//CLanguageCode.Czech,
		CLanguageCode.HebrewIW,
		CLanguageCode.German,
		CLanguageCode.English,
		CLanguageCode.Russian
	};
	
	
	public static void build(OnScreenKeyboard k, CLanguageCode c)
	{
		if(c == null)
		{
			c = CLanguageCode. EnglishUS;
		}
		
		switch(c)
		{
//		case Czech:
//			buildCzech(k);
//			break;
		case German:
			buildGerman(k);
			break;
		case Hebrew:
		case HebrewIW:
			buildHebrew(k);
			break;
		case Russian:
			buildRussian(k);
			break;
		default:
			buildEnglishUS(k);
			break;
		}
	}
	

	public static OnScreenKeyboard buildEnglishUS(OnScreenKeyboard kb)
	{
		KeyboardBuilder b = new KeyboardBuilder(kb);
		b.add("`~", "1!", "2@", "3#", "4$", "5%", "6^", "7&", "8*", "9(", "0)", "-_", "=+");
		b.add(VCode.Backspace, 150);
		b.newRow();
		b.add(VCode.Tab, 150);
		b.add("qQ", "wW", "eE", "rR", "tT", "yY",  "uU",  "iI",  "oO",  "pP",  "[{",  "]}",  "\\|");
		b.newRow();
		b.add(VCode.CapsLock, 175);
		b.add("aA", "sS", "dD", "fF", "gG", "hH", "jJ", "kK", "lL", ";:", "'\"");
		b.add(VCode.Enter, 175);
		b.newRow();
		b.add(VCode.Shift, 225);
		b.add("zZ", "xX", "cC", "vV", "bB", "nN", "mM", ",<", ".>", "/?");
		b.add(VCode.Shift, 227);
		b.newRow();
		b.add(VCode.National, 200);
		b.add(VCode.Empty, 200);
		b.add(" ", 600);
		return b.getKeyboard();
	}
	
	
	// needs additional modes (with circle, caron, etc.)
	public static OnScreenKeyboard buildCzech(OnScreenKeyboard kb)
	{
		KeyboardBuilder b = new KeyboardBuilder(kb);
		// ;+ěščřžýáíé=
		b.add(";`", "+1!", "2=", "3№", "4;", "5%", "6:", "7?", "8*", "9(", "0)", "-_", "=+");
		b.add(VCode.Backspace, 150);
		b.newRow();
		b.add(VCode.Tab, 150);
		b.add("йЙ", "цЦ", "уУ", "кК", "еЕ", "нН",  "гГ",  "шШ",  "щЩ",  "зЗ",  "хХ",  "ъЪ",  "\\/");
		b.newRow();
		b.add(VCode.CapsLock, 175);
		b.add("фФ", "ыЫ", "вВ", "аА", "пП", "рР", "оО", "лЛ", "дД", "жЖ", "эЭ");
		b.add(VCode.Enter, 175);
		b.newRow();
		b.add(VCode.Shift, 225);
		b.add("яЯ", "чЧ", "сС", "мМ", "иИ", "тТ", "ьЬ", "бБ", "юЮ", ".,");
		b.add(VCode.Shift, 227);
		b.newRow();
		b.add(VCode.National, 200);
		b.add(VCode.Empty, 200);
		b.add(" ", 600);
		return b.getKeyboard();
	}
	
	
	public static OnScreenKeyboard buildHebrew(OnScreenKeyboard kb)
	{
		KeyboardBuilder b = new KeyboardBuilder(kb);
		b.add
		(
			"`~", 
			"1!", 
			"2@", 
			"3#", 
			"4$₪",
			"5%",
			"6^", 
			"7&", 
			"8*", 
			"9)", 
			"0(", 
			"-_", // TODO underscore
			"=+", 
			"\\|"
		);
		b.add(VCode.Backspace, 100);
		b.newRow();
		
		b.add(VCode.Tab, 150);
		b.add
		(
			"/", 
			"'", 
			"ק\u0000\u20ac", // euro
			"ר",
			"א",
			"ט",
			"ו\u0000װ",
			"ן",
			"ם",
			"פ",
			"]",
			"["
		);
		b.add(VCode.Empty, 149);
		b.newRow();
		
		b.add(VCode.Empty, 175);
		b.add
		(
			"ש",
			"ד",
			"ג",
			"כ",
			"ע",
			"י\u0000ײ",
			"ח\u0000ױ",
			"ל",
			"ך",
			"ף",
			","
		);
		b.add(VCode.Enter, 225);
		b.newRow();
		
		b.add(VCode.Shift, 225);
		b.add
		(
			"ז",
			"ס",
			"ב",
			"ה",
			"נ",
			"מ",
			"צ",
			"ת",
			"ץ",
			"."
		);
		b.add(VCode.Shift, 275);
		b.newRow();
		b.add(VCode.National, 200);
		b.add(VCode.Empty, 200);
		b.add(" ", 600);
		b.add(VCode.AltGr, 150);
		return b.getKeyboard();
	}
	

	public static OnScreenKeyboard buildGerman(OnScreenKeyboard kb)
	{
		KeyboardBuilder b = new KeyboardBuilder(kb);
		b.add("\u0000", "1!", "2\"²", "3§³", "4$", "5%", "6&", "7/{", "8([", "9)]", "0=}", "ß?\\", "\u0000");
		b.add(VCode.Backspace, 200);
		b.newRow();
		b.add(VCode.Tab, 150);
		b.add("qQ", "wW", "eE", "rR", "tT", "zU",  "uU",  "iI",  "oO",  "pP",  "üÜ",  "+*~");
		b.add(VCode.Enter, 149);
		b.newRow();
		b.add(VCode.CapsLock, 175);
		b.add("aA", "sS", "dD", "fF", "gG", "hH", "jJ", "kK", "lL", "öÖ", "äÄ", "#'");
		b.add(VCode.Enter, 125);
		b.newRow();
		b.add(VCode.Shift, 225);
		b.add("<>|", "yY", "xX", "cC", "vV", "bB", "nN", "mMμ", ",;", ".:", "-_");
		b.add(VCode.Shift, 175);
		b.newRow();
		b.add(VCode.National, 200);
		b.add(VCode.Empty, 200);
		b.add(" ", 600);
		b.add(VCode.AltGr, 150);
		return b.getKeyboard();
	}
	
	
	public static OnScreenKeyboard buildRussian(OnScreenKeyboard kb)
	{
		KeyboardBuilder b = new KeyboardBuilder(kb);
		b.add("\u0000Ё", "1!", "2=", "3№", "4;", "5%", "6:", "7?", "8*", "9(", "0)", "-_", "=+");
		b.add(VCode.Backspace, 150);
		b.newRow();
		b.add(VCode.Tab, 150);
		b.add("йЙ", "цЦ", "уУ", "кК", "еЕ", "нН",  "гГ",  "шШ",  "щЩ",  "зЗ",  "хХ",  "ъЪ",  "\\/");
		b.newRow();
		b.add(VCode.CapsLock, 175);
		b.add("фФ", "ыЫ", "вВ", "аА", "пП", "рР", "оО", "лЛ", "дД", "жЖ", "эЭ");
		b.add(VCode.Enter, 175);
		b.newRow();
		b.add(VCode.Shift, 225);
		b.add("яЯ", "чЧ", "сС", "мМ", "иИ", "тТ", "ьЬ", "бБ", "юЮ", ".,");
		b.add(VCode.Shift, 227);
		b.newRow();
		b.add(VCode.National, 200);
		b.add(VCode.Empty, 200);
		b.add(" ", 600);
		return b.getKeyboard();
	}
}
