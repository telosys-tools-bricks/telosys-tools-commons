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
package org.telosys.tools.commons;

import java.sql.Types;

/**
 * Utility class for DATABASE operations ( set of static methods )
 * 
 * @author Laurent GUERIN
 * @since 2.0.7
 */
public final class DatabaseUtil
{
    /** */
    private DatabaseUtil()
    {
    }
    
	//-------------------------------------------------------------------------------------
    /**
     * Returns the database native type name with its size if the size make sense.<br>
     * Examples : INTEGER, VARCHAR(24), NUMBER(8,2), CHAR(3), etc... 
     * @param databaseTypeName
     * @param databaseTypeSize
     * @param jdbcTypeCode
     * @return
     */
    public static String getNativeTypeWithSize(String databaseTypeName, String databaseTypeSize, int jdbcTypeCode)
    {
        if ( jdbcTypeCode == Types.VARCHAR || jdbcTypeCode == Types.CHAR ) {
        	return databaseTypeName + "(" + databaseTypeSize + ")" ;
        }
        else if ( jdbcTypeCode == Types.DECIMAL || jdbcTypeCode == Types.NUMERIC ) {
        	return databaseTypeName + "(" + databaseTypeSize + ")" ;
        }
        else {
        	return databaseTypeName ;
        }
    }
    
}