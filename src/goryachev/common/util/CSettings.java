// Copyright (c) 2005-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;
import java.util.Hashtable;


/** utility class helps storing/restoring various binary values */
public class CSettings
{
	public static final CSettingsProvider NONE = new CSettingsProvider()
	{
		public String getProperty(String key) { return null; }
		public void setProperty(String key, String value) { }
		public CList<String> getPropertyNames() { return new CList(0); }
		public void save() throws Exception { }
	};
	private CSettingsProvider provider = NONE;
	
	/** in-memory settings based on hashtable*/
	public static class CMAP extends CSettings
	{		
		public CMAP()
		{
			super(new CSettingsProvider()
			{
				protected final Hashtable<String,Object> settings = new Hashtable();

				public String getProperty(String key) { return Parsers.parseString(settings.get(key)); }
				public void setProperty(String key, String value) { settings.put(key, value); }
				public CList<String> getPropertyNames() { return new CList(settings.keySet()); }
				public void save() throws Exception { }
			});
		};
	}
	
	
	//
	
	
	public CSettings()
	{
	}
	
	
	public CSettings(CSettingsProvider p)
	{
		setProvider(p);
	}
	
	
	public void setProvider(CSettingsProvider p)
	{
		this.provider = p;
	}
	
	
	public CSettingsProvider getProvider()
	{
		return provider;
	}
	
	
	public String getProperty(String key)
	{
		return provider.getProperty(key);
	}
	
	
	public void setProperty(String key, String value)
	{
		provider.setProperty(key, value);
	}
	
	
	public void save() throws Exception
	{
		provider.save();
	}
	
	
	public void set(String key, Object value)
	{
		setProperty(key, value == null ? null : value.toString());
	}
	
	
	public void setBoolean(String key, boolean val)
	{
		set(key, val);
	}
	
	
	public void setInt(String key, int val)
	{
		set(key, val);
	}
	
	
	public void setLong(String key, long val)
	{
		set(key, val);
	}
	
	
	public boolean getBoolean(String key, boolean defaultValue)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return defaultValue;
		}
		else
		{
			return "true".equalsIgnoreCase(s);
		}
	}
	
	
	public boolean getBoolean(String key)
	{
		return getBoolean(key, false);
	}
	
	
	public int getInt(String key, int defaultValue)
	{
		String s = getProperty(key);
		if(s != null)
		{
			try
			{
				return Integer.parseInt(s);
			}
			catch(Exception e)
			{ }
		}
		return defaultValue;
	}
	
	
	public long getLong(String key, long defaultValue)
	{
		String s = getProperty(key);
		if(s != null)
		{
			try
			{
				return Long.parseLong(s);
			}
			catch(Exception e)
			{ }
		}
		return defaultValue;
	}
	
	
	public String getProperty(String key, String defaultValue)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return defaultValue;
		}
		else
		{
			return s;
		}
	}
	
	
	public String[] getList(String key)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return CKit.emptyStringArray;
		}
		else
		{
			// TODO should support embedded commas
			return CKit.split(s, ",");
		}
	}
	

	public static CSettings loadFromFile(String filename)
	{
		return loadFromFile(new File(filename));
	}
	
	
	public static CSettings loadFromFile(File f)
	{
		return new CSettings(CFileSettings.loadQuiet(f));
	}
	
	
	public File getFile(String key)
	{
		return Parsers.parseFile(getProperty(key));
	}
	
	
	public File getFile(String key, File defaultValue)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return defaultValue;
		}
		return Parsers.parseFile(s);
	}
	
	
	public void setFile(String key, File f)
	{
		setProperty(key, f == null ? null : f.getAbsolutePath());
	}
}
