package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import radia.RadiaFileDTO;
import radia.RadiaSellLineDTO;

public class FtpDownload {

    //static Properties props;
    InputStream inputStream;
    //private static final String FILE_PATH = "/property/radiaftp.properties"; 

    public static void main(String[] args) {

        FtpDownload getRadiaFiles = new FtpDownload();
        /*
        if (args.length < 1)
        {
            System.err.println("Usage: java " + getMyFiles.getClass().getName()+
            " Properties_file");
            System.exit(1);
        }

        String propertiesFile = args[0].trim();
        */
        //String propFileName = "misc/radiaftp.properties";
        //String propFileName = "/property/radiaftp.properties";
        
        //getRadiaFiles.startFTP(FILE_PATH);
        getRadiaFiles.startFTP();

    }

    //public List<RadiaFileDTO> startFTP(String propertiesFile){
     public HashMap<String, List<RadiaFileDTO>> startFTP(){

        
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
    	 
    	 //props = new Properties();
        List<RadiaFileDTO> radiaFileList = new ArrayList<RadiaFileDTO>();
        
        RadiaFileDTO campaignFileDto = new RadiaFileDTO();
        RadiaFileDTO placementFileDto = new RadiaFileDTO();
        RadiaFileDTO monthlyScheduleFileDto = new RadiaFileDTO();
        java.util.Date tempCampaignFileDate = null;
        java.util.Date tempPlacementFileDate = null;
        java.util.Date tempMonthlyScheduleFileDate = null;
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        HashMap<String, List<RadiaFileDTO>> fileMap = new HashMap<String, List<RadiaFileDTO>>();
      
         
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);	
        //LastRun.setNewDate(sdf.parse(sTime.toString()));
        
        
        
        try {

            //props.load(new FileInputStream("properties/" + propertiesFile));
        	//props.load(new FileInputStream(propertiesFile));
        	
        	/*
        	inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);

			if (inputStream != null) 
			{
				props.load(inputStream);
			} 
			else 
			{
				throw new FileNotFoundException("property file '" + propertiesFile + "' not found in the classpath");
			}
			*/
			/*
            String serverAddress = props.getProperty("serverAddress").trim();
            String userId = props.getProperty("userId").trim();
            String password = props.getProperty("password").trim();
            String remoteDirectory = props.getProperty("remoteDirectory").trim();
            String localDirectory = props.getProperty("localDirectory").trim();
           */
			/*
        	String serverAddress = "liveftp.groupm.com";
            String userId = "RadiaFTP";
            String password = "48XEwGmm";
            String remoteDirectory = "/Radia";
            //String localDirectory = "/eclipse-kepler/workspace/RadiaNightlyDataTransfer/radiafile";
            String localDirectory = "/SFDC/Radia/radiafile";
            */
        	String serverAddress = propertyMap.get("ftp_server");
            String userId = propertyMap.get("ftp_userid");
            String password = propertyMap.get("ftp_password");
            String remoteDirectory = propertyMap.get("ftp_remotedirectory");
            //String localDirectory = "/eclipse-kepler/workspace/RadiaNightlyDataTransfer/radiafile";
            String localDirectory = propertyMap.get("ftp_localdirectory");
            
            //Logger.log("----------localDirectory is " + localDirectory, true);
			
            //new ftp client
            FTPClient ftp = new FTPClient();
            //try to connect
            ftp.connect(serverAddress);
            //login to server
            if(!ftp.login(userId, password))
            {
                ftp.logout();
                //return false;
            }
            int reply = ftp.getReplyCode();
            //FTPReply stores a set of constants for FTP reply codes. 
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                //return false;
            }

            //enter passive mode
            ftp.enterLocalPassiveMode();
            //get system name
            //System.out.println("Remote system is " + ftp.getSystemType());
            //change current directory
            ftp.changeWorkingDirectory(remoteDirectory);
            System.out.println("Current directory is " + ftp.printWorkingDirectory());
             
