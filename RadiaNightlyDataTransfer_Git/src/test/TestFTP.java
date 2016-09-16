package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

import misc.FtpDownload;
import radia.RadiaFileDTO;

public class TestFTP {
	  
	@Test
	public void test() {
		 
		 
		List<RadiaFileDTO> radiaFileList =  new ArrayList<RadiaFileDTO>();
		FtpDownload ftp = new FtpDownload();
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
		  
		  Map<String, List<RadiaFileDTO>> radiaFileMap = new TreeMap<String, List<RadiaFileDTO>>(map); 
          
          Set set2 = radiaFileMap.entrySet();
          Iterator iterator2 = set2.iterator();
          while(iterator2.hasNext()) {
               Map.Entry me2 = (Map.Entry)iterator2.next();
               //System.out.print(me2.getKey() + ": ");
               //System.out.println(me2.getValue());
          }
          //System.out.println("After Sorting:");
		  
		  for (String s : radiaFileMap.keySet())
        {
              //System.out.println("-----fileMap key is " + s);
              
              radiaFileList = radiaFileMap.get(s);
              for (int n=0; n<radiaFileList.size();n++)
              {
              	//System.out.println("----file name is " + ((RadiaFileDTO)radiaFileList.get(n)).getName());
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
        }
		   
		  assertNotNull("File name is null!!!!", placementFileName); 
		  assertTrue("campaign file doesn't contain 492",placementFileName.contains("492")); 
		  assertTrue("No campaign file", hasPlacementDetailFile == true);
    
	}
	
	//@Test
	public void testFailure() throws Exception {
		List<RadiaFileDTO> radiaFileList =  new ArrayList<RadiaFileDTO>();
		FtpDownload ftp = new FtpDownload();
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
		  
		  Map<String, List<RadiaFileDTO>> radiaFileMap = new TreeMap<String, List<RadiaFileDTO>>(map); 
          
          Set set2 = radiaFileMap.entrySet();
          Iterator iterator2 = set2.iterator();
          while(iterator2.hasNext()) {
               Map.Entry me2 = (Map.Entry)iterator2.next();
               //System.out.print(me2.getKey() + ": ");
               //System.out.println(me2.getValue());
          }
          //System.out.println("After Sorting:");
		  
		  for (String s : radiaFileMap.keySet())
        {
              //System.out.println("-----fileMap key is " + s);
              
              radiaFileList = radiaFileMap.get(s);
              for (int n=0; n<radiaFileList.size();n++)
              {
              	//System.out.println("----file name is " + ((RadiaFileDTO)radiaFileList.get(n)).getName());
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
        }
	
	
	 
        assertTrue("No campaign file", hasCampaignFile);
		 //assertNotNull("File name is not null!!!!", placementFileName);
	}
}
