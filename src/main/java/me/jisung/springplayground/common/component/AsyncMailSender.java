package me.jisung.springplayground.common.component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;



@Component
@RequiredArgsConstructor
@Slf4j(topic = "MainSender")
public class AsyncMailSender {

    private final JavaMailSender javaMailSender;



    @Async
    public void sendMail(String to, String subject, String content) {
        this.sendMail(to, new String[]{}, subject, content, new File[]{});
    }

    @Async
    public void sendMail(String to, String[] cc, String subject, String content, File[] files) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom("noreply@playground.com");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);

            if(cc.length > 0) mimeMessageHelper.setCc(cc);
            for(File file : files) mimeMessageHelper.addAttachment(file.getName(), file);

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully. to: {}, subject: {}", to, subject);
        } catch (MessagingException | MailSendException e) {
            log.error("An error occurred while sending the email.", e);
        }
    }



}
