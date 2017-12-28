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
* This test tests the invalid-login mechanism
* ----------------
* Tests:
*    1. Try to login with a  valid   username and an invalid password.
*    2. Try to login with an invalid username and a  valid password.
*    3. Try to login with an invalid username and an invalid password.
*    
* Results:
*    1-3. All the invalid login attempts should testFuncs.myFail.
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test2__invalid_login {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test2__invalid_login(String browser) {
	  
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
  public void Invalid_login() throws Exception {
	
	Log.startTestCase(this.getClass().getName());
	  
    // Step 2.1 - invalid login - valid username + invalid password
	testFuncs.myDebugPrinting("Step 2.1 - invalid login - valid username + invalid password");
	invalidLogin(driver, testVars.getSysMainStr(), 0, this.usedBrowser, "https://");
	
    // Step 2.2 - invalid login - invalid username + valid password
	testFuncs.myDebugPrinting("Step 2.2 - invalid login - invalid username + valid password");
	invalidLogin(driver, testVars.getSysMainStr(), 1, this.usedBrowser, "https://");
	
    // Step 2.3 - invalid login - invalid username + invalid password
	testFuncs.myDebugPrinting("Step 2.3 - invalid login - invalid username + invalid password");
	invalidLogin(driver, testVars.getSysMainStr(), 2, this.usedBrowser, "https://");
  }
  
  /**
  *  Invalid login method
  *  @param driver  - Given driver for make all tasks
  *  @param mainStr - Given string for verify bad access
  *  @param i       - Integer to mark the tested test
  *  @param brwType	- Browser type (Chrome, IE or FF)
  *  @param httpStr - Browser type (http or https)
  */
  private void invalidLogin(WebDriver driver, String mainStr, int i, String brwType, String httpStr) {
        
      String title = driver.getTitle();
	  testFuncs.myDebugPrinting("1. title - "   	    + title  			 ,testVars.logerVars.MINOR);
      driver.get("https://" + testVars.getUrl());
      
      // Speicel login for IE browser
      if (brwType.equals("IE") && title.equals("WebDriver") && httpStr.equals("https://")) {
    	  
    	  driver.findElement(By.xpath("//a[@id='overridelink']")).click();
    	  testFuncs.myWait(5000);
    	  
      }
     
	  testFuncs.searchStr(driver, testVars.getMainPageStr());  
      driver.findElement(By.xpath("//*[@id='loginform']/div[1]/input")).clear();
      driver.findElement(By.xpath("//*[@id='loginform']/div[2]/input")).clear();	      
      if        (i == 0) {
    	  
	      driver.findElement(By.xpath("//*[@id='loginform']/div[1]/input")).sendKeys(testVars.getSysUsername().substring(1));
	      driver.findElement(By.xpath("//*[@id='loginform']/div[2]/input")).sendKeys(testVars.getSysPassword());     
      } else if (i == 1) {
    	  
	      driver.findElement(By.xpath("//*[@id='loginform']/div[1]/input")).sendKeys(testVars.getSysUsername());
	      driver.findElement(By.xpath("//*[@id='loginform']/div[2]/input")).sendKeys(testVars.getSysPassword().substring(1));   
      } else if (i == 2) {
      
	      driver.findElement(By.xpath("//*[@id='loginform']/div[1]/input")).sendKeys(testVars.getSysUsername().substring(1));
	      driver.findElement(By.xpath("//*[@id='loginform']/div[2]/input")).sendKeys(testVars.getSysPassword().substring(1));
      }
	  testFuncs.myClick(driver, By.xpath("//*[@id='loginform']/div[4]/div[2]/button"), 3000);   
	  
	  if (i == 0 || i == 2) {
		  
		  testFuncs.searchStr(driver, "Login failed. Check the username and password");		  
	  } else {
		  
		  testFuncs.searchStr(driver, "Login failed. Check the username and password");	  
	  }  
	  testFuncs.myAssertTrue("Login succedded ..", !driver.findElement(By.tagName("body")).getText().contains("NETWORK"));
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