package EMS_Express_Tests_7_4;

public class MenuPaths {
	
	// Setup section
	private String SETUP_SECTION 						 = "//*[@id='navbar-collapse']/ul[1]/li[1]/a";
	private String SETUP_USERS_DEVICES_MANAGE_MULTI_USRS = "//*[@id='left-nav']/ul/li[2]/ul/li[2]/a";
	private String SETUP_USERS_DEVICES_MANAGE_MULTI_DVCS = "//*[@id='left-nav']/ul/li[2]/ul/li[3]/a";
	private String SETUP_PHONE_CONF_SECTION 			 = "//*[@id='left-nav']/ul/li[2]/a";
	private String SETUP_PHONE_CONF_TEMPLATES 			 = "//*[@id='left-nav']/ul/li[3]/ul/li[1]/a";
	private String SETUP_PHONE_CONF_SYS_SETTINGS	 	 = "//*[@id='left-nav']/ul/li[3]/ul/li[2]/a";
	private String SETUP_PHONE_CONF_DHCP_OPTIONS_CONFIG	 = "//*[@id='left-nav']/ul/li[3]/ul/li[3]/a";
	private String SETUP_PHONE_CONF_LDAP_SETTINGS	 	 = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[1]";	
	private String SETUP_PHONE_CONF_SBC_SETTINGS	 	 = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[3]";	
	private String SETUP_PHONE_CONF_TEMPLATES_PHS 		 = "//*[@id='left-nav']/ul/li[3]/ul/li[4]/a";
	private String SETUP_PHONE_CONF_TENANT_CONFIGURATION = "//*[@id='left-nav']/ul/li[3]/ul/li[5]/a";
	private String SETUP_PHONE_CONF_PHONE_CONF_FILES 	 = "//*[@id='left-nav']/ul/li[3]/ul/li[6]/a";
	private String SETUP_PHONE_CONF_PHONE_FIRMWARE_FILES = "//*[@id='left-nav']/ul/li[3]/ul/li[7]/a";	
	private String SETUP_IMPORT_EXPORT_SECTION 			 = "//*[@id='left-nav']/ul/li[4]/a";	
	private String SETUP_IMPORT_EXPORT_CONF_IMPORT	 	 = "//*[@id='left-nav']/ul/li[4]/ul/li[1]/a";
	private String SETUP_IMPORT_EXPORT_CONF_EXPORT 		 = "//*[@id='left-nav']/ul/li[4]/ul/li[2]/a";
	private String SETUP_IMPORT_EXPORT_USRS_DVCS_IMPORT	 = "//*[@id='left-nav']/ul/li[4]/ul/li[3]/a";
	private String SETUP_IMPORT_EXPORT_USRS_DVCS_EXPORT  = "//*[@id='left-nav']/ul/li[4]/ul/li[4]/a";
	private String SETUP_SYSTEM_SECTION 				 = "//*[@id='left-nav']/ul/li[5]/a";
	private String SETUP_SYSTEM_LICENSE 				 = "//*[@id='left-nav']/ul/li[5]/ul/li[2]/a";
	private String SETUP_SYSTEM_TENANTS				 	 = "//*[@id='left-nav']/ul/li[5]/ul/li/a";
	
	// Mainpage section
	private String MAINPAGE_ALERTS_BUTTON 				 = "//*[@id='left-nav']/ul/li/ul/li[3]/a";
	private String MAINPAGE_USER_DETAILS 				 = "//*[@id='navbar-collapse']/ul[3]/li[3]/a";
	private String MAINPAGE_USER_LOGOUT_BUTTON 			 = "/html/body/div[2]/span[2]/a/button";
	private String MONITOR_MAINPAGE_USER_LOGOUT_BUTTON 	 = "/html/body/div[3]/span[2]/a/button";

	// Monitor section
	private String MONITOR_SECTION 						 = "//*[@id='navbar-collapse']/ul[1]/li[2]/a";
	private String MONITOR_DEVICE_STATUS_MENU 			 = "//*[@id='left-nav']/ul/li/ul/li[2]/a/span[1]";
	
	// Troubleshoot section
	private String TROUBLESHOOT_SECTION 				 = "//*[@id='navbar-collapse']/ul[1]/li[3]/a";
	private String TROUBLESHOOT_WEB_ADMIN_LOGS_MENU		 = "//*[@id='left-nav']/ul/li/ul/li[1]/a/span[1]";
	
