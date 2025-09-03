package com.ebook.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Integer bookNo; // 책의 번호
    private String bookTitle; // 책의 제목
    private String bookGenre; // 책의 장르
    private String bookAuthor; // 책의 작가
    private String bookPublisher; // 출판사
    private String bookCategory; // 카테고리
    private String bookDescription; // 설정  // text
    private FileDTO bookImage; // 책 이미지 // longblob
    private int bookLikeCount; // 책 좋아요 수 // like_count
    private Boolean isLiked; // 유저의 책 좋아요 여부
    private List<BookChapterDTO> bookChapters;
    @JsonAlias("cash_amount")
    private Integer bookPrice;
}
