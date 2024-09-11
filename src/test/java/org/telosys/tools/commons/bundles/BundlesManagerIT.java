package org.telosys.tools.commons.bundles;

import java.io.File;
import java.util.Properties;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
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

	private static final String DEPOT_NAME = "telosys-templates" ; // changed in v 4.1.0 ( old = "telosys-templates-v3" )
	
	private static final String BUNDLE_NAME = "java-domain-example" ; // changed in v 4.1.0 ( old = "basic-templates-samples-T300" )
	
	private static final String BUNDLE_NAME2 = "java-jdbc" ;
	
	private TelosysToolsCfg telosysToolsCfg = null ;
	
	private void println(String s) {
		System.out.println(s);
	}
	private void print(String s) {
		System.out.print(s);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		println("===== [ SETUP ] =====");
		
		File projectFolder = TestsEnv.createTmpProjectFolders("myproject");
		
		FileUtil.copy(TestsEnv.getTelosysToolsCfgFile(), TestsEnv.getTmpFile("myproject/telosys-tools.cfg"), false);
				
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
		print("Property '"+name+"' ");
		String value = proxyProperties.getProperty(name) ;
		if ( value != null ) {
			configProperties.setProperty(name, value);
			print(" set to '"+value+"' ");
		}
		else {
			print(" null (not set)");
		}
		println("");
	}
	
	private void printProperty(Properties properties, String name) {
		println(name + " : " + properties.getProperty(name));
	}
	
	private BundlesManager getBundlesManager() {
		BundlesManager bm = new BundlesManager( telosysToolsCfg );
		return bm;
	}
	
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

	public void testBundlesList() throws Exception {
		println("========== List of available bundles  ");

		BundlesManager bm = getBundlesManager();
		DepotResponse depotResponse = bm.getBundlesFromDepot(DEPOT_NAME) ;
		for ( String s : depotResponse.getElementNames() ) {
			println(" . " + s );
		}
		println("Message : " + depotResponse.getRateLimit().getStandardMessage() );
	}
	
	public void testIsBundleInstalled() {
		println("========== isBundleAlreadyInstalled  ");
		BundlesManager bm = getBundlesManager();
		boolean b = bm.isBundleAlreadyInstalled("no-installed");
		assertFalse(b);
	}

	public void testDownloadBundleOK() throws TelosysToolsException {
		println("========== Download  ");
		BundlesManager bm = getBundlesManager();
		println("Downloading bundle '" + BUNDLE_NAME + "'...");
		String zipFileFullPath = bm.downloadBundle(DEPOT_NAME, BUNDLE_NAME);
		assertTrue(zipFileFullPath.endsWith(BUNDLE_NAME+".zip"));
	}

	public void testDownloadBundleInSpecificFolder() throws TelosysToolsException  {
		println("========== Download in specific folder ");
		BundlesManager bm = getBundlesManager();
		println("Downloading bundle in specific folder '" + BUNDLE_NAME + "'...");
		TestsEnv.getTmpExistingFolder("myproject/TelosysTools/downloads2"); // Creates the folder if it doesn't exists yet
		String zipFileFullPath = bm.downloadBundle(DEPOT_NAME, BUNDLE_NAME, "TelosysTools/downloads2");
		assertTrue(zipFileFullPath.endsWith(BUNDLE_NAME+".zip"));
	}

	@Test
	public void testDownloadBundleInNonExistentFolder() {
		println("========== Download in non existent folder ");
		BundlesManager bm = getBundlesManager();
		println("Downloading bundle '" + BUNDLE_NAME + "'...");

		org.junit.Assert.assertThrows(TelosysToolsException.class, () -> bm.downloadBundle(DEPOT_NAME, BUNDLE_NAME, "TelosysTools/downloads-inex") );
	}

	public void testDownloadThenInstallBundle() throws TelosysToolsException { 
		println("========== Download + Install ");
		String bundleName = BUNDLE_NAME2 ; 
		BundlesManager bm = getBundlesManager();
		println("Downloading bundle '" + bundleName + "'...");
		
		String zipFileFullPath = bm.downloadBundle(DEPOT_NAME, bundleName);
		assertTrue(zipFileFullPath.endsWith(bundleName+".zip"));
		
		bm.deleteBundle(bundleName);
		assertFalse(bm.isBundleAlreadyInstalled(bundleName));
		
		// 1rst time : not already installed
		assertTrue(  bm.installBundle(zipFileFullPath, bundleName) );
		// 2nd time : already installed
		assertFalse(  bm.installBundle(zipFileFullPath, bundleName) );
	}

	public void testDownloadAndInstallBundle() throws TelosysToolsException { 
		println("========== downloadAndInstallBundle ");
		BundlesManager bm = getBundlesManager();

		bm.deleteBundle(BUNDLE_NAME2);
		// 1rst time : not already installed
		assertTrue( bm.downloadAndInstallBundle(DEPOT_NAME, BUNDLE_NAME2) );
		// 2nd time : already installed
		assertFalse( bm.downloadAndInstallBundle(DEPOT_NAME, BUNDLE_NAME2) );
	}

}
