package com.example.KDT_bank_server_project2.manager.DtoUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.Agreement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 약정 응답 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class AgreementResponseDto { //계약 응답

    private String agreementId; //계좌 번호
    private String customerId;  // 사용자 id
    private String productName; //상품명
    private LocalDate agreementDate; // 입금
    private LocalDate expirationDate; // 출금
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AgreementResponseDto(Agreement agreement) {
        this.agreementId = agreement.getAgreementId();
        this.customerId = agreement.getCustomerId();
        this.productName = agreement.getProductName();
        this.agreementDate = agreement.getAgreementDate();
        this.expirationDate = agreement.getExpirationDate();

        this.createdAt = agreement.getCreatedAt();
        this.updatedAt = agreement.getUpdatedAt();
        System.out.println("AgreementResponseDto 생성: 약정ID " + agreementId +
                ", 고객ID: " + customerId +
                ", 상품: " + productName +
                ", 체결일: " + agreementDate +
                ", 만료일: " + expirationDate);
    }
}
