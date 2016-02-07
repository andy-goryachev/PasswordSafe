// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.crypto.OpaqueChars;


public interface DataFormat
{
	public byte[] save(DataFile f) throws Exception;
	
	public DataFile load(byte[] encrypted, OpaqueChars passphrase) throws Exception;
}
