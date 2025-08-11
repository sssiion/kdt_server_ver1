package com.example.KDT_bank_server_project2.manager.ControllerUser;

import com.example.KDT_bank_server_project2.manager.DtoUser.ApiResponseUser;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionResponseDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.TransferRequestDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.CashTransaction;
import com.example.KDT_bank_server_project2.manager.ServiceUser.CashTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CashTransactionController {

    private final CashTransactionService cashTransactionService;


    // 거래 기록 생성 (입금/출금/이체)
    public CashTransactionResponseDto createTransaction(CashTransactionResponseDto requestDto) {
        try {
            CashTransaction transaction = cashTransactionService.createTransaction(
                    requestDto.getAccountNumber(),
                    requestDto.getOtherAccountNumber(),
                    CashTransaction.TransactionType.valueOf(requestDto.getTransactionType()),
                    requestDto.getAmount()
            );

            return  new CashTransactionResponseDto(transaction);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // 계좌별 거래 내역 조회
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<ApiResponseUser<List<CashTransactionResponseDto>>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<CashTransaction> transactions = cashTransactionService.getTransactionsByAccountNumber(accountNumber);
        List<CashTransactionResponseDto> responseDtos = transactions.stream()
                .map(CashTransactionResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }



    // 거래 유형별 거래 내역 조회
    @GetMapping("/type/{transactionType}")
    public ResponseEntity<ApiResponseUser<List<CashTransactionResponseDto>>> getTransactionsByType(@PathVariable CashTransaction.TransactionType transactionType) {
        List<CashTransaction> transactions = cashTransactionService.getTransactionsByType(transactionType);
        List<CashTransactionResponseDto> responseDtos = transactions.stream()
                .map(CashTransactionResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }
}
