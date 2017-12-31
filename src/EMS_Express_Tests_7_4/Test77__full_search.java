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
* This test tests the Full search feature.
* ----------------
* Tests:
*  	 - Login the system via an Administrator user.
*    - Create a user via a POST query and enter the Manage users menu.
*    1. Search the user by its MAC address without check the Search-All option
*    1. Search the user by its MAC address with check check the Search-All option
*    3. Delete the users.
*  
* Results:
*    1. A regular search should not find the user according to its MAC address.
*    2. A full-search should find the user according to its MAC address.
*    3. the users should be deleted successfully.
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test77__full_search {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test77__full_search(String browser) {
	  
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
  public void Full_search() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set vars + login
	  String prefix 		   = "fullsearch";
	  String srcUserName1      = prefix + "1" + testFuncs.getId();
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");

	  // Create a user with a POST query
	  testFuncs.myDebugPrinting("Create a user with a POST query");
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       ,
					 										   testVars.getPort()     ,
					 									       "1"				      ,
					 									       srcUserName1            ,
					 										   testVars.getDomain()   ,
					 										   "registered"           ,
					 										   "430HD"                ,
					 										   testVars.getDefTenant(),
					 										   "location");
	  testFuncs.verifyPostUserCreate(driver, srcUserName1 + "@" + testVars.getDomain(), srcUserName1, true);
	  String userMacAddr = testFuncs.readFile("mac_1.txt");
	  testFuncs.myDebugPrinting("userMacAddr - " + userMacAddr, testVars.logerVars.MINOR);

	  // Step 1 - Search the user by its MAC address without check the Search all
	  testFuncs.myDebugPrinting(" Step 1 - Search the user by its MAC address without check the Search all");
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchtext']"), userMacAddr, 7000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button"), 7000);
	  testFuncs.searchStr(driver, "No users found");
	  
	  // Create a user with a POST query
	  testFuncs.myDebugPrinting("Create a user with a POST query");
	  String srcUserName2      = prefix + "2" + testFuncs.getId();
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       ,
					 										   testVars.getPort()     ,
					 									       "1"				      ,
					 									       srcUserName2           ,
					 										   testVars.getDomain()   ,
					 										   "registered"           ,
					 										   "430HD"                ,
					 										   testVars.getDefTenant(),
					 										   "location");
	  testFuncs.verifyPostUserCreate(driver, srcUserName2 + "@" + testVars.getDomain(), srcUserName2, true);
	  userMacAddr = testFuncs.readFile("mac_1.txt");
	  testFuncs.myDebugPrinting("userMacAddr - " + userMacAddr, testVars.logerVars.MINOR);

	  // Step 2 - Search the user by its MAC address with check the Search all
	  testFuncs.myDebugPrinting(" Step 2 - Search the user by its MAC address with check the Search all");
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchtext']"), userMacAddr, 7000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='search_concept_users']")							, 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='all_search']/li[2]/a")							, 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button"), 7000);
	  srcUserName2 = srcUserName2.toLowerCase();
	  testFuncs.searchStr(driver, srcUserName2 + "@" + testVars.getDomain() + "\n" + srcUserName2);
	     
	  // Step 4 - Delete the created users
	  testFuncs.myDebugPrinting("Step 4 - Delete the created users");		
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
	  srcUserName1 = srcUserName1.toLowerCase();
	  testFuncs.searchStr(driver, srcUserName1 + "@" + testVars.getDomain() + " Finished");	  
	  testFuncs.searchStr(driver, srcUserName2 + "@" + testVars.getDomain() + " Finished");
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
