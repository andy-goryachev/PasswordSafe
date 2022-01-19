// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.common.util.CMap;
import goryachev.crypto.OpaqueChars;


public final class PassEntry
{
	public static final byte TYPE_NULL = 'Z';
	public static final byte TYPE_STRING = 's';
	public static final byte TYPE_OPAQUE = 'o';
	public static final byte ID_NAME = 'N';
	public static final byte ID_USERNAME = 'U';
	public static final byte ID_PASSWORD = 'P';
	public static final byte ID_NOTES = 'n';

	private String name; // in the clear
	private CMap<Byte,Object> data = new CMap();

	
	public PassEntry()
	{
	}
	
	
	public int size()
	{
		return data.size();
	}
	
	
	public Byte[] getKeys()
	{
		return data.keySet().toArray(new Byte[size()]);
	}
	
	
	public Object getValue(Byte key)
	{
		return data.get(key);
	}
	
	
	public void putValue(Byte key, Object value)
	{
		data.put(key, value);
	}
	

	public String getName()
	{
		if(name == null)
		{
			Object x = data.get(ID_NAME);
			if(x instanceof String)
			{
				name = (String)x;
			}
			else
			{
				name = null;
			}
		}
		return name;
	}
	
	
	public void setName(String s)
	{
		name = s;
		data.put(ID_NAME, s);
	}
	
	
	public String getUsername()
	{
		Object x = data.get(ID_USERNAME);
		if(x instanceof String)
		{
			return (String)x;
		}
		return null;
	}
	
	
	public void setUserName(String s)
	{
		data.put(ID_USERNAME, s);
	}
	
	
	public OpaqueChars getPassword()
	{
		Object x = data.get(ID_PASSWORD);
		if(x instanceof OpaqueChars)
		{
			return (OpaqueChars)x;
		}
		return null;
	}
	
	
	public void setPassword(OpaqueChars s)
	{
		data.put(ID_PASSWORD, s);
	}
	
	
	public String getNotes()
	{
		Object x = data.get(ID_NOTES);
		if(x instanceof String)
		{
			return (String)x;
		}
		return null;
	}
	
	
	public void setNotes(String s)
	{
		data.put(ID_NOTES, s);
	}
}
