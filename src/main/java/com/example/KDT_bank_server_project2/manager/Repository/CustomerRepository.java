package com.example.KDT_bank_server_project2.manager.Repository;

import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    // 이메일로 고객 찾기

    Optional<Customer> findByResidentNumber(String residentNumber);
    // 주민번호로 고객 찾기

    boolean existsByEmail(String email);
    // 이메일 중복 확인

    boolean existsByResidentNumber(String residentNumber);
    // 주민번호 중복 확인

    List<Customer> findByNameContaining(String name);
    // 이름으로 고객 검색 (부분 일치)

    List<Customer> findByStatus(Customer.CustomerStatus status);
    // 상태별 고객 조회

    List<Customer> findByPhoneContaining(String phone);
    // 전화번호로 고객 검색 (부분 일치)

    List<Customer> findAllByOrderByCreatedAtDesc();
    // 최신 가입순으로 전체 고객 조회

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:keyword% OR c.email LIKE %:keyword% OR c.phone LIKE %:keyword%")
    List<Customer> searchCustomers(@Param("keyword") String keyword);
    // 통합 검색 (이름, 이메일, 전화번호)

    @Query("SELECT c FROM Customer c WHERE c.status = :status ORDER BY c.createdAt DESC")
    List<Customer> findByStatusOrderByCreatedAtDesc(@Param("status") Customer.CustomerStatus status, Pageable pageable);
    // 상태별 최신순 조회 (페이지네이션)
}
