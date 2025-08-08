package com.example.KDT_bank_server_project2.manager.Repository;


import com.example.KDT_bank_server_project2.manager.EntityUser.BankEmployee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankEmployeeRepository extends JpaRepository<BankEmployee, String> {

    Optional<BankEmployee> findByEmail(String email);
    // 이메일로 직원 찾기

    boolean existsByEmail(String email);
    // 이메일 중복 확인

    List<BankEmployee> findByDepartment(String department);
    // 부서별 직원 조회

    List<BankEmployee> findByRole(BankEmployee.EmployeeRole role);
    // 역할별 직원 조회

    List<BankEmployee> findByStatus(BankEmployee.EmployeeStatus status);
    // 상태별 직원 조회

    List<BankEmployee> findByNameContaining(String name);
    // 이름으로 직원 검색 (부분 일치)

    List<BankEmployee> findByDepartmentAndStatus(String department, BankEmployee.EmployeeStatus status);
    // 부서 및 상태별 직원 조회

    List<BankEmployee> findAllByOrderByCreatedAtDesc();
    // 최신 입사순으로 전체 직원 조회

    @Query("SELECT be FROM BankEmployee be WHERE be.department = :department AND be.role = :role AND be.status = 'ACTIVE'")
    List<BankEmployee> findActiveEmployeesByDepartmentAndRole(@Param("department") String department, @Param("role") BankEmployee.EmployeeRole role);
    // 부서 및 역할별 활성 직원 조회

    @Query("SELECT be FROM BankEmployee be WHERE be.name LIKE %:keyword% OR be.department LIKE %:keyword%")
    List<BankEmployee> searchEmployees(@Param("keyword") String keyword);
    // 통합 검색 (이름, 부서)
}