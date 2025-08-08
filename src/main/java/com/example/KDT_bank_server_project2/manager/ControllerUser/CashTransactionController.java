package com.example.KDT_bank_server_project2.manager.ControllerUser;

import com.example.KDT_bank_server_project2.manager.DtoUser.ApiResponseUser;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.CashTransactionResponseDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.TransferRequestDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.CashTransaction;
import com.example.KDT_bank_server_project2.manager.ServiceUser.CashTransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class CashTransactionController {

    private final CashTransactionService cashTransactionService;

    public CashTransactionController(CashTransactionService cashTransactionService) {
        this.cashTransactionService = cashTransactionService;
    }

    // 거래 기록 생성 (입금/출금/이체)
    @PostMapping
    public ResponseEntity<ApiResponseUser<CashTransactionResponseDto>> createTransaction(@Valid @RequestBody CashTransactionCreateRequestDto requestDto) {
        try {
            CashTransaction transaction = cashTransactionService.createTransaction(
                    requestDto.getAccountNumber(),
                    requestDto.getTransactionType(),
                    requestDto.getAmount(),
                    requestDto.getNote()
            );
            CashTransactionResponseDto responseDto = new CashTransactionResponseDto(transaction);

            return ResponseEntity.ok(ApiResponseUser.success("거래가 성공적으로 처리되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
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

    // 이체 처리
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponseUser<String>> processTransfer(@Valid @RequestBody TransferRequestDto requestDto) {
        try {
            cashTransactionService.processTransfer(
                    requestDto.getFromAccountNumber(),
                    requestDto.getToAccountNumber(),
                    requestDto.getAmount(),
                    requestDto.getNote()
            );
            return ResponseEntity.ok(ApiResponseUser.success("이체가 성공적으로 처리되었습니다.", "SUCCESS"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error("이체 처리 실패: " + e.getMessage()));
        }
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
