package com.poly.sof309.controller;

import com.poly.sof309.exception.DuplicateProductException;
import com.poly.sof309.exception.ValidationException;
import com.poly.sof309.model.Product;
import com.poly.sof309.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private boolean notLoggedIn(HttpSession s) { return s.getAttribute("user") == null; }

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        Product p = productService.findById(id);
        if (p == null) return "redirect:/products";
        model.addAttribute("product", p);
        return "product-form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(defaultValue = "0") long id,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "") String price,
                       @RequestParam(defaultValue = "") String quantity,
                       HttpSession session, Model model, RedirectAttributes ra) {
        if (notLoggedIn(session)) return "redirect:/login";
        double priceVal;
        int qtyVal;
        try {
            priceVal = Double.parseDouble(price.trim());
            qtyVal = Integer.parseInt(quantity.trim());
        } catch (NumberFormatException e) {
            return back(model, id, name, 0, 0, List.of("Giá bán và số lượng phải là số hợp lệ"));
        }
        try {
            if (id == 0) {
                productService.add(name, priceVal, qtyVal);
                ra.addFlashAttribute("success", "Thêm sản phẩm thành công");
            } else {
                productService.update(id, name, priceVal, qtyVal);
                ra.addFlashAttribute("success", "Cập nhật sản phẩm thành công");
            }
            return "redirect:/products";
        } catch (ValidationException e) {
            return back(model, id, name, priceVal, qtyVal, e.getErrors());
        } catch (DuplicateProductException | IllegalArgumentException e) {
            return back(model, id, name, priceVal, qtyVal, List.of(e.getMessage()));
        }
    }

    private String back(Model model, long id, String name, double price, int qty, List<String> errors) {
        model.addAttribute("product", new Product(id, name, price, qty));
        model.addAttribute("errors", errors);
        return "product-form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id, HttpSession session, RedirectAttributes ra) {
        if (notLoggedIn(session)) return "redirect:/login";
        try {
            productService.delete(id);
            ra.addFlashAttribute("success", "Xóa sản phẩm thành công");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products";
    }
}
