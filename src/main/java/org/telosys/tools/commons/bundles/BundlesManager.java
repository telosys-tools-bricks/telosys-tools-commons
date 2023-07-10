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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.ZipUtil;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.github.GitHubClient;
import org.telosys.tools.commons.github.GitHubRateLimitResponse;
import org.telosys.tools.commons.github.GitHubRepositoriesResponse;
import org.telosys.tools.commons.github.GitHubRepository;

/**
 * Utility class for bundles management (including GitHub access)
 * 
 * @author Laurent GUERIN
 *
 */
public class BundlesManager {

	public static final String TEMPLATES_CFG = "templates.cfg" ;

	private final TelosysToolsCfg telosysToolsCfg ;
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Constructor 
	 * @param bundlesFolder bundles folder in the file system (full path)
	 */
	public BundlesManager(TelosysToolsCfg cfg) {
		super();
		if ( cfg == null ) throw new IllegalArgumentException("TelosysToolsCfg is null");
		this.telosysToolsCfg = cfg;
	}

	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns the DOWNLOADS folder's full path in the file system <br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/downloads' )
	 * @return
	 */
	public String getDownloadsFolderFullPath() {
		return telosysToolsCfg.getDownloadsFolderAbsolutePath() ;
	}
	
	/**
	 * Returns a File object for the project's bundles folder <br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/templates' )<br>
	 * There's no guarantee the returned file/directory exists
	 * @return
	 */
	public File getBundlesFolder() {
		return new File(telosysToolsCfg.getTemplatesFolderAbsolutePath()) ;
	}

	/**
	 * Returns a File object for the given bundle's folder <br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/templates/bundleName' )<br>
	 * There's no guarantee the returned file/directory exists
	 * @param bundleName
	 * @return
	 */
	public File getBundleFolder(String bundleName) {
		return new File(telosysToolsCfg.getTemplatesFolderAbsolutePath(bundleName)) ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns true if the bundle is already installed (if the bundle's folder exists)
	 * @param bundleName
	 * @return
	 */
	public boolean isBundleAlreadyInstalled( String bundleName ) {
		File bundleFolder = getBundleFolder(bundleName) ; // v 3.0.0
		return bundleFolder.exists();
	}
	
	/**
	 * Returns the 'templates.cfg' File object for the given bundle name <br>
	 * There's no guarantee the returned file exists
	 * @param bundleName
	 * @return the File instance
	 */
	public File getBundleConfigFile(String bundleName) {
//		File bundleFolder = getBundleFolder(bundleName) ;
//		return new File( FileUtil.buildFilePath(bundleFolder.getAbsolutePath(), TEMPLATES_CFG ) ) ;
		return getBundleConfigFile(getBundleFolder(bundleName)) ;
	}
	
	/**
	 * Returns the 'templates.cfg' File object for the given bundle folder <br>
	 * There's no guarantee the returned file exists
	 * @param bundleFolder
	 * @return
	 */
	public File getBundleConfigFile(File bundleFolder) {
		return new File( FileUtil.buildFilePath(bundleFolder.getAbsolutePath(), TEMPLATES_CFG ) ) ;
	}
	
	/**
	 * Returns true if the 'templates.cfg' file exists for the given bundle name
	 * @param bundleName
	 * @return
	 */
	public boolean isBundleConfigFileExists(String bundleName) {
		File file = getBundleConfigFile(bundleName);
		return ( file != null && file.exists() );
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Downloads a bundle (GitHub repo) in the downloads folder defined in the current configuration<br>
	 * e.g. downloads a zip file in 'TelosysTools/downloads'  
	 * @param userName the GitHub user name (e.g. "telosys-tools")
	 * @param bundleName the bundle name, in other words the GitHub repository name 
	 * @return
	 */
	public BundleStatus downloadBundle( String userName, String bundleName ) {
		
		return downloadBundle( userName, bundleName, telosysToolsCfg.getDownloadsFolder() ) ;
	}

	public BundleStatus downloadAndInstallBundle( String userName, String bundleName ) {
		
		BundleStatus status1 = downloadBundle( userName, bundleName, telosysToolsCfg.getDownloadsFolder() ) ;
		if ( status1.isDone() && status1.getException() == null ) {
			BundleStatus status2 = installBundle(status1.getZipFile(), bundleName);
			return status2 ;
		}
		else {
			return status1 ;
		}
	}

	//--------------------------------------------------------------------------------------------------
	/**
	 * Download a bundle (GitHub repo) in a specific project folder
	 * @param userName
	 * @param bundleName
	 * @param downloadFolderInProject
	 * @return
	 */
	public BundleStatus downloadBundle( String userName, String bundleName, String downloadFolderInProject )  {
		BundleStatus status = new BundleStatus();
		GitHubClient gitHubClient = new GitHubClient( telosysToolsCfg.getProperties() ) ; 
		String destinationFile = buildDestinationFileName(bundleName, downloadFolderInProject) ;
		status.log("-> Download bundle '" + bundleName + "' ");
		status.log("   in '" + destinationFile + "' ");
		try {
			gitHubClient.downloadRepository(userName, bundleName, destinationFile);
			status.setDone(true);
			status.setMessage("OK, bundle '" + bundleName + "' downloaded.");
			status.setZipFile(destinationFile);
		} catch (Exception e) {
			status.setDone(false);
			status.setMessage("ERROR: cannot download bundle '" + bundleName + "'.");
			status.setException(e);
		}
		return status ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Return a list of bundles available on GitHub for the given user's name
	 * @param githubUserName the GitHub user name
	 * @return
	 * @throws Exception
	 */
	public BundlesFromGitHub getGitHubBundlesList( String githubUserName ) throws Exception {
		
		// HTTP request to GitHub 
		GitHubClient gitHubClient = new GitHubClient( telosysToolsCfg.getProperties() ) ; 
		GitHubRepositoriesResponse githubResponse = gitHubClient.getRepositories(githubUserName);
		
		// Build list of bundles names (can be void)
		List<String> bundlesList = new LinkedList<>();
		for ( GitHubRepository repo : githubResponse.getRepositories() ) {
			bundlesList.add( repo.getName() );
		}
		
		// Result
		return new BundlesFromGitHub(githubResponse.getHttpStatusCode(), bundlesList, githubResponse.getRateLimit() );
	}

	/**
	 * Returns the current GitHub API rate limit for the current IP address
	 * @return
	 * @throws Exception
	 */
	public GitHubRateLimitResponse getGitHubRateLimit() throws Exception {
		
		// HTTP request to GitHub 
		GitHubClient gitHubClient = new GitHubClient( telosysToolsCfg.getProperties() ) ; 
		return gitHubClient.getRateLimit();
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Build the filesystem full path for the given repository name and destination folder
	 * @param repoName GitHub repository name
	 * @param sDownloadFolder
	 * @return
	 */
	private String buildDestinationFileName(String repoName, String sDownloadFolder) {
		// file path in project
		String sFile = repoName + ".zip" ;
		String pathInProject = FileUtil.buildFilePath(sDownloadFolder, sFile);
		// file path in Operating System 
		return FileUtil.buildFilePath(telosysToolsCfg.getProjectAbsolutePath(), pathInProject);
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Install (unzip) the given zip file in the bundle's destination folder 
	 * @param zipFileName
	 * @param bundleName
	 * @return
	 */
	public BundleStatus installBundle( String zipFileName, String bundleName ) {
		
		BundleStatus status = new BundleStatus();
		
		if ( isBundleAlreadyInstalled( bundleName ) ) {
			status.setDone(false);
			status.setMessage("Bundle already installed.");
			return status ;
		}
		else {
			String bundleFolder = telosysToolsCfg.getTemplatesFolderAbsolutePath(bundleName) ;
			status.log("-> Install '" + zipFileName + "' ");
			status.log("   in '" + bundleFolder + "' ");
			try {
				ZipUtil.unzip(zipFileName, bundleFolder, true ) ;
				status.setDone(true);
				status.setMessage("OK, bundle installed.");
			} catch (Exception e) {
				status.setDone(false);
				status.setMessage("ERROR: Cannot unzip "+ zipFileName);
				status.setException(e);
			}
			return status ;
		}
	}

	/**
	 * Deletes the given bundle
	 * @param bundleName
	 * @return true if found and deleted, false if not found
	 * @throws TelosysToolsException
	 */
	public boolean deleteBundle( String bundleName ) {
		
		// Build the folder full path for the given folder name
		String bundleFolder = telosysToolsCfg.getTemplatesFolderAbsolutePath(bundleName);
		File file = new File(bundleFolder);
		if ( file.exists() && file.isDirectory() ) {
			// Bundle directory found => deleted it 
			DirUtil.deleteDirectory(file);
			return true ;
		}
		return false ;
	}
	
// Removed in 4.1.0
//	/**
//	 * Returns the list of bundles defined in the project <br>
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	public List<String> getProjectBundlesList() throws TelosysToolsException {
//		File dir = getBundlesFolder();
//		if ( dir.exists() ) {
//			if ( dir.isDirectory() ) {
//				List<String> bundles = new LinkedList<>();
//				for ( File bundleDir : dir.listFiles() ) {
//					if ( bundleDir.isDirectory() ) {
//						if ( telosysToolsCfg.hasSpecificTemplatesFolders() ) {
//							// Can contains any kind of folders => check existence of "templates.cfg" 
//							if ( isBundleConfigFileExists( bundleDir.getName() ) ) {
//								bundles.add(bundleDir.getName());
//							}
//						}
//						else {
//							// No specific templates folder => just keep all folders
//							bundles.add(bundleDir.getName());
//						}
//					}
//				}
//				return bundles;
//			}
//			else {
//				throw new TelosysToolsException("Templates folder '" + dir.getAbsolutePath() + "' is not a folder.");
//			}
//		}
//		else {
//			throw new TelosysToolsException("Templates folder '" +  dir.getAbsolutePath() + "' not found.");
//		}
//	}	
	
	/**
	 * Returns all the bundles in the project (each bundle as a directory) 
	 * @return
	 * @since 4.1.0
	 */
	public List<File> getBundles() {
		List<File> bundles = new LinkedList<>();
		File bundlesFolder = getBundlesFolder();
		if ( bundlesFolder.exists() && bundlesFolder.isDirectory() ) {
			for ( File file : bundlesFolder.listFiles() ) {
				if ( isBundle(file) ) {
					bundles.add(file);
				}
			}
		}
		return bundles;
	}	
	
	/**
	 * Returns true if the given file is a 'bundle' ( a directory containing a bundle configuration file )
	 * @param file
	 * @return
	 */
	public boolean isBundle(File file) {
		return file != null && file.isDirectory() && isBundleConfigFileExists(file.getName() ) ;
	}
	
	/**
	 * Returns the target definitions for the given bundle name
	 * @param bundleName
	 * @return
	 * @throws TelosysToolsException
	 */
	public TargetsDefinitions getTargetsDefinitions(String bundleName) throws TelosysToolsException
	{
		if ( StrUtil.nullOrVoid(bundleName) ) {
			throw new TelosysToolsException("Invalid bundle name (null or void) : '" + bundleName + "'  ");
		}

		File templatesCfgFile = getBundleConfigFile(bundleName);
		
		TargetsFile targetsFile = new TargetsFile(templatesCfgFile.getAbsolutePath()) ;
		if ( targetsFile.exists() ) {
			//--- Try to load the targets 
			List<TargetDefinition> allTargetsList = targetsFile.load();
			//--- Build the two lists of targets : templates targets and resources targets 
			List<TargetDefinition> templatesTargets = new LinkedList<>() ;
			List<TargetDefinition> resourcesTargets = new LinkedList<>() ;
			for ( TargetDefinition t : allTargetsList ) {
				if ( t.isResource() ) {
					resourcesTargets.add(t) ;
				}
				else {
					templatesTargets.add(t);
				}
			}
			return new TargetsDefinitions(templatesTargets, resourcesTargets);
		}
		else {
			throw new TelosysToolsException("File not found '" + templatesCfgFile.getAbsolutePath() + "'");
		}
	}	
	
}
