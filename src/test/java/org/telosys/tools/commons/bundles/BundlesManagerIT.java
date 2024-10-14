package org.telosys.tools.commons.bundles;

import java.io.File;
import java.util.Properties;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.depot.Depot;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotResponse;

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
public class BundlesManagerIT extends TestCase {

	//private static final String DEPOT_NAME = "telosys-templates" ; // changed in v 4.1.0 ( old = "telosys-templates-v3" )
	private static final String DEPOT = "github_org:telosys-templates" ; // changed in v 4.2.0 
	
	private static final String MASTER = "master" ;
	private static final String BUNDLE_BRANCH = "master" ;
	private static final String BUNDLE_NAME = "java-domain-example" ; // changed in v 4.1.0 ( old = "basic-templates-samples-T300" )
	private static final String BUNDLE_NAME2 = "java-jdbc" ;
	
	private TelosysToolsCfg telosysToolsCfg = null ;
	
	private void println(String s) {
		System.out.println(s);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		println("===== [ SETUP ] =====");
		
		File projectFolder = TestsEnv.createTmpProjectFolders("myproject");
		
		FileUtil.copyFileToFile(TestsEnv.getTelosysToolsCfgFile(), TestsEnv.getTmpFile("myproject/telosys-tools.cfg"), false);
				
		telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(projectFolder);
		Properties configProperties = telosysToolsCfg.getProperties();
		
		println("Forcing http properties ...");
		Properties proxyProperties = TestsEnv.loadSpecificProxyProperties();
		setProperty(configProperties, proxyProperties, "http.proxyHost");
		setProperty(configProperties, proxyProperties, "http.proxyPort");
		setProperty(configProperties, proxyProperties, "https.proxyHost");
		setProperty(configProperties, proxyProperties, "https.proxyPort");
		
		println("HTTP properties : ");
		printProxyProperties(telosysToolsCfg.getProperties()) ;
	}
	private void printProxyProperties(Properties p) {
		printProperty(p, "http.proxyHost");
		printProperty(p, "http.proxyPort");
		printProperty(p, "https.proxyHost");
		printProperty(p, "https.proxyPort");
	}
	
	private void setProperty(Properties configProperties, Properties proxyProperties, String name) {
		String value = proxyProperties.getProperty(name) ;
		if ( value != null ) {
			configProperties.setProperty(name, value);
			println("Property '"+name+"' : set to '"+value+"' ");
		}
		else {
			println("Property '"+name+"' : null (not set)");
		}
	}
	
	private void printProperty(Properties properties, String name) {
		println(name + " : " + properties.getProperty(name));
	}
	
	private BundlesManager getBundlesManager() {
		return new BundlesManager( telosysToolsCfg );
	}
	
	@Test
	public void testFolder() {
		println("========== File system folder  ");

		String bundlesFolderInConfig = TestsEnv.getTmpBundlesFolderFullPath();
		println("Bundles folder in config : '" + bundlesFolderInConfig + "'");
		BundlesManager bm = getBundlesManager();
		
		println("Getting downloads folder ...");
		String downloadsFolder = bm.getDownloadsFolderFullPath() ;
		println(" result = '" + downloadsFolder + "'... ");
		assertEquals(telosysToolsCfg.getDownloadsFolderAbsolutePath(), downloadsFolder);
	}

	@Test
	public void testBundlesList() throws TelosysToolsException  {
		println("========== List of available bundles  ");

		BundlesManager bm = getBundlesManager();
		DepotResponse depotResponse = bm.getBundlesFromDepot(new Depot(DEPOT));
		for ( String s : depotResponse.getElementNames() ) {
			println(" . " + s );
		}
		for ( DepotElement e : depotResponse.getElements() ) {
			println(" . " + e.getName() + " / " + e.getDefaultBranch() );
			assertEquals("public", e.getVisibility() );
			assertEquals(MASTER, e.getDefaultBranch() );
		}
		println("Message : " + depotResponse.getRateLimit().getStandardMessage() );
	}
	
	@Test
	public void testIsBundleInstalled() {
		println("========== isBundleAlreadyInstalled  ");
		BundlesManager bm = getBundlesManager();
		boolean b = bm.isBundleAlreadyInstalled("no-installed");
		assertFalse(b);
	}

	@Test
	public void testDownloadBundleOK() throws TelosysToolsException {
		BundlesManager bm = getBundlesManager();
		println("Downloading bundle '" + BUNDLE_NAME + "'...");
		String zipFileFullPath = bm.downloadBundleBranch(new Depot(DEPOT), BUNDLE_NAME, BUNDLE_BRANCH);
		assertTrue(zipFileFullPath.endsWith(BUNDLE_NAME+".zip"));
	}

	@Test
	public void testDownloadThenInstallBundle() throws TelosysToolsException { 
		println("========== Download + Install ");
		String bundleName = BUNDLE_NAME2 ; 
		BundlesManager bm = getBundlesManager();
		println("Downloading bundle '" + bundleName + "'...");
		
		String zipFileFullPath = bm.downloadBundleBranch(new Depot(DEPOT), bundleName, BUNDLE_BRANCH);
		assertTrue(zipFileFullPath.endsWith(bundleName+".zip"));
		
		bm.deleteBundle(bundleName);
		assertFalse(bm.isBundleAlreadyInstalled(bundleName));
		
		// 1rst time : not already installed
		assertTrue(  bm.installBundle(zipFileFullPath, bundleName) );
		// 2nd time : already installed
		assertFalse(  bm.installBundle(zipFileFullPath, bundleName) );
	}

	@Test
	public void testDownloadAndInstallBundle() throws TelosysToolsException { 
		println("========== downloadAndInstallBundle ");
		BundlesManager bm = getBundlesManager();

		bm.deleteBundle(BUNDLE_NAME2);
		// 1rst time : not already installed
		assertTrue( bm.downloadAndInstallBundleBranch(new Depot(DEPOT), BUNDLE_NAME2, BUNDLE_BRANCH) );
		// 2nd time : already installed
		assertFalse( bm.downloadAndInstallBundleBranch(new Depot(DEPOT), BUNDLE_NAME2, BUNDLE_BRANCH) );
	}

}
