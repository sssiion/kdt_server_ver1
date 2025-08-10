package com.example.KDT_bank_server_project2.manager.DtoUser;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// 고객 생성 요청 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class CustomerCreateRequestDto { //customer 생성

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @NotBlank(message = "전화번호는 필수입니다")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다")
    private String phone;

    @NotBlank(message = "주민번호는 필수입니다")
    @Pattern(regexp = "^\\d{6}-\\d{7}$", message = "주민번호 형식이 올바르지 않습니다")
    private String residentNumber;

    private String address;
}
