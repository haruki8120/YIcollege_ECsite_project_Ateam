package com.college.yi.EcSite.admin.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.college.yi.EcSite.entity.ProductImage;

@Mapper
public interface ProductImageMapper {

    // 複数IDで画像取得
    List<ProductImage> findByProductIds(@Param("productIds") List<Long> productIds);

    // 単一IDで画像取得（必要な場合はメソッド名を変える）
    List<ProductImage> findByProductId(@Param("productId") Long productId);

    // 単一IDで削除
    void deleteByProductId(@Param("productId") Long productId);

    void insert(ProductImage productImage);
}
