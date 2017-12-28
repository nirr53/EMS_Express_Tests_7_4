package EMS_Tests;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class GlobalStrings {
	
	
    /**
    *  @param map  	Map that contains all the string in the system
    */
	private Map<String, String> map;
	
    /**
    *  Default constructor that create the map with all the needed strings
    */
	public GlobalStrings() {
		
		//System.out.println("GlobalStrings constructor is called");
		this.map = new HashMap<String, String>();
		map.put("test71Hebrew1", "צחי נוסבאום, ראש חטיבת תקשורת ו-IT, תאגיד ISCAR העולמי");	
		map.put("test72Hebrew1", "Let's get started!");
		map.put("test72Hebrew2", "Some basic details and we're good to go:");
		map.put("test72Hebrew3", "Full name");
		map.put("test72Hebrew4", "Company email");		
		map.put("test72Hebrew5", "Mobile phone");	
		map.put("test72Hebrew6", "I agree to the VocaNOM Terms & Conditions");
		map.put("test72Hebrew7", "VocaNOM Terms & Conditions");	
		map.put("test72Hebrew8", "Already joined the VocaNOM Online Activation Tool? Login Here!");	
		map.put("test73Hebrew1", "VocaNOM: ניתוב שיחות ארגוניות לעובדים ולקוחות בחיוג קולי");
		map.put("test73Hebrew2", "יעילות ארגונית וחסכון בזמן");
		map.put("test73Hebrew3", "התחברות וסנכרון לכל סוגי המרכזיות");
		map.put("test73Hebrew4", "נתוני שיחות בזמן-אמת");
		map.put("test411English1", "Selfreg Userdata");	
		map.put("test411English2", "Selfreg Userdata");
		map.put("test411English3", "Full Name");	
		map.put("test411English4", "Company");
		map.put("test411English5", "Role");	
		map.put("test411English6", "Company Email");	
		map.put("test411English7", "Mobile Number");
		map.put("test411English8", "Company Size");	
		map.put("test411English9", "Country");
		map.put("test411English10", "Invite Email");	
		map.put("test411English11", "IP Address");
		map.put("test411English12", "Updated");
		map.put("test411English13", "Export CSV");	

	    

		
		
	}
	
    /**
    *  Function for build the used strings prefix.
    *  The prefix is built from <class name> + <used language>
    *  @param  driver  	   Used driver
	*  @param  className   Used class-name (from calling class)
	*  @param  xpath 	   Xpath of the list that control on the used language
	*  @return prefixname  Prefix name
    */
	
	public String getStringsPrefix(WebDriver driver, String className, String xPath) {
		
		
		System.out.println("Enter getStringsPrefix()");
		String currLang = driver.findElement(By.xpath(xPath)).getText();
		System.out.println("currLang - " + currLang);
		String prefixName = className.substring(0, className.indexOf("__")) + currLang;
		System.out.println("prefixName - " + prefixName);
		return prefixName;
	}
	
    /**
    *  Function for return values from the strings map.
    *  @param  key     Map's key
	*  @return String  Map's value (if not found, an error is raised)
    */
	public String getMapData(String key){
		
		System.out.println("key - <" + key + ">");
		String temp = this.map.get(key);
	    if (temp != null) {
	    	return temp;
	    	
	    } else {
	    	return "";
	    }
		
	}
}
