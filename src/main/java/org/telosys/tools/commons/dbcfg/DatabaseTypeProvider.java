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
package org.telosys.tools.commons.dbcfg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseTypeProvider {

	private final static List<DatabaseType>        dbTypesList = new LinkedList<DatabaseType>();
	private final static Map<String, DatabaseType> dbTypesMap  = new HashMap<String, DatabaseType>();
	
	static {
		// NB : the type name must be unique (used as key in map)
		
		dbTypesList.add( new DatabaseType(
				"DB2", // Name : must be unique (used as KEY)       
				"DB2",     
				"org.hibernate.dialect.DB2Dialect",
				"com.ibm.db2.jcc.DB2Driver", 
				"jdbc:db2://localhost/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"DERBY", // Name : must be unique (used as KEY)        
				"DERBY",     
				"org.hibernate.dialect.DerbyDialect",
				"org.apache.derby.jdbc.ClientDriver", 
				"jdbc:derby://localhost:1527/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"H2 embedded", // Name : must be unique (used as KEY) 
				"H2", 
				"org.hibernate.dialect.H2Dialect",
				"org.h2.Driver",
				"jdbc:h2:mem",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"H2 server", // Name : must be unique (used as KEY)   
				"H2",  
				"org.hibernate.dialect.H2Dialect",
				"org.h2.Driver",
				"jdbc:h2:tcp://localhost/~/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"HSQL-DB embedded",  // Name : must be unique (used as KEY)    
				"HSQL",     
				"org.hibernate.dialect.HSQLDialect",
				"org.hsqldb.jdbcDriver",
				"jdbc:hsqldb:hsql:<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"HSQL-DB server",  // Name : must be unique (used as KEY)    
				"HSQL",     
				"org.hibernate.dialect.HSQLDialect",
				"org.hsqldb.jdbcDriver",
				"jdbc:hsqldb:hsql://localhost:9001/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"INFORMIX", // Name : must be unique (used as KEY)       
				"INFORMIX",       
				"org.hibernate.dialect.InformixDialect",
				"com.informix.jdbc.IfxDriver",
				"jdbc:informix-sqli://localhost:<port>/<DB_NAME>:informixserver=<dbservername>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"INGRES",   // Name : must be unique (used as KEY)     
				"INGRES",       
				"org.hibernate.dialect.IngresDialect",
				"com.ingres.jdbc.IngresDriver",
				"jdbc:ingres://localhost:117/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"InterSystems CacheDB",   // Name : must be unique (used as KEY)     
				"CACHEDB",       
				"org.hibernate.dialect.Cache71Dialect",
				"com.intersys.jdbc.CacheDriver",
				"jdbc:Cache://localhost:1972/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"MYSQL",   // Name : must be unique (used as KEY)     
				"MYSQL",       
				"org.hibernate.dialect.MySQLDialect",
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"ORACLE",   // Name : must be unique (used as KEY)    
				"ORACLE",      
				"org.hibernate.dialect.Oracle10gDialect",
				"oracle.jdbc.driver.OracleDriver",
				"jdbc:oracle:thin:@localhost:1521:<DB_NAME>",
				"!") ) ; // null for Catalog
		
		dbTypesList.add( new DatabaseType(
				"POSTGRESQL",  // Name : must be unique (used as KEY)     
				"POSTGRESQL",      
				"org.hibernate.dialect.PostgreSQLDialect",
				"org.postgresql.Driver",
				"jdbc:postgresql://localhost:5432/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"SQL-SERVER",  // Name : must be unique (used as KEY) 
				"SQL_SERVER",  
				"org.hibernate.dialect.SQLServerDialect",
				"com.microsoft.sqlserver.jdbc.SQLServerDriver",
				"jdbc:sqlserver://localhost:1433;databaseName=<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"SYBASE ASE", // Name : must be unique (used as KEY)  
				"SYBASE",  
				"org.hibernate.dialect.SybaseASE15Dialect",
				"com.sybase.jdbc4.jdbc.SybDriver",
				"jdbc:sybase:Tds:localhost:<port>/<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"SYBASE SQL Anywhere",  // Name : must be unique (used as KEY) 
				"SYBASE",  
				"org.hibernate.dialect.SybaseAnywhereDialect",
				"com.sybase.jdbc4.jdbc.SybDriver",
				"jdbc:sybase:Tds:localhost:<port>?ServiceName=<DB_NAME>",
				"") ) ;
		
		dbTypesList.add( new DatabaseType(
				"( other )",  // Name : must be unique (used as KEY) 
				"",  
				"org.hibernate.dialect.XxxxxxDialect",
				"driver.package.DriverClass",
				"jdbc:xxxxxx",
				"") ) ;
		
		for ( DatabaseType databaseType : dbTypesList ) {
			dbTypesMap.put(databaseType.getName(), databaseType);
		}
	}

	public final static List<DatabaseType> getDbTypesList() {
		return dbTypesList ;
	}
	
	public final static Map<String,DatabaseType> getDbTypesMap() {
		return dbTypesMap ;
	}

	public final static DatabaseType getDatabaseTypeByName(String name) {
		return dbTypesMap.get(name) ;
	}
}
