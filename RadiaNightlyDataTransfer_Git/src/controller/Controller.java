package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sforce.soap.enterprise.sobject.Opportunity;

import dao.GetRadiaData;
//import dao.ReadRadiaData;
import misc.FtpDownload;
import misc.Logger;
import misc.ReadPropertyFile;
import misc.SendMailSSL;
import misc.SendMailSSLEveryone;
import radia.RadiaAdvancedColumnDTO;
import radia.RadiaBuyPlacementDTO;
import radia.RadiaFileDTO;
import radia.RadiaOpportunityDTO;
import radia.RadiaPlacementMonthlyScheduleDTO;
import radia.RadiaSLMonthlyScheduleDTO;
import radia.RadiaSellLineDTO;
 
//import sfdc.SfdcDAO;
import sfdc.SForceServiceGlobal;
import sfdc.SFDCRestApi;
import sfdc.SfdcDAO;
import misc.LastRun;
 
 
 
 

public class Controller {

	/**
	 * @param Patrick Wang 11/1/2015
	 */
	 
	private String updateDate; 
	private Date startTime = new Date();
	private boolean runOK = true;
	private static String tmpLogFile = null;
	private static String successLogFile = null;
	private static String errorLogFile = null;
	private static String exceptionLogFile = null;
	private static List deleteOppList = new ArrayList();
	
	public static int totalCampaignCount = 0;
	//public static int runningCampaignCount = 0;
	public static int processedCampaignCount = 0;
	public static int campaignNotInSFCount = 0;
	public static int successCount = 0;
	public static int restAPICallErrorCount = 0;
	public static int connectionErrorCount = 0;
	
	public static Set<String> totalCampaignSet = new HashSet<String>();
	public static Set<String> runningCampaignSet = new HashSet<String>();
	public static Set<String> processedCampaignSet = new HashSet<String>();
	public static Set<String> campaignNotInSFSet = new HashSet<String>();
	
	
	
	public static String errorMessage = "";
	
	public static void main(String[] args) throws Exception {
		
		 
		 new Controller();
	  	 
	}
	
