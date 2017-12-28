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
* This test tests the change-phone-type and reset device of multiple-devices-changes menu
* ----------------
* Tests:
* 	 - Enter Manage multiple devices changes menu.
* 	 1. Create several users using POST query.
* 	 2. change phone type.
* 	 3.1 reset device - Graceful mode.
* 	 3.2 reset device - Force mode.
* 	 3.3 reset device - Scheduled mode.
* 	 4. Delete the users.
* 
* Results:
* 	 1. Create users should end successfully.
*    2. Devices phone type should be end successfully.
*    3. Devices should be reseted successfully.
*    4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test21__multiple_devices_change_phone_type_reset_device {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test21__multiple_devices_change_phone_type_reset_device(String browser) {
	  
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
  public void Manage_multiple_devices_change_phone_type() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String usersNumber   = "3";
	int usStartIdx 		 = 1;
	String dispPrefix    = "chTemplateResDev" + testFuncs.getId();
    Map<String, String> map = new HashMap<String, String>();

    // Step 1 - Create several users using POST query
	testFuncs.myDebugPrinting("Step 1 - Create several users using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumber		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true);
    
    // Step 2 - change Template
  	testFuncs.myDebugPrinting("Step 2 - change Template");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"     ,  "Change Template");
    map.put("phoneType"  ,  testVars.getDefTemplates(0));
    testFuncs.setMultipleDevicesAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1 " + testFuncs.readFile("mac_1.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_2 " + testFuncs.readFile("mac_2.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_3 " + testFuncs.readFile("mac_3.txt"));
    
    // Step 3.1 - reset device - Graceful mode
	testFuncs.myDebugPrinting("Step 3.1 - reset device - Graceful mode");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"     ,  "Restart Devices");
    map.put("resMode"    ,  "Graceful");
    testFuncs.setMultipleDevicesAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1 " + testFuncs.readFile("mac_1.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_2 " + testFuncs.readFile("mac_2.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_3 " + testFuncs.readFile("mac_3.txt"));
        
    // Step 3.2 - reset device - Force mode
  	testFuncs.myDebugPrinting("Step 3.2 - reset device - Force mode");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("resMode"    ,  "Force");
    testFuncs.setMultipleDevicesAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1 " + testFuncs.readFile("mac_1.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_2 " + testFuncs.readFile("mac_2.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_3 " + testFuncs.readFile("mac_3.txt"));
    
    // Step 3.3 - reset device - Scheduled mode
  	testFuncs.myDebugPrinting("Step 3.3 - reset device - Scheduled mode");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("resMode"    ,  "Scheduled");
    map.put("schTime"    ,  "2 sec");
    testFuncs.setMultipleDevicesAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1 " + testFuncs.readFile("mac_1.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_2 " + testFuncs.readFile("mac_2.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_3 " + testFuncs.readFile("mac_3.txt"));

    // Step 4 - Delete the created users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("usersPrefix"	  , dispPrefix + "_");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    dispPrefix = dispPrefix.toLowerCase();
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
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
