package misc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
 
/**
 * 
 * 
 */
 
public class ReadPropertyFile {
	String result = "";
	InputStream inputStream;
	FileInputStream fis = null;
 
	public HashMap getPropValues() throws IOException {
 
		
       HashMap<String, String> propertyMap = new HashMap<String, String>();
        
        	 
		
		try {
			Properties prop = new Properties();
			//String propFileName = "radia.properties";
			String propFileName = "/sfdc/radia/radia.properties";
			//String propFileName = "/home/pwang/radia_nightly_data_transfer/radia.properties"; // unix
 
			//inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			fis = new FileInputStream(propFileName);
			if (fis != null) {
				prop.load(fis);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			Date time = new Date(System.currentTimeMillis());
 
			// get the property value and print it out
			String host = prop.getProperty("host").trim();
			String port = prop.getProperty("port").trim();
			String to = prop.getProperty("to").trim();
			String data_failure_to = prop.getProperty("data_failure_to").trim();
			String marketplace_failure_to = prop.getProperty("marketplace_failure_to").trim();
			String sfdc_url = prop.getProperty("sfdc_url").trim();
			String sfdc_username = prop.getProperty("sfdc_username").trim();
			String sfdc_password = prop.getProperty("sfdc_password").trim();
			String sfdc_securitytoken = prop.getProperty("sfdc_securitytoken").trim();
			String ftp_server = prop.getProperty("ftp_server").trim();
			String ftp_userid = prop.getProperty("ftp_userid").trim();
			String ftp_password = prop.getProperty("ftp_password").trim();
			String ftp_remotedirectory = prop.getProperty("ftp_remotedirectory").trim();
			String ftp_localdirectory = prop.getProperty("ftp_localdirectory").trim();
			String ftp_archivedirectory = prop.getProperty("ftp_archivedirectory").trim();
			
			String campaign_379_url = prop.getProperty("campaign_379_url").trim();
			String placement_detail_492_url = prop.getProperty("placement_detail_492_url").trim();
			String placement_monthly_delivery_494_url = prop.getProperty("placement_monthly_delivery_494_url").trim();
			String advanced_placement_488_url = prop.getProperty("advanced_placement_488_url").trim();
			
			
			
			
			propertyMap.put("host", host);
			propertyMap.put("port", port);
			propertyMap.put("to", to);
			propertyMap.put("data_failure_to", data_failure_to);
			propertyMap.put("marketplace_failure_to", marketplace_failure_to);
			propertyMap.put("sfdc_url", sfdc_url);
			propertyMap.put("sfdc_username", sfdc_username);
			propertyMap.put("sfdc_password", sfdc_password);
			propertyMap.put("sfdc_securitytoken", sfdc_securitytoken);
			propertyMap.put("ftp_server", ftp_server);
			propertyMap.put("ftp_userid", ftp_userid);
			propertyMap.put("ftp_password", ftp_password);
			propertyMap.put("ftp_remotedirectory", ftp_remotedirectory);
			propertyMap.put("ftp_localdirectory", ftp_localdirectory);
			propertyMap.put("ftp_archivedirectory", ftp_archivedirectory);
			
			propertyMap.put("campaign_379_url", campaign_379_url);
			propertyMap.put("placement_detail_492_url", placement_detail_492_url);
			propertyMap.put("placement_monthly_delivery_494_url", placement_monthly_delivery_494_url);
			propertyMap.put("advanced_placement_488_url", advanced_placement_488_url);
			
			 
			
 
			//result = "Company List = " + company1 + ", " + company2 + ", " + company3;
			//System.out.println(host+"--"+to );
		} catch (Exception e) {
			 e.printStackTrace();
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 Logger.log(errors.toString(),true);
			 Logger.logTechOps(errors.toString(),true);
			 for (StackTraceElement ste : Thread.currentThread().getStackTrace()) 
			 {
				 System.out.println(ste);
				 Logger.log(ste+"\n", true);
				 //Logger.logTechOpt(ste+"\n", true);
			 }
			 
      	 
		}finally {
			fis.close();
		}
		return propertyMap;
	}
}