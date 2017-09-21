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

/**
 * @author Laurent GUERIN
 * 
 */
public final class JavaTypeUtil
{
    /** */
    private JavaTypeUtil()
    {
    }
    
    //-----------------------------------------------------------------------------------
    /**
     * Returns true if the given full class name needs an import
     * @param s
     * @return
     */
    public static boolean needsImport(String s)
    {
    	if ( s == null ) return false ;
    	
    	String s2 = s.trim() ;
    	
    	//--- Contains package ?
    	if ( s2.indexOf('.') < 0 ) 
    	{
    		return false ; // no '.' in the string = no package
    	}
    	
    	//--- In "java.lang" package ?
    	if ( s2.startsWith("java.lang.") )
    	{
    		return false ; // no import for "java.lang" classes
    	}
    	
    	return true ;
    }

	//---------------------------------------------------------------------------------------
    /**
     * Returns true if the given type is a Java primitive type ( int, float, boolean, ... )
     * @param sType
     * @return
     */
    public static boolean isPrimitiveType(String sType)
	{
		if ( "boolean".equals(sType)) return true ;
		if ( "byte".equals(sType))    return true ;
		if ( "char".equals(sType))    return true ;
		if ( "double".equals(sType))  return true ;
		if ( "float".equals(sType))   return true ;
		if ( "int".equals(sType))     return true ;
		if ( "long".equals(sType))    return true ;
		if ( "short".equals(sType))   return true ;
		return false ;
	}

	//----------------------------------------------------------------------------------
	/**
	 * Returns the short type of the given type <br>
	 * e.g. : "java.lang.Boolean" returns "Boolean" ("Boolean" returns "Boolean" )
	 * @param sType : input type long or short ( "int", "String", "java.lang.String" ) 
	 * @return
	 */
	public static String shortType(String sType) {
		if ( sType != null )
		{
			String s = sType.trim();
			int i = s.lastIndexOf('.');
			if ( i >= 0 )
			{
				return s.substring(i+1);
			}
			return s ;
		}
		return null ;
	}

	//----------------------------------------------------------------------------------
	/**
	 * Returns the default "full type" for the given type
	 * @param sType
	 * @return
	 */
	public static String fullType(String sType) 
	{
		if ( sType != null )
		{
			String s = sType.trim();
			
			//--- Already has a point => is a full type
			int i = s.lastIndexOf('.');
			if ( i >= 0 )
			{
				return s ;
			}

			//--- Primitive type ( int, boolean, ... )
			if ( isPrimitiveType(s) )
			{
				return s ; // return as is
			}
			
			//--- Is it a well knowned java type ?
			if ( "Boolean".equals(s) )   return "java.lang.Boolean" ;
			if ( "Character".equals(s) ) return "java.lang.Character" ;			
			if ( "Byte".equals(s) )      return "java.lang.Byte" ;
			if ( "Short".equals(s) )     return "java.lang.Short" ;
			if ( "Integer".equals(s) )   return "java.lang.Integer" ;
			if ( "Long".equals(s) )      return "java.lang.Long" ;
			if ( "Float".equals(s) )     return "java.lang.Float" ;
			if ( "Double".equals(s) )    return "java.lang.Double" ;

			if ( "String".equals(s) )    return "java.lang.String" ;
			
			if ( "Date".equals(s) )      return "java.util.Date" ;

			if ( "BigInteger".equals(s) ) return "java.math.BigInteger" ;
			if ( "BigDecimal".equals(s) ) return "java.math.BigDecimal" ;
			
			if ( "Time".equals(s) )      return "java.sql.Time" ;
			if ( "Timestamp".equals(s) ) return "java.sql.Timestamp" ;
			if ( "Clob".equals(s) )      return "java.sql.Clob" ;
			if ( "Blob".equals(s) )      return "java.sql.Blob" ;
			
		}
		return null ;
	}
	
	public static boolean isCategoryBoolean(String sJavaType)
	{
		if ( null == sJavaType ) return false ;
		if ( "boolean".equals(sJavaType) ) return true ;
		if ( "java.lang.Boolean".equals(sJavaType) ) return true ;
		return false ;
	}

	public static boolean isCategoryDateOrTime(String sJavaType)
	{
		if ( null == sJavaType ) return false ;
		if ( "java.util.Date".equals(sJavaType) ) return true ;
		if ( "java.sql.Date".equals(sJavaType) ) return true ;
		if ( "java.sql.Time".equals(sJavaType) ) return true ;
		if ( "java.sql.Timestamp".equals(sJavaType)  ) return true ;
		return false ;
	}

	public static boolean isCategoryNumber(String sJavaType)
	{
		if ( null == sJavaType ) return false ;
		if ( "byte".equals(sJavaType) ) return true ;
		if ( "java.lang.Byte".equals(sJavaType) ) return true ;

		if ( "double".equals(sJavaType) ) return true ;
		if ( "java.lang.Double".equals(sJavaType) ) return true ;

		if ( "float".equals(sJavaType) ) return true ;
		if ( "java.lang.Float".equals(sJavaType) ) return true ;

		if ( "int".equals(sJavaType) ) return true ;
		if ( "java.lang.Integer".equals(sJavaType) ) return true ;

		if ( "long".equals(sJavaType) ) return true ;
		if ( "java.lang.Long".equals(sJavaType) ) return true ;

		if ( "short".equals(sJavaType) ) return true ;
		if ( "java.lang.Short".equals(sJavaType) ) return true ;

		if ( "java.math.BigDecimal".equals(sJavaType) ) return true ;
		
		if ( "java.math.BigInteger".equals(sJavaType) ) return true ; // Not supported but keep it here
		return false ;
	}

	public static boolean isCategoryString(String sJavaType)
	{
		if ( null == sJavaType ) return false ;
		if ( "java.lang.String".equals(sJavaType) ) return true ;
		return false ;
	}

	public final static int CATEGORY_UNKNOWN      = 0 ;
	public final static int CATEGORY_BOOLEAN      = 1 ;
	public final static int CATEGORY_DATE_OR_TIME = 2 ;
	public final static int CATEGORY_NUMBER       = 3 ;
	public final static int CATEGORY_STRING       = 4 ;
	
	public static int getCategory(String sJavaType)
	{
		if ( null == sJavaType ) return CATEGORY_UNKNOWN ;
		if ( isCategoryBoolean(sJavaType) )    return CATEGORY_BOOLEAN ;
		if ( isCategoryDateOrTime(sJavaType) ) return CATEGORY_DATE_OR_TIME ;
		if ( isCategoryNumber(sJavaType) )     return CATEGORY_NUMBER ;
		if ( isCategoryString(sJavaType) )     return CATEGORY_STRING ;
		return CATEGORY_UNKNOWN ;
	}
}