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

import org.telosys.tools.commons.TelosysToolsException;

public class UserHomeUtil {

	static final String TOKEN_DIR = ".telosys-cfg" ;

	private UserHomeUtil() {
	}

	public static String getUserHome() throws TelosysToolsException {
		String userHome = System.getProperty("user.home");
		if ( userHome != null ) {
			return userHome;
		}
		else {
			throw new TelosysToolsException("Cannot get 'user.home'");
		}
	}
	
	public static File getTelosysDir() throws TelosysToolsException {
		// Create a Path using multiple parts
		Path filePath = Paths.get(getUserHome(), TOKEN_DIR);
		return filePath.toFile();
	}
	
	public static File getFileInTelosysDir(String fileName) throws TelosysToolsException {
		// Create a Path using multiple parts
		Path filePath = Paths.get(getUserHome(), TOKEN_DIR, fileName);
		return filePath.toFile();
	}

	public static File getFile(String fileDir, String fileName) throws TelosysToolsException {
		Path filePath = Paths.get(getUserHome(), fileDir, fileName);
		return filePath.toFile();
	}

}
