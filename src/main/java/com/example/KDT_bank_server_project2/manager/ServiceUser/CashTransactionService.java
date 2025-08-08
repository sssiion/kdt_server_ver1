package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.EntityUser.CashTransaction;
import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
import com.example.KDT_bank_server_project2.manager.Repository.CashTransactionRepository;
import com.example.KDT_bank_server_project2.manager.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CashTransactionService {

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    // 거래 기록 생성
    public CashTransaction createTransaction(Long accountNumber, CashTransaction.TransactionType transactionType,
                                             BigDecimal amount, String note) {
        // 계좌 존재 여부 확인
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        BigDecimal balanceAfter;

        // 거래 유형에 따른 처리
        switch (transactionType) {
            case 입금:
                accountService.deposit(accountNumber, amount);
                balanceAfter = account.getAmount().add(amount);
                break;
            case 출금:
                accountService.withdraw(accountNumber, amount);
                balanceAfter = account.getAmount().subtract(amount);
                break;
            case 이체:
                // 이체는 별도 로직 필요 (여기서는 단순히 출금으로 처리)
                accountService.withdraw(accountNumber, amount);
                balanceAfter = account.getAmount().subtract(amount);
                break;
            default:
                throw new RuntimeException("지원하지 않는 거래 유형입니다");
        }

        CashTransaction transaction = new CashTransaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceAfterTransaction(balanceAfter);
        transaction.setNote(note);
        transaction.setTransactionDate(LocalDateTime.now());

        return cashTransactionRepository.save(transaction);
    }

    // 계좌별 거래 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByAccountNumber(Long accountNumber) {
        return cashTransactionRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber);
    }

    // 계좌별 거래 내역 조회 (페이지네이션)
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByAccountNumber(Long accountNumber, Pageable pageable) {
        return cashTransactionRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber, pageable);
    }

    // 거래 유형별 거래 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByType(CashTransaction.TransactionType transactionType) {
        return cashTransactionRepository.findByTransactionType(transactionType);
    }

    // 특정 계좌의 거래 유형별 내역 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByAccountAndType(Long accountNumber,
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
    public List<CashTransaction> getTransactionsByAccountAndDateRange(Long accountNumber,
                                                                      LocalDateTime startDate, LocalDateTime endDate) {
        return cashTransactionRepository.findByAccountNumberAndTransactionDateBetween(accountNumber, startDate, endDate);
    }

    // 특정 계좌의 거래 건수 조회
    @Transactional(readOnly = true)
    public long getTransactionCountByAccountNumber(Long accountNumber) {
        return cashTransactionRepository.countByAccountNumber(accountNumber);
    }

    // 특정 기간 동안 거래 유형별 총액 조회
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByTypeAndPeriod(Long accountNumber, CashTransaction.TransactionType transactionType,
                                                    LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalAmount = cashTransactionRepository.getTotalAmountByTypeAndPeriod(
                accountNumber, transactionType, startDate, endDate);
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    // 거래 메모로 검색
    @Transactional(readOnly = true)
    public List<CashTransaction> searchTransactionsByNote(String keyword) {
        return cashTransactionRepository.searchByNote(keyword);
    }

    // 특정 계좌의 최근 거래 조회
    @Transactional(readOnly = true)
    public CashTransaction getLatestTransactionByAccountNumber(Long accountNumber) {
        return cashTransactionRepository.findLatestTransactionByAccountNumber(accountNumber);
    }

    // 최소 금액 이상 거래 조회
    @Transactional(readOnly = true)
    public List<CashTransaction> getTransactionsByMinAmount(Long accountNumber, BigDecimal minAmount) {
        return cashTransactionRepository.findByAccountNumberAndMinAmount(accountNumber, minAmount);
    }

    // 이체 처리
    @Transactional
    public void processTransfer(Long fromAccountNumber, Long toAccountNumber, BigDecimal amount, String note) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("이체 금액은 0보다 커야 합니다");
        }

        // 출금 계좌에서 출금 처리
        createTransaction(fromAccountNumber, CashTransaction.TransactionType.이체, amount,
                note + " (이체 출금)");

        // 입금 계좌에서 입금 처리
        createTransaction(toAccountNumber, CashTransaction.TransactionType.입금, amount,
                note + " (이체 입금)");
    }
}
