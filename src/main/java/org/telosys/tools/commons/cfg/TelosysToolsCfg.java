/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.commons.cfg;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.env.TelosysToolsEnv;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.commons.variables.VariablesNames;
import org.telosys.tools.commons.variables.VariablesUtil;

/**
 * Telosys Tools project configuration <br>
 * This class provides the configuration loaded from the properties file <br>
 * for a project of the current workspace
 *   
 * @author Laurent GUERIN
 *
 */
public class TelosysToolsCfg 
{
// v 3.0.0
//	private final static String     DATABASES_DBCFG_FILE = "databases.dbcfg";  
	
// v 3.0.0
//    //--- Properties Names for directories 
//	private final static String REPOS_FOLDER      = "RepositoriesFolder";
//	private final static String TEMPLATES_FOLDER  = "TemplatesFolder";
//	private final static String DOWNLOADS_FOLDER  = "DownloadsFolder";    
//	private final static String LIBRARIES_FOLDER  = "LibrariesFolder";
	private final static String SPECIFIC_DESTINATION_FOLDER  = "SpecificDestinationFolder";
    
	//----------------------------------------------------------------------------------------
	private final String     _projectAbsolutePath ; 
	private final String     _cfgFileAbsolutePath ; 
	private final boolean    _initializedFromFile ; 
	//private final Properties _cfgFileProperties ; // removed in v 3.0.0

	//----------------------------------------------------------------------------------------
	//--- Project folders default values
	private final String _sModelsFolder ; 
	private final String _sTemplatesFolder    ; 
	private final String _sDownloadsFolder    ; 
	private final String _sLibrariesFolder    ; 
	
	//----------------------------------------------------------------------------------------
	//--- Standard variables : packages default values
	private String _ENTITY_PKG = "org.demo.bean" ;
	private String _ROOT_PKG   = "org.demo" ;

	//--- Standard variables : folders default values
	private String _SRC      =  "src/main/java" ;
	private String _RES      =  "src/main/resources" ;
	private String _WEB      =  "src/main/webapp" ;
	private String _TEST_SRC =  "src/test/java" ;
	private String _TEST_RES =  "src/test/resources" ;
	private String _DOC      =  "doc" ;
	private String _TMP      =  "tmp" ;
	
	private String _specificDestinationFolder = "" ;  // v 3.0.0
	
	//----------------------------------------------------------------------------------------
	//--- Specific variables defined by the user for the current project
	//private Variable[] _specificVariables = new Variable[0] ; // 
	private LinkedList<Variable> _specificVariables = new LinkedList<Variable>() ; // v 3.0.0

	//----------------------------------------------------------------------------------------
    /**
     * Constructor 
     * @param projectAbsolutePath the project directory (full path) 
     * @param cfgFileAbsolutePath the configuration file (full path)
     * @param prop the project configuration properties (if null default values will be used) 
     */
    //public TelosysToolsCfg ( String projectAbsolutePath, String cfgFileAbsolutePath, Properties prop )
    protected TelosysToolsCfg ( String projectAbsolutePath, String cfgFileAbsolutePath, Properties prop ) // v 3.0.0
    {
    	// v 3.0.0
    	TelosysToolsEnv env = TelosysToolsEnv.getInstance();
    	_sModelsFolder    = env.getModelsFolder() ; 
    	_sTemplatesFolder = env.getTemplatesFolder() ; 
    	_sDownloadsFolder = env.getDownloadsFolder() ; 
    	_sLibrariesFolder = env.getLibrariesFolder() ;
    	
    	if ( projectAbsolutePath == null ) {
    		throw new IllegalArgumentException("projectAbsolutePath is null");
    	}
    	if ( cfgFileAbsolutePath == null ) {
    		throw new IllegalArgumentException("cfgFileAbsolutePath is null");
    	}
    	_projectAbsolutePath = projectAbsolutePath ;
    	_cfgFileAbsolutePath = cfgFileAbsolutePath ;
    	
    	_initializedFromFile = initFromProperties(prop);
    	// _cfgFileProperties = prop ; // removed in v 3.0.0
    }
    
// removed in v 3.0.0
//    /**
//     * Constructor 
//     * @param projectAbsolutePath the project directory (full path) 
//     */
//    public TelosysToolsCfg ( String projectAbsolutePath )
//    {
//    	if ( projectAbsolutePath == null ) {
//    		throw new IllegalArgumentException("projectAbsolutePath is null");
//    	}
//    	_projectAbsolutePath = projectAbsolutePath ;
//    	
//    	TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectAbsolutePath) ;
//    	_cfgFileAbsolutePath = cfgManager.getCfgFileAbsolutePath();
//    	
//    	_initializedFromFile = false ;
//    	//_cfgFileProperties = null ; // removed in v 3.0.0
//    }
    
