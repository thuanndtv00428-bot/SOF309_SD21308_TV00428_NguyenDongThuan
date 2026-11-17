# KẾ HOẠCH KIỂM THỬ (TEST PLAN)

* **Dự án:** Website Quản Lý Sản Phẩm
* **Sinh viên:** Nguyễn Đông Thuận – TV00428 – SD21308
* **Tài liệu tham chiếu:** `docs/SPECIFICATIONS.md`, `docs/TEST_CASES.md`

---

## 1. MỤC TIÊU KIỂM THỬ
* Kiểm thử logic backend (Unit Test với JUnit 5 cho Controller/Service).
* Kiểm thử giao diện tự động (Automation UI Test với Selenium WebDriver, mô hình Page Object).
* Tích hợp kiểm thử tự động với GitHub Actions CI/CD.

## 2. PHẠM VI KIỂM THỬ
* **In-Scope:** Đăng ký, Đăng nhập, Đăng xuất, bảo vệ trang cần đăng nhập, CRUD Sản phẩm (thêm, sửa, xóa có xác nhận, hiển thị danh sách).
* **Out-of-Scope:** Kiểm thử hiệu năng, bảo mật chuyên sâu, tương thích đa trình duyệt (chỉ Chrome), lưu trữ lâu dài (dữ liệu in-memory).

## 3. KỸ THUẬT THIẾT KẾ TEST
* **Phân vùng tương đương (EP):** chia dữ liệu đầu vào thành nhóm hợp lệ/không hợp lệ (ví dụ username rỗng / <5 / 5–20 / >20 ký tự).
* **Phân tích giá trị biên (BVA):** username 4, 5, 20, 21 ký tự; password 7, 8 ký tự; giá −1, 0, 0.01; số lượng −1, 0, 1.
* **Kiểm thử âm (Negative):** dữ liệu rỗng, sai định dạng, trùng lặp, truy cập khi chưa đăng nhập.

## 4. CÔNG CỤ & MÔI TRƯỜNG
| Hạng mục | Chi tiết |
| :--- | :--- |
| Ngôn ngữ / Build | Java 17, Maven |
| Unit Test | JUnit 5 (`@ParameterizedTest`, `assertThrows`, `@Timeout`) |
| UI Test | Selenium WebDriver 4, Chrome (headless khi chạy CI) |
| Web đích | `http://localhost:8080` |
| CI/CD | GitHub Actions (`.github/workflows/test.yml`) |

## 5. LỊCH TRÌNH
| Giai đoạn | Nội dung | Yêu cầu |
| :--- | :--- | :---: |
| Tuần 1 | Chạy web local, đặc tả, Test Plan, Test Case | Y1, Y2 |
| Tuần 2 | Unit Test Controller/Service | Y3 |
| Tuần 3–4 | Selenium POM, TestNG/Groups/DataProvider, chụp ảnh lỗi | Y4 |
| Tuần 5 | GitHub Actions xanh | Y5 |
| Tuần 6 | AI logs, review, slide | Y6 |

## 6. TIÊU CHÍ BẮT ĐẦU / HOÀN THÀNH
* **Bắt đầu:** web khởi động được tại `localhost:8080`; đặc tả đã chốt.
* **Hoàn thành:** 100% test script chạy pass; mọi Test Case mức High được thực thi; pipeline GitHub Actions báo xanh.

## 7. RỦI RO
| Rủi ro | Giảm thiểu |
| :--- | :--- |
| Dữ liệu in-memory mất khi khởi động lại | Test tự tạo dữ liệu riêng, dùng tên sản phẩm duy nhất |
| Selenium flaky do bất đồng bộ | Dùng `WebDriverWait`, không dùng `Thread.sleep()` |
| Chrome/driver lệch phiên bản trên CI | Dùng Selenium Manager (Selenium 4) |
