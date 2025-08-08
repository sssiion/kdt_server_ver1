package com.example.KDT_bank_server_project2.manager.Controller;

import com.example.KDT_bank_server_project2.manager.Entity.User;
import com.example.KDT_bank_server_project2.manager.EntityUser.BankEmployee;
import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import com.example.KDT_bank_server_project2.manager.EntityUser.Product;
import com.example.KDT_bank_server_project2.manager.Repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
        };
    }
    @Bean
    public ApplicationRunner addInitialData(
            CustomerRepository customerRepository,
            BankEmployeeRepository bankEmployeeRepository,
            ProductRepository productRepository,
            AccountRepository accountRepository) {
        return args -> {



            // 초기 고객 데이터
            if (customerRepository.findByEmail("test@example.com").isEmpty()) {
                Customer customer = new Customer();
                customer.setName("홍길동");
                customer.setEmail("test@example.com");
                customer.setPassword("password123");
                customer.setPhone("010-1111-2222");
                customer.setResidentNumber("900101-1234567");
                customer.setAddress("서울시 강남구");
                customerRepository.save(customer);
            }
        };
    }
}
