package org.example.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {
    private static final String host = "smtp.gmail.com";
    private static final String port = "587";
    private static final String username = System.getenv().getOrDefault("MAIL_USER", "datntty00327@gmail.com");
    private static final String password = System.getenv().getOrDefault("MAIL_PASSWORD", "vomvdeeuibcrduan");


    public static void sendEmail(String toAddress, String subject, String body) throws MessagingException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new MessagingException("Chưa cấu hình MAIL_USER / MAIL_PASSWORD trong biến môi trường!");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // Timeout 10 giây để tránh web bị treo
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}