package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
* This test tests the login mechanism
* 1. Try to login without a username
* 2. Try to login with an invalid username
* 3. Try to login without a password
* 4. Try to login without a  valid password
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test1__login {
	
  public static int     random = 0;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test1__login(String browser) {
	  System.out.println("Browser - "  + browser);
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameterized.Parameters
   public static Collection data() {
    //return Arrays.asList(new Object[][]{{"Chrome"},{"Firefox"},{"IE"}});
    return Arrays.asList(new Object[][]{{"Chrome"}});

   }
  
  @BeforeClass public static void setting_SystemProperties(){
      System.out.println("System Properties seting Key value.");
  }  
  
  @Before
  public void setUp() throws Exception {
	  	
	testVars  = new GlobalVars();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getfirefoxDrvPath());
	System.out.println("Enter setUp(); random - " + random);
	if 		  (random == GlobalVars.CHROME) {
    	driver    = new ChromeDriver();
        
    } else if (random == GlobalVars.FIREFOX) {
    	driver 	  = new FirefoxDriver();
    	
    } else if (random == GlobalVars.IE) {
    	driver 	  = new InternetExplorerDriver();

    }  else {
    	fail ("random is invalid - " + random);
    	
    }
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    testFuncs = new GlobalFuncs(); 
  }

  @Test
  public void testLoginStandAlone1() throws Exception {
	
    // Step 1.1 - login
	System.out.println("Step 1.1 - login");
	testFuncs.login(driver, testVars.getSysMainStr(), "http://");
	
    // Step 1.2 - login via HTTPS
	System.out.println("Step 1.2 - login via HTTPS");
	testFuncs.login(driver, testVars.getSysMainStr(), "https://");
	
    // Step 1.3 - timeout
	//System.out.println("Step 1.3 - timeout");
	//testFuncs.login(driver, testVars.getSysMainStr(), "http://", false);
	testFuncs.myWait(120000);

	
    random += 1;
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
}
