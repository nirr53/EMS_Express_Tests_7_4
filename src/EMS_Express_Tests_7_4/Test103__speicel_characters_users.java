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
* This test tests a create of users with speicel characters
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a user using POST query with !#$ charactarers
* 	 2. Create a user using POST query with /=? charactarers
* 	 3. Create a user using POST query with ^_` charactarers
* 	 4. Create a user using POST query with {|} charactarers
* 	 5. Create a user using POST query with ~;  charactarers
* 	 6. Create a user using POST query with *   charactarer
* 	 7. Create a user using POST query with +;  charactarer
*  	 8. Create a user using POST query with '   charactarer => bug
* 	 9. Delete the users.
* 
* Results:
* 	 1-8. All the users should be created successfully.
* 	   9. All users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test103__speicel_characters_users {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test103__speicel_characters_users(String browser) {
	  
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
  public void Speicel_characters_users() throws Exception {

	Log.startTestCase(this.getClass().getName());
	
	// Set vars and login
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	String Id             = testFuncs.getId();
	String prefixName     = "sChars_" + Id + "_";
//	String suffixes[]     = {"!#$", "/=?", "^_`", "{|}", "~;", "*", "+", "'" }; //The ' character cause bugs - known bug
//	String suffixes[]     = {"!#$", "/=?", "^_`", "{|}", "~;", "*", "+"};
	
	String suffixes[]     = {"'" }; //The ' character cause bugs - known bug

	
    // Step 1-8 - Create a user using POST query with !#$ charactarers
	int len = suffixes.length;
	for (int i = 0; i < len; ++i) {
		
		testFuncs.myDebugPrinting("Step " + i + " - Create a user using POST query with " + suffixes[i] + " charactarers");
		testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
		String temPUsername  = prefixName + suffixes[i];
		testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()      ,
				 testVars.getPort()    		,
				 "1"				   		,
				 temPUsername		   		,
				 testVars.getDomain()  		,
				 "registered"          		,
				 testVars.getDefPhoneModel(),
				 testVars.getDefTenant()    ,
				 "myLocation");
		testFuncs.verifyPostUserCreate(driver, temPUsername, temPUsername, true);	
	}	
	   
	// Step 9 - Delete the users
	testFuncs.myDebugPrinting("Step 9 - Delete the users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, prefixName, String.valueOf(suffixes.length));
	Map<String, String> map = new HashMap<String, String>();
	map.put("action"	 ,  "Delete Users");
    map.put("usersNumber",  String.valueOf(suffixes.length)); 
	map.put("srcUsername",  "Finished");
	map.put("skipVerifyDelete", "true");
	map.put("usersPrefix",  prefixName);
    map.put("startIdx"   ,  String.valueOf(1));
	testFuncs.setMultipleUsersAction(driver, map);
	
	// Verify delete
	testFuncs.myDebugPrinting("Verify delete", testVars.logerVars.NORMAL);
	for (int i = 0; i < len; ++i) {
		
		String temPUsername  = prefixName + suffixes[i];
		testFuncs.myDebugPrinting(i + ". Verify delete of  " + temPUsername, testVars.logerVars.MINOR);
		testFuncs.searchStr(driver, temPUsername + "@" + testVars.getDomain() + " Finished");    
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
