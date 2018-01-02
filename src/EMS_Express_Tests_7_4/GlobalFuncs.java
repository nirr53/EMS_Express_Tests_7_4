package EMS_Express_Tests_7_4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import static org.junit.Assert.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
* This class holds all the functions which been used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalFuncs {
	
	  /**
	  *  webUrl  	  - default url for the used functions
	  *  username  	  - default username for the used functions
	  *  password 	  - default password for the used functions
	  *  StringBuffer - default string for errors buffering
	  */
	  GlobalVars 		   testVars;
	  MenuPaths            testMenuPaths;
	  private String	   webUrl;
	  @SuppressWarnings("unused")
	  private StringBuffer verificationErrors = new StringBuffer();
	  private static final Logger logger = LogManager.getLogger();

	  /**
	  *  Default constructor
	  */
	  public GlobalFuncs() {
		  
		  testVars 		= new GlobalVars();
		  testMenuPaths = new  MenuPaths();
		  webUrl        = testVars.getUrl();
	  }
	  
	  /**
	  *  Constructor which get data
	  *  @param givenUrl 	  - given url
	  *  @param givenUsername - given username
	  *  @param givenPassword - given password
	  */
	  public GlobalFuncs(String givenUrl, String givenUsername, String givenPassword) {
		 
		  testVars      = new GlobalVars();
		  testMenuPaths = new  MenuPaths(); 
		  webUrl        = givenUrl;	 
	  }
	  
	  /**
	  *  login method
	  *  @param driver   - given driver for make all tasks
	  *  @param username - given string for system
	  *  @param password - given password for the system
	  *  @param mainStr  - given string for verify good access
	  *  @param httpStr  - given string for using as a prefix for the url
	  *  @param brwType  - name of the used browser
	  */
	  public void login(WebDriver 	driver, String username, String password, String mainStr, String httpStr, String brwType) {
		  
	      String title = driver.getTitle();
    	  myDebugPrinting("1. title - "   	    + title  			,testVars.logerVars.MINOR);
    	  myDebugPrinting("httpStr + webUrl - " + httpStr + webUrl  ,testVars.logerVars.MINOR);
	      driver.get(httpStr + webUrl);
	      myWait(5000); 	
	      
	      if (brwType.equals("IE") && title.equals("WebDriver") && httpStr.equals("https://")) {
	    	  
	    	  driver.findElement(By.xpath("//a[@id='overridelink']")).click();
	    	  myWait(5000);
	    	  
	      }
    	  searchStr(driver, testVars.getMainPageStr());  
	      myDebugPrinting("username - " + username ,testVars.logerVars.MINOR);
    	  myDebugPrinting("password - " + password ,testVars.logerVars.MINOR);
    	  mySendKeys(driver, By.xpath("//*[@id='loginform']/div[1]/input")	   , username, 2500);
    	  mySendKeys(driver, By.xpath("//*[@id='loginform']/div[2]/input")	   , password, 2500);    	  
    	  myClick(driver, By.xpath("//*[@id='loginform']/div[4]/div[2]/button"), 3000);
	      
	      // Verify good access
    	  myAssertTrue("Login fails ! (mainStr - " + mainStr + " was not detected !!)", driver.findElement(By.tagName("body")).getText().contains(mainStr));
	  }
	  
	  /**
	  *  Verify string in page based on read the whole page
	  *  @param driver  - given driver
	  *  @param strName - given string for detect
	  */
	  public void searchStr(WebDriver 	driver, String strName) {
		  
		  String bodyText     = driver.findElement(By.tagName("body")).getText();
		  if (bodyText.contains(strName)) {
			  
			  myDebugPrinting("<" + strName + "> was detected !!",  testVars.logerVars.MINOR);
		  } else {
			  
			  myFail("<" + strName + "> was not detected !! \nbodyText - " + bodyText);
		  }
	  }
	  
	  /**
	  *  Press the Home Button
	  *  @param driver - given driver
	  */
	  public void pressHomeButton(WebDriver driver) {
		  
		  myWait(7000);		  
		  myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[1]/li[2]/a"), 7000); 
		  searchStr(driver, "Statistics");
	  }
	  
	  /**
	  *  Press the Network Topology Button
	  *  @param driver - given driver
	  */
	  public void pressNetworkTopologyButton(WebDriver driver) {
		  
		  myWait(3000);		  
		  myClick(driver, By.xpath("//*[@id='subHeader']/a[2]"), 2000); 
		  searchStr(driver, "Network Devices Topology");
	  }
	  
	  /**
	  *  Press the EMS Button
	  *  @param driver - given driver
	  */
	  public void pressEMSButton(WebDriver driver) {
		  
		  myWait(3000);		  
		  myClick(driver, By.xpath("/html/body/div[1]/header/a/img"), 7000);
		  for(String winHandle : driver.getWindowHandles()) {
		    	
		        driver.switchTo().window(winHandle);  
		  }
		  searchStr(driver, "USERNAME");
		  searchStr(driver, "PASSWORD");
		  searchStr(driver, "Log In");  
	  } 
	  
	  /**
	  *  Delete all files in directory by given prefix
	  *  @param dir    - given directory path
	  *  @param prefix - given prefix
	  */
	  public void deleteFilesByPrefix(String dir, String prefix) {
	    	
		myDebugPrinting("dir    - " + dir   ,  testVars.logerVars.MINOR);
		myDebugPrinting("prefix - " + prefix,  testVars.logerVars.MINOR);
    	File[] dirFiles = new File(dir).listFiles();
    	int filesNum = dirFiles.length;
    	for (int i = 0; i < filesNum; i++) {
    		
    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
    	    	
    			myDebugPrinting("Delete file - " + dirFiles[i].getName(),  testVars.logerVars.MINOR);
    	        new File(dir + "\\" + dirFiles[i].getName()).delete();
    		    myWait(1000);    
    	    }
    	}	
	    myWait(10000);    

	  }
	  
	  /**
	  *  read file method
	  *  @param  path    - given path for file to read
	  *  @return content - string of the readed file
	  */
	  String readFile(String path) {
		  
		    String content = null;
		    File file = new File(path);
		    FileReader reader = null;
		    try {
		    	
		        reader = new FileReader(file);
		        char[] chars = new char[(int) file.length()];
		        reader.read(chars);
		        content = new String(chars);
		        reader.close();
		    } catch (IOException e) {
		    } finally {
		    	
		        if(reader !=null) {
		        	
		        	try {
		        		
		        		reader.close();
		        	} catch (IOException e) {}
		        }
		    }
		    
			myWait(3000);
	    	myDebugPrinting("content - " + content, testVars.logerVars.DEBUG);
		    return content;
	  }
	  
	  /**
	  *  Verify xpath contains a string
	  *  @param driver   - given driver
	  *  @param elemName - given element xpath
	  *  @param strName  - given string for detect
	  */
	  public void verifyStrByXpathContains(WebDriver 	driver, String xpath, String strName) {
	  	  
		  String txt = driver.findElement(By.xpath(xpath)).getText();
		  if (txt.contains(strName)) {
			  
		    	myDebugPrinting(strName + " was detected", testVars.logerVars.DEBUG);
		  } else {
			  
			  myFail (strName + " was not detected !! (txt - " + txt + ")");
		  }
		  myWait(1000);
	  }
	    
	  /**
	  *  Print a given string to the console
	  *  @param str   - given string to print
	  *  @param level - given print level (MAJOR, NORMAL, MINOR, DEBUG)
	  */
	  public void myDebugPrinting(String str, int level) {
		  
		  String spaces = testVars.logerVars.debug[level];
		  logger.info(spaces + str);
	  }
	  
	  /**
	  * Return the number of lines in given file. Used to calculate number of imports.
	  * @param filePath - given file path
	  * @return lines   - number of lines on given file
	  * @throws IOException 
	  */
	  public int readFileLines(String filePath, Boolean isHeader) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		int lines = 0;
		while (reader.readLine() != null) {
			lines++;
		}
		reader.close();
		if (lines < 1) {
			myFail("The given file is empty -" + filePath);
		}
		myDebugPrinting("filePath - "  + filePath, testVars.logerVars.MINOR);
		if (isHeader) {
			
			myDebugPrinting("isHeader - TRUE, lines - " + (lines - 1)   , testVars.logerVars.MINOR);
			return (lines - 1);
		}
		
		return lines;
	  }
	 
	  /**
	  *  Print a given string to the console with default level of MAJOR
	  *  @param str - A given string to print
	  */
      public void myDebugPrinting(String str) {
			
		String spaces = testVars.logerVars.debug[testVars.logerVars.MAJOR];
		logger.info(spaces + str);
	  }
      
	  /**
	  *  Print a given error string and declares the test as a myFailure
	  *  @param str - A given error string
	  */
      public void myFail(String str) {
			
		logger.error(str);
		fail(str);
	  }
	 
	  /**
	  *  Verify string  method by xpath
	  *  @param driver   - given driver
	  *  @param elemName - given element name
	  *  @param strName  - given string for detect
	  */
	  public void verifyStrByXpath(WebDriver 	driver, String elemName, String strName) {
		    
		  markElemet(driver, driver.findElement(By.xpath(elemName)));   
		  String txt = driver.findElement(By.xpath(elemName)).getText();     
		  myAssertTrue("<" + strName + "> was not detected !! <" + txt + ">", txt.contains(strName));	     
		  myWait(1000);
	  }
	  
	  /**
	  *  Highlight given element
	  *  @param driver  - given driver
	  *  @param element - given element
	  */
	  public void markElemet(WebDriver 	driver, WebElement element) {
			
		// Mark element
	    try {
	    	
		    ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e1) {}
	   ((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", element);
	  }
	
	  /**
	  *  Sleep for a given time
	  *  @param sleepValue - given sleep factor
	  */
	  public void myWait(int sleepValue) {
			
	    try {
	    	
			TimeUnit.MILLISECONDS.sleep(sleepValue);		
		} catch (InterruptedException e1) {
		}	
	  }

	  /**
	  *  Add user manually
	  *  @param driver       - given driver
	  *  @param currUsername - username for the created user
	  *  @param userPass     - password for the created user
	  *  @param userDisName  - display name for the created user
	  *  @param tenant       - tenant for the created user
	  */
	  public void addUser(WebDriver 	driver, String currUsername, String userPass, String userDisName, String tenant) {
		
		// Enter the Manage-users menu & Add-User menu
		enterMenu(driver, "Setup_Manage_users", "New User");
		myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/a"), 3000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add User");
		
	    // Fill details
		myDebugPrinting("currUsername - " + currUsername, testVars.logerVars.MINOR);
		myDebugPrinting("userDisName - "  + userDisName , testVars.logerVars.MINOR);
		mySendKeys(driver, By.xpath("//*[@id='extension']")  , currUsername, 2000);
		mySendKeys(driver, By.xpath("//*[@id='secret']")     , userPass, 2000);
		mySendKeys(driver, By.xpath("//*[@id='displayname']"), userDisName, 2000);
	    
	    // Set Tenant
		myDebugPrinting("Set Tenant - " + tenant, testVars.logerVars.MINOR);
		Select displayOptions = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
		displayOptions.selectByVisibleText(tenant);	
		myWait(5000);
		
		// Submit
		myDebugPrinting("Submit", testVars.logerVars.MINOR);
	    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 6000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "User " + currUsername + " was successfully added.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Do you want to edit this user?");		
	    myClick(driver, By.xpath("//*[@id='modalContentId']/button[2]"), 10000);
		searchStr(driver, "New User");
	    
	    // Verify Create
		myDebugPrinting("Verify Create", testVars.logerVars.MINOR);
		searchUser(driver, currUsername);  
	  }
	
	  /**
	  *  Create a unique Id based on current time
	  *  @return - unique id based on current time 
	  */
	  public String getId() {
		
	    // set id
	    DateFormat dateFormat = new SimpleDateFormat("HH_mm_ssdd");
	    Date date     = new Date();
	    String id     = dateFormat.format(date);
	    id = id.replaceAll("_", "");
		myDebugPrinting("Id is:" + id, testVars.logerVars.MAJOR);
		
	    return id;
	  }
	  
	  /**
	  *  Wrap assertTrue with logger
	  *  @param errorStr  - error message for display at the logger
	  *  @param condition - condition for mark if the assert succeeded or not
	  */
	  public void myAssertTrue(String errorStr, Boolean condition) {
		  
		  if (!condition) {
			  myFail(errorStr);  
		  }
	  }
	  
	  /**
	  *  Wrap assertFalse with logger
	  *  @param errorStr  - error message for display at the logger
	  *  @param condition - condition for mark if the assert succeeded or not
	  */
	  public void myAssertFalse(String errorStr, Boolean condition) {
		  
		  if (condition) {
			  myFail(errorStr);  
		  }
	  }
	  
	  /**
	  *  Get Random number according to given limit
	  *  @param limit - upper limit for the random function
	  *  @return      - random number in range of [1 - <limit>]
	  */
	  public int getNum(int limit) {
		  
		  Random rand = new Random();
		  
		  return (rand.nextInt(limit) + 1);
	  }
	  
	  /**
	  *  Verify the create /non-create of POST users
	  *  @param driver       - given driver
	  *  @param userNamePrefix - Prefix of user-names (same name for the device)
	  *  @param dispNamePrefix - Prefix of display-names
	  *  @param isRegistsred - flag for identify if a registered user was created or not
	  *  @param usersNumber	 - users number
	  */
	  public void verifyPostUsersCreate(WebDriver driver, String userNamePrefix, String dispNamePrefix, boolean isRegistered, int usersNumber) {
		  
		  String username, dispName;
		  enterMenu(driver, "Setup_Manage_users", "New User");
		  for (int idx = 1; idx <= usersNumber; ++idx) {
			  
				// Search user
			  	username = userNamePrefix  + "_" + idx;
			  	dispName = dispNamePrefix  + "_" + idx; 	
				myDebugPrinting("Search user <" + username + "> with prefix <" + dispName + ">", testVars.logerVars.NORMAL);
				mySendKeys(driver, By.xpath("//*[@id='searchtext']"), username, 2000);
				myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/div[2]/button/span[2]"), 2000);
				myClick(driver, By.xpath("//*[@id='all_search']/li[1]/a"), 2000);
				driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
				myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 10000);
			    if (isRegistered) {
			    		    	
			    	if (!username.contains("location_2049")) {
			    	
			    		searchStr(driver, username);
			    		searchStr(driver, dispName);
			    	} else {
			    		
			    		searchStr(driver, "No users found");
			    	}
			    } else {
			    	
				   verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/font", "No users found");
			    }
		  }
		  	
		  enterMenu(driver, "Monitor_device_status", "Devices Status");
		  for (int idx = 1; idx <= usersNumber; ++idx) {
			  
			  // Search device	  	
			  username = userNamePrefix  + "_" + idx;
			  dispName = dispNamePrefix  + "_" + idx; 	
			  myDebugPrinting("Search user <" + username + "> with prefix <" + dispName + ">", testVars.logerVars.NORMAL);
		
			  // Verify that the device was also created		    
			  myDebugPrinting("Verify that the device was also created", testVars.logerVars.MINOR);			    
			  mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + dispName.trim(), 5000);			    
			  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	     
			  myWait(30000);		   
			  if (isRegistered) { 
			    	    	
				  if (!dispName.contains("location_2049")) {
					  

					  myAssertTrue("No users were found for <" + dispName + "> !!", !driver.findElement(By.tagName("body")).getText().contains("There are no devices that fit this search criteria"));
					  searchStr(driver, dispName.trim()); 			    
					  String txt = driver.findElement(By.tagName("body")).getText();  
					  myAssertTrue("Approve button is displayed !! \ntxt - " + txt, !txt.contains("Approve"));	
			    	
				  } else {
			    				    		
					  searchStr(driver, "There are no devices that fit this search criteria");	    	
				  }	    	  
			  } else {
			    			    	
				  String txt = driver.findElement(By.tagName("body")).getText();			    
				  myAssertTrue("Approve button is not displayed !!\ntxt - " + txt, txt.contains("Approve"));
			    
			  } 
		  }
	  }
	
	  /**
	  *  Verify the create /non-create of POST user
	  *  @param driver       - given driver
	  *  @param username     - username name (same name for the device)
	  *  @param dispName     - display name
	  *  @param isRegistsred - flag for identify if a registered user was created or not
	  */
	  public void verifyPostUserCreate(WebDriver driver, String username, String dispName, boolean isRegistered) {
				  	  
		// Search user 
		myDebugPrinting("Search user " + username, testVars.logerVars.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='searchtext']"), username, 2000);
		myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/div[2]/button/span[2]"), 2000);
		myClick(driver, By.xpath("//*[@id='all_search']/li[1]/a"), 2000);
		driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
		myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 10000);
	    if (isRegistered) {
	    		    	
	    	if (!username.contains("location_2049")) {
	    	
	    		searchStr(driver, username);
	    		searchStr(driver, dispName);
	    	} else {
	    		
	    		searchStr(driver, "No users found");
	    	}
	    } else {
	    	
		   verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/font", "No users found");
	    }

		// Verify that the device was also created
	    myDebugPrinting("Verify that the device was also created", testVars.logerVars.MINOR);
		enterMenu(driver, "Monitor_device_status", "Devices Status");
	    mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + dispName.trim(), 5000);
	    driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
	    myWait(30000);
	    if (isRegistered) { 
	    	
	    	if (!dispName.contains("location_2049")) {
	    		
	    		if (driver.findElement(By.tagName("body")).getText().contains("There are no devices that fit this search criteria")) {
	    			
	    			myFail("No users were found for <" + dispName + "> !!");
	    		}
			    verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[8]", dispName.trim()); 
			    String txt = driver.findElement(By.tagName("body")).getText();
			    myAssertTrue("Approve button is displayed !! \ntxt - " + txt, !txt.contains("Approve"));	
	    	} else {
	    		
	    		searchStr(driver, "There are no devices that fit this search criteria");
	    	}	    	
	    } else {
	    	
	    	String txt = driver.findElement(By.tagName("body")).getText();
		    myAssertTrue("Approve button is not displayed !!\ntxt - " + txt, txt.contains("Approve"));
	    }
	  }
	
	  /**
	  *  Create device which added to user
	  *  @param driver       - given driver
	  *  @param username     - user name
	  *  @param deviceName   - device name
	  *  @param phoneType    - phone type
	  *  @param macAddName   - MAC address name
	  *  @param firmWareType - firmware name
	  */
	  public void addDevice(WebDriver driver, String username, String deviceName, String phoneType, String macAddName, String firmWareType) {
		  
		myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 5000); 
		myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[9]/a[1]"), 5000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/form/div/div[2]/div[1]/h3", "Add new device to " + username.toLowerCase());
		
		// Set device name
		myDebugPrinting("Set device name " + deviceName, testVars.logerVars.MINOR);
	    driver.findElement(By.xpath("//*[@id='display_name']")).clear();
	    driver.findElement(By.xpath("//*[@id='display_name']")).sendKeys(deviceName);
	    
	    // Set IP phone type	
		myDebugPrinting("Set IP phone type " + phoneType, testVars.logerVars.MINOR);
		Select displayOptions = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/form/div/div[2]/div[2]/div/table/tbody/tr[3]/td[2]/select")));
		displayOptions.selectByVisibleText(phoneType);
		myWait(5000);
		
	    // Set MAC address
		myDebugPrinting("Set MAC address " + macAddName, testVars.logerVars.MINOR);
	    driver.findElement(By.xpath("//*[@id='macaddress_val']")).clear();
	    driver.findElement(By.xpath("//*[@id='macaddress_val']")).sendKeys(macAddName);
	    
	    // Set firmware
		myDebugPrinting("Set firmware " + firmWareType, testVars.logerVars.MINOR);
		Select firmwareOptions = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/form/div/div[2]/div[2]/div/table/tbody/tr[5]/td[2]/select")));
		firmwareOptions.selectByVisibleText(firmWareType);
		myWait(5000);
	    		
		// Submit & verify create
		myDebugPrinting("Submit & verify create", testVars.logerVars.MINOR);	
	    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/form/div/div[2]/div[3]/button[2]")   , 7000);
	    myClick(driver, By.xpath("/html/body/div[2]/div/button[2]")   , 7000);
	    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/form/div/div[2]/div[3]/button[1]")   , 7000);
	    username = username.toLowerCase();
		searchUser(driver, username);  

		// Verify create
		searchUser(driver, username);  
	    myClick(driver, By.xpath("//*[@id='" + username + "tree']")			, 2000);
	    searchStr(driver, "Devices Display name: " + deviceName);  
		searchStr(driver, "Template: " 			   + phoneType);  
		searchStr(driver, "MAC Address: " 		   + macAddName);
		searchStr(driver, "Firmware: " 			   + firmWareType);
	  }
	  
	  /**
	  *  Search user in the Manage multiple users menu by given username
	  *  @param driver     - given driver
	  *  @param deviceName - user name
	  */
	  public void searchUser(WebDriver driver, String username) {
		
			myDebugPrinting("username - " + username   ,  testVars.logerVars.MINOR);
		    driver.findElement(By.xpath("//*[@id='searchtext']")).clear();
		    myWait(1000);
		    driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(username);
		    driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
		    myWait(5000);
		    myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button"), 10000); 
		    searchStr(driver, username); 
	  }
	  
	  /**
	  *  Find files in a given directory by a given prefix
	  *  @param dir    - given directory path
	  *  @param prefix - given prefix
	  *  @return       - TRUE if files were found
	  */
	  public boolean findFilesByGivenPrefix(String dir, String prefix) {
	    	
			myDebugPrinting("dir    - " + dir   ,  testVars.logerVars.MINOR);
			myDebugPrinting("prefix - " + prefix,  testVars.logerVars.MINOR);
	    	File[] dirFiles = new File(dir).listFiles();
	    	int filesNum = dirFiles.length;
	    	for (int i = 0; i < filesNum; i++) {
	    				
    	    	myDebugPrinting(dirFiles[i].getName(),  testVars.logerVars.DEBUG);
	    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
	    			
	    	    	if (dirFiles[i].getName().contains("crdownload")) {
	    	    		
		    	    	myDebugPrinting("crdownload suffix is detected. Waiting for another 60 seconds.",  testVars.logerVars.MINOR);
	    	    		myWait(60000);
	    	    	}
	    	    	myDebugPrinting("Find a file ! (" + dirFiles[i].getName() + ")",  testVars.logerVars.MINOR);
	    	        return true;
	    	    }
	    	}
	    	
	    	return false;
	  }
	
	  /**
	  *  Create random MAC address
	  *  @return - MAC address based on AC prefix (i.e. 00908f) + random 6 characters long hex
	  */
	  public String getMacAddress() {
		
		char[] chars = "abcdef1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random2 = new Random();
		for (int i = 0; i < 6; i++) {
			
		    char c = chars[random2.nextInt(chars.length)];
		    sb.append(c);
		}
		
		return testVars.getAcMacPrefix() + sb.toString();	
	  }
	
	  /**
	  *  Get last file save on given path
	  *  @param  dirPath - directory path
	  *  @return  		 - last modified file-name or NULL
	  */
	  public File getLatestFilefromDir(String dirPath) {
		
	    File dir = new File(dirPath);
	    File[] files = dir.listFiles();
	    if (files == null || files.length == 0) {
	    	
	        return null;
	    }
	    File lastModifiedFile = files[0];
	    for (int i = 1; i < files.length; i++) {
	    	
	       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
	    	   
	           lastModifiedFile = files[i];
	       }
	    }
	    
	    return lastModifiedFile;
	  }

	  /**
	   *  Wait till the given string not displayed on the screen
	   *  @param driver  - given driver
	   *  @param string  - given string that indicate if we should stop the loop
	   */
	  public void waitTillString(WebDriver driver, String string) {
		
		String bodyText = "";
		int idx         = 0;
		int gapTime     = 5;
		int gapDelay    = gapTime  * 1000;
		while (true) {
			
	      bodyText = driver.findElement(By.tagName("body")).getText();
	      if (!bodyText.contains(string) && !bodyText.contains("Performing")) {	 
	    	  
	    	  myAssertTrue("Users are already exist ..", !bodyText.contains("Already exists"));
	    	  break;    	  
	      } else { 
	    	  
	    	  idx += gapTime;
	    	  myDebugPrinting(string + " is still detected after " + idx + " seconds", testVars.logerVars.MINOR);
	    	  myWait(gapDelay);
	      }
		}
	    myWait(2000);		
	  }
	
	  /**
	  *  Upload file with given path displayed on the screen
	  *  @param driver  		  - given driver
	  *  @param path    		  - path for a file for upload
	  *  @param uploadFieldXpath  - xpath for upload field
	  *  @param uploadButtonXpath - xpath for upload button
	  */
	  public void uploadFile(WebDriver driver, String path, String uploadFieldXpath, String uploadButtonXpath) {
	      		  
		myDebugPrinting("path -   " + path, testVars.logerVars.MINOR);
		mySendKeys(driver, By.xpath(uploadFieldXpath), path  , 2000);
		myClick(driver   , By.xpath(uploadButtonXpath), 250000);
		if (driver.findElement(By.tagName("body")).getText().contains("Failed to import from selected file.")) {
			
			myFail("Upload configuration-file was failed !!");
		}
	  }
	  
	  /**
	  *  Upload file with given path with confirm message-box
	  *  @param driver  		   - given driver
	  *  @param path    		   - path for a file for upload
	  *  @param uploadFieldXpath   - xpath for upload field
	  *  @param uploadButtonXpath  - xpath for upload button
	  *  @param confirmMessageStrs - array of confirm-box strings
	  */
	  public void uploadFile(WebDriver driver, String path, String uploadFieldXpath, String uploadButtonXpath, String[] confirmMessageStrs) {
	      		  
		myDebugPrinting("path -   " + path, testVars.logerVars.MINOR);
		mySendKeys(driver, By.xpath(uploadFieldXpath), path  , 2000);
		myClick(driver   , By.xpath(uploadButtonXpath)		 , 5000);
		if (driver.findElement(By.tagName("body")).getText().contains("Failed to import from selected file.")) {
			
			myFail("Upload configuration-file was failed !!");
		}
		
		if (!confirmMessageStrs[0].isEmpty()) {
		
	    	verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , confirmMessageStrs[0]);	
	    	verifyStrByXpath(driver, "//*[@id='modalContentId']", confirmMessageStrs[1]);	
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 20000);	
		}
	  }
	
	  /**
	  *  Select multiple users in the Manage multiple users menu according to a given prefix
	  *  @param driver    - given driver
	  *  @param prefix    - prefix for search the created users
	  *  @param expNumber - the expected number of users
	  */
	  public void selectMultipleUsers(WebDriver driver, String prefix, String expNumber) {
		    
		mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[2]/div/table/tbody/tr[2]/td/div/div/input"), prefix, 10000);
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[2]/div/table/tbody/tr[2]/td/div/div/a")			   , 10000);
		if (Integer.parseInt(expNumber) == 0) {
	    	
	      	myDebugPrinting("verify delete", testVars.logerVars.NORMAL);
	    	verifyStrByXpath(driver, "//*[@id='modalContentId']", "No device " + prefix + " found.");	
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
	    	return;
	    }
		myClick(driver, By.xpath("//*[@id='maintable']/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/table/tbody/tr[4]/td/a"), 7000);
	    if (Integer.parseInt(expNumber) > 500) {
	    	
			myClick(driver, By.xpath("//*[@id='left_total_id']/a[1]"), 2000);		    
			myClick(driver, By.xpath("//*[@id='maintable']/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[2]/table/tbody/tr[4]/td/a"), 2000);
			verifyStrByXpathContains(driver, "//*[@id='left_total_id']", "of " + expNumber + " users");	    	
	    } else if (Integer.parseInt(expNumber) == -1) {
	    	
	      	myDebugPrinting("users number is unknown ..", testVars.logerVars.MINOR);
	    } else {
	    	
	    	verifyStrByXpath(driver, "//*[@id='left_total_id']", "Showing 1 to " + expNumber + " of " + expNumber);
	    }
	  }
	  
	  /**
	  *  Verify that all the actions were made
	  *  @param bodyText - text of the page
	  *  @param arg1	 - users prefix
	  *  @param arg2     - users number
	  *  @param arg3     - user initial prefix
	  *  @param arg4     - action suffix
	  */
	  private void verifyAction(String bodyText, String arg1, int arg2, int arg3, String arg4) throws IOException {

	    int lim     = arg2 + arg3;
	    String temp = "";
	    Boolean isEqSuffix = false;
	    Boolean isMac      = false;

	    if (arg4.equals(arg1)) {
	    	
	    	myDebugPrinting("isEqSuffix - true", testVars.logerVars.MINOR);
	    	isEqSuffix = true;
	    }
	    if (arg4.equals("isMac")) {
	    	
	    	myDebugPrinting("isMac - true", testVars.logerVars.MINOR);
	    	isMac = true;
	    }
	    temp = arg4;
		for (int i = arg3; i < lim; i++) {
			
			if (isEqSuffix) {
				
				temp = arg1 + i;
			}
			if (isMac) {
				
				temp = readFile("mac_" + i + ".txt");
				myDebugPrinting("temp - " + temp, testVars.logerVars.MINOR);
			}
			if (arg2 == 1) {	
				
				if (!bodyText.contains(arg1 + " " + temp)) {
					
					myFail("Error, the string <" +  arg1 + " " + temp + "> was not recognized !!");
				} else {
					
					return;
				}
			}
			if (!bodyText.contains(arg1 + i + " " + temp)) {
				
				myDebugPrinting("The search prefix is:" + arg1 + i + " " + temp, testVars.logerVars.MINOR);
				myDebugPrinting("bodyText - \n" + bodyText, testVars.logerVars.MINOR);
				myFail("Error, the string <" +  arg1 + i + " " + temp + "> was not recognized !!");
			}
		}  
	  }

	  /**
	  *  Enter a menu
	  *  @param driver       - given driver for make all tasks
	  *  @param menuName 	 - given menu name for the paths function
	  *  @param verifyHeader - string for verify that enter the menu succeeded
	  */
	  public void enterMenu(WebDriver 	driver, String menuName, String verifyHeader) {
		  
		  myDebugPrinting("enterMenu  - " +  menuName, testVars.logerVars.NORMAL);
		  String paths[] = testMenuPaths.getPaths(menuName);
		  int length = paths.length;
		  for (int i = 0; i < length; ++i) {
		  
			  if (paths[i].isEmpty()) {
				  
				  break;
			  }
			  myDebugPrinting("paths[" + i + "] - " +  paths[i], testVars.logerVars.MINOR);
			  myWait(3000);
			  markElemet(driver, driver.findElement(By.xpath(paths[i])));
			  myWait(10000);
			  myDebugPrinting("markElemet", testVars.logerVars.MINOR);
		      driver.findElement(By.xpath(paths[i])).click();
			  myWait(10000);
		  }
		  String title = driver.findElement(By.tagName("body")).getText();
		  driver.switchTo().defaultContent();
		  if (!verifyHeader.isEmpty()) {
			  
			  Assert.assertTrue("Title was not found (" + title + ")", title.contains(verifyHeader));
		  }
		  myDebugPrinting("enterMenu  - " +  menuName + " ended successfully !!", testVars.logerVars.NORMAL);
      }

	  /**
	  * Set a driver according to a given browser name
	  * @param  usedBrowser - given browser name (Chrome, FireFox or IE)
	  * @return driver      - driver object (Failure if usedBrowser is not a valid browser name)
	  */
	  public WebDriver defineUsedBrowser(String usedBrowser) {
			
		if 		  (usedBrowser.equals(testVars.CHROME)) {
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			return new ChromeDriver(options);
			
		} else if (usedBrowser.equals(testVars.FF))     {
			
			myDebugPrinting(testVars.getGeckoPath());
			System.setProperty("webdriver.gecko.driver", testVars.getGeckoPath());
			System.setProperty("webdriver.firefox.marionette", "false");
			return new FirefoxDriver();
			
		} else if (usedBrowser.equals(testVars.IE))     {    	
			
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("requireWindowFocus", true);   	
			return new InternetExplorerDriver(capabilities);	
		
		}  else   {
			
			myFail ("The browser type is invalid - " + usedBrowser);
		}
		
		return null;
	  }
	  
	  /**
	  * Wait to a file from the au3 script that marks the end of users create
	  * @param filePath - given full path to file that marks a successful create
	  * @param maxWait  - max waiting time
	  */
	  public void waitForPostUsersCreate(String filePath, int maxWait) {
		  
	    File f = new File(filePath);
	    int i = 0;
	    while (true) {  
	    	
	    	if (i > maxWait) {
	    		
	    		myFail("maxWait was over .. (" + maxWait + ")");
	    	} else {
	    		
	    		i++;
			    if(f.exists() && !f.isDirectory() && i > 20) {
			    	
			    	break;
			    } else {
			    	
			    	myWait(1100);
			    	myDebugPrinting(i + ".waiting .. (" + maxWait + ")", testVars.logerVars.MINOR);
			    }
	    	}	
	    }
    	myDebugPrinting("Waiting was over ..", testVars.logerVars.MINOR);
	  }  
	  
	  /**
	  *  Perform an action to multiple selected users in the Manage multiple users menu
	  *  @param driver      - given driver
	  *  @param map         - abstract data for the function
	 *   @throws IOException 
	  */
	  public void setMultipleUsersAction(WebDriver driver, Map<String, String> map) throws IOException {
		
			String action     = map.get("action");
		    String usrsPrefix = map.get("usersPrefix");
		    String acSuffix   = map.get("srcUsername");
		    int    usrsNumber = Integer.parseInt(map.get("usersNumber"));
		    int    usrsInIdx  = Integer.parseInt(map.get("startIdx"));

			// Set action
			Select acionsList = new Select(driver.findElement(By.xpath("//*[@id='action']")));
			acionsList.selectByVisibleText(action);
			myWait(5000);
			
			// Perform action
			switch (action) {
			
				case "Delete Users":				
					myDebugPrinting("Enter Delete Users block", testVars.logerVars.NORMAL);	 
					new Select(driver.findElement(By.xpath("//*[@id='maintable']/tbody/tr[4]/td/div/select[1]"))).selectByValue("1");
					myWait(5000);
					new Select(driver.findElement(By.xpath("//*[@id='maintable']/tbody/tr[4]/td/div/select[2]"))).selectByValue("2");
				    myWait(5000);
				    myClick(driver, By.xpath("//*[@id='deleteUsersTR']/td/div/a/span"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the selected users?");					
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
				    
				    // test 34 - Stop and continue test
				    if (usrsPrefix.contains("stopAndContUser")) {
				    	
						myDebugPrinting("stopAndContUser - TRUE (stop action  at middle and than continue)", testVars.logerVars.NORMAL);
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[2]"), 20000);
						myDebugPrinting("STOP button was pressed ..", testVars.logerVars.MINOR);
						String bodyText = driver.findElement(By.tagName("body")).getText();
					    myAssertTrue("Delete action continued although Stop was pressed /n bodyText - " + bodyText, bodyText.contains("Waiting"));
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[3]"), 3000);
					    myDebugPrinting("Continue button was pressed ..", testVars.logerVars.MINOR);
				    	waitTillString(driver, "Waiting");
				    }
				    // test 33 - Stop and not continue test
				    else if (usrsPrefix.contains("stopDelUser") && usrsNumber != -1) {
				    	
						myDebugPrinting("stopAndContUser - TRUE (stop action  at middle and than continue)", testVars.logerVars.NORMAL);
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[2]"), 20000);
						myDebugPrinting("STOP button was pressed ..", testVars.logerVars.MINOR);
					    String bodyText = driver.findElement(By.tagName("body")).getText();
					    myAssertTrue("Delete action continued although Stop was pressed /n bodyText - " + bodyText, bodyText.contains("Waiting"));						
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[1]"), 5000);
					    return;	
				    } else {
				    	
				    	waitTillString(driver, "Waiting");
				    }
				    if (usrsPrefix.contains("lang")) {
				    	
				    	return;
				    } else {
				    	
				    	if (map.containsKey("skipVerifyDelete") && map.get("skipVerifyDelete").equals("true")) {
							
				    		myDebugPrinting("skipVerifyDelete - TRUE", testVars.logerVars.MINOR);
				    		break;
				    	} else {
							
				    		myDebugPrinting("skipVerifyDelete - FALSE", testVars.logerVars.MINOR);
				    		verifyAction(driver.findElement(By.tagName("body")).getText(), usrsPrefix, usrsNumber, usrsInIdx, acSuffix);
				    	}
				    }
				    break;
					
				case "Reset Users Passwords":
					String subAction   = map.get("subAction");
					String password    = map.get("password");
					if (!subAction.isEmpty() && subAction.equals("changePassword")) {
						
						myDebugPrinting("Enter Change Users Passwords block", testVars.logerVars.NORMAL);	
						myClick(driver, By.xpath("//*[@id='resetPassword2All']"), 3000);
						myDebugPrinting("The password we want to modify is - " + password, testVars.logerVars.MINOR);	
						mySendKeys(driver, By.xpath("//*[@id='resetToPassword']"), password, 3000);
					} else {
						
						myDebugPrinting("Enter Reset Users Passwords block", testVars.logerVars.NORMAL);
					}
					myClick(driver, By.xpath("//*[@id='resetPasswordsTR']/td/div[1]/table/tbody/tr/td[4]/a"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the password to selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Set Users Tenant":
					String region     = map.get("region");
					myDebugPrinting("Enter Set Users region block", testVars.logerVars.NORMAL);
					myDebugPrinting("The wanted region is - " + region, testVars.logerVars.MINOR);		
					Select regionsList = new Select(driver.findElement(By.xpath("//*[@id='branch']")));				
					regionsList.selectByVisibleText(region);
					myWait(5000);
					myClick(driver, By.xpath("//*[@id='resetTenantTR']/td/div/a[2]/span"), 7000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the tenant to selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Update Configuration Files":
					myDebugPrinting("Enter Update Configuration Files block", testVars.logerVars.NORMAL); 
					myClick(driver, By.xpath("//*[@id='updateConfigFilesTR']/td/div/a"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: update configuration command will work only on supported IP Phones.\nAre you sure you want to update the selected IP Phones files?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Generate IP Phones Configuration Files":
					myDebugPrinting("Enter Generate IP Phones Configuration Files block", testVars.logerVars.NORMAL);				    
					myClick(driver, By.xpath("//*[@id='setIpPhonesTR']/td/div/div/a/span"), 3000);  
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "The configuration files will be generate to the location define in the template (destinationDir).\nDo you want to continue?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
			    	waitTillString(driver, "Waiting");
					break;				
					
				case "Send Message":
					myDebugPrinting("Enter Send Message block", testVars.logerVars.NORMAL);
					String message = map.get("message");
				    mySendKeys(driver, By.xpath("//*[@id='msgText']"), message, 2000);			
				    myClick(driver, By.xpath("//*[@id='sendMessageTR']/td/div/a"), 3000);	
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to send the message to the selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Delete User configuration":
					myDebugPrinting("Enter Delete User configuration block", testVars.logerVars.NORMAL);
				    myClick(driver, By.xpath("//*[@id='deletePersonalInfoTR']/td[1]/div/a"), 3000);				    
					verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete User configuration");
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete User configuration to selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
					break;
					
				case "User configuration":
					myDebugPrinting("Enter User configuration block", testVars.logerVars.NORMAL);
					String confKey    = map.get("confKey");
					
					// Features block
					if (confKey.contains("features")) {
						
						myDebugPrinting("Enter features configuration features block", testVars.logerVars.NORMAL);
						if   	  (map.containsKey("daylight"))   {
							
						    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
							myDebugPrinting("Add daylight configuration values", testVars.logerVars.NORMAL);
						    myClick(driver, By.xpath("//*[@id='daylight']"), 10000);
							verifyStrByXpath(driver, "//*[@id='modalTitleId']", "Daylight Savings Time");
						    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000); 
						    
						    // Verify that all daylight values were added correctly
							myDebugPrinting("Verify that all daylight values were added correctly", testVars.logerVars.MINOR);
						    String dayLightValues[] = {"system_daylight_saving_activate ENABLE"                ,
									  				   "system_daylight_saving_end_date_day 1" 		  	       ,
									  				   "system_daylight_saving_end_date_day_of_week SUNDAY"    ,
									  				   "system_daylight_saving_end_date_hour 2"		           ,
									  				   "system_daylight_saving_end_date_month 1"	           ,
									  				   "system_daylight_saving_start_date_week 1"		       ,
									  				   "system_daylight_saving_end_date_week 1"		       	   ,
									  				   "system_daylight_saving_mode FIXED"				       ,
									  				   "system_daylight_saving_offset 60"       		   	   ,
									  				   "system_daylight_saving_start_date_day 1"               ,
									  				   "system_daylight_saving_start_date_day_of_week SUNDAY"  ,									  					
									  				   "system_daylight_saving_start_date_hour 2"	           ,
									  				   "system_daylight_saving_start_date_month 1"	           ,
					  					 			   "system_daylight_saving_start_date_week 1"};
			 
							  int dayLightValuesNumber = dayLightValues.length;
							  for (int i = 0; i < dayLightValuesNumber; ++i) {
				  
								  myDebugPrinting(i + ". The searched value is: " + dayLightValues[i], testVars.logerVars.MINOR);
								  searchStr(driver, dayLightValues[i]);	  
								  myWait(3000); 
							  }	  
							  
						} else if (map.containsKey("telnet"))     {
							
							myDebugPrinting("Add Telnet-access value", testVars.logerVars.NORMAL);
							for (int i = 0; i < 2; ++i) {
								
							    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
							    myClick(driver, By.xpath("//*[@id='telnet']")											, 7000);	
								myDebugPrinting("Create configuration value with value - " + i, testVars.logerVars.MINOR);
								verifyStrByXpath(driver, "//*[@id='modalTitleId']"								   , "Activate Telnet access");
								verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[1]/td/div/label", "Activate Telnet access");
								if (i == 1) {
									
									myClick(driver, By.xpath("//*[@id='management_telnet_enabled']"), 3000);
								}
							    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")		, 3000);  
							    searchStr(driver, "management_telnet_enabled " + i);	
							}
							
						} else if (map.containsKey("pinLock"))  {	
							
							myDebugPrinting("Add PIN-lock value", testVars.logerVars.NORMAL);
							for (int i = 0; i < 2; ++i) {
								
							    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
							    myClick(driver, By.xpath("//*[@id='pinlock']")											, 7000);	
								myDebugPrinting("Create configuration value with value - " + i, testVars.logerVars.MINOR);
								verifyStrByXpath(driver, "//*[@id='modalTitleId']"								   , "Pin Lock");
								verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[1]/td/div/label", "Enable Pin Lock");
								if (i == 1) {
									
									myClick(driver, By.xpath("//*[@id='system_pin_lock_enabled']"), 3000);
								}
							    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")		, 3000);  
							    searchStr(driver, "system_pin_lock_enabled " + i);	
							}
							
						} else if (map.containsKey("capProfile")) {
							
							myDebugPrinting("Set CAP profile", testVars.logerVars.NORMAL);
						    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
						    myClick(driver, By.xpath("//*[@id='CAP Profile']")									, 7000);				
							verifyStrByXpath(driver, "//*[@id='modalTitleId']"								   , "CAP Profile");

							// Check that default configuration (three first check-boxes are checked) is set
							myDebugPrinting("Check that default configuration (three first check-boxes are checked) is set", testVars.logerVars.MINOR);
							myAssertTrue("Checkbox is not checked at default !!", driver.findElement(By.xpath("//*[@id='voip_common_area_is_cap_device']")).getAttribute("checked") 			   != null);
							myAssertTrue("Checkbox is not checked at default !!", driver.findElement(By.xpath("//*[@id='lync_corporate_directory_enabled']")).getAttribute("checked")			   != null);
							myAssertTrue("Checkbox is not checked at default !!", driver.findElement(By.xpath("//*[@id='lync_userSetting_prevent_user_sign_out']")).getAttribute("checked")        != null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='lync_calendar_enabled']")).getAttribute("checked") 				  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='lync_VoiceMail_enabled']")).getAttribute("checked") 			  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='lync_BToE_enable']")).getAttribute("checked") 					  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='voip_line_0_call_forward_enabled']")).getAttribute("checked") 	  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='voip_services_do_not_disturb_enabled']")).getAttribute("checked") 			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='system_pin_lock_enabled']")).getAttribute("checked")			  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='system_enable_key_configuration']")).getAttribute("checked") 	  			   == null);

							// Check all available check-boxes
							myDebugPrinting("Check all available check-boxes", testVars.logerVars.MINOR);
						    myClick(driver, By.xpath("//*[@id='lync_calendar_enabled']")			   , 2000);				
						    myClick(driver, By.xpath("//*[@id='lync_VoiceMail_enabled']")			   , 2000);				
						    myClick(driver, By.xpath("//*[@id='lync_BToE_enable']")					   , 2000);				
						    myClick(driver, By.xpath("//*[@id='voip_line_0_call_forward_enabled']")    , 2000);				
						    myClick(driver, By.xpath("//*[@id='voip_services_do_not_disturb_enabled']"), 2000);				
						    myClick(driver, By.xpath("//*[@id='system_pin_lock_enabled']")			   , 2000);				
						    myClick(driver, By.xpath("//*[@id='system_enable_key_configuration']")	   , 2000);				
						    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")				   , 10000);  
						    myWait(10000);
						    
							// Check that all values were added
							myDebugPrinting("Check that all values were added", testVars.logerVars.MINOR);
						    searchStr(driver, "voip_common_area_is_cap_device 1");
						    searchStr(driver, "lync_corporate_directory_enabled 1");
						    searchStr(driver, "lync_userSetting_prevent_user_sign_out 1");
						    searchStr(driver, "lync_calendar_enabled 1");
						    searchStr(driver, "lync_VoiceMail_enabled 1");
						    searchStr(driver, "lync_BToE_enable 1");
						    searchStr(driver, "voip_line_0_call_forward_enabled 1");
						    searchStr(driver, "voip_services_do_not_disturb_enabled 1");
						    searchStr(driver, "system_pin_lock_enabled 1");
						    searchStr(driver, "system_enable_key_configuration 1"); 
						    
						} else if (map.containsKey("deleteAllConfValues"))  {	
						
							myDebugPrinting("Delete all configuration values", testVars.logerVars.NORMAL);	    
							myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[2]/button")		 , 3000);
							myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[2]/ul/li[1]/a/span"), 7000);
							verifyStrByXpath(driver, "//*[@id='modalTitleId']"	, "Delete configuration settings");
							verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
						    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);  
					
						    // Verify delete
							myDebugPrinting("Verify delete", testVars.logerVars.MINOR);		  
							String bodyText     = driver.findElement(By.tagName("body")).getText();
							myAssertTrue("Value was still detected !!", !bodyText.contains("voip_common_area_is_cap_device"));
							myAssertTrue("Value was still detected !!", !bodyText.contains("system_enable_key_configuration"));
						}
										
						return;
					}		
					
					// Regular configuration key
					String confVal    = map.get("confValue");
					String dispPrefix = map.get("dispPrefix");
					
					// Add configuration key
					mySendKeys(driver, By.xpath("//*[@id='ini_name']"), confKey, 4000);	
				    mySendKeys(driver, By.xpath("//*[@id='ini_value']"),confVal, 4000);					    				    
				    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[3]/a/span"), 3000);    
				    
				    // Verify that configuration key was added
					myDebugPrinting("Verify that configuration key was added", testVars.logerVars.MINOR); 
				    searchStr(driver, confKey);
				    searchStr(driver, confVal);
				    
				    // Save configuration key
					myDebugPrinting("Save configuration key", testVars.logerVars.MINOR); 
				    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[4]/a"), 3000);    
					verifyStrByXpath(driver, "//*[@id='modalTitleId']", "Save Configuration");
					verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[1]", "Name");
					verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[2]", "Result");
					for (int i = 1; i < usrsNumber; ++i) {
						
						verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[1]", dispPrefix + "_" + i + "@" + testVars.getDomain());
						verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[2]", "User configuration was saved successfully.");		
					}
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000); 
					break;
					
				case "Restart Devices":
					myDebugPrinting("Enter Restart Devices block", testVars.logerVars.NORMAL);
					String resetMode = map.get("resMode");
					Select resetList = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select")));
					resetList.selectByVisibleText(resetMode);				
				    myWait(5000);
					myDebugPrinting("Reset mode is - " + resetMode, testVars.logerVars.MINOR);
					if (resetMode.equals("Scheduled")) {
						
						String scDelay = map.get("scMinutes");
						Select resetList2 = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/span/select")));			
						resetList2.selectByVisibleText(scDelay);
						myWait(5000);
					}
					myClick(driver, By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/a"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: restart command will work only on supported IP Phones.\nAre you sure you want to restart the selected IP Phones?");		
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
					waitTillString(driver, "Waiting");
					break;
					
				default:
					myFail("Action is not recognized !!");	
					break;
				}	
	  }
	  
	  /**
	  *  Perform an action to multiple selected devices in the Manage multiple devices menu
	  *  @param driver        - given driver
	  *  @param map           - abstract data for the function
	  *   @throws IOException 
	  */
	  public void setMultipleDevicesAction(WebDriver driver, Map<String, String> map) throws IOException {
		  
		  // Set action
		  String action     = map.get("action");
		  Select devAction = new Select(driver.findElement(By.xpath("//*[@id='action']")));	    
		  devAction.selectByVisibleText(action);
		  myWait(5000);
	
		  // Perform action
		  switch (action) {	
			
				case "Change Language":
					myDebugPrinting("Enter Change Language block", testVars.logerVars.NORMAL);
					String language = map.get("language");
				    Select langs = new Select(driver.findElement(By.xpath("//*[@id='deviceLanguage']")));	    
				    langs.selectByVisibleText(language);
					myWait(5000);
					myClick(driver, By.xpath("//*[@id='changeLanguageTR']/td/div[1]/a[2]"), 3000);	
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's language?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Send Message":
					myDebugPrinting("Enter Send Message block", testVars.logerVars.NORMAL);
					String message = map.get("message");
					mySendKeys(driver, By.xpath("//*[@id='sendMessageTR']/td/div/input"), message, 2000);
					myClick(driver, By.xpath("//*[@id='sendMessageTR']/td/div/a"), 2000);	
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to send message to the selected devices?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Change Template":
					myDebugPrinting("Enter Change Template block", testVars.logerVars.NORMAL);
					String phoneType = map.get("phoneType");
				    Select templates = new Select(driver.findElement(By.xpath("//*[@id='ipptype']")));	    
				    templates.selectByVisibleText(phoneType);
					myWait(5000);
					myClick(driver, By.xpath("//*[@id='changeTypeTR']/td/div[1]/a[2]"), 2000);	
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the Template of the selected devices?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Restart Devices":
					myDebugPrinting("Enter Restart Devices block", testVars.logerVars.NORMAL);
					String resMode = map.get("resMode");
					myDebugPrinting("resMode - " + resMode, testVars.logerVars.MINOR);
					Select resSelectMode = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select")));
					resSelectMode.selectByVisibleText(resMode);
					myWait(5000);
					if (resMode.equals("Scheduled")) {
						
						String scTime = map.get("schTime");
						myDebugPrinting("scTime - " + scTime, testVars.logerVars.MINOR);
						Select scTimeSelect = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/span/select")));
						scTimeSelect.selectByVisibleText(scTime);
						myWait(5000);
					}
				    myClick(driver, By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/a"), 2000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: restart command will work only on supported IP Phones.\nAre you sure you want to restart the selected IP Phones?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
					waitTillString(driver, "Waiting");
					break;	
					
				case "Generate IP Phones Configuration Files":
					myDebugPrinting("Enter Generate IP Phone Configuration block", testVars.logerVars.NORMAL);  
				    myClick(driver, By.xpath("//*[@id='setIpPhonesTR']/td/div/div/a"), 2000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "The configuration files will be generate to the location define in the template (destinationDir).\nDo you want to continue?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Update Configuration file":
					myDebugPrinting("Enter Update Configuration block", testVars.logerVars.NORMAL); 
				    myClick(driver, By.xpath("//*[@id='updateConfigFilesTR']/td/div/a"), 5000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: update configuration command will work only on supported IP Phones.\nAre you sure you want to update the selected IP Phones files?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Change Firmware":
					myDebugPrinting("Enter Change Firmware block", testVars.logerVars.NORMAL);
					String firmware = map.get("firmware");
				    Select firmTypes = new Select(driver.findElement(By.xpath("//*[@id='firmware_id']")));
				    firmTypes.selectByVisibleText(firmware);
					myWait(5000);
				    myClick(driver, By.xpath("//*[@id='updateFirmwareTR']/td/div[1]/a[2]"), 5000);
				    verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's firmware?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Change VLAN Discovery Mode":
					myDebugPrinting("Enter Change VLAN Discovery Mode block", testVars.logerVars.NORMAL);
					String vlanMode = map.get("vlanMode");
					myDebugPrinting("vlanMode - " + vlanMode, testVars.logerVars.MINOR);				
				    Select vlanTypes = new Select(driver.findElement(By.xpath("//*[@id='changeVlanTR']/td/div[1]/select")));
				    vlanTypes.selectByVisibleText(vlanMode);
				    myWait(5000);
				    if (vlanMode.equals("Manual Configuration")) {
				    	
						String vlanId = map.get("vlanId");
						String vlanpr = map.get("vlanPriority");
						mySendKeys(driver, By.xpath("//*[@id='changeVlanTR']/td/div[2]/input[1]"), vlanId, 3000);	
						mySendKeys(driver, By.xpath("//*[@id='changeVlanTR']/td/div[2]/input[2]"), vlanpr, 3000);	
				    } else if (vlanMode.equals("Disabled")) {
				    	
				    	return;
				    }
				    myClick(driver, By.xpath("//*[@id='changeVlanTR']/td/div[1]/a[2]"), 5000);
				    verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's VLAN discovery mode?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
					waitTillString(driver, "Waiting");
					break;	
					
				case "Delete Devices":
					myDebugPrinting("Enter Delete Devices block", testVars.logerVars.NORMAL);
					myClick(driver, By.xpath("//*[@id='deleteDevicesTR']/td/div/a"), 2000);	
				    verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the selected devices?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	
					waitTillString(driver, "Waiting");
					break;
					
				default:
					myFail("Action is not recognized !!");	
					break;
		  }  
	  }
	  
	  /**
	  *  Add new template according to given parameters
	  *  @param driver   - given driver
	  *  @param tempName - given template name
	  *  @param tempDesc - given template description
	  *  @param tenant   - given template tenant
	  *  @param map      - object for all the optional parameters
	  */
	  public void addTemplate(WebDriver driver, String tempName, String tempDesc, String tenant, String type, Map<String, String> map) {
		  
		// Create new template
		myDebugPrinting("Create new template"    , testVars.logerVars.MINOR);
		myDebugPrinting("tempName - "  + tempName, testVars.logerVars.MINOR);
		myDebugPrinting("type - "      + type    , testVars.logerVars.MINOR);
		myWait(3000);
		
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/buttton"), 4000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"		   , "Add new Template");
		mySendKeys(driver, By.xpath("//*[@id='modelname']")  , tempName, 2000);	
		mySendKeys(driver, By.xpath("//*[@id='description']"), tempDesc, 2000);
		Select tenantType = new Select (driver.findElement(By.xpath("//*[@id='tenant_id']")));
		tenantType.selectByVisibleText(tenant);
		myWait(5000);
		Select tempType = new Select (driver.findElement(By.xpath("//*[@id='model_type']")));
		tempType.selectByVisibleText(type);
		myWait(5000);

		// Check region default template check-box
		if (map.get("isRegionDefault").equals("true")) {
			
			myDebugPrinting("isRegionDefault - TRUE", testVars.logerVars.MINOR);
		}
	  
		// Is clone from other template is needed
		if (!map.get("cloneFromtemplate").isEmpty()) {
			
			myDebugPrinting("cloneFromtemplate is not empty, clone starts !", testVars.logerVars.MINOR);		
			Select cloneTempName = new Select (driver.findElement(By.xpath("//*[@id='clone_model_id']")));
			cloneTempName.selectByVisibleText(map.get("cloneFromtemplate"));
			myWait(5000);
		}
	  
		// Is download shared templates
		if (map.get("isDownloadSharedTemplates").equals("true")) {
			
			myDebugPrinting("isDownloadSharedTemplates - TRUE", testVars.logerVars.MINOR);	
			
			// Enter the Sharefiles menu
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/table/tbody/tr[7]/td/a"), 15000);	
			ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
			driver.switchTo().window((String) tabs.get(1));
			myWait(20000);
			myDebugPrinting("Check Sharefiles menu buttons", testVars.logerVars.MINOR);	
			String txt = driver.findElement(By.tagName("body")).getText();
			myAssertTrue("Latest header was not detected !! ("  + txt + ")", txt.contains("Latest EMS IPP configuration templates"));
			myAssertTrue("Confirm header was not detected !! (" + txt + ")", txt.contains("I agree: Terms and Conditions"));
			
			// Click on Confirm and Select-all check-boxes
			myDebugPrinting("Click on Confirm and Select-all check-boxes", testVars.logerVars.MINOR);	
			myClick(driver, By.xpath("//*[@id='applicationHost']/div/div[1]/div[2]/div/div[7]/div[2]/div/div[1]/label/div"),  7000);		
			myClick(driver, By.xpath("//*[@id='applicationHost']/div/div[1]/div[2]/div/div[5]/div/div[1]/label/div[1]"), 7000);		
			String shareFilesFile = testVars.getShareFilesName();
			deleteFilesByPrefix(testVars.getDownloadsPath(), shareFilesFile);		
			
			// Click on Download and check the download
			myDebugPrinting("Click on Download and check the download", testVars.logerVars.MINOR);	
			myClick(driver, By.xpath("//*[@id='applicationHost']/div/div[1]/div[2]/div/div[7]/div[2]/div/div[2]/div[2]/button"), 60000);
			myAssertTrue("File was not downloaded successfully !!", findFilesByGivenPrefix(testVars.getDownloadsPath(), shareFilesFile));
			deleteFilesByPrefix(testVars.getDownloadsPath(), shareFilesFile);		
			return;
		}	
		
		// Is two templates create or bad name
		if (map.containsKey("secondCreate")) {
			
			myDebugPrinting("secondCreate - TRUE (create two templates with same name test)", testVars.logerVars.NORMAL);
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 3000);		
			verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Template");
			verifyStrByXpath(driver, "//*[@id='modalContentId']", "Failed to create new template. Reason: this template name exists.");
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);				
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 3000);		
			return;
			
		} else if (map.containsKey("falseCreateName")) {
			
			myDebugPrinting("falseCreateName - TRUE (false data)", testVars.logerVars.MINOR);
			tempName = map.get("falseCreateName");
			tempDesc = map.get("falseCreateDesc");
			
			// Try to confirm empty name
			myDebugPrinting("Try to confirm empty name", testVars.logerVars.MINOR);
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 3000);	
			verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[1]/div[3]/h4", "Please enter template name");
			mySendKeys(driver, By.xpath("//*[@id='modelname']")  , tempName, 2000);	
			
			// Try to confirm empty description
			myDebugPrinting("Try to confirm empty description", testVars.logerVars.MINOR);
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 3000);	
			verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[1]/div[3]/h4", "Please enter template description");
			mySendKeys(driver, By.xpath("//*[@id='description']"), tempDesc, 2000);
		}
				
		if (map.containsKey("secondCreate")) {
			
			myDebugPrinting("secondCreate - TRUE (upload big cfg file test)", testVars.logerVars.NORMAL);
//			driver.findElement(By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[7]/td[2]/a")).click();
//			myWait(3000);
//			verifyStrByXpath(driver, "//*[@id='title']", "Upload IP Phone Template " + tempName);
//			String bigCfgPath = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("36");
//			myDebugPrinting("bigCfgPath - " + bigCfgPath, testVars.logerVars.MINOR);
//			driver.findElement(By.xpath("//*[@id='IntroScreen']/form/table/tbody/tr[3]/td/input")).sendKeys(bigCfgPath);
//			myWait(5000);
//			driver.findElement(By.xpath("//*[@id='submit_img']")).click();
//			myWait(45000);
//			verifyStrByXpath(driver, "//*[@id='promt_div_id']"        , "Please choose a cfg file.");
//		    driver.findElement(By.xpath("//*[@id='jqi_state0_buttonOk']")).click();
//		    myWait(2000);
		    driver.findElement(By.xpath("//*[@id='back_img']")).click();
		    myWait(2000);
			return;
		}
		
		// Submit
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 7000);	
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Template");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Add new template of tenant successfully.");
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "IP Phones Configuration Templates");

		// Verify create
		String currText     = driver.findElement(By.tagName("body")).getText();
	    Assert.assertTrue("Text not found!" 
	    				   + "\ncurrTemplate - " + tempName
	    				   + "\ncurrText - "     + currText    , currText.contains(tempName)); 	
	  }
	  
	  /**
	  *  Delete a template
	  *  @param driver       - given driver
	  *  @param tempName     - given template name
	  *  @throws IOException 
	  */
	  public void deleteTemplate(WebDriver driver, String tempName) throws IOException {
		  
		  myDebugPrinting("tempName - " + tempName, testVars.logerVars.MINOR);	
		  myWait(3000);
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
			  if (l.contains(tempName)) {
				  
				  myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				  break;
			  }
			  if (l.contains("Edit" )) {
				  
				  ++i;
			  }
		  } 
		  
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[8]/div/buttton[2]"), 3000);		  
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Template");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + tempName + " IP Phone Model?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Delete template successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);

		  // Verify delete
		  String bodyText = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("Delete didn't succeed !! (" + tempName + " still appear)", !bodyText.contains(tempName));  
	  }
	  
	  /**
	  *  Edit a template
	  *  @param driver     					- given driver
	  *  @param tempName   					- given template name
	  *  @param map 	   					- data structure of strings for edit
	  *  @throws IOException 
	  *  @throws UnsupportedFlavorException 
	  */
	  public void editTemplate(WebDriver driver, String tempName, Map<String, String> map) throws UnsupportedFlavorException, IOException {
		  
		  myDebugPrinting("tempName - " + tempName, testVars.logerVars.MINOR);
		  myWait(3000);
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
			  if (l.contains(tempName)) {
				  
				  myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				  break;
			  }
			  if (l.contains("Edit" )) {
				  
				  ++i;
			  }
		  } 
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[8]/div/buttton[1]"), 3000);		  	  
		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"        , "IP Phone " + tempName + " Configuration Template");

		  // Edit template
		  myDebugPrinting("Edit template", testVars.logerVars.NORMAL); 		  
		  if (map.containsKey("tenant")) {
			  
			  Select tenId  = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
			  tenId.selectByVisibleText(map.get("tenant"));
				myWait(5000);
		  }
		  if (map.containsKey("model")) {
			  
			  Select motype = new Select(driver.findElement(By.xpath("//*[@id='model_type']")));		
			  motype.selectByVisibleText(map.get("model"));
				myWait(5000);
		  }
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[3]/div/div[3]/button"), 7000);	
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update configuration template");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Succesfull to update configuration template.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		
		  // Verify edit
		  myDebugPrinting("Verify edit", testVars.logerVars.NORMAL);  
		  r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  l = null;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting(l, testVars.logerVars.DEBUG);
			  if (l.contains(tempName)) {
				  
				  if (map.containsKey("tenant")) {
					  				  
					  myAssertTrue("Tenenat was not edited !!", l.contains(map.get("tenant")));
				  }
				  if (map.containsKey("model")) {
					  
					  myAssertTrue("Model was not edited !!", l.contains(map.get("model")));
				  } 
				  break;
			  }
		  }		  
	  }
	  
	  /**
	  *  Add a new region placeholder
	  *  @param driver    - given driver
	  *  @param regPhName - given region placeholder name
	  *  @param regValue  - given region placeholder value
	  *  @param regRegion - given region placeholder region
	  */
	  public void addRegionPlaceholder(WebDriver driver, String regPhName, String regValue, String regRegion) {
		  
		  driver.findElement(By.xpath("//*[@id='ipp-panel']/div[2]/table/tbody/tr[1]/td[3]/button/span")).click();
		  myWait(3000);
		  verifyStrByXpath(driver, "//*[@id='title']", "Add new placeholder");
		  driver.findElement(By.xpath("//*[@id='project']")).sendKeys(regPhName);	  
		  driver.findElement(By.xpath("//*[@id='trunkTBL']/form/table/tbody/tr/td/table/tbody/tr[3]/td[2]/input")).sendKeys(regValue);	  
		  driver.findElement(By.xpath("//*[@id='region_id']")).sendKeys(regRegion);	  
		  driver.findElement(By.xpath("//*[@id='submit_img']")).click();
		  myWait(3000);
		  
		  // verify create
		  myDebugPrinting("verify create", testVars.logerVars.MINOR);
		  driver.findElement(By.xpath("//*[@id='regions1-filtering']")).clear(); 
		  driver.findElement(By.xpath("//*[@id='regions1-filtering']")).sendKeys(regPhName);	  
		  myWait(3000);
		  String bodyText = driver.findElement(By.tagName("body")).getText();
		  Assert.assertTrue("regPhName not found! \n" + bodyText, bodyText.contains(regPhName));
		  Assert.assertTrue("regValue not found! \n"  + bodyText, bodyText.contains(regValue));
		  Assert.assertTrue("regRegion not found \n!" + bodyText, bodyText.contains(regRegion)); 
	  }
	  
	  /**
	  *  Edit an existing region placeholder
	  *  @param driver    - given driver
	  *  @param regPhName - given region placeholder name for modify
	  *  @param regValue  - given region placeholder value for modify
	  *  @param regRegion - given region placeholder region for modify
	  */
	  public void editRegionPlaceholder(WebDriver driver, String regPhName, String regValue, String regRegion) {
		  
		  // Some manipulations to detect row ID
		  String bodyText = driver.findElement(By.tagName("body")).getText();
		  String rowId = bodyText.substring(bodyText.indexOf("Placeholder Value Region") + "Placeholder Value Region".length(), bodyText.indexOf("%ITCS_") - 1);
		  myDebugPrinting("rowId - " + rowId, testVars.logerVars.MINOR);
		  
		  driver.findElement(By.xpath("//*[@id='regions1']/tbody/tr[" + rowId + "]/td[6]/button")).click();
		  myWait(3000);
		  verifyStrByXpath(driver, "//*[@id='title']", "Edit placeholder");
		  driver.findElement(By.xpath("//*[@id='project']")).clear();  
		  driver.findElement(By.xpath("//*[@id='project']")).sendKeys(regPhName);
		  driver.findElement(By.xpath("//*[@id='trunkTBL']/form/table/tbody/tr/td/table/tbody/tr[3]/td[2]/input")).clear();
		  driver.findElement(By.xpath("//*[@id='trunkTBL']/form/table/tbody/tr/td/table/tbody/tr[3]/td[2]/input")).sendKeys(regValue);	  
		  driver.findElement(By.xpath("//*[@id='region_id']")).sendKeys(regRegion);	  
		  driver.findElement(By.xpath("//*[@id='submit_img']")).click();
		  myWait(3000);
		  		  
		  // verify edit
		  myDebugPrinting("verify edit", testVars.logerVars.MINOR);
		  driver.findElement(By.xpath("//*[@id='regions1-filtering']")).clear(); 
		  driver.findElement(By.xpath("//*[@id='regions1-filtering']")).sendKeys(regPhName);	  
		  myWait(3000);
		  verifyStrByXpath(driver, "//*[@id='regions1']/tbody/tr[9]/td[3]/b", regPhName);
		  verifyStrByXpath(driver, "//*[@id='regions1']/tbody/tr[9]/td[4]"  , regValue);
		  verifyStrByXpath(driver, "//*[@id='regions1']/tbody/tr[9]/td[5]"  , regRegion);  
	  }
	  
	  /**
	  *  Delete a region placeholder
	  *  @param driver    - given driver
	  *  @param regPhName - given region placeholder name for delete
	  *  @param regRegion - given region placeholder region for modify
	  */
	  public void deleteRegionPlaceholder(WebDriver driver, String regPhName, String regRegion) {
		  
		  // Some manipulations to detect row ID
		  String bodyText = driver.findElement(By.tagName("body")).getText();
		  String rowId = bodyText.substring(bodyText.indexOf("Placeholder Value Region") + "Placeholder Value Region".length(), bodyText.indexOf("%ITCS_") - 1);
		  myDebugPrinting("rowId - " + rowId, testVars.logerVars.MINOR);  
		  driver.findElement(By.xpath("//*[@id='regions1']/tbody/tr[" + rowId + "]/td[7]/button")).click();
		  myWait(1000);
		  verifyStrByXpath(driver, "//*[@id='jqi_state_state0']/div[1]/table/tbody/tr[1]/th"  , "Delete Region - " + regPhName);  
		  verifyStrByXpath(driver, "//*[@id='promt_div_id']"  , "Are you sure you want to delete this region?:");  
		  driver.findElement(By.xpath("//*[@id='jqi_state0_buttonOk']")).click();
		  myWait(3000);
		  verifyStrByXpath(driver, "//*[@id='jqi_state_state0']/div[1]/table/tbody/tr[1]/th"  , "Delete Region");  
		  verifyStrByXpath(driver, "//*[@id='promt_div_id']"  								  , "The selected " + regPhName + " placeholder has been successfully removed from " + regRegion);
		  driver.findElement(By.xpath("//*[@id='jqi_state0_buttonOk']")).click();

		  // verify delete
		  myDebugPrinting("verify delete", testVars.logerVars.MINOR);
		  driver.findElement(By.xpath("//*[@id='regions1-filtering']")).clear(); 
		  driver.findElement(By.xpath("//*[@id='regions1-filtering']")).sendKeys(regPhName);	  
		  myWait(3000);
		  bodyText = driver.findElement(By.tagName("body")).getText();
		  if (bodyText.contains(regPhName)) {
			  
			  myFail("Delete did not succeeded !!");
		  }
	  }
	  
	  /**
	  *  Copy a placeholder from another Tenant
	  *  @param driver      - given driver
	  *  @param tenThatCopy - tenant to whom who copy a placeholder
	  *  @param tenPhName   - given tenant placeholder name which was copied
	  *  @param tenValue    - given tenant placeholder value which was copied
	  *  @param tenTenant   - given tenant-placeholder tenant we copied from
	  */
	  public void copyPlaceholder(WebDriver driver, String tenThatCopy, String tenPhName, String tenValue, String tenTenant) {
		 
		  // Move to new Tenant
		 Select tenId = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
		 tenId.selectByVisibleText(tenThatCopy);
		 myWait(5000);
		 
		 // Copy the PH from original tenant
		 myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[1]/a"), 3000);
		 verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Copy Tenant Placeholders From");  
		 verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please select the Tenant Placeholders to copy.");  
		 Select tenFromCopy = new Select(driver.findElement(By.xpath("/html/body/div[2]/div/select")));
		 tenFromCopy.selectByVisibleText(tenTenant);
		 myWait(5000);
		 myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		 verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Copy Tenant Placeholders From");  
		 myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		  
		 // verify copy
		 myDebugPrinting("verify copy", testVars.logerVars.MINOR);
		 mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 6000); 
		 searchStr(driver, "%ITCS_" + tenPhName + "%");
		 searchStr(driver, tenValue);
		 searchStr(driver, tenTenant);
	  }
	  
	  /**
	  *  Add new Template placeholder
	  *  @param driver      	  - given driver
	  *  @param tempName     	  - template for which we create the placeholder
	  *  @param tempPhName   	  - name for the Template placeholder
	  *  @param tempPhValue       - value for the Template placeholder
	  *  @param tempPhDescription - description for the Template placeholder
	  */  
	  public void addTemplatePlaceholder(WebDriver driver, String tempName, String tempPhName, String tempPhValue, String tempPhDescription) {
		    
		  // Select a model
		  myDebugPrinting("Select a model", testVars.logerVars.MINOR);
		  Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
		  models.selectByVisibleText(tempName);
		  myWait(10000);
		  
		  // Fill data
		  myDebugPrinting("Fill data", testVars.logerVars.MINOR);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[2]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]/a"), 7000);
