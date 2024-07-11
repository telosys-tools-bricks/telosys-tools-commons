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
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotResponse;
import org.telosys.tools.commons.github.GitHubClient;
import org.telosys.tools.commons.github.GitHubDownloader;

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

	private GitHubClient getGitHubClient() {
		return new GitHubClient( telosysToolsCfg.getCfgFileAbsolutePath() ) ; 
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
	public String downloadBundle(String userName, String bundleName) throws TelosysToolsException {
		return downloadBundle(userName, bundleName, telosysToolsCfg.getDownloadsFolder() ) ;
	}
	
	public String downloadBundle(String userName, String bundleName, String downloadFolder) throws TelosysToolsException {
		GitHubDownloader downloader = new GitHubDownloader(telosysToolsCfg); 
		return downloader.downloadRepo(userName, bundleName, downloadFolder ) ;
	}
	
	public boolean downloadAndInstallBundle(String userName, String bundleName) throws TelosysToolsException {
		
		// BundleStatus status1 = downloadBundle( userName, bundleName, telosysToolsCfg.getDownloadsFolder() ) ;
		GitHubDownloader downloader = new GitHubDownloader(telosysToolsCfg); 
		String downloadedFile = downloader.downloadRepo(userName, bundleName, telosysToolsCfg.getDownloadsFolder() ) ;
		
//		if ( status1.isDone() && status1.getException() == null ) {
//			BundleStatus status2 = installBundle(status1.getZipFile(), bundleName);
//			return status2 ;
//		}
//		else {
//			return status1 ;
//		}
		return installBundle(downloadedFile, bundleName);
	}

	//--------------------------------------------------------------------------------------------------
	/**
	 * Return a list of bundles available on depot (GitHub,..) for the given user's name
	 * @param depotName the depot name, ie GitHub user name or org name
	 * @return
	 * @throws Exception
	 */
	public BundlesFromDepot getBundlesFromDepot( String depotName ) throws Exception {
		
		// HTTP request to GitHub 
		GitHubClient gitHubClient = getGitHubClient(); // v 4.1.1
//		GitHubRepositoriesResponse githubResponse = gitHubClient.getRepositories(githubUserName);
		DepotResponse depotResponse = gitHubClient.getRepositories(depotName); // v 4.2.0
		
		// Build list of bundles names (can be void)
		List<String> bundlesList = new LinkedList<>();
//		for ( GitHubRepository repo : githubResponse.getRepositories() ) {
//			bundlesList.add( repo.getName() );
//		}
		for ( DepotElement repo : depotResponse.getElements() ) {
			bundlesList.add( repo.getName() );
		}
		
		// Result
//		return new BundlesFromGitHub(githubResponse.getHttpStatusCode(), bundlesList, githubResponse.getRateLimit() );
		return new BundlesFromDepot(depotResponse.getHttpStatusCode(), bundlesList, depotResponse.getRateLimit() );
	}

//	/**
//	 * Returns the current GitHub API rate limit for the current IP address
//	 * @return
//	 * @throws Exception
//	 */
//	public GitHubRateLimitResponse getGitHubRateLimit() throws Exception {
//		
//		// HTTP request to GitHub 
//		GitHubClient gitHubClient = getGitHubClient(); // v 4.1.1
//		return gitHubClient.getRateLimit();
//	}
	
	//--------------------------------------------------------------------------------------------------
//	/**
//	 * Build the filesystem full path for the given repository name and destination folder
//	 * @param repoName GitHub repository name
//	 * @param sDownloadFolder
//	 * @return
//	 */
//	private String buildDestinationFileName(String repoName, String sDownloadFolder) {
//		// file path in project
//		String sFile = repoName + ".zip" ;
//		String pathInProject = FileUtil.buildFilePath(sDownloadFolder, sFile);
//		// file path in Operating System 
//		return FileUtil.buildFilePath(telosysToolsCfg.getProjectAbsolutePath(), pathInProject);
//	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Install (unzip) the given zip file in the bundle's destination folder 
	 * @param zipFileName
	 * @param bundleName
	 * @return
	 */
	public boolean installBundle( String zipFileName, String bundleName ) throws TelosysToolsException {
		
//		BundleStatus status = new BundleStatus();
		
		if ( isBundleAlreadyInstalled( bundleName ) ) {
//			status.setDone(false);
//			status.setMessage("Bundle already installed.");
//			return status ;
			return false ;
		}
		else {
			String bundleFolder = telosysToolsCfg.getTemplatesFolderAbsolutePath(bundleName) ;
//			status.log("-> Install '" + zipFileName + "' ");
//			status.log("   in '" + bundleFolder + "' ");
			try {
				ZipUtil.unzip(zipFileName, bundleFolder, true ) ;
				return true;
//				status.setDone(true);
//				status.setMessage("OK, bundle installed.");
			} catch (Exception e) {
//				status.setDone(false);
//				status.setMessage("ERROR: Cannot unzip "+ zipFileName);
//				status.setException(e);
				throw new TelosysToolsException("Cannot unzip "+ zipFileName, e);
			}
//			return status ;
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
