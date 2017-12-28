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
* ----------------
* This test tests the export-results feature.
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 -  Create several users using POST query.
* 	 -  Delete the users.
* 	 1. Export the results to file. 
* 
* Results:
*	 1.   Export results should be ended successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test74__multiple_users_export_results {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test74__multiple_users_export_results(String browser) {
	  
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
  public void Manage_multiple_users_export_results() throws Exception {
	 
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set vars
	  String srcUserPrefix 	    = "exportResultsUser";
	  String exportFilesPrefix  = "exportData";
	  String usersNumber   	    = "3";
	  int usStartIdx 		 	= 1;
	  String dispPrefix   	    = srcUserPrefix + testFuncs.getId();
	  Map<String, String> map   = new HashMap<String, String>();
	  map.put("usersNumber",  usersNumber); 
	  map.put("startIdx"   ,  String.valueOf(usStartIdx));
	  map.put("srcUsername",  "Finished");

	  // Create several users using POST query
	  testFuncs.myDebugPrinting("Create several users using POST query");
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       ,
					 										   testVars.getPort()  	  ,
					 										   usersNumber		 	  ,
					 										   dispPrefix  		      ,
					 										   testVars.getDomain()   ,
					 										   "registered"           ,
					 										   "430HD"                ,
					 										   testVars.getDefTenant(),
					 										   "myLocation");	
	  testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true); 
		       		
	  // Delete the created users
	  testFuncs.myDebugPrinting("Delete the created users");
	  testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
	  testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
	  map.put("usersPrefix"	  , dispPrefix + "_");
	  map.put("usersNumber"	  , usersNumber); 
	  map.put("startIdx"   	  , String.valueOf(usStartIdx));
	  map.put("srcUsername"	  , "Finished");
	  map.put("action"	 	  , "Delete Users");
	  map.put("skipVerifyDelete", "true");
	  testFuncs.setMultipleUsersAction(driver, map);
	  dispPrefix = dispPrefix.toLowerCase();
	  testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
	  testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
	  testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
    
	  // Step 1 - Export results
	  testFuncs.myDebugPrinting("Step 11 - Export results");
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), exportFilesPrefix);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[4]"), 10000);  
	  testFuncs.myAssertTrue("File was not exported successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), exportFilesPrefix));
	  String exportFilesTxt = testFuncs.readFile(testVars.getDownloadsPath() + "\\" + exportFilesPrefix + ".csv");
	  testFuncs.myAssertTrue("The header Device was not found in the export-data file !! \n" + exportFilesTxt, exportFilesTxt.contains("Device"));
	  testFuncs.myAssertTrue("The header Status was not found in the export-data file !! \n" + exportFilesTxt, exportFilesTxt.contains("Status"));
	  testFuncs.myAssertTrue("The header Status was not found in the export-data file !! \n" + exportFilesTxt, exportFilesTxt.contains("Status"));
	  String username;
	  for (int i = 1; i < (Integer.parseInt(usersNumber) + 1); i++) {
		
			username = dispPrefix + "_" + String.valueOf(i);
			testFuncs.myAssertTrue("The user " + username + " was not found in the export-data file !! \n" + exportFilesTxt, exportFilesTxt.contains(username));
	  }
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), exportFilesPrefix);
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
