// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.crypto.OpaqueChars;


public interface DataFormat
{
//	public byte[] encrypt(byte[] data, OpaqueChars pass, SecureRandom random) throws Exception;
//	
//	public byte[] decrypt(byte[] data, OpaqueChars pass) throws Exception;
	
	public byte[] save(DataFile f) throws Exception;
	
	public DataFile load(byte[] encrypted, OpaqueChars passphrase) throws Exception;
}
