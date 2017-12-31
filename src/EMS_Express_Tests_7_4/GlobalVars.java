package EMS_Express_Tests_7_4;

import static org.junit.Assert.fail;

/**
* This class holds all the data which is used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalVars {
	
	/**
	*  // General data 
	*  ip             - Ip of the system
	*  port           - port for create the users (i.e. 8081)
	*  domain         - domain for create new users (i.e. onebox3.com)
	*  url  		  - Default url for access the system (created by ip)
	*  chromeDrvPath  - Chrome driver path
	*  ieDrvPath      - IE driver path
	*  geckoPath	  - Gecko path (marionate for FF)
	*  version    	  - EMS ipp version (i.e 7.4.XXXX)
	*  downloadsPath  - Downloads path
	*  exportAlarms   - Name of the alarms page
	*  sysLogsPrefix  - Prefix of the logs of the system
	*  sysLogsActPre  - Prefix of the logs of the system (Activity)
	*  shFilesFiName  - Name of sherd-files
	*  
	*  // Login data
	*  mainPageStr    - Main page welcome string (used for detect good login)
	*  sysUsername    - Default username for access the system
	*  sysPassword    - Default password for access the system
	*  sysMainStr     - Default string in the welcome page (used for verify access)
	*  sysInvalidStr  - Default string for detect invalid login
	*  
	*  // Create user via POST data
	*  crUserBatName  - Name of the batch file that call the exe that create the users via POST queries
	*  crAlrmsBatName - Name of the batch file that call the exe that sends alarms to the system via POST queries
	*  acMacPrefix    - Audicocodes MAC prefix
	*  defTenant	  - Default Tenant
	*  defTemplates	  - Default Templates
	*  defSite		  - Default Site
	*  defPhoneModel  - Default phone model
	*  
	*  // Source-files names
	*  srcImpConf	  - Source File name for import configuration
	*  srcImpUser  	  - Source File name for import users
	*  srcImpUserDvcs - Source File name for import users + devices
	*  srcImp1000Usrs - Source File name for import 1000 users
	*  srcConfFile    - Source File name for import a configuration
	*  srcFirmFile	  - Source File name for import a firmware
	*  srcBigCfgFile  - Source File name for import a big configuration
	*  srcOperImport1 - Source File name for import users via Operation user
	*  srcOperImport2 - Source File name for import users via Operation user
	*  srcLangsFile   - Source File name for import users in different langueges
	*  srcDhcpCnfFile - Source File name for import DHCP configuration file
	*  srcSbcConfFile - Source File name for import SBC configuration file
	*  
	*  // Browsers names
	*  CHROME 		  - name of Chrome browser
	*  FF 			  - name of Firefox browser
	*  IE		      - name of Internet-Explorer browser
	*/
    public   LogVars 				logerVars;
	private  String ip               = "10.21.8.8";
	private  String port             = "80";
	private  String domain           = "cloudbond365b.com";
    private  String url  		     = ip + "/ipp/admin/AudioCodes_files/login.php";
    private  String crUserBatName    = "createUsersViaPost.exe"; 
    private  String crAlrmsBatName   = "alertEngine.exe";
    private  String crKpAlvBatName   = "changeStatus.exe";
    private  String mainPageStr		 = "IP Phone Manager Express";
    private  String sysUsername      = "Admin";
	private  String sysPassword      = "Admin";
	private  String sysMainStr       = "NETWORK TOPOLOGY";
	private  String failLogMainStr   = "Invalid credentials";
	private  String sysInvalidStr    = "Wrong username or password";
	private  String chromeDrvPath    = "C:\\Users\\nirk\\Desktop\\Selenium\\chromedriver_win32_2\\chromedriver.exe";
	private  String ieDrvPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\IEDriverServer_x64_2.53.1\\IEDriverServer.exe";	
	private  String geckoPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\geckodriver-v0.11.1-win64\\geckodriver.exe";
	private  String srcFilesPath     = "C:\\Users\\nirk\\Desktop\\myEclipseProjects\\EMS_Tests_7_4\\sourceFiles\\EMS";	
	private  String acMacPrefix      = "00908f";	
	private  String defTenant      	 = "Nir";
	private  String defTemplates[]   = {"NirTemplate", "NirTemplate2"};
	private  String defSite			 = "AutoDetection";
	private  String nonDefTeanants[] = {"NirTest1", "NirTest2"};
	private  String defPhoneModel    = "420HD";
	private  String version			 = "7.4.5.43273";
	private  String downloadsPath    = "";
	private  String exportAlarms     = "ExportAlarmsStatus.csv";
	private  String sysLogsPrefix    = "IPP.Manage.Web.Admin.";
	private  String sysLogsActPre    = "IPP.Manage.Activity.";
	private  String shFilesFiName    = "Files.zip";
	private  String srcImpConf       = "Configuration.zip";
	private  String srcImpUserDvcs   = "users.zip";
	private  String srcImp1000Usrs   = "users1000.zip";
	private  String srcConfFile      = "my.cfg";
	private  String srcFirmFile      = "430HD.img";
	private  String srcBigCfgFile    = "templateVeryBig.cfg";
	private  String srcOperImport1   = "operImpUsersDevcs.zip";
	private  String srcOperImport2   = "Test54_2.zip";
	private  String srcLangsFile     = "nirDdifferentLanguages.zip";
	private  String srcDhcpCnfFile   = "dhcp_option_template.cfg";
	private  String srcSbcConfFile   = "proxy_dhcp_option_template.cfg";
	private  String specialCharsSite = "Nir_()'<>/\":*&^%#@!~";
	public   String CHROME  	     = "Chrome";
	public   String FF 			     = "Firefox";
	public 	 String IE			     = "IE";
	private  Object[][] browsersList = {{CHROME}};

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
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";
    	this.logerVars     = new LogVars();
	}
	
    /**
    *  Default constructor for provide interface
    */
    public GlobalVars() {
    	
    	this.logerVars     = new LogVars();
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";  		
	}
    
	/**
    *  Default method for return the url variable
    *  @return url of the system
    */
	public String getUrl()         {  return this.url;             }
	
	/**
    *  Default method for return the mainPageStr variable
    *  @return main string of the system
    */
	public String getMainPageStr() { return this.mainPageStr;      }
	
    /**
    *  Default method for return the username variable
    *  @return username of the system for Admin
    */
	public String getSysUsername() { return this.sysUsername; 	   }
	
    /**
    *  Default method for return the password variable
    *  @return password of the system for Admin
    */
	public String getSysPassword() { return this.sysPassword;      }
	
    /**
    *  Default method for return the main-str variable
    *  @return sysStr of the system
    */
	public String getSysMainStr() { return this.sysMainStr;        }
	
    /**
    *  Default method for return the main-str fail-login variable
    *  @return sysStr of the system
    */
	public String getFailLogStr() { return this.failLogMainStr;    }
	
    /**
    *  Default method for return the invalid-str variable
    *  @return sysInvalidStr of the system
    */
	public String getSysInvalidStr() { return this.sysInvalidStr;  }

    /**
    *  Default method for return the Chrome driver path
    *  @return chromeDrvPath of the system
    */
	public String getchromeDrvPath() { 	return this.chromeDrvPath; }
	
    /**
    *  Default method for return the IE driver path
    *  @return ieDrvPath of the system
    */
	public String getIeDrvPath()     { return this.ieDrvPath;      }
	
    /**
    *  Default method for return the path for the directory of the source files
    *  @return srcFilesPath of the system
    */
	public String getSrcFilesPath() {  return this.srcFilesPath;   }
	
    /**
    *  Default method for return the System IP
    *  @return ip
    */
	public String getIp() 			{ return this.ip;			   }
	
    /**
    *  Default method for return the System Port
    *  @return port
    */
	public String getPort()        { return this.port;			   }
		
    /**
    *  Default method for return the System Domain
    *  @return port
    */
	public String getDomain() 	   { return this.domain;		   }
	
    /**
    *  Default method for return the AutoIT exe  that creates a user
    *  @return crUserBatName
    */
	public String getCrUserBatName() { return this.crUserBatName;  }
	
    /**
    *  Default method for return the AutoIT exe  that sends an alarm
    *  @return crAlrmsBatName
    */
	public String getAlarmsBatName() { return this.crAlrmsBatName;  }
	
    /**
    *  Default method for return the AC prefix string
    *  @return acMacPrefix
    */
	public String getAcMacPrefix()   { return this.acMacPrefix;    }
	
    /**
    *  Default method for return the default Tenant
    *  @return defTenant
    */
	public String getDefTenant()    { return this.defTenant;       }
	
    /**
    *  Default method for return the default Site
    *  @return defSite
    */
	public String getDefSite()    { return this.defSite;       	   }
	
	
    /**
    *  Default method for return the default phone-model (used mainly for create-user-via-post method)
    *  @return defPhoneModel
    */
	public String getDefPhoneModel() { return this.defPhoneModel;  }
	
    /**
    *  Default method for return the version
    *  @return version
    */
	public String getVersion()       { return this.version;        }
	
    /**
    *  Default method for return the downloads path
    *  @return version
    */
	public String getDownloadsPath() { return this.downloadsPath;  }
	
    /**
    *  Default method for return the alarms export file name
    *  @return exportAlarms
    */
	public String getAlarmsExport() { return this.exportAlarms;    }
	
    /**
    *  Default method for return the prefix of the System-logs
    *  @return sysLogsPrefix
    */
	public String getSysLogsPrefix() { return this.sysLogsPrefix;  }
	
    /**
    *  Default method for return the prefix of the System-logs-Activity
    *  @return sysLogsActPre
    */
	public String getSysLogsActPre() { return this.sysLogsActPre;  }

    /**
    *  Default method for return the used browsers in the current test
    *  @return browsersList
    */
	public Object[][] getBrowsers() 	 { return this.browsersList;   }

    /**
    *  Default method for return the gecko driver path (the extarnal driver for ff)
    *  @return ieDrvPath of the system
    */
	public String getGeckoPath()         { return this.geckoPath;      }
	
    /**
    *  Default method for return the Sharefiles file-name
    *  @return shareFilesFileName
    */
	public String getShareFilesName()    {  return this.shFilesFiName; }
	
    /**
    *  Default method for return the default Template
    *  @return defTemplate
    */
	public String getDefTemplates(int i) {
		
		if (i < 0 || i > (defTemplates.length - 1)) {
			
			fail("Invalid idx");
		}
		return this.defTemplates[i];	
	}
	
    /**
    *  Default method for return ojne of the non-default Tenants
    *  @return nonDefTeanants[i]
    */
	public String getNonDefTenant(int i) {
		
		if (i < 0 || i > (nonDefTeanants.length - 1)) {
			
			fail("Invalid idx");
		}	
		return this.nonDefTeanants[i];
	}	
	
    /**
    *  Default method for return a name of source file by given integer
    *  @param idx Index of the current test in the format of <test>.<sub-test>
    *  @return String that represent name of the used file
    */
	public String getImportFile(String idx) {
		
		String usedSrcFile = "";
		switch (idx) {
		
			case "11":
				 usedSrcFile = this.srcImpConf;
				 break;
			case "12.1":
				 usedSrcFile = this.srcImpUserDvcs;
				 break;	
			case "12.2":
				 usedSrcFile = this.srcImp1000Usrs;
				 break;			 
			case "30":
				 usedSrcFile = this.srcConfFile;
				 break;
			case "31":
				 usedSrcFile = this.srcFirmFile;
				 break;	 
			case "36":
				 usedSrcFile = this.srcBigCfgFile;
				 break;
			case "54.1":
				 usedSrcFile = this.srcOperImport1;
				 break;
			case "54.2":
				 usedSrcFile = this.srcOperImport2;
				 break;
			case "65":
				 usedSrcFile = this.srcLangsFile;
				 break;
			case "66":
				 usedSrcFile = this.srcSbcConfFile;
				 break;
			case "67":
				 usedSrcFile = this.srcDhcpCnfFile;
				 break;
			default:
				usedSrcFile = "";
				break;
		}
		System.out.println("   usedSrcFile - " + usedSrcFile);	
		return usedSrcFile;
	}

    /**
    *  Default method for return the AutoIT exe  that sends a Keep-alive packet
    *  @return crSndKpAlveBatName
    */
	public String getKpAlveBatName() { return this.crKpAlvBatName;  }

    /**
    *  Default method for return a Site with special characters in its name
    *  @return spCharsSite
    */
	public String getSpecialCharsSite()  { return this.specialCharsSite; }
}