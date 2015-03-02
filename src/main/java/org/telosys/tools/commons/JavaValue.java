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
package org.telosys.tools.commons;

import java.math.BigDecimal;

/**
 * @author Laurent GUERIN
 * 
 */
public final class JavaValue
{
    /** */
    private JavaValue()
    {
    }
    
    //-----------------------------------------------------------------------------------
    public static boolean isValidForBooleanType(String sValue)
    {
		if ( StrUtil.nullOrVoid(sValue) ) return true ;  // No value => OK
		
		if ( "true".equals(sValue) ) return true ;
		if ( "false".equals(sValue) ) return true ;
		return false ;
    }

    //-----------------------------------------------------------------------------------
    public static boolean isValidForNumberType(String sValue, String sJavaFullType)
    {
		if ( StrUtil.nullOrVoid(sValue) ) return true ;  // No value => OK
		
		String type = sJavaFullType ;

		if ( "byte".equals(type) || "java.lang.Byte".equals(type) ) {
			try {
				Byte.parseByte(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "short".equals(type) || "java.lang.Short".equals(type) ) {
			try {
				Short.parseShort(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "int".equals(type) || "java.lang.Integer".equals(type) ) {
			try {
				Integer.parseInt(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "long".equals(type) || "java.lang.Long".equals(type) ) {
			try {
				Long.parseLong(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "float".equals(type) || "java.lang.Float".equals(type) ) {
			try {
				Float.parseFloat(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "double".equals(type) || "java.lang.Double".equals(type) ) {
			try {
				Double.parseDouble(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "java.math.BigDecimal".equals(type) )
		{
			try {
				new BigDecimal(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else {
			// Unknown type 
			return false ;
		}
	}
    
    public static boolean isValidForDateType(String sValue, String sJavaFullType) 
	{
		if ( StrUtil.nullOrVoid(sValue) ) return true ;  // No value => OK
		
		String type = sJavaFullType ;
		
		if ( "java.util.Date".equals(type) ) {
			int length = sValue.length();
			if ( length == 10 ) { // the length is 10 "YYYY-MM-DD"
				try {
					DateUtil.parseDate(sValue);
					return true;
				} catch (Throwable e) {
					return false ;
				}
			}
			else if ( length == 8 ) {  // the length is 8 "HH:MM:SS"
				try {
					DateUtil.parseTime(sValue);
					return true;
				} catch (Throwable e) {
					return false ;
				}
			}
			else if ( length == 19 ) { // the length is 19 "YYYY-MM-DD HH:MM:SS"
				try {
					DateUtil.parseDateTime(sValue);
					return true;
				} catch (Throwable e) {
					return false ;
				}				
			}
			else {
				return false ;
			}
		}
		else if ( "java.sql.Date".equals(type) )
		{
			try {
				DateUtil.parseDate(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "java.sql.Time".equals(type) )
		{
			try {
				DateUtil.parseTime(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else if ( "java.sql.Timestamp".equals(type) )
		{
			try {
				DateUtil.parseDateTime(sValue);
				return true;
			} catch (Throwable e) {
				return false ;
			}
		}
		else {
			// Unknown type 
			return false ;
		}
	}

}