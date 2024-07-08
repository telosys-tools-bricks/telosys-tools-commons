package org.telosys.tools.commons.bundles;

import java.io.File;
import java.util.Properties;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;

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

	private static final String GITHUB_STORE = "telosys-templates" ; // changed in v 4.1.0 ( old = "telosys-templates-v3" )
	
	private static final String BUNDLE_NAME = "java-domain-example" ; // changed in v 4.1.0 ( old = "basic-templates-samples-T300" )
	
	private static final String BUNDLE_NAME2 = "java-jdbc" ;
	
	private TelosysToolsCfg telosysToolsCfg = null ;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("===== [ SETUP ] =====");
		
		File projectFolder = TestsEnv.createTmpProjectFolders("myproject");
		
		FileUtil.copy(TestsEnv.getTelosysToolsCfgFile(), TestsEnv.getTmpFile("myproject/telosys-tools.cfg"), false);
		
//		System.out.println("Loading configuration from folder : " + TestsEnv.getTestsRootFolder() );
//		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(TestsEnv.getTestsRootFolder());
//		try {
//			this.telosysToolsCfg = cfgManager.loadProjectConfig();
//		} catch (TelosysToolsException e) {
//			e.printStackTrace();
//			throw new RuntimeException("Cannot load project properties", e);
//		}
//		Properties configProperties = telosysToolsCfg.getProperties();
		
//		Properties configProperties = TestsEnv.loadTelosysToolsCfgProperties();
		
		telosysToolsCfg = TestsEnv.loadTelosysToolsCfg(projectFolder);
		Properties configProperties = telosysToolsCfg.getProperties();
		
		System.out.println("Forcing http properties ...");
		Properties proxyProperties = TestsEnv.loadSpecificProxyProperties();
		setProperty(configProperties, proxyProperties, "http.proxyHost");
		setProperty(configProperties, proxyProperties, "http.proxyPort");
		setProperty(configProperties, proxyProperties, "https.proxyHost");
		setProperty(configProperties, proxyProperties, "https.proxyPort");
		
		System.out.println("HTTP properties : ");
