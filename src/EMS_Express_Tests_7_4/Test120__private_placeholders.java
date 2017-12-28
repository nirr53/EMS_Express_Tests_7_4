package EMS_Express_Tests_7_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
import org.openqa.selenium.support.ui.Select;

/**
* ----------------
* This test tests the private placeholders feature
* ----------------
* Tests:
* 	- Create a phone Template
* 	 1. Edit the created Template and add a private placeholder to it.
* 	 2. Edit the created Template and add a regular placeholder to it.
* 	 3. Delete all the created data
* 
* Results:
* 	  1. The placeholder should be added automatically to the Template-PHs menu with empty value.
* 	  2. The placeholder should not be added automatically to the Template-PHs menu.
* 	  3. All data should be deleted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test120__private_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test120__private_placeholders(String browser) {
	  
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
  public void Private_placeholders() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());		
	
	// Set vars
	String Id 		 = testFuncs.getId();
	String tempName	 = "my420HDTemplate_" + Id;
	String tempDesc  = "my420HDdesc"      + Id;
    Map<String, String> map = new HashMap<String, String>();
    map.put("isRegionDefault"		   ,  "false");
    map.put("isDownloadSharedTemplates",  "false");
  	map.put("cloneFromtemplate"		   , "Audiocodes_420HD_LYNC");
//    map.put("cloneFromtemplate"        ,  ""); 

    // Login and enter the Phone Templates menu
	testFuncs.myDebugPrinting("Login and enter the Phone Templates menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
//	map.put("cloneFromtemplate", "Audiocodes_" + "420HD");
	testFuncs.addTemplate(driver, tempName, tempDesc, testVars.getNonDefTenant(0), "420HD", map);
  	
	// Step 1 - Add private placeholder and normal place holder
	testFuncs.myDebugPrinting("Step 1 - Add private placeholder and normal place holder");
	enterTemplate(driver, tempName);
	addCfg(driver, true , Id);
	addCfg(driver, false, Id);			
	
	// Step 2 - Enter the Template-place holders menu and verify that only private PH is added
	testFuncs.myDebugPrinting("Step 2 - Enter the Template-place holders menu and verify that only private PH is added");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders"); 	
	new Select(driver.findElement(By.xpath("//*[@id='models']"))).selectByVisibleText(tempName);
	String bodyText = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Private PH was not detected !! \nbodyText - " + bodyText, bodyText.contains("%ITCS_P_" + Id + "% Edit Delete"));
	testFuncs.myAssertTrue("Non Private PH was detected !! \nbodyText - " + bodyText, !bodyText.contains("%ITCS_" + Id + "%"));

	// Step 3 - Delete the created Template
	testFuncs.myDebugPrinting("Step 3 - Delete the created Template");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");	
	testFuncs.deleteTemplate(driver, tempName);
  }

  // Add private configuration key
  private void addCfg(WebDriver driver2, boolean isPrivate, String id) {
	  
	  String tempStr = "%ITCS_P_" + id + "%";
	  if (!isPrivate) {
		  
		  tempStr = "%ITCS_" + id + "%";
	  }
	  testFuncs.myDebugPrinting("tempStr - " + tempStr, testVars.logerVars.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , id		, 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), tempStr, 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[3]/a/span"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successfully to save the template new configuration settings");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	  
  }

// Enter the created Template
  private void enterTemplate(WebDriver driver, String tempName) throws IOException {
	  
	  // Get idx
	  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
	  String l = null;
	  int i = 1;
	  while ((l = r.readLine()) != null) {
		  
		  testFuncs.myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
		  if (l.contains(tempName)) {
			  
			  testFuncs.myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
			  break;
		  }
		  if (l.contains("Edit" )) {
			  
			  ++i;
		  }
	  } 
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[8]/div/buttton[1]"), 3000);		  	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"        , "IP Phone " + tempName + " Configuration Template");
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
