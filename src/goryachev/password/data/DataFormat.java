// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.memsafecrypto.OpaqueChars;
import java.security.SecureRandom;


@Deprecated // FIX remove
public interface DataFormat
{
	public byte[] save(DataFile f, SecureRandom random) throws Exception;
	
	public DataFile load(byte[] encrypted, OpaqueChars passphrase) throws Exception;
}
