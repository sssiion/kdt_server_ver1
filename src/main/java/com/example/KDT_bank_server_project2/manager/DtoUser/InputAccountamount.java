package com.example.KDT_bank_server_project2.manager.DtoUser;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor //입금
public class InputAccountamount {
    @NotNull(message = "입금 계좌번호는 필수입니다")
    private String toAccountNumber;

    @NotNull(message = "이체 금액은 필수입니다")
    @DecimalMin(value = "0.01", message = "이체 금액은 0보다 커야 합니다")
    private String amount;
}
