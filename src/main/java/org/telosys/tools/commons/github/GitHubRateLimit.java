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
package org.telosys.tools.commons.github;

import java.util.Date;

/**
 * This class holds GitHub "rate limit" information <br>
 * 
 * See https://developer.github.com/v3/#rate-limiting <br>
 * 
 * @author Laurent Guerin
 *
 */
public class GitHubRateLimit {

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
	 * Http response body in JSON
	 */
	private final String   responseBody ; 

	public GitHubRateLimit(String limit, String remaining, String reset, String responseBody ) {
		super();
		this.limit = limit;
		this.remaining = remaining;
		this.reset = reset;
		this.responseBody = responseBody;
	}

	public String getLimit() {
		return limit;
	}

	public String getRemaining() {
		return remaining;
	}

	public String getReset() {
		return reset;
	}

	public Date getResetDate() {
		// RESET : The time at which the current rate limit window resets in UTC epoch seconds.
		long seconds = Long.parseLong(this.reset);
		long milliseconds = seconds * 1000 ;
		return new Date(milliseconds);
	}

	public String getResponseBody() {
		return responseBody;
	}

	@Override
	public String toString() {
		return "GitHubRateLimit [limit=" + limit + ", remaining=" + remaining + ", reset=" + reset + "]";
	}
	
}
