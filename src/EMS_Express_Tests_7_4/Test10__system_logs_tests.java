package EMS_Express_Tests_7_4;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

/**
* ----------------
* This test tests the System logs page
* ----------------
* Tests:
* 	 - Enter the System logs page.
* 	 1. Check the Web admin menu and download last file
* 	 2. Check display mode and log-level mode.
* 	 3. Download Archive files
* 	 4. Check the Activity logs menu and download last file.
* 	 5. Check display modes.
* 	 6. Download Archive files.
* 
* Results:
* 	 1.6 All the headers should be detected. All downloads should work.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test10__system_logs_tests {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test10__system_logs_tests(String browser) {
	  
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
  public void System_logs() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
    // Step 1 - Enter the System Logs menu & check its appearance
	testFuncs.myDebugPrinting("Step 1 - Enter the System Logs menu & check its appearance");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Troubleshoot_system_diagnostics", "System Logs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[1]/h3"					  , "System Logs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/form/table/thead/tr/th", "System Logs");
    testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/form/table/tbody/tr[1]/td[1]/span/b", "Web Admin");
    testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/form/table/tbody/tr[2]/td[1]/span/b", "Activity");

    // Step 2.1 - Check the Web admin menu and download last file
	if (!this.usedBrowser.equals(testVars.IE)) {
		
	  	testFuncs.myDebugPrinting("Step 2.1 - Check the Web admin menu and download last file"); 	
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getSysLogsPrefix());
	  	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/form/table/tbody/tr[1]/td[2]/a")												, 2000);
	  	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/a"), 2000);
	  	testFuncs.myWait(120000);
	  	String fileName = testFuncs.getLatestFilefromDir(testVars.getDownloadsPath()).getName();
	    testFuncs.myDebugPrinting("fileName - " + fileName, testVars.logerVars.MINOR);
	    testFuncs.myAssertTrue("Downloaded file does not matches:\n" +
	    		          	   "fileName - "                         + fileName 													     + "   \n" +
	    		          	   "getSystemLogsFilename - "            + getSystemLogsFilename(testVars.getSysLogsPrefix(), ".log", false) + "   \n" +   		           
	    		          	   "getSystemLogsFilename - "            + getSystemLogsFilename(testVars.getSysLogsPrefix(), ".log", true)
	    		          	   ,
	    		          	   fileName.contains(getSystemLogsFilename(testVars.getSysLogsPrefix(), ".log", false)) ||
	    		          	   fileName.contains(getSystemLogsFilename(testVars.getSysLogsPrefix(), ".log", true)));
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getSysLogsPrefix());
	}

    // Step 2.2 - Check display mode and log-level mode
  	testFuncs.myDebugPrinting("Step 2.2 - Check display mode and log-level mode");
	Select displayOptions = new Select(driver.findElement(By.xpath("//*[@id='lines']")));
	for (int i = 0; i < displayOptions.getOptions().size(); ++i) {
		
		displayOptions.selectByIndex(i);
	  	testFuncs.myWait(1000); 	
	}	
	Select displayLogOptions = new Select(driver.findElement(By.xpath("//*[@id='loglevel']")));
	for (int i = 0; i < displayLogOptions.getOptions().size(); ++i) {
		
		displayLogOptions.selectByIndex(i);
	  	testFuncs.myWait(3000);
		testFuncs.myDebugPrinting(displayLogOptions.getFirstSelectedOption().getText(), testVars.logerVars.MINOR);
	  	testFuncs.myClick(driver, By.xpath("//*[@id='append_']"), 500);
	  	
	  	
	  	
	  	testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[1]/div[4]/h4", "Web Admin log level was saved successfully.");
	  	testFuncs.myWait(3000);
	}

    // Step 2.3 - Download Archive files
	if (!this.usedBrowser.equals(testVars.IE)) {
		
		testFuncs.myDebugPrinting("Step 2.3 - Download Archive files");
	  	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[1]/td[2]/a"), 3000);
	  	testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[2]/form/table/thead/tr/th", "Web Admin Archive Files");	
		String txt = driver.findElement(By.tagName("body")).getText();
		testFuncs.myDebugPrinting("txt - " + txt, testVars.logerVars.MINOR);
		int numOfFiles = txt.length() - txt.replace(".txt", "aaa").length();
		testFuncs.myDebugPrinting("numOfFiles - " + numOfFiles, testVars.logerVars.MINOR);
		for (int j = 1; j < numOfFiles; ++j) {
			
			testFuncs.myDebugPrinting("Download file #" + j, testVars.logerVars.MINOR);
		    driver.findElement(By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/table/tbody/tr[" + j  +"]/td[2]/a")).click();
		  	testFuncs.myWait(3000);		  	
		}
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getSysLogsPrefix());
	}
	
	// Returned to System-logs menu
  	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button"), 7000);
  	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button"), 7000);
  	  	
    // Step 2.4 - Check the Activity logs menu and download last file
	if (!this.usedBrowser.equals(testVars.IE)) {
		
	  	testFuncs.myDebugPrinting("Step 2.4 - Check the Activity logs menu and download last file"); 	
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getSysLogsActPre());
	  	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/form/table/tbody/tr[2]/td[2]/a")												, 30000);
	  	driver.findElement(By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/a")).click();
	  	testFuncs.myWait(120000);
	  	String fileName = testFuncs.getLatestFilefromDir(testVars.getDownloadsPath()).getName();
	    testFuncs.myDebugPrinting("fileName - " + fileName, testVars.logerVars.MINOR);
	    testFuncs.myAssertTrue("Downloaded file does not matches:\n" +
	          	   "fileName - "                         + fileName 													     + "   \n" +
	          	   "getSystemLogsFilename - "            + getSystemLogsFilename(testVars.getSysLogsActPre(), ".csv", false) + "   \n" +   		           
	          	   "getSystemLogsFilename - "            + getSystemLogsFilename(testVars.getSysLogsActPre(), ".csv", true)
	          	   ,
	          	   fileName.contains(getSystemLogsFilename(testVars.getSysLogsActPre(), ".csv", false)) ||
	          	   fileName.contains(getSystemLogsFilename(testVars.getSysLogsActPre(), ".csv", true)));
	    testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getSysLogsActPre());
	}
		
    // Step 2.5 - Check display mode
  	testFuncs.myDebugPrinting("Step 2.5 - Check display mode");
	displayOptions = new Select(driver.findElement(By.xpath("//*[@id='lines']")));
	for (int i = 0; i < displayOptions.getOptions().size(); ++i) {
	
		displayOptions.selectByIndex(i);
	  	testFuncs.myWait(3000);
	}
	
    // Step 2.6 - Download Archive files
	if (!this.usedBrowser.equals(testVars.IE)) {
		
		testFuncs.myDebugPrinting("Step 2.6 - Download Archive files");
	 	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[1]/td[2]/a"), 3000);
	 	testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[2]/form/table/thead/tr/th", "Activity Archive Files");
		String txt = driver.findElement(By.tagName("body")).getText();
		testFuncs.myDebugPrinting("txt - " + txt, testVars.logerVars.MINOR);
		int numOfFiles = txt.length() - txt.replace(".csv", "aaa").length();
		testFuncs.myDebugPrinting("numOfFiles - " + numOfFiles, testVars.logerVars.MINOR);
	 	for (int j = 1; j <= numOfFiles; ++j) {
	 		
			testFuncs.myDebugPrinting("Download file #" + j, testVars.logerVars.MINOR);
	 	    driver.findElement(By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/table/tbody/tr[" + j + "]/td[2]/a")).click();
	 	  	testFuncs.myWait(20000);
	 	}
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getSysLogsActPre());
	}
  }
  
  /**
  *  Get System logs name of the log
  *  @param  logPrefix      - suffix of the log (I.e. IPP.Manage.Activity.)
  *  @param  suffix         - suffix of the log (I.e. .csv or . log)
  *  @param  isAddDayNeeded - is add day is needed
  *  @return Current name of the System logs log
  */
  @SuppressWarnings("deprecation")
  public String getSystemLogsFilename(String logPrefix, String suffix, Boolean isAddDayNeeded) {
	  
	int addedvalue = 0;
    Date   date  = new Date();
    if (isAddDayNeeded) { addedvalue = -1; }
	String day   = Integer.toString(date.getDate() + addedvalue);
	String month = Integer.toString(date.getMonth() + 1);
	if (day.length() == 1)   { day   = "0" + day;   }
    if (month.length() == 1) { month = "0" + month; }
    logPrefix += day + "-" + month + "-";
    logPrefix += Integer.toString(date.getYear() + 1900) + suffix;
    return logPrefix;	
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
