package com.ebook.dto;


import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookChapterDTO {
    private Integer no;
    private Integer bookNo;
    private String bookChapterName;
    private String bookUploadDate;
    private Integer bookChapterPrice;
    private Boolean isBought; // 유저의 구매 여부
}
