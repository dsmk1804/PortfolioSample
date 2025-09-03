package com.bytearchive.service;

import com.bytearchive.dto.UserDTO;
import com.bytearchive.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    // 로그인 페이지에서 로그인 버튼 누르면 loadUserByUsername 가 최우선 자동 실행됨

    // 여기서 로그인 로직 구현 (username => 화면에서 입력한 id 값이 옴)
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDTO findUser = userMapper.getUserById(id);

        if (Objects.isNull(findUser)) {
            throw new UsernameNotFoundException("Not Found User: " + id);
        }

        log.info("UserDTO from DB: {}", findUser);
        log.info("ID: {}, PW: {}", findUser.getId(), findUser.getPassword());

        return findUser;
    }
}
