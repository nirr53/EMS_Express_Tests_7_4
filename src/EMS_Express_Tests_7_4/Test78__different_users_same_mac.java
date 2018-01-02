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
* This test tests the scenrio of create two users with the same MAC
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create first user using POST query.
* 	 2. Create a second user with differnt username but with the same MAC address using POST query
*    3. Verify that in the Device-Status menu only the second user appears
*    4. Verify that in the Manage-Multiple-Users-Changes menu both users exist
* 	 5. Delete the users.
* 
* Results:
* 	 1. User should be created successfully.
* 	 2. The user should be created successfully.
* 	 3. nd previous device should be deleted.
* 	 4. The two users should appear.
* 	 5. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test78__different_users_same_mac {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test78__different_users_same_mac(String browser) {
	  
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
  public void Different_users_same_mac() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars login
	String Id             = testFuncs.getId();
	String prefixName     = "sameMac";
	String firstUsername  = prefixName + "_1_" + Id;
	String secondUsername = prefixName + "_2_" + Id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	
    // Step 1 - Create a user using POST query
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()      ,
			 testVars.getPort()    		,
			 "1"				   		,
			 firstUsername		   		,
			 testVars.getDomain()  		,
			 "registered"          		,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver, firstUsername, firstUsername, true);
	testFuncs.myWait(10000);
	
    // Step 2 - Create a user using POST query
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()      ,
			 testVars.getPort()    		,
			 "1"				   		,
			 secondUsername		   		,
			 testVars.getDomain()  	    ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver, secondUsername, secondUsername, true);
 
    // Step 3 - Verify that in the Device-Status menu only the second user appears	
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + firstUsername, 5000);
	driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
	testFuncs.myWait(10000);
	testFuncs.searchStr(driver, "There are no devices that fit this search criteria");

	// Step 4 - Verify that in the Manage-Multiple-Users-Changes menu both users exist
	testFuncs.myDebugPrinting("Step 4 - Verify that in the Manage-Multiple-Users-Changes menu both users exist");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, prefixName, "2");
    
   // Step 5 - Delete the user
  	testFuncs.myDebugPrinting("Step 5 - Delete the user");
    Map<String, String> map = new HashMap<String, String>();
    map.put("action"	 	  ,  "Delete Users");
    map.put("srcUsername"	  ,  "Finished");
    map.put("skipVerifyDelete", "true");
    map.put("usersNumber"	  , "2"); 
    map.put("startIdx"   	  , "1");
    map.put("usersPrefix"     ,  prefixName);
    testFuncs.setMultipleUsersAction(driver, map);
    firstUsername  = firstUsername.toLowerCase();
    secondUsername = secondUsername.toLowerCase();
    testFuncs.searchStr(driver, firstUsername  + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, secondUsername + "@" + testVars.getDomain() + " Finished");
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
