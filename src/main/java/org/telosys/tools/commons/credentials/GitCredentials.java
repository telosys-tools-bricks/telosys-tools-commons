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

    /**
     * Get 'global' credentials
     * @return
     */
    public final GitUserToken getCredentialsForGlobal() {
        return getCredentialsForScope(GLOBAL);
    }
    /**
     * Set 'global' credentials
     * @param gitUserToken
     */
    public final void setCredentialsForGlobal(GitUserToken gitUserToken) {
    	setCredentialsForScope(GLOBAL, gitUserToken);
    }
    /**
     * Remove 'global' credentials
     */
    public final void removeCredentialsForGlobal() {
    	removeCredentialsForScope(GLOBAL);
    }
    
    /**
     * Get 'models' credentials
     * @return
     */
    public final GitUserToken getCredentialsForModels() {
    	return getCredentialsForScope(MODELS);
    }
    /**
     * Set 'models' credentials
     * @param gitUserToken
     */
    public final void setCredentialsForModels(GitUserToken gitUserToken) {
    	setCredentialsForScope(MODELS, gitUserToken);
    }
    /**
     * Remove 'models' credentials
     */
    public final void removeCredentialsForModels() {
    	removeCredentialsForScope(MODELS);
    }
    
    /**
     * Get 'bundles' credentials
     * @return
     */
    public final GitUserToken getCredentialsForBundles() {
    	return getCredentialsForScope(BUNDLES);
    }
    /**
     * Set 'bundles' credentials
     * @param gitUserToken
     */
    public final void setCredentialsForBundles(GitUserToken gitUserToken) {
    	setCredentialsForScope(BUNDLES, gitUserToken);
    }
    /**
     * Remove 'bundles' credentials
     */
    public final void removeCredentialsForBundles() {
    	removeCredentialsForScope(BUNDLES);
    }
    
    //--------------------------------------------------------------------
    // GET
    //--------------------------------------------------------------------
    protected final GitUserToken getCredentialsForScope(String scope) {
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

    //--------------------------------------------------------------------
    // SET
    //--------------------------------------------------------------------
    protected final void setCredentialsForScope(String scope, GitUserToken gitUserToken) {
    	credentialsMap.put( key(scope,USER) , gitUserToken.getUser());
    	credentialsMap.put( key(scope,TOKEN), gitUserToken.getToken());
    }

    //--------------------------------------------------------------------
    // REMOVE
    //--------------------------------------------------------------------
    protected final void removeCredentialsForScope(String scope) {
    	credentialsMap.remove( key(scope,USER)  );
    	credentialsMap.remove( key(scope,TOKEN) );
    }
    
    private String key(String scope, String what) {
    	return scope + "." + what ;
    }
}
