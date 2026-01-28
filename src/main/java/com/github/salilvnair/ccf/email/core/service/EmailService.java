package com.github.salilvnair.ccf.email.core.service;

import com.github.salilvnair.ccf.email.core.context.EmailDataContext;
import com.github.salilvnair.ccf.email.core.type.EmailGeneratorType;
import com.github.salilvnair.ccf.util.log.StackTraceFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.Map;
import java.util.Properties;

public interface EmailService {
    String smtpHost();
    String smtpPort();
    String smtpUsername();
    String smtpPassword();

    EmailDataContext execute(EmailGeneratorType generatorType, EmailDataContext emailDataContext, Map<String, Object> inputParams) throws Exception;

    default boolean send(EmailDataContext dataContext) throws MessagingException {
        Logger logger = LoggerFactory.getLogger(EmailService.class);
        StringBuilder builder = new StringBuilder();
        String smtpHost = smtpHost();
        String smtpPort = smtpPort();
        String smtpUsername = smtpUsername();
        String smtpPassword = smtpPassword();
        builder
                .append("|templateId:")
                .append(dataContext.getTemplateId())
                .append("|smtpHost:")
                .append(smtpHost)
                .append("|smtpPort:")
                .append(smtpPort)
                .append("|subject:")
                .append(dataContext.getSubject())
                .append("|from:")
                .append(smtpUsername)
                .append("|to:")
                .append(dataContext.getTo())
                .append("|cc:")
                .append(dataContext.getCc())
                .append("|bcc:")
                .append(dataContext.getBcc())
                .append("|hasAttachments:")
                .append(!CollectionUtils.isEmpty(dataContext.getAttachments()))
                .append("|");

        Session session = initSecuredSession(smtpHost, smtpPort, smtpUsername, smtpPassword);

        String logString = builder.toString();

        logger.info("EmailService >> send >> [{}]", logString);

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(smtpUsername));

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dataContext.getTo()));

        if(dataContext.getCc() != null) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(dataContext.getCc()));
        }

        message.setSubject(dataContext.getSubject());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(dataContext.getBody(), "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        if(!CollectionUtils.isEmpty(dataContext.getAttachments())) {
            for (MimeBodyPart attachment : dataContext.getAttachments()) {
                multipart.addBodyPart(attachment);
            }
        }
        message.setContent(multipart);
        return send(message);
    }

    static Session initSecuredSession(String smtpHost, String smtpPort, String smtpUsername, String smtpPassword) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.port", smtpPort);
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        };
        return Session.getDefaultInstance(prop, auth);
    }

    default boolean send(Message message) {
        try {
            Transport.send(message);
            return true;
        }
        catch (MessagingException e) {
            StackTraceFrame stackTraceFrame = new StackTraceFrame(e);
            stackTraceFrame.printStackTrace();
            return false;
        }
    }

    static MimeBodyPart makeAttachment(byte[] content, String contentType, String name) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(content, contentType);
        mimeBodyPart.setFileName(name);
        return mimeBodyPart;
    }
}
