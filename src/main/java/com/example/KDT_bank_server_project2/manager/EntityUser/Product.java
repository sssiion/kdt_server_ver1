package com.example.KDT_bank_server_project2.manager.EntityUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name", unique = true, nullable = false, length = 100)
    private String productName;

    @Column(name = "product_detail", columnDefinition = "TEXT")
    private String productDetail;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "product_category", nullable = false, length = 50)
    private String productCategory;

    @Column(name = "max_rate", precision = 5, scale = 2)
    private BigDecimal maxRate;

    @Column(name = "min_rate", precision = 5, scale = 2)
    private BigDecimal minRate;

    @Column(name = "limitmoney", precision = 15, scale = 2)
    private BigDecimal limitMoney;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ProductStatus {
        ACTIVE, INACTIVE;

        public static ProductStatus fromString(String status) {
            if (status == null) return ACTIVE;
            try {
                return ProductStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ACTIVE;
            }
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ProductStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}