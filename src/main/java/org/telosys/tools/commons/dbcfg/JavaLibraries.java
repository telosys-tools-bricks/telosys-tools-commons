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
package org.telosys.tools.commons.dbcfg;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;

public class JavaLibraries {

	private final List<File> libraries ;

	/**
	 * Constructor
	 * @param telosysToolsCfg
	 * @throws TelosysToolsException
	 */
	public JavaLibraries(TelosysToolsCfg telosysToolsCfg) {
		this.libraries = getLibrariesFromLibFolder(telosysToolsCfg) ;
	}

	/**
	 * Get the libraries
	 * @return
	 */
	public List<File> getLibraries() {
		return libraries;
	}

	/**
	 * Returns libraries paths
	 * @return
	 */
	public String[] getLibFilePaths() {
		String[] paths = new String[ libraries.size() ];
		int i = 0 ;
		for ( File file : libraries ) {
			paths[i++] = file.getAbsolutePath() ;
		}
		return paths;
	}

	/**
	 * Add the given library if not yet in the list
	 * @param library
	 */
	public void addLibrary(File library) {
		if ( ! libraries.contains(library) ) {
			libraries.add(library);
		}
	}
	
    /**
     * Returns the libraries located in the Telosys Tools "lib" folder
     * @return
     */
    protected List<File> getLibrariesFromLibFolder(TelosysToolsCfg telosysToolsCfg) {

    	List<File> list = new LinkedList<>();

    	String libFolderPath  = telosysToolsCfg.getLibrariesFolderAbsolutePath(); 
        
    	File libFolder = new File(libFolderPath);
    	if ( ! libFolder.exists() ) {
    		return list;
    	}
    	if ( ! libFolder.isDirectory() ) {
    		return list;
    	}
        
    	// Select ".jar" and ".zip" files 
        for ( File entry : libFolder.listFiles() ) {
            if ( entry.isFile() ) {
            	String fileName = entry.getName();            	
            	if ( fileName.endsWith(".jar") || fileName.endsWith(".zip") ) {
                    list.add(entry);            	}
            }
        }
        return list ;
    }

}
