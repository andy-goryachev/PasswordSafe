// Copyright © 2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.common.util.CKit;
import goryachev.memsafecrypto.CByteArray;
import goryachev.memsafecrypto.Crypto;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.memsafecrypto.util.CByteArrayInputStreamWrapper;
import goryachev.memsafecrypto.util.CByteArrayOutputStream;
import java.security.SecureRandom;


/**
 * Data IO.
 */
public final class DataIO
{
	private final IEncryptionHandler handler;
	
	
	public DataIO(IEncryptionHandler h)
	{
		this.handler = h;
	}
	
	
	/**
	 * Serializes the data object to a byte array and then encrypts it. 
	 */
	public final byte[] save(DataFile df, SecureRandom random) throws Exception
	{
		OpaqueChars passphrase = df.getPassword();
		
		CByteArrayOutputStream out = new CByteArrayOutputStream(65536);
		try
		{
			PassEntry[] entries = df.getEntries();
			int sz = entries.length;
			DataTools.writeInt(out, sz);
			
			for(int i=0; i<sz; i++)
			{
				PassEntry en = entries[i];
				Byte[] keys = en.getKeys();
				
				DataTools.writeInt(out, keys.length);
				
				for(int j=0; j<keys.length; j++)
				{
					Byte id = keys[j]; 
					DataTools.writeByte(out, id);
					
					Object x = en.getValue(id);
					DataTools.writeObject(out, x);
				}
			}
			
			CByteArray payload = out.toCByteArray();
			try
			{
				return handler.encrypt(random, passphrase, payload);
			}
			finally
			{
				Crypto.zero(payload);
			}
		}
		finally
		{
			CKit.close(out);
		}
	}
	

	/**
	 * Attempts to decrypt the supplied data and deserialize the DataFile object.
	 */
	public final DataFile load(byte[] encrypted, OpaqueChars passphrase) throws Exception
	{
		CByteArray dec = handler.decrypt(encrypted, passphrase);
		try
		{
			CByteArrayInputStreamWrapper in = new CByteArrayInputStreamWrapper(dec);
			try
			{
				int sz = DataTools.readInt(in);
				if(sz < 0)
				{
					throw new PassException(PassException.Error.CORRUPTED);
				}
				
				DataFile df = new DataFile();
				df.setPassword(passphrase);
				
				for(int i=0; i<sz; i++)
				{
					PassEntry en = df.addEntry();
					int keyCount = DataTools.readInt(in);
					
					for(int j=0; j<keyCount; j++)
					{
						byte id = (byte)DataTools.readByte(in);
						Object x = DataTools.readObject(in);
						en.putValue(id, x);
					}
				}
				
				return df;
			}
			finally
			{
				CKit.close(in);
			}
		}
		finally
		{
			Crypto.zero(dec);
		}
	}
}
