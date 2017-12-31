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
* This test tests the features of configuration key menu of multiple-users-changes menu
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create several users using POST query.
* 	 2. Add Daylight-configuration-key
* 	 3. Add Telnet-configuration-keys
* 	 4. Add PIN acceess-configuration-keys
* 	 5. Add CAP profile values
* 	 6. Delete all configuration values
* 	 7. Delete the users.
* 
* Results:
*	 1. Users should be created successfully.
* 	 2. Daylight-configuration-key should be added successfully.
* 	 3. Telnet-access values should be added successfully.
* 	 4. PIN-access values should be added successfully.
*    5. CAP profile values should be added successfully.
*    6. All created configuration values should be deleted.
*    7. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test113__multiple_users_configuration_key_features {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test113__multiple_users_configuration_key_features(String browser) {
	  
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
  public void Manage_multiple_users_configuration_key_features() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String usersNumber   	= "3";
	String Id				= testFuncs.getId();
	int usStartIdx 		 	= 1;
	String dispPrefix   	= "confKeyFeaturesUser" + Id;
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));

    // Step 1 - Create several users using POST query
	testFuncs.myDebugPrinting("Step 1 - Create several users using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumber		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUsersCreate(driver,  dispPrefix,  dispPrefix, true, Integer.valueOf(usersNumber));	
    
    // Step 2 - Add Daylight-configuration-keys
  	testFuncs.myDebugPrinting("Add Daylight-configuration-keys");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"    , "User configuration");
    map.put("confKey"   , "features");
    map.put("daylight"  , "true");
    testFuncs.setMultipleUsersAction(driver, map);
        
    // Step 3 - Add Telnet-access configuration-keys
  	testFuncs.myDebugPrinting("Step 3 - Add Telnet-access configuration-keys");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"    , "User configuration");
    map.put("confKey"   , "features");
    map.remove("daylight");
    map.put("telnet"  , "true");
    testFuncs.setMultipleUsersAction(driver, map);
    
    // Step 4 - Add PIN-lock configuration-keys
  	testFuncs.myDebugPrinting("Step 4 - Add PIN-lock configuration-keys");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"    , "User configuration");
   map.put("confKey"   , "features");
    map.remove("telnet");
    map.put("pinLock"  , "true");
    testFuncs.setMultipleUsersAction(driver, map);
    
    // Step 5 - Add CAP-profile keys
  	testFuncs.myDebugPrinting("Step 5 - Add CAP-profile keys");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"    , "User configuration");
    map.put("confKey"   , "features");
    map.remove("pinLock");
    map.put("capProfile"  , "true");
    testFuncs.setMultipleUsersAction(driver, map);
    
    // Step 6 - Delete all configuration values
  	testFuncs.myDebugPrinting("Step 6 - Delete all configuration values");
    map.put("action"    , "User configuration");
    map.put("confKey"   , "features");
    map.remove("capProfile");
    map.put("deleteAllConfValues"  , "true");
    testFuncs.setMultipleUsersAction(driver, map);
   
    // Step 7 - Delete the created users
  	testFuncs.myDebugPrinting("Step 7 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("usersPrefix"	  , dispPrefix + "_");
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    dispPrefix = dispPrefix.toLowerCase();
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
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
