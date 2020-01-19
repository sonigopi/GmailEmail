package com.hscs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMail {

	Hashtable <String, String> emailHT = new Hashtable <String, String> ();

	 //static Logger logger = Logger.getLogger(SendMail.class.getName());
	static Logger logger = LoggerFactory.getLogger(SendMail.class);
	 
	 
	public static void main(String[] args) {
	
		SendMail sn = new SendMail();
		sn.generateEmailList();
		sn.sendEmails();

	}

	public void generateEmailList() {
		try {
			
			//File myObj = new File(getClass().getClassLoader().getResource("HoustonSCSEmailAddress.txt").getFile());
			File myObj = new File(getClass().getClassLoader().getResource("committee.txt").getFile());
			//File myObj = new File(getClass().getClassLoader().getResource("test.txt").getFile());
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.indexOf("#") != 0) {
					//logger.info("before Data [" +data+"]" );
					if (data.contains("<") || data.contains(">")) {
						data=removeGTandLT(data.trim());
					}
					else {
						data=data.replace(',', ' ').trim();
					}
					//logger.info("Data = [" +data +"]");
					if (data != null && data.trim().length()>4)
					     emailHT.put(data.trim(), data.trim());
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public void sendEmails() {
Enumeration enumeration = emailHT.keys();
		
		while( enumeration.hasMoreElements()) {
			String recipient = (String) enumeration.nextElement();
			logger.info("Sending Email to -----> " + recipient);
			sendEmail(recipient);
			//sendEmailWithAttachment(recipient);
		}
	}
	
	public String removeGTandLT( String data) {
		//logger.info("before Data [" +data+"]" );
		String patternstr = "<.+>";
		Pattern pattern = Pattern.compile(patternstr);
        Matcher matcher = pattern.matcher(data);

        matcher.find();
        int start = matcher.start();
        int end = matcher.end();
        data = data.substring(start+1, end-1);
		//logger.info("after Data = " +data );
		return data ;		
	}

	
	public void sendEmail( String toAddress) {
		// Recipient's email ID needs to be mentioned.
		String to = "sonigopi@yahoo.com";
		
		if (toAddress != null) to = toAddress ;

		// Sender's email ID needs to be mentioned

		String from = "houstonscs@gmail.com";

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(from, "sindhi2020");

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);
	
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("Sad News");
			
			
			String html = "<p>Dear Sindhi Friends,</p>\r\n" + 
					"<p>&nbsp; &nbsp; Please see the announcement from Tulsi and Shalini Kewalramani</p>\r\n" + 
					"<p>&nbsp;</p>\r\n" + 
					"<p style=\"padding-left: 90px;\">In Loving Memory of my late younger brother Mr. Pishu Kewalramani who left us for his heavenly adobe on January 10, 2020 in NYC, we are holding Kirtan followed by Langar on <strong>January 26<sup>th</sup>, 2020</strong> at 7 PM at Gurdwara Sahib of Southwest Houston.</p>\r\n" + 
					"<p style=\"padding-left: 90px;\">We would be very happy for you to attend with your family.</p>\r\n" + 
					"<p style=\"padding-left: 90px;\">Date:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Sunday, January 26, 2020 <br />Time:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 7:00 PM<br />Where:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Gurdwara Sahib of Southwest Houston (GSSWH) <br />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;14811 Lindita Drive <br />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Houston, TX 77083</p>\r\n" + 
					"<p style=\"padding-left: 90px;\">&nbsp;</p>\r\n" + 
					"<p style=\"padding-left: 90px;\">Thank You, <br /><em>Tulsi and Shalini Kewalramani</em></p>";
			//3) create MimeBodyPart object and set your message text     
		    BodyPart messageBodyPart1 = new MimeBodyPart();  
		    messageBodyPart1.setContent(html,"text/html; charset=utf-8" );
		    //messageBodyPart1.setText("This is message body"); 
		    
		    //4) create new MimeBodyPart object and set DataHandler object to this object      
		    //MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		    
		    //String filename = "SendAttachment.java";//change accordingly
		    //String filename = "C:\\Users\\sonig_000\\Desktop\\chetichand\\EmailDraft.pdf";
		    //String attachedName = "EmailDraft.pdf";
		   // DataSource source = new FileDataSource(filename);  
		   // messageBodyPart2.setDataHandler(new DataHandler(source));  
		    //messageBodyPart2.setFileName(attachedName);  
		    

		    //5) create Multipart object and add MimeBodyPart objects to this object      
		    Multipart multipart = new MimeMultipart();  
		    multipart.addBodyPart(messageBodyPart1);  
		    //multipart.addBodyPart(messageBodyPart2);  
		    
		    //6) set the multiplart object to the message object  
		    message.setContent(multipart );  
		    
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public void sendEmailWithAttachment( String toAddress) {
		// Recipient's email ID needs to be mentioned.
		String to = "sonigopi@yahoo.com";
		
		if (toAddress != null) to = toAddress ;

		// Sender's email ID needs to be mentioned

		String from = "houstonscs@gmail.com";

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(from, "sindhi2020");

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);
		
		
	
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");
			
			
			String html = "Body Text";
			//3) create MimeBodyPart object and set your message text     
		    BodyPart messageBodyPart1 = new MimeBodyPart();  
		    messageBodyPart1.setContent(html,"text/html; charset=utf-8" );
		    //messageBodyPart1.setText("This is message body"); 
		    
		    //4) create new MimeBodyPart object and set DataHandler object to this object      
		    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		    
		    //String filename = "SendAttachment.java";//change accordingly
		    String filename = "F";
		    String attachedName = "EmailDraft.pdf";
		    DataSource source = new FileDataSource(filename);  
		    messageBodyPart2.setDataHandler(new DataHandler(source));  
		    messageBodyPart2.setFileName(attachedName);  
		    

		    //5) create Multipart object and add MimeBodyPart objects to this object      
		    Multipart multipart = new MimeMultipart();  
		    multipart.addBodyPart(messageBodyPart1);  
		    multipart.addBodyPart(messageBodyPart2);  
		    
		    //6) set the multiplart object to the message object  
		    message.setContent(multipart );  
		    
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

}