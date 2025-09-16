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
package org.telosys.tools.commons.credentials;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.crypto.SecretKey;

import org.telosys.tools.commons.CryptoAES;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;

/**
 * Token utility 
 * 
 * @author Laurent GUERIN
 *
 */
public class TokenUtil {

	static final String TOKEN_FILE_DIR = ".telosys-cfg" ;
	static final String TOKEN_FILE_PREFIX     = "tk_" ;
	static final String TOKEN_KEY_FILE_PREFIX = "tkk_" ;

	static boolean encryption = true; 
	
	/**
	 * Private constructor for static class
	 */
	private TokenUtil() {
	}
	
	private static File builFile(String fileDir, String fileName) throws TelosysToolsException {
		String userHome = System.getProperty("user.home");
		if ( userHome != null ) {
			Path filePath = Paths.get(userHome, fileDir, fileName);
			return filePath.toFile();
		}
		else {
			throw new TelosysToolsException("Cannot get 'user.home'");
		}
	}
	private static File builTokenFile(String tokenName) throws TelosysToolsException {
		return builFile(TOKEN_FILE_DIR, TOKEN_FILE_PREFIX + tokenName);
	}
	private static File builKeyFile(String tokenName) throws TelosysToolsException {
		return builFile(TOKEN_FILE_DIR, TOKEN_KEY_FILE_PREFIX + tokenName);
	}
	
	/**
	 * Saves the given token in the 'token file'
	 * @param tokenName
	 * @param tokenValue
	 * @param encryption
	 * @return
	 * @throws TelosysToolsException
	 */
	public static File saveToken(String tokenName, String tokenValue, boolean encryption) throws TelosysToolsException {
		File tokenFile = builTokenFile(tokenName);
		if ( encryption ) {
			SecretKey secretKey = CryptoAES.buildSecretKey(); // new AES key
			CryptoAES.writeSecretKey(builKeyFile(tokenName), secretKey); // keep AES key in file
			byte[] encrypted = CryptoAES.encrypt(tokenValue, secretKey); // encrypt token 
			FileUtil.write(tokenFile, encrypted); // write encrypted token 
		}
		else {
			FileUtil.writeString(tokenFile, tokenValue); // write token
		}
		return tokenFile;
	}
	
	/**
	 * Loads a token from the 'token file' 
	 * @param tokenName
	 * @param encryption
	 * @return
	 * @throws TelosysToolsException
	 */
	public static String loadToken(String tokenName, boolean encryption) throws TelosysToolsException {
		File tokenFile = builTokenFile(tokenName);
		if ( encryption ) {
			byte[] encrypted = FileUtil.read(tokenFile); // read encrypted token 
			SecretKey secretKey = CryptoAES.readSecretKey(builKeyFile(tokenName)); // read secret key 
			return CryptoAES.decryptToString(encrypted, secretKey); // decrypt token
		}
		else {
			return FileUtil.readString(tokenFile); // read token 
		}
	}
	
	/**
	 * Delete the 'token file' and 'token key file' if encryption
	 * @param tokenName
	 * @param encryption
	 * @return
	 * @throws TelosysToolsException
	 */
	public static boolean deleteToken(String tokenName, boolean encryption) throws TelosysToolsException {
		boolean deleted = FileUtil.delete(builTokenFile(tokenName));
		if ( encryption ) {
			FileUtil.delete(builKeyFile(tokenName));
		}
		return deleted;
	}

	/**
	 * Return true if the 'token file' exists
	 * @param tokenName
	 * @return
	 * @throws TelosysToolsException
	 */
	public static boolean tokenExists(String tokenName) throws TelosysToolsException {
		File file = builTokenFile(tokenName);
		return file.isFile();
	}
	
	/**
	 * Return true if the 'token key file' exists
	 * @param tokenName
	 * @return
	 * @throws TelosysToolsException
	 */
	public static boolean tokenKeyExists(String tokenName) throws TelosysToolsException {
		File file = builKeyFile(tokenName);
		return file.isFile();
	}

}