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
* This test tests the alarms page
* ----------------
* Tests:
* 	 - Enter the alarms page.
* 	 1. Check its appearance and menus.
* 	 2. Check the Export button.
* 	 3. Check the link to the Alarms page from the Dashboard button
* 
* Results:
* 	 1. All headers should appear.
* 	 2. Reload mechanism should work successfully.
* 	 3. Link should display the Alerts menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test9__alarms_page_tests {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test9__alarms_page_tests(String browser) {
	  
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
  public void Alarms() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);

    // Step 1 - Enter the Alarms menu
	testFuncs.myDebugPrinting("Step 1 - Enter the Alarms menu");
	testFuncs.enterMenu(driver, "Dashboard_Alarms", "Export");	
	
	// Step 2 - Test the Export button
	if (!this.usedBrowser.equals(testVars.IE)) {
		
		testFuncs.myDebugPrinting("Step 2 - Test the Export button");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport());
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div/div[1]/div/a[1]"), 5000);
		testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are your sure you want to export the alarms to CSV??");
		testFuncs.myClick(driver, By.xpath("//*[@id='update']")				 , 2000);
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport()));	
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport());
	}
	
    // Step 3 - Test the Alarms link from the mainpage
	testFuncs.myDebugPrinting("Step 3 - Test the Alarms link from the mainpage");	
    testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[3]/li[2]/a/i"), 3000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/span[2]/a/button"), 3000);
	testFuncs.searchStr(driver, "Alarms");	
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
