/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoAES {
	
	private static final String AES = "AES";

	/**
	 * Private constructor for static class
	 */
	private CryptoAES() {
	}
	
	/**
	 * Build a new secret key using the key generator
	 * @return
	 * @throws TelosysToolsException
	 */
	public static SecretKey buildSecretKey() throws TelosysToolsException {
	    KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance(AES);
		    return keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new TelosysToolsException("Cannot build AES key", e);
		}
	}

	/**
	 * Build a secret key using the given bytes array 
	 * @param keyBytes
	 * @return
	 */
	private static SecretKey buildSecretKey(byte[] keyBytes)  {
	    return new SecretKeySpec(keyBytes, 0, keyBytes.length, AES);
	}
	
	
	/**
	 * Encode the given instance of SecretKey to a Base64 string
	 * @param key
	 * @return
	 */
	public static String secretKeyToBase64(SecretKey key)  {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	/**
	 * Decode the given Base64 string to an instance of SecretKey
	 * @param keyBase64
	 * @return
	 */
	public static SecretKey base64ToSecretKey(String keyBase64)  {
		byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
		// rebuild key using SecretKeySpec
		return buildSecretKey(keyBytes);
	}
	
	/**
	 * @param inputString
	 * @param key
	 * @return
	 * @throws TelosysToolsException
	 */
	public static byte[] encrypt(String inputString, SecretKey key) throws TelosysToolsException {
		return encrypt(inputString.getBytes(), key);
	}
	/**
	 * @param encrypted
	 * @param key
	 * @return
	 * @throws TelosysToolsException
	 */
	public static String decryptToString(byte[] encrypted, SecretKey key) throws TelosysToolsException {
		byte[] decrypted = decrypt(encrypted, key);
		return new String(decrypted);
	}
	
	/**
	 * @param input
	 * @param key
	 * @return
	 * @throws TelosysToolsException
	 */
	public static byte[] encrypt(byte[] input, SecretKey key) throws TelosysToolsException {
		try {
			Cipher cipher = buildCipher(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(input); // Encrypts or decrypts data depending on Cipher mode
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot encrypt with AES key", e);
		}
	}
	
	/**
	 * @param encrypted
	 * @param key
	 * @return
	 * @throws TelosysToolsException
	 */
	public static byte[] decrypt(byte[] encrypted, SecretKey key) throws TelosysToolsException {
		try {
			Cipher cipher = buildCipher(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encrypted); // Encrypts or decrypts data depending on Cipher mode
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot decrypt with AES key", e);
		}
	}
	
	private static Cipher buildCipher(int cipherMode, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(AES);
        cipher.init(cipherMode, secretKey);
        return cipher;
	}
	
	public static void writeSecretKey(File file, SecretKey secretKey) throws TelosysToolsException {
		File parentDir = file.getParentFile(); // Get the parent directory
        if (parentDir != null && !parentDir.exists()) {
            if ( ! parentDir.mkdirs() ) { // try to create parent directories
    			throw new TelosysToolsException("Cannot create file parent directories");
            }
        }
        try {
			try (FileOutputStream fos = new FileOutputStream(file)) {
			    // Write the raw bytes of the secret key to the file
			    fos.write(secretKey.getEncoded());
			}
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot write AES key in file", e);
		}
	}
	
	public static SecretKey readSecretKey(File file) throws TelosysToolsException {
		try {
			byte[] keyBytes = Files.readAllBytes(file.toPath());
			// Rebuild the SecretKey from the byte array
			return buildSecretKey(keyBytes);
		} catch (IOException e) {
			throw new TelosysToolsException("Cannot read AES key from file", e);
		}
	}	
}

