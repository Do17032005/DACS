package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.Category;
import com.example.Clothesshoponline.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "admin/category_form";
    }

    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute Category category,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("category", category);
            model.addAttribute("isEdit", false);
            return "admin/category_form";
        }

        try {
            if (categoryService.existsByName(category.getName())) {
                redirectAttributes.addFlashAttribute("error", "Danh mục với tên này đã tồn tại!");
                return "redirect:/admin/categories/add";
            }
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm danh mục: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getCategoryById(id);
            model.addAttribute("category", category);
            model.addAttribute("isEdit", true);
            return "admin/category_form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục!");
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id,
            @Valid @ModelAttribute Category category,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            category.setId(id);
            model.addAttribute("category", category);
            model.addAttribute("isEdit", true);
            return "admin/category_form";
        }

        try {
            if (categoryService.existsByNameAndIdNot(category.getName(), id)) {
                redirectAttributes.addFlashAttribute("error", "Danh mục với tên này đã tồn tại!");
                return "redirect:/admin/categories/edit/" + id;
            }
            category.setId(id);
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật danh mục: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getCategoryById(id);
            // Check if category has products
            if (category.getProducts() != null && !category.getProducts().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                        "Không thể xóa danh mục này vì còn " + category.getProducts().size() + " sản phẩm!");
                return "redirect:/admin/categories";
            }
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa danh mục: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/toggle/{id}")
    @ResponseBody
    public String toggleActive(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            category.setActive(!category.isActive());
            categoryService.saveCategory(category);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }
}
