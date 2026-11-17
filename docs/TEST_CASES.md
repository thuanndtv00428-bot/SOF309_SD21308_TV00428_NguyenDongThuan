# BỘ KỊCH BẢN KIỂM THỬ (TEST CASES)

> Thiết kế theo Phân vùng tương đương & Giá trị biên dựa trên `docs/SPECIFICATIONS.md`.
> Cột **Trạng thái** để "Chưa chạy" cho tới khi thực thi test (Y3, Y4); cập nhật Pass/Fail sau khi chạy.
> Mức ưu tiên: H = High, M = Medium.

## A. ĐĂNG NHẬP

| Test Case ID | Chức năng | Mô tả | Loại | Ưu tiên | Các bước thực hiện | Dữ liệu kiểm thử | Kết quả mong đợi | Trạng thái |
| :---: | :---: | :--- | :---: | :---: | :--- | :--- | :--- | :---: |
| **TC_AUTH_01** | Login | Đăng nhập thành công | Pos | H | 1. Mở `/login`<br>2. Nhập username, password<br>3. Bấm Đăng nhập | `admin` / `123456` | Chuyển tới `/home`, hiện "Xin chào, admin" | Chưa chạy |
| **TC_AUTH_02** | Login | Để trống mật khẩu | Neg | H | 1. Nhập username<br>2. Bỏ trống password<br>3. Bấm Đăng nhập | `admin` / `""` | Báo "Mật khẩu không được để trống", ở lại `/login` | Chưa chạy |
| **TC_AUTH_03** | Login | Để trống username | Neg | M | 1. Bỏ trống username<br>2. Nhập password<br>3. Bấm Đăng nhập | `""` / `123456` | Báo "Username không được để trống" | Chưa chạy |
| **TC_AUTH_04** | Login | Sai mật khẩu | Neg | H | Nhập đúng username, sai password | `admin` / `sai12345` | Báo "Sai tên đăng nhập hoặc mật khẩu" | Chưa chạy |
| **TC_AUTH_05** | Login | Username không tồn tại | Neg | M | Nhập username lạ | `khongco` / `123456` | Báo "Sai tên đăng nhập hoặc mật khẩu" | Chưa chạy |
| **TC_AUTH_06** | Login | Username không phân biệt hoa/thường | Pos | M | Nhập `ADMIN` | `ADMIN` / `123456` | Đăng nhập thành công | Chưa chạy |
| **TC_AUTH_07** | Login | Password phân biệt hoa/thường | Neg | M | Nhập password viết hoa | `admin` / `ABCDEF` | Đăng nhập thất bại | Chưa chạy |
| **TC_AUTH_08** | Logout | Đăng xuất | Pos | M | Đăng nhập → bấm Đăng xuất | – | Về `/login`; truy cập `/home` bị chuyển lại `/login` | Chưa chạy |
| **TC_AUTH_09** | Phân quyền | Vào `/products` khi chưa đăng nhập | Neg | H | Mở trực tiếp `/products` | – | Chuyển hướng về `/login` | Chưa chạy |

## B. ĐĂNG KÝ (EP + BVA)

| Test Case ID | Chức năng | Mô tả | Loại | Ưu tiên | Các bước | Dữ liệu (username / email / password / confirm) | Kết quả mong đợi | Trạng thái |
| :---: | :---: | :--- | :---: | :---: | :--- | :--- | :--- | :---: |
| **TC_REG_01** | Register | Đăng ký hợp lệ | Pos | H | Điền form hợp lệ → Đăng ký | `thuan123` / `a@b.com` / `Pass@1234` / `Pass@1234` | Về `/login`, "Đăng ký thành công! Hãy đăng nhập." | Chưa chạy |
| **TC_REG_02** | Register | Username rỗng | Neg | H | Bỏ trống username | `""` / `a@b.com` / `Pass@1234` / `Pass@1234` | "Username không được để trống" | Chưa chạy |
| **TC_REG_03** | Register | Username biên dưới 4 ký tự | Neg | H | BVA | `abcd` / … | "Username phải từ 5 đến 20 ký tự" | Chưa chạy |
| **TC_REG_04** | Register | Username biên dưới 5 ký tự | Pos | H | BVA | `abcde` / … | Đăng ký thành công | Chưa chạy |
| **TC_REG_05** | Register | Username biên trên 20 ký tự | Pos | M | BVA | `abcdefghijklmnopqrst` (20) | Đăng ký thành công | Chưa chạy |
| **TC_REG_06** | Register | Username 21 ký tự | Neg | M | BVA | `abcdefghijklmnopqrstu` (21) | "Username phải từ 5 đến 20 ký tự" | Chưa chạy |
| **TC_REG_07** | Register | Username đã tồn tại | Neg | H | Đăng ký lại `admin` | `admin` / `a@b.com` / `Pass@1234` / `Pass@1234` | "Username đã tồn tại" | Chưa chạy |
| **TC_REG_08** | Register | Email rỗng | Neg | M | Bỏ trống email | email `""` | "Email không được để trống" | Chưa chạy |
| **TC_REG_09** | Register | Email sai định dạng | Neg | H | Nhập email thiếu `@` | `abc.com` | "Email không đúng định dạng" | Chưa chạy |
| **TC_REG_10** | Register | Mật khẩu 7 ký tự (biên) | Neg | H | BVA | `Pass@12` | "Mật khẩu phải từ 8 ký tự" | Chưa chạy |
| **TC_REG_11** | Register | Mật khẩu 8 ký tự có ký tự đặc biệt (biên) | Pos | H | BVA | `Pass@123` | Đăng ký thành công | Chưa chạy |
| **TC_REG_12** | Register | Mật khẩu không có ký tự đặc biệt | Neg | H | Nhập `Password123` | `Password123` | "Mật khẩu phải có ít nhất 1 ký tự đặc biệt" | Chưa chạy |
| **TC_REG_13** | Register | Mật khẩu rỗng | Neg | M | Bỏ trống password | `""` | "Mật khẩu không được để trống" | Chưa chạy |
| **TC_REG_14** | Register | Xác nhận mật khẩu không khớp | Neg | H | Nhập confirm khác | `Pass@1234` / `Pass@4321` | "Xác nhận mật khẩu không khớp" | Chưa chạy |
| **TC_REG_15** | Register | Đăng ký xong đăng nhập được | Pos | M | TC_REG_01 → đăng nhập bằng tài khoản mới | `thuan123` / `Pass@1234` | Vào `/home` | Chưa chạy |

