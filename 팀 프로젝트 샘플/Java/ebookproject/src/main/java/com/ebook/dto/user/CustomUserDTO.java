package com.ebook.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDTO {
    private String id;
    private String ci;
    private String userId;
    private String name;
    private String email;
    private String nickName;
    private String profileImageUrl;
}
