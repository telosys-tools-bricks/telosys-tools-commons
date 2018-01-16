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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;

/**
 * JDBC connection manager<br>
 * Used to get and test a connection
 * 
 * @author Laurent GUERIN *  */

public class ConnectionManager {
	
    private final DriverLoader driverLoader ;

    //-----------------------------------------------------------------------------
    /**
     * Constructor for an instance working without list of libraries where to search the driver class<br>
     * The current class loader will be used.
     * @param logger the logger to use
     * @since 2.1.1
     */
    public ConnectionManager() throws TelosysToolsException {
    	// Driver loader based on standard class loader
        driverLoader = new DriverLoader();     
    }
    //-----------------------------------------------------------------------------
    /**
     * Constructor
     * @param libraries array of JAR files where to search the JDBC driver 
     * @param logger the logger to use
     */
    public ConnectionManager ( String[] libraries ) throws TelosysToolsException {
        driverLoader = new DriverLoader(libraries);     
    }

    //-----------------------------------------------------------------------------
    /**
     * Returns the libraries defined for this ConnectionManager
     * @return
     */
    public String[] getLibraries() {
    	return this.driverLoader.getLibraries() ;
    }
    
    //-----------------------------------------------------------------------------
    /**
     * Creates a new connection using the given parameters 
     * 
     * @param driverClassName
     * @param jdbcURL
     * @param properties
     * @return
     */
    public Connection getConnection(String driverClassName, String jdbcURL, Properties properties ) throws TelosysToolsException
    {        	        
        //--- 1) Get the JDBC driver
        if ( driverLoader == null ) {
            throw new TelosysToolsException( "Driver loader is null");
        }

        Driver driver = driverLoader.getDriver(driverClassName);
        if ( driver == null ) {
            throw new TelosysToolsException( "Cannot get JDBC driver from the driver loader");
        }
        
        //--- 2) Create the connection
        Connection con = null ;
        try {
            con = driver.connect(jdbcURL, properties);
        }
        catch (SQLException e) {
            throw new TelosysToolsException( "Cannot connect to the database (SQLException)", e);
        }
        catch (Exception e) {
            throw new TelosysToolsException( "Cannot connect to the database (Exception)", e);
        }
        
        //--- 3) Check the connection
        if ( con != null ) {
            return con ;
        }
        else {
        	// Oracle driver can return null when it cannot connect 
            throw new TelosysToolsException( "Cannot connect to the database. JDBC driver 'connect()' has returned null.");
        }
    }
    
    /**
     * Returns a database connection using from the given database configuration
     * @param databaseConfiguration
     * @return
     * @throws TelosysToolsException
     */
    public Connection getConnection(DatabaseConfiguration databaseConfiguration) throws TelosysToolsException
    {
        if ( null == databaseConfiguration ) {
        	throw new TelosysToolsException( "DatabaseConfiguration parameter is null");
        }
        
		//--- Connection parameters, from DatabaseConfiguration
		String sDriverClass = databaseConfiguration.getDriverClass(); 
		String sJdbcUrl     = databaseConfiguration.getJdbcUrl(); 
		Properties prop = new Properties() ;
		prop.put("user",     databaseConfiguration.getUser() ) ; 
		prop.put("password", databaseConfiguration.getPassword() ) ;
		
		return this.getConnection( sDriverClass, sJdbcUrl, prop );
    }
}