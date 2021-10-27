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
package org.telosys.tools.commons ;

import java.util.List;


public class ListUtil {
	
	/**
	 * Private constructor to avoid instance creation
	 */
	private ListUtil(){}
	
    public static String join(List<String> list, String separator)  {
		StringBuilder sb = new StringBuilder();
		if ( list != null ) {
			int n = 0 ;
			for ( String s : list ) {
				n++;
				if (n > 1 && separator != null) {
					sb.append(separator);
				}
				sb.append(s);
			}
		}
		return sb.toString();
    }
}
