package com.example.KDT_bank_server_project2.manager.Repository;

import com.example.KDT_bank_server_project2.manager.EntityUser.CashTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, String> {

    List<CashTransaction> findByAccountNumber(String accountNumber);
    // 계좌번호로 거래 내역 조회

    List<CashTransaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber);
    // 계좌번호로 거래 내역을 최신순으로 조회

    List<CashTransaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber, Pageable pageable);
    // 계좌번호로 거래 내역을 최신순으로 조회 (페이지네이션)

    List<CashTransaction> findByTransactionType(CashTransaction.TransactionType transactionType);
    // 거래 유형별 조회

    List<CashTransaction> findByAccountNumberAndTransactionType(String accountNumber, CashTransaction.TransactionType transactionType);
    // 계좌번호 및 거래 유형별 조회

    List<CashTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    // 거래 날짜 기간으로 조회

    List<CashTransaction> findByAccountNumberAndTransactionDateBetween(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
    // 계좌번호 및 거래 날짜 기간으로 조회

    List<CashTransaction> countByAccountNumber(String accountNumber);
    // 특정 계좌의 거래 건수

    @Query("SELECT ct FROM CashTransaction ct WHERE ct.accountNumber = :accountNumber AND ct.amount >= :minAmount ORDER BY ct.transactionDate DESC")
    List<CashTransaction> findByAccountNumberAndMinAmount(@Param("accountNumber") String accountNumber, @Param("minAmount") BigDecimal minAmount);
    // 특정 계좌의 최소 금액 이상 거래 조회

    @Query("SELECT SUM(ct.amount) FROM CashTransaction ct WHERE ct.accountNumber = :accountNumber AND ct.transactionType = :transactionType AND ct.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByTypeAndPeriod(@Param("accountNumber") String accountNumber,
                                             @Param("transactionType") CashTransaction.TransactionType transactionType,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
    // 특정 기간 동안 거래 유형별 총액

    //@Query("SELECT ct FROM CashTransaction ct WHERE ct.note LIKE %:keyword% ORDER BY ct.transactionDate DESC")
    //List<CashTransaction> searchByNote(@Param("keyword") String keyword);
    // 거래 메모로 검색

    @Query("SELECT ct FROM CashTransaction ct WHERE ct.accountNumber = :accountNumber ORDER BY ct.transactionDate DESC LIMIT 1")
    CashTransaction findLatestTransactionByAccountNumber(@Param("accountNumber") String accountNumber);
    // 특정 계좌의 최근 거래 1건

    @Query("SELECT ct FROM CashTransaction ct WHERE ct.accountNumber = :accountNumber AND ct.transactionType = :transactionType ORDER BY ct.transactionDate DESC")
    List<CashTransaction> findByAccountNumberAndTransactionTypeOrderByTransactionDateDesc(@Param("accountNumber") String accountNumber,
                                                                                          @Param("transactionType") CashTransaction.TransactionType transactionType,
                                                                                          Pageable pageable);
    // 계좌번호 및 거래 유형별 최신순 조회 (페이지네이션)
}