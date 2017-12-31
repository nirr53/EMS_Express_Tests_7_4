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
* This test tests alarms from different types
* ----------------
* Tests:
* 	- Create a registered users from two different tenants
* 	 1. Create an alarm for the first and second users and search for them according to the Tenant
*    2. Check The Clear-filter button of the page
*    3. Check The Clear-filter button at Filter menu
*    4. Check for no output.
* 	 5. Check Telnet connection for the created Alarm
*    6. Delete the created users and alarms
* 
* Results:
* 	  1. The alarms should be detected by their Tenant/
*   2+3. Filter should be cleard.
*     4. No Alarm should be detected.
*     5. Telnet menu should be opened.
*     6. All created data should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test110__alarms_tests3 {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test110__alarms_tests3(String browser) {
	  
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
  public void Alarms_tests() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set vars and login
	String prefix = "telnetAlert";
	String user1  = prefix + "1_" + testFuncs.getId();
	String user2  = prefix + "2_" + testFuncs.getId();
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	
    // Create a registered users from two different tenants
	testFuncs.myDebugPrinting("Create a registered users from two different tenants");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 "1"				    	,
			 												 user1 						,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, user1, user1, true);
	String mac1 = testFuncs.readFile("mac_1.txt");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 												 testVars.getPort()         ,
			 												 "1"				        ,
			 												 user2 			,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getNonDefTenant(0),
			 												 "myLocation");

	testFuncs.verifyPostUserCreate(driver, user2, user2, true);
	String mac2 = testFuncs.readFile("mac_1.txt");
	
	// Step 1 - Create an alarm for the first and second users and search for them according to the Tenant
	testFuncs.myDebugPrinting("Step 1 - Create an alarm for the first and second users and search for them according to the Tenant");
	String []alertsForSearch1 = {user1};
	String []alertsForSearch2 = {user2};
	testFuncs.enterMenu(driver, "Dashboard_Alarms", "Export");	
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac1								 			 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  user1				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac2								 			 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  user2				    		 				 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
	testFuncs.searchAlarm(driver, "Tenant", testVars.getDefTenant().toLowerCase()    , alertsForSearch1);
	testFuncs.searchAlarm(driver, "Tenant", testVars.getNonDefTenant(0).toLowerCase(), alertsForSearch2);

	// Step 2 - Check The Clear-filter button of the page
	testFuncs.myDebugPrinting("Step 2 - Check The Clear-filter button of the page");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 3000);
	testFuncs.searchStr(driver, user1);
	testFuncs.searchStr(driver, user2);
	
	// Step 3 - Check The Clear-filter button at Filter menu
	testFuncs.myDebugPrinting("Step 3 - Check The Clear-filter button at Filter menu");
	testFuncs.searchAlarm(driver, "Tenant", testVars.getDefTenant().toLowerCase()    , alertsForSearch1);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a")						    , 3000); 
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[4]/div[4]/div/button[2]"), 3000);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[4]/div[4]/div/button[1]"), 5000);
	testFuncs.searchStr(driver, user1);
	testFuncs.searchStr(driver, user2);

	// Step 4 - Check for no output
	testFuncs.myDebugPrinting("Step 4 - Check for no output");
	testFuncs.searchAlarm(driver, "Description", "unknownMac", alertsForSearch1);

	// Step 5 - Check Telnet connection for the created Alarm
	testFuncs.myDebugPrinting("Step 5 - Check Telnet connection for the created Alarm");
	testFuncs.searchAlarm(driver, "Tenant", testVars.getDefTenant().toLowerCase()    , alertsForSearch1);
	testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a")		   , 3000);
	testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[2]/a"), 3000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[1]", "Telnet (DEBUG)");
	testFuncs.verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]/label[1]", "CMD");
	testFuncs.verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]/label[2]", "CMDs");
	testFuncs.verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]/label[3]", "Alarm Name");
	testFuncs.verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]/label[4]", "Password");
	testFuncs.verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]/label[5]", "Wait for response:");
	 // TODO how to test it?
	testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonCancel']"), 2000);
	
	
	// Step 6 - Delete the created alarms and users
	testFuncs.myDebugPrinting("Step 6 - Delete the created alarms and users");
	testFuncs.deleteAlarm(driver, alertsForSearch1[0]);	
	testFuncs.deleteAlarm(driver, alertsForSearch2[0]);	
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");    
	testFuncs.selectMultipleUsers(driver, prefix, "2");
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersPrefix"	  , prefix);  
	map.put("usersNumber"	  , "2"); 
	map.put("startIdx"   	  , String.valueOf("1"));	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");	  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);
	user1 = user1.toLowerCase();
	user2 = user2.toLowerCase();
	testFuncs.searchStr(driver, user1 + "@" + testVars.getDomain() + " Finished");	
	testFuncs.searchStr(driver, user2 + "@" + testVars.getDomain() + " Finished");
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
