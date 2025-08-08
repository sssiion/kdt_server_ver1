package com.example.KDT_bank_server_project2.manager.DtoUser;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



// 대출 서류 업로드 요청 DTO
@Data
@Getter
@Setter
@AllArgsConstructor
public class LoanDocsUploadRequestDto {

    @NotNull(message = "대출 신청 ID는 필수입니다")
    private String applicationId;

    private String fileType;
    private String description;
}
