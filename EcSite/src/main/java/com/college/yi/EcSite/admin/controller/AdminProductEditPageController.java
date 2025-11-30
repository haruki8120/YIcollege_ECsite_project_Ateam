package com.college.yi.EcSite.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.college.yi.EcSite.admin.dto.ProductEditDto;
import com.college.yi.EcSite.admin.form.ProductEditForm;
import com.college.yi.EcSite.admin.service.AdminProductEditService;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductEditPageController {

    private final AdminProductEditService adminProductEditService;

    public AdminProductEditPageController(AdminProductEditService adminProductEditService) {
        this.adminProductEditService = adminProductEditService;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductEditDto dto = adminProductEditService.getProductForEdit(id);
            model.addAttribute("form", dto);
            return "admin/products/edit";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/products";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "取得に失敗しました");
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                 @ModelAttribute("form") ProductEditForm form,
                                 RedirectAttributes redirectAttributes) {
        try {
            adminProductEditService.updateProduct(id, form);
            redirectAttributes.addFlashAttribute("success", "商品を更新しました");
            return "redirect:/admin/products";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "admin/products/edit";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "更新に失敗しました");
            return "admin/products/edit";
        }
    }
}

