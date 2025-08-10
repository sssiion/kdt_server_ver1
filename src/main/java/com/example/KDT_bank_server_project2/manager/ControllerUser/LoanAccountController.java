package com.example.KDT_bank_server_project2.manager.ControllerUser;

import com.example.KDT_bank_server_project2.manager.DtoUser.LoanAccountCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.LoanAccountResponseDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.LoanAccount;
import com.example.KDT_bank_server_project2.manager.ServiceUser.LoanAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Transactional
@RestController
@RequestMapping("/api/loan-accounts")
@CrossOrigin(origins = "*")
public class LoanAccountController {


    private final LoanAccountService loanAccountService;

    // 대출 계좌 생성
    @PostMapping
    public ResponseEntity<LoanAccountResponseDto> createLoanAccount(@RequestBody LoanAccountCreateRequestDto loanAccount) {
        try {
            LoanAccount account = new  LoanAccount();
            account.createLoanAccount(loanAccount);
            account  = loanAccountService.createLoanAccount(account);
            LoanAccountResponseDto dto = new LoanAccountResponseDto(account);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    //대출 만기일 변경
    @PostMapping("/expiredate")
    public ResponseEntity<LoanAccountResponseDto> changeExpireDate(@RequestBody LoanAccountCreateRequestDto loanAccount) {
        try{
            LoanAccountResponseDto dto =loanAccountService.changeLoanAccount(loanAccount);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    // 모든 대출 계좌 조회
    @GetMapping
    public ResponseEntity<List<LoanAccount>> getAllLoanAccounts() {
        List<LoanAccount> loanAccounts = loanAccountService.getAllLoanAccounts();
        return ResponseEntity.ok(loanAccounts);
    }

    // 대출 ID로 대출 계좌 조회
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanAccount> getLoanAccountById(@PathVariable String loanId) {
        Optional<LoanAccount> loanAccount = loanAccountService.getLoanAccountById(loanId);
        return loanAccount.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 고객별 대출 계좌 조회
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanAccount>> getLoanAccountsByCustomerId(@PathVariable String customerId) {
        List<LoanAccount> loanAccounts = loanAccountService.getLoanAccountsByCustomerId(customerId);
        return ResponseEntity.ok(loanAccounts);
    }



    // 대출 잔액 조회
    @GetMapping("/{loanId}/balance")
    public ResponseEntity<BigDecimal> getLoanBalance(@PathVariable String loanId) {
        try {
            BigDecimal balance = loanAccountService.getLoanBalance(loanId);
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 대출 상환
    @PostMapping("/{loanId}/repayment")
    public ResponseEntity<LoanAccount> makeRepayment(@PathVariable String loanId,
                                                     @RequestParam BigDecimal repaymentAmount) {
        try {
            LoanAccount updatedLoanAccount = loanAccountService.makeRepayment(loanId, repaymentAmount);
            return ResponseEntity.ok(updatedLoanAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    // 고객의 총 대출 잔액 조회
    @GetMapping("/customer/{customerId}/total-balance")
    public ResponseEntity<BigDecimal> getTotalLoanBalanceByCustomerId(@PathVariable String customerId) {
        BigDecimal totalBalance = loanAccountService.getTotalLoanBalanceByCustomerId(customerId);
        return ResponseEntity.ok(totalBalance);
    }



    // 만기 임박 대출 조회
    @GetMapping("/near-maturity")
    public ResponseEntity<List<LoanAccount>> getLoansNearingMaturity(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LoanAccount> loanAccounts = loanAccountService.getLoansNearingMaturity(date);
        return ResponseEntity.ok(loanAccounts);
    }

    // 상품별 대출 계좌 조회
    @GetMapping("/product/{productName}")
    public ResponseEntity<List<LoanAccount>> getLoanAccountsByProductName(@PathVariable String productName) {
        List<LoanAccount> loanAccounts = loanAccountService.getLoanAccountsByProductName(productName);
        return ResponseEntity.ok(loanAccounts);
    }

}

