package com.example.KDT_bank_server_project2.manager.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserLoginDto {

    private String userId;
    private String password;

    public UserLoginDto(String userId, String password) {
        this.userId = userId;
        this.password = password;

    }
}
