package misc;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public class SendMailSSLEveryone {
	public static synchronized void send(String mailHost, String from, String to, String cc, String bcc,
		    String subject, String body, List<String> attachFile)   {
		
		ReadPropertyFile readProperty = new ReadPropertyFile(); 
		HashMap<String, String> propertyMap = new HashMap<String, String>();
		try {
			propertyMap = readProperty.getPropValues();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("in SendMailSSL, host is " + propertyMap.get("host") + "\n to is "+ propertyMap.get("to"));
		
		//final String username = "patrick.wang@xaxis.com";
        //final String password = "T1ger1027";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.smtp.host", "outlook.office365.com");
        //props.put("mail.smtp.host", "e6smtp1.east6.247realmedia.com");
        props.put("mail.smtp.host", propertyMap.get("host"));
        props.put("mail.smtp.port", propertyMap.get("port"));
        
        to=propertyMap.get("to");
        //props.put("mail.smtp.port", "587");
        //props.put("mail.smtp.port", "25");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("patrick.wang@xaxis.com","Password1");
					//return new PasswordAuthentication("username","password");
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(from));
			 
			message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(to));
			//message.setSubject("hello from patrick");
			message.setSubject(subject);
			//message.setText("Tomorrow is masquerade party," +
			//		"\n\n remember to wear your mask and suit up!");
			
			 // create and fill the first message part
			//String body = "body test";
    	    MimeBodyPart mbp1 = new MimeBodyPart();
    	    mbp1.setText(body);
    	    
    	    /*
    	    // create the second message part
    	    MimeBodyPart mbp2 = new MimeBodyPart();
    	    
    	     
            // attach the file to the message
    	    //String attachFile = "C:\\eclipse\\workspace\\test\\src\\tempLog.txt";
    	    if (!(attachFile == null)){
       	    FileDataSource fds = new FileDataSource(attachFile);
    	    mbp2.setDataHandler(new DataHandler(fds));
    	    mbp2.setFileName(fds.getName());
    	    }

    	    // create the Multipart and its parts to it
    	    Multipart mp = new MimeMultipart();
    	    
    	    // add attachment (if attachFile name provided)
    	    mp.addBodyPart(mbp1);
    	    if (!(attachFile == null))
    	        mp.addBodyPart(mbp2);
			 */
    	    //********************************
    	    
    	    Multipart multipart = new MimeMultipart("mixed");
    	    multipart.addBodyPart(mbp1);
    	    for (String str : attachFile) {
    	        MimeBodyPart messageBodyPart = new MimeBodyPart();
    	        DataSource source = new FileDataSource(str);
    	        messageBodyPart.setDataHandler(new DataHandler(source));
    	        messageBodyPart.setFileName(source.getName());
    	        multipart.addBodyPart(messageBodyPart);
    	    }
    	    
    	    //*********************************
    	    
    	     
    	    // add the Multipart to the message
    	    message.setContent(multipart);

    	    // set the Date: header
    	    message.setSentDate(new Date());
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}