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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author Laurent GUERIN
 *
 */
public class MethodInvoker
{
	private final static String ERROR_GET_GETTER_METHOD    = "Cannot get method " ;
	private final static String ERROR_INVOKE_GETTER_METHOD = "Cannot invoke method " ;

	/**
	 * Full static class : no public constructor
	 */
	private MethodInvoker() {
	}

	//----------------------------------------------------------------------------------
	public static Object invokeGetter(Object instance, String methodName) {

		if ( null == instance ) {
			throw new IllegalArgumentException("Object instance argument is null");
		}
		if ( null == methodName ) {
			throw new IllegalArgumentException("Method name argument is null");
		}

		Class<?> cl = instance.getClass();
		
		Object result = null ;
		Method method ;
		try {
			method = cl.getMethod(methodName, (Class<?>[])null); 
		} catch (SecurityException e) {
			throw new RuntimeException(ERROR_GET_GETTER_METHOD + methodName + " (SecurityException)", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(ERROR_GET_GETTER_METHOD + methodName + " (NoSuchMethodException)", e);
		}

		try {
			result = method.invoke(instance, (Object[])null);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(ERROR_INVOKE_GETTER_METHOD + methodName + " (IllegalArgumentException)", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(ERROR_INVOKE_GETTER_METHOD + methodName + " (IllegalAccessException)", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(ERROR_INVOKE_GETTER_METHOD + methodName + " (InvocationTargetException)", e);
		}
		return result ;
	}
	
	public static boolean invokeBooleanGetter(Object instance, String methodName) {
		Object result = invokeGetter(instance, methodName);
		if ( result instanceof Boolean ) {
			return (Boolean) result ;
		}
		else {
			throw new RuntimeException("Invalid type returned by method " + methodName + " (boolean expected)" );
		}
	}
	
}
