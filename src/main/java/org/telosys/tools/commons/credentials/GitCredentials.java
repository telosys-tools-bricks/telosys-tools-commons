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
package org.telosys.tools.commons.credentials;

import java.util.HashMap;
import java.util.Map;

/**
 * Domain model general information (name, version, description )  <br>
 * stored in the ".model" properties file
 *
 * @author Laurent Guerin
 */
public class GitCredentials {
	
	// scope
	public static final String GLOBAL  = "global" ;
	public static final String MODELS  = "models" ;
	public static final String BUNDLES = "bundles" ;
	
	// what in scope
	public static final String USER  = "user" ;
	public static final String TOKEN = "token" ;

//	public static final String MODELS_USER    = "models.user" ;
//	public static final String MODELS_TOKEN   = "models.token" ;
//	public static final String BUNDLES_USER   = "bundles.user" ;
//	public static final String BUNDLES_TOKEN  = "bundles.token" ;
	
	private final Map<String,String> credentialsMap ;
	
    /**
     * Constructor
     * @param inputMap
     */
    public GitCredentials(Map<String,Object> inputMap) {
        super();
        this.credentialsMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            Object value = entry.getValue();
            this.credentialsMap.put(entry.getKey(), value != null ? String.valueOf(value) : "" );
        }
    }

    public final GitUserToken getGlobalCredentials() {
        return getCredentialsFor(GLOBAL);
    }
    
    public final GitUserToken getCredentialsForModels() {
    	return getCredentialsFor(MODELS);
    }
    
    public final GitUserToken getCredentialsForBundles() {
    	return getCredentialsFor(BUNDLES);
    }
    
    protected Map<String,Object> getMap() {
    	Map<String, Object> objectMap = new HashMap<>();
    	for (Map.Entry<String, String> entry : credentialsMap.entrySet()) {
    		objectMap.put(entry.getKey(), entry.getValue());  // String to Object
    	}
    	return objectMap;
    }

    protected final GitUserToken getCredentialsFor(String scope) {
    	String user = get(scope, USER);
    	String token = get(scope, TOKEN);
    	if (user != null && token != null) {
    		return new GitUserToken(user, token) ;
    	}
    	else {
    		return null;
    	}
    }

    private String get(String scope, String what) {
    	if ( GLOBAL.equals(scope) ) {
    		return  getFromMap(GLOBAL, what);
    	}
    	else {
        	String s = getFromMap(scope, what);
        	return s != null ? s : getFromMap(GLOBAL, what);
    	}
    }
    
    private String getFromMap(String scope, String what) {
    	String key = scope + "." + what ;
    	return credentialsMap.get(key);
    }
}
