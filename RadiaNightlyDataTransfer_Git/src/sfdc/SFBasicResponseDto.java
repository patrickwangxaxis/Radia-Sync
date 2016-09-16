package sfdc;

import java.io.Serializable;
import java.util.List;

public class SFBasicResponseDto<T> implements Serializable {
                private static final long serialVersionUID = 4016401415211707636L;
                private Boolean Success;
    private String Message;
    private String ErrorCode;
    private List<T> Data;
    
                public Boolean getSuccess() {
                                return Success;
                }
                public void setSuccess(Boolean success) {
                                Success = success;
                }
                public String getMessage() {
                                return Message;
                }
                public void setMessage(String message) {
                                Message = message;
                }
                public String getErrorCode() {
                                return ErrorCode;
                }
                public void setErrorCode(String errorCode) {
                                ErrorCode = errorCode;
                }
                public List<T> getData() {
                                return Data;
                }
                public void setData(List<T> data) {
                                Data = data;
                }
                
                
}
