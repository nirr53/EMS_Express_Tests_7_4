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
* This test tests the phone configuration files menu
* ----------------
* Tests:
* 	 - Enter the Phone configuration files menu.
* 	 1. Upload a phone configuration.
* 	 2. Download a phone configuration.
* 	 3. Delete a phone configuration.
* 
* Results:
* 	 1. Phone configuration should be uploaded successfully.
*	 2. Phone configuration should be downloaded successfully.
* 	 3. Phone configuration should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test30__phone_configuration_files {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test30__phone_configuration_files(String browser) {
	  
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
  public void Phone_configuration_files_menu() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
    // Enter the Phone configuration files menu
	testFuncs.myDebugPrinting("Enter the Phone configuration files menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Manage Configuration Files");
	
	// Step 1 - Upload a phone configuration file
  	testFuncs.myDebugPrinting("Step 1 - Upload a phone configuration file");
	uploadConfigurationFile(driver,  testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("30"), testVars.getImportFile("30"));
	
//	// Step 2 - Download a phone configuration file
// 	testFuncs.myDebugPrinting("Step 2 - Download a phone configuration file");
//	// TODO find a solution for download problem
// 	testFuncs.downloadConfigurationFile(driver,  testVars.getImportFile("30"));
		
	//	 Step 3 - Delete a phone configuration file
  	testFuncs.myDebugPrinting("Step 3 - Delete a phone configuration file");
	deleteConfigurationFile(driver,  testVars.getImportFile("30"));
  }
  
  /**
  *  Delete a configuration file
  *  @param driver   - A given driver
  *  @param fileName - A name of the deleted file
  */
  private void deleteConfigurationFile(WebDriver driver, String fileName) {
	  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchInput']"), fileName, 7000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='selall']"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='filelist']/div[2]/a"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Files");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure want to delete the selected files?\n" + fileName);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);

	  // Verify delete
	  testFuncs.myDebugPrinting("verify delete", testVars.logerVars.MINOR);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody[1]/tr/td[1]", fileName);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody[1]/tr/td[2]", "Deleted");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
  }
  
  /**
  *  Upload a configuration file
  *  @param driver   - A given driver
  *  @param filePath - A given path to configuration file we want to upload
  *  @param fileName - A name of the uploaded file
  */
  private void uploadConfigurationFile(WebDriver driver, String filePath, String fileName) {
	  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='myfile']"), filePath, 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='form_upload']/div/input[4]"), 10000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[1]/div[1]/h4", "\"" + fileName + "\" File Successfully Uploaded.");
		
	  // Verify upload
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchInput']"), fileName, 7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='fbody']/tr[1]/td[3]/a[2]", fileName);	  
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
