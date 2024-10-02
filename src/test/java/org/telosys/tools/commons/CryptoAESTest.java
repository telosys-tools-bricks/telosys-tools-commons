package org.telosys.tools.commons;

import java.io.File;

import javax.crypto.SecretKey;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.commons.TestsEnv;

import static org.junit.Assert.assertArrayEquals;

public class CryptoAESTest {

	@Test
	public void testBuildAndBase64() throws TelosysToolsException {
		// Build AES key
		SecretKey key = CryptoAES.buildSecretKey();
		
		assertEquals("AES", key.getAlgorithm());
		assertEquals("RAW", key.getFormat());

		String keyBase64 = CryptoAES.secretKeyToBase64(key);
		
		SecretKey key2 = CryptoAES.base64ToSecretKey(keyBase64);
		assertEquals(key, key2);
		assertArrayEquals(key.getEncoded(), key.getEncoded());
	}

	@Test
	public void testEncrypt() throws TelosysToolsException {
		SecretKey key = CryptoAES.buildSecretKey();
		String foo = "ABCDEFGHIJKLMNOP";
		// ENCRYPT
		byte[] encrypted = CryptoAES.encrypt(foo.getBytes(), key);
		// DECRYPT
		byte[] decrypted = CryptoAES.decrypt(encrypted, key);
		String s = new String(decrypted);
		assertEquals(foo, s);
	}

	@Test
	public void testEncryptString() throws TelosysToolsException {
		SecretKey key = CryptoAES.buildSecretKey();
		String foo = "azertyuiop12345";
		// ENCRYPT
		byte[] encrypted = CryptoAES.encrypt(foo, key);
		// DECRYPT
		String s = CryptoAES.decryptToString(encrypted, key);
		assertEquals(foo, s);
	}
	
	@Test
	public void testWriteReadKey() throws TelosysToolsException {
		SecretKey secretKey = CryptoAES.buildSecretKey();
		File file = TestsEnv.getTmpFile("foo/bar/crypto-key");
		// WRITE
		CryptoAES.writeSecretKey(file, secretKey);
		// READ
		SecretKey key2 = CryptoAES.readSecretKey(file);
		// Check
		assertEquals(secretKey, key2);
		assertArrayEquals(secretKey.getEncoded(), key2.getEncoded());
	}
	
}
