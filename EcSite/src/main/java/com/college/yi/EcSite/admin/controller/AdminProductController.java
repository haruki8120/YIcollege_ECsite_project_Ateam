package com.college.yi.EcSite.admin.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.college.yi.EcSite.admin.dto.ProductsDto;
import com.college.yi.EcSite.admin.service.AdminProductService;
import com.college.yi.EcSite.entity.Product;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

   
    @GetMapping
    public String showProductList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<ProductsDto> productPage = adminProductService.getProductPage(PageRequest.of(page, 10));
        model.addAttribute("productPage", productPage);
        return "admin/products/products";
    }
    

    @PostMapping("/{productId}/stock")
    public String updateStock(@PathVariable Long productId,
                              @RequestParam Integer stockQuantity,
                              RedirectAttributes redirectAttributes) {
        try {
            if (stockQuantity == null || stockQuantity < 0) {
                redirectAttributes.addFlashAttribute("stockErrors",
                    Map.of(productId, "在庫数は0以上で入力してください"));
                return "redirect:/admin/products";
            }

            Optional<Product> productOpt = adminProductService.getProduct(productId);
            if (productOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("stockErrors",
                    Map.of(productId, "対象商品が見つかりません"));
                return "redirect:/admin/products";
            }

            adminProductService.updateStock(productId, stockQuantity);
            redirectAttributes.addFlashAttribute("success", "在庫数を更新しました");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("stockErrors",
                Map.of(productId, "在庫更新に失敗しました"));
        }
        return "redirect:/admin/products";
    }
}