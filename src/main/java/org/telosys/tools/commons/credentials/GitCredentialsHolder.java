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
     * Constructor
     * @param inputMap
     */
    public GitCredentialsHolder(Map<String,Object> inputMap) {
        super();
        this.credentialsMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            Object value = entry.getValue();
            this.credentialsMap.put(entry.getKey(), value != null ? String.valueOf(value) : "" );
        }
    }

    /**
     * @return
     */
    protected Map<String,Object> getMap() {
    	Map<String, Object> objectMap = new HashMap<>();
    	for (Map.Entry<String, String> entry : credentialsMap.entrySet()) {
    		objectMap.put(entry.getKey(), entry.getValue());  // String to Object
    	}
    	return objectMap;
    }

//    /**
//     * Get 'global' credentials
//     * @return
//     */
//    public final GitCredentials getCredentialsForGlobal() {
//        return getCredentialsForScope(KEY_GLOBAL);
//    }
//    /**
//     * Set 'global' credentials
//     * @param gitUserToken
//     */
//    public final void setCredentialsForGlobal(GitCredentials gitUserToken) {
//    	setCredentialsForScope(KEY_GLOBAL, gitUserToken);
//    }
//    /**
//     * Remove 'global' credentials
//     */
//    public final void removeCredentialsForGlobal() {
//    	removeCredentialsForScope(KEY_GLOBAL);
//    }
    
//    /**
//     * Get 'models' credentials
//     * @return
//     */
//    public final GitCredentials getCredentialsForModels() {
//    	return getCredentialsForScope(KEY_MODELS);
//    }
//    /**
//     * Set 'models' credentials
//     * @param gitUserToken
//     */
//    public final void setCredentialsForModels(GitCredentials gitUserToken) {
//    	setCredentialsForScope(KEY_MODELS, gitUserToken);
//    }
//    /**
//     * Remove 'models' credentials
//     */
//    public final void removeCredentialsForModels() {
//    	removeCredentialsForScope(KEY_MODELS);
//    }
    
//    /**
//     * Get 'bundles' credentials
//     * @return
//     */
//    public final GitCredentials getCredentialsForBundles() {
//    	return getCredentialsForScope(KEY_BUNDLES);
//    }
//    /**
//     * Set 'bundles' credentials
//     * @param gitUserToken
//     */
//    public final void setCredentialsForBundles(GitCredentials gitUserToken) {
//    	setCredentialsForScope(KEY_BUNDLES, gitUserToken);
//    }
//    /**
//     * Remove 'bundles' credentials
//     */
//    public final void removeCredentialsForBundles() {
//    	removeCredentialsForScope(KEY_BUNDLES);
//    }
    
    //--------------------------------------------------------------------
    // GET
    //--------------------------------------------------------------------
//    protected final GitCredentials getCredentialsForScope(String scope) {
//    	String user = get(scope, KEY_USER);
//    	String token = get(scope, KEY_TOKEN);
//    	if (user != null && token != null) {
//    		return new GitCredentials(user, token) ;
//    	}
//    	else {
//    		return null;
//    	}
//    }
    public final GitCredentials getCredentials(GitCredentialsScope scope) {
    	String user = get(scope, KEY_USER);
    	String token = get(scope, KEY_TOKEN);
    	if (user != null && token != null) {
    		return new GitCredentials(user, token) ;
    	}
    	else {
    		return null;
    	}
    }
//    private String get(String scope, String what) {
//    	if ( KEY_GLOBAL.equals(scope) ) {
//    		return  credentialsMap.get(key(KEY_GLOBAL, what) );
//    	}
//    	else {
//        	String s = credentialsMap.get(key(scope, what) );
//        	return s != null ? s : credentialsMap.get(key(KEY_GLOBAL, what) );
//    	}
//    }
    private String get(GitCredentialsScope scope, String what) {
    	if ( scope == GitCredentialsScope.GLOBAL ) {
    		return  credentialsMap.get(key(GitCredentialsScope.GLOBAL, what) );
    	}
    	else {
        	String s = credentialsMap.get(key(scope, what) );
        	return s != null ? s : credentialsMap.get(key(GitCredentialsScope.GLOBAL, what) );
    	}
    }
//    private String getFromMap(String scope, String what) {
//    	String key = scope + "." + what ;
//    	return credentialsMap.get(key);
//    }

    //--------------------------------------------------------------------
    // SET
    //--------------------------------------------------------------------
    public final void setCredentials(GitCredentialsScope scope, GitCredentials gitCredentials) {
    	credentialsMap.put( key(scope, KEY_USER) , gitCredentials.getUser());
    	credentialsMap.put( key(scope, KEY_TOKEN), gitCredentials.getToken());
    }

    //--------------------------------------------------------------------
    // REMOVE
    //--------------------------------------------------------------------
    public final void removeCredentials(GitCredentialsScope scope) {
    	credentialsMap.remove( key(scope, KEY_USER)  );
    	credentialsMap.remove( key(scope, KEY_TOKEN) );
    }
    
//    private String key(String scope, String what) {
//    	return scope + "." + what ;
//    }
    private String key(GitCredentialsScope scope, String what) {
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
