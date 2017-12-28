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
* This test tests the delete devices mechanism
* ----------------
*  Tests:
* 	 1. Add a user manually & add multiple devices to it.
* 	 2. Delete one device.
* 	 3. Delete the user - > all the devices should be deleted also.
* 
* Results:
* 	 1. User and devices should be added successfully.
* 	 2. Device should be deleted successfully.
* 	 3. The user and all its devices should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test8__delete_devices_tests {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test8__delete_devices_tests(String browser) {
	  
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
  public void Device_delete() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String Id         = testFuncs.getId();
	String userName   = "manual user" + Id;
	String deviceName = "device" + Id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);

    // Create user manually
	testFuncs.myDebugPrinting("Create user manually");
	testFuncs.addUser(driver, userName, "1q2w3e$r", "displayName" + Id, testVars.getDefTenant());
	testFuncs.myWait(2000);
	
	// Add multiple devices to the created user
	testFuncs.myDebugPrinting("Add multiple devices to the created user");
    String [] phoneTypes = {"420HD", "430HD", "440HD", "450HD"};
	for(String type : phoneTypes) {
		
		testFuncs.addDevice(driver, userName, deviceName  , testVars.getDefTemplates(0), testFuncs.getMacAddress(), type);
		testFuncs.myWait(2000);
	}
	
	// Step 1 - delete one of the user devices
	testFuncs.myDebugPrinting("Step 1 - delete one of the user devices");
	testFuncs.searchUser(driver, userName); 
	String deletedDevice = driver.findElement(By.xpath("//*[@id='tr" + userName + "device']/td[2]/table/tbody/tr[1]/td/div/table/tbody/tr[4]/td[2]")).getText();
	testFuncs.myDebugPrinting("deletedDevice  " + deletedDevice, testVars.logerVars.MINOR);
	testFuncs.myClick(driver, By.xpath("//*[@id='"+ userName + "tree']")															  , 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='tr" + userName + "device']/td[2]/table/tbody/tr[1]/td/div/table/tbody/tr[5]/td/a[2]"), 2000);
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete device?");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
	testFuncs.myAssertTrue("Device still exist!", !driver.findElement(By.tagName("body")).getText().contains("Firmware:	" + deletedDevice));

	// Step 2 - delete user with multiple devices
    testFuncs.myDebugPrinting("Step 2 - delete one of the user devices");
	testFuncs.searchUser(driver, userName); 
	testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[9]/a[3]"), 2000);
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the User " + userName);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
    driver.findElement(By.xpath("//*[@id='searchtext']")).clear();
    driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(userName);
    driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
    testFuncs.myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 7000); 
	testFuncs.searchStr(driver, "No users found");
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
