// Copyright © 2022-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.common.util.CKit;
import goryachev.i18n.TXT;
import goryachev.memsafecrypto.CByteArray;
import goryachev.memsafecrypto.CRandom;
import goryachev.memsafecrypto.Crypto;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.memsafecrypto.util.CByteArrayInputStreamWrapper;
import goryachev.memsafecrypto.util.CByteArrayOutputStream;
import goryachev.password.data.v2.EncryptionHandlerV2;
import goryachev.password.data.v4.EncryptionHandlerV4;
import java.io.File;
import java.security.SecureRandom;


/**
 * Data IO.
 */
public final class DataIO
{
	private final static IEncryptionHandler handler = new EncryptionHandlerV4();
	
	
	/**
	 * Attempts to decrypt the supplied data and deserialize the DataFile object.
	 */
	public static DataFile loadDataFile(File file, OpaqueChars pass) throws Exception
	{
		byte[] enc = CKit.readBytes(file, Integer.MAX_VALUE);
		
		CByteArray b;
		long sig = DataTools.getLong(enc);
		if(sig == EncryptionHandlerV4.SIGNATURE)
		{
			b = handler.decrypt(enc, pass);
		}
		else if(sig == EncryptionHandlerV2.SIGNATURE)
		{
			b = new EncryptionHandlerV2().decrypt(enc, pass);
		}
		else
		{
			throw new Exception("unknown file signature");
		}
		
		DataFile df = read(b);
		df.setPassword(pass);
		return df;
	}
	

	/**
	 * Serializes the data object to a byte array and then encrypts it. 
	 */
	public static void saveDataFile(DataFile df, File file) throws Exception
	{
		SecureRandom random = random();
		OpaqueChars pass = df.getPassword();
		
		byte[] b;
		CByteArray payload = store(df);
		try
		{
			b = handler.encrypt(random, pass, payload);
		}
		finally
		{
			Crypto.zero(payload);
		}
		
		// write to temp file
		File f = File.createTempFile("PasswordSafe-", ".tmp", file.getParentFile());
		CKit.write(b, f);
		
		// delete old version and rename
		file.delete();
		
		// finish
		if(f.renameTo(file) == false)
		{
			throw new Exception(TXT.get("DataFile.failed to rename", "Unable to replace existing file {0}.", file));
		}
	}
	

	protected static final DataFile read(CByteArray dec) throws Exception
	{
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

	
	protected static final CByteArray store(DataFile df) throws Exception
	{
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
			
			return out.toCByteArray();
		}
		finally
		{
			CKit.close(out);
		}
	}
	
	
	protected static final SecureRandom random()
	{
		return new CRandom();
	}
}
