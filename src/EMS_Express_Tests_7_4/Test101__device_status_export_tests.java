package EMS_Express_Tests_7_4;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

/**
* ----------------
* This test tests the export-devices of Device-Status menu
* ----------------
* Tests:
* 	 - Create a registered users using a POST query and enter the Device-Status menu.
* 	 1. Check no-devices  , not check 'Export-all' checbox and export the devices
* 	 2. Check no-devices  ,  do check 'Export-all' checbox and export the devices
* 	 3. Check some devices, not check 'Export-all' checbox and export the devices
*    4. Check some devices,  do check 'Export-all' checbox and export the devices
* 
* Results:
* 	 1. All devices should be expoerted to CSV file.
* 	 2. All devices should be expoerted to CSV file.
*    3. Only filtered devices should be exported.
*    4. All devices should be expoerted to CSV file.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test101__device_status_export_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test101__device_status_export_tests(String browser) {
	  
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
  public void Device_status_export() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String downloadedFile 	= "ExportDevicesStatus.csv";
	String userName     	= "deviceActions" + testFuncs.getId();
	int usersNUmber 		= 3;
	String usersNumberStr   = String.valueOf(usersNUmber);
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumberStr); 
    map.put("startIdx"   ,  String.valueOf(1));
	
	// Login via Administrator and create a user using POST query
	testFuncs.myDebugPrinting("Login via Administrator and create a user using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp(),
				 				testVars.getPort()    						 ,
				 				usersNumberStr				   			     ,
				 				userName		   						     ,
				 				testVars.getDomain()  						 ,
				 				"registered"          						 ,
				 				testVars.getDefPhoneModel()              	 ,
				 				testVars.getDefTenant()               		 ,
				 				"myLocation");
	testFuncs.verifyPostUserCreate(driver, userName, userName, true);   
	
	// Step 1 - Check no-devices, don't check 'Export-all' checkbox, export the devices and verify that all displayed devices are exported
	testFuncs.myDebugPrinting("Step 1 - Check no-devices, don't check 'Export-all' checkbox, export the devices and verify that all displayed devices are exported");
	setSearch("");
	exportDevices(downloadedFile, false);
	String exportFileTxt = testFuncs.readFile(testVars.getDownloadsPath() + "\\" + downloadedFile); 
	testFuncs.myWait(5000);
	for (int i = 1; i <= usersNUmber; ++i) {
		
		testFuncs.myAssertTrue("Device "+ userName + "_" + i + " was not detected !!\ntxt - " + exportFileTxt, exportFileTxt.contains(userName + "_" + i));
	}
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
	
	// Step 2 - Check no-devices, check 'Export-all' checkbox, export the devices and verify that all displayed devices are exported
	testFuncs.myDebugPrinting("Step 2 - Check no-devices, check 'Export-all' checkbox, export the devices and verify that all displayed devices are exported");  
	setSearch("");
	exportDevices(downloadedFile, true);
	exportFileTxt = testFuncs.readFile(testVars.getDownloadsPath() + "\\" + downloadedFile); 
	testFuncs.myWait(5000);
	for (int i = 1; i <= usersNUmber; ++i) {
		
		testFuncs.myAssertTrue("Device "+ userName + "_" + i + " was not detected !!\ntxt - " + exportFileTxt, exportFileTxt.contains(userName + "_" + i));
	}
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
	
	// Step 3 - Check some devices, don't check 'Export-all' checkbox, export the devices and verify that only selected devices are exported
	testFuncs.myDebugPrinting("Step 3 - Check some devices, don't check 'Export-all' checkbox, export the devices and verify that only selected devices are exported");  
	setSearch(userName + "_1");
	exportDevices(downloadedFile, false);
	exportFileTxt = testFuncs.readFile(testVars.getDownloadsPath() + "\\" + downloadedFile); 
	testFuncs.myWait(5000);
	testFuncs.myAssertTrue("Device "+ userName + "_1" + " was not detected !!\ntxt - " + exportFileTxt, exportFileTxt.contains(userName + "_1"));
	testFuncs.myAssertTrue("Device "+ userName + "_2" + " was detected !!\ntxt - "     + exportFileTxt, !exportFileTxt.contains(userName + "_2"));
	testFuncs.myAssertTrue("Device "+ userName + "_3" + " was detected !!\ntxt - " 	   + exportFileTxt, !exportFileTxt.contains(userName + "_3"));
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
	
	// Step 4 - Check some devices, check 'Export-all' checkbox, export the devices and verify that all displayed devices are exported
	// Nir 9\7\17 7.4.245 - There is a bug (VI 145832) about this issue
	testFuncs.myDebugPrinting("Check some devices, check 'Export-all' checkbox, export the devices and verify that all displayed devices are exported");  
	setSearch(userName +"_1");
	exportDevices(downloadedFile, true);
	exportFileTxt = testFuncs.readFile(testVars.getDownloadsPath() + "\\" + downloadedFile); 
	testFuncs.myWait(5000);
	for (int i = 1; i <= usersNUmber; ++i) {
		
		testFuncs.myAssertTrue("Device "+ userName + "_" + i + " was not detected !!\ntxt - " + exportFileTxt, exportFileTxt.contains(userName + "_" + i));
	}
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
  }

  // Set search text-box
  private void setSearch(String str) {
	  
	driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).clear();
	testFuncs.myWait(3000);
	if (!str.isEmpty()) {
		
		driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(str);	    	
	}
	driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
	testFuncs.myWait(10000);
  }

  // Export All devices at Device-Status menu
  private void exportDevices(String downloadedFile, boolean isExportAll) {
	  
	// Export All devices at Device-Status menu
	testFuncs.myDebugPrinting("Export All devices at Device-Status menu", testVars.logerVars.NORMAL);  
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[2]"), 7000);      
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Export to CSV"); 
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are your sure you want to export the devices to CSV??");	
	if (isExportAll) {
		
		testFuncs.myDebugPrinting("isExportAll - true", testVars.logerVars.MINOR);    
		testFuncs.myClick(driver, By.xpath("//*[@id='update']"), 5000);
	}
	testFuncs.myClick(driver, By.xpath("/html/body/div[3]/div/button[1]"), 120000);  
	testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile));
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
