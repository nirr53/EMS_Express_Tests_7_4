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
* This test tests an import of users with multiple languages
* ----------------
* Tests:
* 	 - Login and enter the Import users menu.
* 	 1. Import users with multiple languages.
* 	 2. Delete the imported users.
* 
* Results:
* 	 1. All the users should be imported successfully.
* 	 2. All the users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test65__import_diffrent_langs_users {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test65__import_diffrent_langs_users(String browser) {
	  
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
  public void Manage_multiple_users_import_non_english_users() throws Exception {
	  
	  
	Log.startTestCase(this.getClass().getName());
  
	// Set vars
	String 	 path        		= "";
	Map<String, String> map 	= new HashMap<String, String>();
	String   xpathUploadField  	= "//*[@id='file_source']";  
	String   xpathUploadButton  = "//*[@id='uploadForm']/div[2]/a";  
	String[] confirmMessageStrs = {"Import Users & Devices", "The import action can take a few minutes. Do you want to continue?"};  
	String   usersNumber   		= "12";	  
	String   prefixUser		 	= "lang__";
	String[] userNames		 	= {"lang__czech1__úůýžáďéěíňóřšť"	 				  , "lang__czech2__ÚŮÝŽÁČĎÉĚÍŇÓŘŠŤ"												 ,
								   "lang__french1__ùûüÿ€àâæçéèêëïîôœ"				  , "lang__french2__ÙÛÜŸ€ÀÂÆÇÉÈÊËÏÎÔŒ"											 ,
								   "lang__german__äöüßÄÖÜẞ"			 				  , "lang__spanish__€áéíñóúü€ÁÉÍÑÓÚÜ"											 ,
								   "lang__portuguese__€ãáàâçéêíõóôúü€ÃÁÀÂÇÉÊÍÕÓÔÚÜ"   , "lang__russian1__ёъяшертуиопющэасдфгчйкльжзхцвбнм"							 ,
								   "lang__russian2__ЁЪЯШЕРТЫУИОПЩЭАСДФГЧЙКЛЬЖЗХЦВБНМ" , "lang__japanese__ろぬふあうえおやゆよわたていすかんなににらせちとしはきくまのりれけむつさそひこみもねるめ",
								   "lang__italian__àèéìòóù€ÀÈÉÌÒÓÙ€"				  , "lang__polish__€ąćęłńóśźż€ĄĆĘŁŃÓŚŹŻ"}; 

    // Login and Enter the Import-export users+devices menu
	testFuncs.myDebugPrinting("Login and Enter the Import-export users+devices menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser); 
	testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_import", "Import Users and Devices information");
	
	// Step 1 - Import users+devices
	testFuncs.myDebugPrinting("Step 1 - Import users+devices");  
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("65");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton, confirmMessageStrs);			  
	for (int i = 1; i <= Integer.valueOf(usersNumber); ++i) {

		testFuncs.searchStr(driver, userNames[i-1] + "@" + testVars.getDomain() + " Added, Device " + userNames[i-1] + " - added");     	
	}
		
	// Step 2 - Delete the created users
	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");  
	testFuncs.selectMultipleUsers(driver, prefixUser, usersNumber);
	map.put("usersPrefix"	    , prefixUser);
	map.put("usersNumber"	    , usersNumber);    
	map.put("startIdx"   	    , String.valueOf(1));    
	map.put("srcUsername"	    ,  "Finished");
	map.put("action"	 	    , "Delete Users");	    
	map.put("skipVerifyDelete", "true");
	testFuncs.setMultipleUsersAction(driver, map);
	for (int i = 1; i < Integer.valueOf(usersNumber); ++i) {
	
		testFuncs.searchStr(driver, userNames[i-1] + "@" + testVars.getDomain() + " Finished");			  
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
