package EMS_Tests;


/**
* This class holds all the data which is used by the tests
* @author Nir Klieman
* @version 1.00
*/


// TODO add method for increase the speed of IE


public class GlobalVars {
	
	/**
	*  url  		- Default url for access the system
	*  selfRegUrl   - Default url for access the Self-reg url
	*  sysUsername  - Default username for access the system
	*  sysPassword  - Default password for access the system
	*  sysMainStr   - Default string in the welcome page (used for verify access)
	*  globalSleep  - Default value for sleep between parts of the tests
	*  testStrings  - Default class that contain all the strings of the system
	*  custUsername  - Default username for access the system via Customer
	*  custPassword  - Default password for access the system via Customer
	*  
	*/
    private  String url  		    = "10.21.8.30/ipp/admin/logout.php";
    private  String sysUsername     = "nir";
	private  String sysPassword     = "1q2w3e4r5t";
    private  String custUsername    = "nirr53@gmail.com";
	private  String custPassword    = "1q2w3e$r";
	private  String sysMainStr      = "IP Phone Management Server";
	private  String sysInvalidStr   = "Wrong username or password";
	private  String chromeDrvPath  = "C:\\Users\\nirk\\Desktop\\Selenium\\chromedriver_win32\\chromedriver.exe";
	private  String firefoxDrvPath = "C:\\Users\\nirk\\Desktop\\Selenium\\IEDriverServer_x64_2.53.1\\IEDriverServer.exe";	
	private  int    globalSleep    = 2000;
	GlobalStrings	testStrings;

	
	/**
	*  CHROME  	- Default value for CHROME browser
	*  FIREFOX  - Default value for Firefox browser
	*  IE  		- Default value for Internet explorer browser
	*/
	public static int CHROME  = 0;
	public static int FIREFOX = 1;
	public static int IE 	  = 2;
	
    /**
    *  Non-default constructor for provide another data
    *  @param _url  	Non-default url for access the system
	*  @param _username Non-default username for access the system
	*  @param _password Non-default password for access the system
	*  @param _mainStr  Non-default string for verify good access
    */
	public GlobalVars(String _url, String _username, String _password, String _mainStr) {
		
		System.out.println("GlobalVars constructor is called");
		this.url 		 = _url;
		this.sysUsername = _username;
		this.sysPassword = _password;
		this.sysMainStr  = _mainStr;
		testStrings      = new GlobalStrings();
	}
	
    /**
    *  Default constructor for provide interface
    */
    public GlobalVars() {
	}
    
	/**
    *  Default method for return the url variable
    *  @return url of the system
    */
	public String getUrl(){
		return this.url;
	}
		
    /**
    *  Default method for return the Customer username variable
    *  @return username of the system for Customer
    */
	public String getCustUsername(){
		return this.custUsername;
	}
	
    /**
    *  Default method for return the Customer password variable
    *  @return password of the system for Customer
    */
	public String getCustPassword(){
		return this.custPassword;
	}

	
    /**
    *  Default method for return the username variable
    *  @return username of the system for Admin
    */
	public String getSysUsername(){
		return this.sysUsername;
	}
	
    /**
    *  Default method for return the password variable
    *  @return password of the system for Admin
    */
	public String getSysPassword(){
		return this.sysPassword;
	}
	
    /**
    *  Default method for return the main-str variable
    *  @return sysStr of the system
    */
	public String getSysMainStr(){
		return this.sysMainStr;
	}
	
    /**
    *  Default method for return the invalid-str variable
    *  @return sysInvalidStr of the system
    */
	public String getSysInvalidStr(){
		return this.sysInvalidStr;
	}
	
    /**
    *  Default method for return the global sleep value
    *  @return globalSleep of the system
    */
	public int getGlobalSleep(){
		return this.globalSleep;
	}

    /**
    *  Default method for return the Chrome driver path
    *  @return chromeDrvPath of the system
    */
	public String getchromeDrvPath(){
		return this.chromeDrvPath;
	}
	
    /**
    *  Default method for return the Firefox driver path
    *  @return firefoxDrvPath of the system
    */
	public String getfirefoxDrvPath(){
		return this.firefoxDrvPath;
	}

	
}


