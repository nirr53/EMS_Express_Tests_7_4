package EMS_Tests;

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
* This test tests a create of Template with special characters
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a Template with !#$ characters
* 	 2. Create a Template with /=? characters
* 	 3. Create a Template with ^_` characters
* 	 4. Create a Template with {|} characters
* 	 5. Create a Template with ~;  characters
* 	 6. Create a Template with *   characters
* 	 7. Create a Template with +;  characters
*  	 8. Create a Template with '   characters
* 	 9. Delete the Templates.
* 
* Results:
* 	 1-8. All the Templates should be created successfully.
* 	   9. All Templates should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test127__templates_with_speicel_characters {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test127__templates_with_speicel_characters(String browser) {
	  
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
  public void Speicel_characters_templates() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String Id 			  = testFuncs.getId();
	String type			  = "430HD";
	String prefixName     = "sChrsTmpNme_" + Id + "_";
	String prefixDesc     = "sChrsTmpDsc_" + Id + "_";
	String suffixes[]     = {"!#$", "/=?", "^_`", "{|}", "~;", "*", "+", "'" };
    Map<String, String> map = new HashMap<String, String>();
    map.put("isRegionDefault"		   ,  "false");
    map.put("cloneFromtemplate"        ,  "Audiocodes_430HD_LYNC"); 
    map.put("isDownloadSharedTemplates",  "false");

    // Login and enter the Phone Templates menu
	testFuncs.myDebugPrinting("Login and enter the Phone Templates menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);    
  	
    // Step 1-8 - Create a user using POST query with !#$ characters
	int len = suffixes.length;
	for (int i = 0; i < len; ++i) {
		
		testFuncs.myDebugPrinting("Step " + i + ":", testVars.logerVars.NORMAL);
		String tempName = prefixName + suffixes[i];
		String tempDesc = prefixDesc + suffixes[i];
		testFuncs.myDebugPrinting("tempName - " + tempName, testVars.logerVars.MINOR);
		testFuncs.myDebugPrinting("tempDesc - " + tempDesc, testVars.logerVars.MINOR);
		testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
  		map.put("cloneFromtemplate", "Audiocodes_" + type + "_LYNC");
  		testFuncs.addTemplate(driver, tempName, tempDesc, testVars.getDefTenant(), type, map);
  		testFuncs.myWait(3000);
	}
  	
	// Step 9 - Delete the templates
	for (int i = 0; i < len; ++i) {
		
		testFuncs.myDebugPrinting("Step 9." + i + ":", testVars.logerVars.NORMAL);
		String tempName = prefixName + suffixes[i];
		testFuncs.myDebugPrinting("tempName - " + tempName, testVars.logerVars.MINOR);;
  		testFuncs.deleteTemplate(driver,tempName);
  		testFuncs.myWait(3000);
  	}
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
