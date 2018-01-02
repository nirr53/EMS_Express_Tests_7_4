package EMS_Express_Tests_7_4;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
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
* This test tests the DHCP Option configuration menu
* ----------------
* Tests:
* 	 - Login the EMS environment.
* 	 - Enter the system settings menu & the DHCP Option configuration menu.
* 	 1. Edit configuration template
*	 2. Restore configuration template.
*	 3. Upload new configuration template
*	 4. Generate configuration template
* 
* Results:
* 	 1.4 All actions should succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test67__DHCP_Option_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test67__DHCP_Option_configuration(String browser) {
	  
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
  public void DHCP_option_configuration_menu() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set vars
	  String strForEdit = "ems_server/keep_alive_period=" + testFuncs.getId();
	  String uploadStr  = "1234";
	  testFuncs.myDebugPrinting("strForEdit - " + strForEdit, testVars.logerVars.MINOR);
	  
	  // Enter System settings and DHCP option configuration menu
	  testFuncs.myDebugPrinting("Enter System settings and DHCP option configuration menu");
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_dhcp_options_configuration", "DHCP Options Configuration");
  
	  // Step 1 - Edit configuration template
	  testFuncs.myDebugPrinting("Step 1 - Edit configuration template");
	  editDHCPConfigurationTemplate(driver, strForEdit);
	  
	  // Step 2 - Restore configuration template
	  testFuncs.myDebugPrinting("Step 2 - Restore configuration template");
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_dhcp_options_configuration", "DHCP Options Configuration");
	  restoreDHCPConfigurationTemplate(driver, strForEdit);
	  
	  // Step 3 - Upload configuration template
	  testFuncs.myDebugPrinting("Step 3 - Upload configuration template");
	  uploadDHCPConfigurationTemplate(driver, testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("67"), uploadStr);
	  restoreDHCPConfigurationTemplate(driver, uploadStr);
	  
	  // Step 4 - Generate configuration template
	  testFuncs.myDebugPrinting("Step 4 - Generate configuration template");
	  generateDHCPConfigurationTemplate(driver);
  }
  
  // Generate configuration
  private void generateDHCPConfigurationTemplate(WebDriver driver) {
	  
	  // Generate configuration
	  testFuncs.myDebugPrinting("Generate configuration", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/a[1]"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "DHCP Option template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to generate the DHCP option template?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "DHCP Option template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was generated successfully");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
  }

  // Upload DHCP configuration
  private void uploadDHCPConfigurationTemplate(WebDriver driver, String filePath, String uploadStr) throws UnsupportedFlavorException, IOException {
	  
	  // upload file
	  testFuncs.myDebugPrinting("filePath - " + filePath, testVars.logerVars.MINOR);
	  testFuncs.myDebugPrinting("upload file", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[1]/a[3]"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Upload IP Phone Template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='myform']/label"								 , "Press the Browse... button to locate the file and then press the Upload button. When file upload is complete The file has been uploaded successfully message will be shown.");
	  driver.findElement(By.name("uploadedfile")).sendKeys(filePath);
	  testFuncs.myWait(2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Upload File");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "The IP Phone template has been uploaded successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  
	  // Verify upload
	  testFuncs.myDebugPrinting("Verify upload", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[1]/a[1]"), 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea")   			 , 2000);
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  String txt = (String) clipboard.getData(DataFlavor.stringFlavor);
	  testFuncs.myAssertTrue("DHCP configuration file was not uploaded successfully !! \n" + txt, txt.contains(uploadStr));
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was updated successfully.");	 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
  }

  // Restore confiuration
  private void restoreDHCPConfigurationTemplate(WebDriver driver, String strForEdit) throws UnsupportedFlavorException, IOException {
	
	  // Restore DHCP
	  testFuncs.myDebugPrinting("Restore DHCP", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='restoreToDefId']"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Reset the DHCP Options Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to reset the DHCP option template?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Reset the DHCP Options Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was reset successfully");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  
	  // Verify reset
	  testFuncs.myDebugPrinting("Verify reset", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[1]/a[1]"), 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea")   			 , 2000);
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  String txt = (String) clipboard.getData(DataFlavor.stringFlavor);
	  testFuncs.myAssertTrue("DHCP configuration file was not restored successfully !! \n" + txt, !txt.contains(strForEdit));
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was updated successfully.");	 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000); 
  }

  // Edit configuration
  private void editDHCPConfigurationTemplate(WebDriver driver, String strForEdit) throws UnsupportedFlavorException, IOException {
	  
	  // Edit DHCP
	  testFuncs.myDebugPrinting("Edit DHCP", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[1]/a[1]"), 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea")   			 , 2000);
	  testFuncs.mySendKeys(driver, By.xpath("/html/body/div[2]/div/textarea"), strForEdit, 2000); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was updated successfully.");	 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	    
	  // Verify edit
	  testFuncs.myDebugPrinting("Verify edit", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[1]/a[1]"), 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea")   			 , 2000);
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  String txt = (String) clipboard.getData(DataFlavor.stringFlavor);
	  testFuncs.myAssertTrue("DHCP configuration file was not edited successfully !! \n" + txt, txt.contains(strForEdit));
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was updated successfully.");	 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
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
