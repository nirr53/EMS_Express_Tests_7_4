package EMS_Express_Tests_7_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
* This test tests the Tenants menu
* ----------------
* Tests:
* 	 1. Create a Tenant
*  	 2. Edit a Tenant
* 	 3. Delete the created Tenant
* 	 4. Create a default Tenant
* 	 5. Delete a default Tenant 
* 
* Results:
* 	 1. Tenant should be created successfully.
* 	 2. Tenant should be edited successfully.
* 	 3. Tenant should be deleted successfully.
* 	 4. Default Tenant should be created successfully. 
* 	 5. Default Tenant should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test122__tenants_create_edit_delete {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test122__tenants_create_edit_delete(String browser) {
	  
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
  public void Tenant_create_edit_delete() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String id 			 = testFuncs.getId();
	String tenName		 = "tenName" + id;
	String tenDesc		 = "tendesc" + id;
	String subnet 		 = "255.255.255.0";
	String defTenantName = "Default";

	// Login and enter the Tenants menu
	testFuncs.myDebugPrinting("Login and enter the Tenants menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_System_tenants", "Tenant List");
    
    // Step 1 - Create a Tenant
 	testFuncs.myDebugPrinting("Step 1 - Create a Tenant");
 	createTenant(tenName, tenDesc, subnet, false);
  
	// Step 2 - Edit a Tenant
 	testFuncs.myDebugPrinting("Step 2 - Edit a Tenant");	
 	String newTenName 	= "newTenName" 		  + id;
 	String newTenDesc 	= "newTenDescription" + id;
 	String newTenSubnet = "255.255.255.255";
 	editTenant(tenName, newTenName, newTenDesc, newTenSubnet, false);
 	
	// Step 3 - Delete a Tenant
 	testFuncs.myDebugPrinting("Step 3 - Delete a Tenant");
 	deleteTenant(newTenName, false);
 	
	// Step 4 - Create a default Tenant
 	testFuncs.myDebugPrinting("Step 4 - Create a default Tenant");
	tenName	= "tenDefName" + id;
	tenDesc	= "tenDefdesc" + id;
 	createTenant(tenName, tenDesc, subnet, true);
 	
	// Step 5 - Delete a default Tenant
 	testFuncs.myDebugPrinting("Step 5 - Delete a default Tenant");
 	deleteTenant(tenName, true);
 	editTenant(defTenantName, defTenantName, "", "", true);
 	deleteTenant(tenName, false);
  }

  // Delete a Tenant
  private void deleteTenant(String tenName, Boolean isDefault) {
	  
	  // Delete the Teannt
	  testFuncs.myDebugPrinting("Delete the Teannt", testVars.logerVars.MINOR);
	  int idx = getIdx(tenName);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[3]/table/tbody[1]/tr[" + idx + "]/td[7]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Tenant");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the tenant: " + tenName); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Tenant");	  
	  if (isDefault) {
		  
		  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "You cannot delete default tenant.");
		  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);

	  } else {
		  
		  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Tenant was deleted successfully.");
		  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		  
		  // Verify delete
		  testFuncs.myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
		  String bodyText     = driver.findElement(By.tagName("body")).getText();
		  testFuncs.myAssertTrue("Delete failed !!", !bodyText.contains(tenName));
	  }
  }
  
  // Edit a Tenant
  private void editTenant(String tenName, String newTenName, String newTenDesc, String newTenSubnet, Boolean newIsDefault) {
	  
	  // Enter the Teannt menu
	  testFuncs.myDebugPrinting("Enter the Teannt menu", testVars.logerVars.MINOR);
	  int idx = getIdx(tenName);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[3]/table/tbody[1]/tr[" + idx + "]/td[6]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div[2]/div/div[1]", "Update Tenant");

	  // Fill data
	  testFuncs.myDebugPrinting("Fill data", testVars.logerVars.MINOR);
	  if (!tenName.contains(newTenName) && !newTenName.contains(tenName)) {
	  
		  testFuncs.myDebugPrinting("Update name", testVars.logerVars.MINOR);
		  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/div[1]/input"), newTenName  , 3000);
	  }
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/div[2]/input"), newTenDesc  , 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/div[3]/input"), newTenSubnet, 3000);
	  if (newIsDefault) {
		  
		  testFuncs.myClick(driver, By.xpath("//*[@id='isdef']"), 5000);	  
	  }
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[3]/button[2]"), 1000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Tenant");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Tenant was saved successfully."); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  
	  // Verify edit
	  testFuncs.myDebugPrinting("Verify edit", testVars.logerVars.MINOR);
	  String bodyText     = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Update failed !!", bodyText.contains(newTenName));
	  testFuncs.myAssertTrue("Update failed !!", bodyText.contains(newTenDesc));
  }

  // Get idx of row of the edit / delete Tenant
  private int getIdx(String tenName) {
	  
	  // Get idx
	  testFuncs.myDebugPrinting("Get idx", testVars.logerVars.MINOR);
	  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
	  String l = null;
	  int i = 1;
	  Boolean countLines = false;
	  try {
		  
		while ((l = r.readLine()) != null) {
					
			  testFuncs.myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
			  if (l.contains(tenName)) {
						  
				  testFuncs.myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				  if (i > 1) {i++;}
				  break;
			  } else if (countLines) {
				
				i++;
			  } else if (l.contains("Edit   Delete")) {
				
				  countLines = true;
			  }
		  }
	
	  } catch (IOException e) {
		  
		e.printStackTrace();
	  }
	  if (l == null) {
		  
		  testFuncs.myFail("Tenant was not found !!");			  
	  }
	  
	  testFuncs.myDebugPrinting("returned i - " + i, testVars.logerVars.MINOR);
	  return i;
  }


  // Create a new Tenant
  private void createTenant(String tenName, String tenDesc, String tenSubnet, Boolean isDefault) {
	  
	  // Enter the Create-Teannt menu
	  testFuncs.myDebugPrinting("Enter the Create-Teannt menu", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[1]/a"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div[2]/div/div[1]", "Add New Tenant");
	  
	  // Fill data
	  testFuncs.myDebugPrinting("Fill data", testVars.logerVars.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/div[1]/input"), tenName  , 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/div[2]/input"), tenDesc  , 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/div[3]/input"), tenSubnet, 3000);
	  if (isDefault) {
		  
		  testFuncs.myClick(driver, By.xpath("//*[@id='isdef']"), 5000);	  
	  }
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[3]/button[2]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Tenant");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "New Tenant was saved successfully."); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  
	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
	  testFuncs.searchStr(driver, tenName);
	  testFuncs.searchStr(driver, tenDesc);  
	  int idx = getIdx(tenName);	  
	  String icon = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[3]/table/tbody[1]/tr[" + idx + "]/td[5]/i")).getAttribute("class");	  
	  if (isDefault) {
		  
		  testFuncs.myDebugPrinting("Check default Tenant", testVars.logerVars.MINOR);
		  testFuncs.myAssertTrue("Icon was not detecetd !!", icon.contains("-ok-circle green"));
	  } else {
		  
		  testFuncs.myDebugPrinting("Check not-default Tenant", testVars.logerVars.MINOR);
		  testFuncs.myAssertTrue("Icon was not detecetd !!", icon.contains("-ban-circle red"));
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
