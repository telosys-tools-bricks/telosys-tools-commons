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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Specific class loader
 * 
 * @author Laurent GUERIN *  */

public class SpecificClassLoader extends URLClassLoader
{
    /**
     * Constructor
     * @param jarsURLs
     * @param parentLoader
     */
    public SpecificClassLoader (URL[] jarsURLs, java.lang.ClassLoader parentLoader )
    {
        //--- Call the URLClassLoader constructor
        super(jarsURLs, parentLoader);
    }

    /**
     * Constructor without parent classloader <br>
     * Uses the 'system class loader as parent'
     * @param jarsPaths
     */
    public SpecificClassLoader (String[] jarsPaths )
    {
        this(pathToURL(jarsPaths), ClassLoader.getSystemClassLoader() );
    }

    /**
     * Constructor
     * @param jarsPaths
     * @param parentLoader
     */
    public SpecificClassLoader (String[] jarsPaths, java.lang.ClassLoader parentLoader )
    {
        this(pathToURL(jarsPaths), parentLoader);
    }

	//----------------------------------------------------------------------------------
	/**
	 * Converts jars paths (string) to jars URLs (URL) <br>
	 * Throws a RuntimeException it the list contains an invalid path (null, void, mal formed, etc )
	 * @param jarsPaths
	 * @return
	 */
	private final static URL[] pathToURL(String[] jarsPaths) {
        URL[] urls = new URL[jarsPaths.length]; // Size for all jars (valid or not valid)
        int n = 0 ;
        for ( String jarPath : jarsPaths ) {
            if ( jarPath == null ) {
            	// Invalid jar path 
            	throw new RuntimeException("Cannot convert null path to URL ");
            }
            
            String sPath = jarPath.trim();
            if ( sPath.length() == 0 ) {
            	// Invalid void jar path 
            	throw new RuntimeException("Cannot convert void path to URL ");
            }
            
            // urls[n] = new File(sPath).toURL(); // toURL deprecated since Java 5.0
            // The recommended new code ( see JavaDoc )
            URI uri = new File(sPath).toURI();
            
            try {
                urls[n] = uri.toURL(); // throws IllegalArgumentException and MalformedURLException
                n++;
            } 
            catch (IllegalArgumentException e) {
            	throw new RuntimeException("Cannot convert '" + sPath + "' to URL (IllegalArgumentException)", e);
            }
            catch (MalformedURLException e) {
            	throw new RuntimeException("Cannot convert '" + sPath + "' to URL (MalformedURLException)", e);
            }
        }
		return urls ;
	}

//	//-----------------------------------------------------------------------------
//    // Tools
//    //-----------------------------------------------------------------------------
//    private static void logPaths(String[] paths)
//    {
//        log( "Paths length = " + paths.length );
//        for ( String path : paths) {
//            log( " . [" + path + "]");
//        }
//    }
//    //-----------------------------------------------------------------------------
//    private static void logURLs(URL[] urls)
//    {
//        log  ( "URLs length = " + urls.length );
//        for ( URL url : urls ) {
//            log  ( " . [" + url + "]");
//        }
//    }
//    //-----------------------------------------------------------------------------    
//	private static void log(String s) {
//	}
	
}