package EMS_Express_Tests_7_4;

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
*----------------
* This test tests the Import of users+devices
* ----------------
* Tests:
* 	 - Enter the Import+devices users menu.
* 	 1. Import some new users + devices.
* 	 2. Import some exisitng users + devices.
* 	 3. Delete all the import-users
* 	 4. Import 1000 users
* 	 5. Delete all the import-users
* 
* Results:
* 	 1. Import should be ended successfully.
* 	 2. Import should be blocked with appropriate error message.
* 	 3. Delete should be ended successfully.
* 	 4. Import should be ended successfully.
* 	 5. Delete should be ended successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test12__import_users_devices_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test12__import_users_devices_tests(String browser) {
	  
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
  public void Import_users_devices_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String path        	  	 	= "";
	String prefixUser		 	= "importedUser";
	String prefixDevice		 	= "importedDevice";
	int    usersNumberInt		= -1;
	String xpathUploadField  	= "//*[@id='file_source']";
	String xpathUploadButton    = "//*[@id='uploadForm']/div[2]/a";
	String[] confirmMessageStrs = {"Import Users & Devices", "The import action can take a few minutes. Do you want to continue?"};
	String usersNumber   		= "5";
    Map<String, String> map 	= new HashMap<String, String>();
	
	// Login and enter the Import users+devices menu
	testFuncs.myDebugPrinting("Login and enter the Import users-devices menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_import", "Import Users and Devices information");
  
	// Step 1 - Import users+devices
	testFuncs.myDebugPrinting("Step 1 - Import users+devices");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("12.1");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton, confirmMessageStrs);
	usersNumberInt = Integer.valueOf(usersNumber);
	for (int i = 1; i <= usersNumberInt; ++i) {

		String tempIdx = String.valueOf(i);
	    testFuncs.searchStr(driver, prefixUser + "_" + tempIdx + "@" + testVars.getDomain() + " Added, Device " + prefixDevice + "_" + tempIdx + " - added");   
	}
	
	// Step 2 - Import exisitng users+devices
	testFuncs.myDebugPrinting("Step 2 - Import exisitng users+devices");
	testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_import", "Import Users and Devices information");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("12.1");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton, confirmMessageStrs);
	usersNumberInt = Integer.valueOf(usersNumber);
	for (int i = 1; i < usersNumberInt; ++i) {
		
		String tempIdx = String.valueOf(i);
	    testFuncs.searchStr(driver, prefixUser + "_" + tempIdx + "@" + testVars.getDomain() + " Already exists, Device " + prefixDevice + "_" + tempIdx + " - already exists");
	}

    // Step 3 - Delete the created users
  	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, prefixUser, usersNumber);
    map.put("usersPrefix"	  , prefixUser + "_");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(1));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
	usersNumberInt = Integer.valueOf(usersNumber);
	for (int i = 1; i < usersNumberInt; ++i) {
		
		String tempIdx = String.valueOf(i);
		testFuncs.searchStr(driver, prefixUser.toLowerCase() + "_" + tempIdx + "@" + testVars.getDomain() + " Finished");	
	}
 
	// Step 4 - Import 1000 users
	testFuncs.myDebugPrinting("Step 4 - Import 1000 users");
	testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_import", "Import Users and Devices information");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("12.2");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton, confirmMessageStrs);
	testFuncs.waitTillString(driver, "Performing...");
    testFuncs.myWait(10000);		
	usersNumber   		= "1000";
	usersNumberInt 		= Integer.valueOf(usersNumber);
	String bodyText     = driver.findElement(By.tagName("body")).getText();
	for (int i = 1; i <= usersNumberInt; ++i) {

		String tempIdx = String.valueOf(i);	
		String tempName = prefixUser + "_" + tempIdx + "@" + testVars.getDomain() + " Added"; 
		if (bodyText.contains(tempName)) {
			  
			testFuncs.myDebugPrinting("<" + tempName + "> was detected !!",  testVars.logerVars.MINOR);
		  
		} else {
			
			testFuncs.myFail("<" + tempName + "> was not detected !! \nbodyText - " + bodyText);	  
		}
	}
	
    // Step 5 - Delete the created users
  	testFuncs.myDebugPrinting("Step 5 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, prefixUser, usersNumber);
    map.put("usersNumber"	  , usersNumber); 
    testFuncs.setMultipleUsersAction(driver, map);
	testFuncs.waitTillString(driver, "Performing...");
    testFuncs.myWait(10000);
	bodyText     	= driver.findElement(By.tagName("body")).getText();
	usersNumberInt 	= Integer.valueOf(usersNumber);
	prefixUser = prefixUser.toLowerCase();
	for (int i = 1; i < usersNumberInt; ++i) {
		
		String tempIdx = String.valueOf(i);
		String tempName = prefixUser + "_" + tempIdx + "@" + testVars.getDomain() + " Finished"; 
		if (bodyText.contains(tempName)) {
			  
			testFuncs.myDebugPrinting("<" + tempName + "> was detected !!",  testVars.logerVars.MINOR);
		  
		} else {
			
			testFuncs.myFail("<" + tempName + "> was not detected !! \nbodyText - " + bodyText);	  
		}
	}
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
