// Copyright © 2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.common.util.SB;
import goryachev.crypto.OpaqueChars;
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
	
	private final BiConsumer<PasswordGenerator,OpaqueChars> callback;
	private boolean cancelled;
	private Alphabet alphabet = Alphabet.LATIN;
	private boolean uppercase;
	private boolean lowercase;
	private boolean digits;
	private int length = 32;
	private String mustInclude;
	
	
	public PasswordGenerator(BiConsumer<PasswordGenerator,OpaqueChars> callback)
	{
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
		CKit.sleep(200); // FIX
		
		char[] cs = generatePassword();
		OpaqueChars pw = new OpaqueChars(cs);
		
		if(!cancelled)
		{
			D.print("yo"); // FIX
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
		
		// TODO must include
		
		String src = sb.toString();
		int sz = src.length();
		
		SecureRandom r = new SecureRandom();
		char[] cs = new char[length];
		
		for(int i=0; i<length; i++)
		{
			int ix = r.nextInt(sz);
			cs[i] = src.charAt(ix);
		}

		return cs;
	}
}
