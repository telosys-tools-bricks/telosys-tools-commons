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
package org.telosys.tools.commons.langtypes;

/**
 * Language type with its simple/basic type and full type if any <br>
 * The simple and full type have the same value if there's no notion of "package", "namespace", etc <br>
 * Examples : <br>
 * For Java language : "String" and "java.lang.String" or "short" and "short"
 * For c# language : "Int16" and "System.Int16"
 * 
 * @author Laurent Guerin
 *
 */
public class LanguageType {

	private final String simpleType ;

	private final String fullType ;

	/**
	 * Constructor
	 * @param simpleType
	 * @param fullType
	 */
	public LanguageType(String simpleType, String fullType) {
		super();
		this.simpleType = simpleType;
		this.fullType = fullType;
	}

	public String getSimpleType() {
		return simpleType;
	}

	public String getFullType() {
		return fullType;
	}

	@Override
	public String toString() {
		return "LanguageType : '" + simpleType + "', '"	+ fullType + "'";
	}
}
