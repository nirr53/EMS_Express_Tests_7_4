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
* This test tests the login mechanism
* ----------------
* Tests:
* 	 1. Test the 'Log off' button.
* 	 2. Test the displayed version number
*    3. Test the 'Help' button
*    4. Test the 'Home' button
*    5. Test the Audiocodes Twitter link button
* 
* Results:
*    1. 'Log off' should disconnect you from the system.
*    2. The current version number should be displayed.
*    3. Pop-up with Help message should appear.
*    4. Pressing the Home button should return you to the main menu.
*    5. Pressing the AC Twitter link button should open you a menu of Audicodes Twitter page.
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test3__home_screen_buttons {
	
  private WebDriver 	driver;
  private String        usedBrowser = "";
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test3__home_screen_buttons(String browser) {
	  
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
  public void Home_screen_buttons() throws Exception {
	
	Log.startTestCase(this.getClass().getName());
	  
    // Step 3.1 - press the Log off button
	testFuncs.myDebugPrinting("Step 3.1 - press the Log off button");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());

    // Step 3.2 - Check version number
	testFuncs.myDebugPrinting("Step 3.2 - Check version number");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	String txt = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Version <" + testVars.getVersion() + "> was not detected !! (" + txt + ")", txt.contains(testVars.getVersion()));

	// Step 3.3 - Help Button
	testFuncs.myDebugPrinting("Step 3.3 - Help button");
	pressHelpButton(driver, "Help is not supported yet!");
	
	// Step 3.4 - Home button
	testFuncs.myDebugPrinting("Step 3.4 - Home button");
	testFuncs.enterMenu(driver, "Dashboard_Alarms", "Export");
	testFuncs.pressHomeButton(driver);
	
	// Step 3.5 - Press the Twitter link
	testFuncs.myDebugPrinting("Step 3.5 - Press the Twitter link");
	pressTwitterIcon(driver);
  }

  // Press the Twitter link
  private void pressTwitterIcon(WebDriver driver) {
	
	  driver.switchTo().frame(0);	  
	  testFuncs.myClick(driver, By.xpath("/html/body/div/div[1]/h1/span/a"), 4000); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div/div[1]/h1/span")  , 2000);    
	  for(String winHandle : driver.getWindowHandles()) {
	    	
	        driver.switchTo().window(winHandle);  
	  }
	  
	  // Verify login to the Audiocodes Twitter page
	  testFuncs.searchStr(driver, "AudioCodes Ltd. is a leading vendor of advanced voice networking & media solutions for the digital workplace offering a range innovative products & solutions");
	  testFuncs.searchStr(driver, "Tweets");
	  testFuncs.searchStr(driver, "Tweets & replies");
	  testFuncs.searchStr(driver, "Media");
  }

  // Press the Help button
  private void pressHelpButton(WebDriver driver, String string) {
	  
	  // Test the Help button at right side of page
	  testFuncs.myDebugPrinting("Step 3.3.1 - Test the Help button at right side of page", testVars.logerVars.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[3]/li[4]/a"), 2000);
	  Alert alert = driver.switchTo().alert();	
	  testFuncs.myAssertTrue(string + " was not detected at pop-up!!", alert.getText().contains(string)); 
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
