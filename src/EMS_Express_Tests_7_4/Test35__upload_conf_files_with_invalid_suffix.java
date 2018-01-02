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
* This test verify that we cannot upload files that has no cfg suffix in the Templates,
* Template-placeholders and Phone configuration menus.
* ----------------
* Tests:
* 	 - Enter the Phone configuration files menu
* 	 1. Try to Upload non-cfg file to the Template
* 	 2. Try to Upload non-cfg file to the Phone configuration
* 
* Results:
*    1-2. In all cases upload should not be possible and appropriate error prompt should be displayed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test35__upload_conf_files_with_invalid_suffix {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test35__upload_conf_files_with_invalid_suffix(String browser) {
	  
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
  public void Upload_non_cfg_template() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String nonCfgFile = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("12.1");
	  
    // Login and enter the Phone Templates menu
	testFuncs.myDebugPrinting("Login and enter the Phone Templates menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
	
	// Step 1 - Try to Upload non-cfg file to the Template
  	testFuncs.myDebugPrinting("Step 1 - Try to Upload non-cfg file to the Template");
  	uploadNonCfgToTemplates(driver, nonCfgFile);
  	
  }
  
  @Test
  public void Upload_non_cfg_phone_configuration() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String nonCfgFile = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("12.1");
	
	// Login and enter the Phone configuration menu
	testFuncs.myDebugPrinting("Login and enter the Phone configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Manage Configuration Files");
	  
	// Step 2 - Try to Upload non-cfg file to the Phone configuration
  	testFuncs.myDebugPrinting("Step 2 - Try to Upload non-cfg file to the Phone configuration");
  	uploadNonCfgToPhoneConfiguration(driver, nonCfgFile);  
  }
  
  /**
  *  Upload non-cfg file to Phone configuration menu
  *  @param driver         - A given driver
  *  @param nonCfgFileName -  An invalid file path
  */
  private void uploadNonCfgToPhoneConfiguration(WebDriver driver, String nonCfgFileName) {
	  
	  // Try to upload non-cfg file to Configuration-files menu
	  testFuncs.myDebugPrinting("Try to upload non-cfg file to Configuration-files menu", testVars.logerVars.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='myfile']"), nonCfgFileName, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='form_upload']/div/input[4]") , 5000);
	    
	  // Verify that an error is recived
	  testFuncs.myDebugPrinting("Verify that an error is recived", testVars.logerVars.MINOR);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Invalid file extension");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please choose a cfg file");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.myDebugPrinting("Search for nonCfgFileName - " + nonCfgFileName, testVars.logerVars.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchInput']"), nonCfgFileName, 4000);
	  String txt =  driver.findElement(By.tagName("body")).getText();;
	  testFuncs.myAssertTrue("Configuration file was uploaded successfully !!\ntxt - " + txt, !txt.contains(nonCfgFileName));
  }
  
  /**
  *  Upload non-cfg file to created Template
  *  @param driver         - A given driver
  *  @param nonCfgFileName -  An invalid file path
  */
  private void uploadNonCfgToTemplates(WebDriver driver, String nonCfgFileName) {
	  
	  // Try to edit one of the Templates by uploading a file
	  testFuncs.myDebugPrinting("Try to edit one of the Templates by uploading a file", testVars.logerVars.MINOR);
	  testFuncs.myDebugPrinting("nonCfgFileName - " + nonCfgFileName, testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[3]/td[8]/div/buttton[1]")							 , 5000);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/div/div[2]/div[1]/a[2]"), 5000);
//	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Upload IP Phone Template "); 
	  WebElement fileInput = driver.findElement(By.name("uploadedfile"));
	  fileInput.sendKeys(nonCfgFileName);    
	  testFuncs.myWait(5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Upload File");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please select valid template file to upload (*.cfg)");	  
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
