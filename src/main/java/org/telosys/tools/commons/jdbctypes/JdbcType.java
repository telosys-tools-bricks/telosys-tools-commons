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
package org.telosys.tools.commons.jdbctypes;


public class JdbcType 
{
	private final int     _iCode  ; // the JDBC code 
	
	private final String  _sText  ; // 
	
	private final String _sJavaTypeForNull    ; // "Boolean", "boolean"

	private final String _sJavaTypeForNotNull ; // "Boolean", "boolean"
	
	public JdbcType(int code, String text, String javaType) 
	{
		super();
		_iCode = code;
		_sText = text;
		_sJavaTypeForNull    = javaType;
		_sJavaTypeForNotNull = javaType;
	}
	
	public JdbcType(int code, String text, String typeForNull, String typeForNotNull) 
	{
		super();
		_iCode = code;
		_sText = text;
		_sJavaTypeForNull    = typeForNull ;
		_sJavaTypeForNotNull = typeForNotNull ;
	}
	
	public int getCode()
	{
		return _iCode ;
	}

	public String getText()
	{
		return _sText ;
	}

	public String getJavaTypeForNull()
	{
		return _sJavaTypeForNull ;
	}

	public String getJavaTypeForNotNull()
	{
		return _sJavaTypeForNotNull ;
	}
}
