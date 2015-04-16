// Copyright (c) 2005-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.ui.UI;
import goryachev.common.util.lz.PseudoLocalizationPromptProvider;
import goryachev.common.util.lz.TXTFormat;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.util.Locale;


/**
 * This class is used to supply localized prompts in one language, as in, for example,
 * a desktop application.  This code should not be used in a server environment where
 * access to localized prompts may be required in several languages at the same time.
 * <p>
 * 
 * The "master" prompts in a "source" language are hard-coded into the code base. 
 * A resource containing the actual translation is loaded at run time.  This simplifies 
 * software development and localization, since the developers do not have to maintain
 * text resources such as message.properties files separately.  A special scanner is required
 * to extract the source prompts from the code base.
 * 
 * <p><pre>
 * Usage:
 *   TXT.get(String id, String master);
 *   TXT.get(String id, String master, Object ... args);
 * </pre>
 * The id must be globally unique (sorry).  I would recommend to use the class name prefix
 * which incidentally imposes a limitation of globally unique class names (as asking for a package
 * name would be too much).
 * <p>
 * 
 * The key (prompt id) is visible to the translators.  Since there are no limitations on the length or
 * format of the prompt id, it is recommended to provide as much contextual information as possible -
 * especially for short prompts.  Where possible, part of the key may contain the prompt used in a phrase,
 * which would later help translators identify whether the prompt is a
 * <p>
 * - verb or a noun<br>
 * - applies to an animate or inanimate object<br>
 * - specify an object the prompt applies to<br>
 * <p>
 * (Example: a single "Print" prompt may be a verb or a noun, but if you write "print the document" 
 * the meaning becomes absolutely clear.
 * <p>
 * 
 * Arguments:
 *   TXT.get("Longer.property NAME is set to VALUE","Property {0} is set to {1}", arg1, arg2);
 * <p>
 *  
 * Test Mode:
 *   Add the following JVM command line option:<pre>
 *     -Di18n.test=dot      adds a unicode dot prefix to internationalized prompts.
 *     -Di18n.test=pseudo   turns on pseudolocalization.
 */
public class TXT
{
	enum Mode { OFF, DOT, PSEUDO };
	public static final String PROPERTY_KEY = "i18n.test";
	public static final char TEST_CHAR = '\u2022';
	private static PromptProvider provider = createDefaultProvider();
	private static Character testChar;
	private static Mode mode = Mode.OFF;
	private static CLanguage language;
	private static WeakList<HasPrompts> hasPrompts;
	private static AWTEventListener awtListener;
	protected static ComponentOrientation orientation;

	
	/** returns localized prompt in the current language */
	public static String get(String key, String master)
	{
		String s = provider.getPrompt(key, master);
		if(s == null)
		{
			s = master;
		}
		
		if(mode == Mode.DOT)
		{
			// mark internationalized untranslated prompts with a bullet
			String html = "<html>";
			if(CKit.startsWithIgnoreCase(s, html))
			{
				// retain html formatting 
				return html + testChar + s.substring(html.length(), s.length());
			}
			else
			{
				return testChar + s;
			}
		}
			
		return s;
	}
	
	
	/**
	 * Returns parameterized string constructed from localized pattern resolved with 
	 * supplied arguments.  This is a preferred way to construct complex strings, 
	 * as the order of words may change from language to language.
	 * 
	 * Unlike MessageFormat.format(), this method does not render numbers and dates
	 * using the current locale.  Additionally, null arguments are ignored.
	 * 
	 * Example:
	 *   String message = get("My.Message","Your name is {0} {1}\n",firstName,lastName);
	 *   
	 * TODO allow for proper localization of plural forms, something like this:
	 *   get("id", "You have exactly {0,plural,month,months} to live", months);
	 *   
	 * see also - get(String,String)
	 */
	public static String get(CLanguage la, String key, String format, Object[] args)
	{
		return new TXTFormat(la, get(key,format), args).format();
	}
	
	
	/**
	 * Returns parameterized string constructed from localized pattern resolved with 
	 * supplied arguments.  This is a preferred way to construct complex strings, 
	 * as the order of words may change from language to language.
	 * 
	 * Unlike MessageFormat.format(), this method does not render numbers and dates
	 * per current locale.  Neither this method swallows ' characters.
	 * null arguments renderer into empty strings.
	 * 
	 * Example:
	 *   String message = TXT.get("My.Message","Your name is {0} {1}\n",firstName,lastName);
	 *   
	 * TODO allow for proper localization of plural forms, something like this:
	 *   TXT.get("id","You have exactly {0,plural,month,months} to live",months);
	 *   
	 * see also - get(String,String)
	 */
	public static String get(String key, String format, Object... args)
	{
		return get(language, key, format, args);
	}
	
	
	private static void setTestMode(Mode m)
	{
		mode = m;
		
		switch(m)
		{
		case PSEUDO:
			PseudoLocalizationPromptProvider p = new PseudoLocalizationPromptProvider();
			p.setLanguage(getLanguage());
			setTestChar(null);
			setPromptProvider(p);
			break;
		case OFF:
			setTestChar(null);
			break;
		case DOT:
			setTestChar(TEST_CHAR);
			break;
		}
	}


