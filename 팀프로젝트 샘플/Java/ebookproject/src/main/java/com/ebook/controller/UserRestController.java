package com.ebook.controller;

import com.ebook.dto.user.UserDTO;
import com.ebook.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/find")
    public ResponseEntity<Boolean> get_findId(
            @RequestParam("id") String id,
            HttpSession session
    ){
        boolean result = userService.findUser(id);
        if(result){
            session.setAttribute("idCheck", true);
        }else {
            session.setAttribute("idCheck", false);
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/save/{bookNo}")
    public ResponseEntity<String> post_book_like(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("bookNo") Integer bookNo
    ) {
        if(Objects.isNull(user)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인을 해주세요");
        }
        System.out.println(user);
        userService.saveBookLike(user, bookNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("마이리스트에 추가가 완료되었습니다");
    }

    @DeleteMapping("/save/{bookNo}")
    public ResponseEntity<String> delete_book_like(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("bookNo") Integer bookNo
    ) {
        if(Objects.isNull(user)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인을 해주세요");
        }
        userService.removeBookLike(user, bookNo);
        return ResponseEntity.status(HttpStatus.OK)
                .body("마이리스트에서 제거하였습니다");
    }











}
