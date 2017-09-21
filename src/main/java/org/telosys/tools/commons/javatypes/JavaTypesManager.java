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



public class JavaTypesManager {

	private static JavaTypes $types = null ;
	
	/**
	 * Returns the single JavaTypes instance
	 * @return
	 */
	public static JavaTypes getJavaTypes()
	{
		if ( $types == null )
		{
			$types = init();
		}
		return $types ;
	}
	
	private static JavaTypes init()
	{
		JavaTypes types = new JavaTypes();
		
		//--- STRING CATEGORY
		types.add("String",  "java.lang.String",   null); // Can be a Long Text (subtag in XML)
		// "StringBuffer", // not supported in SQL DAO   &  XML mapper
		
		//--- BOOLEAN CATEGORY
		types.add("boolean",  "boolean",            "false");
		types.add("Boolean",  "java.lang.Boolean",  null);

		
		//--- NUMBER CATEGORY : INT
		types.add("byte",    "byte",              "0" );
		types.add("Byte",    "java.lang.Byte",    null );
		
		// "char",      // not supported in SQL DAO  &  XML mapper
		// "Character", // not supported in SQL DAO  &  XML mapper
		
		types.add("short",   "short",             "0" );
		types.add("Short",   "java.lang.Short",   null);
		
		types.add("int",     "int",               "0" );
		types.add("Integer", "java.lang.Integer", null);
		
		types.add("long",    "long",              "0" );
		types.add("Long",    "java.lang.Long",    null);

		// "BigInteger", // not supported in SQL DAO  &  XML mapper
		
		//--- NUMBER CATEGORY : DECIMAL		
		types.add("float",      "float",                "0" );
		types.add("Float",      "java.lang.Float",      null);
		
		types.add("double",     "double",               "0" );
		types.add("Double",     "java.lang.Double",     null);
		
		types.add("BigDecimal", "java.math.BigDecimal", null);  

		//--- NUMBER CATEGORY : DATE OR TIME		
		types.add("Date",            "java.util.Date",     null);  // Can be DATE_ONLY, TIME_ONLY, DATE_AND_TIME
		// TODO : java.util.Calendar ???
		types.add("Date (sql)",      "java.sql.Date",      null ); // XML mapper : util.Date getDateISO()  "YYYY-MM-DD" attributeString(util.Date, DATE_ONLY ) 
		types.add("Time (sql)",      "java.sql.Time",      null ); // XML mapper : util.Date getTimeISO()  "HH:mm:ss"   attributeString(util.Date, TIME_ONLY )
		types.add("Timestamp (sql)", "java.sql.Timestamp", null ); // XML mapper : util.Date getDateTimeISO() "YYYY-MM-DD HH:MM:SS"  attributeString(util.Date, DATE_AND_TIME )
		
		
		//--- BINARY / CLOB / BLOB CATEGORY 
		types.add("byte[]", "byte[]", null ); // not supported in XML mapper
		
		// java.sql classes : not supported in XML mapper (only supported in DAO)
		types.add("Blob (sql)",      "java.sql.Blob", null );   // not supported in XML mapper
		types.add("Clob (sql)",      "java.sql.Clob", null );  // Must be a Long Text (subtag in XML)
		
		return types ;
	}
}
