package com.example.KDT_bank_server_project2.manager.Repository;


import com.example.KDT_bank_server_project2.manager.EntityUser.LoanApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, String> {

    List<LoanApplication> findByCustomerId(String customerId);
    // 고객 ID로 대출 신청 조회

    List<LoanApplication> findByCustomerIdAndStatus(String customerId, LoanApplication.ApplicationStatus status);
    // 고객 ID 및 상태별 대출 신청 조회

    List<LoanApplication> findByStatus(LoanApplication.ApplicationStatus status);
    // 상태별 대출 신청 조회

    List<LoanApplication> findByProductName(String productName);
    // 상품명으로 대출 신청 조회


    List<LoanApplication> findByApprovedBy(String employeeId);
    // 승인 직원으로 대출 신청 조회

    List<LoanApplication> findByTargetAccountNumber(String accountNumber);
    // 목표 계좌번호로 대출 신청 조회

    List<LoanApplication> findByCustomerIdOrderByApplicationDateDesc(String customerId);
    // 고객의 대출 신청을 최신 신청일순으로 조회

    List<LoanApplication> findByApplicationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    // 신청일 기간으로 조회

    List<LoanApplication> findByApprovalDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    // 승인일 기간으로 조회

    @Query("SELECT la FROM LoanApplication la WHERE la.status = 'PENDING' ORDER BY la.applicationDate ASC")
    List<LoanApplication> findPendingApplicationsOrderByApplicationDate();
    // 대기 중인 대출 신청을 신청일순으로 조회

    @Query("SELECT COUNT(la) FROM LoanApplication la WHERE la.customerId = :customerId AND la.status = 'PENDING'")
    long countPendingApplicationsByCustomerId(@Param("customerId") String customerId);
    // 고객의 대기 중인 대출 신청 개수

    @Query("SELECT la FROM LoanApplication la WHERE la.requestedAmount >= :minAmount ORDER BY la.requestedAmount DESC")
    List<LoanApplication> findByMinRequestedAmountOrderByAmountDesc(@Param("minAmount") BigDecimal minAmount);
    // 최소 신청 금액 이상인 대출 신청을 금액순으로 조회

    @Query("SELECT la FROM LoanApplication la WHERE la.productName = :productName AND la.status = :status ORDER BY la.applicationDate DESC")
    List<LoanApplication> findByProductNameAndStatusOrderByApplicationDateDesc(@Param("productName") String productName,
                                                                               @Param("status") LoanApplication.ApplicationStatus status,
                                                                               Pageable pageable);
    // 상품별 및 상태별 대출 신청을 최신순으로 조회 (페이지네이션)

    @Query("SELECT SUM(la.requestedAmount) FROM LoanApplication la WHERE la.status = 'APPROVED' AND la.approvalDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalApprovedAmountByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    // 특정 기간 동안 승인된 총 대출 금액
}