	/** add -Di18n.test=[dot,pseudo] to JVM parameters to set the i18n test mode */
	public static void checkTestMode()
	{
		String s = System.getProperty(PROPERTY_KEY);
		if("dot".equals(s))
		{
			setTestMode(Mode.DOT);
		}
		else if("pseudo".equals(s))
		{
			setTestMode(Mode.PSEUDO);
		}
		else
		{
			setTestMode(Mode.OFF);
		}
	}


	public static String format(String fmt, Object ... args)
	{
		return new TXTFormat(language, fmt, args).format();
	}
	
	
	public static void setTestChar(Character c)
	{
		testChar = c;
	}
	

	private static PromptProvider createDefaultProvider()
	{
		return new PromptProvider()
		{
			public String getPrompt(String id, String master) { return master; }
			public void setLanguage(CLanguage la) { }
		};
	}
	
	
	public static void setPromptProvider(PromptProvider p)
	{
		if(mode == Mode.PSEUDO)
		{
			if(!(p instanceof PseudoLocalizationPromptProvider))
			{
				// ignore
				return;
			}
		}
		
		if(p == null)
		{
			p = createDefaultProvider();
		}
		
		provider = p;
	}
	
	
	public static void setLanguage(CLanguage la)
	{
		if(CKit.notEquals(language, la))
		{
			boolean oldltr = CLanguage.isLeftToRight(language);
				
			language = la;
			Locale.setDefault(la.getLocale());
			
			provider.setLanguage(la);
			
			CList<HasPrompts> clients = getPromptListeners();
			if(clients != null)
			{
				for(HasPrompts c: clients)
				{
					c.updatePrompts();
				}
			}
			
			boolean ltr = CLanguage.isLeftToRight(language); 
			if(ltr != oldltr)
			{
				orientation = ltr ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT;
				UI.setLeftToRightOrientation(orientation);
				
				if(awtListener == null)
				{
					// install awt listener to track events in order to set the proper orientation
					awtListener = new AWTEventListener()
					{
						public void eventDispatched(AWTEvent ev)
						{
							int id = ev.getID();
							switch(id)
							{
							case HierarchyEvent.HIERARCHY_CHANGED:
								{
									Component c = ((HierarchyEvent)ev).getComponent();
									c.applyComponentOrientation(orientation);
								}
								break;
								
							case ContainerEvent.COMPONENT_ADDED:
								{
									Component c = ((ContainerEvent)ev).getChild();
									c.applyComponentOrientation(orientation);
								}
								break;
							}
						}
					};
					
					long mask = 
//						AWTEvent.CONTAINER_EVENT_MASK |
						AWTEvent.HIERARCHY_EVENT_MASK;
					
					Toolkit.getDefaultToolkit().addAWTEventListener(awtListener, mask);
				}
			}
		}
	}
	
	
	public static CLanguage getLanguage()
	{
		if(language == null)
		{
			setLanguage(CLanguage.getDefault());
		}
		return language;
	}
	
	
	public synchronized static void registerListener(HasPrompts c)
	{
		if(hasPrompts == null)
		{
			hasPrompts = new WeakList<HasPrompts>();
		}
		hasPrompts.add(c);
	}
	
	
	private synchronized static CList<HasPrompts> getPromptListeners()
	{
		if(hasPrompts != null)
		{
			return hasPrompts.asList();
		}
		return null;
	}
}
