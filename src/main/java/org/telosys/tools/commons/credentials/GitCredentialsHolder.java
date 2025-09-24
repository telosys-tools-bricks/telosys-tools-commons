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

import org.telosys.tools.commons.exception.TelosysRuntimeException;

/**
 * Domain model general information (name, version, description )  <br>
 * stored in the ".model" properties file
 *
 * @author Laurent Guerin
 */
public class GitCredentialsHolder {
	
	// keys for scope
	private static final String KEY_GLOBAL  = "global" ;
	private static final String KEY_MODELS  = "models" ;
	private static final String KEY_BUNDLES = "bundles" ;
	
	// keys for what in scope
	private static final String KEY_USER  = "user" ;
	private static final String KEY_TOKEN = "token" ;

	private final Map<String,String> credentialsMap ;
	
    /**
     * Default constructor (empty holder)
     */
    protected GitCredentialsHolder() {
        super();
        this.credentialsMap = new HashMap<>();
    }
    
    /**
     * Constructor (init holder with the given map)
     * @param inputMap
     */
    protected GitCredentialsHolder(Map<String,Object> inputMap) {
        super();
        this.credentialsMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            Object value = entry.getValue();
            this.credentialsMap.put(entry.getKey(), value != null ? String.valueOf(value) : "" );
        }
    }

    /**
     * Returns a copy of the current internal map
     * @return
     */
    protected Map<String,Object> getMap() {
    	Map<String, Object> objectMap = new HashMap<>();
    	for (Map.Entry<String, String> entry : credentialsMap.entrySet()) {
    		objectMap.put(entry.getKey(), entry.getValue());  // String to Object
    	}
    	return objectMap;
    }

    /**
     * Returns true if this holder is empty (no credentials in any scope)
     * @return
     */
    public final boolean isEmpty() {
    	return credentialsMap.isEmpty();
    }

    /**
     * Get credentials for the given scope
     * @param scope
     * @return
     */
    public final GitCredentials getCredentials(GitCredentialsScope scope) {
    	String user  = credentialsMap.get(buildKey(scope, KEY_USER));
    	String token = credentialsMap.get(buildKey(scope, KEY_TOKEN));
    	if (user != null && token != null) {
    		return new GitCredentials(user, token) ;
    	}
    	else {
    		return null;
    	}
    }

    /**
     * Set credentials for the given scope
     * @param scope
     * @param gitCredentials
     */
    public final void setCredentials(GitCredentialsScope scope, GitCredentials gitCredentials) {
    	credentialsMap.put( buildKey(scope, KEY_USER) , gitCredentials.getUserName());
    	credentialsMap.put( buildKey(scope, KEY_TOKEN), gitCredentials.getPasswordOrToken());
    }

    /**
     * Remove credentials for the given scope
     * @param scope
     */
    public final boolean removeCredentials(GitCredentialsScope scope) {
    	if ( credentialsExist(scope) ) {
        	credentialsMap.remove( buildKey(scope, KEY_USER)  );
        	credentialsMap.remove( buildKey(scope, KEY_TOKEN) );
        	return true;
    	}
    	else {
    		return false;
    	}
    }

    public final boolean credentialsExist(GitCredentialsScope scope) {
    	return credentialsMap.get(buildKey(scope, KEY_USER) ) != null 
    		|| credentialsMap.get(buildKey(scope, KEY_TOKEN) ) != null  ;
    }
    
    private String buildKey(GitCredentialsScope scope, String what) {
    	switch (scope) {
    	case GLOBAL:
    		return KEY_GLOBAL + "." + what ;
    	case MODELS:
    		return KEY_MODELS + "." + what ;
    	case BUNDLES:
    		return KEY_BUNDLES + "." + what ;
    	default:
    		throw new TelosysRuntimeException("Unexpected scope"); // Cannot happen
    	}
    }
}
