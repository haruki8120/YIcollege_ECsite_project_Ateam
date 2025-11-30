package com.college.yi.EcSite.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "product_images")
public class ProductImage {
    @Id
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_main", nullable = false)
    private Boolean isMain;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private Long id;

}

