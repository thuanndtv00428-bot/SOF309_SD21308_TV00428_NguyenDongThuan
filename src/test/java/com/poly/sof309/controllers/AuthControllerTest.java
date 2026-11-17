package com.poly.sof309.controllers;

import com.poly.sof309.exception.DuplicateUserException;
import com.poly.sof309.exception.ValidationException;
import com.poly.sof309.model.User;
import com.poly.sof309.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test cho nghiệp vụ Auth (AuthService được Controller sử dụng).
 * Áp dụng @ParameterizedTest + @CsvSource (biên form) và assertThrows (ngoại lệ nghiệp vụ).
 */
@DisplayName("Y3 - Unit Test AuthController / AuthService")
class AuthControllerTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(); // mỗi test instance mới, có sẵn user "admin"
    }

    // ==================== LOGIN ====================

    @Test
    @DisplayName("TC_AUTH_01: Đăng nhập thành công với admin/123456")
    void loginSuccess() {
        assertTrue(authService.login("admin", "123456"));
    }

    @Test
    @DisplayName("TC_AUTH_06: Username không phân biệt hoa/thường")
    void loginCaseInsensitiveUsername() {
        assertTrue(authService.login("ADMIN", "123456"));
        assertTrue(authService.login("Admin", "123456"));
    }

    @ParameterizedTest(name = "[{index}] user=''{0}'' pass=''{1}'' → fail")
    @DisplayName("Login thất bại - các trường hợp biên / sai")
    @CsvSource({
            "admin, '', false",                    // TC_AUTH_02 mật khẩu trống
            "'', 123456, false",                   // TC_AUTH_03 username trống
            "admin, sai12345, false",              // TC_AUTH_04 sai mật khẩu
            "khongco, 123456, false",              // TC_AUTH_05 username không tồn tại
            "admin, ABCDEF, false",                // TC_AUTH_07 password phân biệt hoa thường
            "'   ', 123456, false",                // username blank
            "admin, '   ', false"                  // password blank-ish (isEmpty check)
    })
    void loginFailureCases(String username, String password, boolean expected) {
        assertEquals(expected, authService.login(username, password));
    }

    // ==================== REGISTER - VALIDATION (Parameterized + BVA) ====================

    @ParameterizedTest(name = "[{index}] user={0} email={1} pass={2} confirm={3} → valid={4}")
    @DisplayName("Kiểm tra validateRegister với giá trị biên form đăng ký")
    @CsvSource({
            // username biên
            "'', a@b.com, Pass@1234, Pass@1234, false",                          // rỗng
            "abcd, a@b.com, Pass@1234, Pass@1234, false",                        // 4 ký tự < 5
            "abcde, a@b.com, Pass@1234, Pass@1234, true",                        // 5 ký tự = biên dưới
            "abcdefghijklmnopqrst, a@b.com, Pass@1234, Pass@1234, true",         // 20 ký tự = biên trên
            "abcdefghijklmnopqrstu, a@b.com, Pass@1234, Pass@1234, false",       // 21 ký tự > 20
            // email
            "validuser, '', Pass@1234, Pass@1234, false",                        // email rỗng
            "validuser, abc.com, Pass@1234, Pass@1234, false",                   // thiếu @
            "validuser, a@b.com, Pass@1234, Pass@1234, true",                    // hợp lệ
            // password
            "validuser, a@b.com, '', '', false",                                 // password rỗng
            "validuser, a@b.com, Pass@12, Pass@12, false",                       // 7 ký tự
            "validuser, a@b.com, Pass@123, Pass@123, true",                      // 8 ký tự + special
            "validuser, a@b.com, Password123, Password123, false",               // không có ký tự đặc biệt
            // confirm
            "validuser, a@b.com, Pass@1234, Pass@4321, false"                    // không khớp
    })
    void registerValidationBoundary(
            String username, String email, String password, String confirm, boolean expectedValid) {
        boolean actual = authService.isValidRegister(username, email, password, confirm);
        assertEquals(expectedValid, actual,
                () -> "Expected valid=" + expectedValid + " nhưng validate trả về: "
                        + authService.validateRegister(username, email, password, confirm));
    }

    @Test
    @DisplayName("validateRegister trả về đúng message khi username rỗng")
    void validateRegisterEmptyUsernameMessage() {
        List<String> errors = authService.validateRegister("", "a@b.com", "Pass@1234", "Pass@1234");
        assertTrue(errors.stream().anyMatch(e -> e.contains("Username không được để trống")));
    }

    @Test
    @DisplayName("validateRegister trả về đúng message khi password thiếu ký tự đặc biệt")
    void validateRegisterNoSpecialCharMessage() {
        List<String> errors = authService.validateRegister("validuser", "a@b.com", "Password123", "Password123");
        assertTrue(errors.stream().anyMatch(e -> e.contains("ký tự đặc biệt")));
    }

    // ==================== REGISTER - EXCEPTION TESTING ====================

    @Test
    @DisplayName("assertThrows: ValidationException khi dữ liệu đăng ký không hợp lệ")
    void registerInvalidThrowsValidationException() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
                authService.register("ab", "bad", "123", "456")
        );
        assertFalse(ex.getErrors().isEmpty());
        assertNotNull(ex.getMessage());
    }

    @Test
    @DisplayName("assertThrows: DuplicateUserException khi đăng ký username đã tồn tại (admin)")
    void registerDuplicateThrowsDuplicateUserException() {
        DuplicateUserException ex = assertThrows(DuplicateUserException.class, () ->
                authService.register("admin", "new@poly.edu.vn", "Pass@1234", "Pass@1234")
        );
        assertTrue(ex.getMessage().contains("tồn tại") || ex.getMessage().toLowerCase().contains("exist"));
    }

    @Test
    @DisplayName("assertThrows: DuplicateUserException không phân biệt hoa/thường")
    void registerDuplicateCaseInsensitive() {
        assertThrows(DuplicateUserException.class, () ->
                authService.register("ADMIN", "new@poly.edu.vn", "Pass@1234", "Pass@1234")
        );
    }

    @Test
    @DisplayName("Đăng ký thành công rồi login được")
    void registerThenLoginSuccess() {
        User user = authService.register("thuan123", "thuan@poly.edu.vn", "Pass@1234", "Pass@1234");
        assertNotNull(user);
        assertEquals("thuan123", user.getUsername());
        assertTrue(authService.login("thuan123", "Pass@1234"));
    }
}
