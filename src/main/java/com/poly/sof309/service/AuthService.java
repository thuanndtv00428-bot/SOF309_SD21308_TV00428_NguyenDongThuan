package com.poly.sof309.service;

import com.poly.sof309.exception.DuplicateUserException;
import com.poly.sof309.exception.ValidationException;
import com.poly.sof309.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/** Nghiệp vụ Đăng ký / Đăng nhập (lưu trong bộ nhớ, không cần DB). */
@Service
public class AuthService {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern SPECIAL = Pattern.compile("[^A-Za-z0-9]");

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public AuthService() {
        users.put("admin", new User("admin", "admin@poly.edu.vn", "123456"));
    }

    /** Trả về danh sách lỗi validation đăng ký; rỗng nếu hợp lệ. */
    public List<String> validateRegister(String username, String email, String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();
        if (username == null || username.isBlank()) {
            errors.add("Username không được để trống");
        } else if (username.length() < 5 || username.length() > 20) {
            errors.add("Username phải từ 5 đến 20 ký tự");
        }
        if (email == null || email.isBlank()) {
            errors.add("Email không được để trống");
        } else if (!EMAIL.matcher(email).matches()) {
            errors.add("Email không đúng định dạng");
        }
        if (password == null || password.isEmpty()) {
            errors.add("Mật khẩu không được để trống");
        } else if (password.length() < 8) {
            errors.add("Mật khẩu phải từ 8 ký tự");
        } else if (!SPECIAL.matcher(password).find()) {
            errors.add("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
        }
        if (password != null && !password.equals(confirmPassword)) {
            errors.add("Xác nhận mật khẩu không khớp");
        }
        return errors;
    }

    public boolean isValidRegister(String username, String email, String password, String confirmPassword) {
        return validateRegister(username, email, password, confirmPassword).isEmpty();
    }

    /** @throws ValidationException dữ liệu sai; @throws DuplicateUserException username đã tồn tại */
    public User register(String username, String email, String password, String confirmPassword) {
        List<String> errors = validateRegister(username, email, password, confirmPassword);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        User user = new User(username, email, password);
        if (users.putIfAbsent(username.toLowerCase(), user) != null) {
            throw new DuplicateUserException("Username đã tồn tại");
        }
        return user;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.isEmpty()) {
            return false;
        }
        User user = users.get(username.toLowerCase());
        return user != null && user.getPassword().equals(password);
    }
}
