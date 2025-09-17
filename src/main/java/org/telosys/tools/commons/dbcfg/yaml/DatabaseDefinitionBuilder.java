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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.exception.TelosysRuntimeException;

/**
 * Database definition loaded from YAML file 
 * 
 * @author Laurent GUERIN
 *
 */
public class DatabaseDefinitionBuilder {	

	//----------------------------------------------------------------------------------
    /**
     * Constructor
     */
    public DatabaseDefinitionBuilder() {
    	super();
    }
    
    public DatabaseDefinitions buildDatabaseDefinitions( Map<String,Object> yamlData) throws TelosysToolsException {
    	
    	List<DatabaseDefinition> databaseDefinitionsList = new LinkedList<>(); 
    	
		Object databases = yamlData.get("databases");
		if (databases instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) databases; // List (ArrayList)
			// Iteration on all the databases defined in the YAML file ( each "-" entry )
			for ( Object element : list ) {
				if (element instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String,Object> map = (Map<String,Object>) element; // Map (LinkedHashMap)
					DatabaseDefinition databaseDefinition = buildDatabaseDefinition(map); 
					databaseDefinitionsList.add(databaseDefinition);
				}
			}
		}
		else {
			throw new TelosysToolsException("'databases' is not a List");
		}
		return new DatabaseDefinitions(databaseDefinitionsList);
    }

//    public List<DatabaseDefinition> buildDatabaseDefinitionsList( Map<String,Object> databases) {
//    	List<DatabaseDefinition> list = new LinkedList<>();
//    	for (Map.Entry<String, Object> entry : databases.entrySet()) {
//    	    String dbName = entry.getKey();
//    	    Map<String, Object> dbConfig = (Map<String, Object>) entry.getValue();
//    	}
//    	return list;
//    }
    
    public DatabaseDefinition buildDatabaseDefinition(Map<String,Object> map) throws TelosysToolsException {
    	// New instance with default values
		DatabaseDefinition databaseDefinition = new DatabaseDefinition();
    	// Set values from the given map 
		populateFromMap(databaseDefinition, map);
		return databaseDefinition;
    }
    
    protected void populateFromMap(DatabaseDefinition target, Map<String, Object> inputMap) throws TelosysToolsException {
    	BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(target.getClass(), Object.class);
		} catch (IntrospectionException e) {
			throw new TelosysRuntimeException("Cannot introspect DatabaseDefinition class ", e);
		}
    	PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    	
        // Use JavaBeans Introspector to get property descriptors
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors ) {
        	Method setter = propertyDescriptor.getWriteMethod();
            if (setter != null) { // has a setter
                String propertyName = propertyDescriptor.getName();
                // try to get input value from Map
                Object value = inputMap.get(propertyName);
                if (value != null) {
                    // input value found => call 'setter' method
                	try {
						setter.invoke(target, value);
	                    // can throw exceptions: IntrospectionException, InvocationTargetException, IllegalAccessException
					} catch (Exception e) {
						throw new TelosysToolsException("Cannot set property '"+propertyName+"'", e);
					}
                }
            }
        }
    }
}