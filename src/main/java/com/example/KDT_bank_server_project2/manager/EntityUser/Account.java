package com.example.KDT_bank_server_project2.manager.EntityUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account { //계좌 생성
    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(precision = 30, scale = 2)
    private BigDecimal amount;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;



    @Column(name = "product_type", nullable = false, length = 50)
    private String productType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}