package EMS_Tests;

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
* 	 1. Add configuration key
* 	 2. Add another configuration key with the same name.
* 	 3. Enter the Template-Placeholders menu and create a Template-PH
* 	 4. Try to create another Template-PH
* 	 5. Delete the created Template and PH
* 
* Results:
*    1. Configuration key should be added successfully.
*    2. The new key should override the old one.
*    4. The PH should be created successfully.
*    3. The new PH value should not be created. 
* 	 5. The Template and PH should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test128__template_exisiting_configuration_and_ph {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test128__template_exisiting_configuration_and_ph(String browser) {
	  
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
  public void Template_exisitng_configuration_and_ph() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
    Map<String, String> map = new HashMap<String, String>();   
	String Id 			  = testFuncs.getId();
	String tempName  = "myTmpNme_" + Id;
	String tempDesc  = "myTmpDsc_" + Id;
	String confName	 = "confName"  + Id;
	String confValue = "confvalue" + Id;
    map.put("isRegionDefault"		   ,  "false");
    map.put("isDownloadSharedTemplates",  "false");
  	map.put("cloneFromtemplate"		   , "Audiocodes_420HD_LYNC");

    // Login, enter the Phone Templates menu, add a Template and select the create template
	testFuncs.myDebugPrinting("Login, enter the Phone Templates menu, add a Template and select the create template");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");	
  	testFuncs.addTemplate(driver, tempName, tempDesc, testVars.getDefTenant(), "420HD", map);
  	pressEditOfCreatedTemplate(driver, tempName);
  	
	// Step 1 - Add configuration value
	testFuncs.myDebugPrinting("Step 1 - Add configuration value");
	addConfValueValue(confName, confValue);
	
	// Step 2 - Try to add an existing configuration value
	testFuncs.myDebugPrinting("Step 2 - Try to add an existing configuration value");
	String newConfValue = "newConfvalue" + Id;
	addExistConfValueValue(confName, newConfValue, confValue);
	
	// Step 3 - Enter the Template-Placeholders menu and create a Template-PH
	testFuncs.myDebugPrinting("Step 3 - Enter the Template-Placeholders menu and create a Template-PH");
	String tempPhName        = "myPHolderName"  	  + Id;
	String tempPhValue       = "myPHolderValue" 	  + Id;
	String tempPhDescription = "myPHolderDescription" + Id;
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
    testFuncs.addTemplatePlaceholder(driver, tempName, tempPhName, tempPhValue, tempPhDescription);

    // Step 4 - Try to create another Template-PH
 	testFuncs.myDebugPrinting("Step 4 - Try to create another Template-PH");	  
 	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[2]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]/a"), 7000);
 	testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_name']") , tempPhName       , 2000);
 	testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tempPhValue      , 2000);
 	testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_desc']") , tempPhDescription, 2000);
 	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[4]/button[2]"), 7000);
 	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[1]/div[3]/h4", "Failed to save new placeholder. The name is in use.");

	// Step 5 - Delete the created template-PH and Template
  	testFuncs.myDebugPrinting("Step 5 - Delete the created template-PH and Template");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
    testFuncs.deleteTemplatePlaceholder(driver, tempName, tempPhName);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");		
	testFuncs.deleteTemplate(driver,tempName);
  }
  
  // Add an existing configuration value to the Template
  private void addExistConfValueValue(String confName, String newConfValue, String confValue) {
	  
	  // Add new configuration key	  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , confName , 2500);	  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), newConfValue, 2500);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[3]/a"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Add Setting");		  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "This setting name is already in use.\nAre you sure you want to replace " + confValue + " to " + newConfValue); 	   
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);		
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successfully to save the template new configuration settings");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);					  

	  // Verify edit
	  testFuncs.myDebugPrinting("Verify edit", testVars.logerVars.MINOR);
	  testFuncs.searchStr(driver, confName);
	  testFuncs.searchStr(driver, newConfValue); 
  }

  // Add new configuration value to the Template
  private void addConfValueValue(String confName, String confValue) {
	  
	  // Add new configuration key
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , confName , 2500);	  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), confValue, 2500);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[3]/a"), 3000);  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successfully to save the template new configuration settings");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);				

	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
	  testFuncs.searchStr(driver, confName);
	  testFuncs.searchStr(driver, confValue);  
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
