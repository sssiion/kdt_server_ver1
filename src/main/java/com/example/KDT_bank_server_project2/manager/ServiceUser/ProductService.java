package com.example.KDT_bank_server_project2.manager.ServiceUser;


import com.example.KDT_bank_server_project2.manager.EntityUser.Product;
import com.example.KDT_bank_server_project2.manager.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 상품 생성
    public Product createProduct(Product product) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new RuntimeException("이미 존재하는 상품명입니다: " + product.getProductName());
        }
        return productRepository.save(product);
    }

    // 모든 상품 조회
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAllByOrderByCreatedAtDesc();
    }

    // 활성 상품만 조회
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findActiveProducts();
    }

    // ID로 상품 조회
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    // 상품명으로 상품 조회
    @Transactional(readOnly = true)
    public Optional<Product> getProductByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    // 상품 정보 수정
    public Product updateProduct(String id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        existingProduct.setProductDetail(updatedProduct.getProductDetail());
        existingProduct.setMaxRate(updatedProduct.getMaxRate());
        existingProduct.setMinRate(updatedProduct.getMinRate());
        existingProduct.setLimitMoney(updatedProduct.getLimitMoney());

        return productRepository.save(existingProduct);
    }

    // 상품 상태 변경
    public Product updateProductStatus(String id, Product.ProductStatus status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        product.setStatus(status);
        return productRepository.save(product);
    }

    // 카테고리별 상품 조회
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndStatus(category, Product.ProductStatus.ACTIVE);
    }

    // 상품 카테고리별 조회
    @Transactional(readOnly = true)
    public List<Product> getProductsByProductCategory(String productCategory) {
        return productRepository.findByProductCategory(productCategory);
    }

    // 금리 범위로 상품 검색
    @Transactional(readOnly = true)
    public List<Product> getProductsByInterestRateRange(BigDecimal minRate, BigDecimal maxRate) {
        return productRepository.findByInterestRateRange(minRate, maxRate);
    }

    // 상품 검색
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    // 카테고리별 최고금리순 상품 조회
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryOrderByMaxRate(String category) {
        return productRepository.findActiveByCategoryOrderByMaxRateDesc(category);
    }

    // 상품 비활성화
    public void deactivateProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        product.setStatus(Product.ProductStatus.INACTIVE);
        productRepository.save(product);
    }
}
