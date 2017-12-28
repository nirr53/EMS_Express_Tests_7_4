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
* This test tests the add-device mechanism
* ----------------
* Tests:
* 	 - Create a user manually.
* 	 1. Add a device to it.
* 	 2. Delete the created user.
* 
* Results:
* 	 - User should be added successfully.
* 	 1. Device should be added successfully.
* 	 2. Delete the created user.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test6__add_device {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test6__add_device(String browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
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
  public void Device_add() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String Id         = testFuncs.getId();
	String userName   = "manual user"   + Id;
	String deviceName = "manual device" + Id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);

    // Create user manually
	testFuncs.myDebugPrinting("Create user manually");
	testFuncs.addUser(driver, userName, "1q2w3e$r", "displayName" + Id, testVars.getDefTenant());
	testFuncs.myWait(3000);
		
     // Step 1 - Create device manually
	testFuncs.myDebugPrinting("Step 1 - Create device manually");
	testFuncs.addDevice(driver, userName, deviceName, testVars.getDefTemplates(0), testFuncs.getMacAddress(), testVars.getDefPhoneModel());
	
    // Step 2 - Delete the created user
  	testFuncs.myDebugPrinting("Step 2 - Delete the created user");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, userName, "1");
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber"     ,  "1"); 
    map.put("startIdx"        ,  String.valueOf(1));
    map.put("usersPrefix"	  , userName);
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    userName = userName.toLowerCase();
    testFuncs.searchStr(driver, userName + " Finished");
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
