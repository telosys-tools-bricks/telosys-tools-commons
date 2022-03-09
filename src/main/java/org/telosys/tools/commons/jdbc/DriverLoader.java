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
package org.telosys.tools.commons.jdbc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

import org.telosys.tools.commons.TelosysToolsException;

/**
 * JDBC driver loader <br>
 * based on current class loader or specific class loader if specific libraries are defined
 * 
 * @author Laurent GUERIN *  */

public class DriverLoader {

	/**
     * Specific class loader ( Inner class )
     */
    private static class MySpecificClassLoader extends URLClassLoader {
        /**
         * Constructor
         * @param urls
         * @param parentLoader
         */
        public MySpecificClassLoader(URL[] urls, java.lang.ClassLoader parent) {
            super(urls, parent);
        }
    }

    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private final String[]                  specificLibraries ; 

    private final MySpecificClassLoader     specificClassLoader ; // Specific Class Loader instance

//    private final Hashtable<String,Driver>  drivers = new Hashtable<>(); // Loaded drivers
    private final Map<String,Driver>  drivers = new HashMap<>(); // Loaded drivers
    
    //-----------------------------------------------------------------------------
    /**
     * Constructor for a driver loader without list of libraries where to search the driver class<br>
     * The current class loader will be used.
     * @param logger
     */
    public DriverLoader() {
    	// No libraries => void array
    	this.specificLibraries = new String[0] ;
    	// no specific class loader (current class loader will be used)
    	this.specificClassLoader = null ; 
    }
    
    /**
     * Constructor for a driver loader with a list of libraries where to search the driver class<br>
     * @param libraries
     * @throws TelosysToolsException
     */
    public DriverLoader(String[] libraries) throws TelosysToolsException {
        if ( libraries == null ) {
        	throw new TelosysToolsException( "DriverLoader constructor : 'libraries' is null !" );
        }
        
        if ( libraries.length == 0 ) {
        	// No libraries => void array
        	this.specificLibraries = new String[0] ;
        	this.specificClassLoader = null ;
        }
        else {
        	this.specificLibraries = libraries ;
        	URL[] urls = buildLibrariesURL(libraries);
            ClassLoader parentClassLoader = ClassLoader.getSystemClassLoader();
            this.specificClassLoader = new MySpecificClassLoader(urls, parentClassLoader);
        }
    }
    
    private URL[] buildLibrariesURL(String[] libraries) throws TelosysToolsException {
        //--- Convert String[] to URL[] (eliminate void urls and detect malformed urls )
        URL[] urls = new URL[libraries.length];
        int n = 0 ;
        for ( int i = 0 ; i < libraries.length ; i++ ) {
            if ( libraries[i] != null ) {
                String sPath = libraries[i].trim();
                if ( sPath.length() > 0 ) {                
		            try {
		                // urls[n] = new File(sPath).toURL(); // toURL deprecated since Java 5.0
		                // The recommended new code ( see JavaDoc )
		                URI uri = new File(sPath).toURI();
		                urls[n] = uri.toURL();
		                n++;
		            } 
		            catch (MalformedURLException e) {
		            	throw new TelosysToolsException("DriverLoader : Cannot convert path '" + sPath + "' to URL (MalformedURLException)", e);
		            }
                }
            }
        }
        
        //--- Build an array with only the valid URLs 
        URL[] validURLs = new URL[n];
        System.arraycopy(urls, 0, validURLs, 0, n);
        if ( validURLs.length == 0 ) {
        	throw new TelosysToolsException( "No valid library URL (URL list is void)" );
        }
        return validURLs;
    }
    //-----------------------------------------------------------------------------
    /**
     * Returns the libraries used by this instance to search the JDBC Driver
     * @return
     */
    public String[] getLibraries() {
    	return this.specificLibraries ;
    }
    
    /**
     * Returns the libraries URL used by the specific class loader, or a void array if none
     * @return
     */
    public URL[] getLibrariesURL() {
    	if ( specificClassLoader != null ) {
        	return specificClassLoader.getURLs() ;
    	}
    	else {
    		return new URL[0];
    	}
    }
    
    /**
     * Returns the libraries used by this instance to search the JDBC Driver as a single String
     * @return
     */
    public String getLibrariesListAsString() {
    	StringBuilder sb = new StringBuilder();
    	for ( String s : this.specificLibraries ) {
    		sb.append(s);
    		sb.append("\n");
    	}
    	return sb.toString();
    }
    
    //-----------------------------------------------------------------------------
    // The method to provide a driver (via the specific class loader)
    //-----------------------------------------------------------------------------
    public Driver getDriver(String sDriverClassName) throws TelosysToolsException
    {
        Driver driverInstance = null;

        //--- Try to find an existing instance of this type of driver 
        driverInstance = drivers.get(sDriverClassName);
        if ( driverInstance != null ) {
            // Driver already loaded 
            return driverInstance ;
        }
                        
        //--- Try to load the driver class with the specific class loader
        Class<?> driverClass = null;
        try {
            if (specificClassLoader != null) {
                // Loading with specific class loader
                driverClass = specificClassLoader.loadClass(sDriverClassName);
            }
            else {
                // Loading with default class loader ...
            	driverClass = loadWithDefaultClassLoader(sDriverClassName) ; // v 2.1.1 #LGU
            }
        } 
        catch (ClassNotFoundException e) {
        	throw new TelosysToolsException("Cannot load class '" + sDriverClassName + "' (ClassNotFoundException)", e);
        }
        if (driverClass == null) {
        	// Unexpected situation 
        	throw new TelosysToolsException("Cannot load class '" + sDriverClassName + "' (unknown reason)");
        }

        //--- Try to create an instance of this driver
        try {
            driverInstance = (Driver) driverClass.newInstance();
        } 
        catch (InstantiationException e) {
        	throw new TelosysToolsException("Cannot create driver instance (InstantiationException)", e);
        } 
        catch (IllegalAccessException e) {
        	throw new TelosysToolsException("Cannot create driver instance (IllegalAccessException)", e);
        }

        if ( driverInstance != null ) {
            //--- Store the driver instance ( for the future )
            drivers.put(sDriverClassName, driverInstance);
        }
        else {
        	throw new TelosysToolsException("Cannot create driver instance (unknown reason)");
        }
        return driverInstance ;
    }
    
    /**
     * Loads the driver using the current class loader
     * @param driverClassName
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> loadWithDefaultClassLoader(String driverClassName) throws ClassNotFoundException
    {
    	ClassLoader classLoader = this.getClass().getClassLoader();
		return classLoader.loadClass(driverClassName);
    }
}