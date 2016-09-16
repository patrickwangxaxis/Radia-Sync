package misc;

import java.io.*;
import java.text.*;
import java.util.Date;

public class LastRun {
    
    
    //private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String logFile = "LastRunDate.txt";
    private static String lastRadiaCampaignFile = "LastRadiaCampaignDate.txt";
    private static String lastRadiaPlacementFile = "LastRadiaPlacementDate.txt";
    private static String lastRadiaMonthlyScheduleFile = "LastRadiaMonthlyScheduleDate.txt";
    //private static String techOptFile = "techOpt.txt";
   
    
    
    public static void setNewDate(Date newDate) {
        String timeStamp = sdf.format(newDate);
        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(LastRun.logFile,false)),true);
            out.println(timeStamp);
            out.close();
        } catch (IOException io) {}
        
    }
    
    public static String  getPreviousDate() {
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(logFile); 
            int n;     
            while ((n = fis.available()) > 0) {
              byte[] b = new byte[n];
              int result = fis.read(b);
              if (result == -1) break;
              String s = new String(b);
              sb.append(s);
            }
           
        }
        catch (IOException e) {System.err.println(e);}
        return sb.toString();
 
    }
    
    
    
    /*  
    public static void setTechOptsLog(String log) {
         
        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(LastRun.techOptFile,false)),true);
            out.println(log);
            out.close();
        } catch (IOException io) {}
        
    }
    */
}






