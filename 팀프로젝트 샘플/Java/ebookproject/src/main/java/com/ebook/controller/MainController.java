package com.ebook.controller;

import com.ebook.dto.BookDTO;
import com.ebook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private BookService bookService;


    @GetMapping("/main")
    public String mainPageImage(
            @RequestParam(value = "genre",required = false) String bookGenre,
            Model model
    ) {
        // 책 목록을 전체 가져오기
        List<BookDTO> allBooks = bookService.findingAllBooks();
        // 장르별로 필터링
        allBooks = allBooks.parallelStream().filter(book -> bookGenre == null || book.getBookGenre().equals(bookGenre)).toList();
        // 카테고리 별 그룹화 진행
        Map<String, List<BookDTO>> booksByCategory = allBooks.stream()
                .collect(Collectors.groupingBy(BookDTO::getBookCategory));
        // 그 중에서 5개 랜덤 선택
        List<BookDTO> randomBookByGenre = getRandomBooks(allBooks, 5);
        model.addAttribute("randomBookByGenre", randomBookByGenre);
        model.addAttribute("booksByCategory", booksByCategory);
        return "main";
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
