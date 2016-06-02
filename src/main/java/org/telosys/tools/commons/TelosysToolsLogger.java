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
 * Interface for basic logging (only for debug)
 * 
 * @author Laurent GUERIN
 *
 */
public interface TelosysToolsLogger
{
	/**
	 * Logs a message from the given instance
	 * @param instance the instance that call the method
	 * @param s
	 */
	public void log(Object instance, String s) ;
	
	/**
	 * Logs a message
	 * @param s
	 */
	public void log(String s) ;

	/**
	 * Logs an error message
	 * @param s
	 */
	public void error (String s) ;
	
	/**
	 * Logs an information message
	 * @param s
	 */
	public void info (String s) ;

}
