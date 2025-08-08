package com.example.KDT_bank_server_project2.manager.Repository;


import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
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
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByCustomerId(String customerId);
    // 고객 ID로 계좌 조회

    List<Account> findByCustomerIdAndStatus(String customerId, Account.AccountStatus status);
    // 고객 ID 및 상태별 계좌 조회

    List<Account> findByProductName(String productName);
    // 상품명으로 계좌 조회

    List<Account> findByStatus(Account.AccountStatus status);
    // 상태별 계좌 조회

    List<Account> findByProductType(String productType);
    // 상품 유형별 계좌 조회

    Optional<Account> findByAccountNumber(String accountNumber);
    // 계좌번호로 계좌 찾기

    List<Account> findByCustomerIdOrderByCreatedAtDesc(String customerId);
    // 고객의 계좌를 최신 생성순으로 조회

    List<Account> findByOpeningDateBetween(LocalDate startDate, LocalDate endDate);
    // 개설일 기간으로 계좌 조회
    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") String customerId);
    // 고객의 활성 계좌만 조회

    @Query("SELECT a FROM Account a WHERE a.amount >= :minAmount ORDER BY a.amount DESC")
    List<Account> findByMinAmountOrderByAmountDesc(@Param("minAmount") BigDecimal minAmount);
    // 최소 잔액 이상인 계좌를 잔액순으로 조회

    @Query("SELECT COUNT(a) FROM Account a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    String countActiveAccountsByCustomerId(@Param("customerId") String customerId);
    // 고객의 활성 계좌 개수

    @Query("SELECT a FROM Account a WHERE a.closingDate IS NOT NULL AND a.closingDate <= :currentDate")
    List<Account> findExpiredAccounts(@Param("currentDate") LocalDate currentDate);
    // 만료된 계좌 조회
}