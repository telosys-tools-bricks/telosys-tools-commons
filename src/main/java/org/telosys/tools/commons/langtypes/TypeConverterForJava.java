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

public class TypeConverterForJava extends TypeConverter {

	public TypeConverterForJava() {
		super();
		
		//--- Object types 
		declareObjectType(NeutralType.STRING,    buildJavaType(java.lang.String.class) );
		declareObjectType(NeutralType.BOOLEAN,   buildJavaType(java.lang.Boolean.class) );
		declareObjectType(NeutralType.BYTE,      buildJavaType(java.lang.Byte.class) );
		declareObjectType(NeutralType.SHORT,     buildJavaType(java.lang.Short.class) );
		declareObjectType(NeutralType.INTEGER,   buildJavaType(java.lang.Integer.class) );
		declareObjectType(NeutralType.LONG,      buildJavaType(java.lang.Long.class) );
		declareObjectType(NeutralType.FLOAT,     buildJavaType(java.lang.Float.class) );
		declareObjectType(NeutralType.DOUBLE,    buildJavaType(java.lang.Double.class) );
		declareObjectType(NeutralType.DECIMAL,   buildJavaType(java.math.BigDecimal.class) );
		declareObjectType(NeutralType.DATE,      buildJavaType(java.util.Date.class) );
		declareObjectType(NeutralType.TIME,      buildJavaType(java.util.Date.class) );
		declareObjectType(NeutralType.TIMESTAMP, buildJavaType(java.util.Date.class) );
		declareObjectType(NeutralType.LONGTEXT,  buildJavaType(java.lang.String.class) );
		declareObjectType(NeutralType.BINARY,    buildJavaType(java.nio.ByteBuffer.class) );

		//--- Object SQL types 
		declareObjectSqlType(NeutralType.DATE,      buildJavaType(java.sql.Date.class) );
		declareObjectSqlType(NeutralType.TIME,      buildJavaType(java.sql.Time.class) );
		declareObjectSqlType(NeutralType.TIMESTAMP, buildJavaType(java.sql.Timestamp.class) );
		declareObjectSqlType(NeutralType.LONGTEXT,  buildJavaType(java.sql.Clob.class) );
		declareObjectSqlType(NeutralType.BINARY,    buildJavaType(java.sql.Blob.class) );
		
		//--- Primitive types 
		// STRING => No primitive type
		declarePrimitiveType(NeutralType.BOOLEAN,  buildJavaType(boolean.class) );
		declarePrimitiveType(NeutralType.BYTE,     buildJavaType(byte.class) );
		declarePrimitiveType(NeutralType.SHORT,    buildJavaType(short.class) );
		declarePrimitiveType(NeutralType.INTEGER,  buildJavaType(int.class) );
		declarePrimitiveType(NeutralType.LONG,     buildJavaType(long.class) );
		declarePrimitiveType(NeutralType.FLOAT,    buildJavaType(float.class) );
		declarePrimitiveType(NeutralType.DOUBLE,   buildJavaType(double.class) );
		// DECIMAL => No primitive type
		// DATE => No primitive type
		// TIME => No primitive type
		// TIMESTAMP => No primitive type
		// LONGTEXT => No primitive type
		declarePrimitiveType(NeutralType.BINARY,   buildJavaType(byte[].class) );
		
		//--- Unsigned primitive types : No unsigned primitive types in Java
	}

	private LanguageType buildJavaType(Class<?> clazz) {
		if ( clazz.isPrimitive() ) {
			return new LanguageType(clazz.getSimpleName(), clazz.getSimpleName() );
		}
		else {
			return new LanguageType(clazz.getSimpleName(), clazz.getCanonicalName() );
		}
	}	
}
