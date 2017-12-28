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
* This test tests the phone firmware files menu
* ----------------
* Tests:
* 	 - Enter the Phone firmware files menu
* 	 1. Upload a phone firmware.
* 	 2. Edit a phone firmware.
* 	 3. Delete a phone firmware.
* 
* Results:
* 	 1. The firmware should be uploaded successfully.
* 	 2. The firmware should be edited successfully.
* 	 3. The firmware should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test31__phone_firmware_files {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test31__phone_firmware_files(String browser) {
	  
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
  public void Phone_firmware_files() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String Id = testFuncs.getId();
	String firmName     = "myFirmName"  + Id;
	String firmDesc     = "myFirmDesc"  + Id;
	String firmVersion = String.valueOf(testFuncs.getNum(128)) + "." + 
						 String.valueOf(testFuncs.getNum(128)) + "." +
						 String.valueOf(testFuncs.getNum(128)) + "." +
						 String.valueOf(testFuncs.getNum(128));
	String firmRegion   = testVars.getNonDefTenant(0);
	String firmFileName = testVars.getImportFile("31");
	 
    // Enter the Phone firmware files menu
	testFuncs.myDebugPrinting("Enter the Phone firmware files menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");

	// Step 1 - Add new IP Phone firmware
  	testFuncs.myDebugPrinting("Step 1 - Add new IP Phone firmware");
	testFuncs.addNewFirmware(driver, firmName, firmDesc, firmVersion, firmRegion, firmFileName);
	
	// Step 2 - Edit an existing firmware and check Download
  	testFuncs.myDebugPrinting("Step 2 - Edit an existing firmware and check Download");
	String newFirmDesc    = "edited"  + firmDesc;
	String newFirmRegion  = testVars.getNonDefTenant(1);
	String newFirmVersion = String.valueOf(testFuncs.getNum(128)) + "." +
			  	  			String.valueOf(testFuncs.getNum(128)) + "." +
			  	  			String.valueOf(testFuncs.getNum(128)) + "." +
			  	  			String.valueOf(testFuncs.getNum(128));	
  	testFuncs.editFirmware(driver, firmName, firmDesc, newFirmDesc, newFirmVersion, newFirmRegion);  	
		
	// Step 3 - Delete a firmware
  	testFuncs.myDebugPrinting("Step 3 - Delete a firmware");
	testFuncs.deleteFirmware(driver,  firmName, newFirmDesc, newFirmVersion);
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
