package com.college.yi.EcSite.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.yi.EcSite.admin.form.StockUpdateForm;
import com.college.yi.EcSite.admin.repository.ProductMapper;
import com.college.yi.EcSite.entity.Product;

@Service
public class AdminStockService {
    private final ProductMapper productMapper;

    public AdminStockService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product getProduct(Long productId) {
        return productMapper.findById(productId).orElseThrow(() ->
            new IllegalArgumentException("対象商品が見つかりません"));
    }

    @Transactional
    public void updateStock(StockUpdateForm form) {
        if (form.getStockQuantity() == null || form.getStockQuantity() < 0) {
            throw new IllegalArgumentException("在庫数は0以上の整数で入力してください");
        }

        int updated = productMapper.updateStock(form.getProductId(), form.getStockQuantity());
        if (updated == 0) {
            throw new RuntimeException("在庫更新に失敗しました");
        }
    }
}