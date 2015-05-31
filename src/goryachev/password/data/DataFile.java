// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.common.util.CList;
import goryachev.crypto.OpaqueChars;


public final class DataFile
{
	public static final long SIGNATURE_V1 = 0x1DEA201111111014L;
	public static final String ALGORITHM_HMAC = "HmacSHA512";
	public static final String ALGORITHM_PRNG = "SHA1PRNG";
	public static final String ALGORITHM_HASH = "SHA-512";
	public static final int SALT_LENGTH = 256;
	public static final int KDF_ITERATION_COUNT = 4096;
	public static final int IV_LENGTH = 128/8;
	public static final int KEY_LENGTH_BYTES = 256/8;
	public static final int AES_BLOCK_SIZE_BYTES = 128/8;
	public static final int HASH_LENGTH = 512/8;
	public static final int MAX_STRING_LENGTH = 0x7fffff;
	
	private CList<PassEntry> list = new CList();
	private OpaqueChars password;
	
	
	public DataFile()
	{
	}
	
	
	public void setPassword(OpaqueChars s)
	{
		password = s;
	}
	
	
	public OpaqueChars getPassword()
	{
		return password;
	}
	
	
	public PassEntry[] getEntries()
	{
		return list.toArray(new PassEntry[list.size()]);
	}
	
	
	public PassEntry addEntry()
	{
		PassEntry en = new PassEntry();
		list.add(en);
		return en;
	}
	
	
	public void removeEntry(PassEntry en)
	{
		list.remove(en);
	}
}
