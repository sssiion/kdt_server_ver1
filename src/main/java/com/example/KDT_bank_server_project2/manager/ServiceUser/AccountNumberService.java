package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.Repository.AccountRepository;
import com.example.KDT_bank_server_project2.manager.Repository.LoanAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountNumberService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanAccountRepository loanAccountRepository;

    /**
     * Java 기본 기능으로 13자리 계좌번호 생성 (가장 안전함)
     */
    public String generateUniqueAccountNumber() {
        Long accountNumber;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            // 13자리 숫자 생성 (1000000000000 ~ 9999999999999)
            accountNumber = ThreadLocalRandom.current().nextLong(1_000_000_000_000L, 10_000_000_000_000L);
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("계좌번호 생성 실패: 최대 시도 횟수 초과");
            }
        } while (accountRepository.existsByAccountNumber(accountNumber.toString()));

        return accountNumber.toString();
    }

    /**
     * Java 기본 기능으로 13자리 대출ID 생성
     */
    public String generateUniqueLoanId() {
        String loanId;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            // 13자리 숫자를 문자열로 생성
            Long randomNumber = ThreadLocalRandom.current().nextLong(1_000_000_000_000L, 10_000_000_000_000L);
            loanId = String.valueOf(randomNumber);
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("대출ID 생성 실패: 최대 시도 횟수 초과");
            }
        } while (loanAccountRepository.existsByLoanId(loanId));

        return loanId;
    }

    /**
     * StringBuilder를 사용한 방법 (더 세밀한 제어)
     */
    public String generateLoanIdWithStringBuilder() {
        String loanId;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            StringBuilder sb = new StringBuilder();

            // 첫자리는 1-9
            sb.append(ThreadLocalRandom.current().nextInt(1, 10));

            // 나머지 12자리는 0-9
            for (int i = 0; i < 12; i++) {
                sb.append(ThreadLocalRandom.current().nextInt(0, 10));
            }

            loanId = sb.toString();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("대출ID 생성 실패: 최대 시도 횟수 초과");
            }
        } while (loanAccountRepository.existsByLoanId(loanId));

        return loanId;
    }
}
