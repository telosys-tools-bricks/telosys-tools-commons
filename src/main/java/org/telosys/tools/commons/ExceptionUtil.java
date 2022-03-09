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
 * Stack Trace utility class
 * 
 * @author Laurent GUERIN
 *
 */
public class ExceptionUtil 
{
	private static final int STACK_MAX_CALLERS = 100 ;
	
	/**
	 * Priavte constructor
	 */
	private ExceptionUtil() {	
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Returns a multi-line string containing the summary of the given exception (with causes if any)<br>
	 * eg : <br>
	 * Exception java.lang.RuntimeException : My test exception <br>
	 *  at org.telosys.tools.tests.commons.ExceptionUtilTest.throwExceptionWithCause ( line 32 ) <br>
	 * Cause java.lang.IllegalArgumentException : Illegal value <br>
	 *  at org.telosys.tools.tests.commons.ExceptionUtilTest.throwExceptionCause ( line 24 )  <br>
	 * <br>
	 * @param ex
	 * @return
	 */
	public static String getExceptionSummary( Throwable ex ) {
		if ( ex != null ) {
			StringBuilder sb = new StringBuilder();
			addExceptionSummary("Exception", ex, sb);
			Throwable cause = ex.getCause() ;
			while ( cause != null ) {
				addExceptionSummary("Cause", cause, sb);
				cause = cause.getCause();
			}
			return sb.toString();
		}
		else {
			return "No exception (null)" ;
		}
	}
	
	private static void addExceptionSummary( String type, Throwable ex, StringBuilder sb ) {
		sb.append(type );
		sb.append(" " );
		sb.append(ex.getClass().getName() );
		sb.append(" : " );
		sb.append(ex.getMessage() );
		sb.append("\n" );
		StackTraceElement[] stackTrace = ex.getStackTrace() ;
		if ( stackTrace != null && stackTrace.length > 0 ) {
			StackTraceElement ste = stackTrace[0] ;
			sb.append(" at " ) ;
			sb.append(ste.getClassName() );
			sb.append(".");
			sb.append(ste.getMethodName() );
			sb.append(" ( line ");
			sb.append(ste.getLineNumber() );
			sb.append(" ) \n" );
		}
	}
	
	//----------------------------------------------------------------------------------
	public static String getStackTraceAsString( Throwable ex ) {
		return getStackTraceAsString(ex, STACK_MAX_CALLERS ) ;
	}
	
	//----------------------------------------------------------------------------------
	public static String getStackTraceAsString(Throwable ex, int maxCallers ) {
		if ( ex != null ) {
			return getStackTraceAsString(ex.getStackTrace(), maxCallers);
		}
		else {
			return "No stack trace (the given Exception is null)" ; 
		}
	}

	//----------------------------------------------------------------------------------
	public static String getStackTraceAsString(StackTraceElement[] stackTrace ) {
		return getStackTraceAsString(stackTrace, STACK_MAX_CALLERS); 
	}
	
	//----------------------------------------------------------------------------------
	public static String getStackTraceAsString(StackTraceElement[] stackTrace, int maxCallers ) {
		
		if ( stackTrace.length > 0 ) {
        	StringBuilder sb = new StringBuilder();
            //--- Callers stack ( 1 to N )
    		for ( int i = 0 ; i < stackTrace.length && i < maxCallers ; i++ ) {
	        	StackTraceElement stackTraceElement = stackTrace[i];
				sb.append(" . ");
				sb.append(stackTraceElement.getClassName());
				sb.append(".");
				sb.append(stackTraceElement.getMethodName());
				sb.append(" (line " + stackTraceElement.getLineNumber() + ")" );
				sb.append("\n");
	        }
	        return sb.toString();
        }
        else {
        	return "No stack trace (void)";
        }
	}
}
