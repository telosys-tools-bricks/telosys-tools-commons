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
package org.telosys.tools.commons ;

import java.io.File;



public class DirUtil {
	
	/**
	 * Creates the given directory if it doesn't exist <br>
	 * All the required parent directories are created if necessary 
	 * @param directory
	 * @throws IllegalArgumentException if the given argument is null 
	 */
	public static void createDirectory(File directory) {
	    if ( directory == null ) {
	    	throw new IllegalArgumentException("directory argument is null");
	    }
		if ( ! directory.exists() ) {
			// Creates the directory, including any necessary but nonexistent parent directories.
			boolean result = directory.mkdirs() ; 
			if ( result != true ) {
				throw new RuntimeException("cannot create directory '" + directory.getPath() + "'");
			}
		}
		else {
			// already exists 
			if ( ! directory.isDirectory() ) {
				// not a directory :-(
				throw new RuntimeException("cannot create directory '" + directory.getPath() + "' (already exists and not a directory) ");
			}
		}
	}
	
	/**
	 * Deletes the given directory and all its content (recursive deletion) 
	 * @param directory
	 * @throws IllegalArgumentException if the given argument is null or not a directory
	 */
	public static void deleteDirectory(File directory) {
	    if ( directory == null ) {
	    	throw new IllegalArgumentException("directory argument is null");
	    }
	    if ( ! directory.isDirectory() ) {
	    	throw new IllegalArgumentException("argument is not a directory");
	    }
	    deleteDirectoryRecursively(directory);
	}
	
	private static void deleteDirectoryRecursively(final File directory) {
//	    if ( directory == null ) {
//	        return;
//	    }
	    
	    if ( directory.exists() ) {
	    	//--- Delete the directory content
	        for ( File entry : directory.listFiles() ) {
	            if ( entry.isDirectory() ) {
	            	//--- Delete the directory content
	            	deleteDirectoryRecursively(entry);
//	            	//--- Delete the directory itself
//	                f.delete();
	            }
	            else {
	            	//--- Delete the file
	                entry.delete();
	            }
	        }
        	//--- Delete the directory itself (now is void)
	        directory.delete();
	    }
	}
}
