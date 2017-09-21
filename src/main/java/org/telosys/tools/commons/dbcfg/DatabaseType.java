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

public class DatabaseType {

	private final String name ;
	private final String driver ;
	private final String url ;
	private final String metadataCatalog ;
	private final String typeName ; // ver 2.1.0
	private final String dialect ; // ver 2.1.0
	
	/**
	 * Constructor
	 * @param name
	 * @param typeName
	 * @param dialect
	 * @param driver
	 * @param url
	 * @param metadataCatalog
	 */
	public DatabaseType(String name, String typeName, String dialect, String driver, String url, String metadataCatalog) {
		super();
		this.name = name;
		this.typeName = typeName ;
		this.dialect = dialect ;
		this.driver = driver;
		this.url = url;
		this.metadataCatalog = metadataCatalog ;
	}

	/**
	 * Returns the database name (the name to be displayed)
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the database type name or vendor name <br>
	 * Can be used as JPA type or datasource type<br>
	 * ( e.g. "DB2", "DERBY", "H2", "MYSQL", "ORACLE", "POSTGRESQL", etc )
	 * @return
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Returns the database dialect <br>
	 * ( e.g. the Hibernate dialect class )
	 * @return
	 */
	public String getDialect() {
		return dialect;
	}

	/**
	 * Returns the JDBC driver class
	 * @return
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * Returns the JDBC connection URL
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	public String getMetadataCatalog() {
		return metadataCatalog;
	}

	@Override
	public String toString() {
		return "[typeName=" + name + ", driver=" + driver
				+ ", url=" + url + ", metadataCatalog=" + metadataCatalog + "]";
	}
	
}
