package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.EntityUser.BankEmployee;
import com.example.KDT_bank_server_project2.manager.Repository.BankEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BankEmployeeService {

    @Autowired
    private BankEmployeeRepository bankEmployeeRepository;

    // 직원 생성
    public BankEmployee createEmployee(BankEmployee employee) {
        if (bankEmployeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + employee.getEmail());
        }
        return bankEmployeeRepository.save(employee);
    }

    // 모든 직원 조회
    @Transactional(readOnly = true)
    public List<BankEmployee> getAllEmployees() {
        return bankEmployeeRepository.findAllByOrderByCreatedAtDesc();
    }

    // ID로 직원 조회
    @Transactional(readOnly = true)
    public Optional<BankEmployee> getEmployeeById(String id) {
        return bankEmployeeRepository.findById(id);
    }

    // 이메일로 직원 조회
    @Transactional(readOnly = true)
    public Optional<BankEmployee> getEmployeeByEmail(String email) {
        return bankEmployeeRepository.findByEmail(email);
    }

    // 직원 정보 수정
    public BankEmployee updateEmployee(String id, BankEmployee updatedEmployee) {
        BankEmployee existingEmployee = bankEmployeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다: " + id));

        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setRole(updatedEmployee.getRole());
        existingEmployee.setPhone(updatedEmployee.getPhone());

        return bankEmployeeRepository.save(existingEmployee);
    }

    // 직원 상태 변경
    public BankEmployee updateEmployeeStatus(String id, BankEmployee.EmployeeStatus status) {
        BankEmployee employee = bankEmployeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다: " + id));

        employee.setStatus(status);
        return bankEmployeeRepository.save(employee);
    }

    // 부서별 직원 조회
    @Transactional(readOnly = true)
    public List<BankEmployee> getEmployeesByDepartment(String department) {
        return bankEmployeeRepository.findByDepartment(department);
    }

    // 역할별 직원 조회
    @Transactional(readOnly = true)
    public List<BankEmployee> getEmployeesByRole(BankEmployee.EmployeeRole role) {
        return bankEmployeeRepository.findByRole(role);
    }

    // 활성 직원 조회
    @Transactional(readOnly = true)
    public List<BankEmployee> getActiveEmployees() {
        return bankEmployeeRepository.findByStatus(BankEmployee.EmployeeStatus.ACTIVE);
    }

    // 직원 검색
    @Transactional(readOnly = true)
    public List<BankEmployee> searchEmployees(String keyword) {
        return bankEmployeeRepository.searchEmployees(keyword);
    }

    // 비밀번호 변경
    public void changePassword(String id, String newPassword) {
        BankEmployee employee = bankEmployeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다: " + id));

        employee.setPassword(newPassword); // 실제 환경에서는 암호화 필요
        bankEmployeeRepository.save(employee);
    }
}

