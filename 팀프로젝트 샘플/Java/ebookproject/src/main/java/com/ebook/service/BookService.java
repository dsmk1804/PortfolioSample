package com.ebook.service;

import com.ebook.dto.BookChapterDTO;
import com.ebook.dto.BookDTO;
import com.ebook.dto.user.UserDTO;
import com.ebook.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {
    @Autowired private BookMapper bookMapper;

    // 책의 모든 정보를 가져옵니다.
    public List<BookDTO> findingAllBooks(){
        return bookMapper.findAllBooks();
    }

    public BookDTO get_book(Integer bookNo, UserDTO user, String sort){
        if(Objects.isNull(user)){
            return bookMapper.select_book_by_no(bookNo, null, sort);
        }else {
            return bookMapper.select_book_by_no(bookNo, user.getUserId(), sort);
        }
    }

    public BookDTO getBookChapters(Integer bookNo, Integer bookChapterNo){
        return bookMapper.selectBookByChapters(bookNo, bookChapterNo);
    }

    public List<BookChapterDTO>  getBookAllChapters(Integer bookNo){
        return bookMapper.selectBookAllChapters(bookNo);
    };



//    public List<BookChapterDTO> get_books_chapters(Integer bookNo){
//
//        return bookMapper.select_book_chapters_by_book_no(bookNo);
//    }

    public List<BookDTO> searchBooksByTitle(String search){
        return bookMapper.findAllBooksByTitle(search);
    }

//    // 검색창의 책 보여주기.
//    public List<String> searchBooksTitle(String search){
//        return bookMapper.findBooksTitle(search);
//    }

    // 책의 구매 여부 판단
    // 특정 유저가 특정 챕터를 구매했는지 여부를 반환
    public Boolean isChapterBought(String userId, Integer no) {
        // 책의 구매 여부를 확인
        List<BookChapterDTO> boughtChapters = bookMapper.findUserIdWithBoughtBook(userId, no);

        // 구매한 챕터 번호와 요청된 챕터 번호를 비교
        for (BookChapterDTO chapter : boughtChapters) {
            if (Objects.equals(chapter.getNo(), no)) {
                return true;
            }
        }
        return false;
    }


}