## C. CRUD SẢN PHẨM

| Test Case ID | Chức năng | Mô tả | Loại | Ưu tiên | Các bước thực hiện | Dữ liệu kiểm thử (Tên / Giá / SL) | Kết quả mong đợi | Trạng thái |
| :---: | :---: | :--- | :---: | :---: | :--- | :--- | :--- | :---: |
| **TC_CRUD_01** | Read | Hiển thị danh sách sản phẩm | Pos | H | Đăng nhập → vào `/products` | – | Bảng hiển thị ≥ 2 dòng dữ liệu mẫu | Chưa chạy |
| **TC_CRUD_02** | Create | Thêm sản phẩm hợp lệ | Pos | H | Bấm Thêm → điền → Lưu | `Bàn phím cơ` / `1200000` / `20` | "Thêm sản phẩm thành công", dòng mới xuất hiện | Chưa chạy |
| **TC_CRUD_03** | Create | Tên rỗng | Neg | H | Để trống tên | `""` / `1000` / `1` | "Tên sản phẩm không được để trống", không lưu | Chưa chạy |
| **TC_CRUD_04** | Create | Tên trùng (hoa/thường khác) | Neg | H | Thêm `laptop dell xps` | `laptop dell xps` / `1000` / `1` | "Tên sản phẩm đã tồn tại" | Chưa chạy |
| **TC_CRUD_05** | Create | Giá = 0 (biên) | Neg | H | BVA | `SP A` / `0` / `1` | "Giá bán phải lớn hơn 0" | Chưa chạy |
| **TC_CRUD_06** | Create | Giá âm | Neg | M | EP | `SP A` / `-1` / `1` | "Giá bán phải lớn hơn 0" | Chưa chạy |
| **TC_CRUD_07** | Create | Giá nhỏ nhất hợp lệ 0.01 (biên) | Pos | M | BVA | `SP B` / `0.01` / `1` | Thêm thành công | Chưa chạy |
| **TC_CRUD_08** | Create | Số lượng = −1 (biên) | Neg | H | BVA | `SP C` / `1000` / `-1` | "Số lượng phải lớn hơn hoặc bằng 0" | Chưa chạy |
| **TC_CRUD_09** | Create | Số lượng = 0 (biên) | Pos | H | BVA | `SP D` / `1000` / `0` | Thêm thành công | Chưa chạy |
| **TC_CRUD_10** | Create | Giá nhập chữ | Neg | M | Nhập `abc` vào giá | `SP E` / `abc` / `1` | "Giá bán và số lượng phải là số hợp lệ" | Chưa chạy |
| **TC_CRUD_11** | Update | Sửa sản phẩm hợp lệ | Pos | H | Bấm Sửa → đổi giá → Lưu | giá `26000000` | "Cập nhật sản phẩm thành công", giá mới hiển thị | Chưa chạy |
| **TC_CRUD_12** | Update | Lưu lại giữ nguyên tên của chính nó | Pos | M | Sửa chỉ số lượng, giữ tên | giữ tên cũ | Cập nhật thành công (không báo trùng) | Chưa chạy |
| **TC_CRUD_13** | Update | Sửa thành tên trùng sản phẩm khác | Neg | M | Đổi tên thành `Chuột Logitech` | – | "Tên sản phẩm đã tồn tại" | Chưa chạy |
| **TC_CRUD_14** | Delete | Xóa có xác nhận (đồng ý) | Pos | H | Bấm Xóa → OK | – | "Xóa sản phẩm thành công", dòng biến mất | Chưa chạy |
| **TC_CRUD_15** | Delete | Xóa nhưng hủy xác nhận | Neg | M | Bấm Xóa → Cancel | – | Dòng vẫn còn | Chưa chạy |
