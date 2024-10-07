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
package org.telosys.tools.commons.variables;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class VariablesUtil 
{
    public static final String PROJECT_VARIABLE_PREFIX  = "ProjectVariable.";

    /**
     * Private constructor
     */
    private VariablesUtil() {
	}

    //---------------------------------------------------------------------------------------------------------
    /**
	 * Returns the specific variables defined in the given properties, using the standard project prefix <br>
	 * @param properties
	 * @return array of variables (never null)
	 */
	public static List<Variable> getSpecificVariablesFromProperties( Properties properties )
	{
		LinkedList<Variable> list = new LinkedList<>();
		Enumeration<Object> e = properties.keys() ;
		while ( e.hasMoreElements() ) {
			String key = (String) e.nextElement() ;
			if ( key.startsWith( PROJECT_VARIABLE_PREFIX ) ) {
				String sVarValue = properties.getProperty(key);
				String sVarName = key.substring( PROJECT_VARIABLE_PREFIX.length() ) ;
				//--- Add in the list 
				list.add( new Variable(sVarName, sVarValue ) );
			}
		}
		sortByVariableName(list);
		return list ;
	}
	
	private static final String PROP_ARG_IS_NULL = "Properties argument is null";
	
    //---------------------------------------------------------------------------------------------------------
	/**
	 * Put the given variables in the properties, using the standard project prefix <br>
	 * All the given variable names are supposed to be valid.
	 * @param variables
	 * @param properties
	 * @return the number of variables stored in the properties
	 */
	//public static int putVariablesInProperties( Variable[] variables, Properties properties )
	public static int putSpecificVariablesInProperties( List<Variable> variables, Properties properties )
	{
		int count = 0 ;
		if ( null == variables ) return 0 ;
		if ( null == properties ) {
			throw new IllegalArgumentException(PROP_ARG_IS_NULL);
		}
		for ( Variable var : variables ) {
			putSpecificVariableInProperties(var, properties);
			count++ ;
		}
		return count ;
	}
	
    //---------------------------------------------------------------------------------------------------------
	/**
	 * Put the given variable in the properties, using the standard project prefix <br> 
	 * @param variable
	 * @param properties
	 */
	private static void putSpecificVariableInProperties( Variable variable, Properties properties ) {
		if ( null == variable ) {
			throw new IllegalArgumentException("Variable argument is null");
		}
		if ( null == properties ) {
			throw new IllegalArgumentException(PROP_ARG_IS_NULL);
		}
		String variableName = variable.getName().trim();
		if ( variableName.length() > 0 ) {
			String sPropName = PROJECT_VARIABLE_PREFIX + variableName ;
			properties.put(sPropName, variable.getValue() );
		}
	}

	//---------------------------------------------------------------------------------------------------------
	/**
	 * Sorts the given list of variable ( sorted by 'name' : see 'compareTo' in Variable )
	 * @param variablesList
	 */
	protected static void sortByVariableName( List<Variable> variablesList ) {
		Collections.sort(variablesList, new VariableNameComparator() );
	}
}
