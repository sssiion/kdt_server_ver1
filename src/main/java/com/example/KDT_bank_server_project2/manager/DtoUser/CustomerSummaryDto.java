package com.example.KDT_bank_server_project2.manager.DtoUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// 고객 요약 DTO (목록용)
@Data
@Getter
@Setter
@AllArgsConstructor
public class CustomerSummaryDto {

    private String id;
    private String name;
    private String email;
    private String phone;
    private Customer.CustomerStatus status;

    public CustomerSummaryDto(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.status = customer.getStatus();
        System.out.println("CustomerSummaryDto 생성: " + name +
                ", email: " + email +
                ", status: " + status);
    }

}
