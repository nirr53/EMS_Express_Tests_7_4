package EMS_Express_Tests_7_4;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

/**
* ----------------
* This test tests the Import of users and configuration
* ----------------
* Tests:
* 	 - Login and enter the Import configuration menu.
* 	 1. Import exisitng configuration
* 	 2. Check headers of the import-result
* 	 3. Remove a value from each of the sections, and verify that the import action re-add them
* 
* Results:
* 	 1. Import should end successfully.
* 	 2. All headers should be displayed
* 
* 
* 	 4.  Delete should be ended successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test11__import_configuration_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test11__import_configuration_tests(String browser) {
	  
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
  public void Import_configuration() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	String path        	  	 = "";
	String xpathUploadField  = "//*[@id='fileToUpload']";
	String xpathUploadButton = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/input";
	
    // Login and enter the Import users menu and import users
	testFuncs.myDebugPrinting("Login and enter the Import users menu and import users");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	
	// Step 1 - Upload Configuration file
	testFuncs.myDebugPrinting("Step 1 - Login and enter the Import users menu and import users");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
	
	// Step 2 - Check headers of the import-result
	testFuncs.myDebugPrinting("Step 2 - Check headers of the import-result");
	testFuncs.myWait(10000);
	driver.switchTo().frame(0);
	testFuncs.searchStr(driver, "Import Result");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/div/label", "Tenants");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[3]/div/div/label", "Regions");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[4]/div/div/label", "Sites");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[5]/div/div/label", "Templates");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[6]/div/div/label", "System Settings");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[7]/div/div/label", "Template Placeholders");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[8]/div/div/label", "Tenant Placeholders");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[9]/div/div/label", "Site Placeholders");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[10]/div/div/label", "Phone Firmware Files");

	// Check Data
	testFuncs.myDebugPrinting("Check Data", testVars.logerVars.NORMAL);
	
	// Tenants
	testFuncs.searchStr(driver, "NirTest1 Tenant that used for Nir auto testing Exist");
	
	// Regions
	testFuncs.searchStr(driver, "Test1Region Test1 Test1Region Exist");
	testFuncs.searchStr(driver, "AutoDetection Nir This region is intended for automatic detection nodes Exist");
	testFuncs.searchStr(driver, "AutoDetection NirTest1 This region is intended for automatic detection nodes Exist");
	
	// Sites
	testFuncs.searchStr(driver, "Test1Site Test1Site Test1 Test1Region Exist");
	testFuncs.searchStr(driver, "AutoDetection Nir AutoDetection Exist");
	testFuncs.searchStr(driver, "AutoDetection NirTest1 AutoDetection Exist");

	// Templates
	String templatesName = "Template_for_configuration_test";
	testFuncs.searchStr(driver, templatesName + " Template for Import configuration tests");
		
	// System Settings
	testFuncs.searchStr(driver, "MwiVmNumber 888 Exist");
	testFuncs.searchStr(driver, "ntpserver 10.1.1.10 Exist");

	// Template placeholders
	String tempPhName 	  = "ConfTest_ph";
	String tempPhValue	  = "1234";
	String tempPhTemplate = "Audiocodes_430HD";
	testFuncs.searchStr(driver, tempPhName + " " +  tempPhValue + " Template PH that used for configuration import tests " + tempPhTemplate + " Exist");

	// Tenant placeholders
	String tenPhName   = "ConfTest_Tenant_ph_key";
	String tenPhValue  = "1234";
	String tenPhTenant = "Nir";
	testFuncs.searchStr(driver, tenPhName + " " + tenPhValue + " " + tenPhTenant + " Exist");
	
	// Site placeholders
	String sitePhName    = "ConfTest_Site_ph_key";
	String sitePhValue   = "1234";
	String sitePhSite    = "AutoDetection";
	String siteForDelete = sitePhSite + " [" + sitePhSite + "]";
	String siteForSearch = sitePhSite + " [" + sitePhSite + "] / Nir";
	testFuncs.searchStr(driver, sitePhName + " " + sitePhValue + " " + sitePhSite + " Exist");

	// Phone Firmware Files
	String firmName    = "430HD";
	String firmDesc    = "430HD - default firmware";
	String firmVersion = "430HDUC_2.0.13.121";
	testFuncs.searchStr(driver, firmName + " " + firmDesc + " " + firmVersion + " /data/NBIF/ippmanager/generate//firmware/430HD.img Exist");

    // Step 3 - Remove a value from each of the sections, and verify that the import action re-add them
 	testFuncs.myDebugPrinting("Step 2 - Remove a value from each of the sections, and verify that the import action re-add them");
	
 	// Delete Templates
	testFuncs.myDebugPrinting("Delete Templates", testVars.logerVars.NORMAL);
 	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
	testFuncs.deleteTemplate(driver, templatesName);
	
	// Change MWI number
	testFuncs.myDebugPrinting("Change MWI number", testVars.logerVars.NORMAL);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='MwiVmNumber']"), "1234", 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							, 5000);
	
	// Change System Settings
	testFuncs.myDebugPrinting("Change System Settings", testVars.logerVars.NORMAL);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='MwiVmNumber']"), "1234", 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							, 5000);
	
	// Delete Template placeholders
	testFuncs.myDebugPrinting("Delete Template placeholders", testVars.logerVars.NORMAL);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
    testFuncs.deleteTemplatePlaceholder(driver, tempPhTemplate, tempPhName);

    // Delete Tenant placeholders
	testFuncs.myDebugPrinting("Delete Tenant placeholders", testVars.logerVars.NORMAL);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	testFuncs.selectTenant(driver, tenPhTenant);
	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);

	// Delete Site-placeholders
	testFuncs.myDebugPrinting("Delete Site-placeholders", testVars.logerVars.NORMAL);
	testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");
	testFuncs.selectSite(driver, siteForSearch);
	testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, siteForDelete);

	// Delete from Phone-firmware-files
	testFuncs.myDebugPrinting("Delete from Phone-firmware-files", testVars.logerVars.NORMAL);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");
	testFuncs.deleteFirmware(driver,  firmName, firmDesc, firmVersion);

	// Re-import	
	testFuncs.myDebugPrinting("Re-import", testVars.logerVars.NORMAL);
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
	
	// Tenants
	testFuncs.searchStr(driver, "NirTest1 Tenant that used for Nir auto testing " + "Exist");
	
	// Regions
	testFuncs.searchStr(driver, "Test1Region Test1 Test1Region " 										   		+ "Exist");
	testFuncs.searchStr(driver, "AutoDetection Nir This region is intended for automatic detection nodes " 		+ "Exist");
	testFuncs.searchStr(driver, "AutoDetection NirTest1 This region is intended for automatic detection nodes " + "Exist");
	
	// Sites
	testFuncs.searchStr(driver, "Test1Site Test1Site Test1 Test1Region " + "Exist");
	testFuncs.searchStr(driver, "AutoDetection Nir AutoDetection " 		 + "Exist");
	testFuncs.searchStr(driver, "AutoDetection NirTest1 AutoDetection "  + "Exist");

	// Templates		
	testFuncs.searchStr(driver, templatesName + " Template for Import configuration tests");

	// System Settings
	testFuncs.searchStr(driver, "MwiVmNumber 888 "     + "Exist");
	testFuncs.searchStr(driver, "ntpserver 10.1.1.10 " + "Exist");

	// Template placeholders
	testFuncs.searchStr(driver, tempPhName + " " +  tempPhValue + " Template PH that used for configuration import tests " + tempPhTemplate + " Added");

	// Tenant placeholders
	testFuncs.searchStr(driver, tenPhName + " " + tenPhValue + " Nir " + "Added");
	
	// Site placeholders
	testFuncs.searchStr(driver, sitePhName + " " + sitePhValue + " " + sitePhSite + " Added");

	// Phone Firmware Files
	testFuncs.searchStr(driver, firmName + " " + firmDesc + " " + firmVersion + " /data/NBIF/ippmanager/generate//firmware/430HD.img " + "Added");
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
