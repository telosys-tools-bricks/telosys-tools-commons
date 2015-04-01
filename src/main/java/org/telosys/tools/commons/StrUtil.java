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

/**
 * @author Laurent GUERIN
 * 
 */
public final class StrUtil
{
    /** */
    private StrUtil()
    {
    }
    
    //-----------------------------------------------------------------------------------
    /**
     * Count the number of occurrence of the given chararcter in the given string
     * @param s
     * @param c
     * @return the number of char c in string s 
     */
    public static int countChar(String s, char c)
    {
        int count = 0 ;
        if ( s != null )
        {            
	        int i = s.indexOf(c);
	        while ( i >= 0 )
	        {
	            count++;
	            i = s.indexOf(c,i+1);	            
	        }
        }
        return count ;
    }

    //-----------------------------------------------------------------------------------
    /**
     * @param sVal : the string to convert 
     * @return boolean : true is the string is "1" or "true" ( after trim ), else ( other values or null ) false
     */
    public static boolean getBoolean(final String sVal)
    {
        if (sVal == null)
        {
            return false ;
        }
        else
        {
            String s = sVal.trim();
            return ( s.equals("1") || s.equals("true") ) ;            
        }
    }

    //-----------------------------------------------------------------------------------
    /**
     * @param sVal :
     * @param iMaxLength :
     * @return String :
     */
    public static String getLimitedString(final String sVal, final int iMaxLength)
    {
        if (sVal != null)
        {
            if (sVal.length() <= iMaxLength)
            {
                return sVal;
            }
            else
            {
                return sVal.substring(0, iMaxLength);
            }
        }
        else
        {
            return "";
        }
    }

    //-----------------------------------------------------------------------------------
    /**
     * @param sVal
     * @return string with protected characters
     */
    public static String getProtectedString(final String sVal)
    {
        if (sVal != null)
        {
        	StringBuffer sbNew = new StringBuffer(100);
        	char c = '\0' ;
        	for ( int i=0 ; i < sVal.length() ; i++ )
        	{
        		c = sVal.charAt(i) ;
        		if ( c == '"')
        		{
        			sbNew.append("&quot;"); //--- Protection par '\' 
        		}
        		else
        		{
        			sbNew.append(c);
        		}
        	}
            return sbNew.toString();
        }
        else
        {
            return "";
        }
    }

    //-----------------------------------------------------------------------------------
    /**
     * @param s :
     * @return String :
     */
    public static String notNull(final String s)
    {
        if (s != null)
        {
            return s;
        }
        else
        {
            return "";
        }
    }

    //-----------------------------------------------------------------------------------
    /**
     * Returns true if the given String is null or void ( "", " ", "  " )
     * @param s
     * @return
     */
    public static boolean nullOrVoid(final String s)
    {
        if (s == null)
        {
            return true ;
        }
        else
        {
            if ( s.trim().length() == 0 )
            {
                return true ;
            }
        }
        return false ;
    }

    /**
     * @param sVal : String value to convert 
     * @param iDefaultValue : Default value if the string is null or contains an invalid integer value
     * @return
     */
    public static int getInt(final String sVal, final int iDefaultValue )
    {
        if (sVal == null)
        {
            return iDefaultValue ;
        }

        try
        {
            return Integer.parseInt(sVal.trim());
        }
        catch (NumberFormatException ex)
        {
            return iDefaultValue;
        }
    }
    /**
     * 
     * @param sVal :
     * @return int :
     */
    public static int getInt(final String sVal)
    {
        return getInt(sVal,0);
    }

    /**
     * Returns an instance of Integer for the given string value <br>
     * @param sValue
     * @return the instance or null if the given value is null or invalid
     */
    public static Integer getIntegerObject(final String sValue)
    {
    	Integer i = null ;
    	if ( sValue != null ) {
        	try {
            	i = new Integer(sValue.trim());
        	}
            catch (NumberFormatException ex) {
            	i = null ;
            }
    	}
        return i; 
    }

