package sfdc;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.*;

import misc.LastRun;
import misc.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Buy_Placement__c;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.soap.enterprise.sobject.OpportunityMonthlySchedule__c;
import com.sforce.soap.enterprise.sobject.Opportunity_Buy__c;
import com.sforce.soap.enterprise.sobject.Placement_Monthly_Schedule__c;
import com.sforce.soap.enterprise.sobject.RecordType;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import radia.RadiaOpportunityDTO;


public class SfdcDAO {
	EnterpriseConnection sfconnection;
	
	
	
	public boolean deleteRadiaData(List<String> oppDeleteList, List<String> slDeleteList, List<String> bpDeleteList, List<String> slmsDeleteList, List<String> pmsDeleteList)  throws Exception {
        // TODO Auto-generated method stub
     
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  boolean result = false;
  if (sfservice.login() == true) {
          sfconnection = sfservice.GetSForceService();
	  
         
      	   
      	  //ArrayList<String> deleteOppIds = new ArrayList<String>(200);
          ArrayList<String> deleteIds = new ArrayList<String>(200);
      	  String inClause = "";
      	 if (oppDeleteList != null && oppDeleteList.size() > 0)
 		 {
      	  try
      	  {
      		  HashMap<String, String> radiaIdToOppMap = new HashMap<String, String>();
      			System.out.println("oppDeleteList is not null and size is " + oppDeleteList.size());
      			  inClause = listToString(oppDeleteList);
      		   
      		  String soqlOppQuery = "Select Id, Radia_Id__c FROM Opportunity where Radia_Id__c in ("+ inClause + ")";
      		   
      		   System.out.println("soqlOppQuery is " + soqlOppQuery);
      		   //Logger.log("soqlOppQuery   is " + soqlOppQuery, true);
      	        
      	       QueryResult queryResult = sfconnection.query(soqlOppQuery);
      	       
      	       boolean done = false;
      	       if (queryResult.getSize() > 0) {
      		      //  System.out.println("\nLogged-in user can see " + 
      		     //   		queryResult.getRecords().length + 
      		    //        " opp ids."
      		     //   );
      		        while (! done) {
      		          SObject[] records = queryResult.getRecords(); 
      		          for ( int i = 0; i < records.length; ++i ) {
      		            Opportunity opp = (Opportunity) records[i];
      		            String oppId = opp.getId();
      		            String radiaId = opp.getRadia_Id__c();
      		            radiaIdToOppMap.put(radiaId, oppId);
      		            {
      		              System.out.println("Opportunity " + (i + 1) + ": " + oppId);
      		            //deleteOppIds.add(oppId);
      		              deleteIds.add(oppId);
      		            }
      		          }
      		          if (queryResult.isDone()) {
      		            done = true;
      		          } else {
      		            queryResult = 
      		            		sfconnection.queryMore(queryResult.getQueryLocator());
      		          }
      		        }
      		      } else {
      		        System.out.println("No records found.");
      		        Logger.log("No records found.", true);
      		      }
      	       	 
      	    	 
      	     //batchDeleteRadiaData(deleteOppIds);
      	       //****************************************
      	       //log un-found delete to exception file
      	       for (int i=0; i<oppDeleteList.size();i++)
      	       {
      	    	   if (radiaIdToOppMap.get(oppDeleteList.get(i)) == null)
      	    	   {
      	    		   Logger.logException("Radia campaign with Radia Id " + oppDeleteList.get(i) + " is marked as delete but can not be found in Salesforce", true);
      	    	   }
      	       }
      	       
      	       //****************************************
      	       
      	    	 
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
 		  }//if (oppDeleteList != null && oppDeleteList.size() > 0)
      	
      	if (slDeleteList != null && slDeleteList.size() > 0)
		{
      	
      		Logger.log("---slDeleteList.size() is " + slDeleteList.size(), true);
          	
      		int batch_sl_deleteNum = 0;
      		ArrayList<String> action_delete = new ArrayList<String>(900);
      		 
      		for (int n = 0; n<slDeleteList.size(); n++)
			{
				
			action_delete.add(slDeleteList.get(n));
			if (action_delete.size() == 900)
			{
      		
      		try
    	  {
      		HashMap<String, String> radiaIdToSLMap = new HashMap<String, String>();  
      		inClause = listToString(action_delete);
    		  	  
    		  String soqlSLQuery = "Select Id, Radia_Id__c FROM Opportunity_Buy__c where Radia_Id__c in ("+ inClause + ")";
    		   
    		   System.out.println("soqlSLQuery is " + soqlSLQuery);
    		   Logger.log("soqlSLQuery   is " + soqlSLQuery, true);
    	        
    	       QueryResult queryResult = sfconnection.query(soqlSLQuery);
    	       
    	       boolean done = false;
    	       if (queryResult.getSize() > 0) {
    		        //System.out.println("\nLogged-in user can see " + 
    		        //		queryResult.getRecords().length + 
    		        //    " sl ids."
    		       // );
    		        while (! done) {
    		          SObject[] records = queryResult.getRecords(); 
    		          for ( int i = 0; i < records.length; ++i ) {
    		            Opportunity_Buy__c sl = (Opportunity_Buy__c) records[i];
    		            String slId = sl.getId();
    		            String radiaId = sl.getRadia_ID__c();
    		            radiaIdToSLMap.put(radiaId, slId);
    		            {
    		              System.out.println("Sell Line " + (i + 1) + ": " + slId);
    		            deleteIds.add(slId);
    		            }
    		          }
    		          if (queryResult.isDone()) {
    		            done = true;
    		          } else {
    		            queryResult = 
    		            		sfconnection.queryMore(queryResult.getQueryLocator());
    		          }
    		        }
    		      } else {
    		        System.out.println("No records found.");
    		        Logger.log("No records found.", true);
    		      }
    	       	 
    	    	 
    	     //batchDeleteRadiaData(deleteIds);
    	       
    	     //****************************************
      	       //log un-found delete to exception file
      	       for (int i=0; i<action_delete.size();i++)
      	       {
      	    	   if (radiaIdToSLMap.get(action_delete.get(i)) == null)
      	    	   {
      	    		   Logger.logException("Radia Sell Line with Radia Id " + action_delete.get(i) + " is marked as delete but can not be found in Salesforce", true);
      	    	   }
      	       }
      	       
      	       //****************************************
      	     action_delete.clear(); 
    	  }catch (Exception e) 
    	  {
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
      			batch_sl_deleteNum++;
			
			}//if (action_delete.size() == 900)
			}//for (int n = 0; n<slDeleteList.size(); n++)
      		if (action_delete.size() > 0)
			{
      			try
          	  {
            		HashMap<String, String> radiaIdToSLMap = new HashMap<String, String>();  
            		inClause = listToString(action_delete);
          		  	  
          		  String soqlSLQuery = "Select Id, Radia_Id__c FROM Opportunity_Buy__c where Radia_Id__c in ("+ inClause + ")";
          		   
          		   System.out.println("soqlSLQuery is " + soqlSLQuery);
          		   Logger.log("soqlSLQuery   is " + soqlSLQuery, true);
          	        
          	       QueryResult queryResult = sfconnection.query(soqlSLQuery);
          	       
          	       boolean done = false;
          	       if (queryResult.getSize() > 0) {
          		        //System.out.println("\nLogged-in user can see " + 
          		        //		queryResult.getRecords().length + 
          		         //   " sl ids."
          		       // );
          		        while (! done) {
          		          SObject[] records = queryResult.getRecords(); 
          		          for ( int i = 0; i < records.length; ++i ) {
          		            Opportunity_Buy__c sl = (Opportunity_Buy__c) records[i];
          		            String slId = sl.getId();
          		            String radiaId = sl.getRadia_ID__c();
          		            radiaIdToSLMap.put(radiaId, slId);
          		            {
          		              System.out.println("Sell Line " + (i + 1) + ": " + slId);
          		            deleteIds.add(slId);
          		            }
          		          }
          		          if (queryResult.isDone()) {
          		            done = true;
          		          } else {
          		            queryResult = 
          		            		sfconnection.queryMore(queryResult.getQueryLocator());
          		          }
          		        }
          		      } else {
          		        System.out.println("No records found.");
          		        Logger.log("No records found.", true);
          		      }
          	       	 
          	    	 
          	     //batchDeleteRadiaData(deleteIds);
          	     //****************************************
            	       //log un-found delete to exception file
            	       for (int i=0; i<action_delete.size();i++)
            	       {
            	    	   if (radiaIdToSLMap.get(action_delete.get(i)) == null)
            	    	   {
            	    		   Logger.logException("Radia Sell Line with Radia Id " + action_delete.get(i) + " is marked as delete but can not be found in Salesforce", true);
            	    	   }
            	       }
            	       
            	       //****************************************
          	    	 
          	  }catch (Exception e) 
          	  {
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
			}
      		Logger.log("-- batch_sl_deleteNum is " + batch_sl_deleteNum, true);
		}//if (slDeleteList != null && slDeleteList.size() > 0)   
      	
      	Logger.log("---after sl deletion, deleteIds.size() is " + deleteIds.size(), true);
      	
      	if (bpDeleteList != null && bpDeleteList.size() > 0)
		{
      		Logger.log("---bpDeleteList.size() is " + bpDeleteList.size(), true);
      	
      		int batch_bp_deleteNum = 0;
      		ArrayList<String> action_delete = new ArrayList<String>(900);
      		 
      		for (int n = 0; n<bpDeleteList.size(); n++)
			{
				
			action_delete.add(bpDeleteList.get(n));
			if (action_delete.size() == 900)
			{
      		
      		try
    	  {
      		HashMap<String, String> radiaIdToBPMap = new HashMap<String, String>();   
      		inClause = listToString(action_delete);
    		  	  
    		  String soqlBPQuery = "Select Id, Radia_Id__c FROM Buy_Placement__c where Radia_Id__c in ("+ inClause + ")";
    		   //Logger.logException("soqlBPQuery is " + soqlBPQuery, true);
    		   System.out.println("soqlBPQuery is " + soqlBPQuery);
    		   Logger.log("soqlBPQuery   is " + soqlBPQuery, true);
    	        
    	       QueryResult queryResult = sfconnection.query(soqlBPQuery);
    	       
    	       boolean done = false;
    	       if (queryResult.getSize() > 0) {
    		       // System.out.println("\nLogged-in user can see " + 
    		       // 		queryResult.getRecords().length + 
    		       //     " bp ids."
    		     //   );
    		        while (! done) {
    		          SObject[] records = queryResult.getRecords(); 
    		          for ( int i = 0; i < records.length; ++i ) {
    		            Buy_Placement__c bp = (Buy_Placement__c) records[i];
    		            String bpId = bp.getId();
    		            String radiaId = bp.getRadia_ID__c();
    		             
    		            radiaIdToBPMap.put(radiaId, bpId);
    		            //Logger.logException("in delete, found the record in sfdc with radia id " + radiaId + "--- sfdc id " + bpId, true);
    		            {
    		              System.out.println("Buy Placement " + (i + 1) + ": " + bpId);
    		            deleteIds.add(bpId);
    		            }
    		          }
    		          if (queryResult.isDone()) {
    		            done = true;
    		          } else {
    		            queryResult = 
    		            		sfconnection.queryMore(queryResult.getQueryLocator());
    		          }
    		        }
    		      } else {
    		        System.out.println("No records found.");
    		        Logger.log("No records found.", true);
    		      }
    	       	 
    	    	 
    	     //batchDeleteRadiaData(deleteIds);
    	       
    	     //****************************************
      	       //log un-found delete to exception file
      	       for (int i=0; i<action_delete.size();i++)
      	       {
      	    	   //Logger.logException("mark as delete, radia id is " + bpDeleteList.get(i), true);
      	    	   if (radiaIdToBPMap.get(action_delete.get(i)) == null)
      	    	   {
      	    		   Logger.logException("Radia Buy Placement with Radia Id " + action_delete.get(i) + " is marked as delete but can not be found in Salesforce", true);
      	    	   }
      	       }
      	       
      	       //****************************************
      	     action_delete.clear(); 
    	  }catch (Exception e) 
    	  {
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
      		batch_bp_deleteNum++;
			
			}//if (action_delete.size() == 900)
			}//for (int n = 0; n<bpDeleteList.size(); n++)
      		if (action_delete.size() > 0)
			{
      			try
          	  {
            		HashMap<String, String> radiaIdToBPMap = new HashMap<String, String>();   
            		inClause = listToString(action_delete);
          		  	  
          		  String soqlBPQuery = "Select Id, Radia_Id__c FROM Buy_Placement__c where Radia_Id__c in ("+ inClause + ")";
          		   //Logger.logException("soqlBPQuery is " + soqlBPQuery, true);
          		   System.out.println("soqlBPQuery is " + soqlBPQuery);
          		   Logger.log("soqlBPQuery   is " + soqlBPQuery, true);
          	        
          	       QueryResult queryResult = sfconnection.query(soqlBPQuery);
          	       
          	       boolean done = false;
          	       if (queryResult.getSize() > 0) {
          		        //System.out.println("\nLogged-in user can see " + 
          		       // 		queryResult.getRecords().length + 
          		       //     " bp ids."
          		      //  );
          		        while (! done) {
          		          SObject[] records = queryResult.getRecords(); 
          		          for ( int i = 0; i < records.length; ++i ) {
          		            Buy_Placement__c bp = (Buy_Placement__c) records[i];
          		            String bpId = bp.getId();
          		            String radiaId = bp.getRadia_ID__c();
          		             
          		            radiaIdToBPMap.put(radiaId, bpId);
          		            //Logger.logException("in delete, found the record in sfdc with radia id " + radiaId + "--- sfdc id " + bpId, true);
          		            {
          		              System.out.println("Buy Placement " + (i + 1) + ": " + bpId);
          		            deleteIds.add(bpId);
          		            }
          		          }
          		          if (queryResult.isDone()) {
          		            done = true;
          		          } else {
          		            queryResult = 
          		            		sfconnection.queryMore(queryResult.getQueryLocator());
          		          }
          		        }
          		      } else {
          		        System.out.println("No records found.");
          		        Logger.log("No records found.", true);
          		      }
          	       	 
          	    	 
          	     //batchDeleteRadiaData(deleteIds);
          	     //****************************************
            	       //log un-found delete to exception file
            	       for (int i=0; i<action_delete.size();i++)
            	       {
            	    	   //Logger.logException("mark as delete, radia id is " + bpDeleteList.get(i), true);
            	    	   if (radiaIdToBPMap.get(action_delete.get(i)) == null)
            	    	   {
            	    		   Logger.logException("Radia Buy Placement with Radia Id " + action_delete.get(i) + " is marked as delete but can not be found in Salesforce", true);
            	    	   }
            	       }
            	       
            	       //****************************************
          	    	 
          	  }catch (Exception e) 
          	  {
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
			}
      		Logger.log("--batch_bp_deleteNum is " + batch_bp_deleteNum, true);
			
		}//if (bpDeleteList != null && bpDeleteList.size() > 0)   
      	
      	Logger.log("---after bp deletion, deleteIds.size() is " + deleteIds.size(), true);
      	
      	if (slmsDeleteList != null && slmsDeleteList.size() > 0)
		{
      		int batch_slms_deleteNum = 0;
      		ArrayList<String> action_delete = new ArrayList<String>(900);
      		Logger.log("--size of slmsDeleteList is " + slmsDeleteList.size(), true);
      		for (int n = 0; n<slmsDeleteList.size(); n++)
			{
				
			action_delete.add(slmsDeleteList.get(n));
			if (action_delete.size() == 900)
			{
				try
				{
    		  inClause = listToString(action_delete);
    		  	  
    		  String soqlSLMSQuery = "Select Id, Radia_Id__c FROM OpportunityMonthlySchedule__c where Radia_Id__c in ("+ inClause + ")";
    		   
    		   System.out.println("soqlSLMSQuery is " + soqlSLMSQuery);
    		   Logger.log("soqlSLMSQuery   is " + soqlSLMSQuery, true);
    	        
    	       QueryResult queryResult = sfconnection.query(soqlSLMSQuery);
    	       
    	       boolean done = false;
    	       if (queryResult.getSize() > 0) {
    		       // System.out.println("\nLogged-in user can see " + 
    		      //  		queryResult.getRecords().length + 
    		      //      " slms ids."
    		       // );
    		        while (! done) {
    		          SObject[] records = queryResult.getRecords(); 
    		          for ( int i = 0; i < records.length; ++i ) {
    		        	  OpportunityMonthlySchedule__c slms = (OpportunityMonthlySchedule__c) records[i];
    		            String slmsId = slms.getId();
    		            {
    		              System.out.println("Sell Line Monthly Schedule " + (i + 1) + ": " + slmsId);
    		            deleteIds.add(slmsId);
    		            }
    		          }
    		          if (queryResult.isDone()) {
    		            done = true;
    		          } else {
    		            queryResult = 
    		            		sfconnection.queryMore(queryResult.getQueryLocator());
    		          }
    		        }
    		      } else {
    		        System.out.println("No records found.");
    		        Logger.log("No records found.", true);
    		      }
    	       	 
    	    	 
    	     //batchDeleteRadiaData(deleteIds);
    	       action_delete.clear();
    	    	 
    	  }
      	  catch (Exception e) 
      	  {
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
				batch_slms_deleteNum++;
				
			}//if (action_delete.size() == 900)
			}//for (int n = 0; n<slmsDeleteList.size(); n++)
      		if (action_delete.size() > 0)
			{	
      			try
				{
    		  inClause = listToString(action_delete);
    		  	  
    		  String soqlSLMSQuery = "Select Id, Radia_Id__c FROM OpportunityMonthlySchedule__c where Radia_Id__c in ("+ inClause + ")";
    		   
    		   System.out.println("soqlSLMSQuery is " + soqlSLMSQuery);
    		   Logger.log("soqlSLMSQuery   is " + soqlSLMSQuery, true);
    	        
    	       QueryResult queryResult = sfconnection.query(soqlSLMSQuery);
    	       
    	       boolean done = false;
    	       if (queryResult.getSize() > 0) {
    		        //System.out.println("\nLogged-in user can see " + 
    		        //		queryResult.getRecords().length + 
    		     //       " slms ids."
    		      //  );
    		        while (! done) {
    		          SObject[] records = queryResult.getRecords(); 
    		          for ( int i = 0; i < records.length; ++i ) {
    		        	  OpportunityMonthlySchedule__c slms = (OpportunityMonthlySchedule__c) records[i];
    		            String slmsId = slms.getId();
    		            {
    		              System.out.println("Sell Line Monthly Schedule " + (i + 1) + ": " + slmsId);
    		            deleteIds.add(slmsId);
    		            }
    		          }
    		          if (queryResult.isDone()) {
    		            done = true;
    		          } else {
    		            queryResult = 
    		            		sfconnection.queryMore(queryResult.getQueryLocator());
    		          }
    		        }
    		      } else {
    		        System.out.println("No records found.");
    		        Logger.log("No records found.", true);
    		      }
    	       	 
    	    	 
    	     //batchDeleteRadiaData(deleteIds);
    	    	 
    	  }
      	  catch (Exception e) 
      	  {
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
			}//if (action_delete.size() > 0)
      		
      		Logger.log("--batch_slms_deleteNum is " + batch_slms_deleteNum, true);
		}//if (slmsDeleteList != null && slmsDeleteList.size() > 0)
      	
      	Logger.log("---after slms deletion, deleteIds.size() is " + deleteIds.size(), true);
      	
      	if (pmsDeleteList != null && pmsDeleteList.size() > 0)
		{
      		int batch_pms_deleteNum = 0;
      		Logger.log("-- pmsDeleteList.size() is " + pmsDeleteList.size(), true);
      		ArrayList<String> action_delete = new ArrayList<String>(900);
      		for (int n = 0; n<pmsDeleteList.size(); n++)
			{
				
			action_delete.add(pmsDeleteList.get(n));
			if (action_delete.size() == 900)
			{
      		
      		try
    	    {
    		  inClause = listToString(action_delete);
    		  	  
    		  String soqlPMSQuery = "Select Id, Radia_Id__c FROM Placement_Monthly_Schedule__c where Radia_Id__c in ("+ inClause + ")";
    		   
    		   System.out.println("soqlPMSQuery is " + soqlPMSQuery);
    		   Logger.log("soqlPMSQuery   is " + soqlPMSQuery, true);
    	        
    	       QueryResult queryResult = sfconnection.query(soqlPMSQuery);
    	       
    	       boolean done = false;
    	       if (queryResult.getSize() > 0) {
    		        //System.out.println("\nLogged-in user can see " + 
    		       // 		queryResult.getRecords().length + 
    		       //     " pms ids."
    		     //   );
    		        while (! done) {
    		          SObject[] records = queryResult.getRecords(); 
    		          for ( int i = 0; i < records.length; ++i ) {
    		        	  Placement_Monthly_Schedule__c pms = (Placement_Monthly_Schedule__c) records[i];
    		            String pmsId = pms.getId();
    		            {
    		              System.out.println("Placement Monthly Schedule " + (i + 1) + ": " + pmsId);
    		            deleteIds.add(pmsId);
    		            }
    		          }
    		          if (queryResult.isDone()) {
    		            done = true;
    		          } else {
    		            queryResult = 
    		            		sfconnection.queryMore(queryResult.getQueryLocator());
    		          }
    		        }
    		      } else {
    		        System.out.println("No records found.");
    		        Logger.log("No records found.", true);
    		      }
    	       	 
    	    	 
    	     //batchDeleteRadiaData(deleteIds);
    	       action_delete.clear();
    	    	 
    	  }
      		catch (Exception e) 
      		{
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
      		batch_pms_deleteNum++;
			}//if (action_delete.size() == 900)
			}//for (int n = 0; n<pmsDeleteList.size(); n++)
      		if (action_delete.size() > 0)
			{
      			try
        	    {
        		  inClause = listToString(action_delete);
        		  	  
        		  String soqlPMSQuery = "Select Id, Radia_Id__c FROM Placement_Monthly_Schedule__c where Radia_Id__c in ("+ inClause + ")";
        		   
        		   System.out.println("soqlPMSQuery is " + soqlPMSQuery);
        		   Logger.log("soqlPMSQuery   is " + soqlPMSQuery, true);
        	        
        	       QueryResult queryResult = sfconnection.query(soqlPMSQuery);
        	       
        	       boolean done = false;
        	       if (queryResult.getSize() > 0) {
        		        //System.out.println("\nLogged-in user can see " + 
        		       // 		queryResult.getRecords().length + 
        		      //      " pms ids."
        		     //   );
        		        while (! done) {
        		          SObject[] records = queryResult.getRecords(); 
        		          for ( int i = 0; i < records.length; ++i ) {
        		        	  Placement_Monthly_Schedule__c pms = (Placement_Monthly_Schedule__c) records[i];
        		            String pmsId = pms.getId();
        		            {
        		              System.out.println("Placement Monthly Schedule " + (i + 1) + ": " + pmsId);
        		            deleteIds.add(pmsId);
        		            }
        		          }
        		          if (queryResult.isDone()) {
        		            done = true;
        		          } else {
        		            queryResult = 
        		            		sfconnection.queryMore(queryResult.getQueryLocator());
        		          }
        		        }
        		      } else {
        		        System.out.println("No records found.");
        		        Logger.log("No records found.", true);
        		      }
        	       	 
        	    	 
        	     //batchDeleteRadiaData(deleteIds);
        	    	 
        	  }
          		catch (Exception e) 
          		{
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
			}//if (action_delete.size() > 0)
      		Logger.log("-- batch_pms_deleteNum is " + batch_pms_deleteNum, true);
      		
      		
			
		}//if (pmsDeleteList != null && pmsDeleteList.size() > 0) 
      	
      	Logger.log("---after pms deletion, deleteIds.size() is " + deleteIds.size(), true);
      	
      	
      	batchDeleteRadiaData(deleteIds);
      	
  }
  			
  			return result;
        
  }
	
	public boolean batchDeleteRadiaData(ArrayList<String> idsDelete)
	  {
	  	  boolean result = true;
	  	  
	  	  
	  	  ArrayList<String> action_delete = new ArrayList<String>(200);
	  	  //String[] action_array = new String[200];
	  	  int batch_deleteNum = 0;
	  	  
	  	  if (idsDelete != null)
		{
			System.out.println("idsDelete has " + idsDelete.size());
			Logger.log("idsDelete has " + idsDelete.size(),true);
			for (int i = 0; i<idsDelete.size(); i++)
			{
				
				action_delete.add(idsDelete.get(i));
				if (action_delete.size() == 200)
				{
					try{
						 
						@SuppressWarnings("unchecked")
						Object ia[] = action_delete.toArray();   //get array
	     
	     
						//String[] stringArray = Arrays.copyOf(ia, ia.length, String[].class);
						String[] stringArray = Arrays.asList(ia).toArray(new String[ia.length]);
						/*
						String stringArray[]=new String[action_deleteInventories.size()];

						for (int j=0;j<stringArray.length;i++)
						{
							stringArray[j]=action_deleteInventories.get(j).toString();
						}*/
						deleteRecords(stringArray);
						action_delete.clear();
					}catch (Exception e) {
					Logger.log(e.getMessage(), true);
					e.printStackTrace();
					}
					batch_deleteNum++;
					System.out.println("batch_deleteNum is " + batch_deleteNum);
					Logger.log("batch_deleteNum is " + batch_deleteNum, true);
				}
			
			}
			if (action_delete.size() > 0)
			{	try {
				@SuppressWarnings("unchecked")
					Object ia[] = action_delete.toArray();   //get array
	   
					String[] stringArray = Arrays.asList(ia).toArray(new String[ia.length]);
					deleteRecords(stringArray);
				} catch (Exception e) {
					Logger.log(e.getMessage(), true);
					e.printStackTrace();
				}
			}
			
		}
	  	  
	  			
	  	  return result;
	  }
	    	 
	
	public void deleteRecords(String[] ids) {
		  try {
		    DeleteResult[] deleteResults = sfconnection.delete(ids);
		    for (int i = 0; i < deleteResults.length; i++) {
		      DeleteResult deleteResult = deleteResults[i];
		      if (deleteResult.isSuccess()) {
		        System.out.println("Deleted ID: " + 
		            deleteResult.getId()
		        );
		        Logger.log("Deleted ID: " + 
		            deleteResult.getId(), true);
		      } else {
		         
		        com.sforce.soap.enterprise.Error[] errors = deleteResult.getErrors();
		        if (errors.length > 0)  {
		          System.out.println("Error: could not delete " + 
		              " ID " + deleteResult.getId() + "."
		          );
		          Logger.log("Error: could not delete " + 
			              " ID " + deleteResult.getId() + ".", true);
		          System.out.println("   The error reported was: (" +
		              errors[0].getStatusCode() + ") " +
		              errors[0].getMessage() + "\n"
		          );
		        }
		      }
		    }
		  } catch (Exception e) {
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
		  
		}

	
	public String getOppHash(String radiaCampaignId)  throws Exception {
        // TODO Auto-generated method stub
     
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String oppSfdcHashStr = "";
   if (sfservice.login() == true) {
           sfconnection = sfservice.GetSForceService();
	  
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
  	      
  	      String soqlQuery = "select id, name, Radia_Id__c, Radia_Advertiser_Name__c, Radia_Budget__c, Radia_Budget_Approved__c, " + 
  	      	                  " Radia_Campaign_Start_Date__c, Radia_Campaign_End_Date__c " + 
  	      	                  " from Opportunity " + 
  	      	                  " where Radia_Id__c = '" + radiaCampaignId + "'";
  	      
  	     System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuery); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	        //System.out.println("\nLogged-in user can see " + 
  	        //    result.getRecords().length + 
  	       //     " opportunities."
  	       // );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	            Opportunity opp = (Opportunity) records[i];
  	            String name = opp.getName();
  	            String radiaAdvertiserName = opp.getRadia_Advertiser_Name__c();
  	            String radiaBudget = String.valueOf(opp.getRadia_Budget__c());
  	            String radiaBudgetApproved = String.valueOf(opp.getRadia_Budget_Approved__c());
  	            
  	            String radiaCampaignStartDate = "";
  	            if (opp.getRadia_Campaign_Start_Date__c() != null)
  	            {
  	            	radiaCampaignStartDate = sdf.format(opp.getRadia_Campaign_Start_Date__c().getTime());
  	            }
  	            String radiaCampaignEndDate = "";
  	            if (opp.getRadia_Campaign_End_Date__c() != null)
  	            {
  	            	radiaCampaignEndDate = sdf.format(opp.getRadia_Campaign_End_Date__c().getTime());
  	            } 
  	          oppSfdcHashStr = name + "--" + radiaAdvertiserName + "--" + radiaBudget + "--" +
  	        		  		radiaBudgetApproved + "--" + radiaCampaignStartDate + "--" +  radiaCampaignEndDate;
  	            
  	            System.out.println("oppSfdcHashStr is " + oppSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No opportunity is retrieved.");
  	    	  Logger.log("No opportunity is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } 
        catch (Exception e) {
			 e.printStackTrace();
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 Logger.log(errors.toString(),true);
			 Logger.logTechOps(errors.toString(),true);
			 for (StackTraceElement ste : Thread.currentThread().getStackTrace()) 
         	 {
         		System.out.println(ste);
         		Logger.log(ste+"\n", true);
         		//Logger.logTechOps(ste+"\n", true);
         	 }
         	 
		}
        /*
        catch (ConnectionException ce) {
  	      ce.printStackTrace();
  	    }*/
        
              
   }
  		 
        return oppSfdcHashStr;
  }
	
	public RadiaOpportunityDTO getOppToUpdate(String radiaCampaignId)  throws Exception {
        // TODO Auto-generated method stub
		String oppId = "";
		RadiaOpportunityDTO oppDto = new RadiaOpportunityDTO();
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  
  if (sfservice.login() == true) {
       sfconnection = sfservice.GetSForceService();
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
        	String soqlQuery = "select Id, AccountId, StageName  from Opportunity where Radia_Id__c =  '" + radiaCampaignId+ "'";
  	      
  	      
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuery); 
  	      boolean done = false;
  	      
  	    
	   		
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	        //System.out.println("\nLogged-in user can see " + 
  	          //  result.getRecords().length + 
  	         //   " opportunities."
  	       // );
  	        
  	        while (! done) 
  	        {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) 
  	          {
  	            Opportunity opp = (Opportunity) records[i];
  	            //oppId = opp.getId();
  	            oppDto.setOppId(opp.getId());
  	            oppDto.setAccountId(opp.getAccountId());
  	            oppDto.setStage(opp.getStageName());
  	             
  	          }
  	          if (result.isDone()) 
  	          {
  	            done = true;
  	          } 
  	          else 
  	          {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No opportunity is retrieved.");
  	    	  Logger.log("*****************No opportunity is retrieved for radia id " + radiaCampaignId, true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  }
  		 
        return oppDto;
  }
	
	/*
	public String getOppToUpdate(String radiaCampaignId)  throws Exception {
        // TODO Auto-generated method stub
		String oppId = "";
		
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String oppSfdcHashStr = "";
  if (sfservice.login() == true) {
        sfconnection = sfservice.GetSForceService();
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  
        	//String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  	//				 " Where o.StageName = 'Closed Won' "+ 
  		  	//				 " AND o.Campaign_Start_Date__c != null " + 
  		  	//				 " AND o.CloseDate != null " +
  		  	//				 " AND o.AccountId = '001M0000009GYCi'" + 
  		  	//				 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	       
        	String soqlQuery = "select Id, AccountId from Opportunity where Radia_Id__c =  '" + radiaCampaignId+ "'";
  	      
  	      
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuery); 
  	      boolean done = false;
  	      
  	    
	   		
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	        System.out.println("\nLogged-in user can see " + 
  	            result.getRecords().length + 
  	            " opportunities."
  	        );
  	        
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	            Opportunity opp = (Opportunity) records[i];
  	            oppId = opp.getId();
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No opportunity is retrieved.");
  	    	  Logger.log("No opportunity is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (ConnectionException ce) {
  	      ce.printStackTrace();
  	    }
              
  }
  		 
        return oppId;
  }
	*/
	
	public String getSellLineHash(String radiaId)  throws Exception {
        // TODO Auto-generated method stub
     
  //SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String sellLineSfdcHashStr = "";
  //if (sfservice.login() == true) {
    //    EnterpriseConnection sfconnection = sfservice.GetSForceService();
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
  	      
  	      String soqlQuery = "select id, Radia_Id__c, Buy_Name_txt__c, PlacementId__c, Supplier_Code__c, Supplier_Name__c, Line_Type__c, Media_Name__c, " +
  	    		  			 " MO_Advertiser_Name__c, MO_Advertiser_Code__c, MO_Product_Code__c, MO_Product_Name__c, " + 
  	    		  			 " MO_Estimate_Code__c, MO_Estimate_Name__c, Radia_Buyline_Name__c, Category__c, Start_Date__c, End_Date__c, " +
  	    		  			 " Last_Modified_By__c, Site__c, Net_Rate_Type__c, Buy_Type__c, Unit_Type__c, Rate__c, Vendor_IO_Number__c, " + 
  	    		  			 " Served_By__c, Supplier_Product_Name__c, Last_Changed_in_Radia__c, Adserver_Name__c, PackageID__c, PackageType__c, " + 
  	    		  			 " Sell_Line_Creation_Date__c, Sell_Line_Creating_User__c, Line_Item_Type__c, BudgetLineId__c, Margin__c, Margin_Value__c " + 
  	      	                  " from Opportunity_Buy__c " + 
  	      	                  " where Radia_Id__c = '" + radiaId + "'";
  	      
  	    
    	
		  
			 
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuery); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	        //System.out.println("\nLogged-in user can see " + 
  	       //     result.getRecords().length + 
  	      //      " sell lines."
  	      //  );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	            Opportunity_Buy__c sl = (Opportunity_Buy__c) records[i];
  	            String name = sl.getBuy_Name_txt__c();
  	            String sfRadiaId = sl.getRadia_ID__c();
  	            String supplierCode = sl.getSupplier_Code__c();
  	            String supplierName = sl.getSupplier_Name__c();
  	            String lineType = sl.getLine_Type__c();
  	            String mediaName = sl.getMedia_Name__c();
  	            String mOAdvertiserName = sl.getMO_Advertiser_Name__c();
  	            String mOAdvertiserCode = sl.getMO_Advertiser_Code__c();
  	            String mOProductName = sl.getMO_Product_Name__c();
  	            String mOProductCode = sl.getMO_Product_Code__c();
  	            String mOEstimateName = sl.getMO_Estimate_Name__c();
  	            String mOEstimateCode = sl.getMO_Estimate_Code__c();
  	            String category = sl.getCategory__c();
  	            String lastModifiedBy = sl.getLast_Modified_By__c();
  	            String site = sl.getSite__c();
  	            String netRateType = sl.getNet_Rate_Type__c();
  	            String unitType = sl.getUnit_Type__c();
  	            String rate = String.valueOf(sl.getRate__c());
  	            String vendorIONumber = sl.getVendor_IO_Number__c();
  	            String servedBy = sl.getServed_By__c();
  	            String supplierProductName = sl.getSupplier_Product_Name__c();
  	            String lastChangeInRadia = "";
  	            if (sl.getLast_Changed_in_Radia__c() != null)
  	            {
  	            	lastChangeInRadia = sdf.format(sl.getLast_Changed_in_Radia__c().getTime());
  	            }
  	            String advertiserName = sl.getAdserver_Name__c();
  	            String packageId = sl.getPackageID__c();
  	            String packageType = sl.getPackageType__c();
  	            String sellLineCreationDate = "";
  	            if (sl.getSell_Line_Creation_Date__c() != null)
  	            {
  	            	sellLineCreationDate = sdf.format(sl.getSell_Line_Creation_Date__c().getTime());
  	            }
  	            String sellLineCreatingUser = sl.getSell_Line_Creating_User__c();
  	            String lineItemType = sl.getLine_Item_Type__c();
  	             
  	            String margin = String.valueOf(sl.getMargin__c());
  	            String marginValue = String.valueOf(sl.getMargin_Value__c());
  	            
  	            
  	            
  	           
  	            String radiaSLStartDate = sdf.format(sl.getStart_Date__c().getTime());
  	            String radiaSLEndDate = sdf.format(sl.getEnd_Date__c().getTime());
  	            
  	          sellLineSfdcHashStr = name + "--" + sfRadiaId + "--" + supplierCode + "--" + supplierName + "--" +
  	        		lineType + "--" + mediaName + "--" +  mOAdvertiserName + "--" + mOAdvertiserCode + "--" + mOProductName + "--" + 
  	        		mOProductCode + "--" + mOEstimateName + "--" + mOEstimateCode + "--" + category + "--" + lastModifiedBy + "--" + 
  	        		site + "--" + netRateType + "--" + unitType + "--" + rate + "--" + vendorIONumber + "--" + servedBy + "--" + 
  	        		supplierProductName + "--" + lastChangeInRadia + "--" + advertiserName + "--" + packageId + "--" + packageType + "--" +
  	        		sellLineCreationDate + "--" + sellLineCreatingUser + "--" + lineItemType + "--" +  margin + "--" + 
  	        		marginValue + "--" + radiaSLStartDate + "--" + radiaSLEndDate;
  	            
  	            System.out.println("sellLineSfdcHashStr is " + sellLineSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No sell line is retrieved.");
  	    	  Logger.log("No sell line is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
 // }
  		 
        return sellLineSfdcHashStr;
  }
	
	public HashMap<String, String> getSellLineHashMap(String oppId)  throws Exception {
        // TODO Auto-generated method stub
     
  //SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  HashMap<String, String> sellLineSfdcHashMap = new HashMap<String, String>();
  String sellLineSfdcHashStr = "";
  //if (sfservice.login() == true) {
    //    EnterpriseConnection sfconnection = sfservice.GetSForceService();
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
  	      
  	      String soqlQuery = "select id, Radia_Id__c, Buy_Name_txt__c, PlacementId__c, Supplier_Code__c, Supplier_Name__c, Line_Type__c, Media_Name__c, " +
  	    		  			 " MO_Advertiser_Name__c, MO_Advertiser_Code__c, MO_Product_Code__c, MO_Product_Name__c, " + 
  	    		  			 " MO_Estimate_Code__c, MO_Estimate_Name__c, Radia_Buyline_Name__c, Category__c, Start_Date__c, End_Date__c, " +
  	    		  			 " Last_Modified_By__c, Site__c, Net_Rate_Type__c, Buy_Type__c, Unit_Type__c, Rate__c, Vendor_IO_Number__c, " + 
  	    		  			 " Served_By__c, Special_Product__c, Supplier_Product_Name__c, Last_Changed_in_Radia__c, Adserver_Name__c, PackageID__c, PackageType__c, " + 
  	    		  			 " Sell_Line_Creation_Date__c, Sell_Line_Creating_User__c, Line_Item_Type__c, BudgetLineId__c, Margin__c, Margin_Value__c, " + 
  	    		  			 " AD_Labs__c, Agency_Billing_Platform__c, Media_Code__c, Creative_Detail__c, Creative_Format__c, " +
  	    		  			 " Data_Tactic__c, Device__c, Inventory__c, Network_Billing_Platform__c, Primary_KPI__c, Product_Code__c " + 
  	      	                  " from Opportunity_Buy__c " + 
  	      	                  " where Opportunity__c = '" + oppId + "'";
  	      
  	    
    	
		  
			 
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuery); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	       // System.out.println("\nLogged-in user can see " + 
  	      //      result.getRecords().length + 
  	       //     " sell lines."
  	      //  );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	            Opportunity_Buy__c sl = (Opportunity_Buy__c) records[i];
  	            String name = sl.getBuy_Name_txt__c();
  	            String sfRadiaId = sl.getRadia_ID__c();
  	            String supplierCode = sl.getSupplier_Code__c();
  	            String supplierName = sl.getSupplier_Name__c();
  	            String lineType = sl.getLine_Type__c();
  	            String mediaName = sl.getMedia_Name__c();
  	            String mOAdvertiserName = sl.getMO_Advertiser_Name__c();
  	            String mOAdvertiserCode = sl.getMO_Advertiser_Code__c();
  	            String mOProductName = sl.getMO_Product_Name__c();
  	            String mOProductCode = sl.getMO_Product_Code__c();
  	            String mOEstimateName = sl.getMO_Estimate_Name__c();
  	            String mOEstimateCode = sl.getMO_Estimate_Code__c();
  	            String category = sl.getCategory__c();
  	            String lastModifiedBy = sl.getLast_Modified_By__c();
  	            String site = sl.getSite__c();
  	            String netRateType = sl.getNet_Rate_Type__c();
  	            String unitType = sl.getUnit_Type__c();
  	            String rate = String.valueOf(sl.getRate__c());
  	            String vendorIONumber = sl.getVendor_IO_Number__c();
  	            String servedBy = sl.getServed_By__c();
  	            //String supplierProductName = sl.getSupplier_Product_Name__c();
  	            String supplierProductName = sl.getSpecial_Product__c(); //PW 2016-05-10 On sell line, speical product name populated on Special_Product__c
	            String lastChangeInRadia = "";
  	            if (sl.getLast_Changed_in_Radia__c() != null)
  	            {
  	            	lastChangeInRadia = sdf.format(sl.getLast_Changed_in_Radia__c().getTime());
  	            }
  	            String advertiserName = sl.getAdserver_Name__c();
  	            String packageId = sl.getPackageID__c();
  	            String packageType = sl.getPackageType__c();
  	            String sellLineCreationDate = "";
  	            if (sl.getSell_Line_Creation_Date__c() != null)
  	            {
  	            	sellLineCreationDate = sdf.format(sl.getSell_Line_Creation_Date__c().getTime());
  	            }
  	            String sellLineCreatingUser = sl.getSell_Line_Creating_User__c();
  	            String lineItemType = sl.getLine_Item_Type__c();
  	             
  	            String margin = String.valueOf(sl.getMargin__c());
  	            String marginValue = String.valueOf(sl.getMargin_Value__c());
  	            String radiaSLStartDate = sdf.format(sl.getStart_Date__c().getTime());
  	            String radiaSLEndDate = sdf.format(sl.getEnd_Date__c().getTime());
  	            
  	            String adLabs = sl.getAD_Labs__c();
  			    String agencyBillingPlatform = sl.getAgency_Billing_Platform__c();
  			    String channel = sl.getMedia_Code__c();
  			    String creativeDetail = sl.getCreative_Detail__c();
  			    String creativeFormat = sl.getCreative_Format__c();
  			    String dataTactic = sl.getData_Tactic__c();
  			    String device = sl.getDevice__c();
  			    String inventory = sl.getInventory__c();
  			    String networkBillingPlatform = sl.getNetwork_Billing_Platform__c();
  			    String primaryKpi = sl.getPrimary_KPI__c();
  			    String productCode = sl.getProduct_Code__c(); 
  	            
  	            
  	          sellLineSfdcHashStr = name + "--" + sfRadiaId + "--" + supplierCode + "--" + supplierName + "--" +
  	        		lineType + "--" + mediaName + "--" +  mOAdvertiserName + "--" + mOAdvertiserCode + "--" + mOProductName + "--" + 
  	        		mOProductCode + "--" + mOEstimateName + "--" + mOEstimateCode + "--" + category + "--" + lastModifiedBy + "--" + 
  	        		site + "--" + netRateType + "--" + unitType + "--" + rate + "--" + vendorIONumber + "--" + servedBy + "--" + 
  	        		supplierProductName + "--" + lastChangeInRadia + "--" + advertiserName + "--" + packageId + "--" + packageType + "--" +
  	        		sellLineCreationDate + "--" + sellLineCreatingUser + "--" + lineItemType + "--" +  margin + "--" + 
  	        		marginValue + "--" + radiaSLStartDate + "--" + radiaSLEndDate + 
  	        		"--" + adLabs + 
  	        		"--" + agencyBillingPlatform + 
  	        		"--" + channel + 
  	        		"--" + creativeDetail + 
  	        		"--" + creativeFormat + 
  	        		"--" + dataTactic + 
  	        		"--" + device + 
  	        		"--" + inventory + 
  	        		"--" + networkBillingPlatform + 
  	        		"--" + primaryKpi + 
  	        		"--" + productCode
  	        		;
  	            
  	            System.out.println("sellLineSfdcHashStr is " + sellLineSfdcHashStr);
  	          sellLineSfdcHashMap.put(sfRadiaId, sellLineSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No sell line is retrieved.");
  	    	  Logger.log("No sell line is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
 // }
  		 
        return sellLineSfdcHashMap;
  }
	
	
	public String getSLToUpdate(String radiaId)  throws Exception {
        // TODO Auto-generated method stub
		String slId = "";
  //SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
   
 // if (sfservice.login() == true) {
     //   EnterpriseConnection sfconnection = sfservice.GetSForceService();
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
        	String soqlQuery = "select Id from Opportunity_Buy__c where Radia_Id__c =  '" + radiaId+ "'";
  	      
  	      
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuery); 
  	      boolean done = false;
  	      
  	     
  	      if (result.getSize() > 0) {
  	        //System.out.println("\nLogged-in user can see " + 
  	        //    result.getRecords().length + 
  	       //     " Sell Lines."
  	       // );
  	        
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	        	Opportunity_Buy__c sl = (Opportunity_Buy__c) records[i];
  	            slId = sl.getId();
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No Sell Line is retrieved.");
  	    	  Logger.log("No Sell Line is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  //}
  		 
        return slId;
  }
	
	public String getBuyPlacementHash(String radiaId)  throws Exception {
        // TODO Auto-generated method stub
     
  //SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String buyPlacementSfdcHashStr = "";
  //if (sfservice.login() == true) {
     //   EnterpriseConnection sfconnection = sfservice.GetSForceService();
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
  	      
  	      String soqlQueryGetBuyPlacementHash = "select id, Radia_Id__c, PlacementId__c, Supplier_Code__c, Supplier_Name__c,  " +
  	    		  			 " MO_Advertiser_Name__c, MO_Advertiser_Code__c, MO_Product_Code__c, MO_Product_Name__c, " + 
  	    		  			 " MO_Estimate_Code__c, MO_Estimate_Name__c, Radia_Buyline_Name__c, Start_Date__c, End_Date__c, " +
  	    		  			 " Last_Modified_By__c, Site__c, Unit_Type__c, Rate__c, Vendor_IO_Number__c, " + 
  	    		  			 " Served_By__c, Supplier_Product_Name__c, Last_Changed_in_Radia__c, Adserver_Name__c, PackageID__c, PackageType__c, " + 
  	    		  			 " Buyline_Creation_Date__c, Buyline_Creating_User__c, Line_Item_Type__c, BudgetLineId__c, Margin__c, Margin_Value__c, Comments__c " + 
  	      	                  " from Buy_Placement__c " + 
  	      	                  " where Radia_Id__c = '" + radiaId + "'";
  	      
  	    
    	
		  
			 
  	     //System.out.println(" soqlQueryGetBuyPlacementHash is " +  soqlQueryGetBuyPlacementHash);
  	    	QueryResult result = sfconnection.query(soqlQueryGetBuyPlacementHash); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	       // System.out.println("\nLogged-in user can see " + 
  	      //      result.getRecords().length + 
  	       //     " Buy Placements."
  	       // );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	            Buy_Placement__c bp = (Buy_Placement__c) records[i];
  	            
  	            String sfRadiaId = bp.getRadia_ID__c();
  	            String supplierCode = bp.getSupplier_Code__c();
  	            String supplierName = bp.getSupplier_Name__c();
  	            
  	            String mOAdvertiserName = bp.getMO_Advertiser_Name__c();
  	            String mOAdvertiserCode = bp.getMO_Advertiser_Code__c();
  	            String mOProductName = bp.getMO_Product_Name__c();
  	            String mOProductCode = bp.getMO_Product_Code__c();
  	            String mOEstimateName = bp.getMO_Estimate_Name__c();
  	            String mOEstimateCode = bp.getMO_Estimate_Code__c();
  	             
  	            String lastModifiedBy = bp.getLast_Modified_By__c();
  	            String site = bp.getSite__c();
  	            
  	            String unitType = bp.getUnit_Type__c();
  	            String rate = String.valueOf(bp.getRate__c());
  	            String vendorIONumber = bp.getVendor_IO_Number__c();
  	            String servedBy = bp.getServed_By__c();
  	            String supplierProductName = bp.getSupplier_Product_Name__c();
  	            String lastChangeInRadia = "";
  	            if(bp.getLast_Changed_in_Radia__c() != null)
  	            {
  	            	lastChangeInRadia = sdf.format(bp.getLast_Changed_in_Radia__c().getTime());
  	            }
  	            String advertiserName = bp.getAdserver_Name__c();
  	            String packageId = bp.getPackageID__c();
  	            String packageType = bp.getPackageType__c();
  	            String sellLineCreationDate = sdf.format(bp.getBuyline_Creation_Date__c().getTime());
  	            String sellLineCreatingUser = bp.getBuyline_Creating_User__c();
  	            String lineItemType = bp.getLine_Item_Type__c();
  	             
  	            String margin = String.valueOf(bp.getMargin__c());
  	            String marginValue = String.valueOf(bp.getMargin_Value__c());
  	            String comments = bp.getComments__c();
  	            
  	            String radiaBPStartDate = sdf.format(bp.getStart_Date__c().getTime());
  	            String radiaBPEndDate = sdf.format(bp.getEnd_Date__c().getTime());
  	            
  	            
  	            
  	          buyPlacementSfdcHashStr =  sfRadiaId + "--" + supplierCode + "--" + supplierName + "--" +
  	        		mOAdvertiserName + "--" + mOAdvertiserCode + "--" + mOProductName + "--" + 
  	        		mOProductCode + "--" + mOEstimateName + "--" + mOEstimateCode + "--" + lastModifiedBy + "--" + 
  	        		site + "--" + unitType + "--" + rate + "--" + vendorIONumber + "--" + servedBy + "--" + 
  	        		supplierProductName + "--" + lastChangeInRadia + "--" + advertiserName + "--" + packageId + "--" + packageType + "--" +
  	        		sellLineCreationDate + "--" + sellLineCreatingUser + "--" + lineItemType + "--" +  margin + "--" + 
  	        		marginValue + "--" + comments + "--" + radiaBPStartDate + "--" + radiaBPEndDate;
  	            
  	            System.out.println("buyPlacementSfdcHashStr is " + buyPlacementSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No sell line is retrieved.");
  	    	  Logger.log("No sell line is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  //}
  		 
        return buyPlacementSfdcHashStr;
  }
	
	public HashMap<String, String> getBuyPlacementHashMap(String oppId)  throws Exception {
        // TODO Auto-generated method stub
     HashMap<String, String> buyPlacementSfdcHashMap = new HashMap<String, String>();
  //SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String buyPlacementSfdcHashStr = "";
  //if (sfservice.login() == true) {
     //   EnterpriseConnection sfconnection = sfservice.GetSForceService();
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
  	      
  	      String soqlQueryGetBuyPlacementHash = "select id, Radia_Id__c, PlacementId__c, Supplier_Code__c, Supplier_Name__c,  " +
  	    		  			 " MO_Advertiser_Name__c, MO_Advertiser_Code__c, MO_Product_Code__c, MO_Product_Name__c, " + 
  	    		  			 " MO_Estimate_Code__c, MO_Estimate_Name__c, Radia_Buyline_Name__c, Start_Date__c, End_Date__c, " +
  	    		  			 " Last_Modified_By__c, Site__c, Unit_Type__c, Rate__c, Vendor_IO_Number__c, " + 
  	    		  			 " Served_By__c, Supplier_Product_Name__c, Last_Changed_in_Radia__c, Adserver_Name__c, PackageID__c, PackageType__c, " + 
  	    		  			 " Buyline_Creation_Date__c, Buyline_Creating_User__c, Line_Item_Type__c, BudgetLineId__c, Margin__c, Margin_Value__c, Comments__c,  " + 
  	    		  			 " AD_Labs__c, Agency_Billing_Platform__c, Channel__c, Creative_Detail__c, Creative_Format__c, " +
	    		  			 " Data_Tactic__c, Device__c, Inventory__c, Network_Billing_Platform__c, Primary_KPI__c, Product_Code__c " + 
	      	                 " from Buy_Placement__c " + 
  	      	                 " where Sell_Line__r.Opportunity__c = '" + oppId + "'";
  	      
  	    
    	
		  
			 
  	     //System.out.println(" soqlQueryGetBuyPlacementHash is " +  soqlQueryGetBuyPlacementHash);
  	    	QueryResult result = sfconnection.query(soqlQueryGetBuyPlacementHash); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	      //  System.out.println("\nLogged-in user can see " + 
  	      //      result.getRecords().length + 
  	      //      " Buy Placements."
  	     //   );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	            Buy_Placement__c bp = (Buy_Placement__c) records[i];
  	            
  	            String sfRadiaId = bp.getRadia_ID__c();
  	            String supplierCode = bp.getSupplier_Code__c();
  	            String supplierName = bp.getSupplier_Name__c();
  	            
  	            String mOAdvertiserName = bp.getMO_Advertiser_Name__c();
  	            String mOAdvertiserCode = bp.getMO_Advertiser_Code__c();
  	            String mOProductName = bp.getMO_Product_Name__c();
  	            String mOProductCode = bp.getMO_Product_Code__c();
  	            String mOEstimateName = bp.getMO_Estimate_Name__c();
  	            String mOEstimateCode = bp.getMO_Estimate_Code__c();
  	             
  	            String lastModifiedBy = bp.getLast_Modified_By__c();
  	            String site = bp.getSite__c();
  	            
  	            String unitType = bp.getUnit_Type__c();
  	            String rate = String.valueOf(bp.getRate__c());
  	            String vendorIONumber = bp.getVendor_IO_Number__c();
  	            String servedBy = bp.getServed_By__c();
  	            String supplierProductName = bp.getSupplier_Product_Name__c();
  	            String lastChangeInRadia = "";
  	            if(bp.getLast_Changed_in_Radia__c() != null)
  	            {
  	            	lastChangeInRadia = sdf.format(bp.getLast_Changed_in_Radia__c().getTime());
  	            }
  	            String advertiserName = bp.getAdserver_Name__c();
  	            String packageId = bp.getPackageID__c();
  	            String packageType = bp.getPackageType__c();
  	            
  	            String sellLineCreationDate = "";
  	            if (bp.getBuyline_Creation_Date__c() != null)
  	            {
  	            	sellLineCreationDate = sdf.format(bp.getBuyline_Creation_Date__c().getTime());
  	            }
  	            String sellLineCreatingUser = bp.getBuyline_Creating_User__c();
  	            String lineItemType = bp.getLine_Item_Type__c();
  	             
  	            String margin = String.valueOf(bp.getMargin__c());
  	            String marginValue = String.valueOf(bp.getMargin_Value__c());
  	            String comments = bp.getComments__c();
  	            
  	            String radiaBPStartDate = sdf.format(bp.getStart_Date__c().getTime());
  	            String radiaBPEndDate = sdf.format(bp.getEnd_Date__c().getTime());
  	            
  	            String adLabs = bp.getAD_Labs__c();
			    String agencyBillingPlatform = bp.getAgency_Billing_Platform__c();
			    String channel = bp.getChannel__c();
			    String creativeDetail = bp.getCreative_Detail__c();
			    String creativeFormat = bp.getCreative_Format__c();
			    String dataTactic = bp.getData_Tactic__c();
			    String device = bp.getDevice__c();
			    String inventory = bp.getInventory__c();
			    String networkBillingPlatform = bp.getNetwork_Billing_Platform__c();
			    String primaryKpi = bp.getPrimary_KPI__c();
			    String productCode = bp.getProduct_Code__c(); 
  	            
  	          buyPlacementSfdcHashStr =  sfRadiaId + "--" + supplierCode + "--" + supplierName + "--" +
  	        		mOAdvertiserName + "--" + mOAdvertiserCode + "--" + mOProductName + "--" + 
  	        		mOProductCode + "--" + mOEstimateName + "--" + mOEstimateCode + "--" + lastModifiedBy + "--" + 
  	        		site + "--" + unitType + "--" + rate + "--" + vendorIONumber + "--" + servedBy + "--" + 
  	        		supplierProductName + "--" + lastChangeInRadia + "--" + advertiserName + "--" + packageId + "--" + packageType + "--" +
  	        		sellLineCreationDate + "--" + sellLineCreatingUser + "--" + lineItemType + "--" +  margin + "--" + 
  	        		marginValue + "--" + comments + "--" + radiaBPStartDate + "--" + radiaBPEndDate + 
  	        		"--" + adLabs +  
  	        		"--" + agencyBillingPlatform + 
  	        		"--" + channel +  
  	        		"--" + creativeDetail +  
  	        		"--" + creativeFormat +  
  	        		"--" + dataTactic +  
  	        		"--" + device +  
  	        		"--" + inventory +  
  	        		"--" + networkBillingPlatform +  
  	        		"--" + primaryKpi +  
  	        		"--" + productCode;
  	            
  	            System.out.println("buyPlacementSfdcHashStr is " + buyPlacementSfdcHashStr);
  	            
  	          buyPlacementSfdcHashMap.put(sfRadiaId, buyPlacementSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No sell line is retrieved.");
  	    	  Logger.log("No sell line is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  //}
  		 
        return buyPlacementSfdcHashMap;
  }
	
	public String getBPToUpdate(String radiaId)  throws Exception {
        // TODO Auto-generated method stub
		String bpId = "";
  //SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
   
  //if (sfservice.login() == true) {
      //  EnterpriseConnection sfconnection = sfservice.GetSForceService();
        try {
  	    
        	String soqlQuery = "select Id from Buy_Placement__c where Radia_Id__c =  '" + radiaId+ "'";
  	      
  	      
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuery); 
  	      boolean done = false;
  	      
  	     
  	      if (result.getSize() > 0) {
  	        //System.out.println("\nLogged-in user can see " + 
  	       //     result.getRecords().length + 
  	       //     " Buy Placements."
  	      //  );
  	        
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	        	Buy_Placement__c bp = (Buy_Placement__c) records[i];
  	            bpId = bp.getId();
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No Buy Placement is retrieved.");
  	    	  Logger.log("No Buy Placement is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
 // }
  		 
        return bpId;
  }
	
	public String getSellLineMonthlyScheduleHash(String radiaId)  throws Exception {
        // TODO Auto-generated method stub
     
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String slmsSfdcHashStr = "";
  //if (sfservice.login() == true) {
    //      sfconnection = sfservice.GetSForceService();
	  
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
  	      
  	      String soqlQuerySLMS = "select id, name, Radia_Start_Date__c, Radia_End_Date__c, Date__c, Gross_Budget_Editable__c " + 
  	      	                  " from OpportunityMonthlySchedule__c " + 
  	      	                  " where Radia_Id__c = '" + radiaId + "'";
  	      
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuerySLMS); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	       // System.out.println("\nLogged-in user can see " + 
  	       //     result.getRecords().length + 
  	       //     " sell line monthly schedule."
  	       // );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	        	OpportunityMonthlySchedule__c slms = (OpportunityMonthlySchedule__c) records[i];
  	            String date = sdf.format(slms.getDate__c().getTime());;
  	            
  	            String cost = String.valueOf(slms.getGross_Budget_Editable__c()); 
  	            
  	           
  	            String startDate = sdf.format(slms.getRadia_Start_Date__c().getTime());
  	            String endDate = sdf.format(slms.getRadia_End_Date__c().getTime());
  	            
  	          slmsSfdcHashStr = date + "--" + cost + "--" + startDate + "--" + endDate;
  	            
  	            System.out.println("slmsSfdcHashStr is " + slmsSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No Sell Line Monthly Schedule is retrieved.");
  	    	  Logger.log("No Sell Line Monthly Schedule is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  //}
  		 
        return slmsSfdcHashStr;
  }
	
	public HashMap<String, String> getSellLineMonthlyScheduleHashMap(String oppId)  throws Exception {
        // TODO Auto-generated method stub
   HashMap<String, String> slmsSfdcHashMap = new HashMap<String, String>();  
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String slmsSfdcHashStr = "";
  //if (sfservice.login() == true) {
    //      sfconnection = sfservice.GetSForceService();
	  
        try {
  	      //String soqlQuery = "SELECT FirstName, LastName FROM Contact limit 100";
  		  /*
        	String soqlQuery = "Select o.Name, o.Id, o.AccountId, o.CreatedDate, o.Campaign_Start_Date__c, o.CloseDate, o.Campaign_End_Date__c From Opportunity o " + 
  		  					 " Where o.StageName = 'Closed Won' "+ 
  		  					 " AND o.Campaign_Start_Date__c != null " + 
  		  					 " AND o.CloseDate != null " +
  		  					 " AND o.AccountId = '001M0000009GYCi'" + 
  		  					 " Order by o.AccountId, o.Campaign_Start_Date__c, o.CloseDate, o.CreatedDate ";
  	      */
  	      
  	      String soqlQuerySLMS = "select id, name, RadiaIdTemp__c, Radia_Start_Date__c, Radia_End_Date__c, Date__c, Planned_Billable_Units__c, Gross_Budget_Editable__c " + 
  	      	                  " from OpportunityMonthlySchedule__c " + 
  	      	                  " where Opportunity__c = '" + oppId + "'";
  	      
  	     //System.out.println(" soqlQuery is " +  soqlQuery);
  	    	QueryResult result = sfconnection.query(soqlQuerySLMS); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	        //System.out.println("\nLogged-in user can see " + 
  	        //    result.getRecords().length + 
  	        //    " sell line monthly schedule."
  	       // );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	        	OpportunityMonthlySchedule__c slms = (OpportunityMonthlySchedule__c) records[i];
  	            String date = sdf.format(slms.getDate__c().getTime());;
  	            
  	            String cost = String.valueOf(slms.getGross_Budget_Editable__c()); 
  	            String plannedBillableUnits = String.valueOf(slms.getPlanned_Billable_Units__c());
  	           
  	            String startDate = ""; 
  	            if (slms.getRadia_Start_Date__c() != null)
  	            {
  	            	startDate = sdf.format(slms.getRadia_Start_Date__c().getTime());
  	            }
  	            String endDate = "";
  	            if (slms.getRadia_End_Date__c() != null)
  	            {
  	            	endDate = sdf.format(slms.getRadia_End_Date__c().getTime());
  	            }
  	            String radiaId = slms.getRadiaIdTemp__c();
  	            
  	          slmsSfdcHashStr = cost + "--" + plannedBillableUnits + "--" + startDate + "--" + endDate;
  	            
  	            System.out.println("slmsSfdcHashStr is " + slmsSfdcHashStr);
  	          slmsSfdcHashMap.put(radiaId, slmsSfdcHashStr);  
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No Sell Line Monthly Schedule is retrieved.");
  	    	  Logger.log("No Sell Line Monthly Schedule is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  //}
  		 
        return slmsSfdcHashMap;
  }
	
	public String getPlacementMonthlyScheduleHash(String radiaId)  throws Exception {
        // TODO Auto-generated method stub
     
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String pmsSfdcHashStr = "";
  //if (sfservice.login() == true) {
   //       sfconnection = sfservice.GetSForceService();
	  
        try {
  	      
  	      
  	      String soqlQueryPMS = "select id, name, Start_Date__c, End_Date__c, Placement_Monthly_Schedule_Type__c, Radia_Planned_Billable_Cost__c, " + 
  	    		  			  " Radia_Planned_Payable_Cost__c,  Radia_Planned_Units__c, PlannedFees__c, " + 
  	    		  			  " Radia_Supplier_Billable_Units__c " +
  	      	                  " from Placement_Monthly_Schedule__c " + 
  	      	                  " where Radia_Id__c = '" + radiaId + "'";
  	      
  	     	QueryResult result = sfconnection.query(soqlQueryPMS); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	       // System.out.println("\nLogged-in user can see " + 
  	       //     result.getRecords().length + 
  	       //     " placement monthly schedule."
  	       // );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	        	Placement_Monthly_Schedule__c pms = (Placement_Monthly_Schedule__c) records[i];
  	        	String packageType = pms.getPlacement_Monthly_Schedule_Type__c();
  	            String billablePlannedAmount = String.valueOf(pms.getRadia_Planned_Billable_Cost__c());
  	            String plannedAmount = String.valueOf(pms.getRadia_Planned_Payable_Cost__c());
  	            String plannedUnits = String.valueOf(pms.getRadia_Planned_Units__c());
  	            String supplierUnits = String.valueOf(pms.getRadia_Supplier_Billable_Units__c());
  	            String plannedFee = String.valueOf(pms.getPlannedFees__c());
  	        	
  	           
  	            String startDate = sdf.format(pms.getStart_Date__c().getTime());
  	            String endDate = sdf.format(pms.getEnd_Date__c().getTime());
  	            
  	          pmsSfdcHashStr = packageType + "--" + billablePlannedAmount + "--" + plannedAmount + "--" + plannedUnits + "--" + supplierUnits + "--"+ plannedFee + "--" + startDate + "--" + endDate;
  	            
  	            System.out.println("pmsSfdcHashStr is " + pmsSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No Placement Monthly Schedule is retrieved.");
  	    	  Logger.log("No Placement Monthly Schedule is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  //}
  		 
        return pmsSfdcHashStr;
  }
	
	public HashMap<String, String> getPlacementMonthlyScheduleHashMap(String oppId)  throws Exception {
        // TODO Auto-generated method stub
		HashMap<String, String> pmsSfdcHashMap = new HashMap<String, String>();  
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String pmsSfdcHashStr = "";
  //if (sfservice.login() == true) {
   //       sfconnection = sfservice.GetSForceService();
	  
        try {
  	      
  	      
  	      String soqlQueryPMS = "select id, name, Start_Date__c, End_Date__c, Placement_Monthly_Schedule_Type__c, Radia_Planned_Billable_Cost__c, " + 
  	    		  			  " Radia_Planned_Payable_Cost__c,  RadiaIdTemp__c, Radia_Planned_Units__c, PlannedFees__c, " + 
  	    		  			  " Radia_Supplier_Billable_Units__c " +
  	      	                  " from Placement_Monthly_Schedule__c " + 
  	      	                  " where buy_placement__r.sell_line__r.opportunity__c = '" + oppId + "'";
  	      
  	     	QueryResult result = sfconnection.query(soqlQueryPMS); 
  	      boolean done = false;
  	      
  	    String DATE_FORMAT = "yyyy-MM-dd";
  	    //String DATE_FORMAT = "yyyy-M-d";
    	   
	   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  	      
     	   
     	   		 
  	      
  	      if (result.getSize() > 0) {
  	       // System.out.println("\nLogged-in user can see " + 
  	       //     result.getRecords().length + 
  	       //     " placement monthly schedule."
  	       // );
  	        while (! done) {
  	          SObject[] records = result.getRecords();
  	          for ( int i = 0; i < records.length; ++i ) {
  	        	Placement_Monthly_Schedule__c pms = (Placement_Monthly_Schedule__c) records[i];
  	        	String packageType = pms.getPlacement_Monthly_Schedule_Type__c();
  	            String billablePlannedAmount = String.valueOf(pms.getRadia_Planned_Billable_Cost__c());
  	            String plannedAmount = String.valueOf(pms.getRadia_Planned_Payable_Cost__c());
  	            String plannedUnits = String.valueOf(pms.getRadia_Planned_Units__c());
  	            String supplierUnits = String.valueOf(pms.getRadia_Supplier_Billable_Units__c());
  	            String plannedFee = String.valueOf(pms.getPlannedFees__c());
  	        	
  	           
  	            String startDate = sdf.format(pms.getStart_Date__c().getTime());
  	            String endDate = sdf.format(pms.getEnd_Date__c().getTime());
  	            String radiaId = pms.getRadiaIdTemp__c();
  	            
  	          pmsSfdcHashStr = packageType + "--" + billablePlannedAmount + "--" + plannedAmount + "--" + plannedUnits + "--" + supplierUnits + "--"+ plannedFee + "--" + startDate + "--" + endDate;
  	            
  	            System.out.println("pmsSfdcHashStr is " + pmsSfdcHashStr);
  	            
  	          pmsSfdcHashMap.put(radiaId, pmsSfdcHashStr);
  	             
  	          }
  	          if (result.isDone()) {
  	            done = true;
  	          } else {
  	            result = 
  	            		sfconnection.queryMore(result.getQueryLocator());
  	          }
  	        }
  	      } else {
  	    	  System.out.println("No Placement Monthly Schedule is retrieved.");
  	    	  Logger.log("No Placement Monthly Schedule is retrieved.", true);
  				//System.exit(-1);
  	      }
  	      System.out.println("\nQuery succesfully executed.");
  	    } catch (Exception e) {
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
              
  //}
  		 
        return pmsSfdcHashMap;
  }
	
	public String getBuyPlacementPartnershipEngagement(String supplierCode)  throws Exception {
        // TODO Auto-generated method stub
     
  SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  HashMap<String, String> radiaSupplierIdAccountIdMap = new HashMap<String, String>();
  String accountId = "";
  //if (sfservice.login() == true) {
   //       sfconnection = sfservice.GetSForceService();
	     
          try {
    	     RecordType rt = null;
        	 
    		 String soqlQueryGetPartnershipEngagementRT = "Select Name, Id From RecordType where Name = 'Xaxis Partnership Engagement'";
    		 String partnershipEngagementRTId = "";
    	      
            QueryResult queryResultGetPartnershipEngagementRT = sfconnection.query(soqlQueryGetPartnershipEngagementRT);
        	 	if (queryResultGetPartnershipEngagementRT.getSize() > 0)
        	 	{
        	 		SObject[] rtRecord = queryResultGetPartnershipEngagementRT.getRecords();
        	 		for ( int m = 0; m < rtRecord.length; ++m ) {
        	 			rt = (RecordType) rtRecord[m];
        	 			partnershipEngagementRTId = rt.getId();
                	 
        	 		}
        	 	}
        	 	
        	 	String soqlQuery = "select Id, name, Radia_Supplier_ID__c from Account where RecordTypeId = '" + partnershipEngagementRTId+ "'";
        	     
        	     
         	    //System.out.println(" soqlQuery is " +  soqlQuery);
         	   	QueryResult result = sfconnection.query(soqlQuery); 
         	     boolean done = false;
         	     
         	    
         	     if (result.getSize() > 0) {
         	       
         	       
         	       while (! done) {
         	         SObject[] records = result.getRecords();
         	         for ( int i = 0; i < records.length; ++i ) {
         	       	Account acct = (Account) records[i];
         	       	radiaSupplierIdAccountIdMap.put(acct.getRadia_Supplier_ID__c(), acct.getId());
         	            
         	         }
         	         if (result.isDone()) {
         	           done = true;
         	         } else {
         	           result = 
         	           		sfconnection.queryMore(result.getQueryLocator());
         	         }
         	       }
         	     } else {
         	   	 System.out.println("No Account is retrieved.");
         	   	 Logger.log("No Account is retrieved.", true);
         				//System.exit(-1);
         	     }
         	     System.out.println("\nQuery succesfully executed.");
        	 	
         	    if (supplierCode != null && !supplierCode.equals("")) 
                 {
                   if (radiaSupplierIdAccountIdMap.get(supplierCode) != null)
                   {
                     
                     accountId = radiaSupplierIdAccountIdMap.get(supplierCode);
                      
                      
                   }
                 }
        	 	
    	 }catch (ConnectionException ce) {
    	     ce.printStackTrace();
    	   }
              
  //}
  		 
        return accountId;
  }

//Only fetch parentid that has no child	
public  List<String>   fetchAll(String oppId) throws Exception {
	 
	ArrayList<String> ids = new ArrayList<String>();    
	Set<String> pmsSet = new HashSet<String>();
	Set<String> pmsWithChildSet = new HashSet<String>();
	SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
	if (sfservice.login() == true) 
	{
        sfconnection = sfservice.GetSForceService();
		
		  try 
		  {
			  String soqlQueryFetchPMS = "Select Id FROM Placement_Monthly_Schedule__c where Placement_Monthly_Schedule_Type__c = 'Package' and buy_placement__r.Sell_Line__r.Opportunity__c = '" + oppId+"'";
			  
	          QueryResult queryResultGetPMS = sfconnection.query(soqlQueryFetchPMS);
	 	  	if (queryResultGetPMS.getSize() > 0)
	 	  	{
	 	  		SObject[] records = queryResultGetPMS.getRecords();
	 	  		for ( int i = 0; i < records.length; ++i ) 
	 	  		{
	 	  			Placement_Monthly_Schedule__c pms = (Placement_Monthly_Schedule__c) records[i];
	 	  			pmsSet.add(pms.getId());
	         	     //ids.add(pms.getId());
				}
	 	  	}
	 	  	
	 	  	String soqlQueryFetchPMSWithChild = "select Package_Monthly_Schedule__c from Placement_Monthly_Schedule__c where buy_placement__r.Sell_Line__r.Opportunity__c = '" + oppId+"'";
			  
	          QueryResult queryResultGetPMSWithChild = sfconnection.query(soqlQueryFetchPMSWithChild);
	 	  	if (queryResultGetPMSWithChild.getSize() > 0)
	 	  	{
	 	  		SObject[] records = queryResultGetPMSWithChild.getRecords();
	 	  		for ( int i = 0; i < records.length; ++i ) 
	 	  		{
	 	  			 
	 	  			Placement_Monthly_Schedule__c pms = (Placement_Monthly_Schedule__c) records[i];
	 	  			
	 	  			pmsWithChildSet.add(pms.getPackage_Monthly_Schedule__c()); 
	         	     //ids.add(pms.getId());
				}
	 	  	}
	 	  	
	 	  	//for(String p : pmsWithChildSet)
	 	  	//{
	 	  		
	 	  	//	Logger.log("pms with child " + p, true);
	 	  	//}
	 	  	
	 	  	if (pmsSet != null)
			{
				if (pmsSet.size() > 0)
				{
					for(String pid : pmsSet)
					{
						//Logger.log(" pmsSet has " + pid, true);
						if (!pmsWithChildSet.contains(pid))
						{
							ids.add(pid);
							//Logger.log(" not in pmsWithChildSet " + pid, true);
						}
					}
				}
			}
	 	  	
	 	  	
	 	  	
		  }
		  catch (Exception e) 
		  {
		      e.printStackTrace();
		  }
		  
		  try 
		  {
			  //String soqlQueryFetchSLMS = "Select Id FROM OpportunityMonthlySchedule__c where Opportunity__c = '" + oppId+"'";
			  
			  String soqlQueryFetchSLMS = "select id from opportunitymonthlyschedule__c where opportunity__c = '" + oppId+"' and id not in (select Sell_Line_Monthly_Schedule__c from Placement_Monthly_Schedule__c where buy_placement__r.Sell_Line__r.Opportunity__c = '" + oppId+"')";
					  
			  
	          QueryResult queryResultGetSLMS = sfconnection.query(soqlQueryFetchSLMS);
	 	  	if (queryResultGetSLMS.getSize() > 0)
	 	  	{
	 	  		SObject[] records = queryResultGetSLMS.getRecords();
	 	  		for ( int i = 0; i < records.length; ++i ) 
	 	  		{
	 	  			OpportunityMonthlySchedule__c slms = (OpportunityMonthlySchedule__c) records[i];
	         	     ids.add(slms.getId());
				}
	 	  	}
	 	  	
		  }
		  catch (Exception e) 
		  {
		      e.printStackTrace();
		  }
		  
		  try 
		  {
			  //String soqlQueryFetchBP = "select id from buy_placement__c where Sell_Line__r.Opportunity__c = '" + oppId+"'";
			  String soqlQueryFetchBP = "select id from buy_placement__c where Sell_Line__r.Opportunity__c = '" + oppId+"' and id not in (select Buy_Placement__c from placement_monthly_schedule__c where buy_placement__r.Sell_Line__r.Opportunity__c = '" + oppId+"')";
			  
	          QueryResult queryResultGetBP = sfconnection.query(soqlQueryFetchBP);
	 	  	if (queryResultGetBP.getSize() > 0)
	 	  	{
	 	  		SObject[] records = queryResultGetBP.getRecords();
	 	  		for ( int i = 0; i < records.length; ++i ) 
	 	  		{
	 	  			Buy_Placement__c bp = (Buy_Placement__c) records[i];
	         	     ids.add(bp.getId());
				}
	 	  	}
	 	  	
		  }
		  catch (Exception e) 
		  {
		      e.printStackTrace();
		  }
		  
		  try 
		  {
			  //String soqlQueryFetchSL = "select id from Opportunity_Buy__c where opportunity__c = '" + oppId+"'";
			  String soqlQueryFetchSL = "select id from Opportunity_Buy__c where opportunity__c = '" + oppId+"' and id not in (select Opportunity_Buy__c from OpportunityMonthlySchedule__c where opportunity__c = '" + oppId+"')";
	          QueryResult queryResultGetSL = sfconnection.query(soqlQueryFetchSL);
	 	  	if (queryResultGetSL.getSize() > 0)
	 	  	{
	 	  		SObject[] records = queryResultGetSL.getRecords();
	 	  		for ( int i = 0; i < records.length; ++i ) 
	 	  		{
	 	  			Opportunity_Buy__c sl = (Opportunity_Buy__c) records[i];
	         	     ids.add(sl.getId());
				}
	 	  	}
	 	  	
	 	  	 
	 	  	
		  }
		  catch (Exception e) 
		  {
		      e.printStackTrace();
		  }
	}
		    
		    return ids;
}
	
	
	public String listToString(List<String> testList)
    {
    	String returnString = "'";
    	for (int i = 0; i<testList.size(); i++)
    	{
    		returnString = returnString +testList.get(i)+"','";
    		
    		
    	}
    	returnString = returnString.substring(0,returnString.length()-2);
    	System.out.println("returnString is " + returnString);
    	return returnString;
    }
}
