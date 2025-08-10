package com.example.KDT_bank_server_project2.manager.DtoUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// 계좌 응답 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class AccountResponseDto { //계정 응답

    private String accountNumber; // 계좌번호
    private String customerId; // 사용자 id
    private BigDecimal amount;  // 총금액
    private String productType; // 대출, 예금
    private LocalDateTime createdAt; // 만들어진 날
    private LocalDateTime updatedAt; // 업뎃 날

    public AccountResponseDto(Account account) {
        this.accountNumber = account.getAccountNumber(); // 계좌 번호
        this.customerId = account.getCustomerId(); //
        this.amount = account.getAmount();

        this.productType = account.getProductType();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
        System.out.println("AccountResponseDto 생성: " +
                "계좌번호 " + accountNumber +
                ", 고객ID: " + customerId +
                ", 잔액: " + amount );
    }
}