            //get list of filenames
            FTPFile[] ftpFiles = ftp.listFiles();  
            int notFileNum = 0;
            if (ftpFiles != null && ftpFiles.length > 0) {
            	System.out.println("ftp files length is " + ftpFiles.length);
                //loop thru files
                for (FTPFile file : ftpFiles) {
                    if (!file.isFile()) {
                    	notFileNum++;
                        continue;
                    }
                    System.out.println("File is " + file.getName());
                    Logger.log("File is " + file.getName(), true);
                   
                    //get output stream
                    OutputStream output;
                    output = new FileOutputStream(localDirectory + "/" + file.getName());
                    //get the file from the remote system
                    //if (file.getName().equalsIgnoreCase("Radia_SampleFiles_20151111.xlsx"))
                    //{	
                    	ftp.retrieveFile(file.getName(), output);
                    	//System.out.println("file name is " + file.getName() + " ---- timestamp is " + file.getTimestamp().getTime());
                    	
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
                    	 
                    	boolean isCampaignFile = file.getName().contains(campaign);
                    	boolean isPlacementFile = file.getName().contains(placement);
                    	boolean isMonthlySchedule = file.getName().contains(monthlySchedule);
                        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    	String fileDateStr = "";
                        
                       /*
                        
                    	if (isCampaignFile)
                    	{
                    		System.out.println("tempCampaignFileDate is " + tempCampaignFileDate + " --- file.getTimestamp().getTime() is " + file.getTimestamp().getTime());
                    		System.out.println("*****************after date format, file.getTimestamp().getTime() is " + sdfDate.format(file.getTimestamp().getTime()));
                    		
                    		if (tempCampaignFileDate != null)
                    		{
                    			System.out.println("after format, tempCampaignFileDate is " + sdfTime.format(tempCampaignFileDate));
                    		}
                    		if (tempCampaignFileDate != null &&  file.getTimestamp().getTime().after(tempCampaignFileDate))
                    		{
                    			System.out.println(file.getTimestamp().getTime() + " is after " + tempCampaignFileDate);
                    		}	
                    		else 
                    		{
                    			System.out.println(file.getTimestamp().getTime() + " is before " + tempCampaignFileDate);
                    		}
                    			
                    			
                    		if (tempCampaignFileDate == null || file.getTimestamp().getTime().after(tempCampaignFileDate))
                    		{
                    			tempCampaignFileDate = file.getTimestamp().getTime();
                    			campaignFileDto.setName(file.getName());
                    			
                        		campaignFileDto.setFileDate(tempCampaignFileDate);
                    		}
                    	}
                    	else if (isPlacementFile)
                    	{
                    		if (tempPlacementFileDate == null || file.getTimestamp().getTime().after(tempPlacementFileDate))
                    		{
                    			tempPlacementFileDate = file.getTimestamp().getTime();
                    			placementFileDto.setName(file.getName());
                        		placementFileDto.setFileDate(tempPlacementFileDate);
                    		}
                    		
                    	}
                    	else if (isMonthlySchedule)
                    	{
                    		if (tempMonthlyScheduleFileDate == null || file.getTimestamp().getTime().after(tempMonthlyScheduleFileDate))
                    		{
                    			tempMonthlyScheduleFileDate = file.getTimestamp().getTime();
                    			monthlyScheduleFileDto.setName(file.getName());
                        		monthlyScheduleFileDto.setFileDate(tempMonthlyScheduleFileDate);
                    		}
                    		
                    	}
                    	*/
                    	if (file.getName().contains(campaign) || file.getName().contains(placement) || file.getName().contains(monthlySchedule) || file.getName().contains(advanced))
                    	{
                    		RadiaFileDTO radiaFileDto = new RadiaFileDTO();
                    		fileDateStr = sdfDate.format(file.getTimestamp().getTime());
                    		radiaFileDto.setName(file.getName());
                    		radiaFileDto.setFileDate(file.getTimestamp().getTime());
                    		radiaFileDto.setFileDateStr(fileDateStr);
                    		radiaFileList.add(radiaFileDto);
                    	}
                    //}
                    //close output stream
                    output.close();
                    
                    //delete the file
                    //ftp.deleteFile(file.getName());
                   
                    
                }//for loop
                
            }//if (ftpFiles != null && ftpFiles.length > 0) 
            if (ftpFiles.length == notFileNum)
            {
             String subject = "Radia to SFDC Data Transfer - Failure (No File On FTP Server)";
			 String body = "There is no file in the Radia folder on FTP server.";
			 Logger.logTechOps("There is no file in the Radia folder on FTP server", true);
	         SendMailSSL.send(null,"patrick.wang@xaxis.com",propertyMap.get("to"),null,null,subject,body,null);
	         System.exit(-1);
            }
            //System.out.println("campaign file name is " + campaignFileDto.getName() + "---- file date is " + campaignFileDto.getFileDate() + "\n");
            //System.out.println("placement file name is " + placementFileDto.getName() + "---- file date is " + placementFileDto.getFileDate() + "\n");
            //System.out.println("monthly schedule file name is " + monthlyScheduleFileDto.getName() + "---- file date is " + monthlyScheduleFileDto.getFileDate() + "\n");
            //radiaFileList.add(campaignFileDto);
            //radiaFileList.add(placementFileDto);
            //radiaFileList.add(monthlyScheduleFileDto);
            //LastRun.setLastRadiaCampaignDate(tempCampaignFileDate);
            //LastRun.getLastRadiaCampaignDate();
            //System.out.println(" LastRun.getLastRadiaCampaignDate() -- " + LastRun.getLastRadiaCampaignDate());
            
