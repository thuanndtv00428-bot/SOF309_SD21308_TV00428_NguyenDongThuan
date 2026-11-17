package com.poly.sof309.controllers;

import com.poly.sof309.exception.DuplicateProductException;
import com.poly.sof309.exception.ValidationException;
import com.poly.sof309.model.Product;
import com.poly.sof309.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test cho nghiệp vụ Product (ProductService được ProductController sử dụng).
 * Áp dụng @ParameterizedTest + @CsvSource (biên form) và assertThrows (ngoại lệ nghiệp vụ).
 */
@DisplayName("Y3 - Unit Test ProductController / ProductService")
class ProductControllerTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(); // mỗi test instance mới, có 2 sản phẩm mẫu
    }

    // ==================== VALIDATE (Parameterized + BVA) ====================

    @ParameterizedTest(name = "[{index}] name=''{0}'' price={1} qty={2} → valid={3}")
    @DisplayName("Kiểm tra validate sản phẩm với giá trị biên form")
    @CsvSource({
            // name
            "'', 1000, 1, false",                    // tên rỗng
            "'   ', 1000, 1, false",                 // tên blank
            "Laptop, 1000, 1, true",                 // hợp lệ
            // price biên
            "Laptop, 0, 1, false",                   // giá = 0
            "Laptop, -1, 1, false",                  // giá âm
            "Laptop, 0.01, 1, true",                 // giá > 0 nhỏ
            "Laptop, 25000000, 1, true",             // giá lớn hợp lệ
            // quantity biên
            "Laptop, 1000, -1, false",               // số lượng âm
            "Laptop, 1000, 0, true",                 // số lượng = 0 (cho phép)
            "Laptop, 1000, 1, true"                  // số lượng > 0
    })
    void productValidationBoundary(String name, double price, int quantity, boolean expectedValid) {
        List<String> errors = productService.validate(name, price, quantity);
        assertEquals(expectedValid, errors.isEmpty(),
                () -> "Expected valid=" + expectedValid + " nhưng errors=" + errors);
    }

    @Test
    @DisplayName("validate trả về đúng message khi tên trống")
    void validateEmptyNameMessage() {
        List<String> errors = productService.validate("", 1000, 1);
        assertTrue(errors.stream().anyMatch(e -> e.contains("Tên sản phẩm không được để trống")));
    }

    @Test
    @DisplayName("validate trả về đúng message khi giá <= 0")
    void validatePriceMessage() {
        List<String> errors = productService.validate("Laptop", 0, 1);
        assertTrue(errors.stream().anyMatch(e -> e.contains("Giá bán phải lớn hơn 0")));
    }

    // ==================== ADD ====================

    @Test
    @DisplayName("Thêm sản phẩm hợp lệ thành công")
    void addProductSuccess() {
        int before = productService.findAll().size();
        Product p = productService.add("Bàn phím cơ", 1500000, 20);
        assertNotNull(p);
        assertTrue(p.getId() > 0);
        assertEquals("Bàn phím cơ", p.getName());
        assertEquals(1500000, p.getPrice());
        assertEquals(20, p.getQuantity());
        assertEquals(before + 1, productService.findAll().size());
    }

    @Test
    @DisplayName("assertThrows: ValidationException khi thêm sản phẩm giá âm")
    void addInvalidPriceThrowsValidationException() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
                productService.add("Sản phẩm lỗi", -100, 5)
        );
        assertFalse(ex.getErrors().isEmpty());
    }

    @Test
    @DisplayName("assertThrows: ValidationException khi tên trống")
    void addEmptyNameThrowsValidationException() {
        assertThrows(ValidationException.class, () ->
                productService.add("  ", 1000, 1)
        );
    }

    @Test
    @DisplayName("assertThrows: DuplicateProductException khi tên sản phẩm trùng (không phân biệt hoa thường)")
    void addDuplicateNameThrows() {
        productService.add("Tai nghe Sony", 2000000, 10);
        DuplicateProductException ex = assertThrows(DuplicateProductException.class, () ->
                productService.add("tai nghe sony", 2500000, 5)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("tồn tại")
                || ex.getMessage().toLowerCase().contains("exist"));
    }

    // ==================== UPDATE ====================

    @Test
    @DisplayName("Cập nhật sản phẩm thành công")
    void updateProductSuccess() {
        Product original = productService.findAll().get(0);
        long id = original.getId();
        Product updated = productService.update(id, "Tên mới XPS", 30000000, 5);
        assertEquals(id, updated.getId());
        assertEquals("Tên mới XPS", updated.getName());
        assertEquals(30000000, updated.getPrice());
        assertEquals(5, updated.getQuantity());
    }

    @Test
    @DisplayName("assertThrows: IllegalArgumentException khi update id không tồn tại")
    void updateNotFoundThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                productService.update(99999L, "Không có", 1000, 1)
        );
        assertTrue(ex.getMessage().contains("99999") || ex.getMessage().toLowerCase().contains("không tìm thấy"));
    }

    @Test
    @DisplayName("assertThrows: ValidationException khi update với dữ liệu không hợp lệ")
    void updateInvalidThrowsValidation() {
        long id = productService.findAll().get(0).getId();
        assertThrows(ValidationException.class, () ->
                productService.update(id, "", -1, -5)
        );
    }

    @Test
    @DisplayName("assertThrows: DuplicateProductException khi update trùng tên sản phẩm khác")
    void updateDuplicateNameThrows() {
        List<Product> list = productService.findAll();
        assertTrue(list.size() >= 2);
        long id1 = list.get(0).getId();
        String name2 = list.get(1).getName();
        assertThrows(DuplicateProductException.class, () ->
                productService.update(id1, name2, 1000, 1)
        );
    }

    // ==================== DELETE ====================

    @Test
    @DisplayName("Xóa sản phẩm thành công")
    void deleteProductSuccess() {
        long id = productService.findAll().get(0).getId();
        int before = productService.findAll().size();
        productService.delete(id);
        assertEquals(before - 1, productService.findAll().size());
        assertNull(productService.findById(id));
    }

    @Test
    @DisplayName("assertThrows: IllegalArgumentException khi xóa id không tồn tại")
    void deleteNotFoundThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                productService.delete(99999L)
        );
    }

    // ==================== FIND ====================

    @Test
    @DisplayName("findAll trả về danh sách ban đầu có sẵn dữ liệu mẫu")
    void findAllHasSeedData() {
        List<Product> list = productService.findAll();
        assertTrue(list.size() >= 2);
    }

    @Test
    @DisplayName("findById trả về đúng sản phẩm / null nếu không có")
    void findById() {
        Product first = productService.findAll().get(0);
        Product found = productService.findById(first.getId());
        assertNotNull(found);
        assertEquals(first.getName(), found.getName());
        assertNull(productService.findById(88888L));
    }
}
