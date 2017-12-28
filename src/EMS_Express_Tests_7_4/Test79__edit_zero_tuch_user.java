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
* This test tests an edit of zero-touch user
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a user using POST query.
* 	 2. Try to edit its username and display name
*    3. Try to edit its device name
* 	 4. Delete the user.
* 
* Results:
* 	 1. User should be created successfully.
* 	 2. The usernamd and display name should be edited successfully.
* 	 3. The device-name should be edited successfully
* 	 4. The user should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test79__edit_zero_tuch_user {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test79__edit_zero_tuch_user(String browser) {
	  
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
  public void Edit_zero_touch_user() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars and login
	String Id           = testFuncs.getId();
	String prefixName   = "editztuser";
	String userName     = prefixName + Id;
	String userFullName = userName + "@" + testVars.getDomain();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
		
	// Step 1 - Create a user using POST query
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query");
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
    
    // Step 2 - Try to edit its username and display name
	testFuncs.myDebugPrinting("Step 2 - Try to edit its username and display name");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.searchUser(driver, userName); 
	testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[1]/td[9]/a[2]"), 3000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3/label", "Edit User " + userName);
	String edDispName = "editedDN" + Id;
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='displayname']"), edDispName ,2000);				
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 10000);
  	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update User");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "User " + userName + " successfully updated.");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
  	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Generate Configuration Files");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[2]")							   , 5000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
	testFuncs.searchUser(driver, userFullName); 
	testFuncs.searchStr(driver , edDispName);
	
    // Step 3 - Try to edit its device name
 	testFuncs.myDebugPrinting("Step 3 - Try to edit its device name");
	testFuncs.myClick(driver, By.xpath("//*[@id='"   + userFullName + "tree']")		  												   , 3000);
	testFuncs.myClick(driver, By.xpath("//*[@id='tr" + userFullName + "device']/td[2]/table/tbody/tr/td/div/table/tbody/tr[5]/td/a[1]"), 3000);
	edDispName = "edited DN22" + Id;
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/form/div/div[2]/div[1]/h3", "Edit device of user " + userFullName);
	testFuncs.mySendKeys(driver, By.xpath("	//*[@id='display_name']"), edDispName ,2000);	
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/form/div/div[2]/div[3]/button[2]"), 5000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[2]")									, 5000);
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.searchUser(driver, userName); 	
	testFuncs.myClick(driver, By.xpath("//*[@id='"   + userFullName + "tree']")		  												   , 3000);
	testFuncs.searchStr(driver , edDispName);
	
    // Step 4 - Delete the users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, userName, "1");
    map.put("usersPrefix"	  , userName);
    map.put("usersNumber"	  , "1"); 
    map.put("startIdx"   	  , "1");
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, userFullName + " Finished");
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
