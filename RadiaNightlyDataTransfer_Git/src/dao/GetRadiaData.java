package dao;

 
import misc.LastRun;
import misc.Logger;
import misc.ReadPropertyFile;
import radia.*;
import sfdc.SfdcDAO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;
import controller.Controller;

import com.idoox.xmlrpc.config.HeaderInstanceConfig.Header;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 



public class GetRadiaData {

	//private static final String FILE_PATH = "/eclipse-kepler/workspace/RadiaNightlyDataTransfer/Radia data dump 20150819.xlsx";
	//private static final String FILE_PATH = "/SFDC/Radia/Radia data dump 20150819.xlsx";
	//private static final String FILE_PATH = "/SFDC/Radia/Radia_SampleFiles_20151111.xlsx"; // windows
	//private static final String FILE_PATH = "/home/pwang/radia_nightly_data_transfer/Radia_SampleFiles_20151111.xlsx"; // unix
	//private static final String FILE_PATH = "/eclipse-kepler/workspace/RadiaNightlyDataTransfer/radiafile/379_Radia_Campaigns_635856886094189442.xlsx";
	//private static final String FILE_PATH = "/eclipse-kepler/workspace/RadiaNightlyDataTransfer/radiafile/";
	//private static final String FILE_PATH = "/SFDC/Radia/radiafile/";
	 
	 
	
	private static String tmpLogFile = ""; 
	public static List deleteOppList = new ArrayList();
	public static List deleteSellLineList = new ArrayList();
	public static List deleteBuyPlacementList = new ArrayList();
	public static List deleteSLMonthlyScheduleList = new ArrayList();
	public static List deletePlacementMonthlyScheduleList = new ArrayList();
	
	public static boolean campaignFileHasNewUpdate = false;
	public static boolean placementFileHasNewUpdate = false;
	public static boolean monthlyScheduleFileHasNewUpdate = false;
	public static boolean advancedFileHasNewUpdate = false;
	
	public static boolean campaignFileNotEmpty = false;
	public static boolean placementFileNotEmpty = false;
	public static boolean monthlyScheduleFileNotEmpty = false;
	public static boolean advancedFileNotEmpty = false;
	
	
     
	public static void main(String args[]) throws Exception {

         //getCampaignFromExcel();
		//getSellLineFromExcel();
		//getBuyPlacementFromExcel();
		//getSLMonthlyScheduleFromExcel();
		//getPlacementMonthlyScheduleFromExcel();
    }
	 
 

