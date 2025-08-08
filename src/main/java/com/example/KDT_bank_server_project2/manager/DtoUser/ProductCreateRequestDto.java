package com.example.KDT_bank_server_project2.manager.DtoUser;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

// 상품 생성 요청 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class ProductCreateRequestDto {

    @NotBlank(message = "상품명은 필수입니다")
    private String productName;

    private String productDetail;

    @NotBlank(message = "카테고리는 필수입니다")
    private String category;

    @NotBlank(message = "상품 카테고리는 필수입니다")
    private String productCategory;

    private BigDecimal maxRate;
    private BigDecimal minRate;
    private BigDecimal limitMoney;
}
