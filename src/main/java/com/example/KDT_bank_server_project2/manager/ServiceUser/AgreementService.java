package com.example.KDT_bank_server_project2.manager.ServiceUser;


import com.example.KDT_bank_server_project2.manager.DtoUser.AgreementResponseDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.Agreement;
import com.example.KDT_bank_server_project2.manager.Repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AgreementService {

    private final AgreementRepository agreementRepository;

    // 약정 생성
    public Agreement createAgreement(Agreement agreement) {
        if (agreement.getAgreementDate() == null) {
            agreement.setAgreementDate(LocalDate.now());
        }

        // 만료일이 체결일보다 이전인지 확인
        if (agreement.getExpirationDate().isBefore(agreement.getAgreementDate())) {
            throw new RuntimeException("만료일은 체결일보다 이후여야 합니다");
        }

        return agreementRepository.save(agreement);
    }

    // 모든 약정 조회
    @Transactional(readOnly = true)
    public List<Agreement> getAllAgreements() {
        return agreementRepository.findAll();
    }

    // ID로 약정 조회
    @Transactional(readOnly = true)
    public Optional<Agreement> getAgreementById(String id) {
        return agreementRepository.findById(id);
    }

    // 고객별 약정 조회
    @Transactional(readOnly = true)
    public List<Agreement> getAgreementsByCustomerId(String customerId) {
        return agreementRepository.findByCustomerIdOrderByAgreementDateDesc(customerId);
    }


    // 상품별 약정 조회
    @Transactional(readOnly = true)
    public List<Agreement> getAgreementsByProductName(String productName) {
        return agreementRepository.findByProductName(productName);
    }


    // 약정 정보 수정
    public Agreement updateAgreement(String id, AgreementResponseDto dto) {
        Agreement existingAgreement = agreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("약정을 찾을 수 없습니다: " + id));

        existingAgreement.setExpirationDate(dto.getExpirationDate());

        return agreementRepository.save(existingAgreement);
    }


    // 만료 임박 약정 조회
    @Transactional(readOnly = true)
    public List<Agreement> getAgreementsNearingExpiry(LocalDate date) {
        return agreementRepository.findAgreementsNearingExpiry(date);
    }



    // 기간별 약정 조회
    @Transactional(readOnly = true)
    public List<Agreement> getAgreementsByDateRange(LocalDate startDate, LocalDate endDate) {
        return agreementRepository.findByAgreementDateBetween(startDate, endDate);
    }

    // 약정 만료 처리
    @Transactional
    public void expireAgreements() {
        LocalDate today = LocalDate.now();
        List<Agreement> expiredAgreements = agreementRepository.findByExpirationDateBefore(today);

        for (Agreement agreement : expiredAgreements) {
            if (agreement.getStatus() == Agreement.AgreementStatus.ACTIVE) {
                agreement.setStatus(Agreement.AgreementStatus.EXPIRED);
                agreementRepository.save(agreement);
            }
        }
    }

    // 약정 연장
    public Agreement extendAgreement(String id, LocalDate newExpirationDate) {
        Agreement agreement = agreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("약정을 찾을 수 없습니다: " + id));

        if (newExpirationDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("새로운 만료일은 현재 날짜보다 이후여야 합니다");
        }

        agreement.setExpirationDate(newExpirationDate);
        return agreementRepository.save(agreement);
    }
}
