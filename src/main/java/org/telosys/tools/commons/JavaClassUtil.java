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
public class JavaClassUtil
{
	
	
	/**
	 * Full static class : no public constructor
	 */
	private JavaClassUtil() {
	}

	//----------------------------------------------------------------------------------
	/**
	 * Returns the short name of the given class name <br>
	 * e.g. : "package1.package2.MyClass" returns "MyClass"
	 * @param s 
	 * @return
	 */
	public static String shortName(String sArg) {
		if ( sArg != null )
		{
			String s = sArg.trim();
			int i = s.lastIndexOf('.');
			if ( i >= 0 )
			{
				return s.substring(i+1);
			}
			return s ;
		}
		return null ;
	}

}
