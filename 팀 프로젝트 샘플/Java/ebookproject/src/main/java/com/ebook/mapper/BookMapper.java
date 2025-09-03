package com.ebook.mapper;

import com.ebook.dto.BookChapterDTO;
import com.ebook.dto.BookDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
//    // 책의 카테고리를 가져온다.
//    List<BookDTO> findAllBooksByCategory();

    BookDTO select_book_by_no(
            @Param("bookNo") Integer bookNo,
            @Param("userId") String userId,
            @Param("sort") String sort
    );

    List<BookDTO> findAllBooks();

    // 책 문자열 검색한 후, search 화면으로 책들을 보여줌.
    List<BookDTO> findAllBooksByTitle(@Param("search") String search);

//    List<String> findBooksTitle(@Param("search") String search);


    BookDTO selectBookByChapters(Integer bookNo, Integer bookChapterNo);

    List<BookChapterDTO>  selectBookAllChapters(Integer bookNo);

    // 사용자의 챕터 구매 여부를 확인하기 위해 유저의 아이디를 가져옴 (null = 구매x / 있음 = 구매)
    List<BookChapterDTO> findUserIdWithBoughtBook(String userId, Integer no);

}
