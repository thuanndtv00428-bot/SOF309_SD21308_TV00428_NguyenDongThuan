# ĐẶC TẢ NGHIỆP VỤ WEB CÁ NHÂN (SPECIFICATIONS)

---

## 1. THÔNG TIN DỰ ÁN
* **Tên đề tài:** Website Quản Lý Sản Phẩm (đăng ký, đăng nhập, CRUD sản phẩm)
* **Sinh viên:** Nguyễn Đông Thuận – MSSV TV00428 – Lớp SD21308
* **Công nghệ:** Java 17, Spring Boot 3.2.5, Thymeleaf, lưu dữ liệu trong bộ nhớ (in-memory, không cần cài DB)
* **URL chạy local:** `http://localhost:8080`
* **Cách chạy:** `mvn spring-boot:run`

## 2. DANH SÁCH MÀN HÌNH (URL)
| URL | Mô tả | Yêu cầu đăng nhập |
| :--- | :--- | :---: |
| `/login` | Trang đăng nhập (`/` tự chuyển về đây) | Không |
| `/register` | Trang đăng ký | Không |
| `/home` | Trang chủ sau đăng nhập | Có |
| `/products` | Danh sách sản phẩm | Có |
| `/products/new` | Form thêm sản phẩm | Có |
| `/products/{id}/edit` | Form sửa sản phẩm | Có |
| `/logout` | Đăng xuất, về `/login` | – |

Truy cập trang yêu cầu đăng nhập khi chưa đăng nhập → chuyển hướng về `/login`.

## 3. QUY TẮC NGHIỆP VỤ (BUSINESS RULES)

### 3.1 Đăng ký (`/register`)
| Trường | Id phần tử | Ràng buộc | Thông báo lỗi |
| :--- | :--- | :--- | :--- |
| username | `username` | Bắt buộc, 5–20 ký tự, không trùng | "Username không được để trống" / "Username phải từ 5 đến 20 ký tự" / "Username đã tồn tại" |
| email | `email` | Bắt buộc, đúng định dạng email | "Email không được để trống" / "Email không đúng định dạng" |
| password | `password` | Bắt buộc, tối thiểu 8 ký tự, có ≥ 1 ký tự đặc biệt | "Mật khẩu không được để trống" / "Mật khẩu phải từ 8 ký tự" / "Mật khẩu phải có ít nhất 1 ký tự đặc biệt" |
| confirmPassword | `confirmPassword` | Phải trùng `password` | "Xác nhận mật khẩu không khớp" |

* Thành công → chuyển về `/login`, hiện "Đăng ký thành công! Hãy đăng nhập." (`#success-message`).
* Lỗi hiển thị trong `#error-message`.

### 3.2 Đăng nhập (`/login`)
* Tài khoản có sẵn: `admin` / `123456`.
* Đúng tài khoản/mật khẩu → chuyển hướng `/home`.
* Sai → hiện "Sai tên đăng nhập hoặc mật khẩu".
* Để trống mật khẩu → "Mật khẩu không được để trống"; để trống username → "Username không được để trống".
* Username không phân biệt hoa/thường; mật khẩu có phân biệt.

### 3.3 CRUD Sản phẩm (`/products`)
| Trường | Id phần tử | Ràng buộc | Thông báo lỗi |
| :--- | :--- | :--- | :--- |
| Tên sản phẩm | `name` | Bắt buộc, không trùng (không phân biệt hoa/thường) | "Tên sản phẩm không được để trống" / "Tên sản phẩm đã tồn tại" |
| Giá bán | `price` | Số, > 0 | "Giá bán phải lớn hơn 0" |
| Số lượng | `quantity` | Số nguyên, ≥ 0 | "Số lượng phải lớn hơn hoặc bằng 0" |

* Nhập chữ vào giá/số lượng → "Giá bán và số lượng phải là số hợp lệ".
* **Thêm:** nút `#btn-add` → form → `#btn-save` → về danh sách, hiện "Thêm sản phẩm thành công".
* **Sửa:** nút "Sửa" → form có sẵn dữ liệu → Lưu → "Cập nhật sản phẩm thành công". Khi sửa, được giữ nguyên tên của chính sản phẩm đó.
* **Xóa:** nút "Xóa" → hộp thoại xác nhận → đồng ý mới xóa, hiện "Xóa sản phẩm thành công"; hủy thì không xóa.
* Dữ liệu mẫu ban đầu: "Laptop Dell XPS" (25.000.000 – SL 10), "Chuột Logitech" (350.000 – SL 50).
* Bảng dữ liệu có id `product-table`.

## 4. LỚP NGHIỆP VỤ (phục vụ Unit Test – Y3)
* `AuthService`: `validateRegister(...)`, `register(...)` (ném `ValidationException`, `DuplicateUserException`), `login(...)`.
* `ProductService`: `add`, `update`, `delete`, `findAll`, `findById` (ném `ValidationException`, `DuplicateProductException`, `IllegalArgumentException` khi không tìm thấy id).
* Controller: `AuthController`, `ProductController`.