	public Controller() {
		try {
			 		
			tmpLogFile = Logger.getTmpLogFileName();
			successLogFile = Logger.getSuccessLogFileName();
			errorLogFile = Logger.getErrorLogFileName();
			exceptionLogFile = Logger.getExceptionLogFileName();
			
			
			Logger.log("start timing", true);
			System.out.println("start data transfer");
			/*
			ReadPropertyFile readProperty = new ReadPropertyFile();
			HashMap<String, String> propertyMap = new HashMap<String, String>();
			propertyMap = readProperty.getPropValues();
			//System.out.println("in the map, host is " + propertyMap.get("host") + "\n to is "+ propertyMap.get("to"));
			String subject = "Radia to SFDC Data Transfer";
			String body = "Please see the attached log file"; 
	         
	        SendMailSSL.send(null,"patrick.wang@xaxis.com",propertyMap.get("to"),null,null,subject,body,tmpLogFile);
	        */
			
			upsertRadiaCampaign(startTime);
			 
			//List<RadiaFileDTO> radiaFileList =  new ArrayList<RadiaFileDTO>();
			HashMap<String, List<RadiaFileDTO>> radiaFileMap = new HashMap<String, List<RadiaFileDTO>>();
	        //FtpDownload ftp = new FtpDownload(); 
	     
   		    //radiaFileMap = ftp.startFTP();
			 
		 
		} catch (Exception e) {
			Logger.log(e.getMessage(), true);
			System.out.println("\nError in Controller constructor: "+e.getMessage());
			runOK = false;
			//LastRun.setTechOptsLog(startTime.toString() + ": " + e.getMessage());
			Logger.logTechOps(e.getMessage(), true);
			
		}
		//System.exit(0);
    }
/*
	public void getCompanyDataFromLegacy(Date sTime){
		
		 String header = "\n=============================================\n    " + 
	        "  \n=============================================\n";
	        String footer = "\n=============================================\n   " + 
	        "  \n=============================================\n";

	                
	        Logger.log(header, true);
	        updateDate = LastRun.getPreviousDate();
	         
	   
	   //if ( sfdc.login() ) 
	   //{
		  try{
		    SFDCLegacyDAO sfdcLegacy = new SFDCLegacyDAO();
		    
		    sfdcLegacy.getCompanyData(updateDate);
		  }
		  catch (Exception e) {
			 
			e.printStackTrace();
			runOK = false;
		}
		
		  
	             
	        if (runOK){
	        //String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	        //String DATE_FORMAT = "yyyy-MM-dd";
	         
	        //SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);	
	        //LastRun.setNewDate(sdf.parse(sTime.toString()));
			//java.sql.Date jsqlD = java.sql.Date.valueOf(sdf.format(sTime));
			 //  System.out.println("jsqlD is " + jsqlD);
			   //LastRun.setNewDate(sTime);
	        } 
	        Logger.log(footer, true);
			
			 
			
			Date startTime = new Date();
			
	        
	        Logger.deleteTmpLogFile(tmpLogFile);
	   //}
	  // sfdc.logout();
	}
	 
	
	public void insertCompanyDataToGlobal(Date sTime){
		
		 String header = "\n=============================================\n    " + 
	        "  \n=============================================\n";
	        String footer = "\n=============================================\n   " + 
	        "  \n=============================================\n";

	                
	        Logger.log(header, true);

	         
	    //ArrayList<Opportunity> firstClosedWonOppList= new ArrayList<Opportunity>();
	   //if ( sfdc.login() ) 
	   //{
		  try{
		    SFDCRestApi restApi = new SFDCRestApi();
		    //restApi.getSFAccountsBySFAgencyIdFromREST();
		    restApi.insertCompany();
		    //restApi.queryCompany();
		  }
		  catch (Exception e) {
			 
			e.printStackTrace();
			runOK = false;
		}
		
		  
	             
	        if (runOK){
	        //String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	        //String DATE_FORMAT = "yyyy-MM-dd";
	         
	        //SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);	
	        //LastRun.setNewDate(sdf.parse(sTime.toString()));
			//java.sql.Date jsqlD = java.sql.Date.valueOf(sdf.format(sTime));
			 //  System.out.println("jsqlD is " + jsqlD);
			   LastRun.setNewDate(sTime);
	        } 
	        Logger.log(footer, true);
			
			//tmpLogFile = "C:\\eclipse\\workspace\\AdtechExtract\\scripts\\tempLog.txt";
			
			Date startTime = new Date();
			
	        //SendMailSSL.send(null,null,null,null,null,subject,body,tmpLogFile);
	            
	      
	      // Delete temp log file
	        Logger.deleteTmpLogFile(tmpLogFile);
	   //}
	  // sfdc.logout();
	}
	*/
	public void upsertRadiaCampaign(Date sTime) throws Exception{
		
		 String header = "\n=============================================\n    " + 
	        "  \n=============================================\n";
	        String footer = "\n=============================================\n   " + 
	        "  \n=============================================\n";

	        ReadPropertyFile readProperty = new ReadPropertyFile(); 
	 		HashMap<String, String> propertyMap = new HashMap<String, String>();
	 		try {
	 			propertyMap = readProperty.getPropValues();
	 		} catch (IOException e) {
				 e.printStackTrace();
				 StringWriter errors = new StringWriter();
				 e.printStackTrace(new PrintWriter(errors));
				 Logger.log(errors.toString(),true);
				 
				 for (StackTraceElement ste : Thread.currentThread().getStackTrace()) 
				 {
					 System.out.println(ste);
					 Logger.log(ste+"\n", true);
					 Logger.logTechOps(ste+"\n", true);
				 }
			}
	        
	                
	        Logger.log(header, true);
	        List<RadiaFileDTO> radiaFileList =  new ArrayList<RadiaFileDTO>();
	        List<RadiaFileDTO> radiaFileListToRemove =  new ArrayList<RadiaFileDTO>();
	        FtpDownload ftp = new FtpDownload(); 
	    //ArrayList<Opportunity> firstClosedWonOppList= new ArrayList<Opportunity>();
	   //if ( sfdc.login() ) 
	   //{
		  try{
			  /*
			  CharSequence campaign = "379_Radia_Campaigns";
          	  CharSequence placement = "492_Radia_Placement_Details";
          	  CharSequence monthlySchedule = "494_Radia_Placements_Monthly";
          	  CharSequence advanced = "488_Radia_Advanced";
          	  */
			  CharSequence campaign = "379_";
          	  CharSequence placement = "492_";
          	  CharSequence monthlySchedule = "494_";
          	  CharSequence advanced = "488_";
			  
          	  String campaignFileName = "";
          	  String placementFileName = "";
          	  String monthlyScheduleFileName = "";
          	  String advancedFileName = "";
          	  
          	  boolean hasCampaignFile = false;
          	  boolean hasPlacementDetailFile = false;
          	  boolean hasPlacementMonthlyScheduleFile = false;
          	  boolean hasAdvancedFile = false;
			   
			  HashMap<String, List<RadiaFileDTO>> map = new HashMap<String, List<RadiaFileDTO>>();
			  map = ftp.startFTP();
			  
			  //***********************************************************
			  //If detect there is file missing from the ftp download, automate the scheduler
			  /*
			  Map<String, List<RadiaFileDTO>> radiaSchedulerMap = new TreeMap<String, List<RadiaFileDTO>>(map); 
			  boolean callDatamart = false; 
			  	 
				  if (radiaSchedulerMap.size() > 0)
				  {
					  Map<String, List<RadiaFileDTO>> radiaFileMapToRemove = new TreeMap<String, List<RadiaFileDTO>>(map);
					  for (String s : radiaFileMapToRemove.keySet())
			          {
			                radiaFileListToRemove = radiaFileMapToRemove.get(s);
			                Logger.log("before automate scheduler, radiaFileListToRemove.size() is " + radiaFileListToRemove.size(), true);
			                if (radiaFileListToRemove.size() != 4)
			                {
			                	ftp.moveFiles(radiaFileListToRemove);
			                	callDatamart = true;
			                }
			          }
					   
				  }
				  
				  //radiaSchedulerMap = new HashMap<String, List<RadiaFileDTO>>();
				  boolean automateScheduleResult = false;
              
				  if (callDatamart)
				  {
				  try 
				  {
					  String urlCampaignStr = propertyMap.get("campaign_379_url");
					  System.out.println("urlCampaignStr is " + urlCampaignStr);
					  Logger.log("urlCampaignStr is " + urlCampaignStr, true);
					  URL url = new URL(urlCampaignStr);
					  InputStream is = url.openStream();
              	
					  String urlPlacementDetailStr = propertyMap.get("placement_detail_492_url");
					  System.out.println("urlPlacementDetailStr is " + urlPlacementDetailStr);
					  Logger.log("urlPlacementDetailStr is " + urlPlacementDetailStr, true);
					  url = new URL(urlPlacementDetailStr);
					  is = url.openStream();
            	
					  String urlPlacementMonthlyScheduleStr = propertyMap.get("placement_monthly_delivery_494_url");
					  System.out.println("urlPlacementMonthlyScheduleStr is " + urlPlacementMonthlyScheduleStr);
					  Logger.log("urlPlacementMonthlyScheduleStr is " + urlPlacementMonthlyScheduleStr, true);
					  url = new URL(urlPlacementMonthlyScheduleStr);
					  is = url.openStream();

					  String urlAdvancedPlacementStr = propertyMap.get("advanced_placement_488_url");
					  System.out.println("urlAdvancedPlacementStr is " + urlAdvancedPlacementStr);
					  Logger.log("urlAdvancedPlacementStr is " + urlAdvancedPlacementStr, true);
					  url = new URL(urlAdvancedPlacementStr);
					  is = url.openStream();
					  
					   
					  map = ftp.startFTP();
					  //radiaSchedulerMap = new TreeMap<String, List<RadiaFileDTO>>(map);
					   
					  automateScheduleResult = true;  
					  
					   */
					  
					   /*
						  Thread.sleep(120000);
						  if (radiaSchedulerMap.size() > 0)
						  {
							  Map<String, List<RadiaFileDTO>> radiaFileMapToRemove = new TreeMap<String, List<RadiaFileDTO>>(map);
							  for (String s : radiaFileMapToRemove.keySet())
					          {
					                radiaFileListToRemove = radiaFileMapToRemove.get(s);
					                if (radiaFileListToRemove.size() < 4)
					                {
					                	ftp.moveFiles(radiaFileListToRemove);
					                }
					          }
							   
						  }
						  map = new HashMap<String, List<RadiaFileDTO>>(); 
						  
						  url = new URL(urlCampaignStr);
						  is = url.openStream();
	              	
						  url = new URL(urlPlacementDetailStr);
						  is = url.openStream();
	            	
						  url = new URL(urlPlacementMonthlyScheduleStr);
						  is = url.openStream();

						  url = new URL(urlAdvancedPlacementStr);
						  is = url.openStream();
						  
						  map = ftp.startFTP();
						  */
				 /*	   
				  } 
				  catch(Exception e)
				  {
					  automateScheduleResult = false;
					  e.printStackTrace();
					  for (StackTraceElement ste : Thread.currentThread().getStackTrace()) 
					  {
						  System.out.println(ste);
						  Logger.log(ste+"\n", true);

					  }
				  }
				  }//if (callDatamart)
			   */
			  //***********************************************************
			  
			  Map<String, List<RadiaFileDTO>> radiaFileMap = new TreeMap<String, List<RadiaFileDTO>>(map); 
	            
	            Set set2 = radiaFileMap.entrySet();
	            Iterator iterator2 = set2.iterator();
	            while(iterator2.hasNext()) {
	                 Map.Entry me2 = (Map.Entry)iterator2.next();
	                 System.out.print(me2.getKey() + ": ");
	                 System.out.println(me2.getValue());
	            }
	            System.out.println("After Sorting:");
			  
			  for (String s : radiaFileMap.keySet())
	          {
	                System.out.println("-----fileMap key is " + s);
	                
	                radiaFileList = radiaFileMap.get(s);
	                for (int n=0; n<radiaFileList.size();n++)
	                {
	                	System.out.println("----file name is " + ((RadiaFileDTO)radiaFileList.get(n)).getName());
	                	if (((RadiaFileDTO)radiaFileList.get(n)).getName().contains(campaign))
	                	{
	                		campaignFileName = ((RadiaFileDTO)radiaFileList.get(n)).getName();
	                		hasCampaignFile = true;
	                	}
	                	else if (((RadiaFileDTO)radiaFileList.get(n)).getName().contains(placement))
	                	{
	                		placementFileName = ((RadiaFileDTO)radiaFileList.get(n)).getName();
	                		hasPlacementDetailFile = true;
	                	}
	                	else if (((RadiaFileDTO)radiaFileList.get(n)).getName().contains(monthlySchedule))
	                	{
	                		monthlyScheduleFileName = ((RadiaFileDTO)radiaFileList.get(n)).getName();
	                		hasPlacementMonthlyScheduleFile = true;
	                	}
	                	else if (((RadiaFileDTO)radiaFileList.get(n)).getName().contains(advanced))
	                	{
	                		advancedFileName = ((RadiaFileDTO)radiaFileList.get(n)).getName();
	                		hasAdvancedFile = true;
	                	}
	                	
	                	
	                }//for (int n=0; n<radiaFileList.size();n++)
	              //Logger.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ campaignFileName is " + campaignFileName, true);
	              //Logger.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ placementFileName is " + placementFileName, true);
	              //Logger.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ monthlyScheduleFileName is " + monthlyScheduleFileName, true);
	                
	                
	                if (!hasCampaignFile || !hasPlacementDetailFile || !hasPlacementMonthlyScheduleFile || !hasAdvancedFile)
	                {
	                 String subject = "Radia to SFDC Data Transfer - Failure (Missing File On FTP Server)";
	                 String missingFile = "";
	                 if (!hasCampaignFile)
	                	 missingFile = missingFile + "379_Campaign file ";
	                 if (!hasPlacementDetailFile)
	                	 missingFile = missingFile + "492_Placement_Details file ";
	                 if (!hasPlacementMonthlyScheduleFile)
	                	 missingFile = missingFile + "494_Placements_Monthly file ";
	                 if (!hasAdvancedFile)
	                	 missingFile = missingFile + "488_Advanced_Placement file ";
	                 System.out.println("--propertyMap.get(marketplace_failure_to) is " + propertyMap.get("marketplace_failure_to"));
	    			 String body = "There is " + missingFile + " missing for the 4 file set in the Radia folder on FTP server.";
	    			 Logger.logTechOps("There is missing file for the 4 file set in the Radia folder on FTP server.", true);
	    	         SendMailSSL.send(null,"patrick.wang@xaxis.com",propertyMap.get("marketplace_failure_to"),null,null,subject,body,null);
	    	         System.exit(-1);
	                }
			   
			  
			  ArrayList<RadiaOpportunityDTO> radiaOppList = new ArrayList<RadiaOpportunityDTO>();
			  List radiaOppDeleteList = new ArrayList<String>();
			  
			  GetRadiaData radiaData = new GetRadiaData();
			  //radiaOppList = (ArrayList<RadiaOpportunityDTO>) radiaData.getCampaignFromExcel(radiaFileList.get(0).getName());
			  radiaOppList = (ArrayList<RadiaOpportunityDTO>) radiaData.getCampaignFromExcel(campaignFileName);
			  //radiaOppList = (ArrayList<RadiaOpportunityDTO>) radiaData.getCampaignFromExcel();
			  //radiaOppDeleteList = GetRadiaData.deleteOppList;//PW temporarily comment out 2016-07-27
			  if (radiaOppDeleteList != null)
			  {
				  for (int b = 0; b<radiaOppDeleteList.size(); b++)
				  {
					  System.out.println("In controller,  radiaOppDeleteList is " + radiaOppDeleteList.get(b));
					  Logger.log("In controller,  radiaOppDeleteList is " + radiaOppDeleteList.get(b), true);
				  }
			  }
			  
			  HashMap<String, List<RadiaSellLineDTO>> sellLineMap = new HashMap<String, List<RadiaSellLineDTO>>();
			  //sellLineMap = radiaData.getSellLineFromExcel(radiaFileList.get(1).getName());
			  sellLineMap = radiaData.getSellLineFromExcel(placementFileName);
			  //sellLineMap = radiaData.getSellLineFromExcel();
			  List radiaSLDeleteList = new ArrayList<String>();
			  
			  //radiaSLDeleteList = GetRadiaData.deleteSellLineList;//PW temporarily comment out 2016-07-27
			  if (radiaSLDeleteList != null)
			  {
				  for (int b = 0; b<radiaSLDeleteList.size(); b++)
				  {
					  System.out.println("In controller,  radiaSLDeleteList is " + radiaSLDeleteList.get(b));
					  Logger.log("In controller,  radiaSLDeleteList is " + radiaSLDeleteList.get(b), true);
				  }
			  }
			  
			  HashMap<String, List<RadiaBuyPlacementDTO>> buyPlacementMap = new HashMap<String, List<RadiaBuyPlacementDTO>>();
			  //buyPlacementMap = radiaData.getBuyPlacementFromExcel(radiaFileList.get(1).getName());
			  buyPlacementMap = radiaData.getBuyPlacementFromExcel(placementFileName);
			  //buyPlacementMap = radiaData.getBuyPlacementFromExcel();
			  List radiaBPDeleteList = new ArrayList<String>();
			  //radiaBPDeleteList = GetRadiaData.deleteBuyPlacementList;//PW temporarily comment out 2016-07-27
			  if (radiaBPDeleteList != null)
			  {
				  for (int b = 0; b<radiaBPDeleteList.size(); b++)
				  {
					  System.out.println("In controller,  radiaBPDeleteList is " + radiaBPDeleteList.get(b));
					  Logger.log("In controller,  radiaBPDeleteList is " + radiaBPDeleteList.get(b), true);
				  }
			  }
			  
			  HashMap<String, List<RadiaSLMonthlyScheduleDTO>> slmsMap = new HashMap<String, List<RadiaSLMonthlyScheduleDTO>>();
			  //slmsMap = radiaData.getSLMonthlyScheduleFromExcel(radiaFileList.get(2).getName());
			  slmsMap = radiaData.getSLMonthlyScheduleFromExcel(monthlyScheduleFileName);
			  //slmsMap = radiaData.getSLMonthlyScheduleFromExcel();
			  List radiaSLMSDeleteList = new ArrayList<String>();
			  //radiaSLMSDeleteList = GetRadiaData.deleteSLMonthlyScheduleList;//PW temporarily comment out 2016-07-27
			  if (radiaSLMSDeleteList != null)
			  {
				  
				  for (int c = 0; c<radiaSLMSDeleteList.size(); c++)
				  {
					  System.out.println("In controller,  radiaSLMSDeleteList is " + radiaSLMSDeleteList.get(c));
					  Logger.log("In controller,  radiaSLMSDeleteList is " + radiaSLMSDeleteList.get(c), true);
				  }
			  }
			  
			  HashMap<String, List<RadiaPlacementMonthlyScheduleDTO>> pmsMap = new HashMap<String, List<RadiaPlacementMonthlyScheduleDTO>>();
			  //pmsMap = radiaData.getPlacementMonthlyScheduleFromExcel(radiaFileList.get(2).getName());
			  pmsMap = radiaData.getPlacementMonthlyScheduleFromExcel(monthlyScheduleFileName);
			  //pmsMap = radiaData.getPlacementMonthlyScheduleFromExcel();
			  List radiaPMSDeleteList = new ArrayList<String>();
			  //radiaPMSDeleteList = GetRadiaData.deletePlacementMonthlyScheduleList;//PW temporarily comment out 2016-07-27
			  if (radiaPMSDeleteList != null)
			  {
				  for (int b = 0; b<radiaPMSDeleteList.size(); b++)
				  {
					  System.out.println("In controller,  radiaPMSDeleteList is " + radiaPMSDeleteList.get(b));
					  Logger.log("In controller,  radiaPMSDeleteList is " + radiaPMSDeleteList.get(b), true);
				  }
			  }
			  
			  HashMap<String, RadiaAdvancedColumnDTO> advancedColumnMap = new HashMap<String, RadiaAdvancedColumnDTO>();
			  advancedColumnMap = radiaData.getAdvancedCustomDataFromExcel(advancedFileName);
			  Logger.log("------in advancedColumnMap, advancedColumnMap.size() is " + advancedColumnMap.size(), true);
			  
			  /*
			  for (String str : advancedColumnMap.keySet())
			  {
			      Logger.log("------in advancedColumnMap, radia id is " + str + "\n" + 
			  "AD LABS: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getAdLabs() + "\n" + 
			  "AGENCY BILLING PLATFORM: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getAgencyBillingPlatform() + "\n"
			  + " CHANNEL: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getChannel() + "\n" 
			  + " CREATIVE DETAIL: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getCreativeDetail() + "\n" 
			  + " CREATIVE FORMAT: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getCreativeFormat() + "\n" 
			  + " DATA/TACTIC: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getDataTactic() + "\n" 
			  + " DEVICE: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getDevice()+ "\n"
			  + " INVENTORY: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getInventory()+ "\n"
			  + " NETWORK BILLING PLATFORM: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getNetworkBillingPlatform()+ "\n"
			  + " PRIMARY KPI: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getPrimaryKpi()+ "\n"
			  + " PRODUCT CODE: " + ((RadiaAdvancedColumnDTO) advancedColumnMap.get(str)).getProductCode()+ "\n"
			  , true);
			  }
			  */
			  SfdcDAO sfdc = new SfdcDAO(); 
			  
			  //********************************************
			  //populate the map before records get deleted for all the child ids
			  /*
			  HashMap<String, List<String>> oppParentMap = new HashMap<String, List<String>>();
			  for (int i= 0; i< radiaOppList.size(); i++)
			  {
				   
				  RadiaOpportunityDTO existingOppDto = new RadiaOpportunityDTO();
				  List<String> parentIds = new ArrayList<String>();
				  
 	               
	  	             existingOppDto = sfdc.getOppToUpdate(radiaOppList.get(i).getRadiaCampaignId());
	  	             String oppId = "";
	  	             
	  	           if (existingOppDto.getOppId() == null)
	  	           {
	  	        	 Logger.logException("Radia Campaign " + radiaOppList.get(i).getName() + " with Radia Id " +radiaOppList.get(i).getRadiaCampaignId()+ " doesn't exist in Salesforce!!!!!!!!!!!!", true);
	    	         continue; 
	  	           }
	  	           else
	  	           {
	  	        	   oppId = existingOppDto.getOppId();
	  	           }
	  	           if(!oppId.equals(""))
	  	           {
	  	        	   parentIds = sfdc.fetchAll(oppId);
	  	        	   for (String p : parentIds)
	  	        	   {
	  	        		   Logger.log("-------in populate map, parent id is " + p, true );
	  	        	   }
	  	        	   
	  	        	   oppParentMap.put(oppId, parentIds);
	  	           }
	  	           
			  }
			  
			  */
			  //********************************************
			  
			  //sfdc.deleteRadiaData(radiaOppDeleteList, radiaSLDeleteList, radiaBPDeleteList, radiaSLMSDeleteList, radiaPMSDeleteList);//PW temporarily comment out 2016-07-27
			  
			  GetRadiaData.deleteOppList.clear();
			  GetRadiaData.deleteSellLineList.clear();
			  GetRadiaData.deleteBuyPlacementList.clear();
			  GetRadiaData.deleteSLMonthlyScheduleList.clear();
			  GetRadiaData.deletePlacementMonthlyScheduleList.clear();
			  
			  SFDCRestApi restApi = new SFDCRestApi();
			  //restApi.getSFAccountsBySFAgencyIdFromREST();
			  //totalCampaignCount = radiaOppList.size();
			  if (totalCampaignSet != null)
			  {
				  totalCampaignCount = totalCampaignSet.size();
				  Logger.log("----totalCampaignCount is " + totalCampaignCount, true);
			  }
			  for (int i= 0; i< radiaOppList.size(); i++)
			  {
				  
				  RadiaOpportunityDTO existingOppDto = new RadiaOpportunityDTO();
 	               
	  	             existingOppDto = sfdc.getOppToUpdate(radiaOppList.get(i).getRadiaCampaignId());
	  	             String oppId = "";
	  	             
	  	           if (existingOppDto.getOppId() == null)
	  	           {
	  	        	 Logger.logException("Radia Campaign " + radiaOppList.get(i).getName() + " with Radia Id " +radiaOppList.get(i).getRadiaCampaignId()+ " doesn't exist in Salesforce!!!!!!!!!!!!", true);
	  	        	 campaignNotInSFSet.add(radiaOppList.get(i).getRadiaCampaignId());
	  	        	 continue; 
	  	           }
				  
				  List<RadiaSellLineDTO> radiaSLList = new ArrayList<RadiaSellLineDTO>();
				  Logger.log("in controller, radiaOppList.get(i).getRadiaCampaignId() is " + radiaOppList.get(i).getRadiaCampaignId(), true);
				  radiaSLList = sellLineMap.get(radiaOppList.get(i).getRadiaCampaignId());
				  RadiaOpportunityDTO oppDto = new RadiaOpportunityDTO();
				  RadiaOpportunityDTO oppDto1 = new RadiaOpportunityDTO();
				  RadiaOpportunityDTO oppDto2 = new RadiaOpportunityDTO();
				  
				  
				  oppDto = radiaOppList.get(i);
				  oppDto1 = radiaOppList.get(i);
				  oppDto2 = radiaOppList.get(i);
				  
				  System.out.println("in controller, campaign name is " + oppDto.getName());
				  Logger.log("in controller, campaign name is " + oppDto.getName(), true);
				  
				  //to see if any buy placement needs dummy sell line
				  List<RadiaBuyPlacementDTO> radiaBPNeedDummySLList = new ArrayList<RadiaBuyPlacementDTO>();
				  radiaBPNeedDummySLList = buyPlacementMap.get(oppDto.getRadiaCampaignId() + "FeeLine");
				  //*********************************************************************************
				  //To populate PMS to BP belongs to FeeLine sell line PW 2016-04-14
				  if (radiaBPNeedDummySLList != null && radiaBPNeedDummySLList.size() > 0)
				  {
					 for (int m = 0; m<radiaBPNeedDummySLList.size(); m++)
					 {
						 List<RadiaPlacementMonthlyScheduleDTO> radiaPMSList = new ArrayList<RadiaPlacementMonthlyScheduleDTO>();
						 radiaPMSList = pmsMap.get(radiaBPNeedDummySLList.get(m).getRadiaId());
						 Logger.log("-----radiaBPNeedDummySLList.get(m).getRadiaId() is " + radiaBPNeedDummySLList.get(m).getRadiaId(), true);
						 Logger.log("-----pmsMap.get(radiaBPNeedDummySLList.get(m).getRadiaId()) is " + pmsMap.get(radiaBPNeedDummySLList.get(m).getRadiaId()), true);
						  
						 radiaBPNeedDummySLList.get(m).setPlacementmonthlyscheduleData(radiaPMSList);
					 }
				  }
				  //********************************************************************************
				  
				  
				  if (radiaSLList != null && radiaSLList.size() > 0)
				  {
					  for(int j = 0; j<radiaSLList.size(); j++)
					  {
						  List<RadiaBuyPlacementDTO> radiaBPList = new ArrayList<RadiaBuyPlacementDTO>();
						  radiaBPList = buyPlacementMap.get(radiaSLList.get(j).getPlacementId());
						  
						  //**********
						  //set placement monthly schedule 
						  
						  HashMap<String, String> packagePlacementIdPlacementNumberMap = new HashMap<String, String>();
						  HashMap<String, List<RadiaBuyPlacementDTO>> placementPackageChildMap= new HashMap<String, List<RadiaBuyPlacementDTO>>();
						  if (radiaBPList != null && radiaBPList.size() > 0)
						  {
							 for (int m = 0; m<radiaBPList.size(); m++)
							 {
								 List<RadiaPlacementMonthlyScheduleDTO> radiaPMSList = new ArrayList<RadiaPlacementMonthlyScheduleDTO>();
								 radiaPMSList = pmsMap.get(radiaBPList.get(m).getRadiaId());
								 radiaBPList.get(m).setPlacementmonthlyscheduleData(radiaPMSList);
								 
								 if (radiaBPList.get(m).getPackageType().equalsIgnoreCase("Package"))
								 {
									 packagePlacementIdPlacementNumberMap.put(radiaBPList.get(m).getPlacementId(),radiaBPList.get(m).getRadiaId());
								 }
								 
								 //**********************************************************************************
								 //to populate PackageChildBuyPlacement Map
								 if(placementPackageChildMap.get(radiaBPList.get(m).getPackageId()) != null)
				                 {
				                    	placementPackageChildMap.get(radiaBPList.get(m).getPackageId()).add(radiaBPList.get(m));
				                    	Logger.log("in if, radiaBPList.get(m).getPackageId() is " + radiaBPList.get(m).getPackageId(), true);
				                 }else
				                 {
				                        List<RadiaBuyPlacementDTO> listBP = new ArrayList<RadiaBuyPlacementDTO>();
				                        listBP.add((RadiaBuyPlacementDTO)radiaBPList.get(m));
				                        Logger.log("in else, (RadiaBuyPlacementDTO)radiaBPList.get(m) is " +  radiaBPList.get(m).getPackageId(), true);
				                        placementPackageChildMap.put(((RadiaBuyPlacementDTO) radiaBPList.get(m)).getPackageId(), listBP);
				                         
				                 }
								 //**********************************************************************************
							 }
							 
							//populate map of package type placement id and placement number -- PW 2016-01-26
							    List<RadiaBuyPlacementDTO> objsBP = radiaBPList;
				            	Iterator<RadiaBuyPlacementDTO> b = objsBP.iterator();
				            	Set<String> childSet = new HashSet<String>();
				            	while (b.hasNext()) 
				            	{
				            		RadiaBuyPlacementDTO radiaBPDto = b.next();
				            		if (radiaBPDto.getPackageType().equalsIgnoreCase("Child"))
				            		{
				            			String childPackageId = "";
				            			if (packagePlacementIdPlacementNumberMap.get(radiaBPDto.getPackageId()) != null)
				            			{
				            				childPackageId =  packagePlacementIdPlacementNumberMap.get(radiaBPDto.getPackageId());
				            			}
				            			radiaBPDto.setPackageId(childPackageId);
				            		}
				            		
				            		//********************************************
				            		if (radiaBPDto.getPackageType().equalsIgnoreCase("Package"))
				            		{
				            			String allCreativeSizeTemp = "";
				            			String allCreativeSize = "";
				            			String allCreativeSizeStr = "";
				            			List<RadiaBuyPlacementDTO> listChildBP = placementPackageChildMap.get(radiaBPDto.getPlacementId());
				            			
				            			if (listChildBP != null  )
				            			{
				            				if (listChildBP.size() > 0)
				            				{
				            					for (RadiaBuyPlacementDTO bp : listChildBP)
				            					{
				            						childSet.add(bp.getCreativeSize());
				            					}
				            				}
				            			}
				            			
				            			if (childSet != null)
				            			{
				            				if (childSet.size() > 0)
				            				{
				            					//Logger.log("##########Package is " + radiaBPDto.getPlacementId(), true);
				            					for(String creativeStr : childSet)
				            					{
				            						allCreativeSizeTemp += creativeStr + ",";
				            						//Logger.log("##############Child is " + bp.getPlacementId(), true);
				            					}
				            					allCreativeSize = allCreativeSizeTemp.substring(0, allCreativeSizeTemp.length()-1);
				            					
				            					if(allCreativeSize.length()>255)
				                                {
				            						allCreativeSizeStr = allCreativeSize.substring(0,254); 
				                                }
				                                else
				                                {
				                                	allCreativeSizeStr = allCreativeSize;	
				                                }
				                                 
				            					
				            					Logger.log("--type is Package -- allCreativeSize is " + allCreativeSize , true);
				            				}
				            			}
				            			Logger.log("----before setAllCreativeSize, radia id is " + radiaBPDto.getRadiaId() + " -- allCreativeSize is " + allCreativeSize, true);
				            		    radiaBPDto.setAllCreativeSize(allCreativeSizeStr);
				            		}
				            		else if (radiaBPDto.getPackageType().equalsIgnoreCase("Standalone") || radiaBPDto.getPackageType().equalsIgnoreCase("Child"))
				            		{
				            			radiaBPDto.setAllCreativeSize(radiaBPDto.getCreativeSize());
				            			Logger.log("--type is Standalone or Child -- allCreativeSize is " + radiaBPDto.getCreativeSize() , true);
				            		}
				            		
				            		//*******************************************
				            		
				            		//-----------------------------------------------------------
				            		//Populate the advanced column to buy placement
				            		
				            		Logger.log("----advancedColumnMap.size() is " + advancedColumnMap.size(), true);
				            		if (advancedColumnMap.get(radiaBPDto.getRadiaId()) != null)
				            		{
				            			Logger.log("----before radiaBPDto.setAdLabs, radiaBPDto.getRadiaId() is " + radiaBPDto.getRadiaId(), true);
				            			Logger.log("advancedColumnMap.get(radiaBPDto.getRadiaId()).getAdLabs() is " + advancedColumnMap.get(radiaBPDto.getRadiaId()).getAdLabs(), true);
				            		}
				            		if (advancedColumnMap.get(radiaBPDto.getRadiaId()) != null)
				            		{
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getAdLabs() != null)
				            			{
				            				String adLabs = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getAdLabs();
				            				radiaBPDto.setAdLabs(adLabs);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getAgencyBillingPlatform() != null)
				            			{
				            				String agencyBillingPlatform = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getAgencyBillingPlatform();
				            				radiaBPDto.setAgencyBillingPlatform(agencyBillingPlatform);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getChannel() != null)
				            			{
				            				String channel = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getChannel();
				            				radiaBPDto.setChannel(channel);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getCreativeDetail() != null)
				            			{
				            				String creativeDetail = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getCreativeDetail();
				            				radiaBPDto.setCreativeDetail(creativeDetail);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getCreativeFormat() != null)
				            			{
				            				String creativeFormat = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getCreativeFormat();
				            				radiaBPDto.setCreativeFormat(creativeFormat);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getDataTactic() != null)
				            			{
				            				String dataTactic = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getDataTactic();
				            				radiaBPDto.setDataTactic(dataTactic);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getDevice() != null)
				            			{
				            				String device = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getDevice();
				            				radiaBPDto.setDevice(device);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getInventory() != null)
				            			{
				            				String inventory = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getInventory();
				            				radiaBPDto.setInventory(inventory);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getNetworkBillingPlatform() != null)
				            			{
				            				String networkBillingPlatform = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getNetworkBillingPlatform();
				            				radiaBPDto.setNetworkBillingPlatform(networkBillingPlatform);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getPrimaryKpi() != null)
				            			{
				            				String primaryKpi = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getPrimaryKpi();
				            				radiaBPDto.setPrimaryKpi(primaryKpi);
				            			}
				            			if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getProductCode() != null)
				            			{
				            				String productCode = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaBPDto.getRadiaId())).getProductCode();
				            				radiaBPDto.setProductCode(productCode);
				            			}
				            		}
				            		 
				            		//-----------------------------------------------------------
				            	}
							  
						  }
						  
						  //**********
						  
						//-----------------------------------------------------------
		            		//Populate the advanced column to sell line
						  if (advancedColumnMap.get(radiaSLList.get(j).getRadiaId()) != null)
						  {
						  	if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getAdLabs() != null)
		            		{
		            			String adLabs = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getAdLabs();
		            			radiaSLList.get(j).setAdLabs(adLabs);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getAgencyBillingPlatform() != null)
		            		{
		            			String agencyBillingPlatform = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getAgencyBillingPlatform();
		            			radiaSLList.get(j).setAgencyBillingPlatform(agencyBillingPlatform);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getChannel() != null)
		            		{
		            			String channel = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getChannel();
		            			radiaSLList.get(j).setChannel(channel);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getCreativeDetail() != null)
		            		{
		            			String creativeDetail = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getCreativeDetail();
		            			radiaSLList.get(j).setCreativeDetail(creativeDetail);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getCreativeFormat() != null)
		            		{
		            			String creativeFormat = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getCreativeFormat();
		            			radiaSLList.get(j).setCreativeFormat(creativeFormat);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getDataTactic() != null)
		            		{
		            			String dataTactic = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getDataTactic();
		            			radiaSLList.get(j).setDataTactic(dataTactic);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getDevice() != null)
		            		{
		            			String device = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getDevice();
		            			radiaSLList.get(j).setDevice(device);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getInventory() != null)
		            		{
		            			String inventory = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getInventory();
		            			radiaSLList.get(j).setInventory(inventory);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getNetworkBillingPlatform() != null)
		            		{
		            			String networkBillingPlatform = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getNetworkBillingPlatform();
		            			radiaSLList.get(j).setNetworkBillingPlatform(networkBillingPlatform);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getPrimaryKpi() != null)
		            		{
		            			String primaryKpi = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getPrimaryKpi();
		            			radiaSLList.get(j).setPrimaryKpi(primaryKpi);
		            		}
		            		if(((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getProductCode() != null)
		            		{
		            			String productCode = ((RadiaAdvancedColumnDTO) advancedColumnMap.get(radiaSLList.get(j).getRadiaId())).getProductCode();
		            			radiaSLList.get(j).setProductCode(productCode);
		            		}
						  } 
		            		//-----------------------------------------------------------
						  
						  
						  radiaSLList.get(j).setBuyPlacementData(radiaBPList);
						  if (radiaBPList != null)
						  {
						  Logger.log("in controller, radiaBPList.size() is " + radiaBPList.size(), true);
						  }
						  
						  List<RadiaSLMonthlyScheduleDTO> radiaSLMSList = new ArrayList<RadiaSLMonthlyScheduleDTO>();
						  radiaSLMSList = slmsMap.get(radiaSLList.get(j).getRadiaId());
						  radiaSLList.get(j).setSlmonthlyscheduleData(radiaSLMSList);
						  if (radiaSLMSList != null)
						  {
						  Logger.log("in controller, radiaSLMSList.size() is " + radiaSLMSList.size(), true);
						  }
					  }
					  
					  //to add one dummy sell line 
					
					  if (radiaBPNeedDummySLList != null)
					  {
						  RadiaSellLineDTO dummySL = new RadiaSellLineDTO();
						  dummySL.setRadiaId(oppDto.getRadiaCampaignId() + "FeeLine");
						  dummySL.setPlacementId(oppDto.getRadiaCampaignId() + "FeeLine");
						  dummySL.setPlacementName(oppDto.getRadiaCampaignId() + "FeeLine");
						  dummySL.setStartDate(oppDto.getRadiaCampaignStartDate());
						  System.out.println("in controller, oppDto.getRadiaCampaignStartDate() is " + oppDto.getRadiaCampaignStartDate());
						  dummySL.setEndDate(oppDto.getRadiaCampaignEndDate());
						  System.out.println("in controller, oppDto.getRadiaCampaignEndDate() is " + oppDto.getRadiaCampaignEndDate());
						  
						  dummySL.setBuyPlacementData(radiaBPNeedDummySLList);
						  radiaSLList.add(dummySL);
					  }
					  
					  
					  Logger.log(" radiaSLList.size() is " + radiaSLList.size(), true);
					  oppDto.setSellLineData(radiaSLList);
					  
				  }
				  else
				  {
					//to add one dummy sell line 
						
					  List<RadiaSellLineDTO> radiaDummySLList = new ArrayList<RadiaSellLineDTO>();
					  if (radiaBPNeedDummySLList != null)
					  {
						  RadiaSellLineDTO dummySL = new RadiaSellLineDTO();
						  dummySL.setRadiaId(oppDto.getRadiaCampaignId() + "FeeLine");
						  dummySL.setPlacementId(oppDto.getRadiaCampaignId() + "FeeLine");
						  dummySL.setPlacementName(oppDto.getRadiaCampaignId() + "FeeLine");
						  dummySL.setStartDate(oppDto.getRadiaCampaignStartDate());
						  System.out.println("in controller, oppDto.getRadiaCampaignStartDate() is " + oppDto.getRadiaCampaignStartDate());
						  dummySL.setEndDate(oppDto.getRadiaCampaignEndDate());
						  System.out.println("in controller, oppDto.getRadiaCampaignEndDate() is " + oppDto.getRadiaCampaignEndDate());
						  
						  dummySL.setBuyPlacementData(radiaBPNeedDummySLList);
						  
						  radiaDummySLList.add(dummySL);
					  }
					  
					  Logger.log(" radiaDummySLList.size() is " + radiaDummySLList.size(), true);
					  oppDto.setSellLineData(radiaDummySLList);
				  }
				  
				  
				  
				  /*
				  List<String> parentIdList = new ArrayList<>();
				  parentIdList = oppParentMap.get(oppDto.getOppId());
				  if (parentIdList != null && parentIdList.size() > 0)
				  {
					  for (String str : parentIdList)
					  {
						  Logger.log("------parentid is " + str, true);
					  }
					  
					  oppDto.setParentIdList(parentIdList);
				  }
				  */
				  //restApi.upsertOpportunity(radiaOppList.get(i));
				  //SFDCRestApi sfdcRestApi= new SFDCRestApi();
				  String jsonString = "";
				  
				//*******************************************************************
				  //if oppDto.getSellLineData size is more than 200, split oppDto into oppDto1 and oppDto2 with even number of sl
				   
				    
				  if (oppDto.getSellLineData().size() >= 200)
				  {
					  //RadiaOpportunityDTO oppDto1 = new RadiaOpportunityDTO();
					  //oppDto1 = radiaOppList.get(i);
					  List<RadiaSellLineDTO> radiaSLList1 = new ArrayList<RadiaSellLineDTO>();
					  
					  //RadiaOpportunityDTO oppDto2 = new RadiaOpportunityDTO();
					  //oppDto2 = radiaOppList.get(i);
					  List<RadiaSellLineDTO> radiaSLList2 = new ArrayList<RadiaSellLineDTO>();
					  
					  for (int m = 0; m<oppDto.getSellLineData().size(); m++)
					  {
						  if (m<=oppDto.getSellLineData().size()/2)
						  {
							  radiaSLList1.add(oppDto.getSellLineData().get(m));
						  }
						  else
						  {
							  radiaSLList2.add(oppDto.getSellLineData().get(m));
						  }
					  }
					  if (radiaSLList1.size() > 0)
					  {
						  oppDto1.setSellLineData(radiaSLList1);
						  for (int n = 0; n<oppDto1.getSellLineData().size(); n++)
						  {
							  Logger.log("oppDto1--" + oppDto1.getSellLineData().get(n).getRadiaId(), true);
						  }
						  restApi.upsertOpportunity(oppDto1);
					  }
					  if (radiaSLList2.size() > 0)
					  {
						  oppDto2.setSellLineData(radiaSLList2);
						  for (int n = 0; n<oppDto2.getSellLineData().size(); n++)
						  {
							  Logger.log("oppDto2--" + oppDto2.getSellLineData().get(n).getRadiaId(), true);
						  }
						  restApi.upsertOpportunity(oppDto2);
					  }
					   
					  /*
					  for (int n = 0; n<oppDto1.getSellLineData().size(); n++)
					  {
						  Logger.log("oppDto1--" + oppDto1.getSellLineData().get(n).getRadiaId(), true);
					  }
					  */
					  
					  /*
					  Logger.log("----before sfdcRestApi1.upsertOpportunity(oppDto1);", true);
					  SFDCRestApi sfdcRestApi1= new SFDCRestApi(); 
					  sfdcRestApi1.upsertOpportunity(oppDto1);
					  Logger.log("----after sfdcRestApi1.upsertOpportunity(oppDto1);", true);
					  Logger.log("----before sfdcRestApi2.upsertOpportunity(oppDto2);", true);
					  SFDCRestApi sfdcRestApi2= new SFDCRestApi(); 
					  sfdcRestApi2.upsertOpportunity(oppDto2);
					  Logger.log("----after sfdcRestApi2.upsertOpportunity(oppDto2);", true);
					  */
				 
					  
				   }
				    
				  //*******************************************************************
				   else
				   {
				   restApi.upsertOpportunity(oppDto);
				   }
				   
			  }//for (int i= 0; i< radiaOppList.size(); i++)
			  Logger.log("---runOK is " + runOK, true);
			  if (runOK)
			  {
				  ftp.moveFiles(radiaFileList);
				  //ftp.uploadFiles(tmpLogFile);
			  }
		     
		  }//for (String s : radiaFileMap.keySet())
			  
	      
		  }
		  
		  catch (Exception e) {
				 e.printStackTrace();
				 StringWriter errors = new StringWriter();
				 e.printStackTrace(new PrintWriter(errors));
				 Logger.log(errors.toString(),true);
				 
				 for (StackTraceElement ste : Thread.currentThread().getStackTrace()) 
	        	 {
	        		System.out.println(ste);
	        		Logger.log(ste+"\n", true);
	        		Logger.logTechOps(ste+"\n", true);
	        	 }
				 runOK = false;
	        	 
			}
		
		  
	             
	        if (runOK){
	        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	        //String DATE_FORMAT = "yyyy-MM-dd";
	         
	        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);	
	        //LastRun.setNewDate(sdf.parse(sTime.toString()));
			//java.sql.Date jsqlD = java.sql.Date.valueOf(sdf.format(sTime));
			 //  System.out.println("jsqlD is " + jsqlD);
			   LastRun.setNewDate(sTime);
			   //LastRun.setLastRadiaCampaignDate(radiaFileList.get(0).getFileDate());
			   //LastRun.setLastRadiaPlacementDate(radiaFileList.get(1).getFileDate());
			   //LastRun.setLastRadiaMonthlyScheduleDate(radiaFileList.get(2).getFileDate());
			   //LastRun.setTechOptsLog(sTime.toString() + ": Success");
			   Logger.logTechOps("Success", true);
			   //ftp.moveFiles();
			   
	        } 
	        Logger.log("GetRadiaData.campaignFileNotEmpty is " + GetRadiaData.campaignFileNotEmpty, true);
	        Logger.log("GetRadiaData.placementFileNotEmpty is " + GetRadiaData.placementFileNotEmpty, true);
	        Logger.log("GetRadiaData.monthlyScheduleFileNotEmpty is " + GetRadiaData.monthlyScheduleFileNotEmpty, true);
	        Logger.log("GetRadiaData.advancedFileNotEmpty is " + GetRadiaData.advancedFileNotEmpty, true);
	        
	        
	        Logger.log("GetRadiaData.campaignFileHasNewUpdate is " + GetRadiaData.campaignFileHasNewUpdate, true);
			Logger.log("GetRadiaData.placementFileHasNewUpdate is " + GetRadiaData.placementFileHasNewUpdate, true);
			Logger.log("GetRadiaData.monthlyScheduleFileHasNewUpdate is " + GetRadiaData.monthlyScheduleFileHasNewUpdate, true);
			Logger.log("GetRadiaData.advancedFileHasNewUpdate is " + GetRadiaData.advancedFileHasNewUpdate, true);
			
			
			 
			
	        Logger.log(footer, true);
			
			//tmpLogFile = "C:\\eclipse\\workspace\\AdtechExtract\\scripts\\tempLog.txt";
			
			Logger.log("--totalCampaignCount is " + totalCampaignCount, true);
			Logger.log("--processedCampaignCount is " + processedCampaignCount, true);
			Logger.log("--campaignNotInSFCount is " + campaignNotInSFCount, true);
			Logger.log("--successCount is " + successCount, true);
			Logger.log("--restAPICallErrorCount is " + restAPICallErrorCount, true);
			System.out.println("CE count is " + connectionErrorCount); 
			int runningCampaignCount = 0;
			if (processedCampaignSet != null)
			{
				processedCampaignCount = processedCampaignSet.size();
			}
			if (campaignNotInSFSet != null)
			{
				campaignNotInSFCount = campaignNotInSFSet.size();
			}
			
			runningCampaignCount = processedCampaignCount + campaignNotInSFCount;
			 
			
			Logger.log("--runningCampaignCount is " + runningCampaignCount, true);
			
	        String subStr = "";
	        String status = "";
			if (GetRadiaData.campaignFileHasNewUpdate && GetRadiaData.placementFileHasNewUpdate && GetRadiaData.monthlyScheduleFileHasNewUpdate 
					&& GetRadiaData.advancedFileHasNewUpdate && GetRadiaData.campaignFileNotEmpty && GetRadiaData.placementFileNotEmpty 
					&& GetRadiaData.monthlyScheduleFileNotEmpty && GetRadiaData.advancedFileNotEmpty
					&& totalCampaignCount == runningCampaignCount && restAPICallErrorCount ==0)
			{
				subStr = "Success";
				status = "Success";
			}
			else
			{
				subStr = "Failure";
				status = "Failure";
				
				
				if(!GetRadiaData.campaignFileHasNewUpdate || !GetRadiaData.placementFileHasNewUpdate || !GetRadiaData.monthlyScheduleFileHasNewUpdate || !GetRadiaData.advancedFileHasNewUpdate)
				{
					subStr = subStr + " - File from Datamart has no update";
				}	 
				if (totalCampaignCount != runningCampaignCount)
				{
					subStr = subStr + " - Job didn't finish processing all the records";
				}
				if (restAPICallErrorCount > 0)
				{
					subStr = subStr +  " - Job experienced Rest API call error";
				}
				if (totalCampaignCount == runningCampaignCount)
				{
					subStr = subStr + " *** Job has processed all the records";
				}
				
				
			}
	         
			
			 
			
			Date startTime = new Date();
			String subject = "Radia to SFDC Data Transfer - " + subStr;
			String body = ""; 
	         
	        //SendMailSSL.send(null,"patrick.wang@xaxis.com","patrick.wang@xaxis.com",null,null,subject,body,tmpLogFile);
	        
	        List<String> attachmentList = new ArrayList<String>();
	        
	        
	        //************************************************
	        //zip all the log files to 1 file
	        
	        
	        String DATE_FORMAT = "yyyy-MM-dd";
	        
	         
	        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);	
	         
			String dateStr = sdf.format(sTime);
	        String zipFileName = "radiaLog-" + dateStr+".zip";
	        
	        /*
	        File zip = new File(zipFileName);
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
	        
	        File f1 = new File(successLogFile);
	        if(f1.exists() && !f1.isDirectory())
	        {
	          ZipEntry e = new ZipEntry(successLogFile);
	          out.putNextEntry(e);
	        }
	        */
	        
	        FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);

			 
			File f1 = new File(successLogFile);
	        if(f1.exists() && !f1.isDirectory())
	        {
	        	addToZipFile(successLogFile, zos);
	        }
	        File f2 = new File(errorLogFile);
	        if(f2.exists() && !f2.isDirectory())
	        {
	        	addToZipFile(errorLogFile, zos);
	        }
	        File f3 = new File(exceptionLogFile);
	        if(f3.exists() && !f3.isDirectory())
	        {
	        	addToZipFile(exceptionLogFile, zos);
	        }
	        /*
	        File f4 = new File(tmpLogFile);
	        if(f4.exists() && !f4.isDirectory())
	        {
	        	addToZipFile(tmpLogFile, zos);
	        }
	        */
			zos.close();
			fos.close();
	        
	        
	         
	        File f = new File(zipFileName);
	        if(f.exists() && !f.isDirectory())
	        {
	        attachmentList.add(zipFileName);
	        }
	        
	        //************************************************
	        /*
	        File f = new File(successLogFile);
	        if(f.exists() && !f.isDirectory())
	        {
	        attachmentList.add(successLogFile);
	        }
	        File f1 = new File(errorLogFile);
	        if(f1.exists() && !f1.isDirectory())
	        {
	        	attachmentList.add(errorLogFile);
	        }
	        File f2 = new File(exceptionLogFile);
	        if(f2.exists() && !f2.isDirectory())
	        {
	        	attachmentList.add(exceptionLogFile);
	        }
	        File f3 = new File(tmpLogFile);
	        if(f3.exists() && !f3.isDirectory())
	        { 
	        	attachmentList.add(tmpLogFile);
	        }
	        */
	        
	        File zipFile = new File(zipFileName);
			
			long size = zipFile.length();
	        //Logger.log("zip file " + zipFileName + " size is " + size, true);
	        
	        //to upload zipped tmpLogFile
	         
	        File source=new File(tmpLogFile);
	        File destination=new File("tempLog-" + dateStr+".txt");
	        copyFile(source,destination);
	        
			ftp.uploadFiles("tempLog-" + dateStr+".txt");
			
			
	        if (size < 9900000)
	        {
	        	 
	        	if (status.equals("Failure"))
	        	{
	        	
	        		if(!GetRadiaData.campaignFileHasNewUpdate || !GetRadiaData.placementFileHasNewUpdate || !GetRadiaData.monthlyScheduleFileHasNewUpdate || !GetRadiaData.advancedFileHasNewUpdate)
	        		{
	        			if (!GetRadiaData.campaignFileHasNewUpdate)
	        				body = body + " 379_Campaign file has no update " + "\n";
	        			if (!GetRadiaData.placementFileHasNewUpdate)
	        				body = body + "\n 492_Placement_Details file has no update " + "\n";
	        			if (!GetRadiaData.monthlyScheduleFileHasNewUpdate)
	        				body = body + "\n 494_Placements_Monthly file has no update " + "\n";
	        			if (!GetRadiaData.advancedFileHasNewUpdate)
	        				body = body + "\n 488_Advanced_Placement file has no update " + "\n";
	        		 //SendMailSSL.sendToEveryone(null,"patrick.wang@xaxis.com",propertyMap.get("data_failure_to"),null,null,subject,body,attachmentList);
	        		 
	        		}
	        		
	        		if (!errorMessage.equals(""))
	        		{
	        			body = body + "\n " + errorMessage + "\n";
	        		}
	        		
	        		body = body + "\n Total campaigns are " + totalCampaignCount + ". The job processed "+ runningCampaignCount  + " campaigns.\n";
	        		
	        	SendMailSSL.sendToEveryone(null,"patrick.wang@xaxis.com",propertyMap.get("to"),null,null,subject,body,attachmentList);
       		 
	        	}
				 
	        	 else
	        	 {
	        		     body = "Please see the attached log file"; 
	        		     SendMailSSL.sendToEveryone(null,"patrick.wang@xaxis.com",propertyMap.get("to"),null,null,subject,body,attachmentList);
	        		 
	        	 }
				  
	        	
	        	
	        }
	        else
	        {
	        	subject = "Radia to SFDC Data Transfer - Warning (Email Attachment Size Exceeds The Limit)";
				body = "The attachment size is " + size;
				Logger.logTechOps("Email Attachment Size Exceeds The Limit", true);
		        SendMailSSL.send(null,"patrick.wang@xaxis.com",propertyMap.get("to"),null,null,subject,body,null);
	        }
	        
	        
			
	      // Delete temp log file
	        Logger.deleteTmpLogFile(tmpLogFile);
	   //}
	  // sfdc.logout();
	        /*
	        SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
	        String oppSfdcHashStr = "";
	        if (sfservice.login() == true) {
	        	sfservice.logout();
	        }*/
	}
	
	public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	     if(!destFile.exists()) {
	      destFile.createNewFile();
	     }

	     FileChannel source = null;
	     FileChannel destination = null;
	     try {
	      source = new RandomAccessFile(sourceFile,"rw").getChannel();
	      destination = new RandomAccessFile(destFile,"rw").getChannel();

	      long position = 0;
	      long count    = source.size();

	      source.transferTo(position, count, destination);
	     }
	     finally {
	      if(source != null) {
	       source.close();
	      }
	      if(destination != null) {
	       destination.close();
	      }
	    }
	 }

 
}
