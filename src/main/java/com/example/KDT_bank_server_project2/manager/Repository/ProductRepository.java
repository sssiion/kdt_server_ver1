package com.example.KDT_bank_server_project2.manager.Repository;


import com.example.KDT_bank_server_project2.manager.EntityUser.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductName(String productName);
    // 상품명으로 상품 찾기

    boolean existsByProductName(String productName);
    // 상품명 중복 확인

    List<Product> findByCategory(String category);
    // 카테고리별 상품 조회

    List<Product> findByProductCategory(String productCategory);
    // 상품 카테고리별 조회

    List<Product> findByStatus(Product.ProductStatus status);
    // 상태별 상품 조회

    List<Product> findByProductNameContaining(String keyword);
    // 상품명으로 검색 (부분 일치)

    List<Product> findByCategoryAndStatus(String category, Product.ProductStatus status);
    // 카테고리 및 상태별 상품 조회

    List<Product> findAllByOrderByCreatedAtDesc();
    // 최신 등록순으로 전체 상품 조회

    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' ORDER BY p.createdAt DESC")
    List<Product> findActiveProducts();
    // 활성 상품만 조회

    @Query("SELECT p FROM Product p WHERE p.maxRate BETWEEN :minRate AND :maxRate AND p.status = 'ACTIVE'")
    List<Product> findByInterestRateRange(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);
    // 금리 범위로 상품 검색

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword% OR p.productDetail LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);
    // 통합 검색 (상품명, 상품 설명)

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.status = 'ACTIVE' ORDER BY p.maxRate DESC")
    List<Product> findActiveByCategoryOrderByMaxRateDesc(@Param("category") String category);
    // 카테고리별 활성 상품을 최고 금리순으로 조회
}