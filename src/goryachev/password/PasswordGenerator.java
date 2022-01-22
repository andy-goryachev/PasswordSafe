// Copyright © 2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.memsafecrypto.OpaqueChars;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.function.BiConsumer;


/**
 * Password Generator.
 */
public class PasswordGenerator
	extends Thread
{
	public static enum Alphabet
	{
		LATIN,
		CYRILLIC
	}

	public static final int DEFALT_LENGTH = 32;
	private static final int COMFORT_PERIOD = 300;
	private final SecureRandom random;
	private final BiConsumer<PasswordGenerator,OpaqueChars> callback;
	private boolean cancelled;
	private Alphabet alphabet = Alphabet.LATIN;
	private boolean uppercase;
	private boolean lowercase;
	private boolean digits;
	private int length = DEFALT_LENGTH;
	private String mustInclude;
	
	
	public PasswordGenerator(SecureRandom r, BiConsumer<PasswordGenerator,OpaqueChars> callback)
	{
		this.random = r;
		this.callback = callback;
		
		setPriority(MIN_PRIORITY);
		setDaemon(true);
	}
	
	
	public void setAlphabet(Alphabet x)
	{
		alphabet = x;
	}
	
	
	public void setUppercase(boolean on)
	{
		uppercase = on;
	}
	
	
	public void setLowercase(boolean on)
	{
		lowercase = on;
	}
	
	
	public void setDigits(boolean on)
	{
		digits = on;
	}
	
	
	public void setMustInclude(String x)
	{
		if(x != null)
		{
			if(x.length() == 0)
			{
				x = null;
			}
		}
		
		mustInclude = x;
	}
	
	
	public void setLength(int x)
	{
		length = x;
	}
	
	
	public void cancel()
	{
		cancelled = true;
		interrupt();
	}
	
	
	public void generate()
	{
		start();
	}
	
	
	public void run()
	{
		CKit.sleep(COMFORT_PERIOD);
		
		char[] cs = generatePassword();
		OpaqueChars pw = new OpaqueChars(cs);
		
		if(!cancelled)
		{
			callback.accept(this, pw);
		}
	}


	protected char[] generatePassword()
	{
		SB sb = new SB(1024);
		
		switch(alphabet)
		{
		case LATIN:
			{
				String latin = "abcdefghijklmnopqrstuvwxyz";
				if(uppercase)
				{
					sb.append(latin.toUpperCase(Locale.US));
				}
				if(lowercase)
				{
					sb.append(latin);
				}
			}
			break;
		case CYRILLIC:
			{
				String cyrillic = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
				if(uppercase)
				{
					sb.append(cyrillic.toUpperCase(new Locale("ru")));
				}
				if(lowercase)
				{
					sb.append(cyrillic);
				}
			}
			break;
		}
		
		if(digits)
		{
			sb.append("0123456789");
		}
		
		String src = sb.toString();
		int sz = src.length();
		char[] cs = new char[length];
		
		int start = 0;
		if(mustInclude != null)
		{
			int ix = random.nextInt(mustInclude.length());
			cs[0] = mustInclude.charAt(ix);
			start = 1;
		}
		
		for(int i=start; i<length; i++)
		{
			int ix = random.nextInt(sz);
			cs[i] = src.charAt(ix);
		}

		return cs;
	}
}
