// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class KeyboardBuilder
{
	private int x;
	private int y;
	private int mx;
	private Dimension keyDim;
	private int gap = 2;
	private OnScreenKeyboard board;
	private MouseAdapter mouse = createMouseHandler();

	
	public KeyboardBuilder(OnScreenKeyboard kb)
	{
		this.board = (kb == null ? new OnScreenKeyboard() : kb);
		
		// get preferred size
		// FIX better use font metrics
		VKey k = newKey(100);
		k.setText("Ww");
		keyDim = k.getPreferredSize();
		keyDim.width += gap;
		keyDim.height += 1;
		
		Insets m = board.getInsets();
		x = m.left;
		y = m.top;
		mx = 0;
	}
	
	
	protected VKey newKey(int widthPercent)
	{
		VKey k = new VKey(board);
		k.setHover(false);
		k.setOpaque(true);
		k.setFocusable(false);
		k.addMouseListener(mouse);
		board.add(k);
		
		// keyDim will be null in the constructor
		if(keyDim != null)
		{
			int w = Math.round(keyDim.width * widthPercent / 100f); 
			
			k.setBounds(x, y, w - gap, keyDim.height);
			
			x += w;
			
			if(x > mx)
			{
				mx = x;
			}
		}
		
		return k;
	}
	

	public OnScreenKeyboard getKeyboard()
	{
		Insets m = board.getInsets();
		Dimension d = new Dimension(mx + m.right - gap, y + keyDim.height + m.bottom);
		board.setPreferredSize(d);
		return board;
	}
	
	
	public void newRow()
	{
		x = board.getInsets().left;
		y += (keyDim.height + gap);
	}
	
	
	public void add(VCode c)
	{
		add(c, 100);
	}
	
	
	public void add(VCode c, int widthPercent)
	{
		add(c, widthPercent, c.getText());
	}
	
	
	public void add(VCode c, int widthPercent, String symbol)
	{
		VKey k = newKey(widthPercent);
		k.setCode(c);
		k.setText(symbol);
		k.setCtrl(true);
		k.setHover(false);
		
		if(c == VCode.Empty)
		{
			board.remove(k);
		}
	}
	
	
	public void add(String sym, int widthPercent)
	{
		VKey k = newKey(widthPercent);
		k.setSymbols(sym);
		k.setRegister(0);
		k.setHover(false);
	}
	
	
	public void add(String ... keys)
	{
		for(String sym: keys)
		{
			add(sym, 100);
		}
	}
	
	
	private static MouseAdapter createMouseHandler()
	{
		return new MouseAdapter()
		{
			protected VKey getKey(MouseEvent ev)
			{
				return (VKey)ev.getSource();
			}


			public void mouseEntered(MouseEvent ev)
			{
				getKey(ev).setHover(true);
			}


			public void mouseExited(MouseEvent ev)
			{
				getKey(ev).setHover(false);
			}
			
			
			public void mousePressed(MouseEvent ev)
			{
				getKey(ev).handleKeyPress();
			}
		};
	}
}
