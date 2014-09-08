/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
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


public class Variable implements Comparable<Variable>
{
	private String sVariableName;
	private String sVariableValue;
	
	/**
	 * Constructor
	 * @param name
	 * @param value
	 */
	public Variable( String name, String value ) 
	{
		super();
		sVariableName  = ( name  != null ? name.trim() : "" ) ;
		sVariableValue = ( value != null ? value : "" );
	}

	//------------------------------------------------------------

	public String getName() 
	{
		return sVariableName;
	}

	protected void setName(String name ) 
	{
		sVariableName = ( name != null ? name.trim() : "" ) ;
	}

	/**
	 * Returns the variable name between "${ and "}"
	 * @return
	 */
	public String getSymbolicName() 
	{
		return "${"+sVariableName+"}" ;
	}

	//------------------------------------------------------------
	
	public String getValue() 
	{
		return sVariableValue;
	}

	protected void setValue(String value ) 
	{
		sVariableValue = ( value != null ? value : "" );
	}

	//------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Variable other) {
		return this.getName().compareTo( other.getName() );
	}

}