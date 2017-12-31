package EMS_Tests;

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
* This test tests Timeout value of the system the system
* ----------------
* Tests:
* 	 - Login the system and change the Timeout value to be 3 minutes
* 	 - Create a user via a POST query
* 	 1. Verify that the user is not disconnect after less than 180 seconds from the the last keep-alive
*  	 2. Verify that the user is do disconnect after less than 180 seconds from the the last keep-alive
*  	 3. Delete the created user
* 
* Results:
* 	 1. The system should not logged you off after 2 minutes.
* 	 2. The system should logged you off after all the 3 minutes passed.
* 	 3. User should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test126__timeout {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Map<String, String> 	map;
  String 				userName;
  
  // Default constructor for print the name of the used browser 
  public Test126__timeout(String browser) {
	  
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
	map 	  = new HashMap<String, String>();
	userName  = "tmOut" + testFuncs.getId();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void System_settings_http_https() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set vars and login
	String location 		= "myLocation";
	String phoneNumber		= "+97239764713";
	String timeoutMin		= "3";
	int timeoutInt 			= Integer.valueOf(timeoutMin);
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  
    // Set the Timeout value to be 3 minutes and create a registered-user
	testFuncs.myDebugPrinting("Set the Timeout value to be 3 minutes and create a registered-user");
	setTimeout(timeoutMin);
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp(),
				 				testVars.getPort()    						 ,
				 				"1"				   							 ,
				 				userName		   						     ,
				 				testVars.getDomain()  						 ,
				 				"registered"          						 ,
				 				testVars.getDefPhoneModel()              	 ,
				 				testVars.getDefTenant()               		 ,
				 				"myLocation");
	testFuncs.verifyPostUserCreate(driver, userName, userName, true);
	
    // Set the status of the users be Offline and than Online Again
	testFuncs.myDebugPrinting("Set the status of the users be Offline and than Online Again");
	testFuncs.sendKeepAlivePacket(testVars.getKpAlveBatName(),
			  					  testVars.getIp()               ,
			  					  testVars.getPort()           	 ,
			  					  testFuncs.readFile("mac_1.txt"),
			  					  userName				 		 ,
			  					  testVars.getDefPhoneModel()	 ,
			  					  testVars.getDomain()        	 ,
			  					  "offline"						 ,
			  					  location						 ,
			  					  phoneNumber);
	testFuncs.sendKeepAlivePacket(testVars.getKpAlveBatName(),
			  					  testVars.getIp()               ,
			  					  testVars.getPort()           	 ,
			  					  testFuncs.readFile("mac_1.txt"),
			  					  userName				 		 ,
			  					  testVars.getDefPhoneModel()	 ,
			  					  testVars.getDomain()        	 ,
			  					  "registered"						 ,
			  					  location						 ,
			  					  phoneNumber);
	
    // Step 1 - Verify that the user is NOT disconnect after less than 180 (3 minutes) seconds from the the last keep-alive
	testFuncs.myDebugPrinting("Step 1 - Verify that the user is NOT disconnect after less than 180 (3 minutes) seconds from the the last keep-alive");
	testFuncs.myDebugPrinting("Wait for <"  + ((timeoutInt - 1) * 61 * 1000) + "> milli-seconds" , testVars.logerVars.MINOR);
	testFuncs.myWait((timeoutInt - 1) * 61 * 1000);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 5000);
	String classTxt = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[7]/i")).getAttribute("class");
	testFuncs.myAssertTrue("Registered status was not detected !! <" + classTxt + ">", classTxt.contains("fa-check-square"));
	
	// Step 2 - Verify that the user is disconnect after 180 (3 minutes) seconds from the the last keep-alive
	testFuncs.myDebugPrinting("Step 2 - Verify that the user is disconnect after 180 (3 minutes) seconds from the the last keep-alive");	
	testFuncs.myDebugPrinting("Wait for <"  + (1 * 61 * 1000) + "> milli-seconds" , testVars.logerVars.MINOR);
	testFuncs.myWait(1 * 61 * 1000);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 5000);
	classTxt = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[7]/i")).getAttribute("class");
	testFuncs.myAssertTrue("Disconected status was not detected !! <" + classTxt + ">", classTxt.contains("fa-chain-broken"));	
  }
  
  // Set Timeout value 
  private void setTimeout(String timeoutValue) {
	  
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='disconnected_timeout']"), timeoutValue, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");		
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
  }

  @After
  public void tearDown() throws Exception {
	    
	  // Delete the created users & Set timeout to be 10 again
	  testFuncs.myDebugPrinting("Step 3 - Delete the created users");		
	  testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");    
	  testFuncs.selectMultipleUsers(driver, userName, "1");
	  map.put("usersPrefix"	  , userName);  
	  map.put("usersNumber"	  , "1"); 
	  map.put("startIdx"   	  , String.valueOf("1"));	  
	  map.put("srcUsername"	  , "Finished");	    
	  map.put("action"	 	  , "Delete Users");	  	  
	  map.put("skipVerifyDelete", "true");  
	  testFuncs.setMultipleUsersAction(driver, map);	  
	  userName = userName.toLowerCase();  
	  testFuncs.searchStr(driver, userName + "@" + testVars.getDomain() + " Finished");
	  setTimeout("10");  
	  
	  // Close session
	  driver.quit();
	  System.clearProperty("webdriver.chrome.driver");
	  System.clearProperty("webdriver.ie.driver");    
	  String verificationErrorString = verificationErrors.toString();

	  if (!"".equals(verificationErrorString)) {
    	testFuncs.myFail(verificationErrorString);
    
	  }
  }
}
