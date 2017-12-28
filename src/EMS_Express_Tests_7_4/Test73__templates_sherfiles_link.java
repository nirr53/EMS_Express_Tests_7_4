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
* This test tests the Sharefiles menu
* ----------------
* Tests:
* 	 - Enter the Templates menu, and press the Sharefiles link
* 	 1. Verify the Sharefiles menu and download a sharefiles package
* 
* Results:
* 	 1. Sharefiles menu should be detected. Sharefiles package should be downloaded successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test73__templates_sherfiles_link {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test73__templates_sherfiles_link(String browser) {
	  
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
  public void Templates_sharefiles_link() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set vars
	  Map<String, String> map = new HashMap<String, String>();
	  map.put("isRegionDefault"		     ,  "false");
	  map.put("cloneFromtemplate"        ,  ""); 
	  map.put("isDownloadSharedTemplates",  "true"); 
	  
	  // Step 1 - Login, enter the Phone Templates menu and add A Template
	  testFuncs.myDebugPrinting("Step 1 - Login and enter the Phone Templates menu");
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  	
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
	  map.put("cloneFromtemplate", "Audiocodes_" + "430HD" + "_LYNC");
	  testFuncs.addTemplate(driver, "my430HD Template_" + testFuncs.getId(), "my430HD desc", testVars.getNonDefTenant(0), "430HD", map);
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
