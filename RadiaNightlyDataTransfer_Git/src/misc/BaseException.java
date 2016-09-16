package misc;

public class BaseException extends java.lang.Exception {
    
    private String errorCode = "";
    private String errorMessage = "";
    private Throwable rootCause;
    
    
    public BaseException() {}    
    
    public BaseException(Throwable rootCause, String errorCode, String errorMessage) 
    {
        super();
        this.rootCause = rootCause;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public BaseException(Throwable rootCause,String errorMessage) 
    {
        super();
        this.rootCause = rootCause;
        this.errorMessage = errorMessage;
    }
    
    public String getErrorCode() 
    {
        return errorCode;
    }
    
    
    public void setErrorCode(String errorCode) 
    {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() 
    {
        return errorMessage;
    }
   
    public void setErrorMessage(String errorMessage) 
    {
        this.errorMessage = errorMessage;
    }
    
     public Throwable getRootCause() 
    {
        return rootCause;
    }
   
    public void setRootCause(Throwable rootCause) 
    {
        this.rootCause = rootCause;
    }
}
