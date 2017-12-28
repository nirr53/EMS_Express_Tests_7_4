package EMS_Express_Tests_7_4;

import org.apache.log4j.Logger;

public class Log {

	//Initialize Log4j logs
	private static Logger Log = Logger.getLogger(Log.class.getName());
	public static void startTestCase(String sTestCaseName) {
		Log.info("+---------------------------------------------------------------------------------------+");
		Log.info("|                                                                                       |");
		Log.info("|   " + sTestCaseName + "    "                                                            );
		Log.info("|                                                                                       |");
		Log.info("+---------------------------------------------------------------------------------------+\n\n");
		
	}
	public static void info(String message)  {
		Log.info(message);
	}
	public static void warn(String message)  {
		Log.warn(message);
	}
	public static void error(String message) {	
		Log.error(message);
	}
	public static void fatal(String message) {
	   Log.fatal(message);
	}
	public static void debug(String message) {
	   Log.debug(message);
	}

}