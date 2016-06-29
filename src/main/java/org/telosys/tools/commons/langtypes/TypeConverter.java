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

import java.util.HashMap;

public abstract class TypeConverter {

	private final HashMap<String, LanguageType> primitiveTypes         = new HashMap<String, LanguageType>();
	private final HashMap<String, LanguageType> primitiveUnsignedTypes = new HashMap<String, LanguageType>();
	
	private final HashMap<String, LanguageType> objectTypes         = new HashMap<String, LanguageType>();
	private final HashMap<String, LanguageType> objectSqlTypes      = new HashMap<String, LanguageType>();
		
	/**
	 * Declares a regular primitive type
	 * @param neutralType
	 * @param languageType
	 */
	protected void declarePrimitiveType(String neutralType, LanguageType languageType) {
		primitiveTypes.put(neutralType, languageType);
	}
	
	/**
	 * Declares an unsigned primitive type
	 * @param neutralType
	 * @param languageType
	 */
	protected void declarePrimitiveUnsignedType(String neutralType, LanguageType languageType) {
		primitiveUnsignedTypes.put(neutralType, languageType);
	}
	
	/**
	 * Declares a regular object type
	 * @param neutralType
	 * @param languageType
	 */
	protected void declareObjectType(String neutralType, LanguageType languageType) {
		objectTypes.put(neutralType, languageType);
	}
	
	/**
	 * Declares a SQL object type
	 * @param neutralType
	 * @param languageType
	 */
	protected void declareObjectSqlType(String neutralType, LanguageType languageType) {
		objectSqlTypes.put(neutralType, languageType);
	}
	
	/**
	 * Returns a primitive type for the given neutral type (or null if none)
	 * @param neutralType
	 * @param isUnsignedTypeExpected
	 * @return
	 */
	protected LanguageType getPrimitiveType(String neutralType, boolean isUnsignedTypeExpected ) {
		if ( isUnsignedTypeExpected ) {
			// Try to get an unsigned primitive type
			LanguageType lt = primitiveUnsignedTypes.get(neutralType);
			if ( lt != null ) {
				// unsigned type FOUND
				return lt ;
			}
			else {
				// unsigned type NOT FOUND => try to get a regular primitive type
				return primitiveTypes.get(neutralType);
			}
		}
		else {
			// Try to get a regular primitive type
			return primitiveTypes.get(neutralType);
		}
	}
	
	/**
	 * Returns an object type for the given neutral type (or null if none)
	 * @param neutralType
	 * @param isSqlTypeExpected
	 * @return
	 */
	protected LanguageType getObjectType(String neutralType, boolean isSqlTypeExpected ) {
		if ( isSqlTypeExpected ) {
			// Try to get a SQL object type
			LanguageType lt = objectSqlTypes.get(neutralType);
			if ( lt != null ) {
				// SQL type FOUND
				return lt ;
			}
			else {
				// SQL type NOT FOUND => try to get a regular object type
				return objectTypes.get(neutralType);
			}
		}
		else {
			// Try to get a regular object type
			return objectTypes.get(neutralType);
		}
	}
	
	/**
	 * Returns the LanguageType that suits as well as possible with the given type information
	 * @param attributeTypeInfo
	 * @return 
	 */
	public final LanguageType getType(AttributeTypeInfo attributeTypeInfo) {
		
		//--- 1) Process explicit requirements first (if any)
		// A primitive type is explicitly required ( @PrimitiveType or @UnsignedType )
		if ( attributeTypeInfo.isPrimitiveTypeExpected() || attributeTypeInfo.isUnsignedTypeExpected() ) {
			LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), attributeTypeInfo.isUnsignedTypeExpected() ) ;
			if ( lt != null ) {
				// FOUND
				return lt ;
			}
		}
		
		// An object type is explicitly required ( @ObjectType or @SqlType )
		if ( attributeTypeInfo.isObjectTypeExpected() || attributeTypeInfo.isSqlTypeExpected() ) {
			LanguageType lt = getObjectType(attributeTypeInfo.getNeutralType(), attributeTypeInfo.isSqlTypeExpected() ) ;
			if ( lt != null ) {
				// FOUND
				return lt ;
			}
		}

		//--- 2) Process standard type conversion
		if ( attributeTypeInfo.isNotNull() ) {
			// Try to found a primitive type first
			LanguageType lt = getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ;
			if ( lt != null ) {
				// FOUND
				return lt ;
			}
			// Still not found : try to found an object type
			return getObjectType(attributeTypeInfo.getNeutralType(), false ) ;
		}
		else {
			// Try to found an object type first
			LanguageType lt = getObjectType(attributeTypeInfo.getNeutralType(), false ) ;
			if ( lt != null ) {
				// FOUND
				return lt ;
			}
			// Still not found : try to found a primitive type
			return getPrimitiveType(attributeTypeInfo.getNeutralType(), false ) ;
		}
	}
}
