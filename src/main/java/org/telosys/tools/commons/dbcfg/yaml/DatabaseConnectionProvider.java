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
package org.telosys.tools.commons.dbcfg.yaml;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.dbcfg.JavaLibraries;
import org.telosys.tools.commons.jdbc.DriverLoader;

public class DatabaseConnectionProvider {	
	
    private final DriverLoader  driverLoader ;
    private final DatabaseDefinitions databaseDefinitions ;

    /**
     * Constructor with no specific library
     */
    public DatabaseConnectionProvider() {
    	super();
    	this.driverLoader = new DriverLoader();
    	this.databaseDefinitions = null;
    }

    /**
     * Constructor using Telosys project with specific libraries if any and databases definitions 
     * @param telosysToolsCfg
     * @throws TelosysToolsException
     */
    public DatabaseConnectionProvider(TelosysToolsCfg telosysToolsCfg) throws TelosysToolsException {
    	super();
    	JavaLibraries javaLibraries = new JavaLibraries(telosysToolsCfg);
    	String[] libraries = javaLibraries.getLibFilePaths();
		this.driverLoader = new DriverLoader(libraries);
		
		// init database definitions from Telosys project
    	DatabaseDefinitionsLoader loader = new DatabaseDefinitionsLoader();
    	this.databaseDefinitions = loader.load(telosysToolsCfg);
    }
    
    /**
     * Get a connection for the given database id <br>
     * Usable only if TelosysToolsCfg was passed to the constructor
     * @param databaseId
     * @return
     * @throws TelosysToolsException
     */
    public Connection getConnection(String databaseId) throws TelosysToolsException {
    	if (databaseId == null) {
    		throw new IllegalArgumentException("databaseId is null");
    	}
    	if ( this.databaseDefinitions != null) {
    		DatabaseDefinition databaseDefinition = databaseDefinitions.getDatabaseDefinition(databaseId);
    		if ( databaseDefinition != null ) {
    			return getConnection(databaseDefinition);
    		}
    		else {
    			throw new TelosysToolsException("Unknown database : id '" + databaseId + "'");
    		}
    	}
    	else {
			throw new TelosysToolsException("No databases definitions");
    	}
    }
    
    public Connection getConnection(DatabaseDefinition databaseDefinition) throws TelosysToolsException {
        if ( databaseDefinition == null ) {
        	throw new IllegalArgumentException("DatabaseDefinition is null");
        }
		//--- Connection parameters
		String driverClass = databaseDefinition.getDriver();
		String jdbcUrl     = databaseDefinition.getUrl();
		Properties prop = new Properties() ;
		prop.put("user",     databaseDefinition.getUser() ) ; 
		prop.put("password", databaseDefinition.getPassword() ) ;
		//--- Connection creation
		return createConnection(driverClass, jdbcUrl, prop);
    }
    
    /**
     * Creates a new connection using the given parameters 
     * 
     * @param driverClassName
     * @param jdbcURL
     * @param properties
     * @return
     */
    private Connection createConnection(String driverClassName, String jdbcURL, Properties properties) throws TelosysToolsException
    {
    	//--- Get JDBC driver
        Driver driver = driverLoader.getDriver(driverClassName);
        if ( driver == null ) {
            throw new TelosysToolsException("Cannot get JDBC driver from driver loader");
        }
        
        //--- Create the JDBC connection
        Connection con = null ;
        try {
            con = driver.connect(jdbcURL, properties);
        }
        catch (SQLException e) {
            throw new TelosysToolsException("Cannot connect to database (SQLException)", e);
        }
        catch (Exception e) {
            throw new TelosysToolsException("Cannot connect to database (Exception)", e);
        }
        
        //--- Check connection
        if ( con != null ) {
            return con ;
        }
        else {
        	// Oracle driver can return null when it cannot connect 
            throw new TelosysToolsException("Cannot connect to database. JDBC driver 'connect()' has returned null.");
        }
    }

}