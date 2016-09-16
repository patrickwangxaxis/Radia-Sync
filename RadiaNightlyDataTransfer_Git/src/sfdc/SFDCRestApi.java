package sfdc;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.*;

import misc.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Buy_Placement__c;
import com.sforce.soap.enterprise.sobject.Opportunity;
import com.sforce.soap.enterprise.sobject.Opportunity_Buy__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import controller.Controller;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import radia.RadiaBuyPlacementDTO;
import radia.RadiaOpportunityDTO;
import radia.RadiaPlacementMonthlyScheduleDTO;
import radia.RadiaSLMonthlyScheduleDTO;
import radia.RadiaSellLineDTO;


public class SFDCRestApi {
	
	 
	
	public String upsertOpportunity(RadiaOpportunityDTO oppDto)  throws Exception {
        // TODO Auto-generated method stub
    System.out.println("in SFDCRestAPI class, upsertOpportunity method");    
    SForceServiceGlobal sfservice= new SForceServiceGlobal(null);
  String jsonString = "";
  
  Logger.log("in SFDCRestApi class, ", true);
  for (int n = 0; n<oppDto.getSellLineData().size(); n++)
  {
	  Logger.log("oppDto--" + oppDto.getSellLineData().get(n).getRadiaId(), true);
  }
  
  
  
  if (sfservice.login() == true) {
        EnterpriseConnection connection = sfservice.GetSForceService();
         
              try {
                    String sessionId = connection.getSessionHeader().getSessionId();
                    assert  sessionId != null;
                    String sfURL = sfservice.GetInitialRestServiceURL() +"/apexrest/RadiaOpportunity";
                     
                    System.out.println("sfURL is " + sfURL);
                    Logger.log("sfURL is " + sfURL, true);
                    ClientRequest request = new ClientRequest(sfURL);
                    //request.header(HttpHeaders.AUTHORIZATION, "Bearer " + sessionId);
              request.accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + sessionId);
              
              
              
              ClientResponse <String> response = request.get(String.class);
              jsonString = response.getEntity();
              System.out.println("before  if (response.getStatus() != 200)");
              System.out.println("response.getStatus() is " + response.getStatus());
              /*
              if (response.getStatus() != 200) {
                  //throw new ImpactBusinessException(TranslationKey.ERROR, "Response status "+ response.getStatus());
            	  throw new Exception();
              }
              
              */
            GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
            Gson gson = gsonBuilder.create();
            System.out.println("after Gson gson = gsonBuilder.create();");
            
            //Type responseRadiaOpportunityType = new TypeToken<SFBasicResponseDto<RadiaOpportunityDTO>>() {}.getType();
            //SFBasicResponseDto<RadiaOpportunityDTO> dto = gson.fromJson(jsonString, responseRadiaOpportunityType);
              
              //if (!dto.getSuccess()) {
             //     throw new ImpactBusinessException(TranslationKey.ERROR, dto.getMessage());
              //}
              
              //System.out.println("\nRegistered user can see "  + dto.getData().size() + " records.");

              //for (final RadiaOpportunityDTO item : dto.getData()) {
              //      item.getName();
                    //System.out.println(item.getName() + "- " + item.getId());
              //}
              
              //POST test request
              //Boolean ispost = false;
              Boolean ispost = true;
              if (ispost) {
                    System.out.println("\nRSending POST :");
                     
                    request.clear();
                    response = null;
                    
                    //prepare data
                    SFBasicRequestDto reqOpp = new SFBasicRequestDto();
                     
                    RadiaOpportunityDTO opp = new RadiaOpportunityDTO();
                     
                    //opp.setAccountId("001L000000lrMFe");
                    //opp.setName("Radia Campaign Test 2015-11-06 5:13 pm");
                    //opp.setStage("Contract Modification");
                    //opp.setRadiaCampaignId("900000");
                    
                    Date dt = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                     
                    String dtStr =  sdf.format(dt);
                    //opp.setRadiaCampaignStartDate(dtStr);
                    //opp.setRadiaCampaignEndDate(dtStr);
                    
                    
                    
                    boolean changed = false;
                    
                    List<RadiaOpportunityDTO> oppList = new ArrayList<>(); 
                    
                    //compare incoming campaign value with existing sfdc opp value. if nothing change, don't send to the api call
                    
                    String oppRadiaHashStr = "";
                    String name = oppDto.getName();
      	            String radiaAdvertiserName = oppDto.getRadiaAdvertiserName();
      	            String radiaBudget = String.valueOf(oppDto.getRadiaBudget());
      	            String radiaBudgetApproved = String.valueOf(oppDto.getRadiaBudgetApproved());
      	            String radiaCampaignStartDate = String.valueOf(oppDto.getRadiaCampaignStartDate());
      	            String radiaCampaignEndDate = String.valueOf(oppDto.getRadiaCampaignEndDate());
      	            
                    oppRadiaHashStr = name + "--" + radiaAdvertiserName + "--" + radiaBudget + "--" +
  	        		  		radiaBudgetApproved + "--" + radiaCampaignStartDate + "--" +  radiaCampaignEndDate;
  	                System.out.println("oppRadiaHashStr is " + oppRadiaHashStr);
  	                Logger.log("oppRadiaHashStr is " + oppRadiaHashStr, true);
                    
  	                SfdcDAO sfdcDao = new SfdcDAO();
  	                String oppSfdcHashStr = ""; 
  	                oppSfdcHashStr = sfdcDao.getOppHash(oppDto.getRadiaCampaignId());
  	              Logger.log("oppSfdcHashStr is " + oppSfdcHashStr, true);
  	              
  	               RadiaOpportunityDTO existingOppDto = new RadiaOpportunityDTO();
  	               
  	             existingOppDto = sfdcDao.getOppToUpdate(oppDto.getRadiaCampaignId());
  	             String oppId = "";
  	             
  	           //if (existingOppDto.getOppId() == null)
  	           //{
  	        	// Logger.logException("Radia Campaign " + oppDto.getName() + " with Radia Id " +oppDto.getRadiaCampaignId() + " doesn't exist in Salesforce!!!!!!!!!!!!", true);
    	           
  	           //}
  	           //else
  	           //{
  	             if (existingOppDto.getOppId() != null && existingOppDto.getAccountId() !=null && existingOppDto.getStage() != null)
  	             {
  	            	 oppId = existingOppDto.getOppId();
  	            	 Logger.log("**********oppId is " + oppId + "****** before set account id,  existingOppDto.getAccountId() is " + existingOppDto.getAccountId() + "****** existingOppDto.getStage() is " + existingOppDto.getStage(), true);
  	            	 
  	            	 oppDto.setAccountId(existingOppDto.getAccountId());
  	            	 oppDto.setStage(existingOppDto.getStage());
  	             }
  	             else
  	             {
  	            	 //oppDto.setAccountId("0018A000003HlRh");//global qa account id
  	            	 //oppDto.setAccountId("001c000000zc4qa");//staging account id
  	  	             //oppDto.setStage("Contract Modification"); 
  	            	 
  	             }
  	             
  	             
  	             
  	              	//oppDto.setAccountId("001L000000lrMFe");
  	              	//oppDto.setAccountId("00156000002sEuy");
  	              	//oppDto.setAccountId("0018A000003HlRh");
  	              
  	            
  	              	//oppDto.setStage("Contract Modification");
  	                //String oppId = sfdcDao.getOppToUpdate(oppDto.getRadiaCampaignId());
  	              	//oppDto.setOppId("006L0000008O5REIA0");
  	                if (oppId != null && !oppId.equals(""))
  	                {
  	                	oppDto.setOppId(oppId);
  	                	
  	                	 //********************************************
  	    			  //populate the map before records get deleted for all the child ids
  	    			    List<String> parentIds = new ArrayList();
  	    			   
  	    	  	        	   parentIds = sfdcDao.fetchAll(oppId);
  	    	  	        	   //for (String p : parentIds)
  	    	  	        	   //{
  	    	  	        		//   Logger.log("-------parent id has no child  " + p, true );
  	    	  	        	   //}
  	    	  	        	   
  	    	  	       if (parentIds != null && parentIds.size() > 0)
  	    	  	       {
  	    	  	    	   oppDto.setParentIdList(parentIds);
  	    	  	       }
  	    			  //********************************************
  	                }
  	                
  	               boolean upsertOpp;
  	                
  	               if (!oppRadiaHashStr.equalsIgnoreCase(oppSfdcHashStr))
  	               {
  	            	 upsertOpp = true;  
                     oppDto.setNeedUpsert(upsertOpp);
                     changed = true;
                    
  	               }
  	               else
  	               {
  	            	 upsertOpp = false;
  	            	   oppDto.setNeedUpsert(upsertOpp);
  	               }
  	                
  	              //*************************************************************************
  	              //To get SFDC info in a map
  	             HashMap<String, String> sellLineSfdcHashMap = new HashMap<String, String>();
  	             if (existingOppDto.getOppId() != null )
  	             {
  	            	 
  	            	 sellLineSfdcHashMap = sfdcDao.getSellLineHashMap(oppId);
  	             }
  	             
  	             HashMap<String, String> buyPlacementSfdcHashMap = new HashMap<String, String>();
	             if (existingOppDto.getOppId() != null )
	             {
	            	 
	            	 buyPlacementSfdcHashMap = sfdcDao.getBuyPlacementHashMap(oppId);
	             }
	             
	             HashMap<String, String> slmsSfdcHashMap = new HashMap<String, String>();
	             if (existingOppDto.getOppId() != null )
	             {
	            	 
	            	 slmsSfdcHashMap = sfdcDao.getSellLineMonthlyScheduleHashMap(oppId);
	             }
	             
	             HashMap<String, String> pmsSfdcHashMap = new HashMap<String, String>();
	             if (existingOppDto.getOppId() != null )
	             {
	            	 
	            	 pmsSfdcHashMap = sfdcDao.getPlacementMonthlyScheduleHashMap(oppId);
	             }
  	               
  	              //************************************************************************
  	               
  	               
  	               if (oppDto.getSellLineData() != null)
  	               {
  	            	 System.out.println("--place 1, oppDto.getSellLineData().size() is " + oppDto.getSellLineData().size());
  	            	   //RadiaSellLineDTO radiaSLDto = new RadiaSellLineDTO();
  	            	 List<RadiaSellLineDTO> objs = oppDto.getSellLineData();
  	            	Iterator<RadiaSellLineDTO> i = objs.iterator();
  	            	while (i.hasNext()) {
  	            		RadiaSellLineDTO radiaSLDto = i.next();
  	            	  //some condition
  	            	    
  	            	
  	            	   //for (int i = 0; i < oppDto.getSellLineData().size(); i++)
  	            	   //{
  	            		 //radiaSLDto = oppDto.getSellLineData().get(i);
  	            		 String sellLineRadiaHashStr = ""; 
                    	
                    	String slName = radiaSLDto.getPlacementName();
          	            String sfRadiaId = radiaSLDto.getRadiaId();
          	          String supplierCode = "";
          	          if (radiaSLDto.getSupplierCode() !=null && !(radiaSLDto.getSupplierCode().equals("")))
          	          {	  
          	        	supplierCode = trimLeadingZeros(radiaSLDto.getSupplierCode());
          	          }
          	              
          	            String supplierName = radiaSLDto.getSupplierName();
          	            String lineType = radiaSLDto.getLineType();
          	            String mediaName = radiaSLDto.getMediaName();
          	            String mOAdvertiserName = radiaSLDto.getMOAdvertiserName();
          	            String mOAdvertiserCode = radiaSLDto.getMOAdvertiserCode();
          	            String mOProductName = radiaSLDto.getMOProductName();
          	            String mOProductCode = radiaSLDto.getMOProductCode();
          	            String mOEstimateName = radiaSLDto.getMOEstimateName();
          	            String mOEstimateCode = radiaSLDto.getMOEstimateCode();
          	            String category = radiaSLDto.getCategory();
          	            String lastModifiedBy = radiaSLDto.getLastModifiedBy();
          	            String site = radiaSLDto.getSite();
          	            String netRateType = radiaSLDto.getCostMethod();
          	            String unitType = radiaSLDto.getUnitType();
          	            String rate = String.valueOf(radiaSLDto.getRate());
          	            String vendorIONumber = radiaSLDto.getVendorIONumber();
          	            String radiaServedBy = radiaSLDto.getServedBy();
          	            String radiaSupplierProductName = radiaSLDto.getSupplierProductName();
          	            if (radiaSupplierProductName == null)
	      	            {
          	            	radiaSupplierProductName = "null";
	      	            }
          	            String lastChangeInRadia = radiaSLDto.getLastChangedInRadia();
          	            String advertiserName = radiaSLDto.getAdserverName();
          	            String packageId = radiaSLDto.getPackageId();
          	            String radiaPackageType = radiaSLDto.getPackageType();
          	            String sellLineCreationDate = radiaSLDto.getSelllineCreationDate();
          	            String sellLineCreatingUser = radiaSLDto.getSelllineCreatingUser();
          	            String lineItemType = radiaSLDto.getLineItemType();
          	             
          	            String margin = String.valueOf(radiaSLDto.getMargin());
          	            String marginValue = String.valueOf(radiaSLDto.getMarginValue());
          	            String radiaSLStartDate = radiaSLDto.getStartDate();
          	            String radiaSLEndDate = radiaSLDto.getEndDate();
          	            
          	          String adLabs = radiaSLDto.getAdLabs();
          	          String agencyBillingPlatform = radiaSLDto.getAgencyBillingPlatform();
          	          String channel = radiaSLDto.getChannel();
          	          String creativeDetail = radiaSLDto.getCreativeDetail();
          	          String creativeFormat = radiaSLDto.getCreativeFormat();
          	          String dataTactic = radiaSLDto.getDataTactic();
          	          String device = radiaSLDto.getDevice();
          	          String inventory = radiaSLDto.getInventory();
          	          String networkBillingPlatform = radiaSLDto.getNetworkBillingPlatform();
          	          String primaryKpi = radiaSLDto.getPrimaryKpi();
          	          String productCode = radiaSLDto.getProductCode(); 
          	            
          	          sellLineRadiaHashStr = slName + "--" + sfRadiaId + "--" + supplierCode + "--" + supplierName + "--" +
          	        		lineType + "--" + mediaName + "--" +  mOAdvertiserName + "--" + mOAdvertiserCode + "--" + mOProductName + "--" + 
          	        		mOProductCode + "--" + mOEstimateName + "--" + mOEstimateCode + "--" + category + "--" + lastModifiedBy + "--" + 
          	        		site + "--" + netRateType + "--" + unitType + "--" + rate + "--" + vendorIONumber + "--" + radiaServedBy + "--" + 
          	        		radiaSupplierProductName + "--" + lastChangeInRadia + "--" + advertiserName + "--" + packageId + "--" + radiaPackageType + "--" +
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
          	        		"--" + productCode;
          	          
          	        Logger.log("sellLineRadiaHashStr is " + sellLineRadiaHashStr, true); 
          	        //SfdcDAO sfdcDao1 = new SfdcDAO();
  	                String sellLineSfdcHashStr = ""; 
  	               
  	            //sellLineSfdcHashStr = sfdcDao.getSellLineHash(radiaSLDto.getRadiaId());
  	              sellLineSfdcHashStr = sellLineSfdcHashMap.get(radiaSLDto.getRadiaId());//PW 2016-05-10 Instead of calling SF every time, get the hash string from a pre-populated map
  	              Logger.log("sellLineSfdcHashStr is " + sellLineSfdcHashStr, true);
                   
  	              	  
  	                //String slId = sfdcDao1.getSLToUpdate(radiaSLDto.getRadiaId());
  	              String slId = sfdcDao.getSLToUpdate(radiaSLDto.getRadiaId());
  	              	//oppDto.setOppId("006L0000008O5REIA0");
  	                if (!slId.equals(""))
  	                {
  	                	radiaSLDto.setSellLineId(slId);
  	                }
  	                
  	                boolean upsertSL;
  	                 
  	               if (!sellLineRadiaHashStr.equalsIgnoreCase(sellLineSfdcHashStr))
  	               {
  	            	 upsertSL = true;
  	            	 radiaSLDto.setNeedUpsert(upsertSL);
  	            	 changed = true;
                   }
  	               else
  	               {
  	            	 upsertSL = false;
  	            	   radiaSLDto.setNeedUpsert(upsertSL);
  	               }
  	                
  	               
  	               System.out.println("----upsertSL is " + upsertSL);
  	                
  	               //if (!slId.equals("") && upsertSL == false)
  	               /*
  	               if (!slId.equals("") && upsertSL == false)
  	               {
  	            	   //System.out.println("before remove " + radiaSLDto.getSellLineId());
  	            	 //i.remove();
  	               }
  	               */
  	               
  	             if (radiaSLDto.getBuyPlacementData() != null)
	             {
	            	  
	            	 List<RadiaBuyPlacementDTO> objsBP = radiaSLDto.getBuyPlacementData();
	            	Iterator<RadiaBuyPlacementDTO> j = objsBP.iterator();
	            	while (j.hasNext()) 
	            	{
	            		RadiaBuyPlacementDTO radiaBPDto = j.next();
	  	            	 
	            		String buyPlacementRadiaHashStr = "";
	            		 
	      	            String sfRadiaId1 = radiaBPDto.getRadiaId();
	      	            String supplierCode1 = radiaBPDto.getSupplierCode();
	      	            if (supplierCode1 == null)
	      	            {
	      	            	supplierCode1 = "null";
	      	            }
	      	            String supplierName1 = radiaBPDto.getSupplierName();
	      	            
	      	            String mOAdvertiserName1 = radiaBPDto.getMOAdvertiserName();
	      	            String mOAdvertiserCode1 = radiaBPDto.getMOAdvertiserCode();
	      	            String mOProductName1 = radiaBPDto.getMOProductName();
	      	            String mOProductCode1 = radiaBPDto.getMOProductCode();
	      	            String mOEstimateName1 = radiaBPDto.getMOEstimateName();
	      	            String mOEstimateCode1 = radiaBPDto.getMOEstimateCode();
	      	             
	      	            String lastModifiedBy1 = radiaBPDto.getLastModifiedBy();
	      	            String site1 = radiaBPDto.getSite();
	      	            
	      	            String unitType1 = radiaBPDto.getUnitType();
	      	            String rate1 = String.valueOf(radiaBPDto.getRate());
	      	            String vendorIONumber1 = radiaBPDto.getVendorIONumber();
	      	            String servedBy1 = radiaBPDto.getServedBy();
	      	            String supplierProductName1 = radiaBPDto.getSupplierProductName();
	      	            if (supplierProductName1 == null)
	      	            {
	      	            	supplierProductName1 = "null";
	      	            }
	      	            String lastChangeInRadia1 = radiaBPDto.getLastChangedInRadia();
	      	            String advertiserName1 = radiaBPDto.getAdserverName();
	      	            String packageId1 = radiaBPDto.getPackageId();
	      	            String packageType1 = radiaBPDto.getPackageType();
	      	            String buyPlacementCreationDate = radiaBPDto.getBuylineCreationDate();
	      	            String buyPlacementCreatingUser = radiaBPDto.getBuylineCreatingUser();
	      	            String lineItemType1 = radiaBPDto.getLineItemType();
	      	             
	      	            String margin1 = String.valueOf(radiaBPDto.getMargin());
	      	            String marginValue1 = String.valueOf(radiaBPDto.getMarginValue());
	      	            String comments1 = radiaBPDto.getComments();
	      	            
	      	            String radiaBPStartDate = radiaBPDto.getStartDate();
	      	            String radiaBPEndDate = radiaBPDto.getEndDate();
	      	            
	      	          String adLabs1 = radiaBPDto.getAdLabs();
          	          String agencyBillingPlatform1 = radiaBPDto.getAgencyBillingPlatform();
          	          String channel1 = radiaBPDto.getChannel();
          	          String creativeDetail1 = radiaBPDto.getCreativeDetail();
          	          String creativeFormat1 = radiaBPDto.getCreativeFormat();
          	          String dataTactic1 = radiaBPDto.getDataTactic();
          	          String device1 = radiaBPDto.getDevice();
          	          String inventory1 = radiaBPDto.getInventory();
          	          String networkBillingPlatform1 = radiaBPDto.getNetworkBillingPlatform();
          	          String primaryKpi1 = radiaBPDto.getPrimaryKpi();
          	          String productCode1 = radiaBPDto.getProductCode(); 
	      	            
	      	          buyPlacementRadiaHashStr = sfRadiaId1 + "--" + supplierCode1 + "--" + supplierName1 + "--" +
	      	        		mOAdvertiserName1 + "--" + mOAdvertiserCode1 + "--" + mOProductName1 + "--" + 
	      	        		mOProductCode1 + "--" + mOEstimateName1 + "--" + mOEstimateCode1 + "--" + lastModifiedBy1 + "--" + 
	      	        		site1 + "--" + unitType1 + "--" + rate1 + "--" + vendorIONumber1 + "--" + servedBy1 + "--" + 
	      	        		supplierProductName1 + "--" + lastChangeInRadia1 + "--" + advertiserName1 + "--" + packageId1 + "--" + packageType1 + "--" +
	      	        		buyPlacementCreationDate + "--" + buyPlacementCreatingUser + "--" + lineItemType1 + "--" +  margin1 + "--" + 
	      	        		marginValue1 + "--" + comments1 + "--" + radiaBPStartDate + "--" + radiaBPEndDate +
	      	        		"--" + adLabs1 +  
          	        		"--" + agencyBillingPlatform1 +  
          	        		"--" + channel1 +  
          	        		"--" + creativeDetail1 +  
          	        		"--" + creativeFormat1 +  
          	        		"--" + dataTactic1 +  
          	        		"--" + device1 +  
          	        		"--" + inventory1 +  
          	        		"--" + networkBillingPlatform1 +  
          	        		"--" + primaryKpi1 +  
          	        		"--" + productCode1;
	      	            
	      	            System.out.println("buyPlacementRadiaHashStr is " + buyPlacementRadiaHashStr);
	      	          Logger.log("buyPlacementRadiaHashStr is " + buyPlacementRadiaHashStr, true);
	          	        //SfdcDAO sfdcDao2 = new SfdcDAO();
	  	                String buyPlacementSfdcHashStr = ""; 
	  	               
	  	              //buyPlacementSfdcHashStr = sfdcDao.getBuyPlacementHash(radiaBPDto.getRadiaId());
	  	              buyPlacementSfdcHashStr = buyPlacementSfdcHashMap.get(radiaBPDto.getRadiaId());//PW 2016-05-10 Instead of calling SF every time, get the hash string from a pre-populated map
	  	              Logger.log("buyPlacementSfdcHashStr is " + buyPlacementSfdcHashStr, true);
	                   
	  	              	  
	  	                //String bpId = sfdcDao2.getBPToUpdate(radiaBPDto.getRadiaId());
	  	                String bpId = sfdcDao.getBPToUpdate(radiaBPDto.getRadiaId());
	  	              	 
	  	                if (!bpId.equals(""))
	  	                {
	  	                	radiaBPDto.setBuyPlacementId(bpId);
	  	                }
	  	                
	  	                boolean upsertBP;
	  	                
	  	               if (!buyPlacementRadiaHashStr.equalsIgnoreCase(buyPlacementSfdcHashStr))
	  	               {
	  	            	 upsertBP = true;
	  	            	   radiaBPDto.setNeedUpsert(upsertBP);
	  	            	 changed = true;
	                   }
	  	               else
	  	               {
	  	            	 upsertBP = false;
	  	            	   radiaBPDto.setNeedUpsert(upsertBP);
	  	               }
	  	                
	  	              
	  	               System.out.println("----upsertBP is " + upsertBP);
	            		
	  	               /*
	  	               String partnershipEngagementAcctId = "";
	  	               if (radiaBPDto.getSupplierCode() != null && !(radiaBPDto.getSupplierCode().equals("")))
	  	               {
	  	            	   if (sfdcDao.getBuyPlacementPartnershipEngagement(radiaBPDto.getSupplierCode()) !=null && !(sfdcDao.getBuyPlacementPartnershipEngagement(radiaBPDto.getSupplierCode()).equals("")))
	  	            	   {
	  	            		 partnershipEngagementAcctId = sfdcDao.getBuyPlacementPartnershipEngagement(radiaBPDto.getSupplierCode());
	  	            		 radiaBPDto.setPartnershipEngagementAcctId(partnershipEngagementAcctId);
	  	            	   }
	  	               }
	  	               Logger.log("BP radia id is " + radiaBPDto.getRadiaId() + "\n supplier code is " + radiaBPDto.getSupplierCode() +"\n partnershipEngagementAcctId is " + partnershipEngagementAcctId, true);
	  	               */
	  	               
	  	             if (radiaBPDto.getPlacementmonthlyscheduleData() != null)
		             {
		            	  
		            	 List<RadiaPlacementMonthlyScheduleDTO> objsPMS = radiaBPDto.getPlacementmonthlyscheduleData();
		            	Iterator<RadiaPlacementMonthlyScheduleDTO> n = objsPMS.iterator();
		            	while (n.hasNext()) 
		            	{
		            		RadiaPlacementMonthlyScheduleDTO radiaPMSDto = n.next();
		  	            	 
		            		//radiaPMSDto.setRadiaId(radiaBPDto.getRadiaId() + radiaPMSDto.getScheduleDate()); 
		            		
		            		String pmsRadiaHashStr = "";
		            		String packageType = radiaPMSDto.getPackageType();
		            		String billablePlannedAmount = String.valueOf(radiaPMSDto.getBillablePlannedAmount());
		      	            String plannedAmount = String.valueOf(radiaPMSDto.getPlannedAmount());
		      	            String plannedUnits = String.valueOf(radiaPMSDto.getPlannedUnits());
		      	            String supplierUnits = String.valueOf(radiaPMSDto.getSupplierUnits());
		      	            String plannedFee = String.valueOf(radiaPMSDto.getPlannedFees());
		            		String startDate = radiaPMSDto.getStartDate();
		            		String endDate = radiaPMSDto.getEndDate();
		            		
		            		pmsRadiaHashStr = packageType + "--" + billablePlannedAmount + "--" + plannedAmount + "--" + plannedUnits + "--" + supplierUnits + "--"+ plannedFee + "--" + startDate + "--" + endDate;
		            		Logger.log("pmsRadiaHashStr is " + pmsRadiaHashStr, true);
		            		
		            		String pmsSfdcHashStr = "";
		            		//pmsSfdcHashStr = sfdcDao.getPlacementMonthlyScheduleHash(radiaPMSDto.getRadiaId());
		            		pmsSfdcHashStr = pmsSfdcHashMap.get(radiaPMSDto.getRadiaId());//PW 2016-05-10 Instead of calling SF every time, get the hash string from a pre-populated map
		            		
		            		Logger.log("pmsSfdcHashStr is " + pmsSfdcHashStr, true);
		            		
		            		boolean upsertPMS;
		            		 
		            		if (!pmsRadiaHashStr.equalsIgnoreCase(pmsSfdcHashStr))
			  	            {
			  	            	 upsertPMS = true;
			  	            	 radiaPMSDto.setNeedUpsert(upsertPMS);
			  	            	changed = true;
			                }
			  	            else
			  	            {
			  	            	 upsertPMS = false;
			  	            	 radiaPMSDto.setNeedUpsert(upsertPMS);
			  	            }
			  	             
		            		 
			  	           
		            		System.out.println("upsertPMS is " + upsertPMS);
		            	}
		             }
	  	               
	  	               
	            	}//while (j.hasNext())
	               }//if (radiaSLDto.getBuyPlacementData() != null)
  	               
  	           if (radiaSLDto.getSlmonthlyscheduleData() != null)
	             {
	            	  
	            	 List<RadiaSLMonthlyScheduleDTO> objsSLMS = radiaSLDto.getSlmonthlyscheduleData();
	            	Iterator<RadiaSLMonthlyScheduleDTO> m = objsSLMS.iterator();
	            	while (m.hasNext()) 
	            	{
	            		RadiaSLMonthlyScheduleDTO radiaSLMSDto = m.next();
	  	            	 
	            		//radiaSLMSDto.setRadiaId(radiaSLDto.getRadiaId() + radiaSLMSDto.getScheduleDate()); 
	            		
	            		String slmsRadiaHashStr = "";
	            		String scheduleDate = radiaSLMSDto.getScheduleDate();
	            		String cost = String.valueOf(radiaSLMSDto.getCost());
	            		String plannedBillableUnits = String.valueOf(radiaSLMSDto.getPlannedBillableUnits());
	            		String startDate = radiaSLMSDto.getStartDate();
	            		String endDate = radiaSLMSDto.getEndDate();
	            		
	            		//slmsRadiaHashStr = scheduleDate + "--" + cost + "--" + startDate + "--" + endDate;
	            		slmsRadiaHashStr = cost + "--" + plannedBillableUnits + "--" + startDate + "--" + endDate;
	            		Logger.log("slmsRadiaHashStr is " + slmsRadiaHashStr, true);
	            		
	            		String slmsSfdcHashStr = "";
	            		//slmsSfdcHashStr = sfdcDao.getSellLineMonthlyScheduleHash(radiaSLMSDto.getRadiaId());
	            		slmsSfdcHashStr = slmsSfdcHashMap.get(radiaSLMSDto.getRadiaId());//PW 2016-05-10 Instead of calling SF every time, get the hash string from a pre-populated map
	            		
	            		Logger.log("slmsSfdcHashStr is " + slmsSfdcHashStr, true);
	  	                
	  	                boolean upsertSLMS;
	  	                
	  	                
	  	              if (!slmsRadiaHashStr.equalsIgnoreCase(slmsSfdcHashStr))
	  	               {
	  	            	 upsertSLMS = true;
	  	            	 radiaSLMSDto.setNeedUpsert(upsertSLMS);
	  	            	 changed = true;
	                   }
	  	               else
	  	               {
	  	            	 upsertSLMS = false;
	  	            	 radiaSLMSDto.setNeedUpsert(upsertSLMS);
	  	               }
	  	               
	  	                
	  	               System.out.println("----upsertSLMS is " + upsertSLMS);
	  	                
	  	                
	  	               
	            	}//while (m.hasNext()) 
	               }//if (radiaSLDto.getSlmonthlyscheduleData() != null)
  	               
  	             }//while (i.hasNext()) {  for (int i = 0; i < oppDto.getSellLineData().size(); i++)
  	           }//if (oppDto.getSellLineData() != null)
  	           else
  	           {
  	            	 System.out.println("--place 1, oppDto.getSellLineData() is null");
  	            	   
  	           }
  	              /* 
  	             //if opp and any of it's children changed and opp stage is Closed Won, drop to Contract Mod  
  	             if (existingOppDto.getOppId() != null && existingOppDto.getAccountId() !=null && existingOppDto.getStage() != null)
  	             {
  	            	 if (existingOppDto.getStage().equalsIgnoreCase("Closed Won") && changed == true)
  	            	 {
  	            		 oppDto.setStage("Contract Modification");
  	            	 }
  	             }
  	             */
  	               /*
  	               if (oppDto.getSellLineData() != null)
  	               {
  	               System.out.println("oppDto.getSellLineData().size() is " + oppDto.getSellLineData().size());
  	               }
  	               else
  	               {
  	            	   
  	            	   System.out.println("oppDto.getSellLineData() is null ");
  	               }
  	               if (oppDto.getSellLineData() != null)
  	               {
  	               if (oppDto.getSellLineData().size() > 0)
  	               {
  	               for (int j = 0; j < oppDto.getSellLineData().size(); j++)
  	               {
  	            	   
  	            	   System.out.println("-------oppDto.getSellLineData().get(j).getRadiaId() is " +  oppDto.getSellLineData().get(j).getSellLineId() + "--" + oppDto.getSellLineData().get(j).getRadiaId());
  	            	 System.out.println("-------oppDto.getSellLineData().get(j).getNeedUpsert() is " +  oppDto.getSellLineData().get(j).getNeedUpsert());
  	               }
  	               }
  	               }
  	               */
  	               	oppList.add(oppDto);
  	                System.out.println("before send it to API call, oppList size is " + oppList.size());
                    reqOpp.setOppData(oppList);
                     
                    String reqJson = gson.toJson(reqOpp, SFBasicRequestDto.class);
                    
                    Logger.log(reqJson, true);
                    
                    
                    
                    //assert reqJson != null;
                    //prepare and send request
                    request.accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + sessionId);
                    request.body(MediaType.APPLICATION_JSON, reqJson);
                    
                    
                    response =  request.post(String.class);
                    //System.out.println("response.getStatus() is " + response.getStatus());
                    //if (response.getStatus() != 200) {
                     //   throw new Exception();
                    //}
                    //read results
                    System.out.println("\n===============================================");
                    System.out.println("POST Response:");
                    System.out.println(response.getEntity());
                    Logger.log(response.getEntity(), true);
                    //Logger.log("-----response status is "+String.valueOf(response.getStatus()), true);
                    CharSequence success = ":true";
                    
                    if (response.getEntity().contains(success))
                    {
                    	Logger.log("Campaign " + oppDto.getName() + " with Radia ID "+ oppDto.getRadiaCampaignId() + " has been successfully processed." , true);
                    	Logger.logSuccess(oppDto.getName()+";"+oppDto.getRadiaCampaignId(), true);
                    	Controller.successCount ++;
                    }
                    else
                    {
                    	Logger.log("Campaign " + oppDto.getName() + " with Radia ID "+ oppDto.getRadiaCampaignId() + " has error during processing." , true);
                    	Controller.errorMessage = Controller.errorMessage + "Campaign " + oppDto.getName() + " with Radia ID "+ oppDto.getRadiaCampaignId() + " has error during processing. " + "\n"; 
                    	Logger.logError(oppDto.getName()+";"+oppDto.getRadiaCampaignId()+";"+response.getEntity(), true);
                    	Controller.restAPICallErrorCount ++;
                    	
                    }
                     
                    Controller.processedCampaignSet.add(oppDto.getRadiaCampaignId());
                    System.out.println("\n===============================================");
                    Type responsePostType = new TypeToken<SFBasicResponseDto<String>>() {}.getType();
                  //dto = gson.fromJson(response.getEntity(), responsePostType);
                  //assert dto != null;
                  //  if (!dto.getSuccess()) {
                              //     throw new ImpactBusinessException(TranslationKey.ERROR, dto.getMessage());
                  //  }
  	            // }//if (existingOppDto.getOppId() == null) else{}
              }// if (ispost) 


              } 
              catch (Exception e) {
                   e.printStackTrace();
                   System.out.println(e.getMessage());
                    
              throw new Exception();
              }
              
              finally {
                    sfservice.logout();
            	  
              }
  }//if (sfservice.login() == true) {

        return jsonString;
  }
	
	public static String trimLeadingZeros(String str)
	   {
		   int lastLeadZeroIndex = 0;
		   for (int i = 0; i < str.length(); i++) {
		     char c = str.charAt(i);
		     if (c == '0') {
		       lastLeadZeroIndex = i;
		     } else {
		       break;
		     }
		   }

		   str = str.substring(lastLeadZeroIndex+1, str.length());
		   return str;
	   }
	 
}
