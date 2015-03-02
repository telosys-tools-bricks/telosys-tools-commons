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
package org.telosys.tools.commons.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

import org.telosys.tools.commons.GenericTool;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;

/**
 * JDBC connection manager to get and test a connection
 * 
 * @author Laurent GUERIN *  */

public class ConnectionManager extends GenericTool
{
//    private final String[]     _libraries ;

    private final DriverLoader _driverLoader ;


    //-----------------------------------------------------------------------------
    /**
     * Constructor for an instance working without list of libraries where to search the driver class<br>
     * The current class loader will be used.
     * @param logger the logger to use
     * @since 2.1.1
     */
    public ConnectionManager ( TelosysToolsLogger logger ) throws TelosysToolsException
    {
    	super(logger);
    	log ( "ConnectionManager constructor ... " );
    	
    	// Driver loader based on standard class loader
        _driverLoader = new DriverLoader(logger);     
        if ( _driverLoader == null ) {
        	throwException( "ConnectionManager constructor : Cannot create the driver loader" ) ;
        }
        else {
        	log("Driver loader ready.");
        }
    }
    //-----------------------------------------------------------------------------
    /**
     * Constructor
     * @param libraries array of JAR files where to search the JDBC driver 
     * @param logger the logger to use
     */
    public ConnectionManager ( String[] libraries, TelosysToolsLogger logger ) throws TelosysToolsException
    {
    	super(logger);
    	
    	log ( "ConnectionManager constructor ... " );

        _driverLoader = new DriverLoader(libraries, logger);     
        if ( _driverLoader == null ) {
        	throwException( "ConnectionManager constructor : Cannot create the driver loader" ) ;
        }
        else {
        	log("Driver loader ready.");
        }
    }

    //-----------------------------------------------------------------------------
    /**
     * Returns the libraries defined for this ConnectionManager
     * @return
     */
    public String[] getLibraries() {
    	return this._driverLoader.getLibraries() ;
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
        if ( _driverLoader == null ) {
        	throwException( "getConnection : Driver loader is null " );
        }

        Driver driver = _driverLoader.getDriver(driverClassName);
        if ( driver == null ) {
        	throwException( "getConnection : Cannot get JDBC driver from the driver loader " );
        }
        
        //--- 2) Create the connection
        Connection con = null ;
        try {
            con = driver.connect(jdbcURL, properties);
        }
        catch (SQLException e) {
        	logError("getConnection : Cannot connect to the database (SQLException)");
        	logError(e.getMessage() + " / ErrorCode = " + e.getErrorCode() + " / SQLState = " + e.getSQLState());
            throw new TelosysToolsException ( "Cannot connect to the database (SQLException)", e);
        }
        return con ;
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
        	throw new TelosysToolsException ( "DatabaseConfiguration parameter is null");
        }
        
		//--- Connection parameters, from DatabaseConfiguration
		String sDriverClass = databaseConfiguration.getDriverClass(); 
		String sJdbcUrl     = databaseConfiguration.getJdbcUrl(); 
		Properties prop = new Properties() ;
		prop.put("user",     databaseConfiguration.getUser() ) ; 
		prop.put("password", databaseConfiguration.getPassword() ) ;
		// TODO : manage other set of properties for each database (more than user/password) ???
		
		log("Try to get a database connection...");
		log(" . Driver class = " + quote(sDriverClass) );
		log(" . JDBC URL     = " + quote(sJdbcUrl) );
		log(" . Properties : " );
		Set<Object> keys = prop.keySet();
		for ( Object key : keys ) {
			log("   . '" + key + "' = " + quote( (String) prop.get(key) ) );
		}

		return this.getConnection( sDriverClass, sJdbcUrl, prop );
    }
	private String quote ( String s )
	{
		if ( s == null ) return "null" ;
		return "'" + s + "'" ;
	}
    
    //-----------------------------------------------------------------------------
    /**
     * Test the given connection by getting the current catalog
     * @param con
     * @return true if the test is OK
     */
    public boolean testConnection(Connection con)
    {
    	boolean ret = false ;
        if ( con != null ) {
            try {
            	String catalog = con.getCatalog() ;
            	ret = true ;
                logInfo("Connection test OK : calalog = '" + catalog + "'");
            } 
            catch (SQLException e) {
            	ret = false ;
                logError("Cannot get catalog - SQLException : " + e.getMessage() );
            }            
        } 
        else {
        	ret = false ;
        	logError("Connection is null !");
        }
        return ret ;
    }
    //-----------------------------------------------------------------------------
}