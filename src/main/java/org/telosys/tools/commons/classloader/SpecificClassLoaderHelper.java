/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.commons.classloader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 * Specific class loader
 * 
 * @author Laurent GUERIN *  */

public class SpecificClassLoaderHelper 
{
	//----------------------------------------------------------------------------------
	public final static String[] getJarPaths(String directory) {
		return getJarPaths(new File(directory));
	}

	public final static String[] getJarPaths(File directory) {
		List<String> list = new LinkedList<String>();
		if ( directory.exists() ) {
			if ( ! directory.isDirectory() ) {
				throw new IllegalArgumentException("Not a directory");
			}
	
			//--- Get all the directory content
			for (String fileName : directory.list() ) {
				if ( fileName.endsWith(".jar") ) {
					// JAR Found
					File file = new File(directory, fileName);
					list.add(file.getAbsolutePath());
				}
		 	}
		}
		return list.toArray(new String[list.size()]);
	}

}