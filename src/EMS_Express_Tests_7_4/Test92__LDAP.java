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
* This test tests the LDAP menu
* ----------------
* Tests:
* 	 -  Login via Administrator, an enter the LDAP menu
*    1. Verify that menu headers
*    2. Test the Test-LDAP button
* 
* Results:
*	 1. Menu should be displayed properly.
*    2. LDAP button should respond
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test92__LDAP {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test92__LDAP(String browser) {
	  
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
  public void LDAP_menu() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());	
	
	// Set vars
	String ldapAddress  = String.valueOf(testFuncs.getNum(128)) + "." +
			 			  String.valueOf(testFuncs.getNum(128)) + "." +
			 			  String.valueOf(testFuncs.getNum(128)) + "." +
			 			  String.valueOf(testFuncs.getNum(128));
	String Id			= testFuncs.getId();
	String ldapUsername = "username" + Id;
	String ldapPassword = "1q2w3e$R";
	 
	// Login via Administrator and enter the LDAP menu
	testFuncs.myDebugPrinting("Login via Administrator and enter the LDAP menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings_ldap", "LDAP Configuration");

	// Step 1 - verify headers
	testFuncs.myDebugPrinting("Step 1 - verify headers");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"						   , "LDAP Configuration");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/table[1]/thead/tr/th"  , "General");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/table[2]/thead/tr/th/a", "Phone");	
	
	// Step 2 - test the Test-LDAP button
	testFuncs.myDebugPrinting("Step 2 - test the Test-LDAP button");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_SERVER_ADDRESS']"), ldapAddress , 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_USER_NAME']")     , ldapUsername, 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_PASSWORD']")      , ldapPassword, 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_BASE']")		   , Id		 	 , 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/table[2]/tbody/tr[2]/td[3]/a"), 3000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Please enter name for the LDAP test");
	testFuncs.mySendKeys(driver, By.xpath("/html/body/div[2]/div/input[1]"), "Test" + Id, 2000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")				, 3000);

	// Nir 29\6\17 bug of 500-error (VI 145598)
   	Alert alert = driver.switchTo().alert();
    alert.accept(); 
    testFuncs.myWait(3000);
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
