package com.example.KDT_bank_server_project2.manager.ControllerUser;


import com.example.KDT_bank_server_project2.manager.DtoUser.AgreementResponseDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.Agreement;
import com.example.KDT_bank_server_project2.manager.ServiceUser.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@RestController
@RequestMapping("/api/agreements")
@CrossOrigin(origins = "*")
public class AgreementController {


    private final AgreementService agreementService;

    // 약정 생성
    @PostMapping
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        try {
            Agreement createdAgreement = agreementService.createAgreement(agreement);
            return ResponseEntity.ok(createdAgreement);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 모든 약정 조회
    @GetMapping
    public ResponseEntity<List<Agreement>> getAllAgreements() {
        List<Agreement> agreements = agreementService.getAllAgreements();
        return ResponseEntity.ok(agreements);
    }

    // ID로 약정 조회
    @GetMapping("/{id}")
    public ResponseEntity<Agreement> getAgreementById(@PathVariable String id) {
        Optional<Agreement> agreement = agreementService.getAgreementById(id);
        return agreement.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 고객별 약정 조회
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Agreement>> getAgreementsByCustomerId(@PathVariable String customerId) {
        List<Agreement> agreements = agreementService.getAgreementsByCustomerId(customerId);
        return ResponseEntity.ok(agreements);
    }


    // 상품별 약정 조회
    @GetMapping("/product/{productName}")
    public ResponseEntity<List<Agreement>> getAgreementsByProductName(@PathVariable String productName) {
        List<Agreement> agreements = agreementService.getAgreementsByProductName(productName);
        return ResponseEntity.ok(agreements);
    }


    // 약정 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable String id, @RequestBody AgreementResponseDto dto) {
        try {
            Agreement updatedAgreement = agreementService.updateAgreement(id, dto);
            return ResponseEntity.ok(updatedAgreement);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



    // 만료 임박 약정 조회
    @GetMapping("/near-expiry")
    public ResponseEntity<List<Agreement>> getAgreementsNearingExpiry(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Agreement> agreements = agreementService.getAgreementsNearingExpiry(date);
        return ResponseEntity.ok(agreements);
    }



    // 기간별 약정 조회
    @GetMapping("/date-range")
    public ResponseEntity<List<Agreement>> getAgreementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Agreement> agreements = agreementService.getAgreementsByDateRange(startDate, endDate);
        return ResponseEntity.ok(agreements);
    }

    // 약정 만료 처리 (배치 작업)
    @PostMapping("/expire")
    public ResponseEntity<String> expireAgreements() {
        agreementService.expireAgreements();
        return ResponseEntity.ok("만료된 약정이 처리되었습니다.");
    }

    // 약정 연장
    @PostMapping("/{id}/extend")
    public ResponseEntity<Agreement> extendAgreement(
            @PathVariable String id,
            @RequestBody String newExpirationDate) {
        try {
            Agreement extendedAgreement = agreementService.extendAgreement(id, LocalDate.parse(newExpirationDate));
            return ResponseEntity.ok(extendedAgreement);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
