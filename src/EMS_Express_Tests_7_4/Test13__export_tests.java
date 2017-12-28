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
* This test tests an export mechanism
* ----------------
* Tests:
* 	 1. Export users+devices file.
* 	 2. Export a configuration file.
* 
* Results:
*	 1-2. All export tests should be ended successfully.
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test13__export_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test13__export_tests(String browser) {
	  
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
  public void Export_users_devices() throws Exception {
	 
	if (!this.usedBrowser.equals(testVars.IE)) {
				
		Log.startTestCase(this.getClass().getName());
		testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
		
	    // Step 1 - Enter the Export-Users-Devices menu and export users+devices
		String downloadedFile = "users.zip";
		testFuncs.myDebugPrinting("Step 1 - Enter the Export-Users-Devices menu and export users+devices");
		testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_export", "Export Users and Devices information");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/a"), 60000);
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile));
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
	
	    // Step 2 - Enter the Export-Configuration menu and export configuration
		String downloadedFile2 = "Configuration.zip";
		testFuncs.myDebugPrinting("Step 2 - Enter the Export-Configuration menu and export configuration");
		testFuncs.enterMenu(driver, "Setup_Import_export_configuration_export", "To export phone configuration files");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile2);
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/button"), 60000);		
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile2));
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile2);
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
