package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.EntityUser.CashTransaction;
import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
import com.example.KDT_bank_server_project2.manager.Repository.CashTransactionRepository;
import com.example.KDT_bank_server_project2.manager.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CashTransactionService {


    private final CashTransactionRepository cashTransactionRepository;
    private final AccountRepository accountRepository;

    // 거래 기록 생성
    public CashTransaction createTransaction(String accountNumber,String otherAccountNumber, CashTransaction.TransactionType transactionType,
                                             BigDecimal amount) {
        // 계좌 존재 여부 확인
        Account account = accountRepository.findByAccountNumber(accountNumber.trim())
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));
        Account otherAccount = accountRepository.findByAccountNumber(otherAccountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));
        BigDecimal balanceAfter;

        // 거래 유형에 따른 처리
        switch (transactionType) {
            case 입금:
                balanceAfter = account.getAmount().add(amount);
                break;
            case 출금:
                balanceAfter = account.getAmount().subtract(amount);
                break;
            default:
                throw new RuntimeException("지원하지 않는 거래 유형입니다");
        }

        CashTransaction transaction = new CashTransaction(account);
        transaction.setOtherAccountNumber(otherAccountNumber);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setTotalAmount(balanceAfter);
        transaction.setTransactionDate(LocalDateTime.now());


        return cashTransactionRepository.save(transaction);
    }

    // 계좌별 거래 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByAccountNumber(String accountNumber) {
        return cashTransactionRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber);
    }

    // 계좌별 거래 내역 조회 (페이지네이션)
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByAccountNumber(String accountNumber, Pageable pageable) {
        return cashTransactionRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber, pageable);
    }

    // 거래 유형별 거래 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByType(CashTransaction.TransactionType transactionType) {
        return cashTransactionRepository.findByTransactionType(transactionType);
    }

    // 특정 계좌의 거래 유형별 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByAccountAndType(String accountNumber,
                                                                 CashTransaction.TransactionType transactionType) {
        return cashTransactionRepository.findByAccountNumberAndTransactionType(accountNumber, transactionType);
    }

    // 기간별 거래 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return cashTransactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    // 특정 계좌의 기간별 거래 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByAccountAndDateRange(String accountNumber,
                                                                      LocalDateTime startDate, LocalDateTime endDate) {
        return cashTransactionRepository.findByAccountNumberAndTransactionDateBetween(accountNumber, startDate, endDate);
    }

    // 특정 계좌의 거래 건수 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionCountByAccountNumber(String accountNumber) {
        return cashTransactionRepository.countByAccountNumber(accountNumber);
    }

    // 특정 기간 동안 거래 유형별 총액 조회
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByTypeAndPeriod(String accountNumber, CashTransaction.TransactionType transactionType,
                                                    LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalAmount = cashTransactionRepository.getTotalAmountByTypeAndPeriod(
                accountNumber, transactionType, startDate, endDate);
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    // 거래 메모로 검색
    //@Transactional(readOnly = true)
    //public List<CashTransaction> searchTransactionsByNote(String keyword) {
        //return cashTransactionRepository.searchByNote(keyword);
    //}

    // 특정 계좌의 최근 거래 조회
    @Transactional(readOnly = true)
    public CashTransaction getLatestTransactionByAccountNumber(String accountNumber) {
        return cashTransactionRepository.findLatestTransactionByAccountNumber(accountNumber);
    }

    // 최소 금액 이상 거래 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByMinAmount(String accountNumber, BigDecimal minAmount) {
        return cashTransactionRepository.findByAccountNumberAndMinAmount(accountNumber, minAmount);
    }


}
