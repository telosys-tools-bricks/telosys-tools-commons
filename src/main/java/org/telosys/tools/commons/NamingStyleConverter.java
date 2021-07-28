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
* Utility class to convert a string to different naming styles <br>
* Works with styles : camelCase, PascalCase, snake_case, ANACONDA_CASE <br>
* 
* @author Laurent Guerin
*
*/
public class NamingStyleConverter {

	private static final char UNDERSCORE = '_' ; 
	
	/**
	 * Converts a string to "snake_case" <br>
	 * The input string can be in any known style (snake_case, ANACONDA_CASE, camelCase, PascalCase) <br>
	 * Examples : 'myVarName', 'MyVarName' and 'MY_VAR_NAME' are converted to 'my_var_name' <br>
	 * @param s
	 * @return
	 */
	public String toSnakeCase(String s) {
		if ( s == null) {
			return "";
		}
		if ( s.indexOf(UNDERSCORE) >= 0 ) {
			// contains UNDERSCORE => snake_case or ANACONDA_CASE or Cobra_Case
			return s.toLowerCase();
		}
		StringBuilder sb = new StringBuilder();
		boolean started = false ;
		char lastCharAdded = 0;
		for (char c : s.toCharArray()) {
		    if (Character.isUpperCase(c) && started ) {
		    	if ( lastCharAdded != UNDERSCORE ) { // avoid double '_'
			        sb.append(lastCharAdded = UNDERSCORE);
		    	}
		    }
		    if ( c != ' ' ) {
		        sb.append(lastCharAdded = Character.toLowerCase(c));
		        started = true ;
		    }
		}
		return sb.toString();
	}

	/**
	 * Converts a string to "ANACONDA_CASE" <br>
	 * The input string can be in any known style (snake_case, ANACONDA_CASE, camelCase, PascalCase) <br>
	 * Examples : 'myVarName', 'MyVarName' and 'my_var_name' are converted to 'MY_VAR_NAME'<br>
	 * @param s
	 * @return
	 */
	public String toAnacondaCase(String s) {
		String s2 = toSnakeCase(s);
		return s2.toUpperCase();
	}

	/**
	 * Converts the given string to "camelCase" <br>
	 * The input string can be in any known style (snake_case, ANACONDA_CASE, camelCase, PascalCase) <br>
	 * Examples : 'my_var_name', 'MY_VAR_NAME' and 'MyVarName' are converted to "myVarName" <br>
	 * @param s
	 * @return
	 */
	public String toCamelCase(String s) {
		return uncapitalize(toPascalCase(s));
	}

	/**
	 * Converts a string to "camelCase" using the given separator character<br>
	 * Example : "my.var.name" with separator "." is converted to "myVarName" <br>
	 * @param s
	 * @param separator 
	 * @return
	 */
	public String toCamelCase(String s, String separator) {
		return uncapitalize(toPascalCase(s, separator));
	}
	
	/**
	 * Converts a string to "PascalCase" <br>
	 * The input string can be in any known style (snake_case, ANACONDA_CASE, camelCase, PascalCase) <br>
	 * Examples : 'my_var_name', 'MY_VAR_NAME' and 'myVarName' are converted to "MyVarName" <br>
	 * @param s 
	 * @return
	 */
	public String toPascalCase(String s) {
		return toPascalCase(s, UNDERSCORE);
	}
	
	/**
	 * Converts a string to "PascalCase" using the given separator character<br>
	 * Example : "my.var.name" with separator "." is converted to "MyVarName" <br>
	 * @param s
	 * @param separator 
	 * @return
	 */
	public String toPascalCase(String s, String separator) {
		if ( separator != null && ! separator.isEmpty() ) {
			return toPascalCase(s, separator.charAt(0));
		}
		else {
			// throw exception ?
			return toPascalCase(s, '_');
		}
	}
	
	//------------------------------------------------------------------------------------
	
	private String toPascalCase(String s, char separator) {
		if ( s == null) {
			return "";
		}
		if ( s.indexOf(separator) < 0 ) {
			// No separator in string => just capitalize
			return capitalize(s.trim());
		}
		StringBuilder sb = new StringBuilder();
		boolean started = false ;
		boolean previousIsSeparator = false;
		for (char c : s.toCharArray()) {
		    if (c == separator) {
		    	// ignore separator and track it
		    	previousIsSeparator = true ;
		    }
		    else {
			    if ( c != ' ' ) {
			    	char newChar = '?';
				    if ( previousIsSeparator ) {
				    	newChar = Character.toUpperCase(c);		    		
			    	}
			    	else {
			    		if ( started ) {
			    			// Not first char => Upper case
				    		newChar = Character.toLowerCase(c);		    		
			    		}
			    		else {
			    			// First char => Upper case
					    	newChar = Character.toUpperCase(c);		    		
			    		}
			    	}
				    sb.append(newChar);
			    	previousIsSeparator = false ;
			    	started = true ;
		    	}
		    }
		}
		return sb.toString();
	}

	private String capitalize(String s) {
		if ( s != null && ! s.isEmpty() ) {
			// 1rst char to upper case
			char[] charArray = s.toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);
			return new String(charArray);
		}
		else {
			return s ;
		}
	}
	
	private String uncapitalize(String s) {
		if ( s != null && ! s.isEmpty() ) {
			// 1rst char to lower case
			char[] charArray = s.toCharArray();
			charArray[0] = Character.toLowerCase(charArray[0]);
			return new String(charArray);
		}
		else {
			return s ;
		}
	}
	
}

