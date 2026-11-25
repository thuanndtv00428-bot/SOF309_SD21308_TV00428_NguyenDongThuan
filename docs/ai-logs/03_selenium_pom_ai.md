# Nhật ký AI: Xây dựng Selenium Page Object Model (Tuần 3 – Y4 P1)

## 1. Ngữ cảnh & Mục tiêu
- Xây dựng khung tự động hóa giao diện Web bằng Selenium WebDriver theo mô hình **Page Object Model (POM)**.
- Tạo các Page Classes: `LoginPage`, `RegisterPage`, `ProductPage`.
- Viết test scripts: `LoginTest`, `CrudTest`.
- Sử dụng `WebDriverWait` (explicit wait), tuyệt đối không dùng `Thread.sleep()`.

## 2. Câu lệnh đã gửi cho AI (Prompt)
> "Dựa trên HTML templates (login.html, register.html, products.html, product-form.html) và AuthService (admin/123456), hãy viết Page Object Model cho LoginPage, RegisterPage, ProductPage dùng Selenium 4 + WebDriverWait. Sau đó viết LoginTest và CrudTest theo JUnit 5, hỗ trợ headless khi CI=true. Locator ưu tiên By.id."

## 3. Kết quả do AI sinh ra (Raw Output)
- AI sinh đầy đủ 3 Page Classes với locator đúng id trong template.
- Test dùng `@Disabled` để tránh fail khi chưa chạy Spring Boot local.
- Hỗ trợ Chrome headless khi biến môi trường `CI=true` (phục vụ GitHub Actions tuần 5).

## 4. Chỉnh sửa của Sinh viên & Lý do sửa
- Sửa mật khẩu mặc định từ "123" → "123456" (khớp AuthService).
- Thêm `By.id("btn-login")` thay vì chỉ dựa vào `//button[@type='submit']` cho rõ ràng.
- Tách rõ `open(url)` và hành động nghiệp vụ `login(...)`, `register(...)`, `addProduct(...)`.
- CrudTest: tạo tên sản phẩm unique bằng timestamp để tránh trùng khi chạy nhiều lần.
- Giữ `@Disabled` trên test UI vì cần server local đang chạy; bỏ comment khi demo.

## 5. Cấu trúc thư mục nghiệm thu Tuần 3
```
src/test/java/com/poly/sof309/ui/
├── pages/
│   ├── LoginPage.java
│   ├── RegisterPage.java
│   └── ProductPage.java
└── tests/
    ├── LoginTest.java
    └── CrudTest.java
```

## 6. Cách chạy local
1. Terminal 1: `mvn spring-boot:run`
2. Terminal 2: Bỏ `@Disabled` trên method test cần chạy → `mvn test -Dtest=LoginTest,CrudTest`
3. Chrome sẽ tự mở, tự nhập liệu và assert.