//		printProperty(configProperties, "http.proxyHost");
//		printProperty(configProperties, "http.proxyPort");
//		printProperty(configProperties, "https.proxyHost");
//		printProperty(configProperties, "https.proxyPort");
		printProxyProperties(telosysToolsCfg.getProperties()) ;
	}
	private void printProxyProperties(Properties p) {
		printProperty(p, "http.proxyHost");
		printProperty(p, "http.proxyPort");
		printProperty(p, "https.proxyHost");
		printProperty(p, "https.proxyPort");
	}
	
	private void setProperty(Properties configProperties, Properties proxyProperties, String name) {
		System.out.print("Property '"+name+"' ");
		String value = proxyProperties.getProperty(name) ;
		if ( value != null ) {
			configProperties.setProperty(name, value);
			System.out.print(" set to '"+value+"' ");
		}
		else {
			System.out.print(" null (not set)");
		}
		System.out.println("");
	}
	
	private void printProperty(Properties properties, String name) {
		System.out.println(name + " : " + properties.getProperty(name));
	}
	
	private BundlesManager getBundlesManager() {
		BundlesManager bm = new BundlesManager( telosysToolsCfg );
		return bm;
	}
	
	public void testFolder() {
		System.out.println("========== File system folder  ");

		String bundlesFolderInConfig = TestsEnv.getTmpBundlesFolderFullPath();
		System.out.println("Bundles folder in config : '" + bundlesFolderInConfig + "'");
		BundlesManager bm = getBundlesManager();
		
		System.out.println("Getting downloads folder ...");
		String downloadsFolder = bm.getDownloadsFolderFullPath() ;
		System.out.println(" result = '" + downloadsFolder + "'... ");
		assertEquals(telosysToolsCfg.getDownloadsFolderAbsolutePath(), downloadsFolder);

//		System.out.println("Getting bundles folder ...");
//		String bundlesFolder = bm.getBundlesFolderFullPath() ;
//		System.out.println(" result = '" + bundlesFolder + "'... ");
//		assertEquals(telosysToolsCfg.getTemplatesFolderAbsolutePath(), bundlesFolder);
		
		
//		String bundleName = "foo";
//		System.out.println("Getting folder for " + bundleName + "'... ");		
//		String folder = bm.getBundleFolderFullPath(bundleName);
//		System.out.println(" result = '" + folder + "'... ");
//		
//		String expected = telosysToolsCfg.getTemplatesFolderAbsolutePath() + "/" + bundleName ;
//		assertEquals(expected, folder);
	}

	public void testBundlesList() throws Exception {
		System.out.println("========== List of available bundles  ");

		BundlesManager bm = getBundlesManager();
		//List<String> bundles = bm.getGitHubBundlesList(GITHUB_STORE) ;
		BundlesFromGitHub bundles = bm.getGitHubBundlesList(GITHUB_STORE) ;
		for ( String s : bundles.getBundles() ) {
			System.out.println(" . " + s );
		}
		System.out.println("Message : " + bundles.getRateLimitMessage() );
	}
	
	public void testIsBundleInstalled() {
		System.out.println("========== isBundleAlreadyInstalled  ");
		BundlesManager bm = getBundlesManager();
		boolean b = bm.isBundleAlreadyInstalled("no-installed");
		assertFalse(b);
	}

	public void testDownloadBundleOK() throws TelosysToolsException {
		System.out.println("========== Download  ");
		BundlesManager bm = getBundlesManager();
		System.out.println("Downloading bundle '" + BUNDLE_NAME + "'...");
//		BundleStatus status = bm.downloadBundle(GITHUB_STORE, BUNDLE_NAME);
//		System.out.println("Satus message : " + status.getMessage() );
//		System.out.println("Satus is done ? : " + status.isDone() );
//		if ( status.getException() != null ) {
//			System.out.println("Exception : " + status.getException());
//		}
//		System.out.println("Zip file : " + status.getZipFile());
//		
//		assertTrue(status.isDone() );
//		assertNull(status.getException());
		String zipFileFullPath = bm.downloadBundle(GITHUB_STORE, BUNDLE_NAME);
		assertTrue(zipFileFullPath.endsWith(BUNDLE_NAME+".zip"));
	}

	public void testDownloadBundleInSpecificFolder() throws TelosysToolsException  {
		System.out.println("========== Download in specific folder ");
		BundlesManager bm = getBundlesManager();
		System.out.println("Downloading bundle '" + BUNDLE_NAME + "'...");
		TestsEnv.getTmpExistingFolder("myproject/TelosysTools/downloads2"); // Creates the folder if it doesn't exists yet
//		BundleStatus status = bm.downloadBundle(GITHUB_STORE, BUNDLE_NAME, "TelosysTools/downloads2");
//		System.out.println("Satus message : " + status.getMessage() );
//		System.out.println("Satus is done ? : " + status.isDone() );
//		if ( status.getException() != null ) {
//			System.out.println("Exception : " + status.getException());
//		}
//		System.out.println("Zip file : " + status.getZipFile());
//		
//		assertTrue(status.isDone() );
//		assertNull(status.getException());
		String zipFileFullPath = bm.downloadBundle(GITHUB_STORE, BUNDLE_NAME, "TelosysTools/downloads2");
		assertTrue(zipFileFullPath.endsWith(BUNDLE_NAME+".zip"));
	}

	@Test
	public void testDownloadBundleInNonExistentFolder() {
		System.out.println("========== Download in non existent folder ");
		BundlesManager bm = getBundlesManager();
		System.out.println("Downloading bundle '" + BUNDLE_NAME + "'...");
//		BundleStatus status = bm.downloadBundle("telosys-tools", BUNDLE_NAME, "TelosysTools/downloads-inex");
//		System.out.println("Satus message : " + status.getMessage() );
//		System.out.println("Satus is done ? : " + status.isDone() );
//		if ( status.getException() != null ) {
//			System.out.println("Exception : " + status.getException());
//		}
//		System.out.println("Zip file : " + status.getZipFile());
//		
//		assertFalse(status.isDone() );
//		assertNotNull(status.getException());

		org.junit.Assert.assertThrows(TelosysToolsException.class, () -> bm.downloadBundle(GITHUB_STORE, BUNDLE_NAME, "TelosysTools/downloads-inex") );
//		String zipFileFullPath = bm.downloadBundle(GITHUB_STORE, BUNDLE_NAME, "TelosysTools/downloads-inex");
//		assertTrue(zipFileFullPath.endsWith(BUNDLE_NAME+".zip"));
	}

	public void testDownloadThenInstallBundle() throws TelosysToolsException { 
		System.out.println("========== Download + Install ");
		String bundleName = BUNDLE_NAME2 ; 
		BundlesManager bm = getBundlesManager();
		System.out.println("Downloading bundle '" + bundleName + "'...");
		
//		BundleStatus status = bm.downloadBundle(GITHUB_STORE, bundleName);
//
//		System.out.println("Satus message : " + status.getMessage() );
//		System.out.println("Satus is done ? : " + status.isDone() );
//		System.out.println("Zip file : " + status.getZipFile());
//		if ( status.getException() != null ) {
//			System.out.println("Exception : " + status.getException());
//		}
//		
//		assertTrue(status.isDone() );
//		assertNull(status.getException());
		
		String zipFileFullPath = bm.downloadBundle(GITHUB_STORE, bundleName);
		assertTrue(zipFileFullPath.endsWith(bundleName+".zip"));
		
//		String zipFile = status.getZipFile();
//		if ( status.isDone() && status.getException() == null ) {
//			System.out.println("Installing bundle '" + bundleName + "' from " + zipFile );
//			BundleStatus status2 = bm.installBundle(zipFile, bundleName);
//			System.out.println("Satus message : " + status2.getMessage() );
//			System.out.println("Satus is done ? : " + status2.isDone() );
//			System.out.println("Exception : " + status2.getException());
//			System.out.println("Satus log : "  );
//			System.out.println( status2.getLog() );
//		}

		bm.deleteBundle(bundleName);
		assertFalse(bm.isBundleAlreadyInstalled(bundleName));
		
		// 1rst time : not already installed
		assertTrue(  bm.installBundle(zipFileFullPath, bundleName) );
		// 2nd time : already installed
		assertFalse(  bm.installBundle(zipFileFullPath, bundleName) );
	}

	public void testDownloadAndInstallBundle() throws TelosysToolsException { 
		System.out.println("========== downloadAndInstallBundle ");
		BundlesManager bm = getBundlesManager();

		// BundleStatus status = bm.downloadAndInstallBundle(GITHUB_STORE, BUNDLE_NAME2);

//		System.out.println("Satus message : " + status.getMessage() );
//		System.out.println("Satus is done ? : " + status.isDone() );
//		System.out.println("Zip file : " + status.getZipFile());
//		if ( status.getException() != null ) {
//			System.out.println("Exception : " + status.getException());
//		}
		
//		//assertTrue(status.isDone() ); // Not "done" if already installed
//		assertNull(status.getException());
//		System.out.println("Satus log : "  );
//		System.out.println( status.getLog() );

		bm.deleteBundle(BUNDLE_NAME2);
		// 1rst time : not already installed
		assertTrue( bm.downloadAndInstallBundle(GITHUB_STORE, BUNDLE_NAME2) );
		// 2nd time : already installed
		assertFalse( bm.downloadAndInstallBundle(GITHUB_STORE, BUNDLE_NAME2) );
	}

}
