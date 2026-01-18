package com.example.basicauth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender javaMailSender;

    @Value("${mail}")
    private String from;
    public void sendMail(String to, String subject, String content) {
        log.info("Mail is send to {} with subject {} and content: {}", to, subject, content);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
        log.info("Mail sent successfully");
    }


//    public void sendHtml(String to, String subject, String html) {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//
//            helper.setFrom(from);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(html, true);
//
//            javaMailSender.send(mimeMessage);
//            log.info("HTML mail sent successfully");
//        } catch (Exception e) {
//            log.error("Mail sending failed", e);
//
//        }
//    }

}
