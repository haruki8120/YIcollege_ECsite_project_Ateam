package com.college.yi.EcSite.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.college.yi.EcSite.admin.form.ProductRegisterForm;
import com.college.yi.EcSite.admin.service.AdminProductRegisterService;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductRegisterPageController {

    private final AdminProductRegisterService adminProductRegisterService;

    public AdminProductRegisterPageController(AdminProductRegisterService adminProductRegisterService) {
        this.adminProductRegisterService = adminProductRegisterService;
    }

    @GetMapping("/new")
    public String showRegisterForm(Model model) {
        model.addAttribute("form", new ProductRegisterForm());
        model.addAttribute("categories", adminProductRegisterService.getAllCategories());
        return "admin/products/productForm";
    }

    @PostMapping("/new")
    public String registerProduct(@ModelAttribute("form") ProductRegisterForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            adminProductRegisterService.registerProduct(form);
            redirectAttributes.addFlashAttribute("success", "商品を登録しました");
            return "redirect:/admin/products";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "admin/products/productForm";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "登録に失敗しました");
            return "admin/products/productForm";
        }
    }
}

