package EMS_Express_Tests_7_4;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

/**
* ----------------
* This test tests the new buttons of the top layer of the EMS-web-mannager.
* ----------------
* Tests:
* 	 - Login the EMS.
* 	 1. Press the Generate Configuration button.
* 	 2. Press the Network Topology button.
* 
* Results:
* 	 1. The user should reach the Manage-multiple-users menu when 'Generate Configuration' action is selected.
* 	 2. User should reach the 'Network Topology' menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test116__upper_menu_buttons {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test116__upper_menu_buttons(String browser) {
	  
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
  public void Upper_menu_buttons() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Login
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	
    // Step 1 - Press the Generate Configuration button
	testFuncs.myDebugPrinting("Step 1 - Press the Generate Configuration button");
	testFuncs.myClick(driver, By.xpath("//*[@id='subHeader']/a[1]"), 5000);
	testFuncs.searchStr(driver, "Manage Multiple Users - Generate Configuration");	
	String selectAction = new Select(driver.findElement(By.xpath("//*[@id='action']"))).getFirstSelectedOption().getText();
	testFuncs.myDebugPrinting("selectAction - " + selectAction, testVars.logerVars.MINOR);
	testFuncs.myAssertTrue("Generate configuration option is not selected !! (selectAction - " + selectAction + ")", selectAction.equals("Generate IP Phones Configuration Files"));
			   
	// Step 2 - Press the Network Topology button
	testFuncs.myDebugPrinting("Step 2 - Press the Network Topology button");
	testFuncs.pressNetworkTopologyButton(driver);
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
