package EMS_Tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

/**
* ----------------
* This test tests the private place-holders feature
* ----------------
* Tests:
* 	- Create a phone Template and add a private placeholder to it.
* 	 1. Enter the Export-configuration menu and export the System configuration
* 	 2. Enter the Import-configuration menu, import the System configuration and verify the private-placeholder import
* 	 3. Delete all the created data
* 
* Results:
* 	 1. The private-placeholder should be exported.
* 	 2. The private-placeholder should be imported.
* 	 3. All data should be deleted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test129__export_private_template_placeholder {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test129__export_private_template_placeholder(String browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  // Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	GlobalVars testVars2  = new GlobalVars();
    return Arrays.asList(testVars2.getBrowsers());
  }
  
  @BeforeClass
  public static void setting_SystemProperties() {
	  
	  System.out.println("System Properties seting Key value.");
  }  
  
  @Before
  public void setUp() throws Exception {
	  	
	testVars  = new GlobalVars();
    testFuncs = new GlobalFuncs(); 
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Export_private_placeholders() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());		
	
	// Set vars
	String Id 		   		 = testFuncs.getId();
	String prCfgKey	   		 = "private" 		  + Id;
	String tempName	   		 = "my420HDTemplate_" + Id;
	String tempDesc    		 = "my420HDdesc"      + Id;
	String dwnldFle    		 = "Configuration.zip";
	String xpathUploadField  = "//*[@id='fileToUpload']";
	String xpathUploadButton = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/input";	
    Map<String, String> map  = new HashMap<String, String>();
    map.put("isRegionDefault"		   ,  "false");
    map.put("isDownloadSharedTemplates",  "false");
  	map.put("cloneFromtemplate"		   , "Audiocodes_420HD");

    // Login and enter the Phone Templates menu
	testFuncs.myDebugPrinting("Login and enter the Phone Templates menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
	
	// Create a phone Template and add a private placeholder to it.
	testFuncs.myDebugPrinting("Create a phone Template and add a private placeholder to it");
	map.put("cloneFromtemplate", "Audiocodes_" + "420HD");
	testFuncs.addTemplate(driver, tempName, tempDesc, testVars.getNonDefTenant(0), "420HD", map);
	enterTemplate(tempName);
	addCfg(prCfgKey);
	
	// Step 1 - Enter the Export-configuration menu and export the System configuration
	testFuncs.myDebugPrinting("Step 1 - Enter the Export-configuration menu and export the System configuration");
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_export", "To export phone configuration files");
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), dwnldFle);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/button"), 300000);		
	testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), dwnldFle));

	// Step 2 - Enter the Import-configuration menu, import the System configuration and verify the private-placeholder import
	testFuncs.myDebugPrinting("Step 2 - Enter the Import-configuration menu, import the System configuration and verify the private-placeholder import");	
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	String path  = testVars.getDownloadsPath() + "\\" + dwnldFle;
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
	testFuncs.searchStr(driver, prCfgKey);

	// Step 3 - Delete the created Template
	testFuncs.myDebugPrinting("Step 3 - Delete the created Template");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");	
	testFuncs.deleteTemplate(driver, tempName);
  }

  // Add private configuration key
  private void addCfg(String id) {
	  
	  String tempStr = "%ITCS_P_" + id + "%";
	  testFuncs.myDebugPrinting("tempStr - " + tempStr, testVars.logerVars.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , id		, 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), tempStr, 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[3]/a/span"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successfully to save the template new configuration settings");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	  
  }

  // Enter the created Template
  private void enterTemplate(String tempName) throws IOException {
	  
	  // Get idx
	  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
	  String l = null;
	  int i = 1;
	  while ((l = r.readLine()) != null) {
		  
		  testFuncs.myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
		  if (l.contains(tempName)) {
			  
			  testFuncs.myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
			  break;
		  }
		  if (l.contains("Edit" )) {
			  
			  ++i;
		  }
	  } 
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[8]/div/buttton[1]"), 3000);		  	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"        , "IP Phone " + tempName + " Configuration Template");
  }

  @After
  public void tearDown() throws Exception {
	  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
