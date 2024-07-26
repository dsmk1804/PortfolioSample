package com.ebook.controller;

import com.ebook.dto.BookDTO;
import com.ebook.dto.user.UserDTO;
import com.ebook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MyPageController {
    @Autowired UserService userService;

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
            model.addAttribute("books", books);
        }
        model.addAttribute("query", list);
        return "user/mypage";
    }






}