//		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/label", "IP Phone Model - " + tempName);
		  mySendKeys(driver, By.xpath("//*[@id='ph_name']") , tempPhName       , 7000);
		  mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tempPhValue      , 7000);
		  mySendKeys(driver, By.xpath("//*[@id='ph_desc']") , tempPhDescription, 7000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[4]/button[2]"), 10000);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save new template " + tempPhName + " placeholder.");
		  myWait(1000);
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "The new placeholder was saved successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);  
		  
		  // Verify create
		  myDebugPrinting("verify create", testVars.logerVars.MINOR);
		  searchStr(driver, "%ITCS_" + tempPhName + "%");
		  searchStr(driver, tempPhValue);
		  searchStr(driver, tempPhDescription);
	  }
	  
	  /**
	  *  Edit an existing Template placeholder
	  *  @param driver      	  - given driver
	  *  @param tempName     	  - template for which we edit the placeholder
	  *  @param tempPhName   	  - name for edit in the Template placeholder
	  *  @param tempPhValue       - value for edit in the Template placeholder
	  *  @param tempPhDescription - description for edit in the Template placeholder
	  *  @throws IOException
	  */ 
	  public void editTemplatePlaceholder(WebDriver driver,  String tempName, String tempPhName, String tempPhValue, String tempPhDescription) throws IOException {
		   
		  // Select a model
		  myDebugPrinting("Select a model", testVars.logerVars.MINOR);
		  Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
		  models.selectByVisibleText(tempName);
		  myWait(5000);  
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 0;
		  myDebugPrinting("tempPhName - " + tempPhName, testVars.logerVars.MINOR);  
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting(l, testVars.logerVars.MINOR); 
			  if (l.contains("Edit" )) { ++i; }
			  if (l.contains(tempPhName)) {
				  
				  myDebugPrinting("returned i - " + i, testVars.logerVars.MINOR);
				  break;
			  }
		  }  
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[6]/a[1]"), 7000);
		  
		  // Edit 
		  myDebugPrinting("Edit" + tempPhName, testVars.logerVars.MINOR);
		  mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tempPhValue      , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='ph_desc']") , tempPhDescription, 2000);  				  
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[4]/button[2]"), 3000);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update template " + tempPhName + " placeholder.");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Update new placeholder successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);

		  // Verify edit
		  myDebugPrinting("verify edit", testVars.logerVars.MINOR);
		  searchStr(driver, tempPhValue);
		  searchStr(driver, tempPhDescription);
	  }
	  
	  /**
	  *  Delete a template placeholder
	  *  @param driver     - given driver
	  *  @param tempName   - given Template from whom we want to delete a placeholder
	  *  @param tempPhName - given Template placeholder
	  *  @throws IOException 
	  */
	  public void deleteTemplatePlaceholder(WebDriver driver, String tempName, String tempPhName) throws IOException {
		  		  
		  // Select a model
		  myDebugPrinting("Select a model", testVars.logerVars.MINOR);
		  Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
		  models.selectByVisibleText(tempName);
		  myWait(5000);
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 0;
		  myDebugPrinting("tempPhName - " + tempPhName, testVars.logerVars.MINOR);  
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting(l, testVars.logerVars.MINOR);
			  if (l.contains("Edit" )) { ++i; }
			  if (l.contains(tempPhName)) {
				  
				  myDebugPrinting("returned i - " + i, testVars.logerVars.MINOR);
				  break;
			  }
		  }  
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[6]/a[2]"), 7000);	  
		  
		  // Delete 
		  myDebugPrinting("Delete", testVars.logerVars.MINOR);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete " +  tempPhName);  
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this value?");  
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);

		  // Verify delete
		  myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
		  myAssertTrue("Delete did not succeeded !!", !driver.findElement(By.tagName("body")).getText().contains(tempPhName));
	  }
	  
	  /**
	  *  Copy a template placeholder from other Template
	  *  @param driver         - given driver
	  *  @param tempWecopyFrom - given Template from whom we want to delete a placeholder
	  *  @param tempWeCopyTo   - given Template placeholder name
	  */
	  public void copyTemplatePlaceholder(WebDriver driver, String tempWecopyFrom, String tempWeCopyTo, String tempPhName) {
		  	
			Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
			models.selectByVisibleText(tempWeCopyTo);
			myWait(5000);
			myClick(driver, By.xpath("//*[@id='import']"), 3000);
			verifyStrByXpath(driver, "//*[@id='modalTitleId']"	, "Import Place Holders");
			verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please select a model");
			Select modelsToCopy = new Select(driver.findElement(By.xpath("/html/body/div[2]/div/select")));
			modelsToCopy.selectByVisibleText(tempWecopyFrom);
			myWait(5000);
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);

			// Copy a template place holder to Default Template should not be possible
			if (tempWecopyFrom.contains("operTemplate")) {
				
				myDebugPrinting("Copy a template place holder to Default Template should not be possible", testVars.logerVars.MINOR);
				verifyStrByXpath(driver, "/html/body/div/div/div[1]/h3"     , "Unauthorized");
				verifyStrByXpath(driver, "/html/body/div/div/div[2]/div/div", "You do not have permission to modify this item"); 
				return;
			}
			verifyStrByXpath(driver, "//*[@id='modalContentId']", "Import place holders succesfully");
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
			
			// Verify import
			models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
			models.selectByVisibleText(tempWecopyFrom);
			myWait(5000);
			models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
			models.selectByVisibleText(tempWeCopyTo);
			myWait(5000);
			searchStr(driver, "%ITCS_" + tempPhName + "%");
	  }
	  
	  /**
	  *  Add new IP phone firmware
	  *  @param driver       - given driver
	  *  @param firmName     - name of the added firmware
	  *  @param firmDesc     - description of the added firmware
	  *  @param firmVersion  - version of the added firmware
	  *  @param firmTenant   - name of the region for the added firmware
	  *  @param firmFileName - name for the file we want to upload for the firmware
	  */
	  public void addNewFirmware(WebDriver driver, String firmName, String firmDesc, String firmVersion, String firmTenant, String firmFileName) {
		  	
		  // Enter the Add-New-Firmware menu
		  myDebugPrinting("Enter the Add-New-Firmware menu", testVars.logerVars.MINOR);
		  myClick(driver, By.xpath("//*[@id='tbTemps']/tbody/tr[1]/td/a"), 2000);
		  verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[1]/h3", "Add new IP Phone firmware");
		  
		  // Fill data
		  myDebugPrinting("Fill data", testVars.logerVars.MINOR);
		  mySendKeys(driver, By.xpath("//*[@id='name']")       , firmName   , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='description']"), firmDesc   , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='version']")    , firmVersion, 2000);
		  if (!firmTenant.isEmpty()) {

			  Select myFirmTenant = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
			  myFirmTenant.selectByVisibleText(firmTenant);
			  myWait(5000);
		  }
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button[1]"), 7000);
		  
		  // Upload firmware file
		  myDebugPrinting("Upload firmware file", testVars.logerVars.MINOR);		  
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/div/div[5]/a"), 3000);
//		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Upload IP Phone Firmware " + firmName);
		  WebElement fileInput = driver.findElement(By.name("uploadedfile"));
		  fileInput.sendKeys(testVars.getSrcFilesPath() + "\\" + firmFileName);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 15000);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Upload Successful");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "The IP Phone firmware has been uploaded successfully.");	
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 15000);

		  // Verify create
		  myDebugPrinting("Verify create", testVars.logerVars.MINOR);		  
		  enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");
		  searchStr(driver, firmName);
		  searchStr(driver, firmDesc);
		  searchStr(driver, firmDesc);
	  }
	  
	  /**
	  *  Edit an existing IP phone firmware
	  *  @param driver         - given driver
	  *  @param firmName       - name of the created firmware
	  *  @param firmDesc       - description of the created firmware
	  *  @param newFirmDesc    - description of the edited firmware
	  *  @param newFirmVersion - version of the edited firmware
	  *  @param newFirmTenant  - name of the Tenant for the edited firmware
	  *  @throws IOException 
	  */
	  public void editFirmware(WebDriver driver, String firmName, String firmDesc, String newFirmDesc, String newFirmVersion, String newFirmTenant) throws IOException {
		  
		  // Edit Firmware
		  myDebugPrinting("Edit Firmware", testVars.logerVars.MINOR);		  
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting("i - " + i, testVars.logerVars.DEBUG);
			  myDebugPrinting(l			, testVars.logerVars.DEBUG);  
			  if (l.contains(firmName)) {
				  
				  myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				  break;
			  }
			  if (l.contains("Edit" )) {
				  
				  ++i;
			  }
		  } 
		  myClick(driver, By.xpath("//*[@id='tbTemps']/tbody/tr[2]/td/table/tbody/tr["+ i + "]/td[7]/a"), 3000);

		  // Edit Firmware
		  verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[1]/h3", "IP Phone " + firmName + " Firmware");
		  mySendKeys(driver, By.xpath("//*[@id='description']"), newFirmDesc   , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='version']")    , newFirmVersion, 2000); 
		  if (!newFirmTenant.isEmpty()) {
			  
			  Select myFirmTenant = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
			  myFirmTenant.selectByVisibleText(newFirmTenant);
			  myWait(5000);
		  }  
		  
		  if (!newFirmTenant.isEmpty()) {
			  
			  // Check download
			  myDebugPrinting("Check download", testVars.logerVars.MINOR);	
			  String downloadedFile = firmName + ".img";
			  deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
			  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/div/div[6]/a"), 20000);
			  myAssertTrue("File was not downloaded successfully !!", findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile));
			  deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);  
		  }
		
		  // Submit and verify create  
		  myDebugPrinting("Submit and verify create", testVars.logerVars.MINOR);	
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button[1]"), 7000);
		  String bodyText = driver.findElement(By.tagName("body")).getText();
	      myAssertTrue("Edited name was not detected !! ("        + firmName       + ")", bodyText.contains(firmName));
	      myAssertTrue("Edited description was not detected !! (" + newFirmDesc    + ")", bodyText.contains(newFirmDesc));
	      myAssertTrue("Edited version was not detected !! ("     + newFirmVersion + ")", bodyText.contains(newFirmVersion));	  
	  }
	  
	  /**
	  *  Delete an existing IP phone firmware
	  *  @param driver         - given driver
	  *  @param firmName       - name of the deleted firmware
	  *  @param newFirmDesc    - description of the deleted firmware
	  *  @param newFirmVersion - version of the deleted firmware
	  */
	  public void deleteFirmware(WebDriver driver, String firmName, String newFirmDesc, String firmVersion) {
		  
		  // Delete firmware 
		  myDebugPrinting("Delete firmware", testVars.logerVars.MINOR);
		  myClick(driver, By.cssSelector("a[href*='" + firmName + "']"), 4000); 
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Firmware");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + firmName + " IP Phone firmware?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000); 
		  
		  // Verify delete
		  myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
		  String bodyText = driver.findElement(By.tagName("body")).getText();
	      myAssertTrue("Firmware description still detected !! (" + newFirmDesc + ")", !bodyText.contains(newFirmDesc));
	      myAssertTrue("Firmware version still detected !! ("     + firmVersion + ")", !bodyText.contains(firmVersion));	  
	  }
	  
	  /**
	  *  Send a string to a given element using given parameters
	  *  @param driver  - given driver
	  *  @param byType  - given By element (By xpath, name or id)
	  *  @param currUsr - given string to send
	  *  @param timeout - given timeout (Integer)
	  */
	  public void mySendKeys(WebDriver driver, By byType, String currUsr, int timeOut) {
		  
		  driver.findElement(byType).clear();
		  myWait(2000);
		  driver.findElement(byType).sendKeys(currUsr);
		  myWait(timeOut);
	  }
	  
	  /**
	  *  Add a device placeholder to existing registered user
	  *  @param driver   - given driver
	  *  @param userName - pre-create registered user
	  *  @param phName   - placeholder name
	  *  @param phValue  - placeholder value to override
	  */
	  public void addDevicePlaceholder(WebDriver driver, String userName, String phName, String phValue) {
		    
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/div[2]/a"), 3000);
		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/form/div/div[1]/h3", "Change IP Phone Device Placeholder");
		  verifyStrByXpath(driver, "//*[@id='table_all']/thead/tr/th"						 , "Please select a device");
		  mySendKeys(driver, By.xpath("//*[@id='table_all']/tbody/tr[1]/td[2]/div/input"), userName, 2000);
		  driver.findElement(By.xpath("//*[@id='table_all']/tbody/tr[1]/td[2]/div/input")).sendKeys(Keys.ENTER);	    
		  myWait(20000);		  
		  Actions action = new Actions(driver);
		  WebElement element=driver.findElement(By.xpath("//*[@id='devices_body']/tr/td[3]/b[1]"));
		  action.doubleClick(element).perform();
		  Select devKey = new Select(driver.findElement(By.xpath("//*[@id='key']")));
		  devKey.selectByVisibleText(phName);
		  myWait(5000);
		  mySendKeys(driver, By.xpath("//*[@id='over_value']"), phValue, 2000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[3]/button[2]") , 7000);
  
		  // Verify create
		  myDebugPrinting("Verify create", testVars.logerVars.MINOR);  
		  mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
		  String bodyText = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("placeholder name was not found!" , bodyText.contains("%ITCS_" + phName + "%"));
		  myAssertTrue("placeholder value was not found!", bodyText.contains(phValue));
	  }
	  
	  /**
	   *  Delete an existing device placeholder
	   *  @param driver     - given driver
	   *  @param userName   - pre-create registered user
	   *  @param phName     - existing placeholder name for edit
	   *  @param phNewValue - new value for the placeholder
	   */
	   public void deleteDevicePlaceholder(WebDriver driver, String userName, String phName, String phNewValue) {
	 	  
	 	  // Delete the device Placeholder
	 	  myDebugPrinting("Delete the device Placeholder", testVars.logerVars.MINOR);
	 	  mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
	 	  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
	 	  myClick(driver, By.xpath("//*[@id='placeholders_body']/tr/td[8]/a"), 5000);  
	 	  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete item: " + phName);
	 	  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this value?");
	 	  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  
	 	
	 	  // Verify delete
	 	  mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
	 	  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
	 	  searchStr(driver, "There are no placeholders at present");
	 	  String bodyText = driver.findElement(By.tagName("body")).getText();
	 	  myAssertTrue("PH name is still detecetd !!\nbodyText - "  + bodyText, !bodyText.contains(phName));
	 	  myAssertTrue("PH value is still detected !!\nbodyText - " + bodyText, !bodyText.contains(phNewValue));  
	   }
	  
	  /**
	  *  Click on given element by given xpath and waits a given timeout
	  *  @param driver  - given driver
	  *  @param byType  - given By element (By xpath, name or id)
	  *  @param timeout - given timeout value (Integer)
	  */
	  public void myClick(WebDriver driver, By byType, int timeout) {
		  
		  myWait(1000);
	      driver.findElement(byType).click();
		  myWait(timeout);
	  }
	  
	  /**
	  *  Create a user using an external exe script on given element by given xpath and waits a given timeout
	  *  @param crUserBatName - given external script full path
	  *  @param ip  		  - IP for target the script 			  (I.e. 10.21.8.32)
	  *  @param	port		  - port for target the script 			  (I.e. 8090)
	  *  @param usrsNumber 	  - number for users for create
	  *  @param	dipName		  - display name	
	  *  @param domain		  - domain of the created users 		  (I.e onebox3.com)
	  *  @param crStatus	  - status of the create 				  (I.e registered / unregistered, offline etc.)
	  *  @param phoneType 	  - phone type 							  (I.e 420HD etc).
	  *  @param tenant		  - Tenant in which the user is created   (I.e ErezG)
	  *  @param	location	  - Location in which the user is created
	  *  @throws IOException 
	  */
	  public void createUserViaPost(String crUserBatName, String ip, String port, String usrsNumber, String dipName, String domain, String crStatus, String phoneType, String tenant, String location) throws IOException {
		
	    Process process = new ProcessBuilder(System.getProperty("user.dir") + "\\" + testVars.getCrUserBatName(), 
				 ip  	   ,
				 port	   ,
				 usrsNumber,
				 dipName   ,
				 domain    ,   
				 crStatus  ,
				 phoneType ,
				 tenant    ,
				 location).start();
	    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line;
	    while ((line = br.readLine()) != null) {

	    	myDebugPrinting(line, testVars.logerVars.MINOR);
	    	if (line.contains("device_create_error")) {
		
	    		myFail("Device <" + line.substring(line.indexOf("___") + 3) + "> was not created !!");    		
	    	}
	    }
	  }  
	  
	  /**
	  *  Create a user using an external exe script on given element by given xpath and waits a given timeout
	  *  @param crUserBatName - given external script full path
	  *  @param ip  		  - IP for target the script  			(I.e. 10.21.8.32)
	  *  @param	port		  - port for target the script 			(I.e. 8081)
	  *  @param mac 	  	  - MAC address of registered device		(I.e. 00908fdf411a)
	  *  @param	alertName	  - alert name							(I.e. IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE)
	  *  @param alertDesc	  - alert description					(I.e. dateTest)
	  *  @param alertDate	  - alert date							(I.e. 2017-07-217T12:24:18)
	  *  @param alertInfo1 	  - alert info-1						(I.e. info1)
	  *  @param alertInfo2 	  - alert info-2						(I.e. info2)
	  *  @param	alertSev	  - alert severity						(I.e. "info", "minor", "normal", "major", "critical")
	  *  @throws IOException 
	  */
	  public void createAlarmViaPost(String crUserBatName, String ip, String port, String mac,
			  						 String alertName    , String alertDesc      , String alertDate,
			  						 String alertInfo1	 , String alertInfo2	 , String alertSev) throws IOException {
		
	    Process process = new ProcessBuilder(System.getProperty("user.dir") + "\\" + crUserBatName, 
				 ip  	   ,
				 port	   ,
				 mac	   ,
				 alertName ,
				 alertDesc ,   
				 alertDate ,
				 alertInfo1,
				 alertInfo2,
				 alertSev).start();
	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line;
	    while ((line = br.readLine()) != null) {

	    	myDebugPrinting(line, testVars.logerVars.MINOR);
	    }
	  }
	  
	  /**
	  *  Create a Tenant-PH with given variables
	  *  @param driver     - given element
	  *  @param tenPhName  - given Tenant-ph name
	  *  @param tenPhValue - given Tenant-ph value
	  *  @param tenTenant  - given Tenant for the tenant-ph
	  */
	  public void addTenantPH(WebDriver driver, String tenPhName, String tenPhValue, String tenTenant) {
		  
		// Fill data
	    myDebugPrinting("Fill data", testVars.logerVars.NORMAL);  
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a"), 10000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add new placeholder");
		myWait(1000);
		Select tenId = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/select")));
		tenId.selectByVisibleText(tenTenant);
		myWait(7000);
		myDebugPrinting("tenPhName - "  + tenPhName ,testVars.logerVars.MINOR);  
	    myDebugPrinting("tenPhValue - " + tenPhValue,testVars.logerVars.MINOR);
		mySendKeys(driver, By.xpath("//*[@id='ph_name']") , tenPhName , 10000);
		mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tenPhValue, 10000);
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 10000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save new tenant " + tenPhName + " placeholder.");
		myWait(1000);
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The new placeholder was saved successfully." + tenPhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
		
		// Verify the create
	    myDebugPrinting("Verify the create", testVars.logerVars.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 10000);
		searchStr(driver, "%ITCS_" + tenPhName  + "%");
		searchStr(driver, tenPhValue);
	  }
	  
	  /**
	  *  Delete a Tenant-PH with given variables
	  *  @param driver       - given element
	  *  @param tenPhName    - given Tenant-ph name
	  *  @param tenPhValue   - given Tenant-ph value
	  *  @throws IOException 
	  */  
	  public void deleteTenantPH(WebDriver driver, String tenPhName, String tenPhValue) throws IOException {
		  
	    // Get idx for delete
		myDebugPrinting("Get idx for delete", testVars.logerVars.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), " " , 6000);
		BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		String l = null;
		int i = 1;
		while ((l = r.readLine()) != null) {
			
			myDebugPrinting("i - " + i, testVars.logerVars.DEBUG);
			myDebugPrinting(l			, testVars.logerVars.DEBUG);  
			if (l.contains(tenPhName)) {
				  
				myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				break;
			}
			if (l.contains("Delete" )) {
				  
				  ++i;
			}
		}
		
		// Check if the current user is "Monitoring" if so - delete should fail
	    myDebugPrinting("Check if the current user is \"Monitoring\" if so - delete should fail", testVars.logerVars.NORMAL);  
		if (tenPhName.contains("tenMonitPhName") && !tenPhValue.contains("Forced")) {
			
			String deleteButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[2]")).getAttribute("class");
			myAssertTrue("Delete button is not deactivated !!\ndeleteButton - " + deleteButton, deleteButton.contains("not-active"));
			return;
		}
		
		// Delete PH
		myDebugPrinting("Delete PH", testVars.logerVars.NORMAL);
		myClick(driver, By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this placeholder?: " + tenPhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The selected " + tenPhName + " placeholder has been successfully removed from");
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
	
		// Verify delete
		myDebugPrinting("Verify delete", testVars.logerVars.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName, 6000);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		myAssertTrue("Tenant-PH name still exist !!\nbodyText - "  + bodyText, !bodyText.contains(tenPhName));
	  }
	  
	  /**
	  *  Edit a Tenant-PH with given variables
	  *  @param driver        - given element
	  *  @param tenPhName     - given Tenant-ph name
	  *  @param tenNewPhValue - new given Tenant-ph value
	  *  @throws IOException 
	  */  
	  public void editTenantPH(WebDriver driver, String tenPhName,String tenNewPhValue) throws IOException {
		  
		// Get idx for delete
		myDebugPrinting("Get idx for delete", testVars.logerVars.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), " " , 6000);
		BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		String l = null;
		int i = 1;
		while ((l = r.readLine()) != null) {
				
			myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
			if (l.contains(tenPhName)) {
					  
				myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				break;
			}
			if (l.contains(" %ITCS_" )) {
				++i;
			}
		}
		myWait(7000);
	
		// Check if the current user is "Monitoring" if so - edit should fail
	    myDebugPrinting("Check if the current user is \"Monitoring\" if so - edit should fail", testVars.logerVars.NORMAL);  
		if (tenPhName.contains("tenMonitPhName")) {
			
			String editButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]")).getAttribute("class");
			myAssertTrue("Edit button is not deactivated !!\neditButton - " + editButton, editButton.contains("not-active"));
			return;
		}
	    myDebugPrinting("xpath - " + "//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]", testVars.logerVars.DEBUG);  
		myClick(driver, By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]"), 10000);
		
		// Fill data
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Edit placeholder");
		mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tenNewPhValue, 7000);
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update tenant " + tenPhName + " placeholder.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Update new placeholder successfully." + tenPhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
		
		// Verify the create
	    myDebugPrinting("Verify the create", testVars.logerVars.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 6000);
		searchStr(driver, "%ITCS_" + tenPhName + "%");
		searchStr(driver, tenNewPhValue);
	  }
	  
	  /**
	  *  Create a Site-PH with given variables
	  *  @param driver      - given element
	  *  @param sitePhName  - given Site-PH name
	  *  @param sitePhValue - given Site-PH value
	  *  @param sitePHSite  - given Site-PH site
	  *  @param siteTenant  - given Site-PH tenant
	  */
	  public void addSitePH(WebDriver driver, String sitePhName, String sitePhValue, String sitePHSite, String siteTenant) {
		  
		// Add new Site-PH
	    myDebugPrinting("Add new Site-PH", testVars.logerVars.NORMAL);  
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a"), 5000);	
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add new placeholder");		
		Select siteId = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/select")));
		siteId.selectByVisibleText(sitePHSite);	
		myWait(5000);	
		mySendKeys(driver, By.xpath("//*[@id='ph_name']") , sitePhName , 2000);
		mySendKeys(driver, By.xpath("//*[@id='ph_value']"), sitePhValue, 2000);	
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Placeholder.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successfull to save new placeholder: " + sitePhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
		
		// Verify the create
	    myDebugPrinting("Verify the create", testVars.logerVars.MINOR);  
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), sitePhName , 10000);
		searchStr(driver, "%ITCS_" + sitePhName + "%");
		searchStr(driver, sitePhValue);
		searchStr(driver, sitePHSite);
		searchStr(driver, siteTenant);	
	  }
	  
	  /**
	  *  Edit a Site-PH with given variables
	  *  @param driver         - given element
	  *  @param sitePhName     - given Site-ph name
	  *  @param sitePhNewValue - new given Site-ph value
	  *  @throws IOException 
	  */
	  public void editSitePH(WebDriver driver, String sitePhName, String sitePhNewValue) throws IOException {
		  
		// Get idx for edit
		myDebugPrinting("Get idx for edit", testVars.logerVars.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), " " , 6000);
		BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		String l = null;
		int i = 1;
		while ((l = r.readLine()) != null) {
			
			myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
			if (l.contains(sitePhName)) {
						  
				myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				break;
			}
			if (l.contains(" %ITCS_" )) {
					++i;
			}
		}
		
