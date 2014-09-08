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
package org.telosys.tools.commons.github;

/**
 * This class holds few information about GitHub repository
 * 
 * @author L. Guerin
 *
 */
public class GitHubRepository {

	private final long   id ;
	
	private final String name ;
	
	private final String description ;

	private final long   size ;
	
	
	/**
	 * Constructor
	 * @param id
	 * @param name
	 * @param description
	 * @param size
	 */
	public GitHubRepository(long id, String name, String description, long size) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.size = size ;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public long getSize() {
		return size;
	}

}
