package com.example.KDT_bank_server_project2.manager.DtoUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.LoanAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// 대출 계좌 응답 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class LoanAccountResponseDto {
    //총금액이랑, 갚은 금액만
    private String loanId; // 대출계좌 id
    private String customerId; // 사용자 id
    private String productName; // 상품명
    private BigDecimal totalAmount; // 총금액
    private BigDecimal repaymentAmount; // 갚은 양
    private BigDecimal interestRate; // 이율
    private LocalDate loanDate; // 남은 시간
    private LocalDate maturityDate; // 만기일
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LoanAccountResponseDto(LoanAccount loanAccount) {
        this.loanId = loanAccount.getLoanId();
        this.customerId = loanAccount.getCustomerId();
        this.productName = loanAccount.getProductName();
        this.repaymentAmount = loanAccount.getRepaymentAmount();
        this.totalAmount = loanAccount.getTotalAmount();
        this.interestRate = loanAccount.getInterestRate();
        this.loanDate = loanAccount.getLoanDate();
        this.maturityDate = loanAccount.getMaturityDate();
        this.createdAt = loanAccount.getCreatedAt();
        this.updatedAt = loanAccount.getUpdatedAt();
        System.out.println("LoanAccountResponseDto 생성: 대출ID " + loanId +
                ", 고객ID: " + customerId +
                ", 대출금액: " + totalAmount);
    }
}
