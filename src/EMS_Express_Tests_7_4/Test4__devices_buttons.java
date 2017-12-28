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
* This test tests the main page Device-Status buttons.
* ----------------
* Tests:
* 	 1. Press the Registered devices button.
* 	 2. Press the Non-Registered devices button.
* 	 3. Press the Disconnected devices button.
* 	 4. press the Quickuly devices button
* 	 5. Press the View-Devices status button.
* 
* Results:
*    1-5. Each pressing should lead you to Devices menu with different search criteria.
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test4__devices_buttons {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test4__devices_buttons(String browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
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
  public void Device_menus_buttons() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  

    // Step 4.1 - press the Registered devices button
	testFuncs.myDebugPrinting("Step 4.1 - press the Registered devices button");
    testFuncs.myClick(driver, By.xpath("//*[@id='card']/div/article/div[1]/div[3]/span/a"), 7000);
    testFuncs.searchStr(driver, "Devices Status");
	testFuncs.pressHomeButton(driver);

    // Step 4.2 - press the Non-Registered devices button
	testFuncs.myDebugPrinting("Step 4.2 - press the Non-Registered devices button");
    testFuncs.myClick(driver, By.xpath("//*[@id='card']/div/article/div[1]/div[3]/span/a"), 10000);
    testFuncs.searchStr(driver, "Devices Status");
	testFuncs.pressHomeButton(driver);
	
    // Step 4.3 - press the Disconnected devices button
	testFuncs.myDebugPrinting("Step 4.3 - press the Disconnected devices button");
    testFuncs.myClick(driver, By.xpath("//*[@id='card']/div/article/div[1]/div[3]/span/a"), 10000);
    testFuncs.searchStr(driver, "Devices Status");
	testFuncs.pressHomeButton(driver);
	
    // Step 4.4 - press the Quickuly devices button
	testFuncs.myDebugPrinting("Step 4.3 - press the Quickuly devices button");
    testFuncs.myClick(driver, By.xpath("//*[@id='card']/div/article/div[1]/div[3]/span/a"), 10000);
    testFuncs.searchStr(driver, "Devices Status");
	testFuncs.pressHomeButton(driver);
	
    // Step 4.5 - press the View-Devices status button
	testFuncs.myDebugPrinting("Step 4.5 - press the View-Devices status button");
	By css = By.cssSelector("a[href='deviceStatus.php']");
	WebElement element = driver.findElement(css);
	((JavascriptExecutor)driver).executeScript("arguments[0].click();" , element);
    testFuncs.searchStr(driver, "Devices Status");
	testFuncs.pressHomeButton(driver);
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
