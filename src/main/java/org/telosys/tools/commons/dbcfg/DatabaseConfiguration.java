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

import java.util.StringTokenizer;


/**
 * Class for a database configuration ( loaded from the "databases.dbcfg" XML file ) 
 * 
 * @author Laurent GUERIN
 * 
 */
public class DatabaseConfiguration
{	
    private final static String  TO_BE_DEFINED          = "TO_BE_DEFINED" ;

    private int        id                       = 0 ;

    private String     name                     = "";

    private String     jdbcUrl                  = "";

    private String     driverClass              = "";

    private String     user                     = "";

    private String     password                 = "";

    private String     isolationLevel           = "";

    private int        poolSize                 = 1;
   
    //private Properties _properties                = null;

    private String     metadataCatalog          = null;

    private String     metadataSchema           = null;

    private String     metadataTableNamePattern = null;
    
    private String     metadataTableNameInclude = null; // ver 2.1.1
    
    private String     metadataTableNameExclude = null; // ver 2.1.1

    private String     metadataTableTypes       = null;

    //private String[]   _arrayMetadataTableTypes   = null;
	private String     typeName = "" ; // ver 2.1.0
	private String     dialect  = "" ; // ver 2.1.0

	//----------------------------------------------------------------------------------
    /**
     * Constructor <br>
     * Initialize the database configuration with the given DatabaseType and the default values
     */
    public DatabaseConfiguration(int databaseId, DatabaseType databaseType) {
    	super();
    	//--- Set the ID
    	this.id = databaseId ;
    	//--- Initialize default values from the given type
    	this.name            = databaseType.getName() ;
    	this.driverClass     = databaseType.getDriver();
    	this.jdbcUrl         = databaseType.getUrl();
    	this.user            = TO_BE_DEFINED ;
    	this.password        = TO_BE_DEFINED ;
    	this.dialect         = databaseType.getDialect();
    	this.typeName        = databaseType.getTypeName();
    	
    	this.poolSize        = 1 ;
    	this.isolationLevel  = "" ;
    	
    	this.metadataCatalog          = databaseType.getMetadataCatalog();
    	this.metadataSchema           = TO_BE_DEFINED ;
    	this.metadataTableNamePattern = "%" ;
    	this.metadataTableNameInclude = "" ;
    	this.metadataTableNameExclude = "" ;
    	this.metadataTableTypes       = "TABLE" ;
    	
    }

	//----------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public DatabaseConfiguration() {
    	super();
    }

	//----------------------------------------------------------------------------------
    //  
    //----------------------------------------------------------------------------------
    public int getDatabaseId()
    {
        return id;
    }
    public void setDatabaseId(int id) {
		this.id = id;
	}

    //----------------------------------------------------------------------------------
    public String getDatabaseName()
    {
        return name;
    }
	public void setDatabaseName(String databaseName) {
		this.name = databaseName;
	}

    //----------------------------------------------------------------------------------
    public String getDriverClass()
    {
        return driverClass;
    }
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}


    //----------------------------------------------------------------------------------
    public String getJdbcUrl()
    {
        return jdbcUrl;
    }
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

    //----------------------------------------------------------------------------------
    public String getUser()
    {
    	return user ;
    }
	public void setUser(String user) {
		this.user = user;
	}
    
    //----------------------------------------------------------------------------------
    public String getPassword()
    {
    	return password ;
    }
	public void setPassword(String password) {
		this.password = password;
	}

    //----------------------------------------------------------------------------------
    public String getIsolationLevel()
    {
        return isolationLevel;
    }
	public void setIsolationLevel(String isolationLevel) {
		this.isolationLevel = isolationLevel;
	}

    //----------------------------------------------------------------------------------
    public int getPoolSize()
    {
        return poolSize;
    }
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	//----------------------------------------------------------------------------------
	/**
	 * Returns the database type name or vendor name <br>
	 * Can be used as JPA type or datasource type<br>
	 * ( e.g. "DB2", "DERBY", "H2", "MYSQL", "ORACLE", "POSTGRESQL", etc )
	 * @return
	 */
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

    //----------------------------------------------------------------------------------
	/**
	 * Returns the database dialect <br>
	 * ( e.g. the Hibernate dialect class )
	 * @return
	 */
	public String getDialect() {
		return dialect;
	}
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}


    //----------------------------------------------------------------------------------
    // Metadata configuration ( tag <metadata ... /> )
    //----------------------------------------------------------------------------------
    public String getMetadataCatalog()
    {
        return metadataCatalog;
    }
	public void setMetadataCatalog(String metadataCatalog) {
		this.metadataCatalog = metadataCatalog;
	}

    public String getMetadataSchema()
    {
        return metadataSchema;
    }
	public void setMetadataSchema(String metadataSchema) {
		this.metadataSchema = metadataSchema;
	}

    public String getMetadataTableNamePattern()
    {
        return metadataTableNamePattern;
    }
	public void setMetadataTableNamePattern(String metadataTableNamePattern) {
		this.metadataTableNamePattern = metadataTableNamePattern;
	}

    public String getMetadataTableNameInclude()
    {
        return metadataTableNameInclude;
    }
	public void setMetadataTableNameInclude(String metadataTableNameInclude) {
		this.metadataTableNameInclude = metadataTableNameInclude;
	}

    public String getMetadataTableNameExclude()
    {
        return metadataTableNameExclude;
    }
	public void setMetadataTableNameExclude(String metadataTableNameExclude) {
		this.metadataTableNameExclude = metadataTableNameExclude;
	}

    public String getMetadataTableTypes()
    {
        return metadataTableTypes;
    }
	public void setMetadataTableTypes(String metadataTableTypes) {
		this.metadataTableTypes = metadataTableTypes;
	}
	public String[] getMetadataTableTypesArray() {
	    StringTokenizer st = new StringTokenizer(metadataTableTypes);
	    int iCount = st.countTokens();
	    String[] array = new String[iCount];
	    for ( int i = 0 ; i < iCount ; i++ )
	    {
	    	array[i] = st.nextToken();
	    }
	    return array ;
	}

    public String toString()
    {
        return "DatabaseConfiguration : "
        	+ " id = " + id 
        	+ " name = " + name 
        	+ " driver class = " + driverClass 
        	+ " URL = " + jdbcUrl 
        	+ " user = " + user ;
    }
}