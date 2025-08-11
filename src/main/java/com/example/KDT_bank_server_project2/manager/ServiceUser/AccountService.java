package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.ControllerUser.CashTransactionController;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionResponseDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.TransferRequestDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
import com.example.KDT_bank_server_project2.manager.EntityUser.CashTransaction;
import com.example.KDT_bank_server_project2.manager.Repository.AccountRepository;
import com.example.KDT_bank_server_project2.manager.Repository.CashTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {


    private final AccountRepository accountRepository;
    private final CashTransactionService cashTransactionService; //
    private final AccountNumberService accountNumberService;

    // 계좌 생성
    public Account createAccount(Account account) {
        if (account.getAccountNumber() == null) {
            account.setAccountNumber(accountNumberService.generateLoanIdWithStringBuilder().trim());
        }
        if (account.getOpeningDate() == null) {
            account.setOpeningDate(LocalDate.now());
        }
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // 계좌번호로 단일 계좌 조회(필수: 없으면 예외)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber.trim())
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));


    }


    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(String accountNumber) {
        return getAccountByNumber(accountNumber).getAmount();
    }

    public Account updateAccountBalance(String accountNumber, BigDecimal newBalance) {
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("잔액은 음수가 될 수 없습니다");
        }
        Account account = getAccountByNumber(accountNumber);
        account.setAmount(newBalance);
        return accountRepository.save(account);
    }

    /** ✅ 입금 로직 수정 */
    public CashTransactionResponseDto deposit(String accountNumber, String amountStr) {
        BigDecimal amount = new BigDecimal(amountStr);
        System.out.println("입금값: "+amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("입금액은 0보다 커야 합니다");
        }

        // 1. 단일 계좌 조회
        Account account = getAccountByNumber(accountNumber.trim());
        System.out.println("입금로직에서 계좌번호 :"+account.getAccountNumber());
        // 2. 새 잔액 계산
        BigDecimal newBalance = account.getAmount().add(amount);
        account.setAmount(newBalance);
        accountRepository.saveAndFlush(account);

        // 3. 거래 내역 저장
        CashTransaction tx = cashTransactionService.createTransaction(
                account.getAccountNumber(), "", CashTransaction.TransactionType.입금, amount
        );
        
        return new CashTransactionResponseDto(tx);
    }

    /** ✅ 출금 로직 수정 */
    public CashTransactionResponseDto withdraw(String accountNumber, String amountStr) {
        BigDecimal amount = new BigDecimal(amountStr);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("출금액은 0보다 커야 합니다");
        }

        // 1. 단일 계좌 조회
        Account account = getAccountByNumber(accountNumber.trim());
        System.out.println("출금로직 :"+account);
        // 2. 잔액 부족 체크
        if (account.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("잔액이 부족합니다");
        }

        // 3. 새 잔액 계산 (🚀 변경: 출금은 빼기)
        BigDecimal newBalance = account.getAmount().subtract(amount);
        account.setAmount(newBalance);
        accountRepository.saveAndFlush(account);

        // 4. 거래 내역 저장
        CashTransaction tx = cashTransactionService.createTransaction(
                account.getAccountNumber(), "", CashTransaction.TransactionType.출금, amount
        );

        return new CashTransactionResponseDto(tx);
    }

    /** ✅ 송금 로직 수정 */
    public void remittance(TransferRequestDto dto) {
        BigDecimal amount = new BigDecimal(dto.getAmount());

        // 출금 계좌 차감
        withdraw(dto.getFromAccountNumber(), amount.toString());

        // 입금 계좌 증가
        deposit(dto.getToAccountNumber(), amount.toString());
        
    }

    public List<Account> getAccountsByProductName(String productName) {
        return accountRepository.findByProductName(productName);
    }

    public void deleteByAccountNumber(String accountNumber) {
        accountRepository.deleteByAccountNumber(accountNumber);
    }

}
