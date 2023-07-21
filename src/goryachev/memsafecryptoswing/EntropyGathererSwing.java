// Copyright © 2012-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.memsafecryptoswing;
import goryachev.memsafecrypto.CRandom;
import goryachev.swing.UI;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


/**
 * An entropy gathering component and corresponding SecureRandom.
 * <p>
 * This goal of this implementation is to improve security of the standard JVM 
 * implementation by using a publicly known algorithm (Bouncycastle's DigestRandomGenerator),
 * while using both JVM SecureRandom and the user input as sources of entropy.
 * By using both sources the overall quality of the generated random numbers should improve,
 * even assuming a possibility that the JVM implementation is compromised, as in this case:
 * http://www.theregister.co.uk/2013/08/12/android_bug_batters_bitcoin_wallets/
 * <p>
 * This component must be attached to the EDT thread by calling its start() method 
 * when the Swing subsystem is activated.
 */
public final class EntropyGathererSwing
	implements AWTEventListener
{
	private static EntropyGathererSwing instance;

	
	protected EntropyGathererSwing()
	{
	}
	
	
	/** 
	 * Attaches the global entropy gatherer instance to the FX event queue.  
	 * Subsequent calls have no effect.
	 * It is recommenended to call this method when FX queue becomes available, 
	 * such as on the main application window creation.  
	 */
	public synchronized static final void start()
	{
		UI.inEDT(() ->
		{
			if(instance == null)
			{
				instance = new EntropyGathererSwing();
				
				long mask =
					AWTEvent.HIERARCHY_EVENT_MASK |
					AWTEvent.FOCUS_EVENT_MASK |
					AWTEvent.MOUSE_EVENT_MASK |
					AWTEvent.MOUSE_MOTION_EVENT_MASK |
					AWTEvent.MOUSE_WHEEL_EVENT_MASK |
					AWTEvent.KEY_EVENT_MASK;
				
				Toolkit.getDefaultToolkit().addAWTEventListener(instance, mask);
			}
		});
	}
	
	
	public final void eventDispatched(AWTEvent ev)
	{
		// let's mix in everything we can get our hands on
		CRandom.addSeedMaterial(System.nanoTime());
		CRandom.addSeedMaterial(ev.getID());
		CRandom.addSeedMaterial(Runtime.getRuntime().freeMemory());
		
		if(ev.getSource() != null)
		{
			CRandom.addSeedMaterial(ev.getSource().hashCode());
		}
		
		if(ev instanceof MouseEvent)
		{
			MouseEvent e = (MouseEvent)ev;
			CRandom.addSeedMaterial(e.getXOnScreen());
			CRandom.addSeedMaterial(e.getYOnScreen());
			CRandom.addSeedMaterial(e.getX());
			CRandom.addSeedMaterial(e.getY());
			CRandom.addSeedMaterial(e.getModifiersEx());
			CRandom.addSeedMaterial(e.getClickCount());
		}
		else if(ev instanceof KeyEvent)
		{
			KeyEvent e = (KeyEvent)ev;
			CRandom.addSeedMaterial(e.getKeyChar());
			CRandom.addSeedMaterial(e.getKeyCode());
			CRandom.addSeedMaterial(e.getModifiersEx());
		}
	}
}
