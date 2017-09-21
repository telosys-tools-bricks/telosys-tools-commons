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

import java.util.LinkedList;


/**
 * List of JdbcType instances <br>
 * List of associations "JDBC code" - "code text" + "recommended Java type" 
 * @author L. Guerin
 *
 */
public class JdbcTypes {
	
	private final LinkedList<JdbcType> _list = new LinkedList<JdbcType>() ;
	
	protected JdbcTypes() {
		super();
	}

	/**
	 * Adds a new JDBC type in the list
	 * @param code the JDBC code 
	 * @param text the JDBC text ( Java constant name from 'java.sql.Types' : "NUMERIC", "TINYINT", "VARCHAR", ... )
	 * @param type the recommended Java type associated with the JDBC code 
	 */
	protected void add(int code, String text, String type)
	{
		_list.add( new JdbcType(code, text, type) );
	}
	
	/**
	 * Adds a new JDBC type in the list with different types for "NULL" and "NOT NULL"
	 * @param code
	 * @param text
	 * @param typeForNull the recommended Java type associated with the JDBC code if "NULL"
	 * @param typeForNotNull the recommended Java type associated with the JDBC code if "NOT NULL"
	 */
	protected void add(int code, String text, String typeForNull, String typeForNotNull)
	{
		_list.add( new JdbcType(code, text, typeForNull, typeForNotNull) );
	}
	
	/**
	 * Returns the Java constant name ( from 'java.sql.Types' ) for the given JDBC code 
	 * @param code
	 * @return the text or null if unknown code
	 */
	public String getTextForCode( int code )
	{
		for ( JdbcType t : _list ) {
			if ( t.getCode() == code )
			{
				return t.getText() ;
			}
		}
		return null ;
	}
	
	/**
	 * Returns the recommended Java type associated with the given JDBC code 
	 * @param iJdbcTypeCode
	 * @return the Java type ( primitive type or fully qualified class name )<br>
	 * e.g. : "int", "java.lang.String", "byte[]", "java.sql.Time"
	 */
	public String getJavaTypeForCode( int iJdbcTypeCode, boolean bNotNull )
	{
		for ( JdbcType t : _list ) {
			if ( t.getCode() == iJdbcTypeCode )
			{
				if ( bNotNull ) {
					return t.getJavaTypeForNotNull() ;
				}
				else {
					return t.getJavaTypeForNull() ;
				}
			}
		}
		return null ;
	}
	
	/**
	 * Returns the list size
	 * @return
	 */
	public int size()
	{
		return _list.size();
	}
}
