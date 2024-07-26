package com.ebook.dto;

import com.ebook.dto.user.UserDTO;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookBuyDTO {
    private Integer bookNo;
    private UserDTO userId;

}
