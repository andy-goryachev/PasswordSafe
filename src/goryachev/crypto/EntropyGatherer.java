// Copyright © 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto;
import goryachev.common.util.WeakList;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;


/**
 * An entropy gathering component and corresponding SecureRandom.
 * <p>
 * This goal of this implementation is to improve security of the standard JVM 
 * implementation by using a publicly known algorithm (Bouncycastle's DigestRandomGenerator),
 * while using both JVM SecureRandom and user AWT events as the source of entropy.
 * By using both sources the overall quality of the generated random numbers should improve,
 * even assuming a possibility that the JVM implementation is compromised, as in this case:
 * http://www.theregister.co.uk/2013/08/12/android_bug_batters_bitcoin_wallets/
 * <p>
 * This component must be attached to the AWT thread by calling its start() method when the 
 * event dispatch thread is active.  Recommended way is to call EntropyGatherer.start() in
 * every application window constructor.
 */
public final class EntropyGatherer
	implements AWTEventListener
{
	public static interface Listener
	{
		public void onEntropyCollectionTick();
	}
	
	//
	
	protected final DigestRandomGenerator random;
	private final SecureRandomSpi spi;
	private final Provider provider;
	protected final SecureRandom jvmRandom;
	private static EntropyGatherer instance;
	private static WeakList<Listener> listeners;
	
	
	private EntropyGatherer()
	{
		jvmRandom = new SecureRandom();

		random = new DigestRandomGenerator(new SHA512Digest());
		
		spi = new SecureRandomSpi()
		{
			private boolean init = true;
			
			protected final void engineSetSeed(byte[] seed)
			{
				random.addSeedMaterial(seed);
			}

			protected final void engineNextBytes(byte[] bytes)
			{
				if(init)
				{
					// initialize generator with randomness from jvm
					random.addSeedMaterial(jvmRandom.generateSeed(256));
					init = false;
				}
				else
				{
					random.addSeedMaterial(jvmRandom.nextLong());					
				}
				
				random.addSeedMaterial(System.currentTimeMillis());
				random.addSeedMaterial(Runtime.getRuntime().freeMemory());
				random.addSeedMaterial(System.nanoTime());
				
				random.nextBytes(bytes);
			}

			protected final byte[] engineGenerateSeed(int numBytes)
			{
				byte[] b = new byte[numBytes];
				engineNextBytes(b);
				return b;
			}
		};
		
		provider = new Provider("AWT EntropyCollector", 1.1, "andy goryachev") { };
	}
	
	
	public static void addListener(Listener li)
	{
		if(listeners == null)
		{
			listeners = new WeakList();
		}
		
		listeners.add(li);
	}
	
	
	protected static void tick()
	{
		if(listeners != null)
		{
			for(Listener li: listeners.asList())
			{
				li.onEntropyCollectionTick();
			}
			
			if(listeners.size() == 0)
			{
				listeners = null;
			}
		}
	}
	
	
	/** 
	 * Attaches the global entropy gatherer instance to the AWT event queue.  
	 * Subsequent calls have no effect.
	 * It is recommenended to call this method when AWT queue becomes available, 
	 * such as on the main application window creation.  
	 */
	public synchronized static final void start()
	{
		if(instance == null)
		{
			instance = new EntropyGatherer();
			
			long mask =
				AWTEvent.HIERARCHY_EVENT_MASK |
				AWTEvent.FOCUS_EVENT_MASK |
				AWTEvent.MOUSE_EVENT_MASK |
				AWTEvent.MOUSE_MOTION_EVENT_MASK |
				AWTEvent.MOUSE_WHEEL_EVENT_MASK |
				AWTEvent.KEY_EVENT_MASK;
			
			Toolkit.getDefaultToolkit().addAWTEventListener(instance, mask);
		}
	}
	
	
	/** Adds entropy to the generator. */
	public static final void addSeedMaterial(long x)
	{
		instance.random.addSeedMaterial(x);
	}
	
	
	/** Adds entropy to the generator. */
	public static final void addSeedMaterial(byte[] x)
	{
		instance.random.addSeedMaterial(x);
	}
	
	
	/** Returns an instance of SecureRanom based on this entropy gatherer component. */
	public static final SecureRandom getSecureRandom()
	{
		return instance.getSecureRandomPrivate();
	}
	
	
	private final SecureRandom getSecureRandomPrivate()
	{
		return new SecureRandom(spi, provider) { };
	}


	public final void eventDispatched(AWTEvent ev)
	{
		// let's mix in everything we can get our hands on
		random.addSeedMaterial(jvmRandom.nextLong());
		random.addSeedMaterial(System.currentTimeMillis());
		random.addSeedMaterial(ev.getID());
		
		if(ev.getSource() != null)
		{
			random.addSeedMaterial(ev.getSource().hashCode());
		}
		
		if(ev instanceof MouseEvent)
		{
			MouseEvent e = (MouseEvent)ev;
			random.addSeedMaterial(e.getXOnScreen());
			random.addSeedMaterial(e.getYOnScreen());
			random.addSeedMaterial(e.getX());
			random.addSeedMaterial(e.getY());
			random.addSeedMaterial(e.getModifiersEx());
			random.addSeedMaterial(e.getClickCount());
		}
		else if(ev instanceof KeyEvent)
		{
			KeyEvent e = (KeyEvent)ev;
			random.addSeedMaterial(e.getKeyChar());
			random.addSeedMaterial(e.getKeyCode());
			random.addSeedMaterial(e.getModifiersEx());
		}
		
		random.addSeedMaterial(Runtime.getRuntime().freeMemory());
		random.addSeedMaterial(System.nanoTime());
		
		tick();
	}
}
