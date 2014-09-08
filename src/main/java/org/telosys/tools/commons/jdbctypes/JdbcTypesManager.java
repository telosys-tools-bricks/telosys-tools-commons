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
package org.telosys.tools.commons.jdbctypes;

import java.sql.Types;



/**
 * All the standard JDBC type (defined in java.sql.Types)
 * with their corresponding constant name, 
 * and the recommended Java type to use.
 * 
 * @author Laurent GUERIN
 *
 */
public class JdbcTypesManager {

	private static JdbcTypes $types = null ;
	
	public static JdbcTypes getJdbcTypes()
	{
		if ( $types == null )
		{
			$types = init();
		}
		return $types ;
	}
	
	private static JdbcTypes init()
	{
		JdbcTypes types = new JdbcTypes();
		
		//         JDBC TYPE CODE       JDBC TYPE        JAVA TYPE             JAVA TYPE
		//                                               if NULLABLE           if NOT NULLABLE  ( same if not set )    
		types.add( Types.CHAR,          "CHAR",          "java.lang.String"); // 1
		types.add( Types.VARCHAR,       "VARCHAR",       "java.lang.String"); // 12
		types.add( Types.LONGVARCHAR,   "LONGVARCHAR",   "java.lang.String"); // -1
		
		types.add( Types.NUMERIC,       "NUMERIC",       "java.math.BigDecimal"); // 2
		types.add( Types.DECIMAL,       "DECIMAL",       "java.math.BigDecimal"); // 3
		
//		types.add( Types.BIT,           "BIT",           "java.lang.Boolean",  "boolean"); // -7
//		types.add( Types.BOOLEAN,       "BOOLEAN",       "java.lang.Boolean",  "boolean"); // 16
//		
//		types.add( Types.TINYINT,       "TINYINT",       "java.lang.Byte",     "byte");  // -6
//		types.add( Types.SMALLINT,      "SMALLINT",      "java.lang.Short",    "short"); // 5
//		types.add( Types.INTEGER,       "INTEGER",       "java.lang.Integer",  "int");   // 4
//		types.add( Types.BIGINT,        "BIGINT",        "java.lang.Long",     "long");  // -5
//		
//		types.add( Types.REAL,          "REAL",          "java.lang.Float",    "float");   // 7
//		types.add( Types.FLOAT,         "FLOAT",         "java.lang.Double",   "double");  // 6
//		types.add( Types.DOUBLE,        "DOUBLE",        "java.lang.Double",   "double");  // 8

//--- Ver 2.1.0 (always use Wrappers )
		types.add( Types.BIT,           "BIT",           "java.lang.Boolean" ); // -7
		types.add( Types.BOOLEAN,       "BOOLEAN",       "java.lang.Boolean" ); // 16
		
		types.add( Types.TINYINT,       "TINYINT",       "java.lang.Byte"    );  // -6
		types.add( Types.SMALLINT,      "SMALLINT",      "java.lang.Short"   ); // 5
		types.add( Types.INTEGER,       "INTEGER",       "java.lang.Integer" );   // 4
		types.add( Types.BIGINT,        "BIGINT",        "java.lang.Long"    );  // -5
		
		types.add( Types.REAL,          "REAL",          "java.lang.Float"   );   // 7
		types.add( Types.FLOAT,         "FLOAT",         "java.lang.Double"  );  // 6
		types.add( Types.DOUBLE,        "DOUBLE",        "java.lang.Double"  );  // 8
	
		types.add( Types.BINARY,        "BINARY",        "byte[]");  // -2
		types.add( Types.VARBINARY,     "VARBINARY",     "byte[]");  // -3
		types.add( Types.LONGVARBINARY, "LONGVARBINARY", "byte[]");  // -4
//--- Ver 2.1.0 (always use Wrappers )

		types.add( Types.DATE,          "DATE",          "java.sql.Date");       // forced to "java.util.Date" by rule
		types.add( Types.TIME,          "TIME",          "java.sql.Time");       // forced to "java.util.Date" by rule
		types.add( Types.TIMESTAMP,     "TIMESTAMP",     "java.sql.Timestamp");  // forced to "java.util.Date" by rule
		
		types.add( Types.CLOB,          "CLOB",          "java.sql.Clob");   // 2005
		types.add( Types.BLOB,          "BLOB",          "java.sql.Blob");   // 2004

		//--- Not supported types :
		// Types.ARRAY
		// Types.DISTINCT
		// Types.STRUCT
		// Types.REF
		// Types.DATALINK
		
		return types ;
	}
}