    public static List getCampaignFromExcel(String fileName) throws Exception {

    	ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
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
				  
			 }
		 
		}
  		String filePath = propertyMap.get("ftp_localdirectory");
    	
        List CampaignList = new ArrayList();

        FileInputStream fis = null;
        
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\379_Radia_Campaigns_635856886094189442.csv"; //csv file address
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\" + fileName; //csv file address
        String csvFileAddress = filePath + fileName;   
        
        //CSVReader reader = null;
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));
		
		try 
		{
			//Get the CSVReader instance with specifying the delimiter to be used
			  
			
			//reader = new CSVReader(new FileReader("/eclipse-kepler/workspace/RadiaNightlyDataTransfer/radiafile/379_Radia_Campaigns_635856886094189442.csv"),'\t');
			Map<String,Integer> headingMap = new HashMap<String,Integer>();
				 
			headingMap.put("CampaignId", 0);
			headingMap.put("CampaignPublicId", 1);
			headingMap.put("CampaignName", 2);
			headingMap.put("CampaignStartDate", 3);
			headingMap.put("CampaignEndDate", 4);
			headingMap.put("AdvertiserCode", 5);
			headingMap.put("AdvertiserName", 6);
			headingMap.put("CampaignStatus", 7);
			headingMap.put("Strategy", 8);
			headingMap.put("RateType", 9);
			headingMap.put("Budget", 10);
			headingMap.put("BudgetApproved", 11);
			headingMap.put("ReferenceId", 12);
			headingMap.put("User", 13);
			headingMap.put("ECPM", 14);
			headingMap.put("ECPA", 15);
			headingMap.put("ECPC", 16);
			headingMap.put("IncludePostImpressions", 17);
			headingMap.put("PrimaryGoal", 18);
			headingMap.put("AgencyAlphaCode", 19);
			headingMap.put("AgencyName", 20);
			headingMap.put("LocationCompanyCode", 21);
			headingMap.put("FieldDefinitionTag", 22);
			headingMap.put("FieldDefinitionFileName", 23);
			headingMap.put("CampaignCreationUser", 24);
			headingMap.put("CampaignCreationDate", 25);
			headingMap.put("CampaignSource", 26);
			headingMap.put("CampaignSourceId", 27);
			headingMap.put("InsertDatetime", 28);
			headingMap.put("InsertBy", 29);
			headingMap.put("UpdateDatetime", 30);
			headingMap.put("UpdateBy", 31);
			headingMap.put("source_item_name", 32);
			headingMap.put("is_deleted", 33);
			headingMap.put("Rowstamp", 34);
			
			
			
			
			//BufferedReader reader1 = new BufferedReader(new FileReader(csvFileAddress));
			//BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));
			
			List<String> lines = new ArrayList<>();
			String line = null;
			Map<Integer,String> lineMap = new HashMap<Integer,String>();
			
			int k=1;
			while ((line = reader1.readLine()) != null) 
			{
				RadiaOpportunityDTO radiaOppDto = new RadiaOpportunityDTO(); 
				if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			    {
				lines.add(line);
			    lineMap.put(k, line);
			    System.out.println("line length is " + line.length());
			    
			    
			     //if (k>=2 && k<=2)
			    if (k>=2)
			    {
			    	String[] tokens = lineMap.get(k).split("\t",-1);
			    	
			    	campaignFileNotEmpty = true;
			    	
			    	String val = tokens[headingMap.get("CampaignPublicId")];
			    	radiaOppDto.setRadiaCampaignId(val);
                    System.out.println("in csv file, campaignid is " + radiaOppDto.getRadiaCampaignId());
                    
                    //*********************************************************************
                    //SFG-3750
                    java.util.Date date = new java.util.Date();
         		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String dateFormatTwoDigits =  sdf.format(date);
                    CharSequence todayDateTwoDigits = dateFormatTwoDigits; 
                    
                    SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yyyy");
                    String dateFormatOneDigit =  sdf1.format(date);
                    CharSequence todayDateOneDigit = dateFormatOneDigit; 
                    
                    val = tokens[headingMap.get("UpdateDatetime")];
                    String updateDateTime = val;
                    val = tokens[headingMap.get("InsertDatetime")];
                    String insertDateTime = val;
                    
                    
                    //Logger.log("today's date two digits is " + todayDateTwoDigits, true);
                    //Logger.log("today's date one digit is " + todayDateOneDigit, true);
                    //Logger.log("UpdateDatetime is " + updateDateTime, true);
                    //Logger.log("InsertDateTime is " + insertDateTime, true);
                    if (updateDateTime.contains(todayDateTwoDigits) || updateDateTime.contains(todayDateOneDigit) || insertDateTime.contains(todayDateTwoDigits) || insertDateTime.contains(todayDateOneDigit))
                    {
                    	//Logger.log("UpdateDateTime or InsertDateTime contains today's date", true);
                    	campaignFileHasNewUpdate = true;
                    }
                    
                    //*********************************************************************
                    
                    
                    val = tokens[headingMap.get("CampaignId")];
                    radiaOppDto.setRadiaCampaignNumber(val);
                    System.out.println("in csv file, campaignNumber is " + radiaOppDto.getRadiaCampaignNumber());
			    	
                    val = tokens[headingMap.get("CampaignName")];
                    radiaOppDto.setName(val);
                    System.out.println(" name is " + radiaOppDto.getName());
                    
                    val = String.valueOf(tokens[headingMap.get("CampaignStartDate")]);
                    System.out.println("before format, campaignStartDate is " + val);
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //String cellValue =  sdf.format(val);
                     
                    //val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    if (val != null && !val.equals(""))
                    	val = dateStringFormat(val);
                            
                    radiaOppDto.setRadiaCampaignStartDate(val);
                    System.out.println(" -----campaignStartDate is " + radiaOppDto.getRadiaCampaignStartDate());
                    
                    //cell = row.getCell(4);
                    val = tokens[headingMap.get("CampaignEndDate")];
                    //sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //cellValue =  sdf.format(val);
                   // val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    if (val != null && !val.equals(""))
                    	val = dateStringFormat(val);
                    radiaOppDto.setRadiaCampaignEndDate(val);
                    System.out.println(" campaignEndDate is " + radiaOppDto.getRadiaCampaignEndDate());
                    
                    val = tokens[headingMap.get("AdvertiserName")];
                    radiaOppDto.setRadiaAdvertiserName(val);
                    
                    val = tokens[headingMap.get("Budget")];
                    if (val != null && !val.equals(""))
                    {
                    	radiaOppDto.setRadiaBudget(Double.valueOf(val));
                    }
                    	
                    val = tokens[headingMap.get("BudgetApproved")];
                    boolean approved;
                    //approved = (cell.getNumericCellValue() == 1 ? true:false);
                    if (val.equalsIgnoreCase("true"))
                    approved = true;
                    else
                    approved = false;
                    radiaOppDto.setRadiaBudgetApproved(approved);
                    System.out.println("radiaOppDto.setRadiaBudgetApproved is " + radiaOppDto.getRadiaBudgetApproved());
                     
                    //cell = row.getCell(headingMap.get("CampaignCreationDate"));
                    val=tokens[headingMap.get("CampaignCreationDate")];
                     
                    //if (cell.equals("NULL") || cell == null)
                    if (val == null || val.equals(""))
                    {
                    	//System.out.println("CampaignCreationDate is " + cell);
                    }
                    else
                    {
                    	
                    	System.out.println("date is " + val);
                    	//sdf = new SimpleDateFormat("yyyy-MM-dd");
                    	//cellValue =  sdf.format(val);
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaOppDto.setRadiaCampaignCreatedDate(val);
                    }
                    
                    /* 
                    val = tokens[headingMap.get("is_deleted")];
                    System.out.println("is_deleted is " + val);
                    boolean isDeleted = false;
                    if (val == null || val.equals(""))
                    {}
                    else
                    {
                    	if(val.equalsIgnoreCase("TRUE"))
                    	{
                    		deleteOppList.add(radiaOppDto.getRadiaCampaignId());
                    		isDeleted = true;
                    	}
                    }
                    */ 
                    //PW 2016-01-26 per Eric Levin, to skip process campaign with CampaignStatus = DELETED
                    val = tokens[headingMap.get("CampaignStatus")];
                    System.out.println("CampaignStatus is " + val);
                    boolean isDeleted = false;
                    if (val == null || val.equals(""))
                    {}
                    else
                    {
                    	if(val.equalsIgnoreCase("DELETED"))
                    	{
                    		//deleteOppList.add(radiaOppDto.getRadiaCampaignId());
                    		isDeleted = true;
                    	}
                    }
                    
                    if (isDeleted == false)
                    {
                    	CampaignList.add(radiaOppDto);
                    	Controller.totalCampaignSet.add(radiaOppDto.getRadiaCampaignId());
                    }
			      }
			    k++;
			    }
			}
			//System.out.println(lines.get(0));
			
			/* 
			String[] tokens = lines.get(0).split("\t");
			for(String token : tokens)
			{
				int i = 0;
				//Print all tokens
				System.out.println(token + "\n");
				headingMap.put(token, i);
				i++;
			}
			 */
			//System.out.println("number of campaign start date is " + headingMap.get("CampaignStartDate"));
			
			/*
			String [] nextLine;
			
			//Read one line at a time
		    while ((nextLine = reader.readNext()) != null) 
		    {
		    	
		    	 
		    	for(String token : nextLine)
				{
					//Print all tokens
					System.out.println(token);
				}
		    }
		    */
		 }
		catch (FileNotFoundException e) {

            e.printStackTrace();
            StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 Logger.log(errors.toString(),true);
			 Logger.logTechOps(errors.toString(),true);
          
        } 
		catch (IOException e) {

            e.printStackTrace();
            StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 Logger.log(errors.toString(),true);
			 Logger.logTechOps(errors.toString(),true);
            

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
				  
			 }
		 
		}
		finally	{
			try {
				reader1.close();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), true);
				 
			}
		}
         
		
        return CampaignList;

    }

 
    public static HashMap getSellLineFromExcel(String fileName) throws Exception {

        /*
         * Map<String,List<Integer>> prodMap = new Map<String, List<Integer>>();
		for(sObject prod:products){
    if(prodMap.get(prod.Name) != null){
        prodMap.get(prod.Name).add(prod.value);
    }else{
        List<Integer> listOfValues = new List<Integer>();
        listOfValues.add(prod.value);
        prodMap.put(prod.Name, listOfValues);
    }
}
         * 
         */
    	
    	ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
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
				  
			 }
		 
		}
  		String filePath = propertyMap.get("ftp_localdirectory");
    	
    	HashMap<String, List<RadiaSellLineDTO>> sellLineMap = new HashMap<String, List<RadiaSellLineDTO>>();
    	
    	List sellLineList = new ArrayList();

        //FileInputStream fis = null;
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\492_Radia_Placement_Details_635859324014689133.csv"; //csv file address
    	//String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\" + fileName; //csv file address
    	String csvFileAddress = filePath + fileName;    

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));
		
		try 
		{
			//Get the CSVReader instance with specifying the delimiter to be used
			  
			
			//reader = new CSVReader(new FileReader("/eclipse-kepler/workspace/RadiaNightlyDataTransfer/radiafile/379_Radia_Campaigns_635856886094189442.csv"),'\t');
			Map<String,Integer> headingMap = new HashMap<String,Integer>();
			
			/*
			SupplierCode	SupplierZone	SupplierBusinessKey	SupplierName	Folio	CampaignId	
			CampaignPublicId	CampaignName	BuyType	MediaCode	MediaName	AdvertiserCode	
			AdvertiserBusinessKey	AdvertiserName	ProductCode	ProductBusinessKey	ProductName	EstimateCode	
			EstimateName	EstimateStartDate	EstimateEndDate	EstimateBusinessKey	PlacementId	PlacementName	
			PlacementNumber	BuyCategory	PlacementStartDate	PlacementEndDate	PlacementCategory	PlacementUser
			PlacementType	Site	Dimension	Positioning	Section	CostMethod	
			UnitType	Market	MarketRate	Rate	IONumber	ServedBy	
			CapCost	TargetInfo	Comments	FileType	MaxFileSize	FlashVersion	
			CreativeDueDate	MaxLooping	MaxAnimationTiming	RichMediaAccepted	RichMediaIncCmp	EstimatedUniqueViews	
			EstimatedPageViews	ShareOfVoice	ClickThroughUrl	SupplierProductName	SupplierValidityEndDate	SupplierSalesOrderRefVer	
			SupplierAdServerOrderId	SupplierPlacementRef	SupplierOrderDate	PlacementChangeDate	CreativeTypeDescription	InventoryType	
			AdserverName	InstanceName	AdserverAdvertiserCode	AdserverAdvertiserName	AdserverClientCode	AdserverClientName	
			AdserverSupplierCode	AdserverSupplierName	AdserverSiteCode	AdserverSiteName	PackageId	PackageType	
			SupplierSalesOrderRef	AdserverPlacementId	AdserverCampaignId	AdserverPackageId	PrimaryPlacement	PlacementCreationDate	
			PlacementCreationUser	LineItemType	BudgetLineId	MarginPercentage	MarginValue	IsPlacementDeleted	
			InsertDatetime	InsertBy	UpdateDatetime	UpdateBy	source_item_name	is_deleted	
			Rowstamp
			*/
			
			headingMap.put("SupplierCode", 0);
			headingMap.put("SupplierZone", 1);
			headingMap.put("SupplierBusinessKey", 2);
			headingMap.put("SupplierName", 3);
			headingMap.put("Folio", 4);
			headingMap.put("CampaignId", 5);
			headingMap.put("CampaignPublicId", 6);
			headingMap.put("CampaignName", 7);
			headingMap.put("BuyType", 8);
			headingMap.put("MediaCode", 9);
			headingMap.put("MediaName", 10);
			headingMap.put("AdvertiserCode", 11);
			headingMap.put("AdvertiserBusinessKey", 12);
			headingMap.put("AdvertiserName", 13);
			headingMap.put("ProductCode", 14);
			headingMap.put("ProductBusinessKey", 15);
			headingMap.put("ProductName", 16);
			headingMap.put("EstimateCode", 17);
			headingMap.put("EstimateName", 18);
			headingMap.put("EstimateStartDate", 19);
			headingMap.put("EstimateEndDate", 20);
			headingMap.put("EstimateBusinessKey", 21);
			headingMap.put("PlacementId", 22);
			headingMap.put("PlacementName", 23);
			headingMap.put("PlacementNumber", 24);
			headingMap.put("BuyCategory", 25);
			headingMap.put("PlacementStartDate", 26);
			headingMap.put("PlacementEndDate", 27);
			headingMap.put("PlacementCategory", 28);
			headingMap.put("PlacementUser", 29);
			headingMap.put("PlacementType", 30);
			headingMap.put("Site", 31);
			headingMap.put("Dimension", 32);
			headingMap.put("Positioning", 33);
			headingMap.put("Section", 34);
			headingMap.put("CostMethod", 35);
			headingMap.put("UnitType", 36);
			headingMap.put("Market", 37);
			headingMap.put("MarketRate", 38);
			headingMap.put("Rate", 39);
			headingMap.put("IONumber", 40);
			headingMap.put("ServedBy", 41);
			headingMap.put("CapCost", 42);
			headingMap.put("TargetInfo", 43);
			headingMap.put("Comments", 44);
			headingMap.put("FileType", 45);
			headingMap.put("MaxFileSize", 46);
			headingMap.put("FlashVersion", 47);
			headingMap.put("CreativeDueDate", 48);
			headingMap.put("MaxLooping", 49);
			headingMap.put("MaxAnimationTiming", 50);
			headingMap.put("RichMediaAccepted", 51);
			headingMap.put("RichMediaIncCmp", 52);
			headingMap.put("EstimatedUniqueViews", 53);
			headingMap.put("EstimatedPageViews", 54);
			headingMap.put("ShareOfVoice", 55);
			headingMap.put("ClickThroughUrl", 56);
			headingMap.put("SupplierProductName", 57);
			headingMap.put("SupplierValidityEndDate", 58);
			headingMap.put("SupplierSalesOrderRefVer", 59);
			headingMap.put("SupplierAdServerOrderId", 60);
			headingMap.put("SupplierPlacementRef", 61);
			headingMap.put("SupplierOrderDate", 62);
			headingMap.put("PlacementChangeDate", 63);
			headingMap.put("CreativeTypeDescription", 64);
			headingMap.put("InventoryType", 65);
			headingMap.put("AdserverName", 66);
			headingMap.put("InstanceName", 67);
			headingMap.put("AdserverAdvertiserCode", 68);
			headingMap.put("AdserverAdvertiserName", 69);
			headingMap.put("AdserverClientCode", 70);
			headingMap.put("AdserverClientName", 71);
			headingMap.put("AdserverSupplierCode", 72);
			headingMap.put("AdserverSupplierName", 73);
			headingMap.put("AdserverSiteCode", 74);
			headingMap.put("AdserverSiteName", 75);
			headingMap.put("PackageId", 76);
			headingMap.put("PackageType", 77);
			headingMap.put("SupplierSalesOrderRef", 78);
			headingMap.put("AdserverPlacementId", 79);
			headingMap.put("AdserverCampaignId", 80);
			headingMap.put("AdserverPackageId", 81);
			headingMap.put("PrimaryPlacement", 82);
			headingMap.put("PlacementCreationDate", 83);
			headingMap.put("PlacementCreationUser", 84);
			headingMap.put("LineItemType", 85);
			headingMap.put("BudgetLineId", 86);
			headingMap.put("MarginPercentage", 87);
			headingMap.put("MarginValue", 88);
			headingMap.put("IsPlacementDeleted", 89);
			headingMap.put("InsertDatetime", 90);
			headingMap.put("InsertBy", 91);
			headingMap.put("UpdateDatetime", 92);
			headingMap.put("UpdateBy", 93);
			headingMap.put("source_item_name", 94);
			headingMap.put("is_deleted", 95);
			headingMap.put("Rowstamp", 96);
			headingMap.put("BillableRate", 97);
			 
			
		
			
			
			
			
			
			
			//BufferedReader reader1 = new BufferedReader(new FileReader(csvFileAddress));
			//BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));
			
			List<String> lines = new ArrayList<>();
			String line = null;
			Map<Integer,String> lineMap = new HashMap<Integer,String>();
			int sellLineCounter = 0;
			
			int k=1;
			while ((line = reader.readLine()) != null) 
			{
				RadiaSellLineDTO radiaSLDto = new RadiaSellLineDTO();
				if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			    {
				lines.add(line);
			    lineMap.put(k, line);
			    //System.out.println("line length is " + line.length());
			    
			    
			    if (k>=2)
			    {
			    	//String[] tokens = lineMap.get(k).split("\t");
			    	String[] tokens = lineMap.get(k).split("\t",-1);
			    	
			    	placementFileNotEmpty = true;
			    
                    String val = tokens[headingMap.get("BudgetLineId")];
                    String budgetLineId = "";
                    //if (cell.CELL_TYPE_STRING == cell.getCellType())
                    //if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
                    if (val == null || val.equals(""))
                    {
                    	 
                    	
                    }
                    else 
                    {	
                    	budgetLineId =  val;
                    	
                    }
                    val = tokens[headingMap.get("PackageType")];
                    String packageType = "";
                     
                    packageType = val;
                    
                  //*********************************************************************
                    //SFG-3750
                    java.util.Date date = new java.util.Date();
         		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String dateFormatTwoDigits =  sdf.format(date);
                    CharSequence todayDateTwoDigits = dateFormatTwoDigits; 
                    
                    SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yyyy");
                    String dateFormatOneDigit =  sdf1.format(date);
                    CharSequence todayDateOneDigit = dateFormatOneDigit; 
                    
                    val = tokens[headingMap.get("UpdateDatetime")];
                    String updateDateTime = val;
                    val = tokens[headingMap.get("InsertDatetime")];
                    String insertDateTime = val;
                    
                    
                    //Logger.log("today's date two digits is " + todayDateTwoDigits, true);
                    //Logger.log("today's date one digit is " + todayDateOneDigit, true);
                    //Logger.log("UpdateDatetime is " + updateDateTime, true);
                    //Logger.log("InsertDateTime is " + insertDateTime, true);
                    if (updateDateTime.contains(todayDateTwoDigits) || updateDateTime.contains(todayDateOneDigit) || insertDateTime.contains(todayDateTwoDigits) || insertDateTime.contains(todayDateOneDigit))
                    {
                    	//Logger.log("UpdateDateTime or InsertDateTime contains today's date", true);
                    	placementFileHasNewUpdate = true;
                    }
                    
                    //*********************************************************************
                    
                    //if (budgetLineId.equalsIgnoreCase("NULL") && packageType.equalsIgnoreCase("TradingDesk"))
                    if (budgetLineId.equalsIgnoreCase("") && packageType.equalsIgnoreCase("TradingDesk"))
                    {
                    	sellLineCounter++;
                    	
                    	
                    	val = tokens[headingMap.get("CampaignPublicId")];
                    	radiaSLDto.setCampaignId(val);
                        //System.out.println("campaign id is " + radiaSLDto.getCampaignId());
                    	
                         
                    	val = tokens[headingMap.get("PlacementNumber")];
                        radiaSLDto.setRadiaId(val);
                        //System.out.println("placement number is " + radiaSLDto.getRadiaId());
                    	
                        val = tokens[headingMap.get("PlacementId")];
                    	radiaSLDto.setPlacementId(val);
                        //System.out.println("placement id is " + radiaSLDto.getPlacementId());
                    	
                        val = tokens[headingMap.get("SupplierCode")];
                    	radiaSLDto.setSupplierCode(val);
                        
                    	val = tokens[headingMap.get("SupplierName")];
                    	radiaSLDto.setSupplierName(val);
                    	//System.out.println("SL supplierName is " + radiaSLDto.getSupplierName());
                    
                    	val = tokens[headingMap.get("PlacementName")];
                    	radiaSLDto.setPlacementName(val);
                    		
                    	val = tokens[headingMap.get("BuyType")];
                    	radiaSLDto.setLineType(val);
                     
                    	val = tokens[headingMap.get("MediaName")];
                    	radiaSLDto.setMediaName(val);
                    	
                    	val = tokens[headingMap.get("AdvertiserName")];
                    	radiaSLDto.setMOAdvertiserName(val);
                    	
                    	val = tokens[headingMap.get("AdvertiserCode")];
                    	radiaSLDto.setMOAdvertiserCode(val);
                    	
                    	val = tokens[headingMap.get("ProductName")];
                    	radiaSLDto.setMOProductName(val);
                    	
                    	val = tokens[headingMap.get("ProductCode")];
                    	radiaSLDto.setMOProductCode(val);
                    
                    	val = tokens[headingMap.get("EstimateName")];
                    	radiaSLDto.setMOEstimateName(val);
                    	
                    	val = tokens[headingMap.get("EstimateCode")];
                    	radiaSLDto.setMOEstimateCode(val);
                    	
                    	val = tokens[headingMap.get("BuyCategory")];
                    	radiaSLDto.setCategory(val);
                    	
                    	val = tokens[headingMap.get("PlacementStartDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaSLDto.setStartDate(val);
                    	
                    	val = tokens[headingMap.get("PlacementEndDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaSLDto.setEndDate(val);
                    	
                    	val = tokens[headingMap.get("PlacementUser")];
                    	radiaSLDto.setLastModifiedBy(val);
                    	
                    	val = tokens[headingMap.get("Site")];
                    	radiaSLDto.setSite(val);
                    	
                    	val = tokens[headingMap.get("CostMethod")];
                    	radiaSLDto.setCostMethod(val);
                    	
                    	val = tokens[headingMap.get("UnitType")];
                    	radiaSLDto.setUnitType(val);
                    	
                    	//val = tokens[headingMap.get("Rate")];
                    	//SFG-3444 sell line uses BillableRate PW 2016-05-05
                    	
                    	val = tokens[headingMap.get("BillableRate")];
                    	if (val != null && !val.equals(""))
                    	{
                    		radiaSLDto.setRate(Double.valueOf(val));
                    	}
                    	
                    		
                    	val = tokens[headingMap.get("IONumber")];
                    	if (val != null && !val.equals(""))
                    	radiaSLDto.setVendorIONumber(val);
                    		 
                    	
                    	val = tokens[headingMap.get("ServedBy")];
                    	if (val != null && !val.equals(""))
                    	radiaSLDto.setServedBy(val);
                    	
                    	val = tokens[headingMap.get("SupplierProductName")];
                    	//if (val != null && !val.equals(""))
                    	radiaSLDto.setSupplierProductName(val);
                    	
                    	val = tokens[headingMap.get("PlacementChangeDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaSLDto.setLastChangedInRadia(val);
                    	
                    	val = tokens[headingMap.get("AdserverName")];
                    	radiaSLDto.setAdserverName(val);
                    	
                    	val = tokens[headingMap.get("PackageId")];
                    	if (val != null && !val.equals(""))
                    	radiaSLDto.setPackageId(val);
                    	
                    	val = tokens[headingMap.get("PackageType")];
                    	radiaSLDto.setPackageType(val);
                    	 
                    	val = tokens[headingMap.get("PlacementCreationDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaSLDto.setSelllineCreationDate(val);
                    	
                    	val = tokens[headingMap.get("PlacementCreationUser")];
                    	radiaSLDto.setSelllineCreatingUser(val);
                    	
                    	val = tokens[headingMap.get("LineItemType")];
                    	radiaSLDto.setLineItemType(val);
                    	
                    	
                    	val = tokens[headingMap.get("BudgetLineId")];
                    	radiaSLDto.setBudgetLineId(val);
                    	
                    	val = tokens[headingMap.get("MarginPercentage")];
                    	if (val == null || val.equals(""))
                    	{}
                    	else 
                    	{	
                    		radiaSLDto.setMargin(Double.valueOf(val));
                    		
                    	}
                    	
                        
                    	val = tokens[headingMap.get("MarginValue")];
                    	if (val == null || val.equals(""))
                    	{}
                    	else 
                    	{	
                    		radiaSLDto.setMarginValue(Double.valueOf(val));
                    	}
                    	
                    	//SFG-3050
                    	val = tokens[headingMap.get("BuyCategory")];
                    	radiaSLDto.setBuyCategory(val);
                    	
                    	 
                    	//SFG-3077
                    	val = tokens[headingMap.get("InventoryType")];
                    	radiaSLDto.setInventoryType(val);
                    	
                    	//SFG-3076
                    	val = tokens[headingMap.get("TargetInfo")];
                    	radiaSLDto.setTargetInfo(val);
                    	 
                    	
                    	 
                    	val = tokens[headingMap.get("is_deleted")];
                    	//Logger.log("**********placement number is " + radiaSLDto.getRadiaId(), true);
                    	//Logger.log("**********tokens[headingMap.get('is_deleted')] is " + tokens[headingMap.get("is_deleted")], true);
                    	
                    	boolean isDeleted = false;
                        if (val == null || val.equals(""))
                        {
                        	//Logger.log("**********in if (val == null || val.equals(''))", true);
                        }
                        else
                        {
                        	if(val.equalsIgnoreCase("TRUE"))
                        	{
                        		//Logger.log("**********right b4 deleteSellLineList.add(radiaSLDto.getRadiaId());", true);
                        		deleteSellLineList.add(radiaSLDto.getRadiaId());
                        		isDeleted = true;
                        	}
                        }
                    	 
                    	//Logger.log("isDeleted is " + isDeleted, true);
                    	
                        
                        if (isDeleted == false)
                        {
                        	sellLineList.add(radiaSLDto);
                        	//Logger.log("**********right after sellLineList.add(radiaSLDto);", true);
                        	//Logger.log("isDeleted is " + isDeleted, true);
                        	 
                        }
                        //System.out.println("sell line row number is " + k);
                    
                    }//if (budgetLineId.equalsIgnoreCase("NULL") && packageType.equalsIgnoreCase("TradingDesk"))
                    
			    }//if (k>=2)
			    k++;
			    }//if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			}//while loop
			 
		    Logger.log("sellLineCounter is " + sellLineCounter, true);
                
                System.out.println("sellLineList.size() is " + sellLineList.size());
                Logger.log("sellLineList.size() is " + sellLineList.size(), true);
                int counter = 0;
                
                for (int j = 0; j< sellLineList.size(); j++)
                {
                    if(sellLineMap.get(((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId()) != null)
                    {
                        sellLineMap.get(((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId()).add((RadiaSellLineDTO)sellLineList.get(j));
                        //System.out.println("((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId()) is " +  ((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId());
                        if (((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId().equals("122980"))
                        {
                        	counter++;
                        	//System.out.println("counter is " + counter);
                        	//Logger.log("counter is " + counter, true);
                        	//System.out.println("sell line placement id is " + ((RadiaSellLineDTO)sellLineList.get(j)).getPlacementId());
                        	//Logger.log("sell line placemen id is " + ((RadiaSellLineDTO)sellLineList.get(j)).getPlacementId() ,true);
                        } 
                        
                    }else
                    {
                        List<RadiaSellLineDTO> listSellLine = new ArrayList<RadiaSellLineDTO>();
                        listSellLine.add((RadiaSellLineDTO)sellLineList.get(j));
                        sellLineMap.put(((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId(), listSellLine);
                        //System.out.println("((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() is " + ((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() + " -- listSellLine is " + listSellLine);
                        //Logger.log("((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() is " + ((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() + " -- listSellLine is " + listSellLine, true); 
                        
                    
                    }
                }
                
            
            //fis.close();

 

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            Logger.log(e.getMessage(), true);

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
				  
			 }
		 
		}
		finally	{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), true);
			}
		}
        return sellLineMap;

    }
    
    public static  HashMap getBuyPlacementFromExcel(String fileName) throws Exception {

    	ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
  		} catch (IOException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
  		String filePath = propertyMap.get("ftp_localdirectory"); 
    	
    	HashMap<String, List<RadiaBuyPlacementDTO>> buyPlacementMap = new HashMap<String, List<RadiaBuyPlacementDTO>>();
    	
    	List buyPlacementList = new ArrayList();

        FileInputStream fis = null;
        
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\492_Radia_Placement_Details_635859324014689133.csv"; //csv file address
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\" + fileName; //csv file address
        String csvFileAddress = filePath + fileName;    

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));

        try {
        	
        	Map<String,Integer> headingMap = new HashMap<String,Integer>();
			
			/*
			SupplierCode	SupplierZone	SupplierBusinessKey	SupplierName	Folio	CampaignId	
			CampaignPublicId	CampaignName	BuyType	MediaCode	MediaName	AdvertiserCode	
			AdvertiserBusinessKey	AdvertiserName	ProductCode	ProductBusinessKey	ProductName	EstimateCode	
			EstimateName	EstimateStartDate	EstimateEndDate	EstimateBusinessKey	PlacementId	PlacementName	
			PlacementNumber	BuyCategory	PlacementStartDate	PlacementEndDate	PlacementCategory	PlacementUser
			PlacementType	Site	Dimension	Positioning	Section	CostMethod	
			UnitType	Market	MarketRate	Rate	IONumber	ServedBy	
			CapCost	TargetInfo	Comments	FileType	MaxFileSize	FlashVersion	
			CreativeDueDate	MaxLooping	MaxAnimationTiming	RichMediaAccepted	RichMediaIncCmp	EstimatedUniqueViews	
			EstimatedPageViews	ShareOfVoice	ClickThroughUrl	SupplierProductName	SupplierValidityEndDate	SupplierSalesOrderRefVer	
			SupplierAdServerOrderId	SupplierPlacementRef	SupplierOrderDate	PlacementChangeDate	CreativeTypeDescription	InventoryType	
			AdserverName	InstanceName	AdserverAdvertiserCode	AdserverAdvertiserName	AdserverClientCode	AdserverClientName	
			AdserverSupplierCode	AdserverSupplierName	AdserverSiteCode	AdserverSiteName	PackageId	PackageType	
			SupplierSalesOrderRef	AdserverPlacementId	AdserverCampaignId	AdserverPackageId	PrimaryPlacement	PlacementCreationDate	
			PlacementCreationUser	LineItemType	BudgetLineId	MarginPercentage	MarginValue	IsPlacementDeleted	
			InsertDatetime	InsertBy	UpdateDatetime	UpdateBy	source_item_name	is_deleted	
			Rowstamp
			*/
			
			headingMap.put("SupplierCode", 0);
			headingMap.put("SupplierZone", 1);
			headingMap.put("SupplierBusinessKey", 2);
			headingMap.put("SupplierName", 3);
			headingMap.put("Folio", 4);
			headingMap.put("CampaignId", 5);
			headingMap.put("CampaignPublicId", 6);
			headingMap.put("CampaignName", 7);
			headingMap.put("BuyType", 8);
			headingMap.put("MediaCode", 9);
			headingMap.put("MediaName", 10);
			headingMap.put("AdvertiserCode", 11);
			headingMap.put("AdvertiserBusinessKey", 12);
			headingMap.put("AdvertiserName", 13);
			headingMap.put("ProductCode", 14);
			headingMap.put("ProductBusinessKey", 15);
			headingMap.put("ProductName", 16);
			headingMap.put("EstimateCode", 17);
			headingMap.put("EstimateName", 18);
			headingMap.put("EstimateStartDate", 19);
			headingMap.put("EstimateEndDate", 20);
			headingMap.put("EstimateBusinessKey", 21);
			headingMap.put("PlacementId", 22);
			headingMap.put("PlacementName", 23);
			headingMap.put("PlacementNumber", 24);
			headingMap.put("BuyCategory", 25);
			headingMap.put("PlacementStartDate", 26);
			headingMap.put("PlacementEndDate", 27);
			headingMap.put("PlacementCategory", 28);
			headingMap.put("PlacementUser", 29);
			headingMap.put("PlacementType", 30);
			headingMap.put("Site", 31);
			headingMap.put("Dimension", 32);
			headingMap.put("Positioning", 33);
			headingMap.put("Section", 34);
			headingMap.put("CostMethod", 35);
			headingMap.put("UnitType", 36);
			headingMap.put("Market", 37);
			headingMap.put("MarketRate", 38);
			headingMap.put("Rate", 39);
			headingMap.put("IONumber", 40);
			headingMap.put("ServedBy", 41);
			headingMap.put("CapCost", 42);
			headingMap.put("TargetInfo", 43);
			headingMap.put("Comments", 44);
			headingMap.put("FileType", 45);
			headingMap.put("MaxFileSize", 46);
			headingMap.put("FlashVersion", 47);
			headingMap.put("CreativeDueDate", 48);
			headingMap.put("MaxLooping", 49);
			headingMap.put("MaxAnimationTiming", 50);
			headingMap.put("RichMediaAccepted", 51);
			headingMap.put("RichMediaIncCmp", 52);
			headingMap.put("EstimatedUniqueViews", 53);
			headingMap.put("EstimatedPageViews", 54);
			headingMap.put("ShareOfVoice", 55);
			headingMap.put("ClickThroughUrl", 56);
			headingMap.put("SupplierProductName", 57);
			headingMap.put("SupplierValidityEndDate", 58);
			headingMap.put("SupplierSalesOrderRefVer", 59);
			headingMap.put("SupplierAdServerOrderId", 60);
			headingMap.put("SupplierPlacementRef", 61);
			headingMap.put("SupplierOrderDate", 62);
			headingMap.put("PlacementChangeDate", 63);
			headingMap.put("CreativeTypeDescription", 64);
			headingMap.put("InventoryType", 65);
			headingMap.put("AdserverName", 66);
			headingMap.put("InstanceName", 67);
			headingMap.put("AdserverAdvertiserCode", 68);
			headingMap.put("AdserverAdvertiserName", 69);
			headingMap.put("AdserverClientCode", 70);
			headingMap.put("AdserverClientName", 71);
			headingMap.put("AdserverSupplierCode", 72);
			headingMap.put("AdserverSupplierName", 73);
			headingMap.put("AdserverSiteCode", 74);
			headingMap.put("AdserverSiteName", 75);
			headingMap.put("PackageId", 76);
			headingMap.put("PackageType", 77);
			headingMap.put("SupplierSalesOrderRef", 78);
			headingMap.put("AdserverPlacementId", 79);
			headingMap.put("AdserverCampaignId", 80);
			headingMap.put("AdserverPackageId", 81);
			headingMap.put("PrimaryPlacement", 82);
			headingMap.put("PlacementCreationDate", 83);
			headingMap.put("PlacementCreationUser", 84);
			headingMap.put("LineItemType", 85);
			headingMap.put("BudgetLineId", 86);
			headingMap.put("MarginPercentage", 87);
			headingMap.put("MarginValue", 88);
			headingMap.put("IsPlacementDeleted", 89);
			headingMap.put("InsertDatetime", 90);
			headingMap.put("InsertBy", 91);
			headingMap.put("UpdateDatetime", 92);
			headingMap.put("UpdateBy", 93);
			headingMap.put("source_item_name", 94);
			headingMap.put("is_deleted", 95);
			headingMap.put("Rowstamp", 96);
            
        	 
        	//fis = new FileInputStream(FILE_PATH + fileName);
			List<String> lines = new ArrayList<>();
			String line = null;
			Map<Integer,String> lineMap = new HashMap<Integer,String>();
			int sellLineCounter = 0;
			
			int k=1;
			while ((line = reader.readLine()) != null) 
			{
				RadiaBuyPlacementDTO radiaBPDto = new RadiaBuyPlacementDTO();
				if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			    {
				lines.add(line);
			    lineMap.put(k, line);
			    //System.out.println("line length is " + line.length());
			    
			    
			    if (k>=2)
			    {
			    	String[] tokens = lineMap.get(k).split("\t",-1);
			    	
			    	placementFileNotEmpty = true;
			    
			    	String val = tokens[headingMap.get("BudgetLineId")];
                    String budgetLineId = "";
                    int budgetLineIdInt = 0;
                    if (val == null || val.equals(""))
                    {}
                    else
                    {
                    	budgetLineId =  val;
                    	budgetLineIdInt = Integer.parseInt(val);
                    	
                    }
                    
                    val = tokens[headingMap.get("PackageType")];
                    String packageType = "";
                    packageType = val;
                    
                    
                    int packageIdInt = 0;
                    val = tokens[headingMap.get("PackageId")];
                    if (val == null || val.equals(""))
                    {}
                    else
                    {
                    	packageIdInt = Integer.parseInt(val);
                    	
                    }
                	 //System.out.println("**************in getBuyPlacementFromExcel, budgetLineId is " + budgetLineId);
                    //if (!budgetLineId.equalsIgnoreCase("NULL") && !packageType.equalsIgnoreCase("TradingDesk") )
                	//if ((!budgetLineId.equalsIgnoreCase("NULL") && !packageType.equalsIgnoreCase("TradingDesk") && !(budgetLineIdInt == packageIdInt && packageType.equalsIgnoreCase("Child"))) || (budgetLineId.equalsIgnoreCase("NULL") && packageType.equalsIgnoreCase("FeeOrder")))
                	//if (tokens[headingMap.get("PlacementNumber")].equalsIgnoreCase("R4X0PR"))
                	//{
                	//	Logger.log("-------radia id is R4X0PR" + "/n budgetLineIdInt is " + budgetLineIdInt + "/n packageIdInt is " + packageIdInt + "/n packageType is " + packageType, true);
                	//}
                    
                  //*********************************************************************
                    //SFG-3750
                    java.util.Date date = new java.util.Date();
         		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String dateFormatTwoDigits =  sdf.format(date);
                    CharSequence todayDateTwoDigits = dateFormatTwoDigits; 
                    
                    SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yyyy");
                    String dateFormatOneDigit =  sdf1.format(date);
                    CharSequence todayDateOneDigit = dateFormatOneDigit; 
                    
                    val = tokens[headingMap.get("UpdateDatetime")];
                    String updateDateTime = val;
                    val = tokens[headingMap.get("InsertDatetime")];
                    String insertDateTime = val;
                    
                    
                    //Logger.log("today's date two digits is " + todayDateTwoDigits, true);
                    //Logger.log("today's date one digit is " + todayDateOneDigit, true);
                    //Logger.log("UpdateDatetime is " + updateDateTime, true);
                    //Logger.log("InsertDateTime is " + insertDateTime, true);
                    if (updateDateTime.contains(todayDateTwoDigits) || updateDateTime.contains(todayDateOneDigit) || insertDateTime.contains(todayDateTwoDigits) || insertDateTime.contains(todayDateOneDigit))
                    {
                    	//Logger.log("UpdateDateTime or InsertDateTime contains today's date", true);
                    	placementFileHasNewUpdate = true;
                    }
                    
                    //*********************************************************************
                    
                    if ((!budgetLineId.equalsIgnoreCase("") && !packageType.equalsIgnoreCase("TradingDesk") && !(budgetLineIdInt == packageIdInt && packageType.equalsIgnoreCase("Child"))) || (budgetLineId.equalsIgnoreCase("") && packageType.equalsIgnoreCase("FeeOrder")))
                    {
                        
                		val = tokens[headingMap.get("PlacementNumber")];
                        radiaBPDto.setRadiaId(val);
                        //System.out.println("placement number is " + radiaSLDto.getRadiaId());
                    	
                        val = tokens[headingMap.get("PlacementId")];
                    	radiaBPDto.setPlacementId(val);
                    	//System.out.println("placementId is " + radiaBPDto.getPlacementId());
                        //System.out.println("placement id is " + radiaSLDto.getPlacementId());
                    	
                    	val = tokens[headingMap.get("SupplierCode")];
                    	if (val !=null && !val.equals(""))
                    	{
                    		val = trimLeadingZeros(val);
                    	}
                    	radiaBPDto.setSupplierCode(val);
                        //Logger.log("---------------supplier code is " + val, true);
                        
                    	//Cell cell = row.getCell(0);
                    	val = tokens[headingMap.get("SupplierName")];
                    	radiaBPDto.setSupplierName(val);
                    	//System.out.println("SL supplierName is " + radiaSLDto.getSupplierName());
                    
                    	
                    	//cell = row.getCell(2);
                    	val = tokens[headingMap.get("PlacementName")];
                    	radiaBPDto.setPlacementName(val);
                    	//System.out.println(" PlacementName is " + radiaSLDto.getPlacementName());
                    	
                    	val = tokens[headingMap.get("AdvertiserName")];
                    	radiaBPDto.setMOAdvertiserName(val);
                    	
                    	val = tokens[headingMap.get("AdvertiserCode")];
                    	radiaBPDto.setMOAdvertiserCode(val);
                    	
                    	val = tokens[headingMap.get("ProductName")];
                    	radiaBPDto.setMOProductName(val);
                    	
                    	val = tokens[headingMap.get("ProductCode")];
                    	radiaBPDto.setMOProductCode(val);
                    
                    	val = tokens[headingMap.get("EstimateName")];
                    	radiaBPDto.setMOEstimateName(val);
                    	
                    	val = tokens[headingMap.get("EstimateCode")];
                    	radiaBPDto.setMOEstimateCode(val);
                    	
                    	val = tokens[headingMap.get("PlacementStartDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaBPDto.setStartDate(val);
                    	
                    	val = tokens[headingMap.get("PlacementEndDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaBPDto.setEndDate(val);
                    	
                    	val = tokens[headingMap.get("PlacementUser")];
                    	radiaBPDto.setLastModifiedBy(val);
                    	//System.out.println("--placementUser is " + radiaBPDto.getLastModifiedBy());
                    	
                    	val = tokens[headingMap.get("Site")];
                    	radiaBPDto.setSite(val);
                    	//System.out.println("--site is " + radiaBPDto.getSite());
                    	 
                    	val = tokens[headingMap.get("UnitType")];
                    	radiaBPDto.setUnitType(val);
                    	//System.out.println("--UnitType is " + radiaBPDto.getUnitType());
                    	
                    	val = tokens[headingMap.get("Rate")];
                    	if (val != null && !val.equals(""))
                    	{
                    		radiaBPDto.setRate(Double.valueOf(val));
                    	}
                    	
                    	val = tokens[headingMap.get("IONumber")];
                    	if (val != null && !val.equals(""))
                    	radiaBPDto.setVendorIONumber(val);
                    	
                    	val = tokens[headingMap.get("ServedBy")];
                    	if (val != null && !val.equals(""))
                    	radiaBPDto.setServedBy(val);
                    	
                    	val = tokens[headingMap.get("SupplierProductName")];
                    	if (val != null && !val.equals(""))
                    	radiaBPDto.setSupplierProductName(val);
                    	
                    	val = tokens[headingMap.get("PlacementChangeDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaBPDto.setLastChangedInRadia(val);
                    	
                    	val = tokens[headingMap.get("AdserverName")];
                    	radiaBPDto.setAdserverName(val);
                    	
                    	val = tokens[headingMap.get("PackageId")];
                    	if (val != null && !val.equals(""))
                    	radiaBPDto.setPackageId(val);
                    	
                    	val = tokens[headingMap.get("PackageType")];
                    	radiaBPDto.setPackageType(val);
                    	 
                    	val = tokens[headingMap.get("PlacementCreationDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                        	val = dateStringFormat(val);
                    	radiaBPDto.setBuylineCreationDate(val);
                    	
                    	val = tokens[headingMap.get("PlacementCreationUser")];
                    	radiaBPDto.setBuylineCreatingUser(val);
                    	
                    	val = tokens[headingMap.get("LineItemType")];
                    	radiaBPDto.setLineItemType(val);
                    	
                    	val = tokens[headingMap.get("CampaignPublicId")];
                    	String campaignPublicId = "";
                    	campaignPublicId = val;
                    	
                    	val = tokens[headingMap.get("BudgetLineId")];
                    	//if (budgetLineId.equalsIgnoreCase("NULL") && packageType.equalsIgnoreCase("FeeOrder"))
                    	if (budgetLineId.equalsIgnoreCase("") && packageType.equalsIgnoreCase("FeeOrder"))
                        {
                    		//if (cell.CELL_TYPE_STRING == cell.getCellType())
                    		if (val == null || val.equals(""))
                            {
                            	 //radiaBPDto.setBudgetLineId(campaignPublicId+"dummy");
                    			radiaBPDto.setBudgetLineId(campaignPublicId+"FeeLine");
                            }
                        	else 
                            {
                            	
                            }
                    	}
                    	
                    	else
                    	{
                    		if (val != null && !val.equals(""))
                    		{
                    			 radiaBPDto.setBudgetLineId(val);
                    		}
                    	}
                    	
                    	//System.out.println("PlacementId is " + radiaBPDto.getPlacementId() + "-- BudgetLineId is " + radiaBPDto.getBudgetLineId());
                    	//Logger.log("PlacementId is " + radiaBPDto.getPlacementId() + "-- BudgetLineId is " + radiaBPDto.getBudgetLineId(),true);
                    	
                    	val = tokens[headingMap.get("MarginPercentage")];
                    	if (val == null || val.equals(""))
                    	{}
                    	else 
                    	{	
                    		radiaBPDto.setMargin(Double.valueOf(val));
                    	}
                    	
                    	val = tokens[headingMap.get("MarginValue")];
                    	if (val == null || val.equals(""))
                    	{}
                    	else 
                    	{	
                    		radiaBPDto.setMarginValue(Double.valueOf(val));
                    	}
                    	
                    	val = tokens[headingMap.get("Comments")];
                    	if (val != null && !val.equals(""))
                    	radiaBPDto.setComments(val);
                    	
                    	//Added on 2016-03-17
                    	val = tokens[headingMap.get("CostMethod")];
                    	radiaBPDto.setCostMethod(val);
                    	//Added on 2016-03-17
                    	val = tokens[headingMap.get("Dimension")];
                    	radiaBPDto.setCreativeSize(val);
                    	
                    	//SFG-3050
                    	val = tokens[headingMap.get("BuyCategory")];
                    	radiaBPDto.setBuyCategory(val);;
                    	
                    	 
                    	//SFG-3077
                    	val = tokens[headingMap.get("InventoryType")];
                    	radiaBPDto.setInventoryType(val);
                    	
                    	//SFG-3076
                    	val = tokens[headingMap.get("TargetInfo")];
                    	radiaBPDto.setTargetInfo(val);
                    	
                    	//SFG-3113
                    	if (packageType.equalsIgnoreCase("FeeOrder"))
                    	{
                    		val = tokens[headingMap.get("InstanceName")];
                    		radiaBPDto.setInstanceName(val);
                    	}
                    	
                    	//SFG-3046
                    	val = tokens[headingMap.get("CreativeTypeDescription")];
                    	radiaBPDto.setCreativeTypeDescription(val);
                    	  
                    	
                    	val = tokens[headingMap.get("is_deleted")];
                    	boolean isDeleted = false;
                        if (val == null || val.equals(""))
                        {}
                        else
                        {
                        	if(val.equalsIgnoreCase("TRUE"))
                        	{
                        		deleteBuyPlacementList.add(radiaBPDto.getRadiaId());
                        		isDeleted = true;
                        	}
                        }
                    	 
                    	if (isDeleted == false)
                    	buyPlacementList.add(radiaBPDto);
                    
                    }//if ((!budgetLineId.equalsIgnoreCase("") && !packageType.equalsIgnoreCase("TradingDesk") && !(budgetLineIdInt == packageIdInt && packageType.equalsIgnoreCase("Child"))) || (budgetLineId.equalsIgnoreCase("") && packageType.equalsIgnoreCase("FeeOrder")))
                    
			    }//if (k>=2)
			    k++;
			    }//if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			}//while loop
 
 
                
                System.out.println("buyPlacementList.size() is " + buyPlacementList.size());
                Logger.log("buyPlacementList.size() is " + buyPlacementList.size(), true);
                int counter = 0;
                
                for (int j = 0; j< buyPlacementList.size(); j++)
                {
                    if(buyPlacementMap.get(((RadiaBuyPlacementDTO) buyPlacementList.get(j)).getBudgetLineId()) != null)
                    {
                        buyPlacementMap.get(((RadiaBuyPlacementDTO) buyPlacementList.get(j)).getBudgetLineId()).add((RadiaBuyPlacementDTO)buyPlacementList.get(j));
                        //System.out.println("((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId()) is " +  ((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId());
                        if (((RadiaBuyPlacementDTO) buyPlacementList.get(j)).getBudgetLineId().equals("4508360"))
                        {
                        	counter++;
                        	System.out.println("for budgetLindid 4508360, buy placement counter is " + counter);
                        	//Logger.log("for budgetLindid 4508360, buy placement counter is " + counter, true);
                        	//Logger.log("buy placement counter is " + counter, true);
                        	 
                        } 
                        
                    }else
                    {
                        List<RadiaBuyPlacementDTO> listBuyPlacement = new ArrayList<RadiaBuyPlacementDTO>();
                        listBuyPlacement.add((RadiaBuyPlacementDTO)buyPlacementList.get(j));
                        buyPlacementMap.put(((RadiaBuyPlacementDTO) buyPlacementList.get(j)).getBudgetLineId(), listBuyPlacement);
                        //System.out.println("((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() is " + ((RadiaSellLineDTO) sellLineList.get(j)).getCampaignId() + " -- listSellLine is " + listSellLine);
                        //Logger.log("((RadiaBuyPlacementDTO) buyPlacementList.get(j)).getBudgetLineId() is " + ((RadiaBuyPlacementDTO) buyPlacementList.get(j)).getBudgetLineId() + " -- listBuyPlacement is " + listBuyPlacement, true); 
                        
                    
                    }
                }
                
            
 

           // fis.close();

 

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            Logger.log(e.getMessage(), true);

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
				  
			 }
		 
		}
		finally	{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), true);
			}
		}

        return buyPlacementMap;

    }
 
    public static HashMap getSLMonthlyScheduleFromExcel(String fileName) throws Exception {
    	
    	ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
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
				  
			 }
		 
		}
  		String filePath = propertyMap.get("ftp_localdirectory");
      
    	HashMap<String, List<RadiaSLMonthlyScheduleDTO>> slmsMap = new HashMap<String, List<RadiaSLMonthlyScheduleDTO>>();
    	
    	List slmsList = new ArrayList();

        FileInputStream fis = null;
        
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\494_Radia_Placements_Monthly_635858628353483617.csv"; //csv file address
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\" + fileName; //csv file address
        String csvFileAddress = filePath + fileName;   

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));

        try {
        	/*
        	PlacementId	PlacementNumber	PackageType	PrimaryPlacement	PlacementMonth	LateStartDate	
        	EarlyEndDate	PlacementMonthlyStartDate	PlacementMonthlyEndDate	PlannedAmount	PlannedUnits	PlannedImpressions	
        	PlannedClicks	PlannedActions	IOAmount	SupplierUnits	SupplierImpressions	SupplierClicks	
        	SupplierActions	SupplierCost	AdserverUnits	AdserverImpressions	AdserverClicks	AdserverActions	
        	AdserverCost	EffectivePostImpressions	EffectivePostClicks	EffectiveActions	WeightedPostImpressions	WeightedPostClicks	
        	WeightedActions	ConversionPostImpressions	ConversionPostClicks	ConversionActions	DeliveryExists	BillablePlannedAmount	
        	PlannedFees	PlannedMargin	TpFeeActualCost	CampaignId	InsertDatetime	InsertBy	
        	UpdateDatetime	UpdateBy	source_item_name	is_deleted	Rowstamp
 			*/
        	
        	Map<String,Integer> headingMap = new HashMap<String,Integer>();
        	headingMap.put("PlacementId", 0);
        	headingMap.put("PlacementNumber", 1);
        	headingMap.put("PackageType", 2);
        	headingMap.put("PrimaryPlacement", 3);
        	headingMap.put("PlacementMonth", 4);
        	headingMap.put("LateStartDate", 5);
        	headingMap.put("EarlyEndDate", 6);
        	headingMap.put("PlacementMonthlyStartDate", 7);
        	headingMap.put("PlacementMonthlyEndDate", 8);
        	headingMap.put("PlannedAmount", 9);
        	headingMap.put("PlannedUnits", 10);
        	headingMap.put("PlannedImpressions", 11);
        	headingMap.put("PlannedClicks", 12);
        	headingMap.put("PlannedActions", 13);
        	headingMap.put("IOAmount", 14);
        	headingMap.put("SupplierUnits", 15);
        	headingMap.put("SupplierImpressions", 16);
        	headingMap.put("SupplierClicks", 17);
        	headingMap.put("SupplierActions", 18);
        	headingMap.put("SupplierCost", 19);
        	headingMap.put("AdserverUnits", 20);
        	headingMap.put("AdserverImpressions", 21);
        	headingMap.put("AdserverClicks", 22);
        	headingMap.put("AdserverActions", 23);
        	headingMap.put("AdserverCost", 24);
        	headingMap.put("EffectivePostImpressions", 25);
        	headingMap.put("EffectivePostClicks", 26);
        	headingMap.put("EffectiveActions", 27);
        	headingMap.put("WeightedPostImpressions", 28);
        	headingMap.put("WeightedPostClicks", 29);
        	headingMap.put("WeightedActions", 30);
        	headingMap.put("ConversionPostImpressions", 31);
        	headingMap.put("ConversionPostClicks", 32);
        	headingMap.put("ConversionActions", 33);
        	headingMap.put("DeliveryExists", 34);
        	headingMap.put("BillablePlannedAmount", 35);
        	headingMap.put("PlannedFees", 36);
        	headingMap.put("PlannedMargin", 37);
        	headingMap.put("TpFeeActualCost", 38);
        	headingMap.put("CampaignId", 39);
        	headingMap.put("InsertDatetime", 40);
        	headingMap.put("InsertBy", 41);
        	headingMap.put("UpdateDatetime", 42);
        	headingMap.put("UpdateBy", 43);
        	headingMap.put("source_item_name", 44);
        	headingMap.put("is_deleted", 45);
        	headingMap.put("Rowstamp", 46);
        	headingMap.put("PlannedBillableUnits", 48);
        	
        	

        	
        	//fis = new FileInputStream(FILE_PATH + fileName);
			List<String> lines = new ArrayList<>();
			String line = null;
			Map<Integer,String> lineMap = new HashMap<Integer,String>();
			int sellLineCounter = 0;
			
			int k=1;
			while ((line = reader.readLine()) != null) 
			{
				RadiaBuyPlacementDTO radiaBPDto = new RadiaBuyPlacementDTO();
				if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			    {
				lines.add(line);
			    lineMap.put(k, line);
			    //System.out.println("line length is " + line.length());
			    
			    
			    if (k>=2)
			    {
			    	
			    	RadiaSLMonthlyScheduleDTO radiaSLMSDto = new RadiaSLMonthlyScheduleDTO();
			    	String[] tokens = lineMap.get(k).split("\t",-1);
			    	
			    	monthlyScheduleFileNotEmpty = true;
			    	
			    	String val = tokens[headingMap.get("PackageType")];
                    String packageType = "";
                    packageType = val;
                    String tempStr = "";
                    //System.out.println("packageType is " + packageType); 
                    
                  //*********************************************************************
                    //SFG-3750
                    java.util.Date date = new java.util.Date();
         		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String dateFormatTwoDigits =  sdf.format(date);
                    CharSequence todayDateTwoDigits = dateFormatTwoDigits; 
                    
                    SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yyyy");
                    String dateFormatOneDigit =  sdf1.format(date);
                    CharSequence todayDateOneDigit = dateFormatOneDigit; 
                    
                    val = tokens[headingMap.get("UpdateDatetime")];
                    String updateDateTime = val;
                    val = tokens[headingMap.get("InsertDatetime")];
                    String insertDateTime = val;
                    
                    
                    //Logger.log("today's date two digits is " + todayDateTwoDigits, true);
                    //Logger.log("today's date one digit is " + todayDateOneDigit, true);
                    //Logger.log("UpdateDatetime is " + updateDateTime, true);
                    //Logger.log("InsertDateTime is " + insertDateTime, true);
                    if (updateDateTime.contains(todayDateTwoDigits) || updateDateTime.contains(todayDateOneDigit) || insertDateTime.contains(todayDateTwoDigits) || insertDateTime.contains(todayDateOneDigit))
                    {
                    	//Logger.log("UpdateDateTime or InsertDateTime contains today's date", true);
                    	monthlyScheduleFileHasNewUpdate = true;
                    }
                    
                    //*********************************************************************
                    
                    
                	if (packageType.equalsIgnoreCase("TradingDesk"))
                    {
                        
                		val = tokens[headingMap.get("PlacementNumber")];
                    	radiaSLMSDto.setPlacementNumber(val);
                        //System.out.println("placement number is " + radiaSLDto.getRadiaId());
                    	
                    	val = tokens[headingMap.get("PlacementId")];
                        radiaSLMSDto.setPlacementId(val);
                    	
                        
                        //val = tokens[headingMap.get("PlacementMonth")];
                        String valSLMSName = "";
                        val = tokens[headingMap.get("PlacementMonthlyStartDate")];
                        tempStr = tokens[headingMap.get("PlacementMonthlyStartDate")];;
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                        if (val != null && !val.equals(""))
                        {
                        	val = dateStringFormat(val);
                        	radiaSLMSDto.setScheduleDate(val);
                        	valSLMSName = dateStringFormatForScheduleName(tempStr);
                        }
                    	 
                        
                    	val = tokens[headingMap.get("PlacementMonthlyStartDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                    	{
                        	val = dateStringFormat(val);
                        	radiaSLMSDto.setStartDate(val);
                    	}
                    	
                    	val = tokens[headingMap.get("PlacementMonthlyEndDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                    	{
                        	val = dateStringFormat(val);
                        	radiaSLMSDto.setEndDate(val);
                    	}
                    	
                    	val = tokens[headingMap.get("BillablePlannedAmount")];
                    	if (val == null || val.equals(""))
                    	{}
                    	else
                    	{
                    		radiaSLMSDto.setCost(Double.valueOf(val));
                    	}
                    	
                    	//SFG-3686
                    	val = tokens[headingMap.get("PlannedBillableUnits")];
                    	if (val == null || val.equals(""))
                    	{
                    		 
                    	}
                    	else
                    	{
                    		radiaSLMSDto.setPlannedBillableUnits(Double.valueOf(val));
                    	}
                    	//Logger.log("------radiaSLMSDto.getPlannedBillableUnits is " + radiaSLMSDto.getPlannedBillableUnits(), true);
                    	
                    	String slmsRadiaId = "";
                    	//Logger.log("--------valSLMSName is " + valSLMSName, true);
                		slmsRadiaId = radiaSLMSDto.getPlacementNumber() + "-" + valSLMSName;
                		radiaSLMSDto.setRadiaId(slmsRadiaId);
                    	
                		 
                		val = tokens[headingMap.get("is_deleted")];
                    	boolean isDeleted = false;
                        if (val == null || val.equals(""))
                        {}
                        else
                        {
                        	if(val.equalsIgnoreCase("TRUE"))
                        	{
                        		
                        		deleteSLMonthlyScheduleList.add(slmsRadiaId);
                        		isDeleted = true;
                        	}
                        }
                    	//System.out.println("before add to list, k is " + k + " --- radiaSLMSDto.getPlacementNumber() " + radiaSLMSDto.getPlacementNumber() + " isDeleted is " + isDeleted);
                        if (isDeleted == false) 
                    	slmsList.add(radiaSLMSDto);
			    	
			    	
			    }//if (packageType.equalsIgnoreCase("TradingDesk"))
                
			    }//if (k>=2)
			    k++;
			    }//if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			}//while loop	
        	 
        	//fis = new FileInputStream(FILE_PATH + fileName);

  
                System.out.println("slmsList.size() is " + slmsList.size());
                Logger.log("slmsList.size() is " + slmsList.size(), true);
                int counter = 0;
                
                for (int j = 0; j< slmsList.size(); j++)
                {
                    if(slmsMap.get(((RadiaSLMonthlyScheduleDTO) slmsList.get(j)).getPlacementNumber()) != null)
                    {
                    	slmsMap.get(((RadiaSLMonthlyScheduleDTO) slmsList.get(j)).getPlacementNumber()).add((RadiaSLMonthlyScheduleDTO)slmsList.get(j));
                    	//Logger.log("In if statement, ((RadiaSLMonthlyScheduleDTO) slmsList.get(j)).getPlacementNumber() is " + ((RadiaSLMonthlyScheduleDTO) slmsList.get(j)).getPlacementNumber() + " -- (RadiaSLMonthlyScheduleDTO)slmsList.get(j) is " + (RadiaSLMonthlyScheduleDTO)slmsList.get(j), true); 
                         
                        
                    }else
                    {
                        List<RadiaSLMonthlyScheduleDTO> listSLMS = new ArrayList<RadiaSLMonthlyScheduleDTO>();
                        listSLMS.add((RadiaSLMonthlyScheduleDTO)slmsList.get(j));
                        slmsMap.put(((RadiaSLMonthlyScheduleDTO) slmsList.get(j)).getPlacementNumber(), listSLMS);
                        //Logger.log("In else statement, ((RadiaSLMonthlyScheduleDTO) slmsList.get(j)).getPlacementNumber() is " + ((RadiaSLMonthlyScheduleDTO) slmsList.get(j)).getPlacementNumber() + " -- listSLMS is " + listSLMS, true); 
                        
                    
                    }
                }
                
            
 

            //fis.close();

 

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            Logger.log(e.getMessage(), true);

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
				  
			 }
		 
		}
		finally	{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), true);
			}
		}

        return slmsMap;

    }
    
    public static HashMap getPlacementMonthlyScheduleFromExcel(String fileName) throws Exception {

    	Logger.log("--in getPlacementMonthlyScheduleFromExcel, fileName is " + fileName, true);
    	ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
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
				  
			 }
		 
		}
  		String filePath = propertyMap.get("ftp_localdirectory");
    	
    	HashMap<String, List<RadiaPlacementMonthlyScheduleDTO>> pmsMap = new HashMap<String, List<RadiaPlacementMonthlyScheduleDTO>>();
    	
    	List pmsList = new ArrayList();

        FileInputStream fis = null;
        
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\494_Radia_Placements_Monthly_635858628353483617.csv"; //csv file address
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\"+ fileName; //csv file address
        String csvFileAddress = filePath + fileName;   

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));

        try {

            
        	Map<String,Integer> headingMap = new HashMap<String,Integer>();
        	headingMap.put("PlacementId", 0);
        	headingMap.put("PlacementNumber", 1);
        	headingMap.put("PackageType", 2);
        	headingMap.put("PrimaryPlacement", 3);
        	headingMap.put("PlacementMonth", 4);
        	headingMap.put("LateStartDate", 5);
        	headingMap.put("EarlyEndDate", 6);
        	headingMap.put("PlacementMonthlyStartDate", 7);
        	headingMap.put("PlacementMonthlyEndDate", 8);
        	headingMap.put("PlannedAmount", 9);
        	headingMap.put("PlannedUnits", 10);
        	headingMap.put("PlannedImpressions", 11);
        	headingMap.put("PlannedClicks", 12);
        	headingMap.put("PlannedActions", 13);
        	headingMap.put("IOAmount", 14);
        	headingMap.put("SupplierUnits", 15);
        	headingMap.put("SupplierImpressions", 16);
        	headingMap.put("SupplierClicks", 17);
        	headingMap.put("SupplierActions", 18);
        	headingMap.put("SupplierCost", 19);
        	headingMap.put("AdserverUnits", 20);
        	headingMap.put("AdserverImpressions", 21);
        	headingMap.put("AdserverClicks", 22);
        	headingMap.put("AdserverActions", 23);
        	headingMap.put("AdserverCost", 24);
        	headingMap.put("EffectivePostImpressions", 25);
        	headingMap.put("EffectivePostClicks", 26);
        	headingMap.put("EffectiveActions", 27);
        	headingMap.put("WeightedPostImpressions", 28);
        	headingMap.put("WeightedPostClicks", 29);
        	headingMap.put("WeightedActions", 30);
        	headingMap.put("ConversionPostImpressions", 31);
        	headingMap.put("ConversionPostClicks", 32);
        	headingMap.put("ConversionActions", 33);
        	headingMap.put("DeliveryExists", 34);
        	headingMap.put("BillablePlannedAmount", 35);
        	headingMap.put("PlannedFees", 36);
        	headingMap.put("PlannedMargin", 37);
        	headingMap.put("TpFeeActualCost", 38);
        	headingMap.put("CampaignId", 39);
        	headingMap.put("InsertDatetime", 40);
        	headingMap.put("InsertBy", 41);
        	headingMap.put("UpdateDatetime", 42);
        	headingMap.put("UpdateBy", 43);
        	headingMap.put("source_item_name", 44);
        	headingMap.put("is_deleted", 45);
        	headingMap.put("Rowstamp", 46);
        	
        	//fis = new FileInputStream(FILE_PATH + fileName);
			List<String> lines = new ArrayList<>();
			String line = null;
			Map<Integer,String> lineMap = new HashMap<Integer,String>();
			int sellLineCounter = 0;
			
			int k=1;
			while ((line = reader.readLine()) != null) 
			{
				RadiaBuyPlacementDTO radiaBPDto = new RadiaBuyPlacementDTO();
				if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			    {
				lines.add(line);
			    lineMap.put(k, line);
			    //System.out.println("line length is " + line.length());
			    //Logger.log(line, true);
			    
			    if (k>=2)
			    {
			    	
			    	RadiaPlacementMonthlyScheduleDTO radiaPMSDto = new RadiaPlacementMonthlyScheduleDTO();
			    	String[] tokens = lineMap.get(k).split("\t",-1);
			    	
			    	monthlyScheduleFileNotEmpty = true;
			    	
			    	String val = tokens[headingMap.get("PackageType")];
                    String packageType = "";
                    packageType = val;
                    String tempStr = "";
                    
                  //*********************************************************************
                    //SFG-3750
                    java.util.Date date = new java.util.Date();
         		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String dateFormatTwoDigits =  sdf.format(date);
                    CharSequence todayDateTwoDigits = dateFormatTwoDigits; 
                    
                    SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yyyy");
                    String dateFormatOneDigit =  sdf1.format(date);
                    CharSequence todayDateOneDigit = dateFormatOneDigit; 
                    
                    val = tokens[headingMap.get("UpdateDatetime")];
                    String updateDateTime = val;
                    val = tokens[headingMap.get("InsertDatetime")];
                    String insertDateTime = val;
                    
                    
                    //Logger.log("today's date two digits is " + todayDateTwoDigits, true);
                    //Logger.log("today's date one digit is " + todayDateOneDigit, true);
                    //Logger.log("UpdateDatetime is " + updateDateTime, true);
                    //Logger.log("InsertDateTime is " + insertDateTime, true);
                    if (updateDateTime.contains(todayDateTwoDigits) || updateDateTime.contains(todayDateOneDigit) || insertDateTime.contains(todayDateTwoDigits) || insertDateTime.contains(todayDateOneDigit))
                    {
                    	//Logger.log("UpdateDateTime or InsertDateTime contains today's date", true);
                    	monthlyScheduleFileHasNewUpdate = true;
                    }
                    
                    //*********************************************************************
                    
                	 //System.out.println("**************in getBuyPlacementFromExcel, budgetLineId is " + budgetLineId);
                    //if (!budgetLineId.equalsIgnoreCase("NULL") && !packageType.equalsIgnoreCase("TradingDesk") )
                	//if ((!budgetLineId.equalsIgnoreCase("NULL") && !packageType.equalsIgnoreCase("TradingDesk") && !(budgetLineIdInt == packageIdInt && packageType.equalsIgnoreCase("Child"))) || (budgetLineId.equalsIgnoreCase("NULL") && packageType.equalsIgnoreCase("FeeOrder")))
                	if (!packageType.equalsIgnoreCase("TradingDesk"))
                    {
                        
                		val = tokens[headingMap.get("PlacementNumber")];
                    	radiaPMSDto.setPlacementNumber(val);
                        
                    	//val = tokens[headingMap.get("PlacementMonth")];
                    	String valPMSName = "";
                    	val = tokens[headingMap.get("PlacementMonthlyStartDate")];
                    	tempStr = tokens[headingMap.get("PlacementMonthlyStartDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                    	{
                        	val = dateStringFormat(val);
                        	valPMSName = dateStringFormatForScheduleName(tempStr);
                        	radiaPMSDto.setScheduleDate(val);
                    	}
                    	
                    	
                    	val = tokens[headingMap.get("PlacementMonthlyStartDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                    	{
                        	val = dateStringFormat(val);
                        	radiaPMSDto.setStartDate(val);
                    	}
                    	
                    	val = tokens[headingMap.get("PlacementMonthlyEndDate")];
                    	//val = val.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3-$1-$2");
                    	if (val != null && !val.equals(""))
                    	{
                        	val = dateStringFormat(val);
                        	radiaPMSDto.setEndDate(val);
                    	}
                    	
                    	val = tokens[headingMap.get("BillablePlannedAmount")];
                    	if (val == null || val.equals(""))
                    	{}
                    	else
                    	{
                    		radiaPMSDto.setBillablePlannedAmount(Double.valueOf(val));
                    	}
                    	
                    	val = tokens[headingMap.get("PlannedAmount")];
                    	if (val == null || val.equals(""))
                    	{}
                    	else
                    	{
                    		radiaPMSDto.setPlannedAmount(Double.valueOf(val));
                    	}
                    	
                    	val = tokens[headingMap.get("PlannedUnits")];
                    	if (val == null || val.equals(""))
                    	{
                    		//radiaPMSDto.setPlannedUnits(Double.valueOf(0)); 
                    	}
                    	else
                    	{
                    		radiaPMSDto.setPlannedUnits(Double.valueOf(val));
                    	}
                    	
                    	val = tokens[headingMap.get("SupplierUnits")];
                    	if (val == null ||  val.equals(""))
                    	{
                    		//radiaPMSDto.setSupplierUnits(Double.valueOf(0));
                    	}
                    	else
                    	{
                    		radiaPMSDto.setSupplierUnits(Double.valueOf(val));
                    	}
                    	
                    	val = tokens[headingMap.get("PackageType")];
                    	radiaPMSDto.setPackageType(val);
                    	
                    	 
                    	val = tokens[headingMap.get("PlannedFees")];
                    	//Logger.log("------PlannedFees is " + val, true);
                    	if (val == null || val.equals(""))
                    	{
                    		 
                    	}
                    	else
                    	{
                    		radiaPMSDto.setPlannedFees(Double.valueOf(val));
                    	}
                    	 
                    	  
                    	 
                    	String pmsRadiaId = "";
                		//pmsRadiaId = radiaPMSDto.getPlacementNumber() + radiaPMSDto.getScheduleDate();
                    	pmsRadiaId = radiaPMSDto.getPlacementNumber() + "-" + valPMSName; //2016-03-30 to follow the format of RadiaID-yyyy-MM
                    	radiaPMSDto.setRadiaId(pmsRadiaId);
                    	//if (radiaPMSDto.getPlacementNumber().equalsIgnoreCase("R4X0T3"))
                		//System.out.println("----------- With Radia ID R4X0T3, placement schedule is " + radiaPMSDto.getRadiaId());
                		
                		val = tokens[headingMap.get("is_deleted")];
                    	boolean isDeleted = false;
                        if (val == null || val.equals(""))
                        {}
                        else
                        {
                        	if(val.equalsIgnoreCase("TRUE"))
                        	{
                        		
                        		deletePlacementMonthlyScheduleList.add(pmsRadiaId);
                        		isDeleted = true;
                        	}
                        }
                    	if (isDeleted == false) 
                    	pmsList.add(radiaPMSDto);
			    	
			    	
			    }//if (packageType.equalsIgnoreCase("TradingDesk"))
                
			    }//if (k>=2)
			    k++;
			    }//if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			}//while loop	
        	 

        	 
        	//fis = new FileInputStream(FILE_PATH + fileName);

 
                
                System.out.println("pmsList.size() is " + pmsList.size());
                Logger.log("pmsList.size() is " + pmsList.size(), true);
                int counter = 0;
                
                for (int j = 0; j< pmsList.size(); j++)
                {
                    if(pmsMap.get(((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber()) != null)
                    {
                    	pmsMap.get(((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber()).add((RadiaPlacementMonthlyScheduleDTO)pmsList.get(j));
                    	//Logger.log("In if statement, ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() is " + ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() + " -- (RadiaPlacementMonthlyScheduleDTO)pmsList.get(j) is " + (RadiaPlacementMonthlyScheduleDTO)pmsList.get(j), true); 
                         
                        
                    }else
                    {
                        List<RadiaPlacementMonthlyScheduleDTO> listPMS = new ArrayList<RadiaPlacementMonthlyScheduleDTO>();
                        listPMS.add((RadiaPlacementMonthlyScheduleDTO)pmsList.get(j));
                        pmsMap.put(((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber(), listPMS);
                        //Logger.log("In else statement, ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() is " + ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() + " -- listPMS is " + listPMS, true); 
                        
                    
                    }
                }
                
            
 

            //fis.close();

 

        }  catch (FileNotFoundException e) {

            e.printStackTrace();
            Logger.log(e.getMessage(), true);

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
				  
			 }
		 
		}
		finally	{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), true);
			}
		}

        return pmsMap;

    }
    
    public static HashMap getAdvancedCustomDataFromExcel(String fileName) throws Exception {

    	Logger.log(" --- in getAdvancedCustomDataFromExcel, fileName is " + fileName, true);
    	ReadPropertyFile readProperty = new ReadPropertyFile(); 
  		HashMap<String, String> propertyMap = new HashMap<String, String>();
  		try {
  			propertyMap = readProperty.getPropValues();
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
				  
			 }
		 
		}
  		String filePath = propertyMap.get("ftp_localdirectory");
    	
    	HashMap<String, List<RadiaPlacementMonthlyScheduleDTO>> pmsMap = new HashMap<String, List<RadiaPlacementMonthlyScheduleDTO>>();
    	
    	HashMap<String, RadiaAdvancedColumnDTO> advancedColumnMap = new HashMap<String, RadiaAdvancedColumnDTO>();
    	
    	HashMap<String, KeyValueDTO> columnValueMap = new HashMap<String, KeyValueDTO>();
    	
    	
    	List nameValueList = new ArrayList();

        FileInputStream fis = null;
        
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\494_Radia_Placements_Monthly_635858628353483617.csv"; //csv file address
        //String csvFileAddress = "C:\\eclipse-kepler\\workspace\\RadiaNightlyDataTransfer\\radiafile\\"+ fileName; //csv file address
        String csvFileAddress = filePath + fileName;   

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileAddress), "UTF-16"));

        try {

            
        	Map<String,Integer> headingMap = new HashMap<String,Integer>();
        	headingMap.put("CampaignId", 0);
        	headingMap.put("CampaignPublicId", 1);
        	headingMap.put("PackageType", 2);
        	headingMap.put("PackageId", 3);
        	headingMap.put("PlacementId", 4);
        	headingMap.put("PlacementNumber", 5);
        	headingMap.put("PrimaryPlacement", 6);
        	headingMap.put("CustomColumnName", 7);
        	headingMap.put("CustomColumnValue", 8);
        	headingMap.put("InsertDatetime", 9);
        	headingMap.put("UpdateDatetime", 11);
        	 
        	
        	//fis = new FileInputStream(FILE_PATH + fileName);
			List<String> lines = new ArrayList<>();
			String line = null;
			Map<Integer,String> lineMap = new HashMap<Integer,String>();
			int sellLineCounter = 0;
			
			String tempRadiaId = "";
			RadiaAdvancedColumnDTO radiaAdvancedColumnDto = null;
			List<KeyValueDTO> customColumnList = new ArrayList<KeyValueDTO>();
			Map<String, List<KeyValueDTO>> customColumnMap = new HashMap<String, List<KeyValueDTO>>();
			int k=1;
			while ((line = reader.readLine()) != null) 
			{
				
				if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			    {
				lines.add(line);
			    lineMap.put(k, line);
			    //System.out.println("line length is " + line.length());
			    //Logger.log(line, true);
			    
			    if (k>=2)
			    {
			    	
			    	//RadiaAdvancedColumnDTO radiaAdvancedColumnDto = new RadiaAdvancedColumnDTO();
			    	KeyValueDTO keyValueDto = new KeyValueDTO();
			    	String[] tokens = lineMap.get(k).split("\t",-1);
			    	
			    	advancedFileNotEmpty = true;
			    	
			    	//*********************************************************************
                    //SFG-3750
                    java.util.Date date = new java.util.Date();
         		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String dateFormatTwoDigits =  sdf.format(date);
                    CharSequence todayDateTwoDigits = dateFormatTwoDigits; 
                    
                    SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yyyy");
                    String dateFormatOneDigit =  sdf1.format(date);
                    CharSequence todayDateOneDigit = dateFormatOneDigit; 
                    
                    String val = tokens[headingMap.get("UpdateDatetime")];
                    String updateDateTime = val;
                    val = tokens[headingMap.get("InsertDatetime")];
                    String insertDateTime = val;
                    
                    
                    //Logger.log("today's date two digits is " + todayDateTwoDigits, true);
                    //Logger.log("today's date one digit is " + todayDateOneDigit, true);
                    //Logger.log("UpdateDatetime is " + updateDateTime, true);
                    //Logger.log("InsertDateTime is " + insertDateTime, true);
                    if (updateDateTime.contains(todayDateTwoDigits) || updateDateTime.contains(todayDateOneDigit) || insertDateTime.contains(todayDateTwoDigits) || insertDateTime.contains(todayDateOneDigit))
                    {
                    	//Logger.log("UpdateDateTime or InsertDateTime contains today's date", true);
                    	advancedFileHasNewUpdate = true;
                    }
                    
                    //*********************************************************************
			    	
			    	
			    	String radiaId = ""; 
                    radiaId = tokens[headingMap.get("PlacementNumber")];
                    keyValueDto.setRadiaId(radiaId);
                    
                    String columnName = "";
                    columnName = tokens[headingMap.get("CustomColumnName")];
                    keyValueDto.setKey(columnName);
                    
                    String columnValue = "";
                    columnValue = tokens[headingMap.get("CustomColumnValue")];
                    keyValueDto.setValue(columnValue);
                    
                    customColumnList.add(keyValueDto);
                    /*
                    if (!tempRadiaId.equals(radiaId))
                    {
                    	tempRadiaId = radiaId;
                    	radiaAdvancedColumnDto = new RadiaAdvancedColumnDTO(); 
                    	advancedColumnMap.put(tempRadiaId, radiaAdvancedColumnDto);
                    }
                    
                     
                     
                    if (columnName.equalsIgnoreCase("AD LABS"))
                    {
                    	radiaAdvancedColumnDto.setAdLabs(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("AGENCY BILLING PLATFORM"))
                    {
                    	radiaAdvancedColumnDto.setAgencyBillingPlatform(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("CHANNEL"))
                    {
                    	radiaAdvancedColumnDto.setChannel(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("CREATIVE DETAIL"))
                    {
                    	radiaAdvancedColumnDto.setCreativeDetail(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("CREATIVE FORMAT"))
                    {
                    	radiaAdvancedColumnDto.setCreativeFormat(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("DATA/TACTIC"))
                    {
                    	radiaAdvancedColumnDto.setDataTactic(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("DEVICE"))
                    {
                    	radiaAdvancedColumnDto.setDevice(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("INVENTORY"))
                    {
                    	radiaAdvancedColumnDto.setInventory(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("NETWORK BILLING PLATFORM"))
                    {
                    	radiaAdvancedColumnDto.setNetworkBillingPlatform(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("PRIMARY KPI"))
                    {
                    	radiaAdvancedColumnDto.setPrimaryKpi(columnValue);
                    }
                    else if (columnName.equalsIgnoreCase("PRODUCT CODE"))
                    {
                    	radiaAdvancedColumnDto.setProductCode(columnValue);
                    }
                     */
                    //advancedColumnMap.put(radiaAdvancedColumnDto.getRadiaId(), radiaAdvancedColumnDto);
                	
			    }//if (k>=2)
			    k++;
			    }//if (!line.equals(" ") && line != null && !line.isEmpty() && line.length() > 1)
			}//while loop	
        	 
			for (int j= 0; j< customColumnList.size(); j++)
			{
				if (customColumnMap.get(((KeyValueDTO) customColumnList.get(j)).getRadiaId()) != null)
				{
					customColumnMap.get(((KeyValueDTO) customColumnList.get(j)).getRadiaId()).add((KeyValueDTO)customColumnList.get(j));
				}
				else
				{
					List<KeyValueDTO> listCC = new ArrayList<KeyValueDTO>();
					listCC.add((KeyValueDTO)customColumnList.get(j));
					customColumnMap.put(((KeyValueDTO) customColumnList.get(j)).getRadiaId(), listCC);
				}
				
			}
			
			String columnName = "";
			String columnValue = "";
			
			for (String radiaId : customColumnMap.keySet())
			  {
			       
			     
					if (!tempRadiaId.equals(radiaId))
					{
						tempRadiaId = radiaId;
						radiaAdvancedColumnDto = new RadiaAdvancedColumnDTO(); 
						advancedColumnMap.put(tempRadiaId, radiaAdvancedColumnDto);
					}
					
					for (int m = 0; m < customColumnMap.get(radiaId).size(); m++)
					{
						
					columnName = ((KeyValueDTO) customColumnMap.get(radiaId).get(m)).getKey();	
					columnValue = ((KeyValueDTO) customColumnMap.get(radiaId).get(m)).getValue();
					
			  		if (columnName.equalsIgnoreCase("AD LABS"))
			  		{
			  			radiaAdvancedColumnDto.setAdLabs(columnValue);
			  		} 
			  		else if (columnName.equalsIgnoreCase("AGENCY BILLING PLATFORM"))
	                {
	                    radiaAdvancedColumnDto.setAgencyBillingPlatform(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("CHANNEL"))
	                {
	                    radiaAdvancedColumnDto.setChannel(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("CREATIVE DETAIL"))
	                {
	                    radiaAdvancedColumnDto.setCreativeDetail(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("CREATIVE FORMAT"))
	                {
	                    radiaAdvancedColumnDto.setCreativeFormat(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("DATA/TACTIC"))
	                {
	                    radiaAdvancedColumnDto.setDataTactic(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("DEVICE"))
	                {
	                    radiaAdvancedColumnDto.setDevice(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("INVENTORY"))
	                {
	                    radiaAdvancedColumnDto.setInventory(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("NETWORK BILLING PLATFORM"))
	                {
	                    radiaAdvancedColumnDto.setNetworkBillingPlatform(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("PRIMARY KPI"))
	                {
	                    radiaAdvancedColumnDto.setPrimaryKpi(columnValue);
	                }
	                else if (columnName.equalsIgnoreCase("PRODUCT CODE"))
	                {
	                    radiaAdvancedColumnDto.setProductCode(columnValue);
	                }
					}
			 
			  }
			 
			
			/*
			for (int j = 0; j< pmsList.size(); j++)
             {
                 if(pmsMap.get(((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber()) != null)
                 {
                 	pmsMap.get(((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber()).add((RadiaPlacementMonthlyScheduleDTO)pmsList.get(j));
                 	//Logger.log("In if statement, ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() is " + ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() + " -- (RadiaPlacementMonthlyScheduleDTO)pmsList.get(j) is " + (RadiaPlacementMonthlyScheduleDTO)pmsList.get(j), true); 
                      
                     
                 }else
                 {
                     List<RadiaPlacementMonthlyScheduleDTO> listPMS = new ArrayList<RadiaPlacementMonthlyScheduleDTO>();
                     listPMS.add((RadiaPlacementMonthlyScheduleDTO)pmsList.get(j));
                     pmsMap.put(((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber(), listPMS);
                     //Logger.log("In else statement, ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() is " + ((RadiaPlacementMonthlyScheduleDTO) pmsList.get(j)).getPlacementNumber() + " -- listPMS is " + listPMS, true); 
                     
                 
                 }
             }
             */
			
        }  catch (FileNotFoundException e) {

            e.printStackTrace();
            Logger.log(e.getMessage(), true);

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
				  
			 }
		 
		}
		finally	{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), true);
			}
		}

        return advancedColumnMap;

    }
    
    public static String dateStringFormat(String dateStr)
    {
    	String returnStr = "";
    	if (dateStr != null && !dateStr.equals(""))
    	{
    		String[] tokens = dateStr.split("/");
    		//System.out.println("tokens length is " + tokens.length);
    		if (tokens.length == 3)
    		{
    			String month = tokens[0].trim();
    			String day = tokens[1].trim();
    			String year = tokens[2].trim();
    			if (day.length()==1)
    				day = "0" + day;
    			if (month.length() == 1)
    				month = "0" + month;
    			returnStr = year + "-" + month + "-" + day;
    			
    		}
    	}
    	return returnStr;
    }
    
    public static String dateStringFormatForScheduleName(String dateStr)
    {
    	String returnStr = "";
    	if (dateStr != null && !dateStr.equals(""))
    	{
    		String[] tokens = dateStr.split("/");
    		//System.out.println("tokens length is " + tokens.length);
    		if (tokens.length == 3)
    		{
    			String month = tokens[0].trim();
    			String day = tokens[1].trim();
    			String year = tokens[2].trim();
    			if (day.length()==1)
    				day = "0" + day;
    			if (month.length() == 1)
    				month = "0" + month;
    			returnStr = year + "-" + month;
    			
    		}
    	}
    	return returnStr;
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
