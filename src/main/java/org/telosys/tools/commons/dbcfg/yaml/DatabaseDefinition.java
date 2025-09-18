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

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.telosys.tools.commons.StrUtil;

/**
 * Database definition loaded from YAML file 
 * 
 * @author Laurent GUERIN
 *
 */
public class DatabaseDefinition {	

	private static final String VOID = "";
	
    private String     id       = VOID ;
    private String     name     = VOID;
    private String     type     = VOID;
    //--- JDBC connection
    private String     driver   = VOID;
    private String     url      = VOID;
    private String     user     = VOID;
    private String     password = VOID;
    //--- Metadata criteria
    private String     catalog          = "!";  // default value since v 4.3.0 ("!" = do not use to get metadata)
    private String     schema           = "!";  // default value since v 4.3.0 ("!" = do not use to get metadata)
    private String     tableNamePattern = "%" ; // default value since v 4.3.0
    private String     tableNameInclude = VOID;
    private String     tableNameExclude = VOID;
    private String     tableTypes       = "TABLE" ; // default value since v 4.3.0
    
    //--- DSL model creation
//	"dbModelName" is UNUSED : removed in v 4.3.0 (before just for backward compatibility with YAML parser) 
	// what kind of links to define in the model
	private boolean    linksManyToOne  = true ; 
	private boolean    linksOneToMany  = false ; 
	private boolean    linksManyToMany = false ; 
	// what kind of database information to define in the model (true by default for all)
	private boolean    dbCatalog       = true ;  // v 4.1.0  @DbCatalog(string)       Entity 
	private boolean    dbSchema        = true ;  // v 4.1.0  @DbSchema(string)        Entity 
	private boolean    dbTable         = true ;  // v 4.1.0  @DbTable(string)         Entity 
	private boolean    dbView          = true ;  // v 4.1.0  @DbView                  Entity 
	private boolean    dbComment       = true ;  // v 4.1.0  @DbComment(string)       Entity & Attribute
	private boolean    dbDefaultValue  = true ;  // v 4.1.0  @DbDefaultValue(string)  Attribute 
	private boolean    dbName          = true ;  // v 4.1.0  @DbName(string)          Attribute 
	private boolean    dbType          = true ;  // v 4.1.0  @DbType(string)          Attribute 
	// @DbTablespace(string) / Entity : not retrieved from metadata

