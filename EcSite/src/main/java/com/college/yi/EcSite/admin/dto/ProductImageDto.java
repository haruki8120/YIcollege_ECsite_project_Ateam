package com.college.yi.EcSite.admin.dto;

import jakarta.validation.constraints.Size;

import com.college.yi.EcSite.entity.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
    
    @NonNull
    private Long imageId;

    @Size(max = 255)
    private String imageUrl;

    private Integer sortOrder;
    private Boolean isMain;
    private Long productId;

   
    public boolean isImageMissing() {
        return (imageUrl == null || imageUrl.isBlank());
    }

    public static ProductImageDto of(ProductImage img) {
        ProductImageDto dto = new ProductImageDto();
        dto.setImageId(img.getImageId());
        dto.setImageUrl(img.getImageUrl());
        dto.setSortOrder(img.getSortOrder());
        dto.setIsMain(img.getIsMain());
        dto.setProductId(img.getProductId());
        return dto;
    }
}
