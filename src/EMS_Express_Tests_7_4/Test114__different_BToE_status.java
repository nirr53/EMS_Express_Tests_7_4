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
* This test tests the create of users with differnt BToE statuses.
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a user using POST query with "BToE disabled" status.
* 	 2. Create a user using POST query with "auto paired"   status.
* 	 3. Create a user using POST query with "manual paired" status.
* 	 4. Create a user using POST query with "not paired"    status.
* 	 5. Delete the users.
* 
* Results:
* 	 1-4. User should be created successfully with the given status.
* 	   5. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test114__different_BToE_status {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test114__different_BToE_status(String browser) {
	  
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
  public void Different_BToE_status() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars login
	String Id             = testFuncs.getId();
	String prefixName     = "BToE_user";
	String usersNumber	  = "4";
	String btoeDisabled   = prefixName + "_dis_"    + Id;
	String btoeAutoPaired = prefixName + "_auto_"   + Id;
	String btoeManPaired  = prefixName + "_manual_" + Id;
	String btoeNotPaired  = prefixName + "_not_"    + Id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	
    // Step 1 - Create a user using POST query with "BToE disabled" status
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query with \"BToE disabled\" status");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()      ,
			 testVars.getPort()    		,
			 "1"				   		,
			 btoeDisabled		   		,
			 testVars.getDomain()  		,
			 "registered"          		,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver, btoeDisabled, btoeDisabled, true);
	checkBToE("disabled");  
	
    // Step 2 - Create a user using POST query with "auto paired" status
	testFuncs.myDebugPrinting("Step 2 - Create a user using POST query with \"auto paired\" status");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()      ,
			 testVars.getPort()    		,
			 "1"				   		,
			 btoeAutoPaired		   		,
			 testVars.getDomain()  	    ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver, btoeAutoPaired, btoeAutoPaired, true);
	checkBToE("automatic");  
	
    // Step 3 - Create a user using POST query with "manual paired" status
	testFuncs.myDebugPrinting("Step 3 - Create a user using POST query with \"manual paired\" status.");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()      ,
			 testVars.getPort()    		,
			 "1"				   		,
			 btoeManPaired		   		,
			 testVars.getDomain()  	    ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver, btoeManPaired, btoeManPaired, true);
	checkBToE("manual");  	
	
    // Step 4 - Create a user using POST query with "not paired" status
	testFuncs.myDebugPrinting("Step 4 - Create a user using POST query with \"not paired\"    status.");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()      ,
			 testVars.getPort()    		,
			 "1"				   		,
			 btoeNotPaired		   		,
			 testVars.getDomain()  	    ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver, btoeNotPaired, btoeNotPaired, true);
   
   // Step 5 - Delete the users
  	testFuncs.myDebugPrinting("Step 5 - Delete the users");
    Map<String, String> map = new HashMap<String, String>();
    map.put("action"	      , "Delete Users");
    map.put("srcUsername"     , "Finished");
    map.put("skipVerifyDelete", "true");
    map.put("usersPrefix"     , prefixName);
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(1));  
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, prefixName, usersNumber);   
    testFuncs.setMultipleUsersAction(driver, map);  
    btoeDisabled   = btoeDisabled.toLowerCase();
    btoeAutoPaired = btoeAutoPaired.toLowerCase();
    btoeManPaired  = btoeManPaired.toLowerCase();
    btoeNotPaired  = btoeNotPaired.toLowerCase();   
    testFuncs.searchStr(driver, btoeDisabled   + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, btoeAutoPaired + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, btoeManPaired  + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, btoeNotPaired  + "@" + testVars.getDomain() + " Finished");
  }
  
  private void checkBToE(String state) {
	      
	  // Check BToE
	  testFuncs.myDebugPrinting("Check that BToE icon and BToE version were detected !", testVars.logerVars.NORMAL);  
	  String title = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[5]/i")).getAttribute("title");
	  String style = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[5]/i")).getAttribute("style");  
	  testFuncs.myDebugPrinting("state - " + state, testVars.logerVars.MINOR);  
	  switch (state) {
	  
	  	case "disabled":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("BToE disabled"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: red"));
	  		break;	  		
	  	case "automatic":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("auto paired"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: green"));
	  		break;
	  	case "manual":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("manual paired"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: blue"));
	  		break;
	  	case "notPaired":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("not paired"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: orange"));
	  		break;
	  }; 
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
