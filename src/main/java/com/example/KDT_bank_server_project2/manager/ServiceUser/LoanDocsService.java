package com.example.KDT_bank_server_project2.manager.ServiceUser;

import com.example.KDT_bank_server_project2.manager.EntityUser.LoanDocs;
import com.example.KDT_bank_server_project2.manager.Repository.LoanDocsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LoanDocsService {

    @Autowired
    private LoanDocsRepository loanDocsRepository;

    private final String uploadDir = "uploads/loan-docs/"; // 파일 업로드 디렉토리

    // 대출 서류 업로드
    public LoanDocs uploadLoanDocument(Long applicationId, MultipartFile file, String fileType) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("파일이 비어있습니다");
        }

        // 파일명 중복 확인
        if (loanDocsRepository.existsByApplicationIdAndFileName(applicationId, file.getOriginalFilename())) {
            throw new RuntimeException("같은 이름의 파일이 이미 존재합니다: " + file.getOriginalFilename());
        }

        // 파일 저장
        String fileName = saveFile(file);
        String filePath = uploadDir + fileName;

        LoanDocs loanDocs = new LoanDocs();
        loanDocs.setApplicationId(applicationId);
        loanDocs.setFileName(file.getOriginalFilename());
        loanDocs.setFilePath(filePath);
        loanDocs.setFileType(fileType);
        loanDocs.setUploadDate(LocalDateTime.now());

        return loanDocsRepository.save(loanDocs);
    }

    // 파일 저장 (실제 파일시스템에 저장)
    private String saveFile(MultipartFile file) throws IOException {
        // 업로드 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 고유한 파일명 생성 (UUID + 원본 파일명)
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    // 모든 대출 서류 조회
    @Transactional(readOnly = true)
    public List<LoanDocs> getAllLoanDocs() {
        return loanDocsRepository.findAll();
    }

    // ID로 대출 서류 조회
    @Transactional(readOnly = true)
    public Optional<LoanDocs> getLoanDocsById(Long id) {
        return loanDocsRepository.findById(id);
    }

    // 대출 신청별 서류 조회
    @Transactional(readOnly = true)
    public List<LoanDocs> getLoanDocsByApplicationId(Long applicationId) {
        return loanDocsRepository.findByApplicationIdOrderByUploadDateDesc(applicationId);
    }

    // 파일 유형별 서류 조회
    @Transactional(readOnly = true)
    public List<LoanDocs> getLoanDocsByFileType(String fileType) {
        return loanDocsRepository.findByFileType(fileType);
    }

    // 대출 신청별 특정 파일 유형 서류 조회
    @Transactional(readOnly = true)
    public List<LoanDocs> getLoanDocsByApplicationIdAndFileType(Long applicationId, String fileType) {
        return loanDocsRepository.findByApplicationIdAndFileType(applicationId, fileType);
    }

    // 파일명으로 서류 검색
    @Transactional(readOnly = true)
    public List<LoanDocs> searchLoanDocsByFileName(String fileName) {
        return loanDocsRepository.findByFileNameContaining(fileName);
    }

    // 특정 대출 신청의 서류 개수 조회
    @Transactional(readOnly = true)
    public long getLoanDocsCountByApplicationId(Long applicationId) {
        return loanDocsRepository.countByApplicationId(applicationId);
    }

    // 기간별 서류 조회
    @Transactional(readOnly = true)
    public List<LoanDocs> getLoanDocsByUploadDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return loanDocsRepository.findByUploadDateBetween(startDate, endDate);
    }

    // 서류 삭제
    public void deleteLoanDocs(Long id) throws IOException {
        LoanDocs loanDocs = loanDocsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("서류를 찾을 수 없습니다: " + id));

        // 파일시스템에서 파일 삭제
        Path filePath = Paths.get(loanDocs.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // 데이터베이스에서 삭제
        loanDocsRepository.delete(loanDocs);
    }

    // 대출 신청별 모든 서류 삭제
    public void deleteLoanDocsByApplicationId(Long applicationId) throws IOException {
        List<LoanDocs> docsList = loanDocsRepository.findByApplicationId(applicationId);

        for (LoanDocs docs : docsList) {
            // 파일시스템에서 파일 삭제
            Path filePath = Paths.get(docs.getFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }

        // 데이터베이스에서 삭제
        loanDocsRepository.deleteAll(docsList);
    }

    // 파일 다운로드 (파일 경로 반환)
    @Transactional(readOnly = true)
    public String getFilePathForDownload(Long id) {
        LoanDocs loanDocs = loanDocsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("서류를 찾을 수 없습니다: " + id));

        return loanDocs.getFilePath();
    }

    // 파일 유효성 검사
    public boolean isValidFileType(String fileName) {
        String[] allowedExtensions = {".pdf", ".jpg", ".jpeg", ".png", ".doc", ".docx"};
        String lowerCaseFileName = fileName.toLowerCase();

        for (String extension : allowedExtensions) {
            if (lowerCaseFileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    // 파일 크기 검사 (10MB 이하)
    public boolean isValidFileSize(MultipartFile file) {
        long maxSize = 10 * 1024 * 1024; // 10MB
        return file.getSize() <= maxSize;
    }
}
