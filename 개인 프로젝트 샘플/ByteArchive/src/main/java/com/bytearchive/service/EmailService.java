package com.bytearchive.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Log4j2
@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Value("${mail.from}")
    private String FROM;

    // 회원가입 시 유저에게 인증번호가 담긴 메일 발송
    public String send_signup_auth_mail(String userEmail) throws Exception{
        // 인증번호 랜덤 생성
        Random random = new Random();
        // 랜덤 4자리 숫자 생성
        String authNumber = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        // 이메일 메시지 템플렛 생성
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("어서와요 byteArchive에!"); // 메일의 제목 설정
        helper.setText("<b>인증번호:</b> " + authNumber, true); // 메일의 내용 설정, true: html로 전송이 가능
        helper.setTo(userEmail); // 메일을 받는 사람
        helper.setFrom(FROM); // 메일을 보내는 사람

        // 이메일 메시지 발송
        mailSender.send(mimeMessage);
        log.info("인증번호 발송 완료!");

        // 생성된 인증번호 반환
        return authNumber;
    }
}
