package com.example.KDT_bank_server_project2.manager.ControllerUser;


import com.example.KDT_bank_server_project2.manager.DtoUser.*;
import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import com.example.KDT_bank_server_project2.manager.ServiceUser.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 고객 생성
    @PostMapping
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> createCustomer(@Valid @RequestBody CustomerCreateRequestDto requestDto) {
        try {
            Customer customer = convertToEntity(requestDto);
            Customer createdCustomer = customerService.createCustomer(customer);
            CustomerResponseDto responseDto = new CustomerResponseDto(createdCustomer);

            return ResponseEntity.ok(ApiResponseUser.success("고객이 성공적으로 생성되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 모든 고객 조회 (요약 정보)
    @GetMapping
    public ResponseEntity<ApiResponseUser<List<CustomerSummaryDto>>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerSummaryDto> responseDtos = customers.stream()
                .map(CustomerSummaryDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // ID로 고객 조회 (상세 정보)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            CustomerResponseDto responseDto = new CustomerResponseDto(customer.get());
            return ResponseEntity.ok(ApiResponseUser.success(responseDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 이메일로 고객 조회
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        if (customer.isPresent()) {
            CustomerResponseDto responseDto = new CustomerResponseDto(customer.get());
            return ResponseEntity.ok(ApiResponseUser.success(responseDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 고객 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequestDto requestDto) {
        try {
            Customer updateData = convertToUpdateEntity(requestDto);
            Customer updatedCustomer = customerService.updateCustomer(id, updateData);
            CustomerResponseDto responseDto = new CustomerResponseDto(updatedCustomer);

            return ResponseEntity.ok(ApiResponseUser.success("고객 정보가 수정되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 고객 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> updateCustomerStatus(@PathVariable Long id,
                                                                                    @RequestParam Customer.CustomerStatus status) {
        try {
            Customer updatedCustomer = customerService.updateCustomerStatus(id, status);
            CustomerResponseDto responseDto = new CustomerResponseDto(updatedCustomer);
            return ResponseEntity.ok(ApiResponseUser.success("고객 상태가 변경되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 고객 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponseUser<List<CustomerSummaryDto>>> searchCustomers(@RequestParam String keyword) {
        List<Customer> customers = customerService.searchCustomers(keyword);
        List<CustomerSummaryDto> responseDtos = customers.stream()
                .map(CustomerSummaryDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 상태별 고객 조회
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseUser<List<CustomerSummaryDto>>> getCustomersByStatus(@PathVariable Customer.CustomerStatus status) {
        List<Customer> customers = customerService.getCustomersByStatus(status);
        List<CustomerSummaryDto> responseDtos = customers.stream()
                .map(CustomerSummaryDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 이메일 중복 확인
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponseUser<Boolean>> checkEmailExists(@PathVariable String email) {
        boolean exists = customerService.isEmailExists(email);
        return ResponseEntity.ok(ApiResponseUser.success("이메일 중복 확인 완료", exists));
    }

    // 고객 삭제 (비활성화)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseUser<Void>> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok(ApiResponseUser.success("고객이 비활성화되었습니다.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // DTO -> Entity 변환 메서드
    private Customer convertToEntity(CustomerCreateRequestDto dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(dto.getPassword());
        customer.setPhone(dto.getPhone());
        customer.setResidentNumber(dto.getResidentNumber());
        customer.setAddress(dto.getAddress());
        return customer;
    }

    private Customer convertToUpdateEntity(CustomerUpdateRequestDto dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        return customer;
    }
}
