package com.example.KDT_bank_server_project2.manager.ControllerUser;



import com.example.KDT_bank_server_project2.manager.DtoUser.*;
import com.example.KDT_bank_server_project2.manager.EntityUser.Account;
import com.example.KDT_bank_server_project2.manager.ServiceUser.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 계좌 생성
    @PostMapping
    public ResponseEntity<ApiResponseUser<AccountResponseDto>> createAccount(@Valid @RequestBody AccountCreateRequestDto requestDto) {
        try {
            Account account = convertToEntity(requestDto);
            Account createdAccount = accountService.createAccount(account);
            AccountResponseDto responseDto = new AccountResponseDto(createdAccount);

            return ResponseEntity.ok(ApiResponseUser.success("계좌가 성공적으로 생성되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 모든 계좌 조회
    @GetMapping
    public ResponseEntity<ApiResponseUser<List<AccountResponseDto>>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountResponseDto> responseDtos = accounts.stream()
                .map(AccountResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }
    //고객 아이디로 고객 모든 계죄 조회
    @GetMapping("/{costomerId}")
    public ResponseEntity<ApiResponseUser<List<AccountResponseDto>>> getAccountById(@PathVariable String costomerId) {
        List<Account> accounts = accountService.getAccountsByCustomerId(costomerId);
        if (accounts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }else{
            List<AccountResponseDto> dto = accounts.stream()
                    .map(AccountResponseDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseUser.success(dto));
        }
    }

    // 계좌번호로 계좌 조회
    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponseUser<AccountResponseDto>> getAccountByNumber(@PathVariable String accountNumber) {
        Optional<Account> account = accountService.getAccountByNumber(accountNumber);
        if (account.isPresent()) {
            AccountResponseDto responseDto = new AccountResponseDto(account.get());
            return ResponseEntity.ok(ApiResponseUser.success(responseDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 고객별 계좌 조회
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponseUser<List<AccountResponseDto>>> getAccountsByCustomerId(@PathVariable String customerId) {
        List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
        List<AccountResponseDto> responseDtos = accounts.stream()
                .map(AccountResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 입금
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponseUser<CashTransactionResponseDto>> deposit( @Valid @RequestBody CashTransactionResponseDto requestDto) {
        try {
            CashTransactionResponseDto updatedAccount = accountService.deposit(requestDto.getAccountNumber(), requestDto.getAmount());

            return ResponseEntity.ok(ApiResponseUser.success("입금이 완료되었습니다.", updatedAccount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }
    //송금
    @PostMapping("/remittance")
    public ResponseEntity<ApiResponseUser<CashTransactionResponseDto>> remittance(@Valid @RequestBody TransferRequestDto requestDto) {
        try{
            CashTransactionResponseDto account = accountService.remittance(requestDto);
            return ResponseEntity.ok(ApiResponseUser.success("출금이 완료되었습니다.", account));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 출금
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponseUser<CashTransactionResponseDto>> withdraw(@PathVariable String accountNumber,
                                                                       @Valid @RequestBody TransferRequestDto requestDto) {
        try {
            CashTransactionResponseDto updatedAccount = accountService.withdraw(accountNumber, requestDto.getAmount());

            return ResponseEntity.ok(ApiResponseUser.success("출금이 완료되었습니다.", updatedAccount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // DTO -> Entity 변환
    private Account convertToEntity(AccountCreateRequestDto dto) {
        Account account = new Account();
        account.setCustomerId(dto.getCustomerId());
        account.setProductName(dto.getProductName());
        account.setAmount(dto.getAmount());
        account.setOpeningDate(dto.getOpeningDate());
        account.setClosingDate(dto.getClosingDate());
        account.setProductType(dto.getProductType());
        return account;
    }
}
