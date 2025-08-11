package com.example.KDT_bank_server_project2.manager.ControllerUser;

import com.example.KDT_bank_server_project2.manager.DtoUser.LoanApplicationCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.LoanApplicationResponseDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.LoanApplication;
import com.example.KDT_bank_server_project2.manager.ServiceUser.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loan-applications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Transactional
public class LoanApplicationController {


    private final LoanApplicationService loanApplicationService;

    // 대출 신청 생성
    @PostMapping
    public ResponseEntity<LoanApplicationResponseDto> createLoanApplication(@RequestBody LoanApplicationCreateRequestDto loanApplication) {
        try {
            LoanApplication application = new LoanApplication(loanApplication);
            LoanApplication createdApplication = loanApplicationService.createLoanApplication(application);
            return ResponseEntity.ok(new LoanApplicationResponseDto(createdApplication));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 모든 대출 신청 조회
    @GetMapping
    public ResponseEntity<List<LoanApplicationResponseDto>> getAllLoanApplications() {
        List<LoanApplication> applications = loanApplicationService.getAllLoanApplications();
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }

    // ID로 대출 신청 조회
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponseDto> getLoanApplicationById(@PathVariable String id) {
        Optional<LoanApplication> application = loanApplicationService.getLoanApplicationById(id);
        LoanApplicationResponseDto dto = new LoanApplicationResponseDto(application.get());
        return ResponseEntity.ok(dto);
    }

    // 고객별 대출 신청 조회
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanApplicationResponseDto>> getLoanApplicationsByCustomerId(@PathVariable String customerId) {
        List<LoanApplication> applications = loanApplicationService.getLoanApplicationsByCustomerId(customerId);
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }


    // 대기 중인 대출 신청 조회
    @GetMapping("/pending")
    public ResponseEntity<List<LoanApplicationResponseDto>> getPendingApplications() {
        List<LoanApplication> applications = loanApplicationService.getPendingApplications();
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }

    // 상품별 대출 신청 조회
    @GetMapping("/product/{productName}")
    public ResponseEntity<List<LoanApplicationResponseDto>> getLoanApplicationsByProductName(@PathVariable String productName) {
        List<LoanApplication> applications = loanApplicationService.getLoanApplicationsByProductName(productName);
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }

    // 대출 신청 승인
    @PatchMapping("/{applicationId}/approve")
    public ResponseEntity<LoanApplicationResponseDto> approveLoanApplication(@PathVariable String applicationId,
                                                                  @RequestParam String approverId) {
        try {
            LoanApplication approvedApplication = loanApplicationService.approveLoanApplication(applicationId, approverId);
            LoanApplicationResponseDto dto = new LoanApplicationResponseDto(approvedApplication);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 대출 신청 거절
    @PatchMapping("/{applicationId}/reject")
    public ResponseEntity<LoanApplicationResponseDto> rejectLoanApplication(@PathVariable String applicationId,
                                                                 @RequestParam String rejectionReason) {
        try {
            LoanApplication rejectedApplication = loanApplicationService.rejectLoanApplication(applicationId, rejectionReason);
            return ResponseEntity.ok(new LoanApplicationResponseDto(rejectedApplication));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 대출 신청 취소
    @PatchMapping("/{applicationId}/cancel")
    public ResponseEntity<LoanApplicationResponseDto> cancelLoanApplication(@PathVariable String applicationId) {
        try {
            LoanApplication cancelledApplication = loanApplicationService.cancelLoanApplication(applicationId);
            return ResponseEntity.ok(new LoanApplicationResponseDto(cancelledApplication));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 고객의 대기 중인 대출 신청 개수 조회
    @GetMapping("/customer/{customerId}/pending-count")
    public ResponseEntity<String> getPendingApplicationCountByCustomerId(@PathVariable String customerId) {
        String count = loanApplicationService.getPendingApplicationCountByCustomerId(customerId);
        return ResponseEntity.ok(count);
    }

    // 기간별 신청 조회
    @GetMapping("/date-range")
    public ResponseEntity<List<LoanApplicationResponseDto>> getApplicationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<LoanApplication> applications = loanApplicationService.getApplicationsByDateRange(startDate, endDate);
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }

    // 기간별 승인 조회
    @GetMapping("/approvals/date-range")
    public ResponseEntity<List<LoanApplicationResponseDto>> getApprovalsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<LoanApplication> applications = loanApplicationService.getApprovalsByDateRange(startDate, endDate);
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }

    // 특정 기간 동안 승인된 총 대출 금액
    @GetMapping("/approvals/total-amount")
    public ResponseEntity<BigDecimal> getTotalApprovedAmountByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        BigDecimal totalAmount = loanApplicationService.getTotalApprovedAmountByPeriod(startDate, endDate);
        return ResponseEntity.ok(totalAmount);
    }

    // 최소 신청 금액 이상인 대출 신청 조회
    @GetMapping("/min-amount/{minAmount}")
    public ResponseEntity<List<LoanApplicationResponseDto>> getApplicationsByMinAmount(@PathVariable BigDecimal minAmount) {
        List<LoanApplication> applications = loanApplicationService.getApplicationsByMinAmount(minAmount);
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }

    // 상품별 대출 신청 조회 (페이지네이션)
    @GetMapping("/product/{productName}/")
    public ResponseEntity<List<LoanApplicationResponseDto>> getApplicationsByProductAndStatus(
            @PathVariable String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<LoanApplication> applications = loanApplicationService.getApplicationsByProductAndStatus(
                productName, pageable);
        List<LoanApplicationResponseDto> list =  new ArrayList<>();
        list.addAll(applications.stream().map(LoanApplicationResponseDto::new).toList());
        return ResponseEntity.ok(list);
    }
}
