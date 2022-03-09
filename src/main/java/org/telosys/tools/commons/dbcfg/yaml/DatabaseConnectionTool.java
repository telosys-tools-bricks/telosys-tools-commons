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
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DbConnectionStatus;

public class DatabaseConnectionTool {

    /**
     * Constructor
     */
    private DatabaseConnectionTool() {
		super();
	}
	
    public static void closeConnection(Connection con)  throws TelosysToolsException {
	    try {
		    con.close();
		} catch (SQLException e) {
			throw new TelosysToolsException("Cannot close JDBC connection", e);
		}
    }
    
    public static DbConnectionStatus getConnectionStatus(Connection con) throws TelosysToolsException {
        if ( con == null ) {
        	throw new IllegalArgumentException("Invalid parameter : Connection is null");
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
			//schema = con.getSchema(); // Doesn't work with Oracle : throwable Exception
			schema = getSchema(con);
			
			operation = "getAutoCommit()" ;
			autocommit = con.getAutoCommit() ;
			
			operation = "getClientInfo()" ;
			//clientInfo = con.getClientInfo(); // Doesn't work with Oracle : throwable Exception
			clientInfo = getClientInfo(con);
			
			operation = "getMetaData()" ;
			DatabaseMetaData dbmd =con.getMetaData();
			
			productName = dbmd.getDatabaseProductName();
			productVersion = dbmd.getDatabaseProductVersion();
			
		} catch (SQLException e) {
			throw new TelosysToolsException("SQLException on '" + operation + "'", e);
		} catch (Exception e) {
			throw new TelosysToolsException("Exception on '" + operation + "'", e);
		} catch ( Throwable e ) {
			throw new TelosysToolsException("Throwable on '" + operation + "'", e);
    	}
		return new DbConnectionStatus(productName, productVersion, catalog, schema, autocommit, clientInfo) ;
    }

    /**
     * Specific method for 'getSchema' due to errors thrown by some drivers like Oracle
     * @param con
     * @return
     */
    private static String getSchema(Connection con) {
    	String result = "" ;
    	try {
   			result = con.getSchema();
    	}
    	catch ( SQLException e ) {
    		result = "ERROR : SQLException : " + e.getMessage() ;
    	}
    	catch ( Exception e ) {
    		result = "ERROR : Exception : " + e.getMessage() ;
    	}
    	catch ( Throwable e ) {
    		result = "ERROR : Throwable : " + e.getMessage() ; // yes, it happends with Oracle !
    	}
    	return result ;
    }
    
    /**
     * Specific method for 'getClientInfo' due to errors thrown by some drivers like Oracle
     * @param con
     * @return
     */
    private static Properties getClientInfo(Connection con) {
    	Properties clientInfo ;
    	try {
    		clientInfo = con.getClientInfo();
    	}
    	catch ( Throwable e ) { // Top of the exceptions tree
    		clientInfo = new Properties(); // Void properties
    	}
    	return clientInfo ;
    }
}
