package com.example.KDT_bank_server_project2.manager.Repository;

import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findAll();

    Optional<Customer> findByEmail(String email);
    // 이메일로 고객 찾기

    Optional<Customer> findByResidentNumber(String residentNumber);
    // 주민번호로 고객 찾기

    Customer findByResidentNumberAndName(String name, String residentNumber);

    boolean existsByEmail(String email);
    // 이메일 중복 확인

    boolean existsByResidentNumber(String residentNumber);
    // 주민번호 중복 확인

    List<Customer> findByNameContaining(String name);
    // 이름으로 고객 검색 (부분 일치)


    List<Customer> findByPhoneContaining(String phone);
    // 전화번호로 고객 검색 (부분 일치)



    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:keyword% OR c.email LIKE %:keyword% OR c.phone LIKE %:keyword%")
    List<Customer> searchCustomers(@Param("keyword") String keyword);
    // 통합 검색 (이름, 이메일, 전화번호)

}
