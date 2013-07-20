package com.share.trade.common;


import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class SendMail {

	private String from;
	private String to;
	private String subject;
	private String text;
	
	public SendMail(String from, String to, String subject, String text){
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.text = text;
	}
	
	public void send(){ Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    String msgBody = "...";

    try {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from+"@gmail.com", "Sam"));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(to, "w2ms"));
        msg.setSubject(subject);
        msg.setText(text);
        Transport.send(msg);

    } catch (AddressException e) {
        e.printStackTrace();
    } catch (MessagingException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}}