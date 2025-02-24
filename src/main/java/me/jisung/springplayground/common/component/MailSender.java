package me.jisung.springplayground.common.component;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j(topic = "MainSender")
public class MailSender {

    @Value("${smtp_id}")
    private String mailFrom;

    @Value("${smtp_password}")
    private String mailFromPwd;

    @Value("${smtp_host}")
    private String mailSmtpHost;

    @Value("${smtp_port}")
    private int mailSmtpPort;

    @Value("${smtp_ssl_trust}")
    private String mailSmtpSslTrust;

    @Async
    public void sendMail(String to, String subject, String content) {
        this.sendMail(to, new ArrayList<>(), subject, content, new ArrayList<>());
    }

    @Async
    public void sendMail(String to, List<String> ccs, String subject, String content) {
        this.sendMail(to, ccs, subject, content, new ArrayList<>());
    }

    @Async
    public void sendMail(String to, List<String> ccs, String subject, String content, List<MultipartFile> files) {
        Session session = Session.getInstance(getMailProps(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailFrom, mailFromPwd);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));

            /* 수신사 및 참조자 추가 */
            message.addRecipients(Message.RecipientType.TO, to);
            for (String cc : ccs) message.addRecipients(Message.RecipientType.CC, cc);

            /* 메일 제목 설정 */
            message.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html;charset=utf-8");
            multipart.addBodyPart(messageBodyPart);

            /* 메일에 첨부파일 추가 */
            for (MultipartFile file : files) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setFileName(file.getOriginalFilename());
                attachmentPart.setContent(file.getBytes(), file.getContentType());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);
            log.info("메일 전송 완료 - 수신자: {}, 제목: {}", to, subject);
        } catch (MessagingException | IOException e) {
            log.error("메일 전송 실패 - 수신자: {}, 제목: {}, 실패 사유: {}", to, subject, e.getMessage());
        }
    }

    private Properties getMailProps() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host",                  mailSmtpHost);
        prop.put("mail.smtp.port",                  mailSmtpPort);
        prop.put("mail.smtp.ssl.trust",             mailSmtpSslTrust);
        prop.put("mail.smtp.auth",                  "true");
        prop.put("mail.smtp.ssl.enable",            "true");
        prop.put("mail.smtp.starttls.enable",       "true");
        prop.put("mail.smtp.socketFactory.class",   "javax.net.ssl.SSLSocketFactory");
        return prop;
    }
}
