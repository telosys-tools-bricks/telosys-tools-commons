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
package org.telosys.tools.commons ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

/**
 * Utility class for DIRECTORY operations ( set of static methods )
 * 
 * @author Laurent GUERIN 
 * 
 */
public class DirUtil {
	
	/**
	 * Full static class : no public constructor
	 */
	private DirUtil() {
	}

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
			boolean created = directory.mkdirs() ; 
			if ( ! created ) {
				throw new TelosysRuntimeException("cannot create directory '" + directory.getPath() + "'");
			}
		}
		else {
			// already exists 
			if ( ! directory.isDirectory() ) {
				// not a directory :-(
				throw new TelosysRuntimeException("cannot create directory '" + directory.getPath() + "' (already exists and not a directory) ");
			}
		}
	}
	
	/**
	 * Deletes the given directory and all its content (recursive deletion) 
	 * @param directory
	 * @throws TelosysToolsException
	 */
	public static void deleteDirectory(File directory) throws TelosysToolsException {
	    if ( directory == null ) {
	    	throw new TelosysToolsException("directory argument is null");
	    }
	    if ( directory.exists() ) {
		    if ( ! directory.isDirectory() ) {
		    	throw new TelosysToolsException("argument is not a directory");
		    }
		    deleteDirectoryRecursively(directory);
	    }
	}
	
	private static void deleteDirectoryRecursively(File directory) throws TelosysToolsException {
	    
	    if ( directory.isDirectory() ) { // exists and is a directory
	    	// Get all entries in the directory (NB: can be null)
	    	File[] files = directory.listFiles();
	    	if ( files != null ) {
		    	//--- Delete the directory content
		        for ( File entry : directory.listFiles() ) {
		            if ( entry.isDirectory() ) {
		            	//--- Delete the directory content
		            	deleteDirectoryRecursively(entry);
		            }
		            else {
		            	//--- Delete the file
		            	deleteOrThrowException(entry);
		            }
		        }
	    	}
        	//--- Delete the directory itself (now is void)
	        deleteOrThrowException(directory);
	    }
	}
	
	private static final String CANNOT_DELETE = "Cannot delete " ;
	private static void deleteOrThrowException(File file) throws TelosysToolsException {
		try {
			if ( ! Files.deleteIfExists(file.toPath()) ) {
				throw new TelosysToolsException(CANNOT_DELETE + file.getAbsolutePath());
			}
		} catch (IOException e) {
			throw new TelosysToolsException(CANNOT_DELETE + file.getAbsolutePath(), e);
		}
	}

	/**
	 * Returns a list of absolute paths for all the files contained in the given directory
	 * @param directory
	 * @param recursively
	 * @return
	 */
	public static List<String> getDirectoryFiles(final File directory, final boolean recursively) {
		List<String> list = new LinkedList<>();
	    if ( directory == null ) {
	    	throw new IllegalArgumentException("directory argument is null");
	    }
	    if ( directory.exists() ) {
		    if ( ! directory.isDirectory() ) {
		    	throw new IllegalArgumentException("argument is not a directory");
		    }
		    getDirectoryFiles(directory, list, recursively);
	    }
	    return list;
	}
	
	private static void getDirectoryFiles(final File directory, final List<String> list, final boolean recursive) {
	    
	    if ( directory.exists() ) {
	    	//--- Get the directory content
	        for ( File entry : directory.listFiles() ) {
	            if ( entry.isDirectory() ) {
	            	//--- Process the directory content
	            	if ( recursive ) {
		            	getDirectoryFiles(entry, list, recursive);
	            	}
	            }
	            else {
	            	//--- Add the file in the list
	                list.add(entry.getAbsolutePath());
	            }
	        }
	    }
	}
	
	/**
	 * Checks the validity of the given directory path ( exists and is a directory )<br>
	 * throws TelosysToolsException if invalid <br>
	 * @param directoryAbsolutePath
	 * @throws TelosysToolsException
	 * @since v 3.0.0
	 */
	public static void checkIsValidDirectory(String directoryAbsolutePath) throws TelosysToolsException {
		if ( StrUtil.nullOrVoid( directoryAbsolutePath ) ) {
			throw new TelosysToolsException(" not defined");
		}
		
		File file = new File(directoryAbsolutePath);
		
		if ( ! file.exists() ) {
			throw new TelosysToolsException(" '" + directoryAbsolutePath + "' does not exist !");
		}
		if ( ! file.isDirectory() ) {
			throw new TelosysToolsException(" '" + directoryAbsolutePath + "' is not a folder !");
		}
	}
	
	/**
	 * Returns true if the given file exists and is a directory
	 * @param directory
	 * @return
	 * @since v 3.0.0
	 */
	public static boolean isValidDirectory(File directory) {
		return directory.exists() && directory.isDirectory() ;
	}
	
	/**
	 * Returns the user working directory <br>
	 * ( retrieved from the "user.dir" system property ) <br>
	 * It's the directory where the Java program was run from (where the JVM was started). <br>
	 * It can be anywhere where the user has permission to run Java.<br>
	 * @return
	 */
	public static String getUserWorkingDirectory() {
		return System.getProperty("user.dir");
	}
	
	/**
	 * Returns the user "home" directory <br>
	 * ( retrieved from the "user.home" system property ) <br>
	 * @return
	 */
	public static String getUserHomeDirectory() {
		return System.getProperty("user.home");
	}
		
}
