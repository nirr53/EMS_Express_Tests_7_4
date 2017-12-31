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
* This test tests the System settings menu
* ----------------
* Tests:
* 	 - Enter System settings menu and fill data
* 	 1. Check default placeholder values.
* 
* Results:
* 	 1. Data should be updated successfully in the Default placeholders menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test27__system_settings {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test27__system_settings(String browser) {
	  
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
  public void Default_placeholder_values_menu() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set variables
	String mwiVmNumber = String.valueOf(testFuncs.getNum(9999));
	String ntpServerAddress[] = {"10.1.1.10", "10.1.1.11"};
	String ntpServer		  = ntpServerAddress[(int) Math.round(Math.random())];
	String[] langs 			  = {"English", "Finnish", "French", "German", "Hebrew", "Italian", "Japanese", "Korean"};
	String usedLang           = langs[testFuncs.getNum(langs.length -1)];
	testFuncs.myDebugPrinting("ntpServer - " + ntpServer);
	testFuncs.myDebugPrinting("usedLang - "  + usedLang);
	  
    // Enter System settings menu and fill data
	testFuncs.myDebugPrinting("Enter System settings menu and fill data");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	Select sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	sysLangs.selectByVisibleText(usedLang);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='ntpserver']")  , ntpServer  , 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='MwiVmNumber']"), mwiVmNumber, 2000);
	if (!driver.findElement(By.xpath("//*[@id='SRTP']")).isSelected()) {
	
		driver.findElement(By.xpath("//*[@id='SRTP']")).click();
	}
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 15000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							, 2000);
  
    // Step 1 - Check default placeholder values
	testFuncs.myDebugPrinting("Step 1 - Check default placeholder values");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[4]"), 3000);	
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[1]/td[3]" , testVars.getIp());
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[2]/td[3]" , "IST");
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[3]/td[3]" , "");
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[4]/td[3]" , "0");
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[5]/td[3]" , mwiVmNumber);
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[7]/td[3]" , usedLang);
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[8]/td[3]" , "1");
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[9]/td[3]" , "admin");
	testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[10]/td[3]", "1234");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[3]/button"), 7000);
  }
  
  @After
  public void tearDown() throws Exception {
	   
	// Restore English  
	testFuncs.myDebugPrinting("Restore English");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	Select sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	sysLangs.selectByVisibleText("English");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 15000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							, 2000);

	// close  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
