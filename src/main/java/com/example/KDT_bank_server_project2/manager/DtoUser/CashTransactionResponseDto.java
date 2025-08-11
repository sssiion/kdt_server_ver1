package com.example.KDT_bank_server_project2.manager.DtoUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.CashTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 현금 거래 응답 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class CashTransactionResponseDto {

    private Long transactionId; //거래id
    private String accountNumber;// 계좌 번호
    private String otherAccountNumber; // 상대 계좌 번호
    private BigDecimal amount;// 입출금 금액
    private String transactionType; // 입출금 타입
    private LocalDateTime transactionDate; // 입출금 기록 날짜

    public CashTransactionResponseDto(CashTransaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.accountNumber = transaction.getAccountNumber();
        this.otherAccountNumber = transaction.getOtherAccountNumber();
        this.transactionType = transaction.getTransactionType().toString();
        this.amount = transaction.getAmount();
        this.transactionDate = transaction.getTransactionDate();
        System.out.println("CashTransactionResponseDto 생성: 거래ID " + transactionId +
                ", 계좌번호: " + accountNumber +
                ", 거래유형: " + transactionType +
                ", 금액: " + amount);
    }

    public CashTransactionResponseDto() {

    }
}
