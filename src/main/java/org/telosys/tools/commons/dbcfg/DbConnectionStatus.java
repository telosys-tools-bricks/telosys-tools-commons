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

import java.util.Properties;

/**
 * Database connection status 
 * 
 * @author Laurent GUERIN
 *
 */
public class DbConnectionStatus {

	private final String productName ;
	private final String productVersion ;
	private final String catalog ;
	private final String schema ;
	private final boolean autocommit;
	private final Properties clientInfo ;
	
    /**
     * Constructor
     * @param telosysToolsCfg
     */
    public DbConnectionStatus(String productName, String productVersion, String catalog, String schema, boolean autocommit, Properties clientInfo)  {
		super();
		this.productName = productName;
		this.productVersion = productVersion;
		this.catalog = catalog;
		this.schema = schema;
		this.autocommit= autocommit;
		this.clientInfo= clientInfo;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}

	public boolean isAutocommit() {
		return autocommit;
	}

	public Properties getClientInfo() {
		return clientInfo;
	}
	
}
