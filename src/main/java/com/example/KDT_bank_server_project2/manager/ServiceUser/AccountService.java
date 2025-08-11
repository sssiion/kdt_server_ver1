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
    private final CashTransactionController  cashTransactionController;
    private final AccountNumberService accountNumberService;

    // 계좌 생성
    public Account createAccount(Account account) {
        // 계좌번호가 없으면 고유한 번호 생성
        if (account.getAccountNumber() == null) {
            account.setAccountNumber(accountNumberService.generateLoanIdWithStringBuilder());
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

    public Account getAccountByNumber(String accountNumber) {
        System.out.println("[" + accountNumber + "] len=" + accountNumber.length());
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다: [" + accountNumber+"]" +accountNumber.length()));
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
        Account account = getAccountByNumber(accountNumber);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("잔액은 음수가 될 수 없습니다");
        }

        account.setAmount(newBalance);
        return accountRepository.save(account);
    }

    // 입금 > 결과
    public CashTransactionResponseDto deposit(String accountNumber, String amount, String userId) {
        if (amount.equals("0")) {
            throw new RuntimeException("입금액은 0보다 커야 합니다");
        }
        List<Account> account = accountRepository.findByCustomerId(userId);
        Account currentAccout =new Account();
        for(Account acc : account) {
            if(accountNumber.trim().equals(acc.getAccountNumber().trim())){
                currentAccout = acc;
            }
        }


        CashTransactionResponseDto dto = new  CashTransactionResponseDto();
        dto.setAccountNumber(accountNumber);
        dto.setAmount(new BigDecimal(amount));
        dto.setAccountNumber(currentAccout.getAccountNumber());
        dto.setOtherAccountNumber("");
        dto.setTransactionType("입금");
        CashTransactionResponseDto textdto = cashTransactionController.createTransaction(dto);


        BigDecimal newBalance = currentAccout.getAmount().add(new BigDecimal(amount));
        currentAccout.setAmount(newBalance);
        accountRepository.saveAndFlush(currentAccout);
        return textdto;
    }

    // 출금
    public CashTransactionResponseDto withdraw(String accountNumber, String amount, String userId) {
        if (amount.equals("0")) {
            throw new RuntimeException("출금액은 0보다 커야 합니다");
        }

        List<Account> account = accountRepository.findByCustomerId(userId);
        Account currentAccout =new Account();
        for(Account acc : account) {
            if(accountNumber.trim().equals(acc.getAccountNumber().trim())){
                currentAccout = acc;
                System.out.println(acc);
            }
        }


        if (currentAccout.getAmount().compareTo(new BigDecimal(amount)) < 0) {
            throw new RuntimeException("잔액이 부족합니다");
        }

        CashTransactionResponseDto dto = new CashTransactionResponseDto();
        dto.setAmount(new BigDecimal(amount)); // 양 저장
        dto.setAccountNumber(currentAccout.getAccountNumber()); // 내 account 저장
        dto.setOtherAccountNumber(""); // 상대방 account 저장
        dto.setTransactionType("출금"); // 타입
        cashTransactionController.createTransaction(dto); // 여기에 save도 있음.
        BigDecimal newBalance = currentAccout.getAmount().add(new BigDecimal(amount));
        currentAccout.setAmount(newBalance);
        accountRepository.saveAndFlush(currentAccout);
        return dto;
    }
    //송금
    public CashTransactionResponseDto remittance(TransferRequestDto dto,String userId) {
        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(dto.getAmount()));
        CashTransactionResponseDto account = deposit(dto.getToAccountNumber(), amount.toString(),userId);
        CashTransactionResponseDto otheraccount = withdraw(dto.getFromAccountNumber(), amount.toString(), userId);

        BigDecimal newBalance = account.getAmount().subtract(amount);
        account.setAmount(newBalance);
        account.setOtherAccountNumber(otheraccount.getOtherAccountNumber());
        account.setTransactionType("출금");
        BigDecimal otherBalance = otheraccount.getAmount().add(amount);
        otheraccount.setAmount(otherBalance);
        otheraccount.setTransactionType("입금");
        otheraccount.setOtherAccountNumber(dto.getFromAccountNumber());

        return account;
    }

    // 상품별 계좌 조회
    @Transactional(readOnly = true)
    public List<Account> getAccountsByProductName(String productName) {
        return accountRepository.findByProductName(productName);
    }
    //계좌 삭제
    public void deleteByAccountNumber(String accountNumber) {
          accountRepository.deleteByAccountNumber(accountNumber);
    }


}
