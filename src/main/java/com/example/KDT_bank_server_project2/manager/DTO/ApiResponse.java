package com.example.KDT_bank_server_project2.manager.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T>{
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, message, data);
    }
    public static <T> ApiResponse<T> failure(String message){
        return new ApiResponse<>(false, message, null);
    }
}
