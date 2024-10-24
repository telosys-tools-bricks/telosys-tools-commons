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

import java.util.LinkedList;
import java.util.List;

public class Filter {
	
	/**
	 * Private constructor
	 */
	private Filter() {	
	}

	/**
	 * Filter the given list with the given criterion
	 * @param elements
	 * @param criterion
	 * @return
	 */
	public static List<String> filter(List<String> elements, String criterion) {

		if ( noCriterion(criterion) )  {
			// No criterion => no filter => return the same list
			return elements ;
		}
		else {
			// Filter : check bundle name contains at least 1 of the given criteria
			List<String> result = new LinkedList<>();
			for ( String element : elements ) {
				if ( keepElement(element, criterion) ) {
					result.add(element);
				}
			}
			return result;
		}
	}
	
	/**
	 * Filter the given list with the given criteria
	 * @param elements
	 * @param criteria
	 * @return
	 */
	public static List<String> filter(List<String> elements, List<String> criteria) {

		if ( noCriteria(criteria) )  {
			// No criteria => no filter => return the same list
			return elements ;
		}
		else {
			// Filter : check bundle name contains at least 1 of the given criteria
			List<String> result = new LinkedList<>();
			for ( String element : elements ) {
				if ( keepElement(element, criteria) ) {
					result.add(element);
				}
			}
			return result;
		}
	}
	
	/**
	 * Returns true if the given element must be kept according with the given criteria
	 * @param element
	 * @param criteria
	 * @return
	 */
	public static boolean keepElement(String element, List<String> criteria) {
		for ( String criterion : criteria ) {
			if ( keepElement(element, criterion) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the given element must be kept according with the given criterion
	 * @param element
	 * @param criterion
	 * @return
	 */
	public static boolean keepElement(String element, String criterion) {
		return element.contains(criterion);
	}
	
	public static boolean noCriterion(String criterion) {
		return criterion == null || criterion.isEmpty() || "*".equals(criterion);
	}
	
	public static boolean noCriteria(List<String> criteria) {
		return criteria == null || criteria.isEmpty() || criteria.contains("*"); 
	}
	
}
