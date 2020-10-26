package com.example.SimpleEmailSenderUsingSpringBoot;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.InternetAddress;

import com.sendgrid.smtpapi.SMTPAPI;

public class JavaMailTextExample {
    // Setting Information
    static final String USERNAME ="*******";
    static final String PASSWORD ="*******";

    //static final String PASSWORD = System.getenv("");

    static final String TOS[]    = {"prasadmanoj09@gmail.com"};
    static final String NAMES[]  = {"Manoj"};
    //static final String FROM     = System.getenv("FROM");
    static final String FROM     = "manojrules123@protonmail.com";
    static final String CHARSET  = "ISO-2022-JP"; // "UTF-8";
    static final String ENCODE   = "7bit"; // "base64"; // "quoted-printable";

    public static void main(String[] args)
            throws MessagingException, UnsupportedEncodingException {
        // SMTP Connection Information
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);
        mailSession.setDebug(true); // console log for debug

        // Message construction
        MimeMessage message = new MimeMessage(mailSession);

        // Dummy destination (X-SMTPAPI destination takes precedence)
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(FROM));

        // From
        message.setFrom(FROM);

        // Subject
        message.setSubject("Hello SendGrid", CHARSET);

        // Body
        String body = "Hello、nameMr\r\nWelcome to the world of text mail!";
        message.setText(body, CHARSET, "plain");
        message.setHeader("Content-Transfer-Encoding", ENCODE);

        // X-SMTPAPI header
        String smtpapi = createSmtpapi(TOS, NAMES);
        smtpapi = MimeUtility.encodeText(smtpapi);
        message.setHeader("X-SMTPAPI", MimeUtility.fold(76, smtpapi));

        // Send
        mailSession.getTransport().send(message);
    }

    // Generate value to set in X-SMTPAPI header
    private static String createSmtpapi(String[] tos, String[] names) {
        SMTPAPI smtpapi = new SMTPAPI();
        smtpapi.setTos(tos);
        smtpapi.addSubstitutions("name", names);
        smtpapi.addCategory("category1");
        return smtpapi.rawJsonString();
    }

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(USERNAME, PASSWORD);
        }
    }
}