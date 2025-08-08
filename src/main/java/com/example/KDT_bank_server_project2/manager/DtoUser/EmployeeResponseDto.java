package com.example.KDT_bank_server_project2.manager.DtoUser;

import com.example.KDT_bank_server_project2.manager.EntityUser.BankEmployee;
import com.example.KDT_bank_server_project2.manager.EntityUser.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 직원 응답 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class EmployeeResponseDto {

    private String employeeId;
    private String name;
    private String email;
    private String department;
    private String role;
    private String phone;
    private String status;
    private LocalDateTime createdAt;

    public EmployeeResponseDto(BankEmployee employee) {
        this.employeeId = employee.getEmployeeId();
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.department = employee.getDepartment();
        this.role = employee.getRole().toString();
        this.phone = employee.getPhone();
        this.status = employee.getStatus().toString();
        this.createdAt = employee.getCreatedAt();
        System.out.println("EmployeeResponseDto 생성: " + name +
                ", department: " + department +
                ", role: " + role +
                ", status: " + status);
    }
    public static EmployeeResponseDto from(BankEmployee customer) {
        return new EmployeeResponseDto(customer);
    }
}
