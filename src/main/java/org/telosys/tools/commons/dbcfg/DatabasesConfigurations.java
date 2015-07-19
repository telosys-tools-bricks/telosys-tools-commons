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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabasesConfigurations {

	private int databaseMaxId     = 0 ;
	
	private int databaseDefaultId = 0 ;
	
	private Map<Integer, DatabaseConfiguration> databasesMap = new HashMap<Integer, DatabaseConfiguration>();

	/**
	 * Constructor
	 */
	public DatabasesConfigurations() {
		super();
		databaseDefaultId = 0 ;
	}
	
	public int getDatabaseMaxId() {
		return databaseMaxId;
	}
	public void setDatabaseMaxId(int dbMaxId) {
		this.databaseMaxId = dbMaxId;
	}

	public int getDatabaseDefaultId() {
		return databaseDefaultId;
	}
	public void setDatabaseDefaultId(int dbDefaultId) {
		this.databaseDefaultId = dbDefaultId;
	}

	/**
	 * Store a database configuration (add a new one or replace if the id already exists )
	 * @param databaseConfiguration
	 */
	public void storeDatabaseConfiguration(DatabaseConfiguration databaseConfiguration) {
		// Integer databaseId = new Integer(databaseConfiguration.getDatabaseId());
		Integer databaseId = Integer.valueOf(databaseConfiguration.getDatabaseId()); // v 3.0.0 : Sonar Issue Fixed
		databasesMap.put(databaseId, databaseConfiguration);
	}
	
	/**
	 * Returns the database configuration for the given database id
	 * @param id
	 * @return the database configuration (or null if none)
	 */
	public DatabaseConfiguration getDatabaseConfiguration(int id) {
		// Integer databaseId = new Integer(id);
		Integer databaseId = Integer.valueOf(id); // v 3.0.0 : Sonar Issue Fixed
		return databasesMap.get(databaseId) ;
	}
	
	/**
	 * Returns the database configuration for the default database
	 * @return the database configuration (or null if none)
	 */
	public DatabaseConfiguration getDatabaseConfiguration() {
		int defaultDbId = getDatabaseDefaultId() ;
		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration( getDatabaseDefaultId() ) ;
		if  ( databaseConfiguration == null ) {
			throw new RuntimeException("Cannot get database configuration for default id " + defaultDbId );
		}
		return databaseConfiguration ;
	}
	
	/**
	 * Removes the database configuration identified by the given id
	 * @param id
	 * @return true if found and removed (else false)
	 */
	public boolean removeDatabaseConfiguration(int id) {
		// Integer databaseId = new Integer(id);
		Integer databaseId = Integer.valueOf(id); // v 3.0.0 : Sonar Issue Fixed
		DatabaseConfiguration removed = databasesMap.remove(databaseId) ;
		return removed != null ;
	}
	
	/**
	 * Returns the number of databases configurations stored
	 * @return
	 */
	public int getNumberOfDatabases() {
		return databasesMap.size();
	}
	
	/**
	 * Return a list of all the databases configuration ordered by id
	 * @return
	 */
	public List<DatabaseConfiguration> getDatabaseConfigurationsList() {
		
		//--- List of sorted id
		ArrayList<Integer> keysArrayList = new ArrayList<Integer>(databasesMap.keySet()) ;
		Collections.sort(keysArrayList) ;
		
		LinkedList<DatabaseConfiguration> list = new LinkedList<DatabaseConfiguration>();
		for ( Integer id : keysArrayList ) {
			list.add( databasesMap.get(id) ) ;
		}
		return list;
	}
	
}
