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
* This test tests  the nickname feature
* ----------------
* Tests:
* 	 - Create a several users + devices using POST query
* 	 1. Select one of the devices and try to edit its nickname
* 	 2. Select several devices and try to add a nickname to them
* 	 3. Delete the users.
* 
* Results:
* 	 1. The edit should end successfully
*	 2. The add should be forbidden.
* 	 3. All users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test106__device_status_nickname_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test106__device_status_nickname_tests(String browser) {
	  
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
  public void Nickname_Tests() throws Exception {

	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	int usStartIdx 		 	= 1;
	String usersNumberStr   = String.valueOf(3);
	String Id 				= testFuncs.getId();
	String dispPrefix   	= "nickname" + Id;
	String editedUser		= dispPrefix + "_1";
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumberStr); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");
	
    // Create users + devices using POST query
	testFuncs.myDebugPrinting("Create a users + devices using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumberStr		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUsersCreate(driver,  dispPrefix,  dispPrefix, true, Integer.valueOf(usersNumberStr));	
	   
	// Step 1 - Add and edit a nickname for one of the devices
	testFuncs.myDebugPrinting("Step 1 - Add and edit a nickname for one of the devices");
	searchAndSelectDevice(driver, editedUser);
	setDeviceNickname(editedUser, "nickname" + Id);
	searchAndSelectDevice(driver, editedUser);
	setDeviceNickname(editedUser, "new Nickname" + Id);
	
	// Step 2 - Verify that you cannot edit a nickname while several devices are selected
	testFuncs.myDebugPrinting("Step 2 - Verify that you cannot edit a nickname while several devices are selected");
	searchAndSelectDevice(driver, dispPrefix);
	testFuncs.myClick(driver, By.xpath("//*[@id='selectall']"), 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);  
	testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[5]/a"), 2000);	  
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Nickname");	  
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Nickname can be updated to one device at a time."); 
	testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div/button[1]"), 5000);	  	
				
    // Step 3 - Delete the created users
  	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumberStr);
    map.put("usersNumber"	  , usersNumberStr); 
    map.put("usersPrefix"	  , dispPrefix);
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
  }
  
  // Set nickname
  private void setDeviceNickname(String deviceName, String nickname) {
	  
	  // Set nickname
	  testFuncs.myDebugPrinting("set nickname for device", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[5]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Nickname");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Nickname"); 
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='mtext']"), nickname, 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div/button[1]"), 10000);  
	  	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", testVars.logerVars.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " [" + nickname + "]");  
  }
  
  // Search for a device and select it via Select-All checkbox
  private void searchAndSelectDevice(WebDriver driver, String userName) {
	  
	  // Search device
	  testFuncs.myDebugPrinting("Search device", testVars.logerVars.NORMAL);
	  testFuncs.enterMenu(driver , "Monitor_device_status", "Devices Status");   
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + userName.trim(), 5000);
	  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	        
	  testFuncs.myWait(7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[8]", userName.trim());
	  
	  // Select the searched device via check Select-All check-box
	  testFuncs.myDebugPrinting("Select the searched device via check Select-All check-box", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='table']/tbody[1]/tr/td[2]/input"), 3000);  
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