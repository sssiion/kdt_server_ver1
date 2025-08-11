package com.example.KDT_bank_server_project2.manager.DtoUser;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

// 이체 요청 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class TransferRequestDto {

    private String fromAccountNumber;
    private String toAccountNumber;

    @NotNull(message = "이체 금액은 필수입니다")
    private String amount;

}
