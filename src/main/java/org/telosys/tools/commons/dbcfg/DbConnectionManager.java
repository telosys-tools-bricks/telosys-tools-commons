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
import java.sql.SQLException;
import java.util.List;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.jdbc.ConnectionManager;

public class DbConnectionManager {

	private final TelosysToolsCfg telosysToolsCfg ;
	private final JavaLibraries javaLibraries ;
	private final TelosysToolsLogger logger ;
	
    /**
     * Constructor
     * @param telosysToolsCfg
     */
    public DbConnectionManager(TelosysToolsCfg telosysToolsCfg) throws TelosysToolsException {
		super();
		this.logger = telosysToolsCfg.getLogger();
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
    	return openConnection(databaseConfiguration);
    }

    /**
     * Returns a connection on the database identified by the given id
     * @param databaseId
     * @return
     * @throws TelosysToolsException
     */
    public Connection getConnection(int databaseId ) throws TelosysToolsException {
    	DatabaseConfiguration databaseConfiguration = getDatabasesConfigurations().getDatabaseConfiguration(databaseId) ;
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
			logger.error("Cannot get database connection", e);
			throw e;
//			Throwable cause = e.getCause();
//			if ( cause != null && cause instanceof SQLException ) {
//				SQLException sqlException = (SQLException)cause;
//	            MsgBox.error("Cannot connect to the database ! "
//	                      + "\n SQLException :"
//	                      + "\n . Message   : " + sqlException.getMessage() 
//	                      + "\n . ErrorCode : " + sqlException.getErrorCode() 
//	                      + "\n . SQLState  : " + sqlException.getSQLState() 
//	                      );
//			}
//			else {
//				msgBoxErrorWithClassPath("Cannot connect to the database !", e, cm.getLibraries());
//			}
		} catch (Throwable e) {
			logger.error("Cannot get database connection", e);
			throw new TelosysToolsException("Cannot get database connection", e);
//			logException(e);
//		    MsgBox.error("Cannot connect to the database ! "
//		            + "\n Exception : " + e.getClass().getName()
//		            + "\n . Message : " + e.getMessage() 
//		            );
		}
    }
    
    /**
     * Creates a ConnectionManager instance
     * @param libraries all the libraries where to search the JDBC Driver
     * @return
     */
    private ConnectionManager createConnectionManager() throws TelosysToolsException {
//        if ( javaLibraries.getLibraries().size() == 0 ) {
//        	throw new TelosysToolsException("No library defined (size=0)");
//        }
        ConnectionManager cm = null ;
		try {
			if ( javaLibraries.getLibraries().size() > 0 ) {
				// ConnectionManager with specific libraries
				cm = new ConnectionManager( javaLibraries.getLibFilePaths(), telosysToolsCfg.getLogger() );
			}
			else {
				// ConnectionManager based on the standard ClassPath
				cm = new ConnectionManager( telosysToolsCfg.getLogger() );
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

    public void testConnection(Connection con)  throws TelosysToolsException {
        if ( con == null ) {
        	throw new TelosysToolsException("Invalid parameter : Connection is null");
        }
        String catalog = "?";
        boolean autocommit ;
        String operation = "?" ;
		try {
			operation = "getCatalog()" ;
			catalog = con.getCatalog() ;
			
			operation = "getAutoCommit()" ;
			autocommit = con.getAutoCommit() ;
			
		} catch (SQLException e) {
			logger.error("SQLException on '" + operation + "'", e);
			throw new TelosysToolsException("SQLException on '" + operation + "'", e);
		} catch (Throwable e) {
			logger.error("Exception on '" + operation + "'", e);
			throw new TelosysToolsException("Exception on '" + operation + "'", e);
		}
		logger.info("Test connection OK : catalog = '" + catalog + "', autocommit = " + autocommit );
    }

//    public DbInfo getDatabaseInfo(Connection con)  throws TelosysToolsException {
//        if ( con == null ) {
//        	throw new TelosysToolsException("Invalid parameter : Connection is null");
//        }
//        DatabaseMetaData dbmd ;
//        try {
//			dbmd = con.getMetaData();			
//		} catch (SQLException e) {
//			throw new TelosysToolsException("Cannot get metadata", e);
//		}
//        return new DbInfo(dbmd);
//    }

}
