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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.jdbc.ConnectionManager;

public class DbConnectionManager {

	private final TelosysToolsCfg telosysToolsCfg ;
	private final JavaLibraries javaLibraries ;
	
    /**
     * Constructor
     * @param telosysToolsCfg
     */
    public DbConnectionManager(TelosysToolsCfg telosysToolsCfg) throws TelosysToolsException {
		super();
		this.telosysToolsCfg = telosysToolsCfg;
		this.javaLibraries = new JavaLibraries(telosysToolsCfg);
	}
	
    /**
     * Adds additional librairies (to search the JDC driver)
     * @param additionalLibraries
     */
    public void addLibraries(List<File> additionalLibraries) {
    	for ( File lib : additionalLibraries ) {
    		javaLibraries.addLibrary(lib);
    	}
	}
	
	/**
	 * Returns a connection on the default database
	 * @return
	 * @throws TelosysToolsException
	 */
	public Connection getConnection() throws TelosysToolsException {
    	DatabaseConfiguration databaseConfiguration = getDatabasesConfigurations().getDatabaseConfiguration() ;
    	if ( databaseConfiguration == null ) {
    		throw new TelosysToolsException("No configuration for default database");
    	}
    	return openConnection(databaseConfiguration);
    }

    /**
     * Returns a connection on the database identified by the given id
     * @param databaseId
     * @return
     * @throws TelosysToolsException
     */
    public Connection getConnection(int databaseId) throws TelosysToolsException {
    	DatabaseConfiguration databaseConfiguration = getDatabasesConfigurations().getDatabaseConfiguration(databaseId) ;
    	if ( databaseConfiguration == null ) {
    		throw new TelosysToolsException("No configuration for database " + databaseId);
    	}
    	return openConnection(databaseConfiguration);
    }

    private DatabasesConfigurations getDatabasesConfigurations() throws TelosysToolsException {
    	DbConfigManager dbConfigManager = new DbConfigManager(telosysToolsCfg) ;
    	DatabasesConfigurations databasesConfigurations = dbConfigManager.load();
    	if ( databasesConfigurations == null ) {
    		throw new TelosysToolsException("Cannot get DatabasesConfigurations (DbConfigManager returns null)");
    	}
    	return databasesConfigurations;
    }
    
    private Connection openConnection(DatabaseConfiguration databaseConfiguration ) throws TelosysToolsException {
        if ( databaseConfiguration == null ) {
        	throw new TelosysToolsException("Invalid parameter : DatabaseConfiguration is null");
        }
        
		ConnectionManager connectionManager = createConnectionManager();

        try {
			return connectionManager.getConnection(databaseConfiguration);
		} catch (TelosysToolsException e) {
			throw e;
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot get database connection", e);
		}
    }
    
    /**
     * Creates a ConnectionManager instance
     * @param libraries all the libraries where to search the JDBC Driver
     * @return
     */
    private ConnectionManager createConnectionManager() throws TelosysToolsException {
        ConnectionManager cm = null ;
		try {
			if ( ! javaLibraries.getLibraries().isEmpty() ) {
				// ConnectionManager with specific libraries
				cm = new ConnectionManager( javaLibraries.getLibFilePaths() );
			}
			else {
				// ConnectionManager based on the standard ClassPath
				cm = new ConnectionManager();
			}
		} catch (TelosysToolsException e) {
    		throw new TelosysToolsException("Cannot create ConnectionManager", e);
		}
        return cm ;
    }
    

    public void closeConnection(Connection con)  throws TelosysToolsException {
	    try {
		    con.close();
		} catch (SQLException e) {
			throw new TelosysToolsException("Cannot close JDBC connection", e);
		}
    }

    public DbConnectionStatus testConnection(Connection con)  throws TelosysToolsException {
        if ( con == null ) {
        	throw new TelosysToolsException("Invalid parameter : Connection is null");
        }
        
        String productName = "?";
        String productVersion = "?";
        String catalog = "?";
        String schema = "?";
        boolean autocommit ;
        Properties clientInfo = new Properties();
        
        String operation = "?" ;
		try {
			operation = "getCatalog()" ;
			catalog = con.getCatalog() ;
			
			operation = "getSchema()" ;
			schema = con.getSchema();
			
			operation = "getAutoCommit()" ;
			autocommit = con.getAutoCommit() ;
			
			operation = "getClientInfo()" ;
			clientInfo = con.getClientInfo();
			
			operation = "getMetaData()" ;
			DatabaseMetaData dbmd =con.getMetaData();
			
			productName = dbmd.getDatabaseProductName();
			productVersion = dbmd.getDatabaseProductVersion();
			
		} catch (SQLException e) {
			throw new TelosysToolsException("SQLException on '" + operation + "'", e);
		} catch (Exception e) {
			throw new TelosysToolsException("Exception on '" + operation + "'", e);
		}
		return new DbConnectionStatus(productName, productVersion, catalog, schema, autocommit, clientInfo) ;
    }

}
