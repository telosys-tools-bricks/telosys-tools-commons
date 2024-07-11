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

import java.util.Date;

/**
 * This class holds the depot "rate limit" information if any. <br>
 * Typically for GitHub : see https://developer.github.com/v3/#rate-limiting <br>
 * 
 * @author Laurent Guerin
 *
 */
public class DepotRateLimit {

	/**
	 * The maximum number of requests you're permitted to make per hour.
	 */
	private final String   limit ;
	
	/**
	 * The number of requests remaining in the current rate limit window.
	 */
	private final String   remaining ;
	
	/**
	 * The time at which the current rate limit window resets in UTC epoch seconds.
	 */
	private final String   reset ; 

	/**
	 * Constructor 
	 * @param limit
	 * @param remaining
	 * @param reset
	 */
	public DepotRateLimit(String limit, String remaining, String reset) {
		super();
		this.limit = limit;
		this.remaining = remaining;
		this.reset = reset;
	}

	/**
	 * Returns the API 'limit' value as string (original http header recieved)
	 * @return
	 */
	public String getLimit() {
		return limit;
	}
	/**
	 * Returns the API 'limit' value as int
	 * @return
	 */
	public int getLimitAsInt() {
		try {
			return Integer.parseInt(limit);
		} catch (NumberFormatException e) {
			// Can happen if GitHub API change 
			return 0;
		}
	}

	/**
	 * Returns the API 'remaining' value as string (original http header recieved)
	 * @return
	 */
	public String getRemaining() {
		return remaining;
	}
	/**
	 * Returns the API 'remaining' value as int
	 * @return
	 */
	public int getRemainingAsInt() {
		try {
			return Integer.parseInt(remaining);
		} catch (NumberFormatException e) {
			// Can happen if GitHub API change 
			return 0;
		}
	}

	/**
	 * Returns the API 'reset' value as string (original http header recieved)
	 * @return
	 */
	public String getReset() {
		return reset;
	}

	/**
	 * Returns the API 'reset' value as date
	 * @return
	 */
	public Date getResetAsDate() {
		// RESET : The time at which the current rate limit window resets in UTC epoch seconds.
		long seconds ;
		try {
			seconds = Long.parseLong(this.reset);
		} catch (NumberFormatException e) {
			// Can happen if GitHub API change 
			seconds = 0L;
		}
		long milliseconds = seconds * 1000 ;
		return new Date(milliseconds);
	}

	public String getStandardMessage() {
		return "API rate limit : " + remaining + "/" + limit
		+ " (reset " + reset + ")" ;		
	}

	@Override
	public String toString() {
		return "DepotRateLimit [limit='" + limit + "', remaining='" + remaining + "', reset='" + reset + "']";
	}
	
}
