//package com.ebook.controller;
//
//import com.ebook.service.BookService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.List;
//
//@RestController
//public class BookRestController {
//    @Autowired
//    BookService bookService;
//
//    // 검색바에서 검색 내용에 따른 책 리스트들을 보여줌.
//    @GetMapping("/searchbook/result")
//    public List<String> search_book_result(
//            @RequestParam("bookTitle") String bookTitle
//    ){
//        List<String> bookTitles = bookService.searchBooksTitle(bookTitle);
//        return bookTitles;
//    }
//
//}