//		// Check if the current user is "Monitoring" if so - edit should fail
//		myDebugPrinting("Check if the current user is \"Monitoring\" if so - edit should fail", testVars.logerVars.NORMAL);  
//		if (sitePhName.contains("tenMonitPhName")) {
//				
//			String editButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]")).getAttribute("class");
//			myAssertTrue("Edit button is not deactivated !!\neditButton - " + editButton, editButton.contains("not-active"));
//			return;
//		}
		    
		myDebugPrinting("xpath - " + "//*[@id='sites1']/tbody[1]/tr[" + i + "]/td[7]/button[1]", testVars.logerVars.DEBUG);  
		myClick(driver, By.xpath("//*[@id='sites1']/tbody[1]/tr[" + i + "]/td[7]/button[1]"), 5000);
		
		// Fill data
		myDebugPrinting("Fill data", testVars.logerVars.MINOR);  
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Edit placeholder");
		mySendKeys(driver, By.xpath("//*[@id='ph_value']"), sitePhNewValue, 2000);
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Placeholder.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successfull to update placeholder: " + sitePhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
			
		// Verify the create
	    myDebugPrinting("Verify the create", testVars.logerVars.MINOR);  
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), sitePhName , 6000);
		verifyStrByXpath(driver, "//*[@id='sites1']/tbody[1]/tr[2]/td[3]", "%ITCS_" + sitePhName + "%");
		verifyStrByXpath(driver, "//*[@id='sites1']/tbody[1]/tr[2]/td[4]", sitePhNewValue);
	  }
	  
	  /**
	  *  Delete a Site-PH with given variables
	  *  @param  driver      - given element
	  *  @param  sitePhName  - given Site-PH name
	  *  @param  sitePhValue - given Site-PH value
	  *  @param  sitePhSite	 - given Site-PH site
	  *  @throws IOException 
	  */  
	  public void deleteSitePH(WebDriver driver, String sitePhName, String sitePhValue, String sitePhSite) throws IOException {
		  
	    // Get idx for delete
		myDebugPrinting("Get idx for delete", testVars.logerVars.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), " " , 6000);
		BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		String l = null;
		int i = 1;
		while ((l = r.readLine()) != null) {
			
			myDebugPrinting("i - " + i, testVars.logerVars.DEBUG);
			myDebugPrinting(l			, testVars.logerVars.DEBUG);  
			if (l.contains(sitePhName)) {
				  
				myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				break;
			}
			if (l.contains(" %ITCS_" )) {
				  
				  ++i;
			}
		}
		
