package com.ebook.controller;

import com.ebook.dto.BookChapterDTO;
import com.ebook.dto.BookDTO;
import com.ebook.dto.user.CashChargeDTO;
import com.ebook.dto.user.UserDTO;
import com.ebook.service.user.UserMailService;
import com.ebook.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMailService userMailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public void login() {}

    @GetMapping("/register")
    public void get_register(){}

    @PostMapping("/register")
    public String post_register(
            @RequestParam String impUid,
            @Validated UserDTO userDTO,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ){
        log.info("impUid : " + impUid);
        log.info("userDTO : " + userDTO);
        if(bindingResult.hasErrors()){
            log.info("바인딩 에러");
            return "redirect:/user/register";
        }
        Object idCheck = session.getAttribute("idCheck");
        if(Objects.nonNull(idCheck)) {
            System.out.println("id 체크를 하고 옴");
            System.out.println(idCheck);
            System.out.println(idCheck.equals(true));
            if(idCheck.equals(true)) {
                System.out.println("id 체크 했는데 중복이 아니였음");
                session.removeAttribute("idCheck");
                userService.create_user(impUid, userDTO);
                return "redirect:/user/login";
            }
            System.out.println("id 체크 했는데 중복이였음");
        }
        redirectAttributes.addFlashAttribute("certErrorMsg", "본인인증이 완료되지 않았습니다");
        return "redirect:/user/register";

    }

    @GetMapping("/findId")
    public void get_findId(){}

    @PostMapping("/findId")
    public String post_findId(
            @RequestParam("userName") String name,
            @RequestParam("userEmail") String email,
            RedirectAttributes redirectAttributes
    ){
        String id = userService.findUserId(name, email);
        redirectAttributes.addFlashAttribute("id", id);
        return "redirect:/user/findId";
    }

    @GetMapping("/resetPassword")
    public void get_resetPassword(){}


    @PostMapping("/resetPassword")
    public String post_resetPassword(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String userEmail
    ){
        Random rand = new Random();
        String temporaryPassword = "" + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10);

        String EncodingPassword = passwordEncoder.encode(temporaryPassword);
        System.out.println(EncodingPassword);
        userService.resetPassword(userId, userName, userEmail, EncodingPassword);
        userMailService.sendTemporaryPassword(temporaryPassword, userEmail);

        return "redirect:/user/login";

    }

    @GetMapping("/cashcharge")
    public String get_cashcharge() {
        return "cashcharge";
    }

    @ResponseBody
    @PostMapping("/cashcharge")
    public ResponseEntity<Void> get_cashcharge(
            @AuthenticationPrincipal UserDTO user,
            @RequestBody CashChargeDTO cashCharge
    ) {
        if(Objects.isNull(user)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        userService.charge_cash(user, cashCharge);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    // 유저가 산 책의 정보를 book_order db에 저장함
    @ResponseBody
    @PostMapping("/chapter/{chapterNo}")
    public ResponseEntity<Void> post_buy_book(
            @PathVariable("chapterNo") Integer chapterNo,
            @AuthenticationPrincipal UserDTO user
    ) {
        // 유저 정보가 조회되지 않았다면(로그인 하지 않았다면) 로그인 후 이용가능합니다 메시지 띄우고 로그인 창으로 보냄.
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            // 유저 정보가 조회가 되었을 시.
            // 1. chapterNo에 해당하는 chapter의 가격을 조회한다
            Integer bookChaptersPrice = userService.select_chapters_price(chapterNo);
            // 2. user의 cash가 챕터 가격보다 큰 지 검사한다
            Integer usersCash = user.getUserCash();
            if (usersCash >= bookChaptersPrice) {
                // 3. 1번과 2번이 전부 ok라면 insert (구매) 시킨다
                userService.user_buy_book(chapterNo, user, bookChaptersPrice);
                // 구매에 성공을 했다면 현재 유저가 가지고 있는 캐시에 챕터 가격만큼 차감한다.
                userService.buyResultCash(user, bookChaptersPrice);
                return ResponseEntity.status(HttpStatus.CREATED).body(null);

            } else {
                // 가지고 있는 캐시가 부족하다면 캐시 충전 페이지로 이동시킨다.
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        }
    }
    /********************************* 마이페이지 ********************************/
    @GetMapping("/mypage")
    public String get_mypage_bookLike(
            @RequestParam(value = "list", defaultValue = "like") String list,
            @AuthenticationPrincipal UserDTO user,
            Model model
    ){
        if(list.equals("like")) {
            System.out.println("좋아요 조회");
            List<BookDTO> books = userService.getAllUserLikeBook(user);
            model.addAttribute("likeBooks", books);
        }else if(list.equals("buy")){
            System.out.println("구매목록 조회");
            List<BookDTO> books = userService.getAllUserBoughtBook(user);
            System.out.println(books);
            for(int i = 0; i < books.size(); i++) {
                System.out.println(books.get(i).getBookChapters());
            }
            model.addAttribute("buyBooks", books);
        }
        model.addAttribute("query", list);
        return "user/mypage";
    }













}
