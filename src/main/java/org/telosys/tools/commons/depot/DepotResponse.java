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

import org.telosys.tools.commons.Filter;

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
	 * URL of the depot (GitHub URL, ... )
	 */
	private final String depotURL ;
	
	/**
	 * Name of the depot 
	 */
	private final String depotName ;
	
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
	 * Number of http requests ( for example with pagination : 3 requets to get 3 pages )
	 */
	private final int numberOfRequests ;

	/**
	 * Constructor
	 * @param depotName
	 * @param depotURL
	 * @param httpStatusCode
	 * @param elements
	 * @param rateLimit
	 * @param numberOfRequests
	 */
	public DepotResponse(String depotName, String depotURL, int httpStatusCode, List<DepotElement> elements, DepotRateLimit rateLimit, int numberOfRequests) {
		super();
		this.depotName = depotName;
		this.depotURL = depotURL;
		this.httpStatusCode = httpStatusCode;
		this.elements = elements;
		sortByName(this.elements);
		
		this.rateLimit = rateLimit ;
		this.numberOfRequests = numberOfRequests ;
	}
	
	/**
	 * Sort by name the given list of elements
	 * 
	 * @param list
	 */
	private void sortByName( List<DepotElement> list ) {
		Collections.sort(list, (e1, e2) -> e1.getName().compareTo(e2.getName()) ) ;
	}

	
	/**
	 * Returns the depot name
	 * @return
	 */
	public String getDepotName() {
		return depotName;
	}

	/**
	 * Returns the URL used to call the depot API 
	 * @return
	 */
	public String getDepotURL() {
		return depotURL;
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
	 * Search and return an element by its name 
	 * @param elementName
	 * @return element found or null
	 */
	public DepotElement getElementByName(String elementName) {
		for ( DepotElement e : elements) {
			if ( elementName.equals(e.getName()) ) {
				return e; 
			}
		}
		return null;
	}
	
	/**
	 * Filter elements list according to given criteria
	 * @param criteria
	 * @return
	 */
	public List<DepotElement> filterElementsByName(List<String> criteria) {
		if ( Filter.noCriteria(criteria)) {
			return elements; // no filtering
		}
		else {
			List<DepotElement> result = new LinkedList<>();
			for ( DepotElement element : elements ) {
				if ( Filter.keepElement(element.getName(), criteria) ) {
					result.add(element);
				}
			}
			return result;
		}
	}
	
	/**
	 * Returns a list of element names (repository names)
	 * @return
	 */
	public List<String> getElementNames() {
		List<String> names = new LinkedList<>();
		if ( elements != null ) {
			for ( DepotElement e : elements ) {
				names.add( e.getName() );				
			}
		}
		return names;
	}

	/**
	 * Returns true if the depot response contains an element having the given name
	 * @param elementName
	 * @return
	 * @since 4.3.0
	 */
	public boolean contains(String elementName) {
		if (elementName == null ) return false;
		for (DepotElement element : elements ) {
			if (elementName.equals(element.getName()) ) {
				return true;
			}
		}
		return false;
	}
	
	public DepotRateLimit getRateLimit() {
		return rateLimit;
	}

	/**
	 * Returns the number of requests used to get the depot elements (more than 1 is possible in case of pagination)
	 * @return
	 */
	public int getNumberOfRequests() {
		return numberOfRequests;
	}

}
