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
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Token utility 
 * 
 * @author Laurent GUERIN
 *
 */
public class TokenUtil {

	static final String TOKEN_FILE_DIR = ".telosys-cfg" ;
	static final String TOKEN_FILE_PREFIX = "tk_" ;
	
	/**
	 * Private constructor for static class
	 */
	private TokenUtil() {
	}
	
	private static File builTokenFile(String tokenName) throws TelosysToolsException {
		String userHome = System.getProperty("user.home");
		if ( userHome != null ) {
			Path filePath = Paths.get(userHome, TOKEN_FILE_DIR, TOKEN_FILE_PREFIX + tokenName);
			return filePath.toFile();
		}
		else {
			throw new TelosysToolsException("Cannot get 'user.home'");
		}
	}
	
	public static File saveToken(String tokenName, String tokenValue) throws TelosysToolsException {
		File file = builTokenFile(tokenName);
		FileUtil.writeString(file, tokenValue);
		return file;
	}
	
	public static String loadToken(String tokenName) throws TelosysToolsException {
		return FileUtil.readString(builTokenFile(tokenName));
	}
	
	public static boolean deleteToken(String tokenName) throws TelosysToolsException {
		return FileUtil.delete(builTokenFile(tokenName));
	}

	public static boolean tokenExists(String tokenName) throws TelosysToolsException {
		File file = builTokenFile(tokenName);
		return file.isFile();
	}
}