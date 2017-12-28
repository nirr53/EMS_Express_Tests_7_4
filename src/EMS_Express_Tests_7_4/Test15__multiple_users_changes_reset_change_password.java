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
* This test tests the reset and change password actions of multiple-users-changes menu
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create several users using POST query.
* 	 2. Reset the users.
* 	 3. Change the users password.
* 	 4. Delete the users.
* 
* Results:
* 	 1. Users should be created successfully.
* 	 2. The users should be reset successfully.
*    3. The users password should be changed successfully.
* 	 4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test15__multiple_users_changes_reset_change_password {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test15__multiple_users_changes_reset_change_password(String browser) {
	  
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
  public void Manage_multiple_users_reset_change_password() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String srcUserPrefix 	= "rUser";
	String usersNumber   	= "3";
	int usStartIdx 		 	= 1;
	String dispPrefix   	= srcUserPrefix + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");
	
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
	testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true);   
    
    // Step 2 - Reset the users password
  	testFuncs.myDebugPrinting("Step 2 - Reset the users password");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"   , "Reset Users Passwords");
    map.put("subAction", " ");
    testFuncs.setMultipleUsersAction(driver, map);
    
    // Step 3 - Change password the created users
  	testFuncs.myDebugPrinting("Step 3 - Change password the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"   , "Reset Users Passwords");
    map.put("password" , "1234567890");
    map.put("subAction", "changePassword");
    testFuncs.setMultipleUsersAction(driver, map);

    // Step 4 - Delete the created users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
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
