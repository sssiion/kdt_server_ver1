package com.example.KDT_bank_server_project2.manager.DtoUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// 고객 수정 요청 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class CustomerUpdateRequestDto {
    private String name;
    private String phone;
    private String address;
}
