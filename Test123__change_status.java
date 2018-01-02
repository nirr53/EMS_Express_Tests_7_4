package EMS_Tests;

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
* This test tests a change of device status
* ----------------
* Tests:
* 	 - Create a registered user via POST query.
* 	 1. Change the user's device status to 'Offline'.
* 	 2. Change the users's device status to 'Registered'.
* 	 3. Delete the created users.
* 
* Results:
* 	 1+2. Device status should be updated.succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test123__change_status {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test123__change_status(String browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
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
  public void Change_device_status() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars and login
	testFuncs.myDebugPrinting("Set vars and login");
	String chngSttsUsername = testFuncs.getId();
	String location 		= "myLocation";
	String phoneNumber		= "+97239764713";
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  

    // Create a registered user
	testFuncs.myDebugPrinting("Create a registered user");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 												 testVars.getPort()         ,
			 												 "1"				        ,
			 												 chngSttsUsername               ,
			 												 testVars.getDomain()       ,
			 												 "registered"          	    ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 location);
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
    testFuncs.verifyPostUserCreate(driver, chngSttsUsername, chngSttsUsername, true);
    
    // Step 1 -  Change the user's device status to 'Offline'
 	testFuncs.myDebugPrinting("Step 1 -  Change the user's device status to 'Offline'");
	testFuncs.sendKeepAlivePacket(testVars.getKpAlveBatName()	 ,
								  testVars.getIp()               ,
								  testVars.getPort()           	 ,
								  testFuncs.readFile("mac_1.txt"),
								  chngSttsUsername				 ,
								  testVars.getDefPhoneModel()	 ,
								  testVars.getDomain()        	 ,
								  "offline"						 ,
								  location						 ,
								  phoneNumber);
	testFuncs.enterMenu(driver, "Monitor_device_status", "Devices Status");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + chngSttsUsername.trim(), 5000);
    driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
    String attClass = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[8]/i")).getAttribute("class");
    testFuncs.myAssertTrue("Offline Icon was not detected !! <" + attClass + ">", attClass.contains("fa-times-circle"));  

    // Step 2 -  Change the user's device status to 'Registered'
 	testFuncs.myDebugPrinting("Step 2 -  Change the user's device status to 'Registered'");
	testFuncs.sendKeepAlivePacket(testVars.getKpAlveBatName()	 ,
								  testVars.getIp()               ,
								  testVars.getPort()           	 ,
								  testFuncs.readFile("mac_1.txt"),
								  chngSttsUsername				 ,
								  testVars.getDefPhoneModel()	 ,
								  testVars.getDomain()        	 ,
								  "registered"						 ,
								  location						 ,
								  phoneNumber);
	testFuncs.enterMenu(driver, "Monitor_device_status", "Devices Status");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + chngSttsUsername.trim(), 5000);
    driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
    String attClass2 = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[8]/i")).getAttribute("class");
    testFuncs.myAssertTrue("Registered Icon was not detected !! <" + attClass2 + ">", attClass2.contains("fa-check-square"));  
     
    // Step 3 - Delete the created users
  	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, chngSttsUsername, "1");
	Map<String, String> map = new HashMap<String, String>();
    map.put("startIdx"   	  ,  String.valueOf(1));
    map.put("usersNumber"	  ,  "1");
    map.put("usersPrefix"	  ,  chngSttsUsername);
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    chngSttsUsername  = chngSttsUsername.toLowerCase();
    testFuncs.searchStr(driver, chngSttsUsername + "@" + testVars.getDomain() + " Finished");
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