    /**
     * @param sVal : String value to convert 
     * @param dDefaultValue : Default value if the string is null or contains an invalid value
     * @return
     */
    public static double getDouble(final String sVal, final double dDefaultValue )
    {
        if (sVal == null)
        {
            return dDefaultValue ;
        }

        try
        {
            return Double.parseDouble(sVal.trim());
        }
        catch (NumberFormatException ex)
        {
            return dDefaultValue;
        }
    }
    /**
     * 
     * @param sVal :
     * @return int :
     */
    public static double getDouble(final String sVal)
    {
        return getDouble(sVal, 0.0);
    }

    /**
     * Return a date instance from a date in French format 
     * @param sDate : date in French format ( "DD/MM/YYYY" ) 
     * @return java.sql.Date : the date instance ( or null is the input date is not valid )
     */
    public static java.sql.Date getDateFR(final String sDate)
    {
        if (sDate == null)
        {
            return null;
        }

        if (sDate.trim().equals(""))
        {
            return null;
        }

        if (sDate.length() != 10)
        {
            return null;
        }

        try
        {
            //--- Convert Date Format : "DD/MM/YYYY" -> "YYYY-MM-DD"
            String sYYYYMMDD = sDate.substring(6, 9 + 1) + "-" + sDate.substring(3, 4 + 1) + "-"
                    + sDate.substring(0, 1 + 1);
            //--- Convert ISO String date to SqlDate 
            return java.sql.Date.valueOf(sYYYYMMDD);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Return a date instance from a date in ISO format 
     * @param sDate : date in ISO format ( "YYYY-MM-DD" ) 
     * @return java.sql.Date : the date instance ( or null is the input date is not valid )
     */
    public static java.sql.Date getDateISO(final String sDate) // "YYYY-MM-DD"
    {
        if (sDate == null)
        {
            return null;
        }
    
        if (sDate.trim().equals(""))
        {
            return null;
        }
    
        if (sDate.length() != 10)
        {
            return null;
        }
    
        try
        {
            //--- Convert String date to SqlDate 
            return java.sql.Date.valueOf(sDate); 
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Removes the quote characters if they are located at the first and last position of the string
     * The string is returned "as is" if there's no quotes at the begining and at the end
     * @param s the string 
     * @param c the quote character to use ( eg '\'', '"' )
     * @return
     */
    public static String removeQuotes( String s, char c )
    {
        if ( s == null ) return null ;
        if ( s.length() > 1 )
        {
            int last = s.length()-1;
            if ( s.charAt(0) == c && s.charAt(last) == c )
            {
                return s.substring(1, last);
            }
        }
        return s;
    }

    /**
     * Split a string using the given char as separator ( simple split without "reg exp" )
     * @param s : the string to split
     * @param c : the separator
     * @return : array of 'tokens' ( never null, size = 0 if the string is null, else 1 to N )
     */
    public static String[] split(String s, char c)
    {
        if (s != null)
        {
            char chars[] = s.toCharArray();
            
            // Count separators
            int count = 0 ;
            for ( int n = 0 ; n < chars.length ; n++ )
            {
                if ( chars[n] == c ) count++ ;
            }
            
            if ( count > 0 )
            {
	            String[] sTokens = new String[count+1] ;
	            int iToken = 0;
	            int iOffset = 0 ;
	            int iLength = 0 ;
	            for ( int i = 0 ; i < chars.length ; i++ )
	            {
	                if ( chars[i] == c )
	                {
	                    //--- Create new token 
	                    sTokens[iToken] = new String(chars, iOffset, iLength );
	                    iToken++;
	                    //--- Reset 
	                    iOffset = i + 1 ;
	                    iLength = 0 ;
	                }
	                else
	                {
	                    iLength++;
	                }
	            }
	            //--- Last Token ( current token ) 
                sTokens[iToken] = new String(chars, iOffset, iLength );
	            return sTokens ;
            }
            else
            {
                //--- No separator
                String[] ret = new String[1];
                ret[0] = s ;
                return ret ;
            }
        }
        return new String[0]; 
    }
    
    /**
     * Returns TRUE if the first character of the string is an alphabetic character <br>
     * ( from 'a' to 'z' and from 'A' to 'Z' ) 
     * @param s
     * @return
     */
    public static boolean isFirstCharAlpha(String s)
    {
        if ( s != null )
        {
            if ( s.length() > 0 )
            {
                char c = s.charAt(0);
                if ( c >= 'A' && c <= 'Z' ) return true ;
                if ( c >= 'a' && c <= 'z' ) return true ;
            }
        }
        return false ;
    }

    /**
     * Replace the given variable name by the given value
     * @param s the initial string ( e.g. "bla bla bla ${MyVar} bla bla" )
     * @param sVarName the variable name ( e.g. "${MyVar}" )
     * @param sVarValue the variable value
     * @return the string after variable replacement
     */
    public static String replaceVar( String s, String sVarName, String sVarValue )
    {
        String sNewString = s ;
        int i = s.indexOf(sVarName);
        if ( i >= 0 ) 
        {
        	int j = i + sVarName.length() ;
            String s1 = s.substring(0,i);
            String s2 = s.substring(j,s.length());
            //--- Replace the var name by the var value
            sNewString = s1 + sVarValue + s2 ;
        	
        }
        return sNewString ;
    }

	/**
	 * Remove all the blank characters in the given string <br>
	 * eg : returns "abc" for " a b  c "
	 * @param s
	 * @return
	 */
	public final static String removeAllBlanks(String s)
	{
		if ( s == null ) return null ;
		StringBuffer sb = new StringBuffer(s.length());
		int n = s.length() ;
		char c ;
		for ( int i = 0 ; i < n ; i++ )
		{
			c = s.charAt(i);
			if ( c != ' ' )
			{
				sb.append(c);
			}
		}
		if ( sb.length() == s.length() )
		{
			return s ;
		}
		else
		{
			return sb.toString() ;
		}
	}
	
	//-------------------------------------------------------------------------------
	/**
	 * Returns true if s1 equals s2 or if s1 and s2 are both null
	 * @param s1
	 * @param s2
	 * @return
	 */
	public final static boolean identical(String s1, String s2) 
	{
		if ( null == s1 )
		{
			return ( null == s2 ? true : false );
		}
		else
		{
			if ( null == s2 ) return false ;
		}
		return s1.equals(s2);
	}

	//-------------------------------------------------------------------------------
	public final static String toCamelCase(String start) {
		if (nullOrVoid(start)) {
			return "";
		} else {
			final StringBuffer sb = new StringBuffer();
		    for (String s : start.toLowerCase().split("_")) {
		        if (sb.length() > 1) {
			        sb.append(Character.toUpperCase(s.charAt(0)));
		        } else {
			        sb.append(s.charAt(0));
		        }
		        sb.append(s.substring(1, s.length()).toLowerCase());
		    }
			return sb.toString();
		}
	}	
	
	//-------------------------------------------------------------------------------
    /**
     * Returns the given string starting by an Upper Case <br>
     * @param s
     * @return
     * @since 2.0.7
     */
    public static String firstCharUC(String s)
    {
    	if ( s != null )
    	{
    		if ( s.length() > 1 )
    		{
                return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    		}
    		else if ( s.length() == 1 )
    		{
    			return s.substring(0, 1).toUpperCase() ;
    		}
    	}
   		return "" ;
    }
	
	//-------------------------------------------------------------------------------
	/**
	 * Returns true if the 2 strings are different
	 * @param s1
	 * @param s2
	 * @return
	 */
	public final static boolean different(String s1, String s2 ) {
		if ( s1 != null ) {
			if ( null == s2 ) {
				return true ; // different
			} 
			else {
				return ! s1.equals(s2) ;
			}
		}
		else {
			return s2 != null ;
		}
	}
}