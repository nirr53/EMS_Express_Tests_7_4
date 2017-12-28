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
* This test tests the URL mechanism of the DHCP- Zero touch
* ----------------
* Tests:
* 	 - Login the EMS environment.
* 	 - Enter the system settings menu & the DHCP Option configuration menu.
* 	 1. Check DHCP option 160 URLs.
* 	 2. Check Tenant URLs.
* 	 3. Select a Tenant without any templates and use the tester.
* 
* Results:
* 	 1. URLs should be changed respectively to the system's IP.
* 	 2. URLs should be changed respectively to the selected tenant.
* 	 2. In all of phone models a configuration should be received as an answer.
* 	 3. In none of the phone models a configuration should be received as an answer.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test68__DHCP_Option_configuration_urls {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test68__DHCP_Option_configuration_urls(String browser) {
	  
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
  public void DHCP_configuration_urls() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// set vars
	String tenWithoutAnyTemp = testVars.getNonDefTenant(1);
	String tenWithAllTemp    = testVars.getDefTenant();
	  
    // Enter System settings and DHCP option configuration menu
	testFuncs.myDebugPrinting("Enter System settings and DHCP option configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_dhcp_options_configuration", "DHCP Options Configuration");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/thead/tr/th"	 , "System URLs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/thead/tr/th"  , "Tenant URLs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[1]", "DHCP option 160 URLs");
    
    // Step 1 - Check DHCP option 160 URLs
  	testFuncs.myDebugPrinting("Step 1 - Check DHCP option 160 URLs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[1]/td[1]/span"    , "EMS accesses phones directly:");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[2]/td[1]/span"    , "EMS accesses phones via SBC HTTP Proxy:");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[1]/td[2]/b/span"  , "http://" + testVars.getIp() + "/firmwarefiles;ipp/dhcpoption160.cfg");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[2]/td[2]/b/span"  , "http://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/httpproxy/");

    // Step 2 - Check Tenant URLs
  	testFuncs.myDebugPrinting("Step 2 - Check Tenant URLs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/span", "The EMS has direct access to the IPP's:");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[2]/td[1]/span", "The EMS access the IPP's throw SBC HTTP Proxy:");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[3]/td[1]/span", "irect URL for the phone (no DHCP available):");
	Select tenants = new Select(driver.findElement(By.id("tenant_id")));
	int size = tenants.getOptions().size();
	for (int i = 0; i < size; ++i) {
		
		tenants.selectByIndex(i);
	    testFuncs.myWait(2000);
	    String tenant = tenants.getFirstSelectedOption().getText();
	  	testFuncs.myDebugPrinting("tenant - " + tenant, testVars.logerVars.MINOR);
	    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/b/span", "http://" + testVars.getIp() + "/firmwarefiles;ipp/tenant/" 	  + tenant);
	    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/b/span", "http://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/tenant/" + tenant);
	    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[3]/td[2]/b/span", "http://" + testVars.getIp() + "/ipp/tenant/" 				  + tenant);
	}

	// Step 3 - Select a Tenant without any templates and use the tester
  	testFuncs.myDebugPrinting("Step 3 - Select a Tenant without any templates and use the tester");
  	tenants.selectByVisibleText(tenWithoutAnyTemp);
    testFuncs.myWait(2000);
	Select models    = new Select(driver.findElement(By.id("model_id")));
	int modelsNumber = models.getOptions().size();
	for (int i = 0; i < modelsNumber; ++i) {
		
		models.selectByIndex(i);
	    testFuncs.myWait(1000);
	    String model = models.getFirstSelectedOption().getText();
	    testFuncs.myWait(1000);
	  	testFuncs.myDebugPrinting("model - " + model, testVars.logerVars.MINOR);
	  	
	  	WebElement el = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[4]/td/a"));
	  	el.click();
	    testFuncs.myWait(7000);
	  	String txt = driver.findElement(By.xpath("//*[@id='modalContentId']")).getText();
	  	testFuncs.myAssertTrue("Meassage was not displayed !!\ntxt - " + txt, (txt.contains("fail to create file for this") || txt.contains("not found url")) && 
	  																			txt.contains(model)	 					  									  &&
	  																			txt.contains(tenWithoutAnyTemp));
	  	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
	}
	
	// Step 4 - Select a Tenant with all templates and use the tester
  	testFuncs.myDebugPrinting("Step 4 - Select a Tenant with all templates and use the tester.");
  	tenants.selectByVisibleText(tenWithAllTemp);
    testFuncs.myWait(2000);
	for (int i = 0; i < modelsNumber; ++i) {
		
		models.selectByIndex(i);
	    testFuncs.myWait(1000);    
	    String model = models.getFirstSelectedOption().getText();
	  	testFuncs.myDebugPrinting("model - " + model, testVars.logerVars.MINOR);	    
	  	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[4]/td/a"), 3000);	
	  	String txt = driver.findElement(By.xpath("//*[@id='modalContentId']")).getText();
	  	String tempType = "";
	  	if (model.contains("HRS")) { tempType = "LYNC";    }
	  	else 					   { tempType = "GENERIC"; }	  	
	  	testFuncs.myAssertTrue("Meassage was not displayed !!\ntxt - " + txt, txt.contains("include Audiocodes_" + model + "_global_" + tempType + "_empty.cfg"));
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
