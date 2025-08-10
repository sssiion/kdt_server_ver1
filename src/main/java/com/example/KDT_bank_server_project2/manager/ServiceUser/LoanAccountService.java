package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.DtoUser.LoanAccountCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.LoanAccountResponseDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.LoanAccount;
import com.example.KDT_bank_server_project2.manager.Repository.LoanAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanAccountService {


    private final LoanAccountRepository loanAccountRepository;
    private final AccountNumberService accountNumberService;

   //대출 정보 변경
   public LoanAccountResponseDto changeLoanAccount(LoanAccountCreateRequestDto loanAccount){
       LoanAccount account = getLoanAccountById(loanAccount.getCustomerId()).get();
       account.setMaturityDate(loanAccount.getMaturityDate());
       account.setInterestRate(loanAccount.getInterestRate());
       loanAccountRepository.save(account);
       return new LoanAccountResponseDto(account);
   }

    // 대출 계좌 생성
    public LoanAccount createLoanAccount(LoanAccount loanAccount) {
        // 대출 ID가 없으면 고유한 번호 생성
        if (loanAccount.getLoanId() == null || loanAccount.getLoanId().isEmpty()) {
            loanAccount.setLoanId(accountNumberService.generateUniqueLoanId());
        }

        if (loanAccount.getLoanDate() == null) {
            loanAccount.setLoanDate(LocalDate.now());
        }

        // 대출 금액 검증
        if (loanAccount.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("대출 금액은 0보다 커야 합니다");
        }

        return loanAccountRepository.save(loanAccount);
    }

    // 모든 대출 계좌 조회
    @Transactional(readOnly = true)
    public List<LoanAccount> getAllLoanAccounts() {
        return loanAccountRepository.findAll();
    }

    // 대출 ID로 대출 계좌 조회
    @Transactional(readOnly = true)
    public Optional<LoanAccount> getLoanAccountById(String loanId) {
        return loanAccountRepository.findByLoanId(loanId);
    }

    // 고객별 대출 계좌 조회
    @Transactional(readOnly = true)
    public List<LoanAccount> getLoanAccountsByCustomerId(String customerId) {
        return loanAccountRepository.findByCustomerIdOrderByLoanDateDesc(customerId);
    }



    // 대출 잔액 조회
    @Transactional(readOnly = true)
    public BigDecimal getLoanBalance(String loanId) {
        LoanAccount loanAccount = loanAccountRepository.findByLoanId(loanId)
                .orElseThrow(() -> new RuntimeException("대출 계좌를 찾을 수 없습니다: " + loanId));

        return loanAccount.getTotalAmount().subtract(loanAccount.getRepaymentAmount());
    }

    // 대출 상환
    public LoanAccount makeRepayment(String loanId, BigDecimal repaymentAmount) {
        if (repaymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("상환 금액은 0보다 커야 합니다");
        }

        LoanAccount loanAccount = loanAccountRepository.findByLoanId(loanId)
                .orElseThrow(() -> new RuntimeException("대출 계좌를 찾을 수 없습니다: " + loanId));

        BigDecimal currentRepayment = loanAccount.getRepaymentAmount();
        BigDecimal newRepayment = currentRepayment.add(repaymentAmount);

        // 상환 금액이 총 대출 금액을 초과하는지 확인
        if (newRepayment.compareTo(loanAccount.getTotalAmount()) > 0) {
            throw new RuntimeException("상환 금액이 대출 잔액을 초과합니다");
        }

        loanAccount.setRepaymentAmount(newRepayment);



        return loanAccountRepository.save(loanAccount);
    }



    // 고객의 총 대출 잔액 조회
    @Transactional(readOnly = true)
    public BigDecimal getTotalLoanBalanceByCustomerId(String customerId) {
        BigDecimal totalBalance = loanAccountRepository.getTotalLoanBalanceByCustomerId(customerId);
        return totalBalance != null ? totalBalance : BigDecimal.ZERO;
    }


    // 만기 임박 대출 조회
    @Transactional(readOnly = true)
    public List<LoanAccount> getLoansNearingMaturity(LocalDate date) {
        return loanAccountRepository.findLoansNearingMaturity(date);
    }

    // 상품별 대출 계좌 조회
    @Transactional(readOnly = true)
    public List<LoanAccount> getLoanAccountsByProductName(String productName) {
        return loanAccountRepository.findByProductName(productName);
    }

    // 잔액이 있는 대출 조회
    @Transactional(readOnly = true)
    public List<LoanAccount> getActiveLoansWithBalance() {
        return loanAccountRepository.findActiveLoansWithBalance();
    }
}
