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
 * Generic class providing logging methods
 * 
 * @author Laurent GUERIN
 *
 */
public abstract class GenericTool 
{
	private final TelosysToolsLogger      _logger  ;

	//----------------------------------------------------------------------------------
	public GenericTool(TelosysToolsLogger logger) {
		super();
		_logger = logger;
	}

	//----------------------------------------------------------------------------------
	protected final void log(Object object, String s) {
    	if ( _logger != null ) {
    		_logger.log(object, s);
    	}
	}
	
	//----------------------------------------------------------------------------------
	public final void log(String s) {
    	if ( _logger != null ) {
    		_logger.log(s);
    	}
	}

	//----------------------------------------------------------------------------------
	public final void logError(String s) {
    	if ( _logger != null ) {
    		_logger.error(s);
    	}
	}

	//----------------------------------------------------------------------------------
	public final void logInfo(String s) {
    	if ( _logger != null ) {
    		_logger.info(s);
    	}
	}
	
	//----------------------------------------------------------------------------------
	public final void logException (Throwable e) {
    	if ( _logger != null ) {
    		_logger.exception(e);
    	}
	}

	//----------------------------------------------------------------------------------
	public final void throwException (String msg) throws TelosysToolsException
	{
    	if ( _logger != null ) {
    		_logger.error(msg);
    	}
    	throw new TelosysToolsException(msg);
	}
	//----------------------------------------------------------------------------------
	public final void throwException (String msg, Throwable e) throws TelosysToolsException
	{
    	if ( _logger != null ) {
    		_logger.error(msg);
    	}
    	throw new TelosysToolsException(msg,e);
	}
	//----------------------------------------------------------------------------------
}
