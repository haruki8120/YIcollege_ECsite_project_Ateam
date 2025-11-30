package com.college.yi.EcSite.admin.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.college.yi.EcSite.entity.Product;

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    Optional<Product> findById(@Param("productId") Long productId);

    void insert(Product product);

    void update(Product product);

    int updateStock(@Param("productId") Long productId, @Param("stockQuantity") Integer stockQuantity);
    
    
    int logicalDelete(@Param("productId") Long productId, @Param("deletedAt") LocalDateTime deletedAt);
}

