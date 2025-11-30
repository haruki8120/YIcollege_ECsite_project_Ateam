package com.college.yi.EcSite.admin.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.college.yi.EcSite.admin.repository.CategoryMapper;
import com.college.yi.EcSite.admin.repository.ProductImageMapper;
import com.college.yi.EcSite.admin.repository.ProductMapper;
import com.college.yi.EcSite.entity.Category;
import com.college.yi.EcSite.entity.Product;
import com.college.yi.EcSite.entity.ProductImage;

public class AdminProductServiceTest {

    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductImageMapper productImageMapper;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private AdminProductService adminProductService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("positive：商品が一件ありカテゴリと画像が取得できること")
    @Test
    public void test_1() {
        Product product = new Product();
        product.setProductId(1L);
        product.setCategoryId(1L);
        product.setShopId(100L);
        product.setName("商品A");
        product.setDescription("説明");
        product.setPrice(new BigDecimal("1000"));
        product.setTaxRate(new BigDecimal("10"));
        product.setStockQuantity(5);
        product.setStatus(1);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        ProductImage image = new ProductImage();
        image.setImageId(1L);
        image.setProductId(1L);
        image.setImageUrl("http://example.com/image.jpg");
        image.setSortOrder(1);
        image.setIsMain(true);
        image.setCreatedAt(LocalDateTime.now());

        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("カテゴリA");

        when(productMapper.findAll()).thenReturn(List.of(product));
        when(productImageMapper.findByProductIds(List.of(1L))).thenReturn(List.of(image));
        when(categoryMapper.findByIds(List.of(1L))).thenReturn(List.of(category));

        Pageable pageable = PageRequest.of(0, 10);
        Page<?> result = adminProductService.getProductPage(pageable);

        assertEquals(1, result.getContent().size());
    }

    @DisplayName("nagative:商品が一件も無く取得できないこと")
    @Test
    public void test_2() {
        when(productMapper.findAll()).thenReturn(Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 10);

        Page<?> result = adminProductService.getProductPage(pageable);

        assertTrue(result.isEmpty());
    }

    @DisplayName("positive:在庫数更新が正常にできること")
    @Test
    public void test_3() {
        Long productId = 1L;
        Integer stockQuantity = 10;
        Product product = new Product();
        product.setProductId(productId);

        when(productMapper.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.updateStock(productId, stockQuantity)).thenReturn(1);

        assertDoesNotThrow(() -> adminProductService.updateStock(productId, stockQuantity));
    }

    @DisplayName("negative:存在しない商品IDの在庫数更新はエラーになること")
    @Test
    public void test_4() {
        Long productId = 99L;
        Integer stockQuantity = 10;

        when(productMapper.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                adminProductService.updateStock(productId, stockQuantity));
        assertTrue(exception.getMessage().contains("対象商品が見つかりません"));
    }

    @DisplayName("negative:在庫数がnullやマイナスならエラー")
    @Test
    public void test_5() {
        Long productId = 1L;

        Exception ex1 = assertThrows(IllegalArgumentException.class, () ->
                adminProductService.updateStock(productId, null));
        assertTrue(ex1.getMessage().contains("在庫数は0以上の整数で入力してください"));

        Exception ex2 = assertThrows(IllegalArgumentException.class, () ->
                adminProductService.updateStock(productId, -1));
        assertTrue(ex2.getMessage().contains("在庫数は0以上の整数で入力してください"));
    }

}
