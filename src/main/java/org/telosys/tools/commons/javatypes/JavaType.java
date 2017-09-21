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
package org.telosys.tools.commons.javatypes;

import org.telosys.tools.commons.JavaClassUtil;

public class JavaType {
	private String _sText = null ; // the text to print ( anything : "Boolean", "Date (sql)", .. )
	private String _sType = null ; // "java.lang.Boolean", "boolean"
	private String _sShortType = null ; // "Boolean", "boolean"
	private String _sDefaultValue = null ; // "0", "0.0", "0.0f", "false", ...
	
	/**
	 * @param text
	 * @param type
	 */
	public JavaType(String text, String type, String defaultValue) {
		super();
		_sText = text;
		_sType = type;
		_sShortType = JavaClassUtil.shortName(type);
		_sDefaultValue = defaultValue ;
	}
	
	/**
	 * Returns the text to print in the "UI view" ( ie "Boolean", "Date (sql)", ... )
	 * @return
	 */
	public String getText()
	{
		return _sText ;
	}

	/**
	 * Returns the full Java type ( ie "java.lang.Boolean", "boolean", ... )
	 * @return
	 */
	public String getType()
	{
		return _sType ;
	}

	/**
	 * Returns the short Java type ( ie "Boolean", "boolean", ... )
	 * @return
	 */
	public String getShortType()
	{
		return _sShortType ;
	}

	/**
	 * Returns the default value for this type if defined ( else null )
	 * @return
	 */
	public String getDefaultValue()
	{
		return _sDefaultValue ;
	}
}
