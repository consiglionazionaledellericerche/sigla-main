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

import it.cnr.contab.util.StringEncrypter.EncryptionException;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;

public class ConfigCrypto {
	
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private static SecretKeyFactory keyFactory;
	private static Cipher cipher;
	private static ConfigCrypto _instance = null;
	public static  ConfigCrypto getInstance() throws EncryptionException
	{
		if ( _instance == null )
		{
			_instance = new ConfigCrypto();
			try {
				keyFactory = SecretKeyFactory.getInstance( DESEDE_ENCRYPTION_SCHEME );
				cipher = Cipher.getInstance( DESEDE_ENCRYPTION_SCHEME );
			}
			catch (NoSuchAlgorithmException e) {
				throw new EncryptionException( e );
			}
			catch (NoSuchPaddingException e)
			{
				throw new EncryptionException( e );
			}
		}
		return _instance;
	}
	public SecretKeyFactory getKeyFactory() {
		return keyFactory;
	}
	public Cipher getCipher() {
		return cipher;
	}
}
