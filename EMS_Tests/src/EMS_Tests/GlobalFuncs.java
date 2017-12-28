package EMS_Tests;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.http.HttpClient;

import junit.framework.Assert;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
* This class holds all the functions which been used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalFuncs {
	
	  /**
	  *  webUrl  		- Default url for the used funcs
	  *  username  	- Default username for the used funcs
	  *  password 	- Default password for the used funcs
	  *  StringBuffer - Default string for errors buffering
	  */
	  GlobalVars 		   testVars;
	  private String 	   username;
	  private String 	   password;
	  private String	   webUrl;
	  @SuppressWarnings("unused")
	  private StringBuffer verificationErrors = new StringBuffer();

	  /**
	  *  Default constructor
	  */
	  public GlobalFuncs() {
		  
		  testVars = new GlobalVars();
		  webUrl   = testVars.getUrl();
		  username = testVars.getSysUsername();
		  password = testVars.getSysPassword();
	  }
	  
	  /**
	  *  Constructor which get data
	  *  @param givenUrl A given url
	  *  @param givenUsername A given username
	  *  @param givenPassword A given password
	  */
	  public GlobalFuncs(String givenUrl, String givenUsername, String givenPassword) {
		 
		  testVars = new GlobalVars();
		  webUrl   = givenUrl;
		  username = givenUsername;
		  password = givenPassword;		 
	  }
	  
	  /**
	  *  login method
	  *  @param driver  A given driver for make all tasks
	  *  @param mainStr A given string for verify good access
	  *  @param httpStr A given string for usign as a prefix for the url
	  */
	  public void login(WebDriver 	driver, String mainStr, String httpStr) {
		  
	      driver.get(httpStr + webUrl);
	      driver.findElement(By.name("auth_admin_user_")).clear();
	      driver.findElement(By.name("auth_admin_password_")).clear();
	      driver.findElement(By.name("auth_admin_user_")).sendKeys(username);
	      driver.findElement(By.name("auth_admin_password_")).sendKeys(password); 
	      driver.findElement(By.xpath("//*[@id='Submit1']")).click();
	      String bodyText = driver.findElement(By.tagName("body")).getText();
	      
	      // Verify good access
	      try {
	    	  assertTrue("Text <" + mainStr + "> not found!", bodyText.contains(mainStr));	
	      } catch (Error e) {
	    	  fail(e.toString());
	      }
	      	      
	   
	  }
	  
	  
	  public void invalidLogin(WebDriver driver, String sysInvalidStr, int i) {

		  
	      driver.get("http://" + webUrl);
	      driver.findElement(By.name("auth_admin_user_")).clear();
	      driver.findElement(By.name("auth_admin_password_")).clear();	      
	      if (i == 0) {	  
		      driver.findElement(By.name("auth_admin_user_")).sendKeys(username.substring(1));
		      driver.findElement(By.name("auth_admin_password_")).sendKeys(password); 
		      
	      } else if (i == 1) {
	    	  
		      driver.findElement(By.name("auth_admin_user_")).sendKeys(username);
		      driver.findElement(By.name("auth_admin_password_")).sendKeys(password.substring(1));
	      
	      } else if (i == 2) {
	      
		      driver.findElement(By.name("auth_admin_user_")).sendKeys(username.substring(1));
		      driver.findElement(By.name("auth_admin_password_")).sendKeys(password.substring(1));
	      }
	      driver.findElement(By.xpath("//*[@id='Submit1']")).click();
	      String bodyText = driver.findElement(By.tagName("body")).getText();
	      
	      // Verify good access
	      try {
	    	  assertTrue("Text <" + sysInvalidStr + "> not found!", bodyText.contains(sysInvalidStr));	
	      } catch (Error e) {
	    	  fail(e.toString());
	      }
		  
		  
		  
	  }
	  
	  
	  
	  /**
	  *  Verify string  method by css
	  *  @param driver    A given driver
	  *  @param elemName  A given element name
	  *  @param strName   A given string for detect
	  */
	  public void verifyStrByCss(WebDriver 	driver, String elemName, String strName) {
	  	  
		markElemet(driver, driver.findElement(By.cssSelector(elemName)));
		try {
	        assertEquals(strName, driver.findElement(By.cssSelector(elemName)).getText());
	        
	    } catch (Error e) {
	  	  fail(e.toString());
	  	  
	    }
	  } 
	 
	  /**
	  *  Verify string  method by xpath
	  *  @param driver    A given driver
	  *  @param elemName  A given element name
	  *  @param strName   A given string for detect
	  */
	  public void verifyStrByXpath(WebDriver 	driver, String elemName, String strName) {
		  
	   markElemet(driver, driver.findElement(By.xpath(elemName)));
	   try {
	        assertEquals(strName, driver.findElement(By.xpath(elemName)).getText());
	        
	    } catch (Error e) {   	
	      System.out.println(Thread.currentThread().getStackTrace()[2].getLineNumber());
	      System.out.println(e.getMessage());
	      System.out.println(e.getLocalizedMessage());
	      fail(e.toString());
	  	  
	    }
	  }
	  
	/**
	*  Highlight given element
	*  @param driver     A given driver
	*  @param element    A given element
	*/
	public void markElemet(WebDriver 	driver, WebElement element) {
			
		// Mark element
	    try {
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e1) {
		}
	   ((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", element);
	}
	
	/**
	*  Sleep for a given time
	*  @param sleepValue     A given sleep factor
	*/
	public void testFuncs.myWait(int sleepValue) {
			
	    try {
			TimeUnit.MILLISECONDS.sleep(sleepValue);
			
		} catch (InterruptedException e1) {
			
		}	
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}