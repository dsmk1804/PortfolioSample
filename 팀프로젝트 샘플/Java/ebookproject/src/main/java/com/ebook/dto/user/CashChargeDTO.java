package com.ebook.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@ToString
public class CashChargeDTO {
    @JsonAlias("merchant_uid")
    private String merchantUid; // 주문번호
    @JsonAlias("imp_uid")
    private String impUid; // 결제번호
    private UserDTO userDTO; // 충전자
    private String name; //주문명
    @JsonAlias("cash_amount")
    private Integer cashAmount; // 충전한 cash
    @JsonAlias("paid_amount")
    private String paidAmount; // 낸 돈
    @JsonAlias("pg_provider")
    private String pgProvider; // 제공자 (kakaopay)
    @JsonAlias("paid_at")
    private Long paidAt; // 결제 시각
}
