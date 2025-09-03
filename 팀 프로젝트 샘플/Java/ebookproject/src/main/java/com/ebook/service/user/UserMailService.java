package com.ebook.service.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;


@Log4j2
@Service
public class UserMailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendTemporaryPassword(String temporaryPassword, String email) {
        log.info("userEmailService : " + email + temporaryPassword);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("임시 비밀번호를 발송해 드립니다");
            helper.setText("임시 비밀번호는 : " + temporaryPassword + " 입니다.");
            helper.setFrom("67klgg@naver.com");
        } catch (MessagingException e) {
            System.out.println("메세지 보내기 실패");
            // handle exception properly if needed
        }
        mailSender.send(message);
    }

}
