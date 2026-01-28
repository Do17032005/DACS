# ✅ QUICK TEST CHECKLIST - CLOTHESSHOPONLINE

> Checklist nhanh để kiểm tra các chức năng chính trước khi release

---

## 🔐 AUTHENTICATION (5-10 phút)

- [ ] Đăng ký tài khoản mới thành công
- [ ] Đăng nhập với tài khoản customer
- [ ] Đăng nhập với tài khoản admin
- [ ] Đăng xuất hoạt động đúng
- [ ] Email/password sai → Hiển thị lỗi đúng
- [ ] Remember me hoạt động

---

## 🛍️ CUSTOMER FLOW (15-20 phút)

### Trang Chủ

- [ ] Banner/slider hiển thị
- [ ] Sản phẩm mới hiển thị
- [ ] Sản phẩm bán chạy hiển thị
- [ ] Menu dropdown (NAM/NỮ) có subcategories

### Tìm Kiếm & Lọc

- [ ] Search box hoạt động
- [ ] Filter theo category (NAM/NỮ)
- [ ] Filter theo subcategory (Áo thun, Quần jean...)
- [ ] Filter kết hợp: category + subcategory hoạt động đúng
- [ ] Sắp xếp (giá, tên, mới nhất) hoạt động

### Sản Phẩm

- [ ] Click menu "Áo thun" → Chỉ hiển thị áo thun
- [ ] Chi tiết sản phẩm hiển thị đầy đủ
- [ ] Gallery hình ảnh hoạt động
- [ ] Chọn size/màu hoạt động
- [ ] Thêm vào giỏ thành công

### Giỏ Hàng & Thanh Toán

- [ ] Xem giỏ hàng hiển thị đúng sản phẩm
- [ ] Cập nhật số lượng hoạt động
- [ ] Xóa sản phẩm hoạt động
- [ ] Áp dụng voucher (test 1 mã hợp lệ)
- [ ] Checkout thành công
- [ ] Đơn hàng được tạo và hiển thị trong "Đơn của tôi"

### Tài Khoản

- [ ] Xem thông tin cá nhân
- [ ] Cập nhật thông tin thành công
- [ ] Xem danh sách đơn hàng
- [ ] Xem chi tiết 1 đơn
- [ ] Thêm địa chỉ mới
- [ ] Hủy đơn hàng (trạng thái "Chờ xác nhận")

---

## 🔧 ADMIN PANEL (15-20 phút)

### Login & Dashboard

- [ ] Đăng nhập admin thành công
- [ ] Dashboard hiển thị thống kê
- [ ] Biểu đồ hiển thị (nếu có)

### Quản Lý Category

- [ ] Xem danh sách categories
- [ ] Thêm category mới với subcategories:
  ```
  Tên: TEST_CATEGORY
  Subcategories: Test Sub 1,Test Sub 2,Test Sub 3
  ```
- [ ] Subcategories hiển thị dạng badges trong table
- [ ] Sửa category → Update subcategories
- [ ] Xóa category test

### Quản Lý Sản Phẩm

- [ ] Xem danh sách sản phẩm
- [ ] Thêm sản phẩm mới:
  - [ ] Chọn category → Dropdown subcategory tự động populate
  - [ ] Chọn subcategory từ dropdown
  - [ ] Upload hình ảnh thành công
  - [ ] Validation hoạt động (bỏ trống tên → Lỗi)
  - [ ] Lưu thành công
- [ ] Sản phẩm mới hiển thị ở trang customer
- [ ] Sửa sản phẩm:
  - [ ] Form hiển thị data cũ
  - [ ] Dropdown subcategory giữ nguyên giá trị đã chọn
  - [ ] Cập nhật thành công
- [ ] Xóa sản phẩm test

### Quản Lý Đơn Hàng

- [ ] Xem danh sách đơn
- [ ] Xem chi tiết đơn
- [ ] Cập nhật trạng thái: "Chờ xác nhận" → "Đang xử lý"
- [ ] Filter theo trạng thái

