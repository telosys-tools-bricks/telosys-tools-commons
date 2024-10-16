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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

/**
 * Variable manager designed to store a set of variables and replace them by their values  
 * 
 * @author L. Guerin
 *
 */
public class VariablesManager {
	
	private static final String VAR_PREFIX = "${" ;
	private static final String VAR_SUFFIX = "}" ;

	/**
	 * Map of variables with their values (e.g. "VAR1" --> "VALUE1")
	 */
	private final HashMap<String,String> hmVariables ; 
	
	
	/**
	 * Constructor by copy (a new Map is created to store the given variables)
	 * @param hmVariables map of variables
	 */
	public VariablesManager(Map<String,String> variables) {
		super();
		// init by copy in order to be able to change some values after
		this.hmVariables = new HashMap<>() ;
		for (Map.Entry<String,String> entry : variables.entrySet()) {
			hmVariables.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Constructor
	 * @param variables array of variables
	 */
	public VariablesManager(Variable[] variables) {
		super();
		// init by copy in order to be able to change some values after
		this.hmVariables = new HashMap<>() ;
		if ( variables != null ) {
			for ( int i = 0 ; i < variables.length ; i++ ) {
				Variable v = variables[i];
				if ( v != null  &&  v.getName() != null  &&  v.getValue() != null ) {
					this.hmVariables.put(v.getName(), v.getValue() );
				}
			}
		}
	}

	/**
	 * Returns all the variables names 
	 * @return
	 */
	public List<String> getVariablesNames() {
		LinkedList<String> list = new LinkedList<>() ;
		if ( hmVariables != null ) {
			for (String varName : hmVariables.keySet()) {
				list.add(varName);
			}
		}
		return list ;
	}
	
	public String getVariableValue(String var) {
		if ( hmVariables != null ) {
			return hmVariables.get(var);
		}
		return null ;
	}

	public String replaceVariables(String s) {
		if ( null == s ) return null ;
		if ( s.length() < 3 ) return s ; // cannot contain "${x}"
		
		StringBuilder sb = new StringBuilder();
		replaceVariables( s, sb ) ;
		return sb.toString() ;
	}
	
	private void replaceVariables(String s, StringBuilder sb ) {
		if ( null == s ) {
			return ;
		}
		
        int i = s.indexOf(VAR_PREFIX);
        if ( i >= 0 ) {
            int j = s.indexOf(VAR_SUFFIX);
            if ( j > i ) {
                String symbolicVariableName = s.substring(i,j+1); // "${MYVAR}"                
                String varValue = getVariableValue(extractVariableName(symbolicVariableName));

                String partBeforeVar = s.substring(0,i);
                String partAfterVar  = s.substring(j+1);
                sb.append( partBeforeVar ) ;
                if ( varValue != null ) {
                    sb.append( varValue ); // replace by variable value
                }
                else {
                    sb.append( symbolicVariableName ); // keep symbolic name as is 
                }
                // recursive call 
                replaceVariables( partAfterVar, sb );
            }
        }
        else {
            sb.append( s ) ;
        }
	}
	
	
	/**
	 * Extract the variable name from the given symbilic name 
	 * @param symbolicVariableName  example '${varName}'
	 * @return 
	 */
	protected String extractVariableName(String symbolicVariableName) {
		if ( symbolicVariableName.startsWith(VAR_PREFIX) && symbolicVariableName.endsWith(VAR_SUFFIX)) {
			String s = symbolicVariableName.substring(2); // Removes the first 2 characters
			return s.substring(0, s.length() - 1); // Removes the last character
		}
		else {
			throw new IllegalArgumentException("Invalid symbolic variable name '" + symbolicVariableName + "'");
		}
	}
	/**
	 * Set a variable (usable to create or update a variable)
	 * @param variableName
	 * @param variableValue
	 * @return
	 * @since  4.2.0
	 */
	public void setVariable(String variableName, String variableValue ) {
		if ( null == variableName ) throw new IllegalArgumentException("Variable name is null") ;
		if ( null == variableValue ) throw new IllegalArgumentException("Variable value is null") ;
		hmVariables.put(variableName, variableValue);

		if ( hmVariables.get(variableName) != null ) {
			hmVariables.put(variableName, variableValue);
		}
		else {
			throw new TelosysRuntimeException("Unknown variable '" + variableName +"'");
		}
	}

	/**
	 * Set a variable (does nothing if the variable is not defined)
	 * @param variableName
	 * @since  4.2.0
	 */
	public void unsetVariable(String variableName) {
		if ( null == variableName ) throw new IllegalArgumentException("Variable name is null") ;
		hmVariables.remove(variableName);
	}
	
	/**
	 * Returns a 'deep copy' of the current instance
	 * @return
	 */
	public VariablesManager copy() {
		return new VariablesManager(this.hmVariables); // Constructor by copy
	}
	
}
