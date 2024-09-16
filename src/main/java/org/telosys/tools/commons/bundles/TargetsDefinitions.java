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
package org.telosys.tools.commons.bundles;

import java.util.LinkedList;
import java.util.List;

public class TargetsDefinitions {
	
	private final List<TargetDefinition> templatesTargets ;
	private final List<TargetDefinition> resourcesTargets ;
	
	/**
	 * Constructor
	 * @param templatesTargets list of templates (vm files)
	 * @param resourcesTargets list of resources (static files)
	 */
	public TargetsDefinitions(List<TargetDefinition> templatesTargets, List<TargetDefinition> resourcesTargets) {
		super();
		this.templatesTargets = templatesTargets ;
		this.resourcesTargets = resourcesTargets ;
	}

	/**
	 * Default constructor <br>
	 * Creates an instance with 2 void lists <br>
	 */
	public TargetsDefinitions() {
		super();
		this.templatesTargets = new LinkedList<>() ;
		this.resourcesTargets = new LinkedList<>() ;
	}

	/**
	 * Returns a list of 'templates' targets (targets definitions for code generation)
	 * @return
	 */
	public List<TargetDefinition> getTemplatesTargets() {
		return templatesTargets;
	}

	/**
	 * Returns a list of 'resources copy' targets (targets definitions for file copy)
	 * @return
	 */
	public List<TargetDefinition> getResourcesTargets() {
		return resourcesTargets;
	}

	@Override
	public String toString() {
		return "TargetsDefinitions : " + templatesTargets.size() + " template(s) " + resourcesTargets.size() + " resource(s)" ;
	}
}
