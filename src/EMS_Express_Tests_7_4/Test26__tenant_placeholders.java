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
* This test tests the Tenant placeholder values
* ----------------
* Tests:
* 	 - Enter Tenant placeholder values menu.
* 	 1. Add a new Tenant Placeholder
*    2. Edit the created Tenant Placeholder
*    3. Delete the created Tenant Placeholder
*    4. Copy placeholders from another tenant.
*    5.  Delete PHs from old copied and copy-to tenants
* 
* Results:
* 	 1. The Tenant Placeholder should be added successfully
* 	 2. The Tenant Placeholder should be edited successfully
* 	 3. The Tenant Placeholder should be deleted successfully
*  	 4. The Tenant Placeholder should be copied successfully
*  	 5. The PHs from all tenants should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test26__tenant_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test26__tenant_placeholders(String browser) {
	  
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
  public void Tenant_placeholders() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String Id 	         = testFuncs.getId();
	String tenPhName     = "tenPhName"     + Id;
	String tenPhValue    = "tenPhValue"    + Id;
	String tenNewPhValue = "tenNewPhValue" + Id;
	String tenTenant     = testVars.getDefTenant();
	String tenNewTenant  = testVars.getNonDefTenant(0);
	
	testFuncs.myDebugPrinting("Enter the Tenants configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	 	
	// Step 1 - Add a new Tenant Placeholder
	testFuncs.myDebugPrinting("Step 1 - Add a new Tenant Placeholder");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
	
	// Step 2 - Edit the created Tenant Placeholder
	testFuncs.myDebugPrinting("Step 2 - Edit the created Tenant Placeholder");
	testFuncs.editTenantPH(driver, tenPhName, tenNewPhValue);
	
	// Step 3 - Delete the created Tenant Placeholder
	testFuncs.myDebugPrinting("Step 3 - Delete the created Tenant Placeholder");
	testFuncs.deleteTenantPH(driver, tenPhName, tenNewPhValue);
	
	// Step 4 - Copy Tenant placeholder from another tenant
	testFuncs.myDebugPrinting("Step 4 - Copy Tenant placeholders from another tenant");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
    testFuncs.copyPlaceholder(driver, tenNewTenant, tenPhName, tenPhValue, tenTenant);
    
    // Step 5 - Delete PHs from old copied and copy-to tenants
	testFuncs.myDebugPrinting("Step 5 - Delete PHs from old copied and copy-to tenants");
	testFuncs.deleteTenantPH(driver, tenPhName, tenNewPhValue);
	testFuncs.selectTenant(driver, tenTenant);
	testFuncs.deleteTenantPH(driver, tenPhName, tenNewPhValue);
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
