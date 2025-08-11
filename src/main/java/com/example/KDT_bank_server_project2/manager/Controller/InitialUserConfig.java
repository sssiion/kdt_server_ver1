package com.example.KDT_bank_server_project2.manager.Controller;

import com.example.KDT_bank_server_project2.manager.DtoUser.AccountResponseDto;
import com.example.KDT_bank_server_project2.manager.Entity.User;
import com.example.KDT_bank_server_project2.manager.EntityUser.*;
import com.example.KDT_bank_server_project2.manager.Repository.*;
import com.example.KDT_bank_server_project2.manager.ServiceUser.AccountNumberService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@Transactional
public class InitialUserConfig {
    @Bean
    public ApplicationRunner addAdminUser(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUserId("root").isEmpty()) {
                User admin = new User();
                admin.setUserId("root");
                admin.setUser_authority(User.UserType.BM);
                admin.setUserName("관리자");
                admin.setUserPhone("000-0000-0000");
                admin.setPassword("1234"); // 필요시 암호화
                admin.setCreateAt(java.time.LocalDateTime.now());
                admin.setOffline(); // 또는 setOnline() 원할 경우

                User client = new User();
                client.setUserId("client");
                client.setUser_authority(User.UserType.BT);
                client.setUserName("client");
                client.setUserPhone("000-0001-0000");
                client.setPassword("1234");
                client.setCreateAt(java.time.LocalDateTime.now());
                client.setOffline();
                userRepository.save(client);

                userRepository.save(admin);
            }
            if (userRepository.findByUserId("user").isEmpty()){
                User user = new User();
                user.setUserId("user");
                user.setUser_authority(User.UserType.BT);
                user.setUserName("user");
                user.setUserPhone("000-0002-0000");
                user.setPassword("1234");
                user.setCreateAt(java.time.LocalDateTime.now());
                user.setOffline();
                userRepository.save(user);
            }
        };
    }
    /**
     * 기본 고객, 상품, 계좌, 거래, 대출, 약정 샘플 데이터
     */
    @Bean
    public ApplicationRunner addInitialData(
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            AccountRepository accountRepository,
            CashTransactionRepository cashTransactionRepository,
            LoanAccountRepository loanAccountRepository,
            LoanApplicationRepository loanApplicationRepository,
            AgreementRepository agreementRepository,
            AccountNumberService accountNumberService) {
        return args -> {

            // 1. 고객
            var customerOpt = customerRepository.findByResidentNumber("900101-1234567");
            var customer = customerOpt.orElseGet(() -> {
                Customer cust = new Customer();
                cust.setName("홍길동");
                cust.setEmail("test@example.com");
                cust.setPassword("password123");
                cust.setPhone("010-1111-2222");
                cust.setResidentNumber("900101-1234567");
                cust.setAddress("서울시 강남구");
                return customerRepository.save(cust);

            });

            if (productRepository.findByProductName("정기예금A").isEmpty()) {
                Product product = new Product();
                product.setProductName("정기예금A");
                product.setProductDetail("1년 만기 정기예금 상품");
                product.setCategory("jegeum");
                product.setProductCategory("적금");
                product.setMaxRate(BigDecimal.valueOf(3.5));
                product.setMinRate(BigDecimal.valueOf(2.8));
                product.setLimitMoney(BigDecimal.valueOf(100_000_000));
                productRepository.save(product);
                // 3. 계좌
                Account account = new Account();
                account.setAccountNumber(accountNumberService.generateUniqueAccountNumber());
                account.setCustomerId(customer.getId());
                account.setProductName(product.getProductName());
                account.setAmount(BigDecimal.valueOf(1_000_000));
                account.setOpeningDate(LocalDate.now());
                account.setProductType("적금");
                accountRepository.save(account);
                // 4. 거래 내역
                CashTransaction tx = new CashTransaction();
                tx.setAccountNumber(account.getAccountNumber());
                tx.setAmount(BigDecimal.valueOf(500_000));
                tx.setTransactionType(CashTransaction.TransactionType.입금);
                cashTransactionRepository.save(tx);
                // 7. 약정
                Agreement agreement = new Agreement();
                agreement.setAgreementId("AG-00001");
                agreement.setCustomerId(customer.getId());
                agreement.setProductName(product.getProductName());
                agreement.setAgreementDate(LocalDate.now());
                agreement.setExpirationDate(LocalDate.now().plusYears(1));
                agreement.setStatus(Agreement.AgreementStatus.ACTIVE);
                agreementRepository.save(agreement);
            }





            // 5. 대출 계좌
            LoanAccount loanAccount = new LoanAccount();
            loanAccount.setLoanId(accountNumberService.generateUniqueAccountNumber());
            loanAccount.setCustomerId(customer.getId());
            loanAccount.setProductName("주택담보대출");
            loanAccount.setTotalAmount(BigDecimal.valueOf(50_000_000));
            loanAccount.setInterestRate(BigDecimal.valueOf(4.5));
            loanAccount.setLoanDate(LocalDate.now());
            loanAccount.setMaturityDate(LocalDate.now().plusYears(5));
            loanAccountRepository.save(loanAccount);

            // 6. 대출 신청
            LoanApplication application = new LoanApplication();
            application.setCustomerId(customer.getId());
            application.setProductName("주택담보대출");
            application.setRequestedAmount(BigDecimal.valueOf(10_000_000));
            application.setStatus(LoanApplication.ApplicationStatus.PENDING);
            loanApplicationRepository.save(application);



            System.out.println("✅ 샘플 고객/상품/계좌/거래/대출/약정 데이터 생성됨");
        };
    }
}
