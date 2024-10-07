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
package org.telosys.tools.commons.github;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;

/**
 * Utility class to download GitHub repositories
 * 
 * @author Laurent Guerin
 *
 */
public class GitHubDownloader {
	
	private final TelosysToolsCfg telosysToolsCfg ;

	/**
	 * Constructor 
	 * @param telosysToolsCfg
	 */
	public GitHubDownloader(TelosysToolsCfg telosysToolsCfg) {
		super();
		if ( telosysToolsCfg == null ) throw new IllegalArgumentException("TelosysToolsCfg argument is null");
		this.telosysToolsCfg = telosysToolsCfg;
	}

	/**
	 * Build the filesystem full path for the given repository name and destination folder
	 * @param repoName GitHub repository name
	 * @param downloadFolder
	 * @return
	 */
	private String buildDestinationFileName(String repoName, String downloadFolder) {
		// file path in project
		String zipFileName = repoName + ".zip" ;
		String pathInProject = FileUtil.buildFilePath(downloadFolder, zipFileName);
		// file path in Operating System 
		return FileUtil.buildFilePath(telosysToolsCfg.getProjectAbsolutePath(), pathInProject);
	}
	
	/**
	 * Download a GitHub repo in a specific project folder
	 * @param userName
	 * @param repoName
	 * @param downloadFolderInProject
	 * @return
	 */
	public String downloadRepo( String userName, String repoName, String downloadFolderInProject ) throws TelosysToolsException {
		GitHubClient gitHubClient = new GitHubClient( telosysToolsCfg.getCfgFileAbsolutePath() ) ;
		String downloadedFile = buildDestinationFileName(repoName, downloadFolderInProject) ;
		try {
			gitHubClient.downloadRepository(userName, repoName, downloadedFile);
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot download repository '" + repoName + "'.", e);
		}
		return downloadedFile;
	}
	
	/**
	 * Download a GitHub repo in the downloads folder defined in the current configuration<br>
	 * e.g. downloads a zip file in 'TelosysTools/downloads'  
	 * @param userName the GitHub user name (e.g. "telosys-tools")
	 * @param repoName the bundle name, in other words the GitHub repository name 
	 * @return
	 */
	public String downloadRepo(String userName, String repoName ) throws TelosysToolsException {
		return downloadRepo( userName, repoName, telosysToolsCfg.getDownloadsFolder() ) ;
	}	
}
