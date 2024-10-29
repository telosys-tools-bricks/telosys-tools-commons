package org.telosys.tools.commons.models;

import java.io.File;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.depot.Depot;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotResponse;
import org.telosys.tools.commons.github.GitHubToken;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

/**
 * This class is "IT" ( Integration Test) due to potential connection problem with https on GitHub
 * javax.net.ssl.SSLException: Received fatal alert: protocol_version
 * https://stackoverflow.com/questions/16541627/javax-net-ssl-sslexception-received-fatal-alert-protocol-version 
 *  
 * @author Laurent Guerin
 *
 */
public class ModelsManagerIT extends TestCase {
	private static final String DEPOT = "github_org:telosys-models" ;  
	private static final String MASTER = "master" ;
	private static final String BRANCH = "master" ;
	private static final String MODEL_NAME = "cars" ;

	private void println(String s) {
		System.out.println(s);
	}

	private ModelsManager initProjectAndGetModelsManager() throws TelosysToolsException {
		// No token in current user home
		GitHubToken.clear();
		// Init project dir with config 
		File projectFolder = TestsEnv.createTmpProjectFolders("myproject");
		FileUtil.copyFileToFile(TestsEnv.getTelosysToolsCfgFile(), TestsEnv.getTmpFile("myproject/telosys-tools.cfg"), false);				
		TelosysToolsCfg telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(projectFolder);
		return new ModelsManager(telosysToolsCfg);
	}
	
	private DepotResponse getModelsFromDepot(String depot) throws TelosysToolsException {
		println("===");
		ModelsManager mm = initProjectAndGetModelsManager();
		// Get elements from depot 
		DepotResponse depotResponse = mm.getModelsFromDepot(new Depot(depot));
		println("Depot URL : " + depotResponse.getDepotURL() ); 
		println("--- status code : " + depotResponse.getHttpStatusCode() );
		println("--- number of req : " + depotResponse.getNumberOfRequests() );
		println("---");
		for ( DepotElement e : depotResponse.getElements() ) {
			println(" . " + e.getName() + " / " + e.getDefaultBranch() );
			assertEquals("public", e.getVisibility() );
			assertEquals(MASTER, e.getDefaultBranch() );
		}		
		println("Message : " + depotResponse.getRateLimit().getStandardMessage() );
		return depotResponse;
	}
	
	@Test
	public void testGetModelsFromDepotViaGitHubUser() throws TelosysToolsException {
		DepotResponse depotResponse = getModelsFromDepot("github_user:telosys-models");
		assertEquals(200, depotResponse.getHttpStatusCode());
	}
	
	@Test
	public void testGetModelsFromDepotViaGitHubOrg() throws TelosysToolsException {
		DepotResponse depotResponse = getModelsFromDepot(DEPOT);
		assertEquals(200, depotResponse.getHttpStatusCode());
	}
	
	@Test
	public void testGetModelsFromDepotViaGitHubCurrentUser() throws TelosysToolsException {
		DepotResponse depotResponse = getModelsFromDepot("github_current_user");
		assertEquals(401, depotResponse.getHttpStatusCode()); // No user token => 401 "Requires authentication"
	}
	
	@Test
	public void testIsModelAlreadyInstalled() throws TelosysToolsException {
		ModelsManager mm = initProjectAndGetModelsManager() ;
		boolean b = mm.isModelAlreadyInstalled("no-installed");
		assertFalse(b);
	}

	@Test
	public void testDownloadOK() throws TelosysToolsException {
		ModelsManager mm = initProjectAndGetModelsManager() ;
		String zipFileFullPath = mm.downloadModelBranch(new Depot(DEPOT), MODEL_NAME, BRANCH);
		assertTrue(zipFileFullPath.endsWith("cars.zip"));
	}
	
	@Test
	public void testDownloadAndInstall() throws TelosysToolsException { 
		ModelsManager mm = initProjectAndGetModelsManager() ;
		mm.deleteModel(MODEL_NAME);
		// 1rst time : not already installed
		assertTrue( mm.downloadAndInstallModelBranch(new Depot(DEPOT), MODEL_NAME, BRANCH) );
		// 2nd time : already installed
		assertFalse( mm.downloadAndInstallModelBranch(new Depot(DEPOT), MODEL_NAME, BRANCH) );
	}

}