	// Main Switch-Case function
	public String[] getPaths(String menuName) {
		
		String[] paths = {"", "", "", ""};
		switch (menuName) {
		
			// -----------------
			//
			// Mainpage section
			//
			// -----------------
			case "Dashboard_Alarms":
            	paths[0] = MAINPAGE_ALERTS_BUTTON;
            	break;     	
			case "General_Informatiom_logout":
            	paths[0] = MAINPAGE_USER_DETAILS;
            	paths[1] = MAINPAGE_USER_LOGOUT_BUTTON;
            	break;
            	
			case "Monitoring_General_Informatiom_logout":
            	paths[0] = MAINPAGE_USER_DETAILS;
            	paths[1] = MONITOR_MAINPAGE_USER_LOGOUT_BUTTON;
            	break;       	
            	            	
    		// -----------------
            //
            // Setup section
           	//
    		// -----------------
            	
			case "Setup_Manage_users":
	            paths[0] = SETUP_SECTION;
	            break;         
            	
            // Users Devices menu
			case "Setup_Manage_multiple_users":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_USERS_DEVICES_MANAGE_MULTI_USRS;
	            break;
			case "Setup_Manage_multiple_devices":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_USERS_DEVICES_MANAGE_MULTI_DVCS;
	            break;
	            
	        // Phone Configuration menus
			case "Setup_Phone_conf_section":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            break; 
			case "Setup_Phone_conf_templates":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_TEMPLATES;
	            break;  
			case "Setup_Phone_conf_system_settings":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_SYS_SETTINGS;
	            break;    
			case "Setup_Phone_conf_dhcp_options_configuration":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_DHCP_OPTIONS_CONFIG;
	            break;	
			case "Setup_Phone_conf_system_settings_sbc_conf":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_SYS_SETTINGS;
	            paths[3] = SETUP_PHONE_CONF_SBC_SETTINGS;            
	            break;	
			case "Setup_Phone_conf_system_settings_ldap":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_SYS_SETTINGS;
	            paths[3] = SETUP_PHONE_CONF_LDAP_SETTINGS;            
	            break;	
			case "Setup_Phone_conf_templates_placeholders":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_TEMPLATES_PHS;
	            break;                      
			case "Tenant_configuration":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_TENANT_CONFIGURATION;
	            break;    
//			case "Site_configuration":
//	            paths[0] = SETUP_SECTION;
//	            paths[1] = SETUP_PHONE_CONF_SECTION;
//	            paths[2] = SETUP_PHONE_CONF_SITE_CONFIGURATION;
//	            break;      
//			case "Setup_user_configuration":
//	            paths[0] = SETUP_SECTION;
//	            paths[1] = SETUP_PHONE_CONF_SECTION;
//	            paths[2] = SETUP_PHONE_CONF_USER_CONFIGURATION;
//	            break;    
//			case "Setup_Phone_conf_phone_device_placeholders":
//	            paths[0] = SETUP_SECTION;
//	            paths[1] = SETUP_PHONE_CONF_SECTION;
//	            paths[2] = SETUP_PHONE_CONF_DEVICE_PLACEHOLDERS;
//	            break;            
			case "Setup_Phone_conf_phone_configuration_files":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_PHONE_CONF_FILES;
	            break;
			case "Setup_Phone_conf_phone_firmware_files":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_PHONE_FIRMWARE_FILES;
	            break;
	            
		    // Import export menus
			case "Setup_Import_export_configuration_import":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_CONF_IMPORT;
	            break; 
			case "Setup_Import_export_configuration_export":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_CONF_EXPORT;
	            break; 
			case "Setup_Import_export_users_devices_import":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_USRS_DVCS_IMPORT;
	            break; 
			case "Setup_Import_export_users_devices_export":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_USRS_DVCS_EXPORT;
	            break;
	            
		    // System menus            
	        case "Setup_System_section":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_SYSTEM_SECTION;
	            break;
	        case "Setup_System_license":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_SYSTEM_SECTION;
	            paths[2] = SETUP_SYSTEM_LICENSE;
	            break; 
	        case "Setup_System_tenants":
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_SYSTEM_SECTION;
	            paths[2] = SETUP_SYSTEM_TENANTS;
	            break;             
	            
	    	// -----------------
	        //
	        // Monitor section
	        //
	   		// -----------------
			case "Monitor_device_status":
		            paths[0] = MONITOR_SECTION;
		            paths[1] = MONITOR_DEVICE_STATUS_MENU;
		            break;
		            
			// -----------------
			//
			// Troubleshoot section
			//
			// -----------------
		    case "Troubleshoot_system_diagnostics":		            
		    	paths[0] = TROUBLESHOOT_SECTION;				            
		    	break;	    	
		    case "Troubleshoot_web_admin_logs_menu":		            
		    	paths[0] = TROUBLESHOOT_SECTION;				            
		    	paths[1] = TROUBLESHOOT_WEB_ADMIN_LOGS_MENU;				            
		    	break;
	            
	         default:
	        	 GlobalFuncs testFuncs = new GlobalFuncs();
	             testFuncs.myFail("Menu name is not recognized !!");    
			}
		
		return paths;      
     }
}
