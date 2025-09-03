package com.ebook.service.user;

import com.ebook.dto.user.CustomUserDTO;
import com.ebook.dto.user.UserDTO;
import com.ebook.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Log4j2
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final String CI = "3gtMPrKkgyLhbAdC35qZMJvlQOR9k5N2KpAZ8Y61po9HAJNQAiVMaBciCEUToY2uE4kOrGEav70EYPjxLaOBkw==";
    private final UserMapper userMapper;

    public CustomOAuth2Service(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        log.info("clientRegistration : " + clientRegistration);
        String clientName = clientRegistration.getClientName();
        log.info("[" + clientName + "] (으)로 로그인 중입니다");
        // 실제 로그인 진행
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User : " + oAuth2User);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes : " + attributes);
        CustomUserDTO customUserDTO =
                switch (clientName.toUpperCase()){
                    case "NAVER" ->naver_login(attributes);
                    case "KAKAO" ->kakao_login(attributes);
                    case "GOOGLE" ->google_login(attributes);
                    default ->throw new OAuth2AuthenticationException("허용되지 않은 로그인");
                };
        log.info("customUserDTO : " + customUserDTO);

        // 해당 SNS 유저로 기존 유저를 찾는다 (CI 값으로 찾는다)
        UserDTO findUser = userMapper.selectUserByCi(customUserDTO.getCi());
        log.info("findUser : " + findUser);
        // 해당 소셜 정보로 가입된 유저를 찾는다
        CustomUserDTO findSocialUser = userMapper.selectCustomUserById(customUserDTO.getId());
        log.info("findSocialUser : " + findSocialUser);
        if(Objects.nonNull(findUser)){
            // 해당 유저가 이 소셜 로그인으로 로그인한 기록이 없는가? =>
            // 기존 유저와 소셜 유저를 연결 시키면 된다 (Insert)
            if (Objects.isNull(findSocialUser)) {
                userMapper.insertSocialUser(customUserDTO);
            }
            // 찾은 유저로 로그인 시키기
            return findUser;

        }
        // 해당 유저가 존재하지 않음 => 먼저 회원부터 회원가입을 시켜야 함
        return oAuth2User;


    }


    private CustomUserDTO naver_login(Map<String, Object> userProperties){
        Map<String, String> response = (Map<String, String>) userProperties.get("response");
        String id = response.get("id");
        String profileImageURL = response.get("profile_image");
        String email = response.get("email");
        String name = response.get("name");
        return CustomUserDTO.builder()
                .id(id)
                .profileImageUrl(profileImageURL)
                .email(email)
                .ci(CI)
                .name(name)
                .build();
    }


    private CustomUserDTO kakao_login(Map<String, Object> attributes){
        String id = attributes.get("id").toString();
        Map<String, String> properties = (Map<String, String>) attributes.get("properties");
        String nickName = properties.get("nickname");
        String profileImageURL = properties.get("profile_image");
        return CustomUserDTO.builder()
                .id(id)
                .ci(CI)
                .profileImageUrl(profileImageURL)
                .nickName(nickName)
                .build();
    }

    private CustomUserDTO google_login(Map<String, Object> attributes){
        Map<String, String> response = (Map<String, String>) attributes.get("response");
        log.info("response : " + response);
        String id = response.get("id");
        String profileImageURL = response.get("profile_image");
        String email = response.get("email");
        String name = response.get("name");
        return CustomUserDTO.builder()
                .id(id)
                .profileImageUrl(profileImageURL)
                .email(email)
                .ci(CI)
                .name(name)
                .build();


    }















}
