// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import goryachev.common.i18n.CLanguage;
import goryachev.common.i18n.CLanguageCode;
import goryachev.common.util.Log;
import goryachev.swing.CAction;
import goryachev.swing.CMenuItem;
import goryachev.swing.CPopupMenu;
import goryachev.swing.UI;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import javax.swing.JButton;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;


public class VKey
	extends JButton
{
	private OnScreenKeyboard kb;
	private String symbols;
	private VCode code;
	private boolean ctrl;
	
	
	public VKey(OnScreenKeyboard kb)
	{
		this.kb = kb;
	}
	
	
	public void setCtrl(boolean on)
	{
		ctrl = on;
	}
	
	
	public void setRegister(int n)
	{
		if(symbols != null)
		{
			if(n >= symbols.length())
			{
				setText(null);
			}
			else
			{
				setText(symbols.substring(n, n+1));
			}
		}
	}
	
	
	public void setSymbols(String s)
	{
		symbols = s;
	}
	
	
	public void setCode(VCode c)
	{
		code = c;
	}
	
	
	public void setHover(boolean on)
	{
		setBackground(on ? kb.getHoverColor() : (ctrl ? kb.getCtrlColor() : kb.getNormalColor()));
		setBorder(on ? kb.getHoverBorder() : kb.getNormalBorder());
	}
	
	
	public OnScreenKeyboard getKeyboard()
	{
		return (OnScreenKeyboard)getParent();
	}


	public void handleKeyPress()
	{
		if(code != null)
		{
			handleSpecial();
		}
		else
		{
			JTextComponent t = getTarget();
			if(t != null)
			{
				typeKey(t, getSymbol());
				return;
			}
			
			UI.beep();
		}
	}
	
	
	protected void typeKey(JTextComponent t, String symbol)
	{
		if("\u0000".equals(symbol))
		{
			return;
		}
		
		try
		{
			int start = t.getSelectionStart();
			int len = t.getSelectionEnd() - start;
			AbstractDocument d = (AbstractDocument)t.getDocument();
			d.replace(start, len, symbol, null);
			
			getKeyboard().setShift(false);
			getKeyboard().setAltGr(false);
		}
		catch(Exception e)
		{
			Log.fail(e);
		}
	}
	
	
	// returns editable text component or null
	protected JTextComponent getTarget()
	{
		Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		if(c != null)
		{
			if(c instanceof JTextComponent)
			{
				JTextComponent t = (JTextComponent)c;
				if(t.isEditable())
				{
					return t;
				}
			}
		}
		return null;
	}
	
	
	protected String getSymbol()
	{
		if(symbols != null)
		{
			int n = getKeyboard().getRegister();
			if(symbols.length() > n)
			{
				return symbols.substring(n, n+1);
			}
			else
			{
				return "";
			}
		}
		return "";
	}
	
	
	public void updateRegister()
	{
		if(symbols != null)
		{
			setText(getSymbol());
		}
	}
	
	
	protected void onBackspace()
	{
		JTextComponent t = getTarget();
		if(t != null)
		{
			try
			{
				int start = t.getSelectionStart();
				int len = t.getSelectionEnd() - start;
				AbstractDocument d = (AbstractDocument)t.getDocument();
				if(len == 0)
				{
					if(start > 0)
					{
						d.remove(start - 1, 1);
					}
				}
				else
				{
					d.remove(start, len);
				}
			}
			catch(Exception e)
			{
				Log.fail(e);
			}
		}
	}
	
	
	protected void onCapsLock()
	{
		getKeyboard().toggleCapsLock();
	}
	
	
	protected void onAltGr()
	{
		getKeyboard().toggleAltGr();
	}
	
	
	protected void onShift()
	{
		getKeyboard().toggleShift();
	}
	
	
	protected void onTab()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
	}
	
	
	protected void onNational()
	{
		CPopupMenu m = new CPopupMenu();
		for(final CLanguageCode c: OnScreenKeyboard.getSupportedLayouts())
		{
			CAction a = new CAction()
			{
				public void action()
				{
					getKeyboard().change(c);
				}
			};
			String name = CLanguage.parse(c.getCode()).getLocalName();
			m.add(new CMenuItem(name, a));
		}
		m.show(this, getWidth(), 0);
	}
	
	
	protected void onEnter()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
	}
	
	
	protected void handleSpecial()
	{
		switch(code)
		{
		case AltGr:
			onAltGr();
			break;
		case Backspace:
			onBackspace();
			break;
		case CapsLock:
			onCapsLock();
			break;
		case Enter:
			onEnter();
			break;
		case National:
			onNational();
			break;
		case Shift:
			onShift();
			break;
		case Tab:
			onTab();
			break;
		default:
			break;
		}
	}
}