// Copyright © 2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.crypto.OpaqueChars;
import java.util.function.BiConsumer;


/**
 * Password Generator.
 */
public class PasswordGenerator
	extends Thread
{
	private boolean cancelled;
	private final BiConsumer<PasswordGenerator,OpaqueChars> callback;
	
	
	public PasswordGenerator(BiConsumer<PasswordGenerator,OpaqueChars> callback)
	{
		this.callback = callback;
		
		setPriority(MIN_PRIORITY);
		setDaemon(true);
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
		
		String s = "" + System.currentTimeMillis();
		OpaqueChars pw = new OpaqueChars(s.toCharArray());
		
		if(!cancelled)
		{
			D.print("yo"); // FIX
			callback.accept(this, pw);
		}
	}
}
