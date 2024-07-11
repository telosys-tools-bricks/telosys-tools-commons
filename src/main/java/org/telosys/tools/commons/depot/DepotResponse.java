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
package org.telosys.tools.commons.depot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class holds a list of bubdles provided by a "depot" (GitHub, GitLab, etc) <br>
 * it also contains "API Rate Limit" information if provided by the depot API 
 * 
 * 
 * @author Laurent Guerin
 *
 */
public class DepotResponse {

	/**
	 * HTTP Status Code returned by the depot (GitHub, ... )
	 */
	private final int httpStatusCode ;
	
	/**
	 * List of repositories found in the depot (GitHub, ... )
	 */
	private final List<DepotElement> elements ;
	
	/**
	 * API rate limit 
	 */
	private final DepotRateLimit rateLimit ;

	/**
	 * Response body
	 */
	private final String responseBody ;

	/**
	 * Constructor
	 * @param httpStatusCode
	 * @param elements
	 * @param rateLimit
	 * @param responseBody
	 */
	public DepotResponse(int httpStatusCode, List<DepotElement> elements, DepotRateLimit rateLimit, String responseBody) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.elements = elements;
		sortByName(this.elements);
		
		this.rateLimit = rateLimit ;
		this.responseBody = responseBody ;
	}
	
	/**
	 * Sort by name the given list of elements
	 * 
	 * @param list
	 */
	private void sortByName( List<DepotElement> list ) {
//		Collections.sort(list, new Comparator<DepotElement>() {
//			@Override
//			public int compare(DepotElement repo1, DepotElement repo2) {
//				String name1 = repo1.getName();
//				String name2 = repo2.getName();
//				return name1.compareTo(name2);
//			}
//		});
		Collections.sort(list, (e1, e2) -> e1.getName().compareTo(e2.getName()) ) ;
	}
	
	/**
	 * Returns the HTTP Status Code ( e.g. : 200, 403, etc)
	 * @return
	 */
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	/**
	 * Returns a list containing all the elements (repositories) found in the depot 
	 * @return
	 */
	public List<DepotElement> getElements() {
		return elements;
	}

	/**
	 * Returns a list of element names (repository names)
	 * @return
	 */
	public List<String> getElementsNames() {
		List<String> bundles = new LinkedList<>();
		if ( elements != null ) {
			for ( DepotElement e : elements ) {
				bundles.add( e.getName() );				
			}
		}
		return bundles ;
	}

	public DepotRateLimit getRateLimit() {
		return rateLimit;
	}

	public String getResponseBody() {
		return responseBody;
	}

}