### Quản Lý Voucher

- [ ] Thêm voucher mới
- [ ] Validation hoạt động
- [ ] Xóa voucher test

### Quản Lý User

- [ ] Xem danh sách users
- [ ] Search user
- [ ] Thêm user mới (test)
- [ ] Xóa user test

---

## 🔒 SECURITY (10 phút)

### Validation

- [ ] Form validation hoạt động (email sai format, password yếu)
- [ ] Server-side validation hoạt động (không chỉ client)

### File Upload

- [ ] Upload file .jpg, .png thành công
- [ ] Upload file .exe, .php → Bị reject
- [ ] File > 5MB → Lỗi "File quá lớn"

### XSS Prevention

- [ ] Nhập `<script>alert('test')</script>` vào tên sản phẩm
- [ ] Lưu và kiểm tra → Script không chạy, hiển thị dạng text

### SQL Injection

- [ ] Login với email: `admin' OR '1'='1`
- [ ] Không đăng nhập được

### Authorization

- [ ] Logout admin, login customer
- [ ] Truy cập `/admin/dashboard`
- [ ] Bị chặn (403 hoặc redirect)

### Password

- [ ] Kiểm tra database → Password đã hash (BCrypt)
- [ ] Không thấy plain text password

---

## 📱 RESPONSIVE (10 phút)

### Desktop (1920x1080)

- [ ] Layout hiển thị đẹp
- [ ] Tất cả chức năng hoạt động

### Tablet (iPad - 768px)

- [ ] Layout responsive
- [ ] Menu hoạt động
- [ ] Chức năng hoạt động đầy đủ

### Mobile (iPhone - 375px)

- [ ] Menu hamburger hoạt động
- [ ] Không có horizontal scroll
- [ ] Form dễ nhập liệu
- [ ] Button đủ lớn để click
- [ ] Cart, checkout hoạt động
- [ ] Search hoạt động

---

## 🌐 BROWSER COMPATIBILITY (10 phút)

- [ ] Chrome: Tất cả hoạt động
- [ ] Firefox: Tất cả hoạt động
- [ ] Edge: Tất cả hoạt động
- [ ] Safari (nếu có Mac): Tất cả hoạt động

---

## ⚡ PERFORMANCE (5 phút)

- [ ] Trang chủ load < 3 giây
- [ ] Search trả kết quả < 1 giây
- [ ] Hình ảnh được optimize (không quá nặng)
- [ ] Không có memory leak (kiểm tra DevTools)

---

## 🎯 CRITICAL PATH (Smoke Test - 5 phút)

> Test nhanh flow chính nhất:

1. [ ] Vào trang chủ
2. [ ] Click menu "Áo thun NAM"
3. [ ] Chọn 1 sản phẩm
4. [ ] Thêm vào giỏ
5. [ ] Thanh toán
6. [ ] Đơn hàng thành công
7. [ ] Login admin
8. [ ] Xem đơn vừa đặt trong admin panel
9. [ ] Cập nhật trạng thái đơn

---

## ✅ FINAL CHECKS

- [ ] Không có console errors (F12)
- [ ] Không có broken images
- [ ] Tất cả links hoạt động
- [ ] Email notifications gửi đúng (nếu có)
- [ ] Database không có lỗi
- [ ] Server logs sạch (không có exceptions)

---

## 📊 RESULT

**Total Checks:** [ ] / 100+

**Pass Rate:** [ ]%

**Ready for Release?** ✅ YES / ❌ NO

**Blockers:**

1. [List critical issues]

---

## 🔄 REGRESSION TEST (Sau mỗi lần fix bug)

- [ ] Re-test tất cả test cases bị fail
- [ ] Test các chức năng liên quan
- [ ] Smoke test lại critical path
- [ ] Verify bug đã fix thành công

---

**Người test:** ******\_\_\_******  
**Ngày:** ******\_\_\_******  
**Chữ ký:** ******\_\_\_******
