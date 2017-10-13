// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import goryachev.common.util.Log;
import goryachev.swing.CBorder;
import goryachev.swing.Theme;
import goryachev.swing.theme.ThemeTools;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.security.SecureRandom;
import javax.swing.JTextArea;


@Deprecated // use EntropyGatherer
public class EntropyGatheringComponent
	extends JTextArea
	implements MouseListener, MouseMotionListener
{
	private Runnable onEntropyCollected;
	private SecureRandom random = new SecureRandom();
	private int eventCount;
	private int sufficientCount = 4000;
	private boolean collected;
	private Color darkColor = new Color(0, 0, 0, 20);
	
	
	public EntropyGatheringComponent(Runnable onEntropyCollected)
	{
		this.onEntropyCollected = onEntropyCollected;
		
		setOpaque(true);
		setBorder(new CBorder(1, ThemeTools.DARKER, 20));
		setBackground(Theme.TEXT_BG);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setFont(Theme.plainFont());
		setWrapStyleWord(true);
		setLineWrap(true);
		setEditable(false);
		setFocusable(false);
		setForeground(Theme.FIELD_FG);
		setMinimumSize(new Dimension(20, 1));
	}
	
	
	public void setSampleCount(int n)
	{
		sufficientCount = n;
	}
	
	
	protected void addEntropy(long x)
	{
		byte[] b = new byte[8];
		b[0] = (byte)(x >> 56);
		b[1] = (byte)(x >> 48);
		b[2] = (byte)(x >> 40);
		b[3] = (byte)(x >> 32);
		b[4] = (byte)(x >> 24);
		b[5] = (byte)(x >> 16);
		b[6] = (byte)(x >>  8);
		b[7] = (byte)(x);
		random.setSeed(b);
	}
	

	public void mouseClicked(MouseEvent ev) { extractEntropy(ev); }
	public void mousePressed(MouseEvent ev) { extractEntropy(ev); }
	public void mouseReleased(MouseEvent ev) { extractEntropy(ev); }
	public void mouseEntered(MouseEvent ev) { extractEntropy(ev); }
	public void mouseExited(MouseEvent ev) { extractEntropy(ev); }
	public void mouseDragged(MouseEvent ev) { extractEntropy(ev); }
	public void mouseMoved(MouseEvent ev) { extractEntropy(ev); }


	protected void extractEntropy(MouseEvent ev)
	{
		addEntropy(System.currentTimeMillis());
		addEntropy(ev.getXOnScreen());
		addEntropy(ev.getYOnScreen());
		addEntropy(ev.getX());
		addEntropy(ev.getY());
		addEntropy(ev.getModifiersEx());
		addEntropy(ev.getClickCount());
		
		tick();
	}
	
	
	protected void tick()
	{
		eventCount = Math.abs(eventCount + 1);
		
		if(!collected)
		{
			if(eventCount > sufficientCount)
			{
				collected = true;
				if(onEntropyCollected != null)
				{
					fireEvent();
				}
			}
		}
		
		repaint();
	}
	
	
	protected void fireEvent()
	{
		try
		{
			onEntropyCollected.run();
		}
		catch(Exception e)
		{
			Log.ex(e);
		}
	}
	
	
	public SecureRandom getEntropy()
	{
		return random;
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		int w = getWidth();
		if(w > 0)
		{
			int h = getHeight();
			
			int x = Math.round((eventCount % sufficientCount) * (float)w / sufficientCount);
			
			g.setColor(darkColor);
			if(collected)
			{
				g.fillRect(0, 0, w, h);
			}
			g.fillRect(0, 0, x, h);
		}
	}
}
