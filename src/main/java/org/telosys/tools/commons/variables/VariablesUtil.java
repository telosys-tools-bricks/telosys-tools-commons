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
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class VariablesUtil 
{
    public final static String PROJECT_VARIABLE_PREFIX  = "ProjectVariable.";
		
    //---------------------------------------------------------------------------------------------------------
	/**
	 * Returns all the variables available for the project from the given properties <br>
	 * The variables array contains the specific variables AND the standard variables ($SRC, $ENTITY_PKG, ...)
	 * @param prop
	 * @return
	 * @since 2.0.7
	 */
	public static Variable[] getAllVariablesFromProperties( Properties prop )
	{
    	//--- All variables : specific project variables + folders 
    	Hashtable<String, String> allVariables = new Hashtable<String, String>();
    	
    	//--- 1) Project specific variables (defined by user)
    	//Variable[] specificVariables = VariablesUtil.getSpecificVariablesFromProperties( prop );
    	List<Variable> specificVariables = VariablesUtil.getSpecificVariablesFromProperties( prop );
    	for ( Variable v : specificVariables ) {
    		allVariables.put(v.getName(), v.getValue());
    	}
    	
    	//--- 2) Packages and folders ( at the end to override specific variables if any )
    	allVariables.put( VariablesNames.ROOT_PKG,   prop.getProperty(VariablesNames.ROOT_PKG,    "") ); // v 2.0.6
    	allVariables.put( VariablesNames.ENTITY_PKG, prop.getProperty(VariablesNames.ENTITY_PKG,  "") ); // v 2.0.6
    	
    	allVariables.put( VariablesNames.SRC,      prop.getProperty(VariablesNames.SRC,      "") );
    	allVariables.put( VariablesNames.RES,      prop.getProperty(VariablesNames.RES,      "") );
    	allVariables.put( VariablesNames.WEB,      prop.getProperty(VariablesNames.WEB,      "") );
    	allVariables.put( VariablesNames.TEST_SRC, prop.getProperty(VariablesNames.TEST_SRC, "") );
    	allVariables.put( VariablesNames.TEST_RES, prop.getProperty(VariablesNames.TEST_RES, "") );
    	allVariables.put( VariablesNames.DOC,      prop.getProperty(VariablesNames.DOC,      "") );
    	allVariables.put( VariablesNames.TMP,      prop.getProperty(VariablesNames.TMP,      "") );
    	
    	//--- 3) Put together all variables 
    	LinkedList<Variable> variablesList = new LinkedList<Variable>();
    	for ( String varName : allVariables.keySet() ) {
    		String varValue = allVariables.get(varName) ;
    		variablesList.add( new Variable( varName, varValue) ) ;
    	}
    	//--- Convert list to array
//    	Variable[] allVariablesArray = variablesList.toArray( new Variable[variablesList.size()] );
//		return allVariablesArray ;
    	return listToArray(variablesList) ; // v 3.0.0
	}
	
    //---------------------------------------------------------------------------------------------------------
    /**
	 * Returns the specific variables defined in the given properties, using the standard project prefix <br>
	 * @param properties
	 * @return array of variables (never null)
	 */
	public static LinkedList<Variable> getSpecificVariablesFromProperties( Properties properties )
	{
		LinkedList<Variable> list = new LinkedList<Variable>();
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
	
    //---------------------------------------------------------------------------------------------------------
//    /**
//	 * Returns the specific variables defined in the given properties, using the standard project prefix <br>
//	 * @param properties
//	 * @return array of variables (never null)
//	 */
//	public static Variable[] getSpecificVariablesArrayFromProperties( Properties properties )
//	{
//		LinkedList<Variable> list = new LinkedList<Variable>();
//		Enumeration<Object> e = properties.keys() ;
//		
//		while ( e.hasMoreElements() )
//		{
//			String key = (String) e.nextElement() ;
//			if ( key.startsWith( PROJECT_VARIABLE_PREFIX ) )
//			{
//				String sVarValue = properties.getProperty(key);
//				String sVarName = key.substring( PROJECT_VARIABLE_PREFIX.length() ) ;
//				Variable newItem = new Variable(sVarName, sVarValue );
//				
//				//--- Insert in ascending order 
//				ListIterator<Variable> iter = list.listIterator();
//				while ( iter.hasNext() ) 
//				{
//					Variable item = iter.next();
//					if ( newItem.compareTo(item) <= 0 ) 
//					{
//						// newItem should come BEFORE item in the list.
//						// Move the iterator back one space so that it points to the correct insertion point,
//						// and end the loop.
//						iter.previous();
//						break;
//					} 
//				}
//				iter.add(newItem);
//			}
//		}
//		
//		//return list.toArray( new Variable[list.size()] );
//		return listToArray(list) ; // v 3.0.0
//	}
	
    //---------------------------------------------------------------------------------------------------------
	public static Variable getSpecificVariableFromProperties( String variableName, Properties properties )
	{
		if ( null == variableName ) {
			throw new IllegalArgumentException("Variable name argument is null");
		}
		if ( null == properties ) {
			throw new IllegalArgumentException("Properties argument is null");
		}
		if ( variableName.length() > 0 ) {
			String sPropName = PROJECT_VARIABLE_PREFIX + variableName ;
			String value = (String) properties.get(sPropName);
			Variable variable = new Variable(variableName, value);
			return variable ;
		}
		else {
			return null ;
		}
	}
	
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
			throw new IllegalArgumentException("Properties argument is null");
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
	public static void putSpecificVariableInProperties( Variable variable, Properties properties ) {
		if ( null == variable ) {
			throw new IllegalArgumentException("Variable argument is null");
		}
		if ( null == properties ) {
			throw new IllegalArgumentException("Properties argument is null");
		}
		String variableName = variable.getName().trim();
		if ( variableName.length() > 0 ) {
			String sPropName = PROJECT_VARIABLE_PREFIX + variableName ;
			properties.put(sPropName, variable.getValue() );
		}
	}

	//---------------------------------------------------------------------------------------------------------
	/**
	 * Converts the given List to Array
	 * @param variablesList
	 * @return
	 */
	public static Variable[] listToArray( List<Variable> variablesList ) {
		if ( variablesList != null && variablesList.size() > 0 ) {
			return variablesList.toArray( new Variable[variablesList.size()] );
		}
		return new Variable[0];
	}

	//---------------------------------------------------------------------------------------------------------
	/**
	 * Sorts the given list of variable ( sorted by 'name' : see 'compareTo' in Variable )
	 * @param variablesList
	 */
	public static void sortByVariableName( List<Variable> variablesList ) {
		Collections.sort(variablesList);
	}
}
