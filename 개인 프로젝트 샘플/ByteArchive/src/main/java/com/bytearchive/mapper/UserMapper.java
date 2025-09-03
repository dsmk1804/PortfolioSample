package com.bytearchive.mapper;

import com.bytearchive.dto.UserDTO;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 유저 정보로 유저 조회
    UserDTO getUserByUserInfo(UserDTO user);

    // 유저 회원가입
    void insertUser(UserDTO user);

    // 로그인 할 때 유저 아이디를 가져와 대조
    UserDTO getUserById(String id);
}
