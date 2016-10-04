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
 * Specific classpath for specific classloader
 * 
 * @author Laurent GUERIN *  */

public class SpecificClassPath 
{
	List<String> classpath = new LinkedList<String>();
	
	public List<String> getClassPath() {
		return classpath;
	}
	
	public int size() {
		return classpath.size() ;
	}
	
	/**
	 * Adds the given directory in the classpath
	 * @param directory
	 * @return
	 */
	public final int addDirectory(File directory) {
		int count = 0;
		if ( directoryIsValid(directory) ) {
			//--- Add the directory path to the list
			classpath.add(directory.getAbsolutePath());
			count++;
		}
		return count ;
	}

	/**
	 * Adds the given jar file in the classpath ( .jar or .zip file )
	 * @param jarFile
	 * @return
	 */
	public final int addJarFile(File jarFile) {
		int count = 0;
		if ( jarFileIsValid(jarFile) ) {
			//--- Add the jar file to the list
			classpath.add(jarFile.getAbsolutePath());
			count++;
		}
		return count ;
	}

	/**
	 * Adds all the '.jar' files located in the given directory
	 * @param pathsList
	 * @param directory
	 * @return
	 */
	public final int addJarFilesInDirectory(File directory) {
		int count = 0;
		if ( directoryIsValid(directory) ) {
			//--- Get all the directory content
			for (String fileName : directory.list() ) {
				if ( fileName.endsWith(".jar") ) {
					// JAR Found
					File file = new File(directory, fileName);
					classpath.add(file.getAbsolutePath());
					count++;
				}
		 	}
		}
		return count ;
	}

	private final boolean directoryIsValid(File directory) {
		if ( directory.exists() ) {
			if ( directory.isDirectory() ) {
				return true ;
			}
			else {
				throw new IllegalArgumentException("Not a directory (" + directory.getAbsolutePath() + ")");
			}
		}
		return false ;
	}
	
	private final boolean jarFileIsValid(File jarFile) {
		if ( jarFile == null ) {
			throw new IllegalArgumentException("Jar file argument is null");
		}
		if ( jarFile.exists() ) {
			if ( jarFile.isFile() ) {
				if ( jarFile.getName().endsWith(".jar") || jarFile.getName().endsWith(".zip") ) {
					return true ;
				}
				else {
					throw new IllegalArgumentException("Not a .jar or .zip file (" + jarFile.getAbsolutePath() + ")");
				}
			}
			else {
				throw new IllegalArgumentException("Not a file (" + jarFile.getAbsolutePath() + ")");
			}
		}
		return false ;
	}
	
}