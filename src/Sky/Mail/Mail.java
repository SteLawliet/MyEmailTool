package Sky.Mail;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;


/**
 * Created by Stelawliet on 17/12/24.
 */

import com.sun.mail.util.MailSSLSocketFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class Mail {
    /**
     * Created by Stelawliet on 17/12/24.
     */

    public static void SendMail(Session session, String sendFrom, String sendTo, String subject, String content, String file) {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sendFrom));
            message.addRecipients(Message.RecipientType.TO, sendTo);
            message.setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
//        message.setContent(content, "text/html;charset=utf-8");
        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart contentPart = new MimeBodyPart();
        try {
            contentPart.setContent(content, "text/html;charset=utf-8");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        try {
            mimeBodyPart.attachFile(
                    new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            String filename = file.substring(file.lastIndexOf("/") + 1);
            mimeBodyPart.setFileName(MimeUtility.encodeText(filename));
            multipart.addBodyPart(contentPart);
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static Session getEmailSession(String username, final String password) {
        final String[] emails = username.split("@");
        Properties properties = new Properties();
        //设置debug
        properties.setProperty("mail.debug", "true");
        //设置发送主机名
        properties.setProperty("mail.host", "smtp." + emails[1]);
        //设置验证
        properties.setProperty("mail.smtp.auth", "true");
        MailSSLSocketFactory ssl = null;
        try {
            ssl = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (ssl != null) {
            ssl.setTrustAllHosts(true);
        }

        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", ssl);
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emails[0], password);
            }
        };
        return Session.getInstance(properties, authenticator);
    }

    public static void main(String[] args) {
        Session s = getEmailSession("572237582@qq.com", "vfhvedmeixzsbbbd");
        SendMail(s, "572237582@qq.com",
                "1909282497@qq.com",
                "mail工具测试",
                "少时诵诗书所",
                "/Volumes/Data/DATA/【备份】/ACGHH-0146-BDFTS.BIN");
    }
}

