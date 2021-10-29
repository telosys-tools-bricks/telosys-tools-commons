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

import java.math.BigDecimal;

/**
 * @author Laurent GUERIN
 * 
 */
public final class StrUtil
{
    /**
     * No public constructor (set of static methods)
     */
    private StrUtil() {
    }
    
	/**
	 * Protect each occurrence of the given char with a backslash in the given string
	 * @param s the string to be processed
	 * @param c the character to be protected with a backslash
	 * @return
	 */
	public static final String backslash (String s, char c ) {
		StringBuilder sb = new StringBuilder();		
		for ( int i = 0 ; i < s.length() ; i++ ) {
			char charInString = s.charAt(i) ;
			if ( charInString == c ) {
				sb.append('\\');
			}
			sb.append(charInString);
		}
		return sb.toString();
	}

	/**
	 * Capitalizes the given string (changes the first letter to upper case)
	 * 
	 * @param str the string to be capitalized
	 * @return
	 */
	public static final String capitalize(String str) {
		if(str == null || str.length() == 0) {
			return str;
		}
		if(str.length() == 1) {
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	//-----------------------------------------------------------------------------------
    /**
     * Count the number of occurrence of the given chararcter in the given string
     * @param s
     * @param c
     * @return the number of char c in string s 
     */
    public static final int countChar(String s, char c) {
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
    
	/**
	 * Returns true if the 2 strings are different
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static final boolean different(String s1, String s2 ) {
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
	
    /**
     * Returns the given string starting by an Upper Case <br>
     * @param s
     * @return
     * @since 2.0.7
     */
    public static final String firstCharUC(String s) {
    	if ( s != null ) {
    		if ( s.length() > 1 ) {
                return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    		}
    		else if ( s.length() == 1 ) {
    			return s.substring(0, 1).toUpperCase() ;
    		}
    	}
   		return "" ;
    }
	
    /**
     * Returns an instance of BigDecimal for the given string value <br>
     * @param sValue
     * @return the instance or null if the given value is null or invalid
     * @since v 3.0.0
     */
    public static final BigDecimal getBigDecimalObject(final String sValue) {
    	BigDecimal r = null ;
    	if ( sValue != null ) {
        	try {
            	r = new BigDecimal(sValue.trim()); 
        	}
            catch (NumberFormatException ex) {
            	r = null ;
            }
    	}
        return r; 
    }

    /**
     * @param sVal : the string to convert 
     * @return boolean : true is the string is "1" or "true" ( after trim ), else ( other values or null ) false
     */
    public static final boolean getBoolean(final String sVal) {
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

    /**
     * Return a date instance from a date in French format 
     * @param sDate : date in French format ( "DD/MM/YYYY" ) 
     * @return java.sql.Date : the date instance ( or null is the input date is not valid )
     */
    public static final java.sql.Date getDateFR(final String sDate) {
    	if (sDate == null) {
    		return null;
    	}

    	if (sDate.trim().equals("")) {
    		return null;
    	}

    	if (sDate.length() != 10) {
    		return null;
    	}

    	try {
    		//--- Convert Date Format : "DD/MM/YYYY" -> "YYYY-MM-DD"
    		String sYYYYMMDD = sDate.substring(6, 9 + 1) + "-" + sDate.substring(3, 4 + 1) + "-"
    				+ sDate.substring(0, 1 + 1);
    		//--- Convert ISO String date to SqlDate 
    		return java.sql.Date.valueOf(sYYYYMMDD);
    	}
    	catch (Exception ex) {
    		return null;
    	}
    }
    
    /**
     * Return a date instance from the given date in ISO format 
     * @param dateISO : date in ISO format ( "YYYY-MM-DD" ) 
     * @return java.sql.Date : the date instance ( or null is the input date is not valid )
     */
    public static final java.sql.Date getDateISO(final String dateISO) {
        if (dateISO == null) {
            return null;
        }
    
        if (dateISO.trim().equals("")) {
            return null;
        }
    
        if (dateISO.length() != 10) {
            return null;
        }
    
        try {
            //--- Convert String date to SqlDate 
            return java.sql.Date.valueOf(dateISO); 
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Converts the given string to double 
     * @param s
     * @return the double value (0.0 if the given string is null or invalid)
     */
    public static final double getDouble(final String s) {
        return getDouble(s, 0.0);
    }
    
    /**
     * Converts the given string to double 
     * @param s
     * @param defaultValue the default value to be returned if the given string is null or invalid
     * @return the double value (the default value if the given string is null or invalid)
     */
    public static final double getDouble(final String s, final double defaultValue ) {
        if (s == null) {
            return defaultValue ;
        }
        try{
            return Double.parseDouble(s.trim());
        }
        catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Converts the given string to int 
     * @param s the string to be converted
     * @return
     */
    public static final int getInt(final String s) {
        return getInt(s,0);
    }
    
    /**
     * Converts the given string to int 
     * @param s the string to be converted
     * @param defaultValue the default value to be returned if the given string is null or invalid
     * @return
     */
    public static final int getInt(final String s, final int defaultValue ) {
        if (s == null) {
            return defaultValue ;
        }
        try {
            return Integer.parseInt(s.trim());
        }
        catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Returns an instance of Integer for the given string value <br>
     * @param sValue
     * @return the instance or null if the given value is null or invalid
     */
    public static final Integer getIntegerObject(final String s) {
    	Integer i = null ;
    	if ( s != null ) {
        	try {
            	i = Integer.valueOf(s.trim()); // v 3.0.0 (Sonar Issue Fixed)
        	}
            catch (NumberFormatException ex) {
            	i = null ;
            }
    	}
        return i; 
    }

    /**
     * Returns a string limited to the given maximum length (cut the string if too long)
     * @param s 
     * @param maxLength 
     * @return 
     */
    public static final String getLimitedString(final String s, final int maxLength) {
        if (s != null) {
            if (s.length() <= maxLength) {
                return s;
            }
            else {
                return s.substring(0, maxLength);
            }
        }
        else {
            return "";
        }
    }

    /**
     * Replaces all the quote characters by '&quot;'
     * @param s
     * @return string with protected characters
     */
    public static final String getProtectedString(final String s) {
        if (s != null) {
        	StringBuilder sb = new StringBuilder(100);
        	for ( int i=0 ; i < s.length() ; i++ ) {
        		char c = s.charAt(i) ;
        		if ( c == '"') {
        			sb.append("&quot;");
        		}
        		else {
        			sb.append(c);
        		}
        	}
            return sb.toString();
        }
        else {
            return "";
        }
    }
    
	/**
	 * Returns true if s1 equals s2 or if s1 and s2 are both null
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static final boolean identical(String s1, String s2) {
		if ( null == s1 ) {
			return ( null == s2 ? true : false );
		}
		else {
			if ( null == s2 ) return false ;
		}
		return s1.equals(s2);
	}

    /**
     * Returns TRUE if the first character of the string is an alphabetic character <br>
     * ( from 'a' to 'z' and from 'A' to 'Z' ) 
     * @param s
     * @return
     */
    public static final boolean isFirstCharAlpha(String s) {
        if ( s != null ) {
            if ( s.length() > 0 ) {
                char c = s.charAt(0);
                if ( c >= 'A' && c <= 'Z' ) return true ;
                if ( c >= 'a' && c <= 'z' ) return true ;
            }
        }
        return false ;
    }
    
    /**
     * Returns true if the given string contains only uppercase letters or digits <br>
     * (return false if the string null or void)
     * @param s
     * @return
     */
    public static final boolean isUpperCase(String s) {
    	if ( s == null ) return false ;
    	if ( s.isEmpty() ) return false ;
    	for (char c : s.toCharArray()) {
    		if ( ! ( Character.isUpperCase(c) || Character.isDigit(c) ) ) {
    			return false ;
    		}
    	}
		return true ;
    }    

    /**
     * Returns true if the given string contains only lowercase letters or digits <br>
     * (return false if the string null or void)
     * @param s
     * @return
     */
    public static final boolean isLowerCase(String s) {
    	if ( s == null ) return false ;
    	if ( s.isEmpty() ) return false ;
    	for (char c : s.toCharArray()) {
    		if ( ! ( Character.isLowerCase(c) || Character.isDigit(c) ) ) {
    			return false ;
    		}
    	}
		return true ;
    }    

    /**
     * Returns a void string if the given string is null, else returs the given string
     * @param s :
     * @return String :
     */
    public static final String notNull(final String s) {
        if (s != null) {
            return s;
        }
        else {
            return "";
        }
    }

    //-----------------------------------------------------------------------------------
    /**
     * Returns true if the given String is null or void ( "", " ", "  " )
     * @param s
     * @return
     */
    public static final boolean nullOrVoid(final String s) {
        if (s == null) {
            return true ;
        }
        else {
            if ( s.trim().length() == 0 ) {
                return true ;
            }
        }
        return false ;
    }


	/**
	 * Remove all the blank characters in the given string <br>
	 * eg : returns "abc" for " a b  c "
	 * @param s
	 * @return
	 */
	public static final String removeAllBlanks(String s) {
		if ( s == null ) return null ;
		StringBuilder sb = new StringBuilder(s.length());
		int n = s.length() ;
		char c ;
		for ( int i = 0 ; i < n ; i++ ) {
			c = s.charAt(i);
			if ( c != ' ' ) {
				sb.append(c);
			}
		}
		if ( sb.length() == s.length() ) {
			return s ;
		}
		else {
			return sb.toString() ;
		}
	}
	
	/**
	 * Removes a given string if present at the end of the original string <br>
	 * 
	 * @param originalString the original string ( e.g. 'abcd' )
	 * @param stringToBeRemoved the string to be removed ( e.g. 'cd' )
	 * @return the original string without the end ( e.g. 'ab' )
	 */
	public static final String removeEnd(String originalString, String stringToBeRemoved) {
		if ( originalString == null || stringToBeRemoved == null ) {
			return originalString;
		}
		if (originalString.endsWith(stringToBeRemoved)) {
			return originalString.substring(0, originalString.length() - stringToBeRemoved.length());
		}
		return originalString;
	}

    /**
     * Removes the quote characters if they are located at the first and last position of the string
     * The string is returned "as is" if there's no quotes at the begining and at the end
     * @param s the string 
     * @param c the quote character to use ( eg '\'', '"' )
     * @return
     */
    public static final String removeQuotes( String s, char c )
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
	 * Repeat the given char n times and returns the resulting string
	 * @param c
	 * @param n
	 * @return
	 */
	public static final String repeat(char c, int n) {
		if ( n <= 0 ) {
			return "" ;
		}
		char[] chars = new char[n];
		for ( int i=0 ; i < n ; i++) {
			chars[i] = c ;
		}
		return new String(chars);
	}
	
    /**
     * Replace the given variable name by the given value
     * @param s the initial string ( e.g. "bla bla bla ${MyVar} bla bla" )
     * @param sVarName the variable name ( e.g. "${MyVar}" )
     * @param sVarValue the variable value
     * @return the string after variable replacement
     */
    public static final String replaceVar( String s, String sVarName, String sVarValue )
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
     * Split a string using the given char as separator ( simple split without "reg exp" )
     * @param s : the string to split
     * @param c : the separator
     * @return : array of 'tokens' ( never null, size = 0 if the string is null, else 1 to N )
     */
    public static final String[] split(String s, char c) {
        if (s != null) {
            char[] chars = s.toCharArray();
            
            // Count separators
            int count = 0 ;
            for ( int n = 0 ; n < chars.length ; n++ ) {
                if ( chars[n] == c ) count++ ;
            }
            
            if ( count > 0 ) {
	            String[] sTokens = new String[count+1] ;
	            int iToken = 0;
	            int iOffset = 0 ;
	            int iLength = 0 ;
	            for ( int i = 0 ; i < chars.length ; i++ ) {
	                if ( chars[i] == c ) {
	                    //--- Create new token 
	                    sTokens[iToken] = new String(chars, iOffset, iLength );
	                    iToken++;
	                    //--- Reset 
	                    iOffset = i + 1 ;
	                    iLength = 0 ;
	                }
	                else {
	                    iLength++;
	                }
	            }
	            //--- Last Token ( current token ) 
                sTokens[iToken] = new String(chars, iOffset, iLength );
	            return sTokens ;
            }
            else {
                //--- No separator
                String[] ret = new String[1];
                ret[0] = s ;
                return ret ;
            }
        }
        return new String[0]; 
    }
    
	//-------------------------------------------------------------------------------
	public static final String toCamelCase(String start) {
		if (nullOrVoid(start)) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
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
}