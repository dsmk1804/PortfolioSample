package com.ebook.controller;

import com.ebook.dto.BookChapterDTO;
import com.ebook.dto.BookDTO;
import com.ebook.dto.user.UserDTO;
import com.ebook.mapper.BookMapper;
import com.ebook.service.BookService;
import com.ebook.service.user.MyPageService;
import org.apache.coyote.Response;
import com.ebook.service.user.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class BookController {
    @Autowired private BookMapper bookMapper;
    @Autowired private BookService bookService;
    @Autowired private MyPageService myPageService;


    @GetMapping("/viewerpage/{bookNo}/{chapterNo}")
    public String get_viewerpage(
            @PathVariable("bookNo") Integer bookNo,
            @PathVariable("chapterNo") Integer chapterNo,
            Model model
    ) {
        BookDTO books = bookService.getBookChapters(bookNo, chapterNo);
        List<BookChapterDTO> chapters = bookService.getBookAllChapters(bookNo);
        Integer chapterCount = 0;
        for (BookChapterDTO chapter : chapters) {
            if (Objects.equals(chapter.getNo(), chapterNo)) {
                chapterCount = chapter.getNo();
            }
//            System.out.println(chapter.getNo());
        }
        model.addAttribute("chapterCount", chapterCount);
        model.addAttribute("chapters", chapters);
        model.addAttribute("books", books);
        for (BookChapterDTO chapter : chapters) {
            if (Objects.equals(chapter.getNo(), chapterNo)) {
                return "viewerpage";
            }
        }
        return "redirect:/detail/" + bookNo + "/ASC";
    }



    @GetMapping("/detail/{bookNo}/{sort}")
    public String get_book(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("bookNo") Integer bookNo,
            @PathVariable("sort") String sort,
            Model model
    ) {
        BookDTO book = bookService.get_book(bookNo, user, sort);
        model.addAttribute("book", book);
        return "detail";
    }

    // 유저의 아이디 유무 여부를 가져와 책의 구매 여부를 확인.
    @ResponseBody
    @GetMapping("/chapter/{chapterNo}")
    public Boolean get_isBought(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("chapterNo") Integer no
    ){
        // 유저 아이디를 가져옵니다.
        // 유저 아이디를 조회하지 못했다면 (= null이라면)
        if(user == null){
            return false;// isBought는 펄스
        }
        else{
            String getUserId = user.getUserId();
            return bookService.isChapterBought(getUserId, no);
        }
    }


    // 책을 랜덤으로 뽑아오기 위한 함수 생성
    private List<BookDTO> getRandomBooks(List<BookDTO> books, int i) {
        Random random = new Random();
        return random.ints(0, books.size())
                .distinct() // 중복 제거
                .limit(i)
                .mapToObj(books::get)
                .collect(Collectors.toList());
    }
}
