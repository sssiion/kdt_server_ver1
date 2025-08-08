package com.example.KDT_bank_server_project2.manager.Repository;


import com.example.KDT_bank_server_project2.manager.EntityUser.Agreement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {

    List<Agreement> findByCustomerId(Long customerId);
    // 고객 ID로 약정 조회

    List<Agreement> findByCustomerIdAndStatus(Long customerId, Agreement.AgreementStatus status);
    // 고객 ID 및 상태별 약정 조회

    List<Agreement> findByProductName(String productName);
    // 상품명으로 약정 조회

    List<Agreement> findByStatus(Agreement.AgreementStatus status);
    // 상태별 약정 조회

    List<Agreement> findByExpirationDateBefore(LocalDate date);
    // 만료일이 특정 날짜 이전인 약정 조회

    List<Agreement> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);
    // 만료일 기간으로 약정 조회

    List<Agreement> findByCustomerIdOrderByAgreementDateDesc(Long customerId);
    // 고객의 약정을 최신 체결일순으로 조회

    List<Agreement> findByAgreementDateBetween(LocalDate startDate, LocalDate endDate);
    // 약정 체결일 기간으로 조회

    @Query("SELECT a FROM Agreement a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    List<Agreement> findActiveAgreementsByCustomerId(@Param("customerId") Long customerId);
    // 고객의 활성 약정만 조회

    @Query("SELECT COUNT(a) FROM Agreement a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    long countActiveAgreementsByCustomerId(@Param("customerId") Long customerId);
    // 고객의 활성 약정 개수

    @Query("SELECT a FROM Agreement a WHERE a.expirationDate <= :date AND a.status = 'ACTIVE' ORDER BY a.expirationDate ASC")
    List<Agreement> findAgreementsNearingExpiry(@Param("date") LocalDate date);
    // 만료가 임박한 약정 조회

    @Query("SELECT a FROM Agreement a WHERE a.productName = :productName AND a.status = 'ACTIVE' ORDER BY a.agreementDate DESC")
    List<Agreement> findActiveAgreementsByProductName(@Param("productName") String productName);
    // 상품별 활성 약정 조회
}