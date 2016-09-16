package misc;

import java.util.*;
import java.io.*;
import java.text.*;

public class Logger {
    
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
    private static String logFile = "mainLog.txt";
    //private static String tmpLogFile;
    private static String tmpLogFile = "tempLog.txt";//added on 06/20/06
    private static String techOpsFile = "techOps.txt";
    private static String successLogFile = "successLog.txt";
    private static String errorLogFile = "errorLog.txt";
    private static String exceptionLogFile = "exceptionLog.txt";
     
    public static void setLogFileName(String s) {
        Logger.logFile=s;
    }

    public static void setTmpLogFileName(String s) {
        Logger.tmpLogFile=s;
    }
    public static String getTmpLogFileName(){
        return Logger.tmpLogFile;
    }
    
    public static void setTechOptLogFileName(String s) {
        Logger.techOpsFile=s;
    }
    public static String getTechOptLogFileName() {
        return Logger.techOpsFile;
    }
    
    public static void setSuccessLogFileName(String s) {
        Logger.successLogFile=s;
    }
    public static String getSuccessLogFileName(){
        return Logger.successLogFile;
    }
    
    public static void setErrorLogFileName(String s) {
        Logger.errorLogFile=s;
    }
    public static String getErrorLogFileName(){
        return Logger.errorLogFile;
    }
    
    public static void setExceptionLogFileName(String s) {
        Logger.exceptionLogFile=s;
    }
    public static String getExceptionLogFileName(){
        return Logger.exceptionLogFile;
    }
    
   
    public static void log(String logMessageIn, boolean addTemp) {
        
        String timeStamp = sdf.format(new Date());
        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(Logger.logFile,true)),true);
            out.println(timeStamp + " ; " + logMessageIn);
            out.close();
        } catch (IOException io) {}
        
        if (addTemp){
            tmpFileLog(logMessageIn);
        }
    }
    public static void tmpFileLog(String logMessageIn) {
        
        String timeStamp = sdf.format(new Date());
        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(Logger.tmpLogFile,true)),true);
            out.println(timeStamp + " ; " + logMessageIn);
            out.close();
        } catch (IOException io) {}
        
    }
    
    public static void log(String logMessageIn, Throwable exp) {
        if ( exp != null ) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exp.printStackTrace(pw);
            logMessageIn = logMessageIn + "\n" + sw.toString();
            String timeStamp = sdf.format(new Date());
            try {
                PrintWriter out
                = new PrintWriter(new BufferedWriter(new FileWriter(Logger.logFile,true)),true);
                out.println(timeStamp + " ; " + logMessageIn);
                out.close();
            } catch (IOException io) {}
            
            
        }
        
    }
    
    public static void stdOutLog(String logMessageIn) {
        String timeStamp = sdf.format(new Date());
        System.out.println(timeStamp + " ; " + logMessageIn);
    }
    
    public static void deleteTmpLogFile(String filename){
        File delFile = new File(filename);
        if (delFile.exists())
            delFile.delete();
    }
    
    public static void logTechOps(String logMessageIn, boolean addTemp) {
        
        String timeStamp = sdf.format(new Date());
        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(Logger.techOpsFile,true)),true);
            out.println(timeStamp + " ; " + logMessageIn);
            out.close();
        } catch (IOException io) {}
        /*
        if (addTemp){
            tmpFileLog(logMessageIn);
        }*/
    }
    
    
    public static void logSuccess(String logMessageIn, boolean addTemp) {
        
        String timeStamp = sdf.format(new Date());
        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(Logger.successLogFile,true)),true);
            out.println(timeStamp + " ; " + logMessageIn);
            out.close();
        } catch (IOException io) {}
        /*
        if (addTemp){
            tmpFileLog(logMessageIn);
        }*/
    }
    
    public static void logError(String logMessageIn, boolean addTemp) {
        
        String timeStamp = sdf.format(new Date());
        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(Logger.errorLogFile,true)),true);
            out.println(timeStamp + " ; " + logMessageIn);
            out.close();
        } catch (IOException io) {}
        /*
        if (addTemp){
            tmpFileLog(logMessageIn);
        }*/
    }
    
    public static void logException(String logMessageIn, boolean addTemp) {
        
        String timeStamp = sdf.format(new Date());        try {
            PrintWriter out
            = new PrintWriter(new BufferedWriter(new FileWriter(Logger.exceptionLogFile,true)),true);
            out.println(timeStamp + " ; " + logMessageIn);
            out.close();
        } catch (IOException io) {}
        /*
        if (addTemp){
            tmpFileLog(logMessageIn);
        }*/
    }
    
}






