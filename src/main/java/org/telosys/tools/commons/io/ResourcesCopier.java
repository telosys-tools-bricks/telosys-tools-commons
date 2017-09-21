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
package org.telosys.tools.commons.io ;

import java.io.File;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.exception.CancelException;


public class ResourcesCopier {
	
	private final OverwriteChooser   _overwriteChooser;
	private final CopyHandler        _copyHandler;

	//----------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param overwriteChooser an OverwriteChooser implementation or null (if null overwrite is always 'YES')
	 * @param copyHandler a CopyHandler implementation or null (if null no notification)
	 */
	public ResourcesCopier(OverwriteChooser overwriteChooser, CopyHandler copyHandler) {
		super();
		if ( overwriteChooser != null ) {
			this._overwriteChooser = overwriteChooser ;
		}
		else {
			this._overwriteChooser = new DefaultOverwriteChooser(OverwriteChooser.YES) ;
		}
		_copyHandler = copyHandler ;
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * Copies a file to another one, or a file to a directory, or a directory in another one <br>
	 * @param origin original file or folder
	 * @param destination  destination file or folder
	 * @return the number of files copied (or -1 if the copy has been canceled)
	 * @throws Exception
	 */
	public int copy(File origin , File destination) throws Exception {
		if ( origin == null ) {
			throw new IllegalArgumentException("origin is null");
		}
		if ( destination == null ) {
			throw new IllegalArgumentException("destination is null");
		}
		try {
			int n = 0 ; 
			if ( origin.isFile() && destination.exists() && destination.isDirectory() ) {
				// Copy a single file to an existing directory
				n = copyFileToFolder(origin, destination);
			}
			else {
				// Copy file to file or folder to folder
				n = recursiveCopy(origin, destination); 
			}
			return n ;
		}
		catch(CancelException e) {
			return -1;
		}
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 * Copy a single file in a folder
	 * @param inputFile
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	private int copyFileToFolder(File inputFile , File directory) throws Exception {
    	String outputFileFullPath = FileUtil.buildFilePath(directory.getAbsolutePath(), inputFile.getName());
    	File outputFile = new File(outputFileFullPath);
		if ( outputFile.exists() ) {
			if ( getOverwriteChoice(outputFile) ) {
				// overwrite the existing file
	    		//FileUtil.copy(inputFile, outputFile, true);
	    		copyFile(inputFile, outputFile);
        		return 1 ;
			}
			else {
				// "NO" : do not overwrite
				return 0 ;
			}
		}
		else {
    		//FileUtil.copy(inputFile, outputFile, true);
    		copyFile(inputFile, outputFile);
    		return 1 ;
		}
	}
	//----------------------------------------------------------------------------------------------------
	/**
	 * Copies a file to another one, or a directory in another one <br>
	 * @param origin original file or folder
	 * @param destination  destination file or folder
	 * @return
	 * @throws Exception
	 */
	private int recursiveCopy(File origin , File destination) throws Exception {
		int count = 0 ;
	    if (origin.isDirectory()) {
	    	// Source is a directory => destination is supposed to be a directory 
	        if ( destination.exists() ) {
	        	if ( ! destination.isDirectory() ) {
	        		throw new Exception("'" + destination + "' is not a directory");
	        	}
	        }
	        else {
	            destination.mkdir();
	        }
	        // Copy recursively the content 
	        String[] children = origin.list();
	        for (String child : children ) {
	        	count = count + recursiveCopy( new File(origin, child), new File(destination, child) );
	        }
	    } else {
	    	// Source is a file 
    		if ( destination.exists() ) {
    			// Destination exists 
    			if ( destination.isFile() ) {
        			// Destination exists and is a file
    				if ( getOverwriteChoice(destination) ) {
    					// overwrite the existing file
    		    		//FileUtil.copy(origin, destination, true);
    		    		copyFile(origin, destination);
    	        		return 1 ;
    				}
    				// else : "NO"
    			}
    			else {
        			// Destination exists and is NOT a file => error
	        		throw new Exception("'" + destination + "' already exists and is not a file");
    			}
    		}
    		else {
    			// Destination doesn't exist => copy 
        		//FileUtil.copy(origin, destination, true);
	    		copyFile(origin, destination);
        		return 1 ;
    		}
	    }
	    return count ;
	}
	
	/**
	 * Copy a single file to another one (creates or overwrites if the file exists)<br>
	 * and notify the handler before and after the copy
	 * @param origin
	 * @param destination
	 * @throws Exception
	 */
	private void copyFile(File origin, File destination) throws Exception {
		if ( _copyHandler != null ) {
			_copyHandler.beforeCopy(origin, destination);
		}
		
		FileUtil.copy(origin, destination, true);

		if ( _copyHandler != null ) {
			_copyHandler.afterCopy(origin, destination);
		}
	}
	
	/**
	 * Uses the given OverwriteChooser instance to determine if the file must be overwritten
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private boolean getOverwriteChoice(File file) throws Exception {
		int choice = _overwriteChooser.choose(file.getName(), file.getParent() );
		switch (choice) {
			case OverwriteChooser.YES :
				return true ;
			case OverwriteChooser.NO :
				return false ;
			case OverwriteChooser.CANCEL :
				throw new CancelException("Copy canceled");
			default: 
				throw new Exception("Invalid choice");
		}
	}
}
