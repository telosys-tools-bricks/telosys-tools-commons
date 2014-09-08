/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
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
public class TelosysToolsException extends Exception 
{
    //private static final TelosysClassLogger log = new TelosysClassLogger(TelosysException.class);

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @param message
     */
    public TelosysToolsException(String message)
    {
        super(message);
//        Telosys.error("TelosysException : " + message);
//        Telosys.error(this);
    }

    /**
     * @param message
     * @param cause
     */
    public TelosysToolsException(String message, Throwable cause)
    {
        super(message, cause);
//        Telosys.error("TelosysException : " + message + " ( cause : " + cause.toString() + ")");
//        Telosys.error(this);

    }
}