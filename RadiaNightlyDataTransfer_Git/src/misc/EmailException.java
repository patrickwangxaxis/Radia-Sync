/*
 * Email.java
 *
 * Created on May 19, 2004, 10:52 AM
 */

package misc;

/**
 *
 * @author  cbecker
 */
public class EmailException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>Email</code> without detail message.
     */
    public EmailException() {
    }
    
    
    /**
     * Constructs an instance of <code>Email</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EmailException(String msg) {
        super(msg);
    }
}