	private static final Map<String, String> JDBC_DRIVERS_MAP ;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("POSTGRESQL", "org.postgresql.Driver"                        );
        map.put("MYSQL",      "com.mysql.cj.jdbc.Driver"                     );
        map.put("MARIADB",    "org.mariadb.jdbc.Driver"                      );
        map.put("SQLITE",     "org.sqlite.JDBC"                              );
        map.put("SQLSERVER",  "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
        map.put("ORACLE",     "oracle.jdbc.OracleDriver"                     );
        map.put("H2",         "org.h2.Driver"                                );
        map.put("DERBY",      "org.apache.derby.jdbc.ClientDriver"           );
        JDBC_DRIVERS_MAP = map; // assign to final field
    }
	//----------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public DatabaseDefinition() {
    	super();
    }
    
    /**
     * To avoid null value if nothing in YAML file (eg "tableTypes:" (no value) )
     * @param s
     * @return
     */
    private String voidIfNull(String s) {
    	return s != null ? s : "" ;
    }

	public String getId() {
		return voidIfNull(id);
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return voidIfNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return voidIfNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}

	//----------------------------------------------------------------------------------

	public String getUrl() {
		return voidIfNull(url);
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		// try to find a default driver class name if not defined  
		if ( StrUtil.nullOrVoid(this.driver) ) { // driver undefined 
			// => try to get default driver from the database type if any 
			String databaseType = getType();
			if ( ! StrUtil.nullOrVoid(databaseType) ) {
				// database type is defined => use it to get the default driver class name 
				String defaultDriver = JDBC_DRIVERS_MAP.get(databaseType.toUpperCase());
				if ( defaultDriver != null ) {
					return defaultDriver;
				}
			}
		}
		// in all others cases => return the driver value "as is" 
		return voidIfNull(driver);
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUser() {
		return voidIfNull(user);
	}
	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return voidIfNull(password);
	}
	public void setPassword(String password) {
		this.password = password;
	}

	//----------------------------------------------------------------------------------

	public String getCatalog() {
		return voidIfNull(catalog);
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return voidIfNull(schema);
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTableNamePattern() {
		return voidIfNull(tableNamePattern);
	}
	public void setTableNamePattern(String tableNamePattern) {
		this.tableNamePattern = tableNamePattern;
	}

	public String getTableNameInclude() {
		return voidIfNull(tableNameInclude);
	}
	public void setTableNameInclude(String tableNameInclude) {
		this.tableNameInclude = tableNameInclude;
	}

	public String getTableNameExclude() {
		return voidIfNull(tableNameExclude);
	}
	public void setTableNameExclude(String tableNameExclude) {
		this.tableNameExclude = tableNameExclude;
	}

	public String getTableTypes() {
		return voidIfNull(tableTypes);
	}
	public void setTableTypes(String tableTypes) { // used by yaml parser
		this.tableTypes = tableTypes;
	}
	public String[] getTableTypesArray() {
		if ( tableTypes != null ) {
		    StringTokenizer st = new StringTokenizer(tableTypes);
		    int n = st.countTokens();
		    String[] array = new String[n];
		    for ( int i = 0 ; i < n ; i++ ) {
		    	array[i] = st.nextToken();
		    }
		    return array ;
		}
		else {
			return new String[0];
		}
	}

// removed in v 4.3.0 : getDbModelName() and setDbModelName(..)

	//----------------------------------------------------------------------------------
	public boolean isLinksManyToOne() {
		return linksManyToOne;
	}
	public void setLinksManyToOne(boolean linksManyToOne) {
		this.linksManyToOne = linksManyToOne;
	}

	public boolean isLinksOneToMany() {
		return linksOneToMany;
	}
	public void setLinksOneToMany(boolean linksOneToMany) {
		this.linksOneToMany = linksOneToMany;
	}

	public boolean isLinksManyToMany() {
		return linksManyToMany;
	}
	public void setLinksManyToMany(boolean linksManyToMany) {
		this.linksManyToMany = linksManyToMany;
	}

	//----------------------------------------------------------------------------------
	public boolean isDbDefaultValue() { // v 4.1.0
		return dbDefaultValue;
	}
	public void setDbDefaultValue(boolean databaseDefaultValue) { // v 4.1.0
		this.dbDefaultValue = databaseDefaultValue;
	}

	public boolean isDbComment() { // v 4.1.0
		return dbComment;
	}
	public void setDbComment(boolean databaseComment) { // v 4.1.0
		this.dbComment = databaseComment;
	}

	public boolean isDbCatalog() {
		return dbCatalog;
	}
	public void setDbCatalog(boolean dbCatalog) {
		this.dbCatalog = dbCatalog;
	}

	public boolean isDbName() {
		return dbName;
	}
	public void setDbName(boolean dbName) {
		this.dbName = dbName;
	}

	public boolean isDbSchema() {
		return dbSchema;
	}
	public void setDbSchema(boolean dbSchema) {
		this.dbSchema = dbSchema;
	}

	public boolean isDbTable() {
		return dbTable;
	}
	public void setDbTable(boolean dbTable) {
		this.dbTable = dbTable;
	}

	public boolean isDbType() {
		return dbType;
	}
	public void setDbType(boolean dbType) {
		this.dbType = dbType;
	}

	public boolean isDbView() {
		return dbView;
	}
	public void setDbView(boolean dbView) {
		this.dbView = dbView;
	}

}