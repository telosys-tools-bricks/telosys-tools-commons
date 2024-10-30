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
package org.telosys.tools.commons.cfg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.env.TelosysToolsEnv;
import org.telosys.tools.commons.logger.ConsoleLogger;
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
    
	//----------------------------------------------------------------------------------------
	private final String     projectAbsolutePath ; 
	private final String     cfgFileAbsolutePath ; 
	private final boolean    initializedFromFile ; 

	//----------------------------------------------------------------------------------------
	//--- Project folders standard/default values
	private final String telosysToolsFolder ;
	private final String modelsFolder ; 
	private final String templatesFolder ; 
	private final String downloadsFolder ; 
	private final String librariesFolder ; 
	private static final String STANDARD_DEPOT_FOR_MODELS  = "github_org:telosys-models";    // v 4.2.0
	private static final String STANDARD_DEPOT_FOR_BUNDLES = "github_org:telosys-templates"; // v 4.2.0
	
	//--- Project specific values
	private static final String PROPERTY_SPECIFIC_DESTINATION_FOLDER  = "SpecificDestinationFolder";
	private String specificDestinationFolder = "" ;  // v 3.0.0

	private static final String PROPERTY_SPECIFIC_TEMPLATES_FOLDER    = "SpecificTemplatesFolder";
	private String specificTemplatesFolder   = "" ;  // v 3.0.0
	
	private static final String PROPERTY_SPECIFIC_MODELS_FOLDER       = "SpecificModelsFolder"; // V 3.4.0
	private String specificModelsFolder = "" ;  // v 3.4.0
	
	private static final String PROPERTY_SPECIFIC_DEPOT_FOR_MODELS = "SpecificDepotForModels"; // 4.2.0
	private String specificDepotForModels = ""; // v 4.2.0

	private static final String PROPERTY_SPECIFIC_DEPOT_FOR_BUNDLES = "SpecificDepotForBundles"; // 4.2.0
	private String specificDepotForBundles = ""; // v 4.2.0

	//----------------------------------------------------------------------------------------
	// Variables
	//----------------------------------------------------------------------------------------
	//--- Standard variables : packages default values
	private String varEntityPkg = "org.demo.bean" ;
	private String varRootPkg   = "org.demo" ;

	//--- Standard variables : folders default values
	private String varSrc     =  "src/main/java" ;
	private String varRes     =  "src/main/resources" ;
	private String varWeb     =  "src/main/webapp" ;
	private String varTestSrc =  "src/test/java" ;
	private String varTestRes =  "src/test/resources" ;
	private String varDoc     =  "doc" ;
	private String varTmp     =  "tmp" ;
	
	//--- Specific variables defined by the user for the current project
	private List<Variable> specificVariables = new LinkedList<>() ; // v 3.0.0

	//----------------------------------------------------------------------------------------
    /**
     * Constructor 
     * @param projectAbsolutePath the project directory (full path) 
     * @param cfgFileAbsolutePath the configuration file (full path)
     * @param prop the project configuration properties (if null default values will be used) 
     */
    protected TelosysToolsCfg ( String projectAbsolutePath, String cfgFileAbsolutePath, Properties prop ) { // v 3.0.0
    	this.telosysToolsFolder = TelosysToolsEnv.getTelosysToolsFolder();
    	this.modelsFolder       = TelosysToolsEnv.getModelsFolder() ; // "TelosysTools/models" since v 3.4.0
    	this.templatesFolder    = TelosysToolsEnv.getTemplatesFolder() ; 
    	this.downloadsFolder    = TelosysToolsEnv.getDownloadsFolder() ; 
    	this.librariesFolder    = TelosysToolsEnv.getLibrariesFolder() ;
    	
    	if ( projectAbsolutePath == null ) {
    		throw new IllegalArgumentException("projectAbsolutePath is null");
    	}
    	if ( cfgFileAbsolutePath == null ) {
    		throw new IllegalArgumentException("cfgFileAbsolutePath is null");
    	}
    	this.projectAbsolutePath = projectAbsolutePath ;
    	this.cfgFileAbsolutePath = cfgFileAbsolutePath ;
    	
    	this.initializedFromFile = initFromProperties(prop);
    }
    
	//------------------------------------------------------------------------------------------------------
    protected boolean initFromProperties(Properties prop) {
    	if ( prop != null ) {    	
	    	specificDestinationFolder = prop.getProperty(PROPERTY_SPECIFIC_DESTINATION_FOLDER, specificDestinationFolder);
	    	specificTemplatesFolder   = prop.getProperty(PROPERTY_SPECIFIC_TEMPLATES_FOLDER, specificTemplatesFolder);
	    	specificModelsFolder      = prop.getProperty(PROPERTY_SPECIFIC_MODELS_FOLDER, specificModelsFolder); // v 3.4.0

	    	specificDepotForModels  = prop.getProperty(PROPERTY_SPECIFIC_DEPOT_FOR_MODELS,  specificDepotForModels ); // v 4.2.0
	    	specificDepotForBundles = prop.getProperty(PROPERTY_SPECIFIC_DEPOT_FOR_BUNDLES, specificDepotForBundles); // v 4.2.0
	    	
	    	//--- Packages 
	    	varRootPkg   = prop.getProperty(VariablesNames.ROOT_PKG,   varRootPkg);
	    	varEntityPkg = prop.getProperty(VariablesNames.ENTITY_PKG, varEntityPkg);
	
	    	//--- Folders  
	    	varSrc     =  prop.getProperty(VariablesNames.SRC,      varSrc);
	    	varRes     =  prop.getProperty(VariablesNames.RES,      varRes);
	    	varWeb     =  prop.getProperty(VariablesNames.WEB,      varWeb);
	    	varTestSrc =  prop.getProperty(VariablesNames.TEST_SRC, varTestSrc);
	    	varTestRes =  prop.getProperty(VariablesNames.TEST_RES, varTestRes);
	    	varDoc     =  prop.getProperty(VariablesNames.DOC,      varDoc);
	    	varTmp     =  prop.getProperty(VariablesNames.TMP,      varTmp);
	    	
	    	//--- Project user defined variables
	    	specificVariables = VariablesUtil.getSpecificVariablesFromProperties( prop );
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
    protected boolean hasBeenInitializedFromFile() {
    	return initializedFromFile ;
    }

	//------------------------------------------------------------------------------------------------------
    /**
     * Returns a set of properties containing the current configuration
     * @return
     */
    public Properties getProperties() {
    	Properties properties = new Properties();  // v 3.0.0
    	
    	properties.setProperty(PROPERTY_SPECIFIC_DESTINATION_FOLDER, specificDestinationFolder );
    	properties.setProperty(PROPERTY_SPECIFIC_TEMPLATES_FOLDER,   specificTemplatesFolder );
    	properties.setProperty(PROPERTY_SPECIFIC_MODELS_FOLDER,      specificModelsFolder ); // v 3.4.0

    	properties.setProperty(PROPERTY_SPECIFIC_DEPOT_FOR_MODELS,  specificDepotForModels );  // v 4.2.0
    	properties.setProperty(PROPERTY_SPECIFIC_DEPOT_FOR_BUNDLES, specificDepotForBundles ); // v 4.2.0
    	
    	//--- Packages 
    	properties.setProperty(VariablesNames.ROOT_PKG,   varRootPkg);
    	properties.setProperty(VariablesNames.ENTITY_PKG, varEntityPkg);

    	//--- Folders  
    	properties.setProperty(VariablesNames.SRC,      varSrc);
    	properties.setProperty(VariablesNames.RES,      varRes);
    	properties.setProperty(VariablesNames.WEB,      varWeb);
    	properties.setProperty(VariablesNames.TEST_SRC, varTestSrc);
    	properties.setProperty(VariablesNames.TEST_RES, varTestRes);
    	properties.setProperty(VariablesNames.DOC,      varDoc);
    	properties.setProperty(VariablesNames.TMP,      varTmp);

    	//--- Variables  
    	VariablesUtil.putSpecificVariablesInProperties(specificVariables, properties);
    	
    	return properties ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the file system project folder (absolute path)
     * @return
     */
    public String getProjectAbsolutePath() {
    	return projectAbsolutePath ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the standard logger
     * @return
     */
    public TelosysToolsLogger getLogger() {
    	return new ConsoleLogger()  ; // To be replaced by the future "FileLogger" ( log files in "log" folder )
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the destination folder for code generation (absolute path) <br>
     * By default the destination folder is the project folder <br>
     * If a specific destination as been defined it replaces the project folder <br>
     * @return
     */
    public String getDestinationFolderAbsolutePath() {
    	if ( hasSpecificDestinationFolder() ) {
    		return specificDestinationFolder ;
    	}
    	else {
    		//--- No specific destination folder => use the project folder
    		return projectAbsolutePath;
    	}
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the absolute file name of the configuration file 
     * @return
     */
    public String getCfgFileAbsolutePath() {
    	return cfgFileAbsolutePath;
	}

    //==============================================================================
    // "databases.yaml" file  
    //==============================================================================
    /**
     * Returns the "databases.yaml" in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools/databases.yaml' )
     * @return
     */
    public String getDatabasesDbCfgFile() {
    	// v 3.4.0 
    	return FileUtil.buildFilePath(getTelosysToolsFolder(), TelosysToolsEnv.getDatabasesDbCfgFileName() );
	}
    /**
     * Returns the absolute file name of the "databases.yaml" file 
     * ( e.g. 'X:/dir/myproject/TelosysTools/databases.yaml' )
     * @return
     */
    public String getDatabasesDbCfgFileAbsolutePath() {
    	// v 3.4.0 
    	return FileUtil.buildFilePath(getTelosysToolsFolderAbsolutePath(), TelosysToolsEnv.getDatabasesDbCfgFileName() );
	}

    //==============================================================================
    // Folders 
    //==============================================================================
    /**
     * Returns the "source folder" ( $SRC predefined variable )
     * @return
     */
    public String getSRC() {
    	return varSrc;
	}
    
    /**
     * Returns the "resources folder" ( $RES predefined variable )
     * @return
     */
    public String getRES() {
    	return varRes;
	}
    
    /**
     * Returns the "web folder" ( $WEB predefined variable )
     * @return
     */
    public String getWEB() {
    	return varWeb;
	}
    
    /**
     * Returns the "test resources folder" ( $TEST_RES predefined variable )
     * @return
     */
    public String getTEST_RES() {
    	return varTestRes;
	}
    
    /**
     * Returns the "test source folder" ( $TEST_SRC predefined variable )
     * @return
     */
    public String getTEST_SRC() {
    	return varTestSrc;
	}
    
    /**
     * Returns the "documentation folder" ( $DOC predefined variable )
     * @return
     */
    public String getDOC(){
    	return varDoc;
	}
    
    /**
     * Returns the "temporary folder" ( $TMP predefined variable )
     * @return
     */
    public String getTMP() {
    	return varTmp;
	}
    
    /**
     * Returns the 'TelosysTools' folder in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools' )
     * @return
     */
    public String getTelosysToolsFolder() {
    	return telosysToolsFolder;
	}
    /**
     * Returns the 'TelosysTools' folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools' )
     * @return
     */
    public String getTelosysToolsFolderAbsolutePath() {
    	return FileUtil.buildFilePath(projectAbsolutePath, telosysToolsFolder ) ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the templates folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools/templates' )
     * @return
     */
    public String getTemplatesFolderAbsolutePath() {
    	if ( this.hasSpecificTemplatesFolders() ) {
    		//--- Specific templates folder => use it  ( v 3.0.0 )
    		return specificTemplatesFolder ; 
    	}
    	else {
    		//--- Default templates location (in the current project)
    		return FileUtil.buildFilePath(projectAbsolutePath, templatesFolder ) ;
    	}    	
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
    	return downloadsFolder;
    }

    /**
     * Returns the download folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools/downloads' )
     * @return
     */
    public String getDownloadsFolderAbsolutePath() {
    	return FileUtil.buildFilePath(projectAbsolutePath, downloadsFolder ) ;
	}
    
	//------------------------------------------------------------------------------------------------------
    /**
     * Returns the libraries folder in the current project (relative path in the project)<br>
     * ( e.g. 'TelosysTools/lib' )
     * @return
     */
    public String getLibrariesFolder() {
    	return librariesFolder ;
    }

    /**
     * Returns the download folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools/lib' )
     * @return
     */
    public String getLibrariesFolderAbsolutePath() {
    	return FileUtil.buildFilePath(projectAbsolutePath, librariesFolder ) ;
	}
    
	//------------------------------------------------------------------------------------------------------
    //--- Depot Name For Bundles (standard or specific)
	/**
	 * Returns true if a specific depot name for bundles of templates has been defined in config file
	 * @return
	 * @since 4.2.0
	 */
	protected boolean hasSpecificDepotForBundles() { 
		return ! StrUtil.nullOrVoid( specificDepotForBundles ) ;
	}
	
    /**
     * Returns the current depot name for bundles of templates (standard or specific if defined in config file)
     * @return
	 * @since 4.2.0
     */
    public String getDepotForBundles() {
    	if ( this.hasSpecificDepotForBundles() ) {
    		return specificDepotForBundles;
    	}
    	else {
    		return STANDARD_DEPOT_FOR_BUNDLES ;
    	}    	
    }
    
	//------------------------------------------------------------------------------------------------------
    //--- Depot Name For Models (standard or specific)
	/**
	 * Returns true if a specific depot name for models has been defined in config file
	 * @return
	 * @since 4.2.0
	 */
	protected boolean hasSpecificDepotForModels() { 
		return ! StrUtil.nullOrVoid( specificDepotForModels ) ;
	}
	
    /**
     * Returns the current depot name for models (standard or specific if defined in config file)
     * @return
	 * @since 4.2.0
     */
    public String getDepotForModels() {
    	if ( this.hasSpecificDepotForModels() ) {
    		return specificDepotForModels;
    	}
    	else {
    		return STANDARD_DEPOT_FOR_MODELS ;
    	}    	
    }
    
    /**
     * Returns the models folder absolute path <br>
     * ( e.g. 'X:/dir/myproject/TelosysTools/models' )
     * @return
     */
    public String getModelsFolderAbsolutePath() { 
    	if ( this.hasSpecificModelsFolders() ) {
    		//--- Specific templates folder => use it as is (v 3.4.0 )
    		return specificModelsFolder ; 
    	}
    	else {
    		//--- Default templates location (in the current project)
    		return FileUtil.buildFilePath(projectAbsolutePath, modelsFolder ) ;
    	}    	
	}

    /**
     * Returns the model folder absolute path for the given model name <br>
     * e.g. : <br>
     * 'X:/dir/myproject/TelosysTools/models/mymodel' for 'mymodel' <br>
     * 'X:/dir/myproject/TelosysTools/models' if model name is null or empty <br>
     * @param modelName
     * @return
     */
    public String getModelFolderAbsolutePath(String modelName) {
    	String modelsFolderAbsolutePath = getModelsFolderAbsolutePath();
		if ( StrUtil.nullOrVoid(modelName) ) {
			// No current model => use the standard templates folder as is
			return modelsFolderAbsolutePath ;
		}
		else {
			// There's a bundle defined => use it
			return FileUtil.buildFilePath(modelsFolderAbsolutePath, modelName.trim());
		}
    }
    
    //=======================================================================================================
    // Packages 
    //=======================================================================================================
	/**
	 * Returns the package for entity classes ( $ENTITY_PKG predefined variable ) <br>
	 * ( e.g. "org.demo.bean" )
	 * @return 
	 */
	public String getEntityPackage() {
		return varEntityPkg ;
	}
	protected void setEntityPackage(String entityPackage)  {
		varEntityPkg = entityPackage ;
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * Returns the root package ( $ROOT_PKG predefined variable ) <br> 
	 * ( e.g. "org.demo" )
	 * @return 
	 */
	public String getRootPackage() {
		return varRootPkg ;
	}
	
    //=======================================================================================================
    // Specific destination folder  ( since v 3.0.0 )
    //=======================================================================================================
	/**
	 * Returns the specific destination folder for code generation <br>
	 * or void if not defined <br> 
	 * ( e.g. "X:/foo/bar/myfolder" or "" if not set )
	 * @return 
     * @since v 3.0.0
	 */
	public String getSpecificDestinationFolder() {
		return specificDestinationFolder ;
	}
	
	/**
	 * Set the specific destination folder for code generation <br>
	 * ( e.g. "X:/foo/bar/myfolder" or "" for not defined )
	 * @param rootPackage
     * @since v 3.0.0
	 */
	public void setSpecificDestinationFolder(String newValue) {
		if ( newValue == null ) {
			specificDestinationFolder = "" ;
		}
		else {
			specificDestinationFolder = newValue.trim() ;
		}
	}
	
    //=======================================================================================================
    // Specific templates folder  ( since v 3.0.0 )
    //=======================================================================================================
    /**
     * Returns the specific templates folder (absolute path) <br>
     * @return
     * @since v 3.0.0
     */
    public String getSpecificTemplatesFolderAbsolutePath() {
    	return specificTemplatesFolder ;
    }
    
    /**
     * Returns the specific models folder (absolute path) <br>
     * @return
     * @since v 3.4.0
     */
    public String getSpecificModelsFolderAbsolutePath() {
    	return specificModelsFolder;
    }
    
	/**
	 * For tests only
	 * @param newValue
	 */
	protected void setSpecificTemplatesFolderAbsolutePath(String newValue) {
		if ( newValue == null ) {
			specificTemplatesFolder = "" ;
		}
		else {
			specificTemplatesFolder = newValue.trim() ;
		}
	}
	
	/**
	 * Returns true if a specific templates folder has been defined (false if none)
	 * @return
     * @since v 3.0.0
	 */
	protected boolean hasSpecificTemplatesFolders() {
		return ! StrUtil.nullOrVoid( specificTemplatesFolder ) ;
	}
	
	/**
	 * Returns true if a specific models folder has been defined (false if none)
	 * @return
     * @since v 3.4.0
	 */
	protected boolean hasSpecificModelsFolders() {
		return ! StrUtil.nullOrVoid( specificModelsFolder ) ;
	}
	
	/**
	 * Returns true if a specific destination folder has been defined (false if none)
	 * @return
     * @since v 3.0.0
	 */
	protected boolean hasSpecificDestinationFolder()  {
		return ! StrUtil.nullOrVoid( specificDestinationFolder ) ;
	}
    //=======================================================================================================
    // Variables 
    //=======================================================================================================
	/**
	 * Returns the specific variables defined for the current project  
	 * @return array of variables (never null, void array if none)
	 */
	public Variable[] getSpecificVariables() {
		return specificVariables.toArray(new Variable[0]); // v 3.0.0
	}
	
	/**
	 * Returns the specific variable for the given variable name 
	 * @param variableName
	 * @return the variable or null if the variable is not defined
	 */
	public Variable getSpecificVariable(String variableName) { // v 3.0.0
		for ( Variable var : specificVariables ) { 
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
	protected boolean hasSpecificVariables() {
		return ! specificVariables.isEmpty();
	}
	
	//------------------------------------------------------------------------------------------------------
	public void setSpecificVariable(Variable variable) {
		
		for ( int index = 0 ; index < specificVariables.size() ; index++ ) {
			Variable var = specificVariables.get(index);
			if ( var.getName().equals(variable.getName()) ) {
				// found => update with new variable
				specificVariables.set(index, variable);
				return ;
			}
		}
		// not found => add
		specificVariables.add(variable);
	}	
	//------------------------------------------------------------------------------------------------------
	/**
	 * Returns all the variables defined for the current project <br>
	 * (standard variables + specific variables )
	 * @return array of variables (never null, void array if none)
	 */
	public Variable[] getAllVariables() {
		Map<String, String> allVariablesMap = getAllVariablesMap();
    	//--- Map to array
    	Variable[] allVariablesArray = new Variable[ allVariablesMap.size() ];
    	int i = 0 ;
    	for ( Map.Entry<String, String> entry : allVariablesMap.entrySet() ) {
    		allVariablesArray[i++] = new Variable( entry.getKey(), entry.getValue() ) ;
    	}
		return allVariablesArray ;
	}	
	
	/**
	 * Returns all the variables defined for the current project <br>
	 * (standard variables + specific variables )
	 * @return
	 */
	public Map<String, String> getAllVariablesMap() {
    	//--- All variables : specific project variables + predefined variables 
    	HashMap<String, String> allVariables = new HashMap<>();
    	
    	//--- 1) Project specific variables (defined by user)
    	for ( Variable v : specificVariables ) {
    		allVariables.put(v.getName(), v.getValue());
    	}
    	
    	//--- 2) Predefined variables ( Packages, folders) at the end to override specific variables if any 
    	allVariables.put( VariablesNames.ROOT_PKG,   varRootPkg ); // v 2.0.6
    	allVariables.put( VariablesNames.ENTITY_PKG, varEntityPkg ); // v 2.0.6
    	
    	allVariables.put( VariablesNames.SRC,      varSrc      );
    	allVariables.put( VariablesNames.RES,      varRes      );
    	allVariables.put( VariablesNames.WEB,      varWeb      );
    	allVariables.put( VariablesNames.TEST_SRC, varTestSrc );
    	allVariables.put( VariablesNames.TEST_RES, varTestRes );
    	allVariables.put( VariablesNames.DOC,      varDoc      );
    	allVariables.put( VariablesNames.TMP,      varTmp      );
    	return allVariables;
	}	
}
