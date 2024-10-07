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

/**
 * Generator context names ( objects names in the Velocity Context )
 * 
 * @author Laurent Guerin
 *  
 */
public class VariablesNames {
	
	private VariablesNames() {
	}

	//--- PACKAGES predefined variables names ( v 2.0.6 )		
	public static final String  ROOT_PKG    = "ROOT_PKG" ;
	public static final String  ENTITY_PKG  = "ENTITY_PKG" ;
	
	//--- FOLDERS predefined variables names ( v 2.0.3 )		
	public static final String  SRC      = "SRC" ;
	public static final String  RES      = "RES" ;
	public static final String  WEB      = "WEB" ;
	public static final String  TEST_SRC = "TEST_SRC" ;
	public static final String  TEST_RES = "TEST_RES" ;
	public static final String  DOC      = "DOC" ;
	public static final String  TMP      = "TMP" ;
	
}