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

/**
* ----------------
* This test tests the Templates configuration features
* ----------------
* Tests:
* 	 - Login, enter the Phone Templates menu, add a Template and select the create template
* 	 1. Add Daylight value
* 	 2. Add Pin-value (enabled + disabled)
* 	 3. Add Telnet-Access (enabled + disabled)
*    4. Delete the created template	
* 
* Results:
* 	 1-3. Values should be added successfully. 
* 	   4. template should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test96__template_features {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test96__template_features(String browser) {
	  
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
  public void Templates_actions_features() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
    Map<String, String> map = new HashMap<String, String>();   
	String Id 			  = testFuncs.getId();
	String site   		  = testVars.getDefSite();
	String tenant 		  = testVars.getDefTenant();
	String telnetPrefix   = "management/telnet/enabled";
	String pinPrefix 	  = "system/pin_lock/enabled";
    map.put("isRegionDefault"		   ,  "false");
    map.put("isDownloadSharedTemplates",  "false");
  	map.put("cloneFromtemplate"		   , "Audiocodes_420HD_LYNC");

    // Login, enter the Phone Templates menu, add a Template and select the create template
	testFuncs.myDebugPrinting("Login, enter the Phone Templates menu, add a Template and select the create template");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");	
  	testFuncs.addTemplate(driver, "my" + "420HD" + "Template_" + Id, "my" + "420HD" + "desc", testVars.getNonDefTenant(0), "420HD", map);
  	pressEditOfCreatedTemplate(driver, "my" + "420HD" + "Template_" + Id);
  	
	// Step 1 - Add Daylight value
	testFuncs.myDebugPrinting("Step 1 - Add Daylight value");
	addDayLightValue(driver, site, tenant);

	// Step 2 - Add Telnet-Access (enabled + disabled)
	testFuncs.myDebugPrinting("Step 2 - Add Telnet-Access (enabled + disabled)");
	addTelnetAccessValue(driver, telnetPrefix, false);
	addTelnetAccessValue(driver, telnetPrefix, true);
	deleteAllConfValues(driver , telnetPrefix);
	
	// Step 3 - Add Pin-value (enabled + disabled)
	testFuncs.myDebugPrinting("Step 3 - Add Pin-value (enabled + disabled)");
	addPinLockValue(driver, pinPrefix, false);
	addPinLockValue(driver, pinPrefix, true);
	deleteAllConfValues(driver , pinPrefix);

	// Step 4 - Delete the created template  		
  	testFuncs.myDebugPrinting("Step 4 -  Delete the created template");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");	
	testFuncs.deleteTemplate(driver, "my" + "420HD" + "Template_" + Id);
  }
  
  // Add PIN-lock value
  private void addPinLockValue(WebDriver driver, String prefix, Boolean isLock) {
	  
	  // Add PIN lock value
	  testFuncs.myDebugPrinting("Add PIN lock value", testVars.logerVars.NORMAL);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='pinlock']")															   , 3000);	
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Pin Lock");
	  if (isLock) {
		  
		  testFuncs.myDebugPrinting("isLock - TRUE", testVars.logerVars.MINOR);
		  testFuncs.myClick(driver, By.xpath("//*[@id='system_pin_lock_enabled']"), 3000);
	  }  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);

	  // Nir - bug 7.4.245 7.4.17 (VI 145708)
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Template configuration saved successfully");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	  

	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
	  testFuncs.searchStr(driver, prefix);
	  String valueForsearch = "0";
	  if (isLock) { valueForsearch = "1"; }
	  testFuncs.searchStr(driver, valueForsearch);
  }
  
  // Press the Edit button of the created Template
  private void pressEditOfCreatedTemplate(WebDriver driver, String tempName) throws IOException {
	
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
  
  // Add Daylight values
  private void addDayLightValue(WebDriver driver, String site, String tenant) {
	  
	  // Add daylight values
	  testFuncs.myDebugPrinting("Add daylight values", testVars.logerVars.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[1]/button"), 9000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='daylight']")															 							, 9000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Daylight Savings Time");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
	  // Nir - bug 7.4.245 7.4.17 (VI 145708)
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Template configuration saved successfully");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	  
	  
	  // Verify that all the values were added
	  testFuncs.myDebugPrinting("Verify that all the values were added", testVars.logerVars.NORMAL);
	  String dayLightValues[] = {"system/daylight_saving/activate"			  , "system/daylight_saving/end_date/day" 		   ,
			  					 "system/daylight_saving/end_date/day_of_week", "system/daylight_saving/end_date/hour"		   ,
			  					 "system/daylight_saving/end_date/month"	  , "system/daylight_saving/end_date/week"		   ,
			  					 "system/daylight_saving/mode"				  , "system/daylight_saving/offset"       		   ,
			  					 "system/daylight_saving/start_date/day"      , "system/daylight_saving/start_date/day_of_week",
			  					 "system/daylight_saving/start_date/hour"	  , "system/daylight_saving/start_date/month"	   ,
			  					 "system/daylight_saving/start_date/week"};
	  
	  
	  
	  int dayLightValuesNumber = dayLightValues.length;
	  for (int i = 0; i < dayLightValuesNumber; ++i) {
		  
		  testFuncs.myDebugPrinting(i + ". The searched value is: " + dayLightValues[i], testVars.logerVars.MINOR);
		  testFuncs.searchStr(driver, dayLightValues[i]);	  
		  testFuncs.myWait(3000); 
	  }  
  }
  
  // Add Telnet-Access value
  private void addTelnetAccessValue(WebDriver driver, String prefix, boolean isTelnetAccess) {
	  
	  // Add Telnet-Access value
	  testFuncs.myDebugPrinting("Add Telnet-Access value", testVars.logerVars.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='telnet']")															   , 3000);	
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Activate Telnet access");
	  if (isTelnetAccess) {
		  
		  testFuncs.myDebugPrinting("isTelnetAccess - TRUE", testVars.logerVars.MINOR);
		  testFuncs.myClick(driver, By.xpath("//*[@id='management_telnet_enabled']"), 3000);
	  }
	  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Template configuration saved successfully");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	
  
	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
	  testFuncs.searchStr(driver, prefix);
	  String valueForsearch = "0";
	  if (isTelnetAccess) { valueForsearch = "1"; }
	  testFuncs.searchStr(driver, prefix + " " + valueForsearch);  	  
  }
  
  // Delete all configuration values
  private void deleteAllConfValues(WebDriver driver, String prefix) {

	  // Delete all Configuration values
	  testFuncs.myDebugPrinting("Delete all Configuration values", testVars.logerVars.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[2]/button")    , 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[2]/ul/li[1]/a"), 3000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration settings");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	  
//	   Nir - bug 7.4.245 7.4.17 (VI 145708)
	   testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + testVars.getDefSite() + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant() + " )");
	   testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");
	   testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	

	  // verify delete
	  testFuncs.myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
	  String txt = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("All tenant configuration values were not delete successfully !!\ntxt - " + txt, !txt.contains(prefix));
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
