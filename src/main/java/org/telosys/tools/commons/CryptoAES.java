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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoAES {
	
	private static final String AES = "AES";
	private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int    GCM_TAG_LENGTH = 128;

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

	private static Cipher buildCipher(int cipherMode, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance(ALGORITHM);

		// Define a fixed 12-byte IV  (not random because the same IV must be used for encryption and decryption)
		// AES-GCM mode requires that the Initialization Vector (IV) used in decryption matches exactly the IV used during encryption.
        byte[] fixedIV = new byte[] { 
                (byte)0x05, (byte)0x06, (byte)0x07, (byte)0x08,
                (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04,
                (byte)0x09, (byte)0x0A, (byte)0x0B, (byte)0x0C
            };
        
        // Create GCMParameterSpec (implements AlgorithmParameterSpec required by Cipher.init)
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, fixedIV);
        
        // Init cipher with AlgorithmParameterSpec parameter (required for "GCM") 
        cipher.init(cipherMode, secretKey, gcmParameterSpec); 
        return cipher;
	}
	
	public static void writeSecretKey(File file, SecretKey secretKey) throws TelosysToolsException {
		File parentDir = file.getParentFile(); // Get the parent directory
        if (parentDir != null && !parentDir.exists()) {
        	boolean dirCreated = parentDir.mkdirs(); // try to create parent directories
            if ( ! dirCreated ) { 
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

