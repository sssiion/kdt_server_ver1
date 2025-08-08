package com.example.KDT_bank_server_project2.manager.ServiceUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.LoanApplication;
import com.example.KDT_bank_server_project2.manager.Repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    // 대출 신청 생성
    public LoanApplication createLoanApplication(LoanApplication loanApplication) {
        if (loanApplication.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("신청 금액은 0보다 커야 합니다");
        }

        if (loanApplication.getApplicationDate() == null) {
            loanApplication.setApplicationDate(LocalDateTime.now());
        }

        return loanApplicationRepository.save(loanApplication);
    }

    // 모든 대출 신청 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    // ID로 대출 신청 조회
    @Transactional(readOnly = true)
    public Optional<LoanApplication> getLoanApplicationById(String id) {
        return loanApplicationRepository.findById(id);
    }

    // 고객별 대출 신청 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getLoanApplicationsByCustomerId(String customerId) {
        return loanApplicationRepository.findByCustomerIdOrderByApplicationDateDesc(customerId);
    }

    // 상태별 대출 신청 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getLoanApplicationsByStatus(String status) {
        return loanApplicationRepository.findByStatus(LoanApplication.ApplicationStatus.valueOf(status));
    }

    // 대기 중인 대출 신청 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getPendingApplications() {
        return loanApplicationRepository.findPendingApplicationsOrderByApplicationDate();
    }

    // 상품별 대출 신청 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getLoanApplicationsByProductName(String productName) {
        return loanApplicationRepository.findByProductName(productName);
    }

    // 대출 신청 승인
    public LoanApplication approveLoanApplication(String applicationId, String approverId) {
        LoanApplication application = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("대출 신청을 찾을 수 없습니다: " + applicationId));

        if (application.getStatus() != LoanApplication.ApplicationStatus.PENDING) {
            throw new RuntimeException("대기 중인 신청만 승인할 수 있습니다");
        }

        application.setStatus(LoanApplication.ApplicationStatus.APPROVED);
        application.setApprovalDate(LocalDateTime.now());
        application.setApprovedBy(approverId);

        return loanApplicationRepository.save(application);
    }

    // 대출 신청 거절
    public LoanApplication rejectLoanApplication(String applicationId, String rejectionReason) {
        LoanApplication application = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("대출 신청을 찾을 수 없습니다: " + applicationId));

        if (application.getStatus() != LoanApplication.ApplicationStatus.PENDING) {
            throw new RuntimeException("대기 중인 신청만 거절할 수 있습니다");
        }

        application.setStatus(LoanApplication.ApplicationStatus.REJECTED);
        application.setRejectionReason(rejectionReason);

        return loanApplicationRepository.save(application);
    }

    // 대출 신청 취소
    public LoanApplication cancelLoanApplication(String applicationId) {
        LoanApplication application = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("대출 신청을 찾을 수 없습니다: " + applicationId));

        if (application.getStatus() != LoanApplication.ApplicationStatus.PENDING) {
            throw new RuntimeException("대기 중인 신청만 취소할 수 있습니다");
        }

        application.setStatus(LoanApplication.ApplicationStatus.CANCELLED);

        return loanApplicationRepository.save(application);
    }

    // 고객의 대기 중인 대출 신청 개수 조회
    @Transactional(readOnly = true)
    public String getPendingApplicationCountByCustomerId(String customerId) {
        return loanApplicationRepository.countPendingApplicationsByCustomerId(customerId);
    }

    // 기간별 신청 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getApplicationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return loanApplicationRepository.findByApplicationDateBetween(startDate, endDate);
    }

    // 기간별 승인 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getApprovalsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return loanApplicationRepository.findByApprovalDateBetween(startDate, endDate);
    }

    // 특정 기간 동안 승인된 총 대출 금액
    @Transactional(readOnly = true)
    public BigDecimal getTotalApprovedAmountByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalAmount = loanApplicationRepository.getTotalApprovedAmountByPeriod(startDate, endDate);
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    // 최소 신청 금액 이상인 대출 신청 조회
    @Transactional(readOnly = true)
    public List<LoanApplication> getApplicationsByMinAmount(BigDecimal minAmount) {
        return loanApplicationRepository.findByMinRequestedAmountOrderByAmountDesc(minAmount);
    }

    // 상품별 및 상태별 대출 신청 조회 (페이지네이션)
    @Transactional(readOnly = true)
    public List<LoanApplication> getApplicationsByProductAndStatus(String productName,
                                                                   LoanApplication.ApplicationStatus status,
                                                                   Pageable pageable) {
        return loanApplicationRepository.findByProductNameAndStatusOrderByApplicationDateDesc(
                productName, status, pageable);
    }
}
