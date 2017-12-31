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
* This test tests the different delete-devices options.
* ----------------
* Tests:
*  	 - Login the system via an Administrator user.
*    - Create a user via a POST query and enter the Manage users menu.
*    1. Delete device at 'manage users'
*    2. Delete device at Manage multiple devices
*    3. Delete the created users
*  
* Results:
*    1. After delete, device should NOT be displayed at Manage users or Manage delete devices.
*    2. After delete, device should NOT be displayed at Manage users or Manage delete devices.
*    3. Users should be created successfully,
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test131__delete_device_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test131__delete_device_tests(String browser) {
	  
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
  public void Delete_devices_tests() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set vars + login
	  Map<String, String> map = new HashMap<String, String>();
	  String prefix    = "delete";
	  String id        = testFuncs.getId();
	  String userName1 = prefix + "man"  + id;
	  String userName2 = prefix + "mlti" + id;
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser); 

	  // Create first user with a POST query
	  testFuncs.myDebugPrinting("Create a user with a POST query");
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       ,
					 										   testVars.getPort()     ,
					 									       "1"				      ,
					 									       userName1            ,
					 										   testVars.getDomain()   ,
					 										   "registered"           ,
					 										   "420HD"                ,
					 										   testVars.getDefTenant(),
					 										   "location");
	  testFuncs.verifyPostUserCreate(driver, userName1 + "@" + testVars.getDomain(), userName1, true);
	  
	  // Create second user with a POST query
	  testFuncs.myDebugPrinting("Create second user with a POST query");
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       ,
					 										   testVars.getPort()     ,
					 									       "1"				      ,
					 									       userName2            ,
					 										   testVars.getDomain()   ,
					 										   "registered"           ,
					 										   "420HD"                ,
					 										   testVars.getDefTenant(),
					 										   "location");
	  testFuncs.verifyPostUserCreate(driver, userName2 + "@" + testVars.getDomain(), userName2, true);

	  // Step 1 - Delete a device via Manage user page
	  testFuncs.myDebugPrinting("Step 1 - Delete a device via Manage user page");
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");	
	  testFuncs.searchUser(driver, userName1);   
	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[1]/td[2]/a"), 2000);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='tr" + userName1 + "@" + testVars.getDomain() + "device']/td[2]/table/tbody/tr/td/div/table/tbody/tr[5]/td/a[2]"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Device");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete device?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
	  
	  // Verify delete
	  testFuncs.searchUser(driver, userName1);
	  String deletedDevice = driver.findElement(By.xpath("//*[@id='results']/tbody/tr/td[2]")).getText();
	  testFuncs.myAssertTrue("Device was not deleted !! <" + deletedDevice + ">", deletedDevice.contains("---"));
	  testFuncs.myDebugPrinting("deletedDevice - " + deletedDevice);
	  testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
	  testFuncs.selectMultipleUsers(driver, userName1, "0");

	  // Step 2 - Delete a device via Manage multiple devices
	  testFuncs.myDebugPrinting("Step 1 - Delete a device via Manage user page");
	  testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
	  testFuncs.selectMultipleUsers(driver, userName2, "1");
	  map.put("action"     ,  "Delete Devices");  
	  testFuncs.setMultipleDevicesAction(driver, map);    
	  testFuncs.searchStr(driver, userName2 + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));

	  // Verify delete
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");	
	  testFuncs.searchUser(driver, userName2); 
	  deletedDevice = driver.findElement(By.xpath("//*[@id='results']/tbody/tr/td[2]")).getText(); 
	  testFuncs.myAssertTrue("Device was not deleted !! <" + deletedDevice + ">", deletedDevice.contains("---"));
	     
	  // Step 3 - Delete the created users
	  testFuncs.myDebugPrinting("Step 3 - Delete the created users");		
	  testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");  
	  testFuncs.selectMultipleUsers(driver, id, "2");
	  map.put("usersPrefix"	  , prefix);  
	  map.put("usersNumber"	  , "2"); 
	  map.put("startIdx"   	  , String.valueOf("1"));	  
	  map.put("srcUsername"	  , "Finished");	  
	  map.put("action"	 	  , "Delete Users");	  
	  map.put("skipVerifyDelete", "true");	  
	  testFuncs.setMultipleUsersAction(driver, map);
	  testFuncs.searchStr(driver, userName1 + "@" + testVars.getDomain() + " Finished");	  
	  testFuncs.searchStr(driver, userName2 + "@" + testVars.getDomain() + " Finished");
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
