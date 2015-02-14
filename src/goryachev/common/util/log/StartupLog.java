// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import goryachev.common.ui.Application;
import goryachev.common.util.CComparator;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CSorter;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class StartupLog
{
	private SB sb;
	private String indent = "\t";
	private DecimalFormat numberFormat = new DecimalFormat("#,##0.##");
	
	
	public String getSystemInfo()
	{
		sb = new SB();
		
		extractApp();
		extractEnvironment();
		extractSystemProperties();
		
		try
		{
			extractUIDefaults();
		}
		catch(Throwable ignore)
		{ }
		
		String s = sb.toString();
		sb = null;
		return s;
	}
	
	
	protected void extractApp()
	{
		sb.a("Application\n");
		sb.a(indent);
		if(CKit.isNotBlank(Application.getTitle()))
		{
			sb.a(Application.getTitle()).a(" version ").a(Application.getVersion()).nl();
		}
		
		sb.a(indent);
		sb.a("Time: ").a(new SimpleDateFormat("yyyy-MMdd HH:mm:ss").format(System.currentTimeMillis())).nl();
		
		long max = Runtime.getRuntime().maxMemory();
		long free = max - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
		
		sb.a(indent).a("Available Memory: ").append(number(max)).nl();
		sb.a(indent).a("Free Memory: ").a(number(free)).nl();
		
		sb.nl();
	}

	
	protected void extractEnvironment()
	{
		sb.a("Environment\n");
		
		Map<String,String> env = System.getenv();
		CList<String> keys = new CList(env.keySet());
		CSorter.sort(keys);
		for(String key: keys)
		{
			sb.a(indent);
			sb.a(key).append(" = ").append(safe(env.get(key))).nl();
		}
		sb.nl();
	}
	
	
	protected void extractSystemProperties()
	{
		sb.a("System Properties\n");
		
		Properties p = System.getProperties();
		CList<String> keys = new CList(p.stringPropertyNames());
		CSorter.sort(keys);
		for(String key: keys)
		{
			sb.a(indent);
			sb.a(key).append(" = ").append(safe(p.getProperty(key))).nl();
		}
		sb.nl();
	}
	
	
	protected String number(Object x)
	{
		return numberFormat.format(x);
	}
	
	
	protected String safe(String s)
	{
		if(s != null)
		{
			boolean notSafe = false;
			int sz = s.length();
			for(int i=0; i<sz; i++)
			{
				char c = s.charAt(i);
				if(c < 0x20)
				{
					notSafe = true;
					break;
				}
			}
			
			if(notSafe)
			{
				SB sb = new SB(sz);
				for(int i=0; i<sz; i++)
				{
					char c = s.charAt(i);
					if(c < 0x20)
					{
						sb.a(unicode(c));
					}
					else
					{
						sb.a(c);
					}
				}
				s = sb.toString();
			}
		}
		return s;
	}
	
	
	protected static String unicode(char c)
	{
		return "\\u" + Hex.toHexString(c, 4);
	}
	
	
	protected void describe(Object x)
	{
		if(x == null)
		{
			return;
		}
		else if(x instanceof String)
		{
			sb.a('"');
			sb.a(x);
			sb.a('"');
		}
		else if(x instanceof Color)
		{
			sb.a(x.getClass().getSimpleName());
			
			Color c = (Color)x;
			sb.a("(r=").a(c.getRed());
			sb.a(",g=").a(c.getGreen());
			sb.a(",b=").a(c.getBlue());
			
			if(c.getAlpha() != 255)
			{
				sb.a(",a=").a(c.getAlpha());
			}
			
			sb.a(")");
		}
		else if(x instanceof Font)
		{
			sb.a(x.getClass().getSimpleName());
			
			Font f = (Font)x;
			sb.a("(");
			sb.a(f.getFamily());
			
			if(CKit.notEquals(f.getFamily(), f.getName()))
			{
				sb.a("/");
				sb.a(f.getName());
			}
			
			sb.a(",");
			sb.a(f.getSize());
			sb.a(",");

			if(f.isBold())
			{
				if(f.isItalic())
				{
					sb.a("bolditalic");
				}
				else
				{
					sb.a("bold");
				}
			}
			else
			{
				if(f.isItalic())
				{
					sb.a("italic");
				}
				else
				{
					sb.a("plain");
				}
			}
			
			sb.a(")");
		}
		else if(x instanceof Dimension)
		{
			sb.a(x.getClass().getSimpleName());
			
			Dimension d = (Dimension)x;
			sb.a("(w=").a(d.getWidth());
			sb.a(",h=").a(d.getHeight());
			sb.a(")");
		}
		else if(x instanceof Insets)
		{
			sb.a(x.getClass().getSimpleName());
			
			Insets m = (Insets)x;
			sb.a("(t=").a(m.top);
			sb.a(",l=").a(m.left);
			sb.a(",b=").a(m.bottom);
			sb.a(",r=").a(m.right);
			sb.a(")");
		}
		else if(x instanceof Icon)
		{
			sb.a(x.getClass().getSimpleName());
			
			Icon d = (Icon)x;
			sb.a("(w=").a(d.getIconWidth());
			sb.a(",h=").a(d.getIconHeight());
			sb.a(")");
		}
		else if(x instanceof Border)
		{
			sb.a(x.getClass().getName());
		}
		else if(x instanceof InputMap)
		{
			sb.a(x.getClass().getSimpleName());
		}
		else if(x instanceof ActionMap)
		{
			sb.a(x.getClass().getSimpleName());
		}
		else if(x instanceof Component)
		{
			sb.a(x.getClass().getName());
		}
		else if(x instanceof Object[])
		{
			Object[] a = (Object[])x;
			sb.a("Object[");
			sb.a(a.length);
			sb.a("]");
		}
		else if(x instanceof int[])
		{
			int[] a = (int[])x;
			sb.a("int[");
			sb.a(a.length);
			sb.a("]");
		}
		else
		{
			sb.a(x);
		}
	}
	
	
	protected void extractUIDefaults()
	{
		sb.a("UIManager.getLookAndFeelDefaults");
		sb.nl();
		
		UIDefaults defs = UIManager.getLookAndFeelDefaults();
		
		CList<Object> keys = new CList(defs.keySet());
		new CComparator<Object>()
		{
			public int compare(Object a, Object b)
			{
				return compareText(a, b);
			}
		}.sort(keys);
		
		for(Object key: keys)
		{
			sb.tab();
			sb.a(key);
			sb.a(" = ");
			
			Object v = defs.get(key);
			describe(v);
			
			sb.nl();
		}
	}
}
