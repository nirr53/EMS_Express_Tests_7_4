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
* 	- Create a registered user via POST query
* 	 1. Create an alarm that sent from an unknown mac address
* 	 2. Create an alarm with speicel characters
* 	 3. Delete all the created Alarms
* 	 4. Delete the created user 
* 
* Results:
* 	1. Alert should not be displayed
* 	2. Alerts should be created for all characters
*   3. All the alerts should be created successfully.
*   4. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test108__alarms_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test108__alarms_tests(String browser) {
	  
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
	String Id 		= testFuncs.getId();
	String username = "regAlert" + Id; 
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	
    // Create a registered and unregisterd users using POST method
	testFuncs.myDebugPrinting("Create a registered and unregisterd users using POST method");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 "1"				    	,
			 												 username 					,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, username, username, true);
	String mac1 = testFuncs.readFile("mac_1.txt");

	// Step 1 - Create an alarm that sent from an unknown mac address
	testFuncs.myDebugPrinting("Step 1 - Create an alarm that sent from an unknown mac address");
	String alertPrefix = "unknownMac";
	String []alertsForSearch = {alertPrefix + "_" + Id};
	testFuncs.enterMenu(driver, "Dashboard_Alarms", "Export");	
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  "00908f123456"								 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch[0]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
		
	// Search the alerts according to their description
	testFuncs.myDebugPrinting("Search the alerts according to their description", testVars.logerVars.MINOR);
	testFuncs.searchAlarm(driver, "Description", alertPrefix  , alertsForSearch);
	
	// Step 2 - Create an alarm with differnt speicel characterss
	testFuncs.myDebugPrinting("Step 2 - Create an alarm with differnt speicel characterss");	
	String alertPrefix2 = "speicelCharcaters_";
	String []alertsForSearch2 = {alertPrefix2 + "!#$/" + "_"  + Id,
								 alertPrefix2 + "=?^`" + "_"  + Id,
								 alertPrefix2 + "{|}~" + "_"  + Id,
								 alertPrefix2 + ";*+"  + "_" + Id};
	testFuncs.enterMenu(driver, "Dashboard_Alarms", "Export");
	
	// Create alarms with differnt sets of characters
	testFuncs.myDebugPrinting("Create alarms with differnt sets of characters", testVars.logerVars.MINOR);
	for (int i = 0; i < 4; ++i) {
		
		testFuncs.myDebugPrinting("alertsForSearch2[i] - " + alertsForSearch2[i], testVars.logerVars.MINOR);
		testFuncs.createAlarmViaPost(testVars.getAlarmsBatName()					,
									 testVars.getIp()  								,						
									 testVars.getPort()								,
									 mac1								 			,
									 "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
									 alertsForSearch2[i]				    		,
									 "2017-07-217T12:24:18"						 	,
									 "info2"	 									,
									 "info1"	 									,
				  					 "minor");
		
		// Search the created alert
		String[] searchedAlerts = {alertsForSearch2[i]};
		testFuncs.myDebugPrinting("Search the created alert", testVars.logerVars.MINOR);
		testFuncs.searchAlarm(driver, "Description", alertPrefix2  , searchedAlerts);
	}
	
	// Step 3 - Delete the created alarms
	testFuncs.myDebugPrinting("Step 3 - Delete the created alarms");
	for (int i = 0; i < 4; ++i) {
		
		testFuncs.myDebugPrinting("Delete alert " +  alertsForSearch2[i], testVars.logerVars.MINOR);
		testFuncs.deleteAlarm(driver, alertsForSearch2[i]);	
	}
	
	// Step 4 - Delete the created and user
	testFuncs.myDebugPrinting("Step 4 - Delete the created user");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");    
	testFuncs.selectMultipleUsers(driver, username, "1");
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersPrefix"	  , username);  
	map.put("usersNumber"	  , "1"); 
	map.put("startIdx"   	  , String.valueOf("1"));	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");	  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);
	username = username.toLowerCase();
	testFuncs.searchStr(driver, username + "@" + testVars.getDomain() + " Finished");	  
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
