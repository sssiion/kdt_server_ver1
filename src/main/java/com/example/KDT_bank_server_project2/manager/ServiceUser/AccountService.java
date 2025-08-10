package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.ControllerUser.CashTransactionController;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionResponseDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.TransferRequestDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
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
    private final CashTransactionController  cashTransactionController;
    private final AccountNumberService accountNumberService;

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
    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    // 고객별 계좌 조회
    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }



    // 계좌 잔액 조회
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));
        return account.getAmount();
    }

    // 계좌 잔액 업데이트
    public Account updateAccountBalance(String accountNumber, BigDecimal newBalance) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("잔액은 음수가 될 수 없습니다");
        }

        account.setAmount(newBalance);
        return accountRepository.save(account);
    }

    // 입금 > 결과
    public CashTransactionResponseDto deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("입금액은 0보다 커야 합니다");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));

        CashTransactionCreateRequestDto dto = new CashTransactionCreateRequestDto();
        dto.setAmount(amount);
        dto.setAccountNumber(account.getAccountNumber());
        dto.setOtherAccountNumber("");
        dto.setTransactionType("입금");
        CashTransactionResponseDto textdto = cashTransactionController.createTransaction(dto);


        BigDecimal newBalance = account.getAmount().add(amount);
        account.setAmount(newBalance);
        accountRepository.save(account);
        return textdto;
    }

    // 출금
    public CashTransactionResponseDto withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("출금액은 0보다 커야 합니다");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: " + accountNumber));


        if (account.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("잔액이 부족합니다");
        }

        CashTransactionCreateRequestDto dto = new CashTransactionCreateRequestDto();
        dto.setAmount(amount);
        dto.setAccountNumber(account.getAccountNumber());
        dto.setOtherAccountNumber("");
        dto.setTransactionType("출금");
        CashTransactionResponseDto textdto =cashTransactionController.createTransaction(dto); // 여기에 save도 있음.
        BigDecimal newBalance = account.getAmount().subtract(amount);
        account.setAmount(newBalance);
        accountRepository.save(account);
        return textdto;
    }
    //송금
    public CashTransactionResponseDto remittance(TransferRequestDto dto) {
        CashTransactionResponseDto account = deposit(dto.getToAccountNumber(), dto.getAmount());
        CashTransactionResponseDto otheraccount = withdraw(dto.getFromAccountNumber(), dto.getAmount());

        BigDecimal newBalance = account.getAmount().subtract(dto.getAmount());
        account.setAmount(newBalance);
        BigDecimal otherBalance = otheraccount.getAmount().add(dto.getAmount());
        otheraccount.setAmount(otherBalance);
        return account;
    }

    // 상품별 계좌 조회
    @Transactional(readOnly = true)
    public List<Account> getAccountsByProductName(String productName) {
        return accountRepository.findByProductName(productName);
    }


}
