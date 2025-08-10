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

    @GetMapping("/{name}/{number}")
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> getCustomerByNameAndResidentNumber(@PathVariable String name, @PathVariable String number) {
        Customer customer = customerService.getCustomerByNameAndResidentNumber(name, number);
        CustomerResponseDto dto = new CustomerResponseDto(customer);

        return ResponseEntity.ok(ApiResponseUser.success("정보조회 성공",dto));
    }
    

    // ID로 고객 조회 (상세 정보) 이름, 주민번호
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> getCustomerById(@PathVariable String id) {
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
    public ResponseEntity<ApiResponseUser<CustomerResponseDto>> updateCustomer(@PathVariable String id, @Valid @RequestBody CustomerUpdateRequestDto requestDto) {
        try {
            Customer updateData = convertToUpdateEntity(requestDto);
            Customer updatedCustomer = customerService.updateCustomer(id, updateData);
            CustomerResponseDto responseDto = new CustomerResponseDto(updatedCustomer);

            return ResponseEntity.ok(ApiResponseUser.success("고객 정보가 수정되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }



    // 고객 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponseUser<List<CustomerResponseDto>>> searchCustomers(@RequestParam String keyword) {
        List<Customer> customers = customerService.searchCustomers(keyword);
        List<CustomerResponseDto> responseDtos = customers.stream()
                .map(CustomerResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }



    // 이메일 중복 확인
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponseUser<Boolean>> checkEmailExists(@PathVariable String email) {
        boolean exists = customerService.isEmailExists(email);
        return ResponseEntity.ok(ApiResponseUser.success("이메일 중복 확인 완료", exists));
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
