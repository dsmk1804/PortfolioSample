package com.ebook.service.user;

import com.ebook.dto.user.UserDTO;
import com.ebook.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {
    @Autowired UserMapper userMapper;

    /******************  마이페이지 사용자 정보  **************/

    // 헤더의 내 정보 가져오기

    public UserDTO getMyPageUser(UserDTO userDTO){
        return userMapper.SelectUserInfo(userDTO);
    }








}