	//------------------------------------------------------------------------------------------------------
    protected boolean initFromProperties(Properties prop)
	{
    	if ( prop != null ) {    	
    		// v 3.0.0
//	    	// Initialization with the given properties, use original values as default values
//	    	_sRepositoriesFolder = prop.getProperty(REPOS_FOLDER,     _sRepositoriesFolder);
//	    	_sTemplatesFolder    = prop.getProperty(TEMPLATES_FOLDER, _sTemplatesFolder);
//	    	_sDownloadsFolder    = prop.getProperty(DOWNLOADS_FOLDER, _sDownloadsFolder);
//	    	_sLibrariesFolder    = prop.getProperty(LIBRARIES_FOLDER, _sLibrariesFolder);
	    	_specificDestinationFolder = prop.getProperty(SPECIFIC_DESTINATION_FOLDER, _specificDestinationFolder);
	    	
	    	//--- Packages 
	    	_ROOT_PKG   = prop.getProperty(VariablesNames.ROOT_PKG,   _ROOT_PKG);
	    	_ENTITY_PKG = prop.getProperty(VariablesNames.ENTITY_PKG, _ENTITY_PKG);
	
	    	//--- Folders  
	    	_SRC      =  prop.getProperty(VariablesNames.SRC,      _SRC);
	    	_RES      =  prop.getProperty(VariablesNames.RES,      _RES);
	    	_WEB      =  prop.getProperty(VariablesNames.WEB,      _WEB);
	    	_TEST_SRC =  prop.getProperty(VariablesNames.TEST_SRC, _TEST_SRC);
	    	_TEST_RES =  prop.getProperty(VariablesNames.TEST_RES, _TEST_RES);
	    	_DOC      =  prop.getProperty(VariablesNames.DOC,      _DOC);
	    	_TMP      =  prop.getProperty(VariablesNames.TMP,      _TMP);
	    	
	    	//--- Project user defined variables
	    	_specificVariables = VariablesUtil.getSpecificVariablesFromProperties( prop );
	    	//_allVariables      = VariablesUtil.getAllVariablesFromProperties(prop); 
	    	return true ;
    	}
    	else {
    		//--- Keep the default values 
	    	//--- No specific variables
	    	return false ; // No properties file
    	}
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns true if the current configuration has been initialized from an existing properties file
     * @return
     */
    public boolean hasBeenInitializedFromFile() {
    	return _initializedFromFile ;
    }
	
    //------------------------------------------------------------------------------------------------------
    protected Variable[] getDefaultSpecificVariables()
	{
    	Variable[] v = new Variable[4] ;
    	int i = 0 ;
    	//-- In alphabetic order
    	v[i++] = new Variable("MAVEN_ARTIFACT_ID", "artifact-to-be-defined"); // for pom.xml artifactId
    	v[i++] = new Variable("MAVEN_GROUP_ID",    "group.to.be.defined" ); // for pom.xml artifactId
    	v[i++] = new Variable("PROJECT_NAME",      "myproject");
    	v[i++] = new Variable("PROJECT_VERSION",   "0.1");
    	return v ;
	}
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns a set of properties containing the current configuration
     * @return
     */
    public Properties getProperties()
	{
//    	Properties properties = null ; 
//    	// v 2.1.1 (keep original properties if any and update)
//    	if ( this._cfgFileProperties != null ) { 
//    		// We have original properties => keep them
//        	properties = this._cfgFileProperties ; 
//    	}
//    	else {
//    		properties = new Properties(); 
//    	}
    	Properties properties = new Properties();  // v 3.0.0
    	
//    	//--- General 
//    	properties.setProperty(REPOS_FOLDER,     _sRepositoriesFolder);
//    	properties.setProperty(TEMPLATES_FOLDER, _sTemplatesFolder);
//    	properties.setProperty(DOWNLOADS_FOLDER, _sDownloadsFolder);
//    	properties.setProperty(LIBRARIES_FOLDER, _sLibrariesFolder);
    	properties.setProperty(SPECIFIC_DESTINATION_FOLDER, _specificDestinationFolder );
    	
    	//--- Packages 
    	properties.setProperty(VariablesNames.ROOT_PKG,   _ROOT_PKG);
    	properties.setProperty(VariablesNames.ENTITY_PKG, _ENTITY_PKG);

    	//--- Folders  
    	properties.setProperty(VariablesNames.SRC,      _SRC);
    	properties.setProperty(VariablesNames.RES,      _RES);
    	properties.setProperty(VariablesNames.WEB,      _WEB);
    	properties.setProperty(VariablesNames.TEST_SRC, _TEST_SRC);
    	properties.setProperty(VariablesNames.TEST_RES, _TEST_RES);
    	properties.setProperty(VariablesNames.DOC,      _DOC);
    	properties.setProperty(VariablesNames.TMP,      _TMP);

    	//--- Variables  
    	VariablesUtil.putSpecificVariablesInProperties(_specificVariables, properties);
    	
    	return properties ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the file system project folder (absolute path)
     * @return
     */
    public String getProjectAbsolutePath()
	{
    	return _projectAbsolutePath ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the destination folder for code generation (absolute path) <br>
     * By default the destination folder is the project folder <br>
     * If a specific destination as been defined it replaces the project folder <br>
     * @return
     */
    public String getDestinationFolderAbsolutePath()
	{
    	if ( StrUtil.nullOrVoid( _specificDestinationFolder ) ) {
    		//--- No specific destination folder => use the project folder
    		return _projectAbsolutePath;
    	}
    	else {
    		return _specificDestinationFolder ;
    	}
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the absolute file name of the configuration file 
     * @return
     */
    public String getCfgFileAbsolutePath()
	{
    	return _cfgFileAbsolutePath;
	}

    //==============================================================================
    // "databases.dbcfg" file  
    //==============================================================================
    /**
     * Returns the "databases.dbcfg" in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools/databases.dbcfg' )
     * @return
     */
    public String getDatabasesDbCfgFile() {
    	//return FileUtil.buildFilePath(getRepositoriesFolder(), DATABASES_DBCFG_FILE);
    	// v 3.0.0
    	return FileUtil.buildFilePath(getModelsFolder(), TelosysToolsEnv.getInstance().getDatabasesDbCfgFileName() );
	}
    /**
     * Returns the absolute file name of the "databases.dbcfg" file 
     * ( e.g. 'X:/dir/myproject/TelosysTools/databases.dbcfg' )
     * @return
     */
    public String getDatabasesDbCfgFileAbsolutePath()
	{
    	//return FileUtil.buildFilePath(getRepositoriesFolderAbsolutePath(), DATABASES_DBCFG_FILE);
    	// v 3.0.0
    	return FileUtil.buildFilePath(getModelsFolderAbsolutePath(), TelosysToolsEnv.getInstance().getDatabasesDbCfgFileName() );
	}

    //==============================================================================
    // Folders 
    //==============================================================================
    /**
     * Returns the "source folder" ( $SRC predefined variable )
     * @return
     */
    public String getSRC() {
    	return _SRC;
	}
    /**
     * Set the "source folder" ( $SRC predefined variable )
     * @param srcFolder
     */
    public void setSRC(String srcFolder) {
    	_SRC = srcFolder ;
	}
    
	//--------------------------------------------------------------------------------
    /**
     * Returns the "resources folder" ( $RES predefined variable )
     * @return
     */
    public String getRES() {
    	return _RES;
	}
    /**
     * Set the "resources folder" ( $RES predefined variable )
     * @param resourcesFolder
     */
    public void setRES(String resourcesFolder) {
    	_RES = resourcesFolder ;
	}
    
	//--------------------------------------------------------------------------------
    /**
     * Returns the "web folder" ( $WEB predefined variable )
     * @return
     */
    public String getWEB() {
    	return _WEB;
	}
    /**
     * Set the "web folder" ( $WEB predefined variable )
     * @param webFolder
     */
    public void setWEB(String webFolder) {
    	_WEB = webFolder ;
	}
    
	//--------------------------------------------------------------------------------
    /**
     * Returns the "test resources folder" ( $TEST_RES predefined variable )
     * @return
     */
    public String getTEST_RES() {
    	return _TEST_RES;
	}
    /**
     * Set the "test resources folder" ( $TEST_RES predefined variable )
     * @param testResourcesFolder
     */
    public void setTEST_RES(String testResourcesFolder) {
    	_TEST_RES = testResourcesFolder ;
	}
    
	//--------------------------------------------------------------------------------
    /**
     * Returns the "test source folder" ( $TEST_SRC predefined variable )
     * @return
     */
    public String getTEST_SRC() {
    	return _TEST_SRC;
	}
    /**
     * Set the "test source folder" ( $TEST_SRC predefined variable )
     * @param testSourceFolder
     */
    public void setTEST_SRC(String testSourceFolder) {
    	_TEST_SRC = testSourceFolder ;
	}
    
	//--------------------------------------------------------------------------------
    /**
     * Returns the "documentation folder" ( $DOC predefined variable )
     * @return
     */
    public String getDOC(){
    	return _DOC;
	}
    /**
     * Set the "documentation folder" ( $DOC predefined variable )
     * @param docFolder
     */
    public void setDOC(String docFolder) {
    	_DOC = docFolder ;
	}
    
	//--------------------------------------------------------------------------------
    /**
     * Returns the "temporary folder" ( $TMP predefined variable )
     * @return
     */
    public String getTMP() {
    	return _TMP;
	}
    /**
     * Set the "temporary folder" ( $TMP predefined variable )
     * @param tmpFolder
     */
    public void setTMP(String tmpFolder) {
    	_TMP = tmpFolder ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the templates folder in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools/templates' )
     * @return
     */
    public String getTemplatesFolder() {
    	return _sTemplatesFolder;
	}
//    /**
//     * Set the templates folder in the current project (relative path in the project) <br>
//     * @param templatesFolder
//     */
//    public void setTemplatesFolder(String templatesFolder) {
//    	_sTemplatesFolder = templatesFolder ;
//	}
    /**
     * Returns the templates folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools/templates' )
     * @return
     */
    public String getTemplatesFolderAbsolutePath() {
    	return FileUtil.buildFilePath(_projectAbsolutePath, _sTemplatesFolder ) ;
	}

    /**
     * Returns the templates bundle folder absolute path for the given bundle name <br>
     * e.g. : <br>
     * 'X:/dir/myproject/TelosysTools/templates/mybundle' for 'mybundle' <br>
     * 'X:/dir/myproject/TelosysTools/templates' for '' or ' ' or null <br>
     * 
     * @param bundleName the bundle name (can be null or void)
     * @return
     */
    public String getTemplatesFolderAbsolutePath(String bundleName) {
    	String templatesFolderAbsolutePath = getTemplatesFolderAbsolutePath()  ;
		if ( StrUtil.nullOrVoid(bundleName) ) {
			// No current bundle => use the standard templates folder as is
			return templatesFolderAbsolutePath ;
		}
		else {
			// There's a bundle defined => use it
			return FileUtil.buildFilePath(templatesFolderAbsolutePath, bundleName.trim() );
		}
    	
	}

	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the downloads folder in the current project (relative path in the project)<br>
     * ( e.g. 'TelosysTools/downloads' )
     * @return
     */
    public String getDownloadsFolder() {
    	return _sDownloadsFolder;
    }
//    /**
//     * Set the downloads folder in the current project (relative path in the project)<br>
//     * @param downloadsFolder
//     */
//    public void setDownloadsFolder(String downloadsFolder) {
//    	_sDownloadsFolder = downloadsFolder ;
//    }
    /**
     * Returns the download folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools/downloads' )
     * @return
     */
    public String getDownloadsFolderAbsolutePath() {
    	return FileUtil.buildFilePath(_projectAbsolutePath, _sDownloadsFolder ) ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the libraries folder in the current project (relative path in the project)<br>
     * ( e.g. 'TelosysTools/lib' )
     * @return
     */
    public String getLibrariesFolder() {
    	return _sLibrariesFolder ;
    }
//    /**
//     * Set the libraries folder in the current project (relative path in the project)<br>
//     * @param librariesFolder
//     */
//    public void setLibrariesFolder(String librariesFolder) {
//    	_sLibrariesFolder = librariesFolder ;
//    }
    /**
     * Returns the download folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools/lib' )
     * @return
     */
    public String getLibrariesFolderAbsolutePath() {
    	return FileUtil.buildFilePath(_projectAbsolutePath, _sLibrariesFolder ) ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the models folder in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools' )
     * @return
     */
    public String getModelsFolder()
	{
    	return _sModelsFolder ;
	}
//    /**
//     * Set the repositories folder in the current project (relative path in the project) <br>
//     * @param repositoriesFolder
//     */
//    public void setRepositoriesFolder(String repositoriesFolder)
//	{
//    	_sRepositoriesFolder = repositoriesFolder ;
//	}
    /**
     * Returns the models folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools' )
     * @return
     */
    public String getModelsFolderAbsolutePath() {
    	return FileUtil.buildFilePath(_projectAbsolutePath, _sModelsFolder ) ;
	}
    
    //=======================================================================================================
    // Packages 
    //=======================================================================================================
	/**
	 * Returns the package for entity classes ( $ENTITY_PKG predefined variable ) <br>
	 * ( e.g. "org.demo.bean" )
	 * @return 
	 */
	public String getEntityPackage() 
	{
		return _ENTITY_PKG ;
	}
	/**
	 * Returns the package for entity classes ( $ENTITY_PKG predefined variable ) <br>
	 * ( e.g. "org.demo.bean" )
	 * @param entityPackage
	 */
	public void setEntityPackage(String entityPackage) 
	{
		_ENTITY_PKG = entityPackage ;
	}
	
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * Returns the root package ( $ROOT_PKG predefined variable ) <br> 
	 * ( e.g. "org.demo" )
	 * @return 
	 */
	public String getRootPackage() 
	{
		return _ROOT_PKG ;
	}
	/**
	 * Set the root package ( $ROOT_PKG predefined variable )  <br> 
	 * ( e.g. "org.demo" )
	 * @param rootPackage
	 */
	public void setRootPackage(String rootPackage) 
	{
		_ROOT_PKG = rootPackage ;
	}
	
    //=======================================================================================================
    // Specific destination folder  ( since v 3.0.0 )
    //=======================================================================================================
	/**
	 * Returns the specific destination folder for code generation <br>
	 * or void if not defined <br> 
	 * ( e.g. "X:/foo/bar/myfolder" or "" if not set )
	 * @return 
	 */
	public String getSpecificDestinationFolder() 
	{
		return _specificDestinationFolder ;
	}
	
	/**
	 * Set the specific destination folder for code generation <br>
	 * ( e.g. "X:/foo/bar/myfolder" or "" for not defined )
	 * @param rootPackage
	 */
	public void setSpecificDestinationFolder(String specificDestinationFolder) 
	{
		if ( specificDestinationFolder == null ) {
			_specificDestinationFolder = "" ;
		}
		else {
			_specificDestinationFolder = specificDestinationFolder.trim() ;
		}
	}
	
	
    //=======================================================================================================
    // Variables 
    //=======================================================================================================
	/**
	 * Returns the specific variables defined for the current project  
	 * @return array of variables (never null, void array if none)
	 */
	public Variable[] getSpecificVariables()
	{
		// return _specificVariables ;
		return _specificVariables.toArray(new Variable[0]); // v 3.0.0
	}
	
	/**
	 * Returns the specific variable for the given variable name 
	 * @param variableName
	 * @return the variable or null if the variable is not defined
	 */
	public Variable getSpecificVariable(String variableName) // v 3.0.0
	{
		for ( Variable var : _specificVariables ) {
			if ( var.getName().equals(variableName) ) {
				return var ;
			}
		}
		return null ;
	}
	
	/**
	 * Returns true if the current configuration has at least one specific variable
	 * @return
	 */
	public boolean hasSpecificVariables() {
		//if ( _specificVariables != null && _specificVariables.length > 0 ) {
		if ( _specificVariables != null && _specificVariables.size() > 0 ) { // v 3.0.0
			return true;
		}
		return false ; 
	}
	
// removed in v 3.0.0
//	/**
//	 * Set the specific variables defined for the current project  
//	 * @param variables
//	 */
//	public void setSpecificVariables(Variable[] variables)
//	{
//		if ( variables != null ) {
//			_specificVariables = variables ;
//		}
//		else {
//			_specificVariables = new Variable[0] ;
//		}
//	}	
	/**
	 * Set the specific variables defined for the current project  
	 * @param variables
	 */
	public void setSpecificVariables(List<Variable> variables ) {
		// v 3.0.0 : from Array to LinkedList
		if ( variables != null ) {
			//_specificVariables = new Variable[variables.size()] ;
			_specificVariables = new LinkedList<Variable>() ;
			//int i = 0 ;
			for ( Variable var : variables ) {
				//_specificVariables[i++] = var ;
				_specificVariables.add(var);
			}
		}
		else {
			//_specificVariables = new Variable[0] ;
			_specificVariables = new LinkedList<Variable>() ;
		}
	}	
	//------------------------------------------------------------------------------------------------------
	public void setSpecificVariable(Variable variable) {
		
		for ( int index = 0 ; index < _specificVariables.size() ; index++ ) {
			Variable var = _specificVariables.get(index);
			if ( var.getName().equals(variable.getName()) ) {
				// found => update with new variable
				_specificVariables.set(index, variable);
				return ;
			}
		}
		// not found => add
		_specificVariables.add(variable);
	}	
	//------------------------------------------------------------------------------------------------------
	/**
	 * Returns all the variables defined for the current project <br>
	 * (standard variables + specific variables )
	 * @return array of variables (never null, void array if none)
	 */
	public Variable[] getAllVariables()
	{
		//return _allVariables ;
		return buildAllVariablesArray() ;
	}	
	
	//------------------------------------------------------------------------------------------------------
	private Variable[] buildAllVariablesArray() {
    	//--- All variables : specific project variables + predefined variables 
    	Hashtable<String, String> allVariables = new Hashtable<String, String>();
    	
    	//--- 1) Project specific variables (defined by user)
    	for ( Variable v : _specificVariables ) {
    		allVariables.put(v.getName(), v.getValue());
    	}
    	
    	//--- 2) Predefined variables ( Packages, folders) at the end to override specific variables if any 
    	allVariables.put( VariablesNames.ROOT_PKG,   _ROOT_PKG ); // v 2.0.6
    	allVariables.put( VariablesNames.ENTITY_PKG, _ENTITY_PKG ); // v 2.0.6
    	
    	allVariables.put( VariablesNames.SRC,      _SRC      );
    	allVariables.put( VariablesNames.RES,      _RES      );
    	allVariables.put( VariablesNames.WEB,      _WEB      );
    	allVariables.put( VariablesNames.TEST_SRC, _TEST_SRC );
    	allVariables.put( VariablesNames.TEST_RES, _TEST_RES );
    	allVariables.put( VariablesNames.DOC,      _DOC      );
    	allVariables.put( VariablesNames.TMP,      _TMP      );
    	
    	//--- 3) Get all variables to build the array
//    	LinkedList<Variable> variablesList = new LinkedList<Variable>();
//    	for ( String varName : allVariables.keySet() ) {
//    		String varValue = allVariables.get(varName) ;
//    		variablesList.add( new Variable( varName, varValue) ) ;
//    	}
//    	//--- Convert list to array
//    	Variable[] allVariablesArray = variablesList.toArray( new Variable[variablesList.size()] );
    	
    	Variable[] allVariablesArray = new Variable[ allVariables.size() ];
    	int i = 0 ;
    	for ( Map.Entry<String, String> entry : allVariables.entrySet() ) {
    		allVariablesArray[i++] = new Variable( entry.getKey(), entry.getValue() ) ;
    	}
		return allVariablesArray ;
	}
}