            for (int m = 0; m< radiaFileList.size(); m++)
            {
            	
            	//System.out.println("%%%%%%%%file name is " + ((RadiaFileDTO)radiaFileList.get(m)).getName());
            	//System.out.println("%%%%%%%%file name is " + ((RadiaFileDTO)radiaFileList.get(m)).getFileDateStr());
            }
            
            
            for (int j = 0; j< radiaFileList.size(); j++)
            {
                if(fileMap.get(((RadiaFileDTO) radiaFileList.get(j)).getFileDateStr()) != null)
                {
                    fileMap.get(((RadiaFileDTO) radiaFileList.get(j)).getFileDateStr()).add((RadiaFileDTO)radiaFileList.get(j));
                     
                    
                }
                else
                {
                    List<RadiaFileDTO> listFile = new ArrayList<RadiaFileDTO>();
                    listFile.add((RadiaFileDTO)radiaFileList.get(j));
                    fileMap.put(((RadiaFileDTO) radiaFileList.get(j)).getFileDateStr(), listFile);
                    //System.out.println("((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() is " + ((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() + " -- listSellLine is " + listSellLine);
                    //Logger.log("((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() is " + ((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() + " -- listSellLine is " + listSellLine, true); 
                    
                
                }
            }
            
            /*
            for (String s : fileMap.keySet())
            {
                //System.out.println("-----fileMap key is " + s);
                List<RadiaFileDTO> listFile = new ArrayList<RadiaFileDTO>();
                listFile = fileMap.get(s);
                for (int i=0; i<listFile.size();i++)
                {
                	//System.out.println("file name is " + ((RadiaFileDTO)listFile.get(i)).getName());
                }
            }
            */
            /*
            Map<String, List<RadiaFileDTO>> map = new TreeMap<String, List<RadiaFileDTO>>(fileMap); 
            System.out.println("After Sorting:");
            Set set2 = map.entrySet();
            Iterator iterator2 = set2.iterator();
            while(iterator2.hasNext()) {
                 Map.Entry me2 = (Map.Entry)iterator2.next();
                 System.out.print(me2.getKey() + ": ");
                 System.out.println(me2.getValue());
            }
            */
            ftp.logout();
            ftp.disconnect();
            
            
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
			 
       	 
		}
        
        return fileMap;
        
    }
     
     public boolean moveFiles(List<RadiaFileDTO> radiaFileList) 
     {
    	 boolean result = true;
    	 //props = new Properties();
    	 
    	 ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
  		} catch (IOException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
          
         try {

            /*
 			String serverAddress = "liveftp.groupm.com";
             String userId = "RadiaFTP";
             String password = "48XEwGmm";
             String remoteDirectory = "/Radia";
             String archiveDirectory = "/Radia/Archive/"; 
            */
             
        	 String serverAddress = propertyMap.get("ftp_server");
             String userId = propertyMap.get("ftp_userid");
             String password = propertyMap.get("ftp_password");
             String remoteDirectory = propertyMap.get("ftp_remotedirectory");
             String archiveDirectory = propertyMap.get("ftp_archivedirectory");
             //Logger.log("--------------archive directory is " + archiveDirectory, true);
 			
             //new ftp client
             FTPClient ftp = new FTPClient();
             //try to connect
             ftp.connect(serverAddress);
             //login to server
             if(!ftp.login(userId, password))
             {
                 ftp.logout();
                 //return false;
             }
             int reply = ftp.getReplyCode();
             //FTPReply stores a set of constants for FTP reply codes. 
             if (!FTPReply.isPositiveCompletion(reply))
             {
                 ftp.disconnect();
                 //return false;
             }

             //enter passive mode
             ftp.enterLocalPassiveMode();
             //get system name
             System.out.println("Remote system is " + ftp.getSystemType());
             //change current directory
             ftp.changeWorkingDirectory(remoteDirectory);
             System.out.println("Current directory is " + ftp.printWorkingDirectory());
              
             //get list of filenames
             FTPFile[] ftpFiles = ftp.listFiles();  
            
             if (ftpFiles != null && ftpFiles.length > 0) {
                 //loop thru files
                 for (FTPFile file : ftpFiles) {
                     if (!file.isFile()) {
                         continue;
                     }
                     System.out.println("File is " + file.getName());
                     
                     //output = new FileOutputStream(localDirectory + "/" + file.getName());
                     //get the file from the remote system
                     //if (file.getName().equalsIgnoreCase("Radia_SampleFiles_20151111.xlsx"))
                     //{	
                     	//ftp.retrieveFile(file.getName(), output);
                     	//System.out.println("file name is " + file.getName() + " ---- timestamp is " + file.getTimestamp().getTime());
                     	
                     	/*
                     	CharSequence campaign = "379_Radia_Campaigns";
                     	CharSequence placement = "492_Radia_Placement_Details";
                     	CharSequence monthlySchedule = "494_Radia_Placements_Monthly";
                     	CharSequence advanced = "494_Radia_Advanced";
                     	 */
                     	CharSequence campaign = "379_";
                     	CharSequence placement = "492_";
                     	CharSequence monthlySchedule = "494_";
                     	CharSequence advanced = "488_";
                     
                     	boolean isCampaignFile = file.getName().contains(campaign);
                     	boolean isPlacementFile = file.getName().contains(placement);
                     	boolean isMonthlySchedule = file.getName().contains(monthlySchedule);
                         SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     	
                     	 
                     	 if (radiaFileList != null && radiaFileList.size()>0)
                     	 {
                     		 for (int i=0;i<radiaFileList.size();i++)
                     		 {
                     			Logger.log("------In remove file  ((RadiaFileDTO)radiaFileList.get(i)).getName() is " + ((RadiaFileDTO)radiaFileList.get(i)).getName(), true);
                     			Logger.log("--------in remove file, file.getName() is " +  file.getName(), true);
                     			 if (((RadiaFileDTO)radiaFileList.get(i)).getName().equalsIgnoreCase(file.getName()))
                     			{
                     				Logger.log("%%%%%%%%%%%file name matched", true);
                     				 ftp.rename(file.getName(), archiveDirectory + file.getName());
                     				 boolean exist = ftp.deleteFile(remoteDirectory +"/"+ file.getName());
                     				  if (exist)
                     					  Logger.log(remoteDirectory +"/"+  file.getName() + " is deleted", true);
                     				  else
                     					  Logger.log(remoteDirectory +"/"+  file.getName() + " is not deleted", true);
                     			}
                     				
                     		 }
                     		 
                     		   
                     	 } 
                     		 
                      
                     
                     
                     //delete the file
                     //ftp.deleteFile(file.getName());
                    
                 }//for loop
                 
             }//if (ftpFiles != null && ftpFiles.length > 0) {
           
            
             ftp.logout();
             ftp.disconnect();
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
			 
       	 
		}
         
    	 return result;
     }
     
     public boolean uploadFiles(String fileName) 
     {
    	 boolean result = true;
    	 //props = new Properties();
    	 
    	 ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
  		} catch (IOException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
         
  		FileInputStream fis = null;
  		FTPClient ftp = new FTPClient();
         try {

            /*
 			String serverAddress = "liveftp.groupm.com";
             String userId = "RadiaFTP";
             String password = "48XEwGmm";
             String remoteDirectory = "/Radia";
             String archiveDirectory = "/Radia/Archive/"; 
            */
             
        	 String serverAddress = propertyMap.get("ftp_server");
             String userId = propertyMap.get("ftp_userid");
             String password = propertyMap.get("ftp_password");
             String remoteDirectory = propertyMap.get("ftp_remotedirectory");
             String archiveDirectory = propertyMap.get("ftp_archivedirectory");
             //Logger.log("--------------archive directory is " + archiveDirectory, true);
 			
              
             
             //try to connect
             ftp.connect(serverAddress);
             //login to server
             if(!ftp.login(userId, password))
             {
                 ftp.logout();
                 //return false;
             }
             int reply = ftp.getReplyCode();
             //FTPReply stores a set of constants for FTP reply codes. 
             if (!FTPReply.isPositiveCompletion(reply))
             {
                 ftp.disconnect();
                 //return false;
             }

             //enter passive mode
             ftp.enterLocalPassiveMode();
             //get system name
             System.out.println("Remote system is " + ftp.getSystemType());
             //change current directory
             ftp.changeWorkingDirectory(remoteDirectory);
             System.out.println("Current directory is " + ftp.printWorkingDirectory());
              
             //String fileName = "tempLog.zip";
             fis = new FileInputStream(fileName);

             ftp.storeFile(archiveDirectory + fileName, fis);
             ftp.logout();
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
			 
       	 
		}finally {
		    try {
		        if (fis != null) {
		            fis.close();
		        }
		        ftp.disconnect();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
         
    	 return result;
     }

}