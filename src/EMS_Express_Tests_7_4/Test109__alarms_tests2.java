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
* 	- Create an unregisterd user via POST query
* 	   1. Create an alarm that sent from a registerd/ un-registerd user
* 	   2. Create alarms on different severity types
* 	 3.1  Create an alarm with 67 characters long description
* 	 3.2  Create an alarm with more than 67 characters long description
* 	   4. Delete all the created Alarms
* 	   5. Delete the created users and alarms

* 
* Results:
* 	  1. Alert should be created for registerd and un-registerd users
* 	  2. Alerts should be created for all severity types
*   3.1  Only the first 67 charcters of the description should be displayed.
*   3.2  Only the first 67 charcters of the description should be displayed.
*     4. All the alerts should be created successfully.
*     5. All created data should be deleted.

* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test109__alarms_tests2 {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test109__alarms_tests2(String browser) {
	  
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
	String Id = testFuncs.getId();
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	
    // Create a registered and unregisterd users using POST method
	testFuncs.myDebugPrinting("Create a registered and unregisterd users using POST method");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 "1"				    	,
			 												 "regAlert" + Id			,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, "regAlert" + Id, "regAlert" + Id, true);
	String mac1 = testFuncs.readFile("mac_1.txt");
    testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 												 testVars.getPort()    	    ,
			 												 " 1"				   	    ,
			 												 "unRegAlert" + Id     		,
			 												 testVars.getDomain()       ,
			 												 "unregistered"             ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, "unRegAlert" + Id, "unRegAlert" + Id, false);
	String mac2 = testFuncs.readFile("mac_1.txt");

	// Step 1 - Enter the alarms menu and create alarms that sent from a registerd/ un-registerd user
	testFuncs.myDebugPrinting("Step 1 - Enter the alarms menu and create alarms that sent from a registerd/ un-registerd user");
	String regAlertPrefix = "regAlert";
	String []alertsForSearch = {regAlertPrefix + "_1_" + Id, regAlertPrefix + "_2_" + Id};
	testFuncs.enterMenu(driver, "Dashboard_Alarms", "Export");	
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac1								 			 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch[0]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac2								 			 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch[1]			    		 	 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
		
	// Search the alerts according to their description
	testFuncs.myDebugPrinting("Search the alerts according to their description", testVars.logerVars.MINOR);
	testFuncs.searchAlarm(driver, "Description", regAlertPrefix  , alertsForSearch);
	
	// Step 2 - Create alarms on different severity types
	testFuncs.myDebugPrinting("Step 2 - Create alarms on different severity types");
	String[] alertsSeverity = {"info", "warning", "minor", "Major", "Critical"};

	for (int i = 0; i < 5; ++i) {
		
		testFuncs.myDebugPrinting("--------");
		testFuncs.myDebugPrinting("Create an alarm of <" + alertsSeverity[i] + "> severity", testVars.logerVars.NORMAL);
		String[] alarmNames   = {"severityTestAlarm_" + Id + "_" +	alertsSeverity[i]};
		testFuncs.myDebugPrinting("alarmNames[0] - " 	 + alarmNames[0]    , testVars.logerVars.MINOR);
		testFuncs.myDebugPrinting("alertsSeverity[i] - " + alertsSeverity[i], testVars.logerVars.MINOR);
		testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
																  testVars.getPort()							 ,
																  mac1								 			 ,
																  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
																  alarmNames[0]									 ,
																  "2017-07-217T12:24:18"						 ,
																  "info2"	 									 ,
																  "info1"	 									 ,
																  alertsSeverity[i]);
		testFuncs.searchAlarm(driver, "Severity", alertsSeverity[i], alarmNames);

	}

	// Step 3.1 - Create an alarm with 67 characters long description
	testFuncs.myDebugPrinting("Step 3.1 - Create an alarm with 67 characters long description");
	String longDescriptionFullString = "very long description_" + Id + "_abcdefghijklmnopqrstuvwxyz 1234567890 1234567890 1234567890";
	String []alertsForSearch2 = {longDescriptionFullString.substring(0, 67)};
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac1								 			 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch2[0]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
	testFuncs.searchAlarm(driver, "Description", alertsForSearch2[0]  , alertsForSearch2);
	
	// Step 3.2 - Create an alarm with more than 67 characters long description
	testFuncs.myDebugPrinting("Step 3.2 - Create an alarm with more than 67 characters long description");
	String []alertsForSearch3 = {longDescriptionFullString};
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac1								 			 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch3[0]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
	testFuncs.searchAlarm(driver, "Description", alertsForSearch3[0]  , alertsForSearch3);
	
	// Step 4 - Delete the created alarms
	testFuncs.myDebugPrinting("Step 4 - Delete the created alarms");
	testFuncs.deleteAlarm(driver, alertsForSearch[0]);
	testFuncs.deleteAlarm(driver, alertsForSearch[1]);
	for (int i = 0; i < 5; ++i) {
		
		String[] alarmNames   = {"severityTestAlarm_" + Id + "_" +	alertsSeverity[i]};
		testFuncs.deleteAlarm(driver, alarmNames[0]);
	}
	testFuncs.deleteAlarm(driver, alertsForSearch3[0]);
	testFuncs.deleteAlarm(driver, alertsForSearch2[0]);
	
	// Step 6 - Delete the created users
	testFuncs.myDebugPrinting("Step 6 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");    
	testFuncs.selectMultipleUsers(driver, regAlertPrefix, "1");
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersPrefix"	  , regAlertPrefix);  
	map.put("usersNumber"	  , "2"); 
	map.put("startIdx"   	  , String.valueOf("1"));	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");	  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);
	regAlertPrefix = regAlertPrefix.toLowerCase();
	testFuncs.searchStr(driver, regAlertPrefix + Id + "@" + testVars.getDomain() + " Finished");	
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
