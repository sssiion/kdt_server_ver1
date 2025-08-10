package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import com.example.KDT_bank_server_project2.manager.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {


    private final CustomerRepository customerRepository;

    // 고객 생성
    public Customer createCustomer(Customer customer) {
        // 이메일 중복 체크
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + customer.getEmail());
        }
        // 주민번호 중복 체크
        if (customerRepository.existsByResidentNumber(customer.getResidentNumber())) {
            throw new RuntimeException("이미 존재하는 주민번호입니다: " + customer.getResidentNumber());
        }
        return customerRepository.save(customer);
    }

    // 모든 고객 조회
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ID로 고객 조회
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(String id) {
        return customerRepository.findById(id);
    }

    // 이메일로 고객 조회
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    @Transactional
    public Customer getCustomerByNameAndResidentNumber(String name, String number) {

        return customerRepository.findByResidentNumberAndName(name, number);
    }

    // 주민번호로 고객 조회
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByResidentNumber(String residentNumber) {
        return customerRepository.findByResidentNumber(residentNumber);
    }

    // 고객 정보 수정
    public Customer updateCustomer(String id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("고객을 찾을 수 없습니다: " + id));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setAddress(updatedCustomer.getAddress());

        return customerRepository.save(existingCustomer);
    }



    // 고객 검색
    @Transactional(readOnly = true)
    public List<Customer> searchCustomers(String keyword) {
        return customerRepository.searchCustomers(keyword);
    }



    // 이메일 중복 확인
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return customerRepository.existsByEmail(email);
    }

    // 주민번호 중복 확인
    @Transactional(readOnly = true)
    public boolean isResidentNumberExists(String residentNumber) {
        return customerRepository.existsByResidentNumber(residentNumber);
    }
}
