/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public final class StringEncrypter  {
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	public static final String DES_ENCRYPTION_SCHEME = "DES";
	public static final String DEFAULT_ENCRYPTION_KEY = "This is a fairly long phrase used to encrypt";

	private static final String UNICODE_FORMAT = "UTF8";

	private static KeySpec getKeySpec( String encryptionKey ) throws EncryptionException
	{
		String encryptionScheme = DESEDE_ENCRYPTION_SCHEME;
		if ( encryptionKey == null )
			throw new IllegalArgumentException( "encryption key was null" );
		if ( encryptionKey.trim().length() < 24 )
			throw new IllegalArgumentException(
			"encryption key was less than 24 characters" );

		KeySpec keySpec;
		try
		{
			byte[] keyAsBytes = encryptionKey.getBytes( UNICODE_FORMAT );

			if ( encryptionScheme.equals( DESEDE_ENCRYPTION_SCHEME) )
			{
				keySpec = new DESedeKeySpec( keyAsBytes );
			}
			else if ( encryptionScheme.equals( DES_ENCRYPTION_SCHEME ) )
			{
				keySpec = new DESKeySpec( keyAsBytes );
			}
			else
			{
				throw new IllegalArgumentException( "Encryption scheme not supported: "
						+ encryptionScheme );
			}
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptionException( e );
		}
		catch (UnsupportedEncodingException e)
		{
			throw new EncryptionException( e );
		}
		return keySpec;
	}

	public static String encrypt( String encryptionKey, String unencryptedString ) throws EncryptionException
	{
		KeySpec keySpec = getKeySpec(Utility.lpad(encryptionKey, 24, '0'));

		if ( unencryptedString == null || unencryptedString.trim().length() == 0 )
			throw new IllegalArgumentException(
			"unencrypted string was null or empty" );

		try
		{
			SecretKeyFactory keyFactory = ConfigCrypto.getInstance().getKeyFactory();
			Cipher cipher = ConfigCrypto.getInstance().getCipher();

			SecretKey key = keyFactory.generateSecret( keySpec );
			cipher.init( Cipher.ENCRYPT_MODE, key );
			byte[] cleartext = unencryptedString.getBytes( UNICODE_FORMAT );
			byte[] ciphertext = cipher.doFinal( cleartext );

			return Base64.getEncoder().encodeToString(ciphertext);
		}
		catch (Exception e)
		{
			throw new EncryptionException( e );
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(encrypt("isin-udg@legalmail.it", "GioISINGlo.7383"));
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
	}

	public static String decrypt( String encryptionKey, String encryptedString ) throws EncryptionException
	{
		KeySpec keySpec = getKeySpec(Utility.lpad(encryptionKey, 24, '0'));

		if ( encryptedString == null || encryptedString.trim().length() <= 0 )
			throw new IllegalArgumentException( "encrypted string was null or empty" );

		try
		{
			SecretKeyFactory keyFactory = ConfigCrypto.getInstance().getKeyFactory();
			Cipher cipher = ConfigCrypto.getInstance().getCipher();

			SecretKey key = keyFactory.generateSecret( keySpec );
			cipher.init( Cipher.DECRYPT_MODE, key );
			byte[] cleartext = Base64.getDecoder().decode( encryptedString );
			byte[] ciphertext = cipher.doFinal( cleartext );

			return bytes2String( ciphertext );
		}
		catch (Exception e)
		{
			throw new EncryptionException( e );
		}
	}

	private static String bytes2String( byte[] bytes )
	{
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			stringBuffer.append( (char) bytes[i] );
		}
		return stringBuffer.toString();
	}

	@SuppressWarnings("serial")
	public static class EncryptionException extends Exception
	{
		public EncryptionException( Throwable t )
		{
			super( t );
		}
	}
}
