package com.example.KDT_bank_server_project2.manager.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserRegisterDto {
    private String userId;
    private String password;
    private String userName;
    private String userPhone;

    public UserRegisterDto(String userId, String password, String userName, String userPhone) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.userPhone = userPhone;

    }

}
