package com.example.KDT_bank_server_project2.manager.Repository;



import com.example.KDT_bank_server_project2.manager.EntityUser.LoanDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanDocsRepository extends JpaRepository<LoanDocs, String> {

    List<LoanDocs> findByApplicationId(String applicationId);
    // 대출 신청 ID로 서류 조회

    List<LoanDocs> findByFileType(String fileType);
    // 파일 유형별 서류 조회

    List<LoanDocs> findByFileNameContaining(String fileName);
    // 파일명으로 서류 검색 (부분 일치)

    List<LoanDocs> findByUploadDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    // 업로드일 기간으로 서류 조회

    List<LoanDocs> findByApplicationIdOrderByUploadDateDesc(String applicationId);
    // 대출 신청별 서류를 최신 업로드순으로 조회

    String countByApplicationId(String applicationId);
    // 특정 대출 신청의 서류 개수

    @Query("SELECT ld FROM LoanDocs ld WHERE ld.applicationId = :applicationId AND ld.fileType = :fileType")
    List<LoanDocs> findByApplicationIdAndFileType(@Param("applicationId") String applicationId, @Param("fileType") String fileType);
    // 대출 신청 ID 및 파일 유형별 서류 조회

    @Query("SELECT ld FROM LoanDocs ld WHERE ld.filePath LIKE %:path%")
    List<LoanDocs> findByFilePathContaining(@Param("path") String path);
    // 파일 경로로 서류 검색

    boolean existsByApplicationIdAndFileName(String applicationId, String fileName);
    // 대출 신청별 파일명 중복 확인
}