package tools;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
	
public class MailTools {
	public static void sendemail(String email, String password) throws AddressException, MessagingException  {
            String host ="smtp.gmail.com" ;
            String user = "3i017.twister@gmail.com";
            String pass = "Adonis123456";
            String to = email;
            String from = "3i017.twister@gmail.com";
            String subject = "The password of your twister account.";
            String messageText = "Hello, "
            		+ "Here is your password :\n" + 
            		password + "\n\n" +
            		"thank you for your trust,\n" + 
            		"The Twister team.";
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject); msg.setSentDate(new Date());
            msg.setText(messageText);

           Transport transport=mailSession.getTransport("smtp");
           transport.connect(host, user, pass);
           transport.sendMessage(msg, msg.getAllRecipients());
           transport.close();
    }
}

