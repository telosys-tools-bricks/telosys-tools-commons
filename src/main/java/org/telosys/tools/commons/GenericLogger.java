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
 * Basic utility class for system console logging (only for DEBUG) 
 * 
 * @author Laurent GUERIN
 *
 */
public abstract class GenericLogger implements TelosysToolsLogger
{
	//----------------------------------------------------------------------------------
	protected abstract void print(String s) ;

	//----------------------------------------------------------------------------------
	public final void log(Object object, String s) {
		String className = "???" ;
		if ( object != null ) {
			className = object.getClass().getSimpleName();
		}
		print("[LOG] (" + className + ") : " + s);
	}
	
	//----------------------------------------------------------------------------------
	public final void log(String s) {
		print("[LOG] " + s);
	}

	//----------------------------------------------------------------------------------
	public final void error(String s) {
		print("[ERROR] " + s);
	}

	//----------------------------------------------------------------------------------
	public final void info(String s) {
		print("[INFO] " + s );
	}
	
	//----------------------------------------------------------------------------------
	public final void logStackTrace (Throwable e) {
		print( "----- "  );
		print( " Exception : " + e.getClass().getName() );
		print( " Message   : " + e.getMessage() );
		printStackTrace(e) ;
		
		Throwable cause = e.getCause() ;
		while ( cause != null ) 
		{
			print( "----- "  );
			print( " Cause   : "  + cause.getClass().getName() );
			print( " Message : " + cause.getMessage() );
			printStackTrace(cause) ;
			
			cause = cause.getCause();
		}
	}

	//----------------------------------------------------------------------------------
//	private static final int STACK_MAX_CALLERS = 1000 ;
	
    private void printStackTrace(Throwable ex)
    {
//        StackTraceElement stackTrace[] = ex.getStackTrace();
//        StackTraceElement stackTraceElement = null;
//        if ( stackTrace.length > 0 )
//        {
//            //--- Callers stack ( 1 to N )
//	        for ( int i = 0 ; i < stackTrace.length && i < STACK_MAX_CALLERS+1 ; i++ )
//	        {
//	            stackTraceElement = stackTrace[i];
//	            int iLineNumber = stackTraceElement.getLineNumber();
//	            String sLineNumber = "" ;
//	            if ( iLineNumber > 0 ) sLineNumber = " (line " + iLineNumber + ")" ;
//	            print( " . " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + sLineNumber );
//	        }
//        }    	
        
        print ( ExceptionUtil.getStackTraceAsString(ex) ) ;
    }
	
}
