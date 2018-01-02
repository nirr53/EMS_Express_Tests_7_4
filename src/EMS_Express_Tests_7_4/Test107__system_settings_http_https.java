package EMS_Express_Tests_7_4;

import java.util.ArrayList;
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
* This test tests the HTTP and HTTPS mechnism on the system
* ----------------
* Tests:
* 	 - Login the systen and create a user via a POST query
* 	 - Enter the system settings menu and set the system to work with HTTP
* 	 1. Verify that the URLs in DHCP settings menu are changed respectivly.
* 	 2. Verify that the opend url at open-web-admin action of devices is changed respectivly.
* 
* 	 - Enter the system settings menu and set the system to work with HTTPS
* 	 3. Verify that the URLs in DHCP settings menu are changed respectivly.
* 	 4. Verify that the opend url at open-web-admin action of devices is changed respectivly.
* 
* 	 5. Delete the created user
* 
* Results:
* 	 1-2. The url should be based on HTTP. 	 
*    3-4. The url should be based on HTTPS.
*      5. The user should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test107__system_settings_http_https {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test107__system_settings_http_https(String browser) {
	  
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
  public void System_settings_http_https() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set vars and login
	String userName     = "httphttpsuser" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
		
	// Create a user using POST query
	testFuncs.myDebugPrinting("Create a user using POST query");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp(),
				 				testVars.getPort()    						 ,
				 				"1"				   							 ,
				 				userName		   						     ,
				 				testVars.getDomain()  						 ,
				 				"registered"          						 ,
				 				testVars.getDefPhoneModel()              	 ,
				 				testVars.getDefTenant()               		 ,
				 				"myLocation");
	testFuncs.verifyPostUserCreate(driver, userName, userName, true);
	  
    // Enter the system settings menu and set the system to work with HTTPS
	testFuncs.myDebugPrinting("Enter the system settings menu and set the system to work with HTTPS");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	setHttpOrhttps(true);
	  
	// Step 1 - Enter the DHCP option configuration menu and check that urls are changed respectivly
	testFuncs.myDebugPrinting("Step 1 - Enter the DHCP option configuration menu and check that urls are changed respectivly");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_dhcp_options_configuration", "DHCP Options Configuration");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/thead/tr/th"	 , "System URLs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[1]/td[2]/b/span"  , "https://" + testVars.getIp() + "/firmwarefiles;ipp/dhcpoption160.cfg");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[2]/td[2]/b/span"  , "https://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/httpproxy/");
    testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/b/span", "https://" + testVars.getIp() + "/firmwarefiles;ipp/tenant/");
    testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/b/span", "https://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/tenant/");	    
    testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[3]/td[2]/b/span", "https://" + testVars.getIp() + "/ipp/tenant/");

	// Step 2 - Enter the devices menu, select the create device and verify that its admin is opened with https
	testFuncs.myDebugPrinting("Step 2 - Enter the devices menu, select the create device and verify that its admin is opened with https");
	testFuncs.enterMenu(driver, "Monitor_device_status", "Devices Status");
	searchAndSelectDevice(driver, userName);
	openWebAdminDevice("https");
	
    // Enter the system settings menu and set the system to work with HTTP
	testFuncs.myDebugPrinting("Enter the system settings menu and set the system to work with HTTP");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	setHttpOrhttps(false);
	
	// Step 3 - Enter the DHCP option configuration menu and check that urls are changed respectivly
	testFuncs.myDebugPrinting("Step 3 - Enter the DHCP option configuration menu and check that urls are changed respectivly");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_dhcp_options_configuration", "DHCP Options Configuration");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/thead/tr/th"	 , "System URLs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[1]/td[2]/b/span"  , "http://" + testVars.getIp() + "/firmwarefiles;ipp/dhcpoption160.cfg");
    testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[1]/tbody/tr[2]/td[2]/b/span"  , "http://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/httpproxy/");
    testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/b/span", "http://" + testVars.getIp() + "/firmwarefiles;ipp/tenant/");
    testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/b/span", "http://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/tenant/");	    
    testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[3]/div/div[2]/table[2]/tbody/tr[2]/td/table/tbody/tr[3]/td[2]/b/span", "http://" + testVars.getIp() + "/ipp/tenant/");

	// Step 4 - Enter the devices menu, select the create device and verify that its admin is opened with https
	testFuncs.myDebugPrinting("Step 4 - Enter the devices menu, select the create device and verify that its admin is opened with https");
	testFuncs.enterMenu(driver, "Monitor_device_status", "Devices Status");
	searchAndSelectDevice(driver, userName);
	openWebAdminDevice("http");
	
    // Step 5 - Delete the created user
  	testFuncs.myDebugPrinting("Step 5 - Delete the created user");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, userName, "1");
    map.put("usersPrefix"	  , userName);
    map.put("usersNumber"	  , "1"); 
    map.put("startIdx"   	  , "1");
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, userName + "@" + testVars.getDomain() + " Finished");
  }
  
  // Open Web Admin of device
  private void openWebAdminDevice(String prefix) {
	  
	  // Open Web Admin
	  testFuncs.myDebugPrinting("Open Web Admin of device", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[4]/a"), 20000);
	  String ip = testFuncs.readFile("ip_1.txt");
	  testFuncs.myDebugPrinting("ip - " + ip, testVars.logerVars.MINOR);
	  String parentHandle = driver.getWindowHandle();
	  ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
	  driver.switchTo().window((String) tabs.get(1));
	  testFuncs.myWait(100000);
	
	  // Verify the url used the given http prefix (http or https)
	  String url = driver.getCurrentUrl();
	  testFuncs.myDebugPrinting("url - " + url, testVars.logerVars.MINOR); 
	  if (prefix.equals("http")) {
		  
		  testFuncs.myAssertTrue("URL was not opened in http format !!", url.contains("http://" + ip) || url.contains("about:blank"));
	  } else if (prefix.equals("https")) {
		  
		  testFuncs.myAssertTrue("URL was not opened in https format !!", url.contains("https://" + ip) || url.contains("chrome-error://chromewebdata/"));  
	  }
	  
	  driver.close();  
	  driver.switchTo().window(parentHandle);  
  }
  
  // Search for a device and select it via Select-All checkbox
  private void searchAndSelectDevice(WebDriver driver, String userName) {
	  
	  // Search device
	  testFuncs.myDebugPrinting("Search device", testVars.logerVars.NORMAL);
	  testFuncs.enterMenu(driver , "Monitor_device_status", "Devices Status");   
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + userName.trim(), 5000);
	  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	        
	  testFuncs.myWait(7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[8]", userName.trim());
	  
	  // Select the searched device via check Select-All check-box
	  testFuncs.myDebugPrinting("Select the searched device via check Select-All check-box", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='table']/tbody[1]/tr/td[2]/input"), 3000);  
  }
  
  // Set http or https according to given string
  private void setHttpOrhttps(Boolean isHttps) {
	  
	  WebElement devHttps  		   = driver.findElement(By.xpath("//*[@id='using_https']"));
	  WebElement confHttps 		   = driver.findElement(By.xpath("//*[@id='using_https_to_ems']"));
	  WebElement openWebAdminHttps = driver.findElement(By.xpath("//*[@id='using_https_open_web']"));
	  if (isHttps) {
		  		
		  testFuncs.myDebugPrinting("isHttps - true, check all checkboxes", testVars.logerVars.MINOR);
		  if (!devHttps.isSelected()) 		   { devHttps.click();   		}
		  if (!confHttps.isSelected())         { confHttps.click();         }
		  if (!openWebAdminHttps.isSelected()) { openWebAdminHttps.click(); }
		   
	  } else {
		  
		  testFuncs.myDebugPrinting("isHttps - false, uncheck all checkboxes", testVars.logerVars.MINOR); 
		  if (devHttps.isSelected()) 		  { devHttps.click();   	   }
		  if (confHttps.isSelected())         { confHttps.click();         }
		  if (openWebAdminHttps.isSelected()) { openWebAdminHttps.click(); }	  
	  }
	  
	  // Submit
	  testFuncs.myDebugPrinting("Submit", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							  , 2000);
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
