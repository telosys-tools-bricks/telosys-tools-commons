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

import java.util.LinkedList;
import java.util.List;



/**
 * Databases definitions loaded from YAML file 
 * 
 * @author Laurent GUERIN
 *
 */
public class DatabaseDefinitions {

    private List<DatabaseDefinition> databaseDefinitionsList = new LinkedList<>();

	//----------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public DatabaseDefinitions() {
    	super();
    }
    /**
     * Constructor
     * @param databaseDefinitionsList
     */
    public DatabaseDefinitions(List<DatabaseDefinition> databaseDefinitionsList ) {
    	super();
    	this.databaseDefinitionsList = databaseDefinitionsList;
    }

	public List<DatabaseDefinition> getDatabases() {
		return databaseDefinitionsList;
	}

	/**
	 * NB: keep "databases" name in order to match property name in YAML file
	 * @param databaseDefinitionsList
	 */
	public void setDatabases(List<DatabaseDefinition> databaseDefinitionsList) {
		this.databaseDefinitionsList = databaseDefinitionsList;
	}
	
	
	/**
	 * Return the database definition for the given database id (or null if none)
	 * @param databaseId
	 * @return
	 */
	public DatabaseDefinition getDatabaseDefinition(String databaseId) {
		if ( databaseId != null && databaseDefinitionsList != null ) {
			for ( DatabaseDefinition dd : databaseDefinitionsList ) {
				if ( databaseId.equals(dd.getId()) ) {
					return dd;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns true if the current database definitions contains the given databaseId
	 * @param databaseId
	 * @return
	 */
	public boolean containsDatabase(String databaseId) {
		return getDatabaseDefinition(databaseId) != null ;
	}
}