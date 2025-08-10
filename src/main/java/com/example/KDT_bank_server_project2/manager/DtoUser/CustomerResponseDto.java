package com.example.KDT_bank_server_project2.manager.DtoUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 고객 응답 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class CustomerResponseDto {

    private String id; // 사용자 아이디
    private String name; // 사용자 이름
    private String email; // 사용자 이메일
    private String phone; // 사용자 번호
    private String address; // 거주지
    private String residentNumber; // 주민번호
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomerResponseDto(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.address = customer.getAddress();
        this.residentNumber = customer.getResidentNumber();

        System.out.println("CustomerResponseDto 생성: " + name +
                ", email: " + email );
    }
    // 기존 컨트롤러 호환을 위한 from() 메서드 추가
    public static CustomerResponseDto from(Customer customer) {
        return new CustomerResponseDto(customer);
    }
}
