package com.example.KDT_bank_server_project2.manager.Repository;


import com.example.KDT_bank_server_project2.manager.EntityUser.LoanAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Long> {

    Optional<LoanAccount> findByLoanId(String loanId);
    // 대출 ID로 대출 계좌 찾기

    List<LoanAccount> findByCustomerId(String customerId);
    // 고객 ID로 대출 계좌 조회

    List<LoanAccount> findByCustomerIdAndStatus(String customerId, LoanAccount.LoanStatus status);
    // 고객 ID 및 상태별 대출 계좌 조회

    List<LoanAccount> findByStatus(LoanAccount.LoanStatus status);
    // 상태별 대출 계좌 조회

    List<LoanAccount> findByProductName(String productName);
    // 상품명으로 대출 계좌 조회

    List<LoanAccount> findByMaturityDateBefore(LocalDate date);
    // 만기일이 특정 날짜 이전인 대출 계좌 조회

    List<LoanAccount> findByMaturityDateBetween(LocalDate startDate, LocalDate endDate);
    // 만기일 기간으로 대출 계좌 조회

    List<LoanAccount> findByCustomerIdOrderByLoanDateDesc(String customerId);
    // 고객의 대출 계좌를 최신 대출일순으로 조회
    boolean existsByLoanId(String loanId);
    @Query("SELECT la FROM LoanAccount la WHERE la.customerId = :customerId AND la.status = 'ACTIVE'")
    List<LoanAccount> findActiveLoansByCustomerId(@Param("customerId") String customerId);
    // 고객의 활성 대출 계좌만 조회

    @Query("SELECT la FROM LoanAccount la WHERE la.totalAmount - la.repaymentAmount > 0 AND la.status = 'ACTIVE'")
    List<LoanAccount> findActiveLoansWithBalance();
    // 잔액이 있는 활성 대출 계좌 조회

    @Query("SELECT COUNT(la) FROM LoanAccount la WHERE la.customerId = :customerId AND la.status = 'ACTIVE'")
    String countActiveLoansByCustomerId(@Param("customerId") String customerId);
    // 고객의 활성 대출 개수

    @Query("SELECT SUM(la.totalAmount - la.repaymentAmount) FROM LoanAccount la WHERE la.customerId = :customerId AND la.status = 'ACTIVE'")
    BigDecimal getTotalLoanBalanceByCustomerId(@Param("customerId") String customerId);
    // 고객의 총 대출 잔액

    @Query("SELECT la FROM LoanAccount la WHERE la.maturityDate <= :date AND la.status = 'ACTIVE' ORDER BY la.maturityDate ASC")
    List<LoanAccount> findLoansNearingMaturity(@Param("date") LocalDate date);
    // 만기가 임박한 대출 조회
}