//		// Check if the current user is "Monitoring" if so - delete should fail
//	    myDebugPrinting("Check if the current user is \"Monitoring\" if so - delete should fail", testVars.logerVars.NORMAL);  
//		if (sitePhName.contains("tenMonitPhName") && !sitePhValue.contains("Forced")) {
//			
//			String deleteButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[2]")).getAttribute("class");
//			myAssertTrue("Delete button is not deactivated !!\ndeleteButton - " + deleteButton, deleteButton.contains("not-active"));
//			return;
//		}
		
		// Delete PH
		myDebugPrinting("Delete PH", testVars.logerVars.NORMAL);
		myDebugPrinting("xpath - " +  "//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[7]/button[2]", testVars.logerVars.NORMAL);
		myClick(driver, By.xpath("//*[@id='sites1']/tbody[1]/tr[" + i + "]/td[7]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Site Placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this site placeholder?:" + sitePhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Site Placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The selected " + sitePhName + " placeholder has been successfully removed from " + sitePhSite);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
		
		// Verify delete
		myDebugPrinting("Verify delete", testVars.logerVars.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), sitePhName , 6000);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		myAssertTrue("Tenant-PH name still exist !!\nbodyText - "  + bodyText, !bodyText.contains(sitePhName));
	  }
	  
	  /**
	  *  Delete a Site Configuration-key by given variables
	  *  @param  driver      - given element
	  *  @param  cfgKeyName  - given configuration name
	  *  @param  cfgKeyValue - given configuration value
	  *  @param  currTenant	 - given configuration tenant
	  *  @param  currSite    - given configuration site
	  *  @throws IOException 
	  */
	  public void deleteSiteCfgKey(WebDriver driver, String cfgKeyName, String cfgKeyValue, String currTenant, String currSite) throws IOException {
		  		    
		  // Select site
		  myDebugPrinting("Select site", testVars.logerVars.MINOR);	
		  selectSite(driver, currSite);				
		  
		  // Get idx
		  myDebugPrinting("Get idx", testVars.logerVars.MINOR);
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  Boolean countLines = false;
		  while ((l = r.readLine()) != null) {
					
			  myDebugPrinting("i - " + i + " " + l, testVars.logerVars.DEBUG);
			  if (l.contains(cfgKeyName)) {
						  
				myDebugPrinting("i - " + i, testVars.logerVars.MINOR);
				break;
			  } else if (countLines) {
				
				i++;
			  } else if (l.contains("Configuration Key Configuration Value")) {
				countLines = true;
			  }
		  }
		  if (l == null) {
			  
			  myFail("Configuration key was not found !!");			  
		  }
		  
		  // Delete key
		  myDebugPrinting("Delete key", testVars.logerVars.MINOR);	  
		  myClick(driver, By.xpath("//*[@id='table_keys']/tbody/tr[" + i + "]/td[3]/div/a/i"), 4000);
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration setting");
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + cfgKeyName + " from the configuration settings?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000); 
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']", currSite);
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");  
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
				  
		  // Verify delete
		  myDebugPrinting("Verify delete", testVars.logerVars.MINOR);	  
		  String txt = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("Delete did not succeeded !!\ntxt - " + txt,  !txt.contains(cfgKeyName));
		  myAssertTrue("Delete did not succeeded !!\ntxt - " + txt,  !txt.contains(cfgKeyValue));
	  }
	  
	  /**
	  *  Add a new site CFG key according to given data
	  *  @param driver       - given element
	  *  @param cfgKeyName   - given configuration name
	  *  @param cfgKeyValue  - given configuration value
	  *  @param currTenant	 - given configuration tenant
	  *  @param currSite     - given configuration site
	  *  @throws IOException 
	  */
	  public void addNewSiteCfgKey(WebDriver driver, String cfgKeyName, String cfgKeyValue, String currTenant, String currSite) {  
		  
		  // Select site
		  myDebugPrinting("Select site", testVars.logerVars.MINOR);	  
		  selectSite(driver, currSite); 
		  
		  // Select key, set data and submit
		  myDebugPrinting("Select key, set data and submit", testVars.logerVars.MINOR);	  
		  mySendKeys(driver, By.xpath("//*[@id='ini_name']"), cfgKeyName  , 2000);  
		  mySendKeys(driver, By.xpath("//*[@id='ini_value']"), cfgKeyValue, 2000);  	  
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a"), 3000);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + currSite + " )");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successfull to save Tenant configuration.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
		  
		  // verify create
		  myDebugPrinting("verify create", testVars.logerVars.MINOR);
		  searchStr(driver, cfgKeyName);
		  searchStr(driver, cfgKeyValue);
	  }
	  
	  /**
	  *  Select a site in Site configuration menu
	  *  @param driver   - a given driver
	  *  @param siteName - a given Site name
	  **/
	  public void selectSite(WebDriver driver, String siteName) {
		  
		  myDebugPrinting("Select site", testVars.logerVars.MAJOR);
		  Select currentSite = new Select(driver.findElement(By.xpath("//*[@id='site_id']")));
		  currentSite.selectByVisibleText(siteName);    
		  myWait(5000);
	  }
	  
	  /**
	  *  Select a tenant in Tenant configuration menu
	  *  @param driver  - a given driver
	  *  @param tenName - a given Tenant name
	  **/
	  public void selectTenant(WebDriver driver, String tenName) {
		  
		  myDebugPrinting("Select tenant", testVars.logerVars.MAJOR);
		  Select currentSite = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
		  currentSite.selectByVisibleText(tenName);    
		  myWait(5000);
	  }
	  
	  /**
	  *  Search for an Alert using given detector
	  *  @param driver          - given driver
	  *  @param searchMode      - given searchMode (I.e. description, severirty)
	  *  @param alertData       - search criteria
	  *  @param alertsForSearch - array of alert descriptions for search
	  **/
	  public void searchAlarm(WebDriver driver, String searchMode, String alertData, String []alertsForSearch) {
		  
		  // Enter the filter menu & clear it before a new search
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div/div[2]/a"), 3000);
		  myClick(driver, By.xpath("//*[@id='searchForm']/div[9]/div/button[2]")		, 3000);
		  switch (searchMode) {
			  case "Description":	
				  myDebugPrinting("Search according to description - " + alertData, testVars.logerVars.MINOR);
				  mySendKeys(driver, By.xpath("//*[@id='inputDescription']"), alertData, 2000);
				  break;
				  
			  case "Severity":	
				  myDebugPrinting("Search according to Severity", testVars.logerVars.MINOR);	  
				  new Select(driver.findElement(By.xpath("//*[@id='inputStatus']"))).selectByVisibleText(alertData);
				  myWait(5000);
				  break;
				  
			  case "Tenant":	
				  myDebugPrinting("Search according to Tenant", testVars.logerVars.MINOR);	  
				  new Select(driver.findElement(By.xpath("//*[@id='inputTenant']"))).selectByVisibleText(alertData);		
				  myWait(5000);
				  break;  
				  
		  }
		  myClick(driver, By.xpath("//*[@id='searchForm']/div[9]/div/button[1]"), 5000);

		  // Search for alerts
		  for (int i = 0; i < alertsForSearch.length; ++i) {
			
			  myDebugPrinting("alertsForSearch[i] - " + alertsForSearch[i], testVars.logerVars.MINOR);
			  if (alertData.contains("unknownMac")) {
			  
				  searchStr(driver, "There are no devices that fit this search criteria");  	  
			  } else {
				  
				  searchStr(driver, alertsForSearch[i]);  
			  }	  
		  }
	  }  
	  
	  /**
	  *  Delete an alarm from Alarms table by a given parameters
	  *  @param driver    - given driver
	  *  @param alertDesc - alert description
	  **/
	  public void deleteAlarm(WebDriver driver, String alertDesc) {
		  		  
		  // Search for the alert according to description
		  myDebugPrinting("Search for the alert according to description", testVars.logerVars.MINOR);
		  String[] alertsForSearch = {alertDesc};
		  searchAlarm(driver, "Description", alertDesc, alertsForSearch); 
		   
		  // Delete the alarm
		  myDebugPrinting("Delete the alarm", testVars.logerVars.MINOR);
		  myClick(driver, By.xpath("//*[@id='dl-menu']/a")		   , 3000);
		  myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[1]/a"), 3000);
		  verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[1]", "Delete");
		  verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]", "Are you sure you want to delete this IPP?");
		  myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 5000);
		  
		  // Verify delete
		  myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
		  String bodyText     = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("String is not detcetd !!", bodyText.contains("There are no devices that fit this search criteria"));	  
	  }
	  
	  /**
	  *  Delete all configuration values at tenant menu
	  *  @param driver     - given driver
	  *  @param prefix 	   - prefix for searched values (if empty - verify of delete is not needed)
	  *  @param currTenant - tenant from whom we delete the values
	  **/
	  public void deleteAllConfValues(WebDriver driver, String prefix, String currTenant) {

		  // Delete all Configuration values
		  myDebugPrinting("Delete all Configuration values", testVars.logerVars.NORMAL);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/button")    , 3000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/ul/li[1]/a"), 3000);
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration settings");
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + currTenant + " )");
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);

		  // verify delete if prefix is not empty
		  if (!prefix.isEmpty()) {
			  
			  myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
			  String txt = driver.findElement(By.tagName("body")).getText();		  
			  myAssertTrue("All tenant configuration values were not delete successfully !!\ntxt - " + txt, !txt.contains(prefix));
		  }
	  }

	  /**
	  *  Send a keep-alive packet for a pre-created device
	  *  @param kpAlveBatName - given batch name that wraps and sends the packet
	  *  @param ip 	   		  - ip of the OVOC
	  *  @param port		  - port of the OVOC for send the keep alive packet
	  *  @param macAddress	  - MAC address of pre-created device
	  *  @param deviceName	  - Name of the device we want to update
	  *  @param phoneModel	  - Name of the phone model we want to update
	  *  @param domain		  - Domain of the OVOC
	  *  @param devStatus	  - Status of device we want to update
	  *  @param location	  - Name of the location we want to update
	  *  @param phoneNumber	  - Name of the phone-number we want to update
	  **/
	  public void sendKeepAlivePacket(String kpAlveBatName, String ip		   , String port      ,
									String macAddress	, String deviceName, String phoneModel,
									String domain		, String devStatus , String location  ,
									String phoneNumber) throws IOException {

	 	myDebugPrinting("kpAlveBatName - " + System.getProperty("user.dir") + "\\" + testVars.getKpAlveBatName(), testVars.logerVars.MINOR);
	 	Process process = new ProcessBuilder(System.getProperty("user.dir") + "\\" + testVars.getKpAlveBatName(), 
				 ip  	   ,
				 port	   ,
				 macAddress,
				 deviceName   ,
				 phoneModel    ,   
				 domain  ,
				 devStatus ,
				 location    ,
				 phoneNumber).start();
	    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line;
	    while ((line = br.readLine()) != null) {

	    	myDebugPrinting(line, testVars.logerVars.MINOR);
	    }
	}
}