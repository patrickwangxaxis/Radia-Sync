package misc;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;

public class CallDatamartURL{
	
	public static void main(String args[]) throws ParseException {
	       System.out.println("calling datamart resutl is " + testURL());
		   
	   }

public static boolean testURL(){	

	boolean result = false;
try {
	String urlCampaignStr = "https://data-feedsqa.groupm.com/API/SavedQuery/92c00835-ddba-4a3d-98e9-3b88fb080613?applicationtype=DataMarketplace_API&useremail=patrick.wang%40xaxis.com&applicationtoken=ea55447a-bd17-404c-8539-87ecba7e72d1";
	URL url = new URL(urlCampaignStr);
	InputStream is = url.openStream();
	
	String urlPlacementDetailStr = "https://data-feedsqa.groupm.com/API/SavedQuery/032584aa-0abd-410c-ba20-5e2129bb919f?applicationtype=DataMarketplace_API&useremail=patrick.wang%40xaxis.com&applicationtoken=ea55447a-bd17-404c-8539-87ecba7e72d1";
	url = new URL(urlPlacementDetailStr);
	is = url.openStream();
	
	String urlPlacementMonthlyScheduleStr = "https://data-feedsqa.groupm.com/API/SavedQuery/cd7fb472-3862-4d46-8601-4181e94cf078?applicationtype=DataMarketplace_API&useremail=patrick.wang%40xaxis.com&applicationtoken=ea55447a-bd17-404c-8539-87ecba7e72d1";
	url = new URL(urlPlacementMonthlyScheduleStr);
	is = url.openStream();
	
	String urlAdvancedPlacementStr = "https://data-feedsqa.groupm.com/API/SavedQuery/7834a8ed-2b96-4b0c-a1da-d5c5b8763dbb?applicationtype=DataMarketplace_API&useremail=patrick.wang%40xaxis.com&applicationtoken=ea55447a-bd17-404c-8539-87ecba7e72d1";
	url = new URL(urlAdvancedPlacementStr);
	is = url.openStream();
	
	
	
    result = true;
} 
catch(Exception e)

{
	result = false;
	e.printStackTrace();	
}

return result;

}
}