package com.example.KDT_bank_server_project2.manager.DtoUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.LoanApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 대출 신청 응답 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class LoanApplicationResponseDto {

    private Long applicationId; //
    private String customerId; // 사용자 아이디
    private String productName; //상품명
    private BigDecimal requestedAmount; // 요청받은 금액
    private LocalDateTime applicationDate;
    private LocalDateTime approvalDate;
    private String approvedBy;
    private String rejectionReason;
    private String targetAccountNumber;

    public LoanApplicationResponseDto(LoanApplication application) {
        this.applicationId = application.getApplicationId();
        this.customerId = application.getCustomerId();

        this.productName = application.getProductName();
        this.requestedAmount = application.getRequestedAmount();

        this.applicationDate = application.getApplicationDate();
        this.approvalDate = application.getApprovalDate();
        this.approvedBy = application.getApprovedBy();
        this.rejectionReason = application.getRejectionReason();
        this.targetAccountNumber = application.getTargetAccountNumber();
        System.out.println("LoanApplicationResponseDto 생성: 신청ID " + applicationId +
                ", 고객ID: " + customerId +
                ", 상품: " + productName +
                ", 신청금액: " + requestedAmount );
    }
}
