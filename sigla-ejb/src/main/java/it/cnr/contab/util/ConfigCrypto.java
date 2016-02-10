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
