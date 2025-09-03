package com.bytearchive.controller;


import com.bytearchive.dto.UserDTO;
import com.bytearchive.service.EmailService;
import com.bytearchive.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Controller
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    public void get_user_login(){}

    @GetMapping("/signup")
    public void get_user_signup(){}

    @PostMapping("/signup")
    public String post_user_signup(
            HttpSession session,
            @RequestParam("email-auth") String emailAuth,
            UserDTO user
    ){
        log.info("로그인 시도하는 유저:" + user);
        log.info("입력된 이메일 인증 번호:" + emailAuth);
        Object o = session.getAttribute("emailAuthNumber");
        // 세션에 저장된 값이 없다면 ( 이메일 전송 없이 POST 요청했더라 )
        if(Objects.isNull(o)){
            return "redirect:/user/signup";
        }
        // 이메일 인증 번호를 가져와서 사용자의 입력값과 비교
        String emailAuthNumber = o.toString();
        if(!emailAuth.equals(emailAuthNumber)){
            return "redirect:/user/signup";
        }
        // 인증번호가 맞았다면 회원가입 시킨다
        userService.create_user(user);
        return "redirect:/user/login";
    }


    /*********************************************************/
    @GetMapping("/check")
    public ResponseEntity<Void> check_user(
            HttpSession session,
            UserDTO user
    ){
        log.info(user);
        boolean result = userService.check_user_is_duplicated(user);
        // 유저가 중복임
        if(result){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        // 유저를 생성할 수 있음
        return ResponseEntity.ok().body(null);
    }

    // 유저에게 이메일로 인증 번호를 전송함
    @ResponseBody
    @PostMapping("/auth")
    public ResponseEntity<Void> post_email_auth(
            HttpSession session,
            @RequestBody Map<String, String> body
    ){
        String email = body.get("email");
        log.info("email 전송 시도: {}", email);
        try {
            String emailAuthNumber = emailService.send_signup_auth_mail(email);
            session.setAttribute("emailAuthNumber", emailAuthNumber);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error("이메일 발송 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
