package sfdc;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;

import misc.LastRun;
import misc.Logger;
import misc.ReadPropertyFile;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.SessionRenewer;

import controller.Controller;

import com.sforce.ws.ConnectionException;

public class SForceServiceGlobal {
      EnterpriseConnection connection = null;
      String authEndPoint = "";
      
      
      public boolean login() throws Exception {
    	   
    	  String key = "https.protocols";
    	  String value = "TLSv1.1,TLSv1.2";
    	  System.setProperty(key, value);
    	  
    	  boolean success = false;
           
            ReadPropertyFile readProperty = new ReadPropertyFile(); 
    		HashMap<String, String> propertyMap = new HashMap<String, String>();
    		try {
    			propertyMap = readProperty.getPropValues();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
            /*
            String url      = ConfigurationHandler.getSForceWSUrl(null);
            String username = ConfigurationHandler.getSForceWSUser(null);
            String password = ConfigurationHandler.getSForceWSPwd(null);
           */
            //String url = "https://test.salesforce.com/services/Soap/c/34.0/0DFe000000001Da";
            //String username = "patrick.wang@xaxis.com.gldev";
            //String password = "mustang10272F2MDtRGak8R9pUlrVLggZOE";
            
            
            //String url = "https://test.salesforce.com/services/Soap/c/35.0/0DF31000000XgQV"; // old qa
            //String url = "https://test.salesforce.com/services/Soap/c/35.0/0DF8A000000004w"; // qa
            //String url = "https://test.salesforce.com/services/Soap/c/35.0/0DFc00000004DEE"; // staging
            String url = propertyMap.get("sfdc_url");//staging
            System.out.println("in SForceServiceGlobal, url is " + url); 
            //String url = "https://test.salesforce.com/services/Soap/c/35.0/0DF56000000CaWg"; // gldev
            	
            //String username = "radia.integration@xaxis.com.globalqa"; // qa
            //String password = "welcome1bI7yE9rQdR0DyLI3iUiGl1Ql"; // qa
            //String username = "patrick.wang@xaxis.com.globalqa";
            //String password = "mustang720101Vp4uivKGA3DHUKRCsEKupb";
            //String username = "patrick.wang@xaxis.com.gldev";
            //String password = "mustang7201AK3aB9Xn68rQW3Uk8AFx7LF3";
            
            //String username = "radia.integration@xaxis.com.glstaging"; // staging
            //String password = "welc0me321zhaAii7myjszENBQjwAYzseK"; // staging
            
             String username = propertyMap.get("sfdc_username"); //staging
             String password =  propertyMap.get("sfdc_password") + propertyMap.get("sfdc_securitytoken");//staging

            
             //url = "https://test.salesforce.com/services/Soap/c/33.0";
             //username = "semen.moroz@xaxis.com.globalqa2" ;
            //https://test.salesforce.com/apex/AccountDetail?id=001i000000eyENx

            try {
               ConnectorConfig config = new ConnectorConfig();
               if (this.authEndPoint == null) {
                   this.authEndPoint = url;
               }
               config.setAuthEndpoint(this.authEndPoint);
               config.setUsername(username);
               config.setPassword(password);
               config.setSessionRenewer(new WSCSessionRenewer());
               connection = new EnterpriseConnection(config);
               //connection.getSessionHeader().getSessionId();
               success = true;
               System.out.println("SF Global Login : " + success);
            } 
            //catch (ConnectionException ce) {
            catch (Exception e) {
   			 e.printStackTrace();
   			 StringWriter errors = new StringWriter();
   			 e.printStackTrace(new PrintWriter(errors));
   			 Controller.connectionErrorCount ++;
   			 Logger.log(errors.toString(),true);
   			 Logger.logTechOps(errors.toString(),true);
   			 
   			 for (StackTraceElement ste : Thread.currentThread().getStackTrace()) 
   			 {
   				 System.out.println(ste);
   				 Logger.log(ste+"\n", true);
   				  
   			 }
   			 throw new Exception();
   		}

            return success;
         }

      public void logout() {
                  try {
                     if (connection != null) {
                         connection.logout();
                           System.out.println("SF Global Logged out.");
                     }
                  } catch (ConnectionException ce) {
                     ce.printStackTrace();
                  }
               }
         
       // Constructor
         public SForceServiceGlobal(String authEndPoint) {
            this.authEndPoint = authEndPoint;
         }
         
         public EnterpriseConnection GetSForceService() {
               return connection;
         }
         
         public String GetInitialRestServiceURL() {
               String retval = "";
               if (connection != null) {
                        String endPoint = connection.getConfig().getServiceEndpoint();
                        //retval = endPoint.substring(0, endPoint.indexOf(Constants.SF_URL)) + Constants.SF_URL +"/services";
                        retval = endPoint.substring(0, endPoint.indexOf("salesforce.com")) + "salesforce.com" +"/services";

               }
               return retval;
         }
         
         public static class WSCSessionRenewer implements SessionRenewer {
        	  @Override
        	  public SessionRenewalHeader renewSession(ConnectorConfig config)
        	    throws ConnectionException {
        	   System.err.println("Renewing Session for Config : " + config);
        	   Logger.log("Renewing Session for Config : " + config, true);
        	   config.setManualLogin(true);
        	   EnterpriseConnection binding = Connector.newConnection(config);
        	   LoginResult lr = binding.login(config.getUsername(), config.getPassword());
        	   
        	    
        	   config.setSessionId(lr.getSessionId());
        	   // If using Partner or Enterprise WSDL 
        	   config.setServiceEndpoint(lr.getServerUrl());
			return null;   
        	  }
        	 }


}
