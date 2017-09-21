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
import java.util.Hashtable;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;

/**
 * Utility class used to load JDBC drivers
 * 
 * @author Laurent GUERIN *  */

public class DriverLoader //extends GenericTool
{
	protected final TelosysToolsLogger  logger ;
	//----------------------------------------------------------------------------------
	private void log(String s) {
		if (logger != null ) {
			logger.log(s);
		}
	}
	//----------------------------------------------------------------------------------
	private final void throwException (String msg) throws TelosysToolsException
	{
    	if ( logger != null ) {
    		logger.error(msg);
    	}
    	throw new TelosysToolsException(msg);
	}
	//----------------------------------------------------------------------------------
	private final void throwException (String msg, Throwable e) throws TelosysToolsException
	{
    	if ( logger != null ) {
    		logger.error(msg);
    	}
    	throw new TelosysToolsException(msg,e);
	}
	//----------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Specific Class Loader ( Inner class )
    //-----------------------------------------------------------------------------
    private static class MyClassLoader extends URLClassLoader
    {
        public MyClassLoader (URL[] urls, java.lang.ClassLoader parentLoader )
        {
            //--- Call the URLClassLoader constructor
            super(urls, parentLoader);
        }
    }

    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private final String[]                  _libraries ;

    private final MyClassLoader             _loader ; // Specific Class Loader instance

    private final Hashtable<String,Driver>  _drivers = new Hashtable<String,Driver>(); // Loaded drivers
    
    //-----------------------------------------------------------------------------
    /**
     * Constructor for a driver loader without list of libraries where to search the driver class<br>
     * The current class loader will be used.
     * @param logger
     * @since 2.1.1
     */
    public DriverLoader( TelosysToolsLogger logger ) 
    {
//    	super(logger);
    	this.logger = logger ;

    	// No libraries => void array
    	_libraries = new String[0] ; 
    	
    	_loader = null ;
    }
    
    //-----------------------------------------------------------------------------
    /**
     * Constructor for a driver loader with a list of libraries where to search the driver class
     * 
     * @param libraries
     * @param logger
     */
    public DriverLoader( String[] libraries, TelosysToolsLogger logger ) throws TelosysToolsException
    {
//    	super(logger);
    	this.logger = logger ;
    	
    	log ( "DriverLoader constructor ... " );
        if ( libraries == null )
        {
        	throwException( "DriverLoader constructor : 'libraries' is null !" );
        }
        else if ( libraries.length == 0 )
        {
        	throwException( "DriverLoader constructor : 'libraries' is void !" );
        }
    	_libraries = libraries ;
        
        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
        //--- Convert String[] to URL[] (eliminate void and malformed urls )
        logPaths(libraries);
        URL[] urls = new URL[libraries.length];
        int n = 0 ;
        for ( int i = 0 ; i < libraries.length ; i++ )
        {
            if ( libraries[i] != null )
            {
                String sPath = libraries[i].trim();
                if ( sPath.length() > 0 )
                {                
		            try
		            {
		                // urls[n] = new File(sPath).toURL(); // toURL deprecated since Java 5.0
		                // The recommended new code ( see JavaDoc )
		                URI uri = new File(sPath).toURI();
		                urls[n] = uri.toURL();
		                n++;
		            } 
		            catch (MalformedURLException e)
		            {
		            	throwException("DriverLoader : Cannot convert path '" + sPath + "' to URL (MalformedURLException)", e);
		            }
                }
            }
        }
        
        //--- Build an array with only the valid URLs 
        URL[] validURLs = new URL[n];
        System.arraycopy(urls, 0, validURLs, 0, n);
        logURLs(validURLs);
        if ( validURLs.length == 0 )
        {
        	throwException( "No valid URL" );
        }
        
        //--- Create the specific class loader
        _loader = new MyClassLoader ( validURLs, parentLoader );
        log  ( "Specific Class Loader created." );
    }
    
    //-----------------------------------------------------------------------------
    /**
     * Returns the libraries used by this instance to search the JDBC Driver
     * @return
     */
    public String[] getLibraries() {
    	return this._libraries ;
    }
    
    //-----------------------------------------------------------------------------
    // The method to provide a driver (via the specific class loader)
    //-----------------------------------------------------------------------------
    public Driver getDriver(String sDriverClassName) throws TelosysToolsException
    {
        Driver driverInstance = null;

        //--- Try to find an existing instance of this type of driver 
        driverInstance = _drivers.get(sDriverClassName);
        if ( driverInstance != null )
        {
            log ("Driver already loaded");
            return driverInstance ;
        }
                        
        //--- Try to load the driver class with the specific class loader
        log ("Loading the driver class '" + sDriverClassName  + "' ...");
        Class<?> driverClass = null;
        try
        {
            if (_loader != null) {
                log ("Loading with specific class loader ...");
                driverClass = _loader.loadClass(sDriverClassName);
            }
            else {
            	//throwException("Class loader is not initialized (_loader == null)");
                log ("Loading with default class loader ...");
            	driverClass = loadWithDefaultClassLoader(sDriverClassName) ; // v 2.1.1 #LGU
            }
        } catch (ClassNotFoundException e)
        {
        	throwException("Cannot load class '" + sDriverClassName + "' (ClassNotFoundException)", e);
        }
        if (driverClass == null) {
        	// Unexpected situation 
        	throwException("Cannot load class '" + sDriverClassName + "' (unknown reason)");
        }

        //--- Try to create an instance of this driver
        log ("Driver class loaded." );
        log ("Creating new driver instance ..." );
        try {
            driverInstance = (Driver) driverClass.newInstance();
        } 
        catch (InstantiationException e) {
        	throwException("Cannot create driver instance (InstantiationException)", e);
        } 
        catch (IllegalAccessException e) {
        	throwException("Cannot create driver instance (IllegalAccessException)", e);
        }

        if ( driverInstance != null ) {
            log ("Driver instance created.");
            //--- Store the driver instance ( for the future )
            _drivers.put(sDriverClassName, driverInstance);
        }
        else {
        	throwException("Cannot create driver instance (unknown reason)");
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
		Class<?> clazz = classLoader.loadClass(driverClassName);
		return clazz ;
    }
    
    //-----------------------------------------------------------------------------
    // Tools
    //-----------------------------------------------------------------------------
    private void logPaths(String[] paths)
    {
        log  ( "Paths length = " + paths.length );
        for ( int i = 0 ; i < paths.length ; i++ )
        {
            log  ( i + " : [" + paths[i] + "]");
        }
    }
    //-----------------------------------------------------------------------------
    private void logURLs(URL[] urls)
    {
        log  ( "URLs length = " + urls.length );
        for ( int i = 0 ; i < urls.length ; i++ )
        {
            log  ( i + " : [" + urls[i] + "]");
        }
    }
    //-----------------------------------------------------------------------------    
}