package com.example.KDT_bank_server_project2.manager.ControllerUser;

import com.example.KDT_bank_server_project2.manager.DtoUser.*;
import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
import com.example.KDT_bank_server_project2.manager.ServiceUser.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    /** 계좌 생성 */
    @PostMapping
    public ResponseEntity<ApiResponseUser<AccountResponseDto>> createAccount(
            @Valid @RequestBody AccountCreateRequestDto requestDto) {
        Account account = new Account();
        account.setCustomerId(requestDto.getCustomerId());
        account.setProductName(requestDto.getProductName());
        account.setAmount(requestDto.getAmount());
        account.setProductType(requestDto.getProductType());

        Account created = accountService.createAccount(account);
        return ResponseEntity.ok(ApiResponseUser.success("계좌 생성 성공", new AccountResponseDto(created)));
    }

    /** 모든 계좌 조회 */
    @GetMapping
    public ResponseEntity<ApiResponseUser<List<AccountResponseDto>>> getAllAccounts() {
        List<AccountResponseDto> dtos = accountService.getAllAccounts()
                .stream().map(AccountResponseDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(dtos));
    }

    /** 고객ID로 계좌 조회 */
    @GetMapping("/id/{customerId}")
    public ResponseEntity<ApiResponseUser<List<AccountResponseDto>>> getAccountsByCustomerId(
            @PathVariable String customerId) {
        List<AccountResponseDto> dtos = accountService.getAccountsByCustomerId(customerId)
                .stream().map(AccountResponseDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(dtos));
    }

    /** 계좌번호로 단일 조회 */
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<ApiResponseUser<AccountResponseDto>> getAccountByNumber(
            @PathVariable String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(ApiResponseUser.success(new AccountResponseDto(account)));
    }

    /** 💰 입금 */
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponseUser<CashTransactionResponseDto>> deposit(
            @RequestBody TransferRequestDto requestDto) {
        System.out.println("입금1: "+requestDto);
        CashTransactionResponseDto result = accountService.deposit( requestDto.getToAccountNumber(), requestDto.getAmount());

        return ResponseEntity.ok(ApiResponseUser.success("입금 완료", result));
    }

    /** 💸 출금 */
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponseUser<CashTransactionResponseDto>> withdraw(
            @RequestBody TransferRequestDto requestDto) {
        CashTransactionResponseDto result = accountService.withdraw( requestDto.getFromAccountNumber(), requestDto.getAmount());
        System.out.println("출금1: "+result);
        return ResponseEntity.ok(ApiResponseUser.success("출금 완료", result));
    }

    /** 🔄 송금 */
    @PostMapping("/remittance")
    public ResponseEntity<ApiResponseUser<String>> remittance(
            @RequestBody TransferRequestDto requestDto) {
        accountService.remittance(requestDto);
        return ResponseEntity.ok(ApiResponseUser.success("송금 완료", null));
    }

    /** 🗑 계좌 삭제 */
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<ApiResponseUser<String>> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteByAccountNumber(accountNumber);
        return ResponseEntity.ok(ApiResponseUser.success("계좌 삭제 완료", null));
    }
}
