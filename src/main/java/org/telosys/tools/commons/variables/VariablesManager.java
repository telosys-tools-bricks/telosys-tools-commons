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

/**
 * Variable manager designed to store a set of variables and replace them by their values  
 * 
 * @author L. Guerin
 *
 */
public class VariablesManager {

	/**
	 * Set of variables with their values 
	 * 
	 * e.g.  "${VAR1}" --> "VALUE1"
	 */
	private final HashMap<String,String> hmVariables ; 
	
	
	/**
	 * Constructor
	 * @param hmVariables set of variables, each variable name is supposed to contains "${" and "}" 
	 */
	public VariablesManager(HashMap<String,String> hmInitialVariables) 
	{
		super();
		//this.hmVariables = hmVariables;
		// v 2.0.6 : init by copy in order to be able to change some values after
		this.hmVariables = new HashMap<String,String>() ;
		for (String varName : hmInitialVariables.keySet()) {
			hmVariables.put(varName, hmInitialVariables.get(varName));
		}
	}

	/**
	 * Constructor
	 * @param variables set of variables, each variable name is NOT supposed to contains "${" and "}" 
	 */
	public VariablesManager(Variable[] variables) 
	{
		super();
		this.hmVariables = new HashMap<String,String>() ;
		if ( variables != null )
		{
			if ( variables.length > 0 )
			{
				for ( int i = 0 ; i < variables.length ; i++ )
				{
					Variable v = variables[i];
					if ( v != null )
					{
						if ( v.getName() != null && v.getValue() != null )
						{
							this.hmVariables.put( "${"+v.getName()+"}", v.getValue() );
						}
					}
				}
			}
		}
	}

	/**
	 * Returns all the variables names ( list of ${name} )
	 * @return
	 */
	public List<String> getVariablesNames()
	{
		LinkedList<String> list = new LinkedList<String>() ;
		if ( hmVariables != null )
		{
			for (String varName : hmVariables.keySet()) {
				list.add(varName);
			}
		}
		return list ;
	}
	
	public String getVariableValue(String var)
	{
		if ( hmVariables != null )
		{
			return (String) hmVariables.get(var);
		}
		return null ;
	}
	
	public String replaceVariables(String s)
	{
		if ( null == s ) return null ;
		if ( s.length() < 3 ) return s ; // cannot contain "${x}"
		
		StringBuffer sb = new StringBuffer();
		replaceVariables( s, sb ) ;
		return sb.toString() ;
	}
	
	private void replaceVariables(String s, StringBuffer sb )
	{
		if ( null == s ) return ;
		
		//if ( s.length() < 3 ) return ; // cannot contain "${x}"
		
        int i = s.indexOf("${");
        if ( i >= 0 ) 
        {
            int j = s.indexOf("}");
            if ( j > i )
            {
                String var = s.substring(i,j+1); // "${MYVAR}"                
                String value = getVariableValue(var);

                String sBeforeVar = s.substring(0,i);
                String sAfterVar  = s.substring(j+1);
                sb.append( sBeforeVar ) ;
                if ( value != null )
                {
                    sb.append( value ) ;
                }
                else
                {
                    sb.append( var ) ;
                }
                                
                replaceVariables( sAfterVar, sb );
            }
        }
        else
        {
            sb.append( s ) ;
        }
	}

	/**
	 * Change the value for a given variable (only in the VariableManger context)
	 * @param variableName
	 * @param variableValue
	 * @return
	 */
	public void changeVariableValue(String variableName, String variableValue )
	{
		if ( null == variableName ) return ;
		if ( null == variableValue ) return ;
		if ( hmVariables.get(variableName) != null ) {
			hmVariables.put(variableName, variableValue);
		}
		else {
			throw new RuntimeException("Unknown variable '" + variableName +"'");
		}
	}
	
	/**
	 * Transform all the values of variables ending by "_PKG" (replace '.' by '/' )
	 */
	public void transformPackageVariablesToDirPath(){
		List<String> names = this.getVariablesNames();
		for ( String name : names ) {
			if ( name.endsWith("_PKG}" ) ) {
				String value = this.getVariableValue(name) ;
				String newValue = value.replace('.', '/');
				this.changeVariableValue(name, newValue);
			}
		}		
	}	
}
