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
* This test tests the create of several phone templates
* ----------------
* Tests:
* 	 - Login and enter the Phone Templates menu
* 	 1. Create a 405, 445 and 450HD templates.
* 	 2. Edit each of templates.
* 	 3. Delete the templates.
* 
* Results:
* 	 1. Templates from all types should be created successfully.
* 	 2. Templates from all types should be edited successfully.
* 	 3. Templates from all types should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test25__templates_create_450_445_405_templates {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test25__templates_create_450_445_405_templates(String browser) {
	  
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
  public void Templates_405_445_450_create_edit_delete() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	int i               = 1;
	String Id 			= testFuncs.getId();
	String phoneTypes[] = {"405", "445HD", "450HD"};	
    Map<String, String> map = new HashMap<String, String>();
    map.put("isRegionDefault"		   ,  "false");
    map.put("cloneFromtemplate"        ,  ""); 
    map.put("isDownloadSharedTemplates",  "false");

    // Login and enter the Phone Templates menu
	testFuncs.myDebugPrinting("Login and enter the Phone Templates menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
	  
	// Step 1 - Add the templates
  	for (String type : phoneTypes) {
  		
  		testFuncs.myDebugPrinting("Step 1." + i + " - Add a " + type + " template");
  		map.put("cloneFromtemplate", "Audiocodes_" + type + "_LYNC");
  		testFuncs.addTemplate(driver, "my" + type + "Template_" + Id, "my" + type + "desc", testVars.getNonDefTenant(0), type, map);
  		testFuncs.myWait(10000);
  		++i;
  	}
  	
	// Step 2 - Edit the templates
  	i  = 1;
  	map.put("tenant", testVars.getNonDefTenant(1));
  	for (String type : phoneTypes) {
  		
  		testFuncs.myDebugPrinting("Step 2." + i + " - Edit a " + type + " template");
  	  	map.put("model", type);
  		testFuncs.editTemplate(driver,  "my" + type + "Template_" + Id, map);
  		testFuncs.myWait(10000);
  		++i;
  	}

	// Step 3 - Delete the templates
  	i  = 1;
  	for (String type : phoneTypes) {
  		
  		testFuncs.myDebugPrinting("Step 3." + i + " - Delete " + type + " template");
  		testFuncs.deleteTemplate(driver, "my" + type + "Template_" + Id);
  		testFuncs.myWait(10000);
  		++i;
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
