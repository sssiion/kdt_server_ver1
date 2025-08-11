package com.example.KDT_bank_server_project2.manager.EntityUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "cash_transaction")
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class CashTransaction { //거래 기록
    @Id //거래가 남을때마다 생성되는것
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId; //계좌 기록  id

    @Column(name = "account_number", nullable = false)
    private String accountNumber; // 계좌번호

    @Column(name="account_number_other")
    private String otherAccountNumber; //상대방 계좌 번호//그냥 입출금은 ""
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // 입출금 금액

    @Column(name ="totalamount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType; // 입출금 타입


    @Column(name = "transaction_date") // 입출금 기록 날짜
    private LocalDateTime transactionDate;

    public enum TransactionType {
        입금, 출금;

        public static TransactionType fromString(String type) {
            if (type == null) return 입금;
            try {
                return TransactionType.valueOf(type);
            } catch (IllegalArgumentException e) {
                return 입금;
            }
        }
    }

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }
    public CashTransaction(Account account){
        this.accountNumber = account.getAccountNumber();
    }



}