package com.example.KDT_bank_server_project2.manager.EntityUser;

import com.example.KDT_bank_server_project2.manager.DtoUser.LoanAccountCreateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "loan_account")
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccount { // 대출 계좌
    @Id
    @Column(name = "loan_id", length = 13, unique = true, nullable = false)
    private String loanId; //계좌 번호

    @Column(name = "customer_id", nullable = false)
    private String customerId; // 사용자 아이디

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName; // 상품명

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;  // 총금액

    @Column(name = "repayment_amount", precision = 15, scale = 2)
    private BigDecimal repaymentAmount; // 지금까지 갚은 금액

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate; //이율

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;  //대출 날짜

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate; //만기일



    @Column(name = "created_at")
    private LocalDateTime createdAt; //만든날

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //업데이트날


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (repaymentAmount == null) {
            repaymentAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    public LoanAccount createLoanAccount(LoanAccountCreateRequestDto dto){
        LoanAccount account = new LoanAccount();
        account.setCustomerId(dto.getCustomerId());
        account.setProductName(dto.getProductName());
        account.setTotalAmount(dto.getTotalAmount());
        account.setInterestRate(dto.getInterestRate());
        account.setLoanDate(dto.getLoanDate());
        account.setMaturityDate(dto.getMaturityDate());

        return account;
    }
}