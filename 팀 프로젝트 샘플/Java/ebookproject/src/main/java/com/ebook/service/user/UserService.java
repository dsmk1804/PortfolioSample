package com.ebook.service.user;

import com.ebook.dto.BookDTO;
import com.ebook.dto.user.CashChargeDTO;
import com.ebook.dto.user.UserDTO;
import com.ebook.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.awt.print.Book;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RestOperations restOperations;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 테스트시 본인의 url 사용하기
    private final String PORT_ONE_IMP_KEY = "3706357072443643";
    private final String PORT_ONE_IMP_SECRET = "MGMGBgVfkjogobPd9VxX9ZrsGP3mBQtMrTteUtFvTw922Ya8zp6pAULzGW9LkIMuAY7LGOSb9cvWQUxT";
    private final String PORT_ONE_ACCESS_TOKEN_URL = "https://api.iamport.kr/users/getToken";
    private final String PORT_ONE_USER_CERT_INFO_URL = "https://api.iamport.kr/certifications/{impUid}";

    // 포트원의 ACCESS_TOKEN 값을 얻는 메서드
    private String get_portone_access_token(){
        RequestEntity<String> getAccessTokenRequest = RequestEntity
                .post(PORT_ONE_ACCESS_TOKEN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(JSONObject.toJSONString(Map.of(
                        "imp_key", PORT_ONE_IMP_KEY,
                        "imp_secret", URLEncoder.encode(PORT_ONE_IMP_SECRET, StandardCharsets.UTF_8)
                )));
        ResponseEntity<Map> getAccessTokenResponse = restOperations.exchange(getAccessTokenRequest, Map.class);
        // 요청이 성공적으로 완료되었음
        if(getAccessTokenResponse.getStatusCode().equals(HttpStatus.OK)){
            Map<String, Map> body = (Map<String, Map>)getAccessTokenResponse.getBody();
            Map<String, String> response = body.get("response");
            String accessToken = response.get("access_token");
            log.info("액세스토큰 발급 완료");
            return accessToken;
        }
        log.error("요청에러(실패)");
        return null;

    }

    // 포트원의 본인인증 정보를 얻는 메서드
    private String get_portone_user_cert_info(String impUid, String accessToken){
        RequestEntity<Void> userCertRequest = RequestEntity.get(PORT_ONE_USER_CERT_INFO_URL, impUid)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<Map> userCertResponse = restOperations.exchange(userCertRequest, Map.class);
        log.info(userCertResponse);
        // 200. OK. 요청이 성공하였음
        if(userCertResponse.getStatusCode().equals(HttpStatus.OK)){
            Map<String, Object> body = userCertResponse.getBody();
            log.info("body : " + body);
            Map<String, Object> response = (Map<String, Object>)body.get("response");
            Boolean certified = (Boolean)response.get("certified");
            if(certified){
                log.info("인증성공");
                String uniqueKey = (String)response.get("unique_key");
                return uniqueKey;
            }
            log.warn("인증실패");
            return null;
        }
        log.error("요청에러(실패)");
        return null;
    }

    public boolean create_user(
            String impUid,
            UserDTO userDTO
    ){

        if(impUid.isBlank()){
            return false;
        }
        String accessToken = get_portone_access_token();
        log.info("accessToken : "+accessToken);
        if(accessToken == null){
            return false;
        }
        String userCi = get_portone_user_cert_info(impUid, accessToken);
        log.info("userCi : " +userCi);
        if(userCi == null){
            return false;
        }

        // 유저를 회원가입 시킬 때, CI 값을 같이 넣어준다
        userDTO.setUserCi(userCi);
        // 유저를 회원가입 시킬 때, 패스워드를 인코딩해서 넣는다
        userDTO.setUserPassword(passwordEncoder.encode(userDTO.getPassword()));
        userMapper.insertUser(userDTO);
        return true;

    }

    public boolean findUser(String id){
        UserDTO findUser = userMapper.selectUserById(id);// 해당 id를 가진 유저를 db에서 조회
        return Objects.isNull(findUser);
    }

    public String findUserId(String name, String email){
        UserDTO findUserId = userMapper.selectUserByNameAndEmail(name, email);
        System.out.println(findUserId);
        if(Objects.isNull(findUserId)){
            return null;
        }
        return findUserId.getUserId();
    }


    public void resetPassword(String id, String name, String email, String EncodingPassword){
        log.info("userservice : " + id + name + email + EncodingPassword);
        userMapper.resetUserPassword(id, name, email, EncodingPassword);
    }


    /************************* 캐시 충전 *******************************/
    public void charge_cash(UserDTO user, CashChargeDTO cashCharge){
        Integer cashAmount = cashCharge.getCashAmount(); // 충전할 금액
        // 캐시 충전 내역을 생성한다
        userMapper.insertCashCharge(user.getUserId(), cashCharge);
        // 유저의 캐시를 충전금액 만큼 올린다
        userMapper.updateUserCash(user.getUserId(), cashAmount);
        // 위 두개가 성공했으면 현재 로그인되어있는 유저의 cash도 올려준다
        user.setUserCash(user.getUserCash() + cashAmount);
    }

    /************************챕터 구매 *******************************************/

    // 해당 챕터의 가격을 가지고 온다.
    public Integer select_chapters_price(Integer chapterNo){
        return userMapper.selectChapterPrice(chapterNo);
    }

    /************************* 책 좋아요 하기 *******************************/
    public List<BookDTO> getAllUserLikeBook(UserDTO user){
        return userMapper.selectBookByUserLike(user.getUserId());
    }

    public List<BookDTO> getAllUserBoughtBook(UserDTO user){
        return userMapper.selectBookByUserBought(user.getUserId());
    }

    // 유저가 구매에 성공했으면 캐시를 챕터의 가격만큼 차감시킨다.
    public void buyResultCash(UserDTO user, @Param("chaptersPrice") Integer chaptersPrice){
        userMapper.updateBuyResult(user.getUserId(), chaptersPrice);
    }
    public void saveBookLike(UserDTO user, Integer bookNo){
        userMapper.insertBookLike(user.getUserId(), bookNo);
    }
    public void removeBookLike(UserDTO user, Integer bookNo){
        userMapper.deleteBookLike(user.getUserId(), bookNo);
    }

    // 유저가 산 책의 정보를 db에 저장
    public void user_buy_book(Integer no, UserDTO userId, Integer bookChaptersPrice){
        userMapper.insertUserByBook(no, userId, bookChaptersPrice);

        //
//        UserDTO user = userMapper.selectUserById(userId);
//        if (user == null) {
//            return null;
//        }
//        int userCash = user.getCash();
//        if (userCash < bookPrice) {
//            return null;
//        }
//        userMapper.updateUserCash(userId, userCash - bookPrice);
//        return user.getUserId();
    }




}


