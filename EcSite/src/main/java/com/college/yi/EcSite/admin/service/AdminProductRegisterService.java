package com.college.yi.EcSite.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.college.yi.EcSite.admin.form.ProductImageRegisterForm;
import com.college.yi.EcSite.admin.form.ProductRegisterForm;
import com.college.yi.EcSite.admin.repository.CategoryMapper;
import com.college.yi.EcSite.admin.repository.ProductImageMapper;
import com.college.yi.EcSite.admin.repository.ProductMapper;
import com.college.yi.EcSite.entity.Category;
import com.college.yi.EcSite.entity.Product;
import com.college.yi.EcSite.entity.ProductImage;

@Service
public class AdminProductRegisterService {

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final CategoryMapper categoryMapper;

    public AdminProductRegisterService(ProductMapper productMapper,
                                      ProductImageMapper productImageMapper,
                                      CategoryMapper categoryMapper) {
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public void registerProduct(ProductRegisterForm form) {
        
        if (form == null || form.getName() == null || form.getName().isEmpty()) {
            throw new IllegalArgumentException("商品名を入力してください");
        }
        if (form.getPrice() == null) {
            throw new IllegalArgumentException("価格を入力してください");
        }
        if (form.getCategoryId() == null) {
            throw new IllegalArgumentException("カテゴリを選択してください");
        }

        Category category = categoryMapper.findById(form.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("選択されたカテゴリが存在しません");
        }

        Product product = new Product();
        product.setShopId(form.getShopId());
        product.setCategoryId(form.getCategoryId());
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setTaxRate(form.getTaxRate());
        product.setStockQuantity(form.getStockQuantity());
        product.setStatus(form.getStatus());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productMapper.insert(product);

        Long productId = product.getProductId();

        if (form.getImageList() != null) {
            for (ProductImageRegisterForm imgForm : form.getImageList()) {
                MultipartFile file = imgForm.getImageFile();
                if (file != null && !file.isEmpty()) {
                    String imageUrl = saveImageAndGetUrl(file);

                    ProductImage image = new ProductImage();
                    image.setProductId(productId);
                    image.setImageUrl(imageUrl);
                    image.setSortOrder(imgForm.getSortOrder());
                    image.setIsMain(Objects.requireNonNullElse(imgForm.getIsMain(), false));
                    image.setCreatedAt(LocalDateTime.now());
                    productImageMapper.insert(image);
                }
            }
        }
    }

    private String saveImageAndGetUrl(MultipartFile file) {
        String saveDir = "C:/tmp/uploaded_images/";
        java.io.File dir = new java.io.File(saveDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        java.io.File saveFile = new java.io.File(dir, fileName);
        try {
            file.transferTo(saveFile);
        } catch (Exception e) {
            throw new RuntimeException("画像ファイルの保存に失敗しました", e);
        }
        return "/uploaded_images/" + fileName;
    }
    
    public List<Category> getAllCategories() {
        return categoryMapper.findAll();
}
}