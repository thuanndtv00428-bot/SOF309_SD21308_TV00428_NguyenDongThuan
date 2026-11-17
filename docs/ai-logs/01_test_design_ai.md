# Nhật ký AI: Thiết kế Test Plan & Test Case (Y2) + Unit Test Controller (Y3)

## 1. Ngữ cảnh & Mục tiêu
- Dự án: Website Quản Lý Sản Phẩm (Spring Boot) – các chức năng Đăng ký, Đăng nhập, CRUD Sản phẩm.
- Mục tiêu Y2: dùng AI + nội dung `docs/SPECIFICATIONS.md` làm ngữ cảnh (RAG) để sinh Test Plan và Test Case.
- Mục tiêu Y3: viết Unit Test JUnit 5 cho Controller/Service (thư mục `src/test/java/.../controllers/`), áp dụng Parameterized Test + Exception Testing, `mvn test` đạt 100% Pass.
- Công cụ AI: Claude / Grok (xAI).

## 2. Câu lệnh đã gửi cho AI (Prompt) – Y2
> "Bạn là QA Lead. Dưới đây là đặc tả nghiệp vụ dự án Web của tôi: [nội dung docs/SPECIFICATIONS.md]. Hãy áp dụng kỹ thuật Phân vùng tương đương và Phân tích giá trị biên để thiết kế Test Plan và Bộ Test Case chi tiết cho Controller và UI."

## 3. Kết quả do AI sinh ra (Raw Output) – Y2
- Test Plan: mục tiêu, phạm vi, kỹ thuật EP/BVA, công cụ, lịch trình, tiêu chí, rủi ro → xem `docs/TEST_PLAN.md`.
- Test Case: 39 ca chia 3 nhóm – Đăng nhập (9), Đăng ký (15), CRUD Sản phẩm (15) → xem `docs/TEST_CASES.md`.

## 4. Chỉnh sửa của Sinh viên & Lý do sửa (Y2)
- Điểm cần đối chiếu: các thông báo lỗi và giá trị biên trong Test Case phải khớp đúng web chạy thực tế.
- Cột "Trạng thái" đang để "Chưa chạy"; sẽ cập nhật Pass/Fail sau khi chạy test ở Y3, Y4.
- Đã rà soát message lỗi trong AuthService / ProductService khớp với TEST_CASES.md.

## 5. Unit Test Controller (Y3) – Thiết kế & Thực hiện

### 5.1. Prompt gửi AI hỗ trợ viết test
> "Dựa trên AuthService, ProductService và bộ TEST_CASES.md, hãy viết unit test JUnit 5 trong package controllers:
> - Dùng @ParameterizedTest + @CsvSource kiểm tra giá trị biên form đăng ký (username 4/5/20/21 ký tự, password 7/8, email, confirm).
> - Dùng assertThrows() kiểm thử ValidationException, DuplicateUserException, DuplicateProductException, IllegalArgumentException.
> - Mỗi test class dùng @BeforeEach tạo service mới để tránh ảnh hưởng state in-memory.
> - Mục tiêu: mvn test 100% Pass."

### 5.2. Cấu trúc test đã tạo
| File | Số test | Kỹ thuật chính |
|------|---------|----------------|
| `AuthControllerTest.java` | 28 | @ParameterizedTest + @CsvSource (login fail, register BVA), assertThrows (ValidationException, DuplicateUserException) |
| `ProductControllerTest.java` | 24 | @ParameterizedTest + @CsvSource (validate name/price/qty biên), assertThrows (ValidationException, DuplicateProductException, IllegalArgumentException) |
| `SampleControllerTest.java` | 2 | Mẫu tham khảo (giữ nguyên) |

### 5.3. Các case biên đã cover (ví dụ)
**Đăng ký (Auth):**
- Username: rỗng, 4 ký tự (fail), 5 ký tự (pass), 20 ký tự (pass), 21 ký tự (fail)
- Password: rỗng, 7 ký tự (fail), 8 ký tự + special (pass), không special (fail)
- Email: rỗng / sai định dạng
- Confirm không khớp
- Trùng username (admin) → DuplicateUserException

**Sản phẩm (Product):**
- Tên rỗng / blank
- Giá = 0, giá âm, giá > 0
- Số lượng âm, số lượng = 0 (cho phép)
- Trùng tên (không phân biệt hoa thường) → DuplicateProductException
- Update/Delete id không tồn tại → IllegalArgumentException

### 5.4. Kết quả chạy
```
mvn test
Tests run: 55, Failures: 0, Errors: 0, Skipped: 1 (Selenium LoginTest – tuần sau)
BUILD SUCCESS
```

### 5.5. Ghi chú sinh viên
- Test tập trung vào Service layer vì Controller chủ yếu ủy quyền + xử lý session/Model; logic nghiệp vụ & exception nằm ở Service.
- Không dùng MockMvc / spring-boot-starter-test (pom chỉ có junit-jupiter) → pure unit test, chạy nhanh, ổn định.
- Khi sang Y4 sẽ bổ sung UI test Selenium + cập nhật trạng thái Pass trong TEST_CASES.md.
