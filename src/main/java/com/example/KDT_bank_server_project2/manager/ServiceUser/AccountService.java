package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
import com.example.KDT_bank_server_project2.manager.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountNumberService accountNumberService;

    // 계좌 생성
    public Account createAccount(Account account) {
        // 계좌번호가 없으면 고유한 번호 생성
        if (account.getAccountNumber() == null) {
            account.setAccountNumber(accountNumberService.generateUniqueAccountNumber());
        }

        if (account.getOpeningDate() == null) {
            account.setOpeningDate(LocalDate.now());
        }

        return accountRepository.save(account);
    }

    // 모든 계좌 조회
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // 계좌번호로 계좌 조회
    @Transactional(readOnly = true)
    public Optional<Account> getAccountByNumber(Long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    // 고객별 계좌 조회
    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    // 고객의 활성 계좌만 조회
    @Transactional(readOnly = true)
    public List<Account> getActiveAccountsByCustomerId(Long customerId) {
        return accountRepository.findActiveAccountsByCustomerId(customerId);
    }

    // 계좌 잔액 조회
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));
        return account.getAmount();
    }

    // 계좌 잔액 업데이트
    public Account updateAccountBalance(Long accountNumber, BigDecimal newBalance) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("잔액은 음수가 될 수 없습니다");
        }

        account.setAmount(newBalance);
        return accountRepository.save(account);
    }

    // 입금
    public Account deposit(Long accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("입금액은 0보다 커야 합니다");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("비활성 계좌입니다");
        }

        BigDecimal newBalance = account.getAmount().add(amount);
        account.setAmount(newBalance);

        return accountRepository.save(account);
    }

    // 출금
    public Account withdraw(Long accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("출금액은 0보다 커야 합니다");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("비활성 계좌입니다");
        }

        if (account.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("잔액이 부족합니다");
        }

        BigDecimal newBalance = account.getAmount().subtract(amount);
        account.setAmount(newBalance);

        return accountRepository.save(account);
    }

    // 계좌 상태 변경
    public Account updateAccountStatus(Long accountNumber, Account.AccountStatus status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        account.setStatus(status);
        if (status == Account.AccountStatus.CLOSED) {
            account.setClosingDate(LocalDate.now());
        }

        return accountRepository.save(account);
    }

    // 상품별 계좌 조회
    @Transactional(readOnly = true)
    public List<Account> getAccountsByProductName(String productName) {
        return accountRepository.findByProductName(productName);
    }

    // 고객의 활성 계좌 개수 조회
    @Transactional(readOnly = true)
    public long getActiveAccountCountByCustomerId(Long customerId) {
        return accountRepository.countActiveAccountsByCustomerId(customerId);
    }

    // 계좌 해지
    public Account closeAccount(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        if (account.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("잔액이 남아있는 계좌는 해지할 수 없습니다");
        }

        account.setStatus(Account.AccountStatus.CLOSED);
        account.setClosingDate(LocalDate.now());

        return accountRepository.save(account);
    }
}
