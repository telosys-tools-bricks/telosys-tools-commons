package org.telosys.tools.commons.models;

import java.io.File;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.depot.Depot;
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
		println("--- response body");
		println(depotResponse.getResponseBody());
		println("---");
		for ( String s : depotResponse.getElementNames() ) {
			println(" . " + s );
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
		DepotResponse depotResponse = getModelsFromDepot("github_org:telosys-models");
		assertEquals(200, depotResponse.getHttpStatusCode());
	}
	
	@Test
	public void testGetModelsFromDepotViaGitHubCurrentUser() throws TelosysToolsException {
		DepotResponse depotResponse = getModelsFromDepot("github_current_user");
		assertEquals(401, depotResponse.getHttpStatusCode()); // No user token => 401 "Requires authentication"
	}
	
//	public void testIsBundleInstalled() {
//		println("========== isBundleAlreadyInstalled  ");
//		ModelsManager mm = initProjectAndGetModelsManager() ;
//		boolean b = mm.isModelAlreadyInstalled("no-installed");
//		assertFalse(b);
//	}

	public void testDownloadOK() throws TelosysToolsException {
		println("========== Download  ");
		ModelsManager mm = initProjectAndGetModelsManager() ;
//		println("Downloading bundle '" + BUNDLE_NAME + "'...");
		String zipFileFullPath = mm.downloadModel(new Depot("github_org:telosys-models"), "cars");
		println("File " + zipFileFullPath);
		assertTrue(zipFileFullPath.endsWith("cars.zip"));
	}

//	public void testDownloadBundleInSpecificFolder() throws TelosysToolsException  {
//		println("========== Download in specific folder ");
//		BundlesManager bm = getBundlesManager();
//		println("Downloading bundle in specific folder '" + BUNDLE_NAME + "'...");
//		TestsEnv.getTmpExistingFolder("myproject/TelosysTools/downloads2"); // Creates the folder if it doesn't exists yet
//		String zipFileFullPath = bm.downloadBundle(DEPOT_NAME, BUNDLE_NAME, "TelosysTools/downloads2");
//		assertTrue(zipFileFullPath.endsWith(BUNDLE_NAME+".zip"));
//	}
//
//	@Test
//	public void testDownloadBundleInNonExistentFolder() {
//		println("========== Download in non existent folder ");
//		BundlesManager bm = getBundlesManager();
//		println("Downloading bundle '" + BUNDLE_NAME + "'...");
//
//		org.junit.Assert.assertThrows(TelosysToolsException.class, () -> bm.downloadBundle(DEPOT_NAME, BUNDLE_NAME, "TelosysTools/downloads-inex") );
//	}
//
//	public void testDownloadThenInstallBundle() throws TelosysToolsException { 
//		println("========== Download + Install ");
//		String bundleName = BUNDLE_NAME2 ; 
//		BundlesManager bm = getBundlesManager();
//		println("Downloading bundle '" + bundleName + "'...");
//		
//		String zipFileFullPath = bm.downloadBundle(DEPOT_NAME, bundleName);
//		assertTrue(zipFileFullPath.endsWith(bundleName+".zip"));
//		
//		bm.deleteBundle(bundleName);
//		assertFalse(bm.isBundleAlreadyInstalled(bundleName));
//		
//		// 1rst time : not already installed
//		assertTrue(  bm.installBundle(zipFileFullPath, bundleName) );
//		// 2nd time : already installed
//		assertFalse(  bm.installBundle(zipFileFullPath, bundleName) );
//	}
//
//	public void testDownloadAndInstallBundle() throws TelosysToolsException { 
//		println("========== downloadAndInstallBundle ");
//		BundlesManager bm = getBundlesManager();
//
//		bm.deleteBundle(BUNDLE_NAME2);
//		// 1rst time : not already installed
//		assertTrue( bm.downloadAndInstallBundle(DEPOT_NAME, BUNDLE_NAME2) );
//		// 2nd time : already installed
//		assertFalse( bm.downloadAndInstallBundle(DEPOT_NAME, BUNDLE_NAME2) );
//	}

}
