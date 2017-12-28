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
* This test tests the Stop and continue feature
* ----------------
* Tests:
* 	 - Enter the Import users menu
* 	 - Create several users via POST query
* 	 1. Enter the Manage-multiple-users menu and delete the import users.
*       Stop the import at the middle and after some seconds continue it.
*       Verify that all the users were deleted.
*       
* Results:
* 	 1. After the 'stop' pressing, users should not be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test34__import_stop_continue {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test34__import_stop_continue(String browser) {
	  
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
  public void Manage_multiple_users_stop_and_continue() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String usersNumber   	= "6";
	int usStartIdx 		 	= 1;
	String dispPrefix   	= "stopAndContUser" + testFuncs.getId();
    Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");
	
    // Create several users using POST query
	testFuncs.myDebugPrinting("Create several users using POST query");
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
  	
    // Step 1 - Delete users, stop at middle and than continue delete
  	testFuncs.myDebugPrinting("Step 1 - Delete users, stop at middle and than continue delete");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, "6");
    map.put("srcUsername"	  , "Finished");
    map.put("usersPrefix"	  , dispPrefix);
    map.put("action"	      , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    dispPrefix = dispPrefix.toLowerCase();
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_4@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_5@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_6@" + testVars.getDomain() + " Finished");
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
