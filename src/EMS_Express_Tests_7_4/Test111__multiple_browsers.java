package EMS_Express_Tests_7_4;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

/**
* ----------------
* This test tests the behavior of the system via multiple browsers
* -----------------
* Tests:
*    1. login via HTTPS via multiple browsers
*    2. Verify that actions on one browser not effect on the other browsers
* 
* Results:
*    1. Login should succeed for all the browsers
*    2. An action on one browsers should not been take place on the other browsers.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test111__multiple_browsers {
	
  private WebDriver 	driver[];
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  static int 			browsersNumber = 3;

  // Default constructor for print the name of the used browser 
  public Test111__multiple_browsers(String browser) {
	  
	  Log.info("Browser - "  + browser);
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
	driver = new WebDriver[browsersNumber];
	for (int i = 0; i < browsersNumber; ++i) {
		
	    testFuncs.myDebugPrinting("Create browser number #" + i, testVars.logerVars.MINOR);
		driver[i] = testFuncs.defineUsedBrowser(this.usedBrowser);
	    driver[i].manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);	
	}
  }

  @Test
  public void Multiple_brosers_login() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
    // Step 1 - login via HTTPS via multiple browsers
    testFuncs.myDebugPrinting("Step 1 - login via HTTPS"); 
    
    // Enter the welcome page
	for (int i = 0; i < browsersNumber; ++i) {	
	    testFuncs.myDebugPrinting("Login via browser number #" + i, testVars.logerVars.MINOR);
	    driver[i].get("https://" + testVars.getUrl());
	    testFuncs.myWait(3000); 
	    testFuncs.searchStr(driver[i], testVars.getMainPageStr());  
	}
	
	// Fill login data and ask for login
	for (int i = 0; i < browsersNumber; ++i) {	
	  	testFuncs.mySendKeys(driver[i], By.xpath("//*[@id='loginform']/div[1]/input"), testVars.getSysUsername(), 500);
	  	testFuncs.mySendKeys(driver[i], By.xpath("//*[@id='loginform']/div[2]/input"), testVars.getSysPassword(), 500);    	  
	  	testFuncs.myClick(driver[i]   , By.xpath("//*[@id='loginform']/div[4]/div[2]/button"), 3000);
	}
	
	// Check that login was made via all browsers
	for (int i = 0; i < browsersNumber; ++i) {		
		testFuncs.searchStr(driver[i], testVars.getSysMainStr());
	}
	
    // Step 2 - verify that actions on one browser not effect on the other browsers
    testFuncs.myDebugPrinting("Step 2 - verify that actions on one browser not effect on the other browsers"); 
	
    // Browser #0  enters the Manage-users menu, the other two stays at welcome page.
    testFuncs.enterMenu(driver[0], "Setup_Manage_users", "New User");
	testFuncs.searchStr(driver[1], testVars.getSysMainStr());
	testFuncs.searchStr(driver[2], testVars.getSysMainStr());
	
	// Browser #1 enters the phone-firmware-files menu, browser #0 still at Manage-users manu, and browser #2 at welcome page.
	testFuncs.enterMenu(driver[1], "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");
	testFuncs.searchStr(driver[0], "New User");
	testFuncs.searchStr(driver[2], testVars.getSysMainStr());
	
	// Browser #2 enters the Alarms menu,  #0 still at Manage-users manu, and browser #1 at phone-fiwmware-files menu.
	testFuncs.enterMenu(driver[2], "Dashboard_Alarms", "Export");	
	testFuncs.searchStr(driver[0], "New User");
	testFuncs.searchStr(driver[1], "Phone firmware files");
  }

  @After
  public void tearDown() throws Exception {
	      
	for (int i = 0; i < browsersNumber; ++i) {	
		
		driver[i].quit();	
	}
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}