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
import org.openqa.selenium.support.ui.Select;

/**
* ----------------
* This test tests a create of users with speicel languages
* ----------------
* Tests:
* 	 - Create a user+device on default tenant using POST query
* 	 - Create user+device on non-default tenant using POST query
* 	 1. Filter the devices by Tenant
* 	 2. Clear filter On Device-Status menu
* 	 3. Search for non-exisitng devices
* 	 4. Delete the users.
* 
* Results:
*	 1. Only the Tenant that belongs to the filtered tenant should be detected.
* 	 2. All Devices should be detected
*	 3. None of the device should be detected.
* 	 4. All users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test105__device_status_filter_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test105__device_status_filter_tests(String browser) {
	  
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
  public void Filter_Tests() throws Exception {

	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	int usStartIdx 		 	= 1;
	int usersNumber			= 1;
	String usersNumberStr   = String.valueOf(usersNumber);
	String dispPrefix   	= "__defTenUsers" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumberStr); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");
	
    // Create a user+device on default tenant using POST query
	testFuncs.myDebugPrinting("Create a user+device on default tenant using POST query");
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
	testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true); 
	
    // Create a user+device on non-default tenant using POST query
	testFuncs.myDebugPrinting("Create a user+device on non-default tenant using POST query");
	String dispPrefix2 = "__nonDefTenUsers" + testFuncs.getId();
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumberStr		        ,
			 dispPrefix2  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getNonDefTenant(0),
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver,  dispPrefix2,  dispPrefix2, true); 
	   	
	// Step 1 - Filter by Tenant
	testFuncs.myDebugPrinting("Step 1 - Filter by Tenant");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a"), 3000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[14]/div/button[2]"), 3000);
	new Select(driver.findElement(By.xpath("//*[@id='inputTenant']"))).selectByVisibleText(testVars.getDefTenant().toLowerCase());
	testFuncs.myWait(2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[14]/div/button[1]"), 7000);
	testFuncs.searchStr(driver, dispPrefix);
	testFuncs.myAssertTrue("User with other tenant was detected !!", !driver.findElement(By.tagName("body")).getText().contains(dispPrefix2));

	// Step 2 - Clear filter On Device-Status menu
	testFuncs.myDebugPrinting("Step 2 - Clear filter On filter menu");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[4]")			 , 5000);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a"), 3000);
	new Select(driver.findElement(By.xpath("//*[@id='inputCount']"))).selectByVisibleText("500");
	testFuncs.myWait(2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[14]/div/button[1]"), 7000);
	testFuncs.searchStr(driver, dispPrefix);
	testFuncs.searchStr(driver, dispPrefix2);
	
	// Step 3 - Search for non-exisiting devices
	testFuncs.myDebugPrinting("Step 3 - Search for non-exisiting devices");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a"), 3000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[14]/div/button[2]"), 3000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='inputUser']"), "abcdefghyhjdhaj" , 2000);
	new Select(driver.findElement(By.xpath("//*[@id='inputCount']"))).selectByVisibleText("500");
	testFuncs.myWait(2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[14]/div/button[1]"), 7000);
	testFuncs.searchStr(driver, "There are no devices that fit this search criteria");
	
    // Step 4 - Delete the created users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, "__", usersNumberStr);
    map.put("usersNumber"	  , usersNumberStr); 
    map.put("usersPrefix"	  , "__");
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
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