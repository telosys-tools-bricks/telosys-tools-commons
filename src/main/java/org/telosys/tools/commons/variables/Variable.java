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

public class Variable
{
	private final String variableName;
	private final String variableValue;
	
	/**
	 * Constructor
	 * @param name
	 * @param value
	 */
	public Variable( String name, String value ) {
		super();
		this.variableName  = ( name  != null ? name.trim() : "" ) ;
		this.variableValue = ( value != null ? value : "" );
	}

	/**
	 * Returns the variable name ( without "${ and "}" )
	 * @return
	 */
	public String getName() {
		return variableName;
	}

	/**
	 * Returns the variable name between "${ and "}"
	 * @return
	 */
	public String getSymbolicName() {
		return "${"+variableName+"}" ;
	}

	/**
	 * Returns the variable value
	 * @return
	 */
	public String getValue() {
		return variableValue;
	}

}