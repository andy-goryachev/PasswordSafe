// Copyright © 2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.memsafecrypto.CByteArray;
import goryachev.memsafecrypto.OpaqueBytes;
import goryachev.memsafecrypto.OpaqueChars;
import java.security.SecureRandom;


/**
 * Defines methods used to encrypt and decrypt data file.
 */
public interface IEncryptionHandler
{
	public byte[] encrypt(SecureRandom random, OpaqueChars pass, CByteArray payload) throws Exception;
	
	
	public CByteArray decrypt(byte[] encrypted, OpaqueChars pass) throws Exception;
}
