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
* This test tests the create-user options (manually and via POST query)
* ----------------
* Tests:
* 	 1. Add new user manually.
* 	 2. Add a registered user.
* 	 3. Add an unregistered user.
* 	 4. Delete the created users
* 
* Results:
* 	 1. User should be added successfully.
* 	 2. User should be added successfully and appear 'registered' in DEvice-status menu.
* 	 3. User should be added unsuccessfully.
* 	 4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test5__add_user_tests {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test5__add_user_tests(String browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
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
  public void User_create() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String Id 				= testFuncs.getId();
	String prefix			= "postUserPrefix";
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));

	// login
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	
    // Step 1 - Create user manually
	testFuncs.myDebugPrinting("Step 1 - Create user manually");
	testFuncs.addUser(driver, prefix + "0_" + Id, "1q2w3e$r", prefix + "0_" + Id, testVars.getDefTenant());
	testFuncs.myWait(2000);

    // Step 2 - Create a registered user using POST method
	testFuncs.myDebugPrinting("Step 2 - Create a registered user using POST method");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 " 1"				    	,
			 												 prefix + "1_" + Id 		,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, prefix + "1_" + Id, prefix + "1_" + Id, true);
    testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
    
    // Step 3 - Create an unregistered user using POST method
	testFuncs.myDebugPrinting("Step 3 - Create an unregistered user using POST method");  
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 												 testVars.getPort()    	    ,
			 												 " 1"				   	    ,
			 												 prefix + "2_" + Id     ,
			 												 testVars.getDomain()       ,
			 												 "unregistered"             ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "Manage Users");
    testFuncs.verifyPostUserCreate(driver, prefix + "2_" + Id, prefix + "2_" + Id, false);
    
    // Step 4 - Delete the users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, prefix, "3");
    map.put("usersPrefix"	  , prefix);
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    prefix = prefix.toLowerCase();
    testFuncs.searchStr(driver, prefix + "0_" + Id + " Finished");
    testFuncs.searchStr(driver, prefix + "1_" + Id + "@" + testVars.getDomain() + " Finished");
  }

  @After
  public void tearDown() throws Exception {
	  
//    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}
