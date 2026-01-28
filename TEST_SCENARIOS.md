# 📋 KỊCH BẢN KIỂM TRA - CLOTHESSHOPONLINE

## 📌 Thông Tin Chung

- **Dự án**: Website Thương Mại Điện Tử - Quần Áo Online
- **Phiên bản**: 2.0
- **Ngày tạo**: 28/01/2026
- **Mục đích**: Kiểm tra toàn bộ chức năng và phát hiện lỗi trước khi deployment

---

## 🔧 YÊU CẦU TRƯỚC KHI TEST

### Môi Trường Test

- **URL Test**: `http://localhost:8080`
- **Database**: MySQL (Aiven Cloud hoặc Local)
- **Browser**: Chrome (latest), Firefox (latest), Edge (latest)
- **Thiết bị**: Desktop, Tablet (iPad), Mobile (iPhone/Android)

### Tài Khoản Test Đã Chuẩn Bị

#### Admin Account

```
Email: admin@clothesshop.com
Password: Admin@123456
```

#### Customer Accounts

```
Tài khoản 1:
Email: customer1@test.com
Password: Test@123456

Tài khoản 2:
Email: customer2@test.com
Password: Test@123456

Tài khoản mới (để test đăng ký):
Email: newuser@test.com
Password: NewUser@2026
```

### Test Data Cần Chuẩn Bị

- Ít nhất 20 sản phẩm trong database
- 2-3 danh mục (NAM, NỮ, TRẺ EM)
- Mỗi danh mục có 5-10 subcategories
- 5-10 vouchers (active và expired)
- 10+ orders với các trạng thái khác nhau

---

## 📱 PHẦN 1: KIỂM TRA CHỨC NĂNG KHÁCH HÀNG (CUSTOMER)

### TC-001: Đăng Ký Tài Khoản

#### Test Case 1.1: Đăng ký thành công

**Bước thực hiện:**

1. Truy cập trang chủ
2. Click "Đăng ký" ở menu
3. Điền thông tin hợp lệ:
   - Họ tên: Nguyễn Văn A
   - Email: newuser@test.com
   - Số điện thoại: 0912345678
   - Mật khẩu: NewUser@2026
   - Xác nhận mật khẩu: NewUser@2026
4. Click "Đăng ký"

**Kết quả mong đợi:**

- ✅ Hiển thị thông báo "Đăng ký thành công"
- ✅ Tự động đăng nhập và chuyển về trang chủ
- ✅ Hiển thị tên user ở header
- ✅ Email xác nhận được gửi (nếu có)

#### Test Case 1.2: Đăng ký với email trùng

**Bước thực hiện:**

1. Truy cập form đăng ký
2. Nhập email đã tồn tại: customer1@test.com
3. Điền các thông tin khác hợp lệ
4. Click "Đăng ký"

**Kết quả mong đợi:**

- ✅ Hiển thị lỗi "Email đã được sử dụng"
- ✅ Form không submit
- ✅ Dữ liệu nhập vẫn được giữ lại

#### Test Case 1.3: Đăng ký với mật khẩu yếu

**Bước thực hiện:**

1. Nhập mật khẩu: 123456
2. Submit form

**Kết quả mong đợi:**

- ✅ Hiển thị lỗi validation
- ✅ Yêu cầu mật khẩu ít nhất 8 ký tự, có chữ hoa, số, ký tự đặc biệt

#### Test Case 1.4: Đăng ký với dữ liệu không hợp lệ

**Test các trường hợp:**

- Email sai format: abc@, @gmail.com, abc
- Số điện thoại sai format: 123, abcdef
- Mật khẩu không khớp
- Bỏ trống các trường bắt buộc

**Kết quả mong đợi:**

- ✅ Mỗi lỗi hiển thị message rõ ràng
- ✅ Highlight field bị lỗi màu đỏ

---

### TC-002: Đăng Nhập

#### Test Case 2.1: Đăng nhập thành công

**Bước thực hiện:**

1. Click "Đăng nhập"
2. Nhập email: customer1@test.com
3. Nhập password: Test@123456
4. Click "Đăng nhập"

**Kết quả mong đợi:**

- ✅ Đăng nhập thành công
- ✅ Chuyển về trang chủ hoặc trang trước đó
- ✅ Hiển thị tên user ở header
- ✅ Menu thay đổi từ "Đăng nhập" → "Tài khoản", "Đăng xuất"

#### Test Case 2.2: Đăng nhập sai mật khẩu

**Kết quả mong đợi:**

- ✅ Hiển thị "Email hoặc mật khẩu không đúng"
- ✅ Không tiết lộ thông tin email có tồn tại hay không

#### Test Case 2.3: Đăng nhập với tài khoản không tồn tại

**Kết quả mong đợi:**

- ✅ Hiển thị "Email hoặc mật khẩu không đúng"

#### Test Case 2.4: Remember Me

**Bước thực hiện:**

1. Đăng nhập và tick "Ghi nhớ đăng nhập"
2. Đóng browser
3. Mở lại browser và truy cập website

**Kết quả mong đợi:**

- ✅ Vẫn đăng nhập (session được giữ)

---

### TC-003: Trang Chủ

#### Test Case 3.1: Hiển thị trang chủ

**Kiểm tra:**

- ✅ Header hiển thị đầy đủ: Logo, Menu, Search, Cart
- ✅ Banner/Slider hoạt động
- ✅ Hiển thị sản phẩm mới (New Products)
- ✅ Hiển thị sản phẩm bán chạy (Bestsellers)
- ✅ Footer hiển thị đầy đủ thông tin
- ✅ Responsive trên mobile/tablet

#### Test Case 3.2: Menu Category

**Bước thực hiện:**

1. Hover vào menu "NAM"
2. Kiểm tra dropdown subcategories
3. Click vào "Áo thun"

**Kết quả mong đợi:**

- ✅ Dropdown hiển thị đầy đủ subcategories
- ✅ Click vào subcategory → Chuyển đến trang products đã filter
- ✅ URL: `/products?category=NAM&subcategory=Áo thun`

---

### TC-004: Tìm Kiếm & Lọc Sản Phẩm

#### Test Case 4.1: Tìm kiếm cơ bản

**Bước thực hiện:**

1. Nhập "áo thun" vào search box
2. Click Search hoặc Enter

**Kết quả mong đợi:**

- ✅ Hiển thị danh sách sản phẩm chứa "áo thun"
- ✅ Highlight từ khóa tìm kiếm
- ✅ Hiển thị số lượng kết quả

#### Test Case 4.2: Tìm kiếm không có kết quả

**Bước thực hiện:**

1. Tìm kiếm: "xyz123notfound"

**Kết quả mong đợi:**

- ✅ Hiển thị "Không tìm thấy sản phẩm nào"
- ✅ Gợi ý tìm kiếm khác hoặc xem tất cả sản phẩm

#### Test Case 4.3: Filter theo category

**Bước thực hiện:**

1. Truy cập /products
2. Click filter "NAM"

**Kết quả mong đợi:**

- ✅ Chỉ hiển thị sản phẩm nam
- ✅ Filter button highlight/active
- ✅ URL update: /products?category=NAM

#### Test Case 4.4: Filter kết hợp (Category + Subcategory)

**Bước thực hiện:**

1. Filter category: NAM
2. Filter subcategory: Áo thun

**Kết quả mong đợi:**

- ✅ Chỉ hiển thị áo thun nam
- ✅ URL: /products?category=NAM&subcategory=Áo thun

#### Test Case 4.5: Filter theo giá

**Kiểm tra:**

- ✅ Filter: Dưới 200k
- ✅ Filter: 200k - 500k
- ✅ Filter: 500k - 1M
- ✅ Filter: Trên 1M
- ✅ Sản phẩm hiển thị đúng khoảng giá

#### Test Case 4.6: Sắp xếp sản phẩm

**Test các option:**

- ✅ Mới nhất
- ✅ Giá tăng dần
- ✅ Giá giảm dần
- ✅ Bán chạy nhất
- ✅ Tên A-Z

---

### TC-005: Chi Tiết Sản Phẩm

#### Test Case 5.1: Xem chi tiết sản phẩm

**Bước thực hiện:**

1. Click vào 1 sản phẩm bất kỳ

**Kết quả mong đợi:**

- ✅ Hiển thị đầy đủ thông tin:
  - Tên, giá, giá gốc, % giảm
  - Hình ảnh chính + gallery hình phụ
  - Mô tả chi tiết
  - Màu sắc, size, chất liệu, thương hiệu
  - Số lượng tồn kho
  - Đánh giá sao
- ✅ Gallery hình ảnh hoạt động (click zoom, slide)
- ✅ Sản phẩm liên quan hiển thị

#### Test Case 5.2: Chọn size

**Bước thực hiện:**

1. Click chọn size: S, M, L, XL

**Kết quả mong đợi:**

- ✅ Size được highlight khi chọn
- ✅ Nếu size hết hàng → disabled/grayed out

#### Test Case 5.3: Thay đổi số lượng

**Bước thực hiện:**

1. Click tăng/giảm số lượng
2. Nhập số lượng vào input

**Kết quả mong đợi:**

- ✅ Số lượng thay đổi chính xác
- ✅ Không cho nhập số âm
- ✅ Không cho nhập > stock
- ✅ Giá tổng cập nhật (nếu có)

#### Test Case 5.4: Thêm vào giỏ hàng

**Bước thực hiện:**

1. Chọn size: M
2. Chọn số lượng: 2
3. Click "Thêm vào giỏ"

**Kết quả mong đợi:**

- ✅ Hiển thị thông báo thành công
- ✅ Icon giỏ hàng cập nhật số lượng (+2)
- ✅ Có thể tiếp tục mua hoặc xem giỏ hàng

#### Test Case 5.5: Đánh giá sản phẩm

**Bước thực hiện:**

1. Đăng nhập
2. Scroll xuống phần đánh giá
3. Click số sao (1-5)
4. Nhập nội dung review
5. Submit

**Kết quả mong đợi:**

- ✅ Đánh giá được lưu và hiển thị
- ✅ Rating trung bình cập nhật
- ✅ Hiển thị tên user, thời gian đánh giá

---

### TC-006: Giỏ Hàng

#### Test Case 6.1: Xem giỏ hàng

**Bước thực hiện:**

1. Thêm 3 sản phẩm khác nhau vào giỏ
2. Click icon giỏ hàng hoặc truy cập /cart

**Kết quả mong đợi:**

- ✅ Hiển thị danh sách 3 sản phẩm
- ✅ Mỗi sản phẩm có: hình, tên, size, màu, giá, số lượng
- ✅ Tổng tiền tính đúng
- ✅ Button "Tiếp tục mua" và "Thanh toán"

#### Test Case 6.2: Cập nhật số lượng trong giỏ

**Bước thực hiện:**

1. Thay đổi số lượng sản phẩm
2. Click "Cập nhật"

**Kết quả mong đợi:**

- ✅ Số lượng cập nhật
- ✅ Tổng tiền cập nhật
- ✅ Không cho nhập > stock

#### Test Case 6.3: Xóa sản phẩm khỏi giỏ

**Bước thực hiện:**

1. Click icon "Xóa" ở sản phẩm

**Kết quả mong đợi:**

- ✅ Confirm dialog "Bạn có chắc muốn xóa?"
- ✅ Sản phẩm bị xóa khỏi giỏ
- ✅ Tổng tiền cập nhật
- ✅ Số lượng ở icon giỏ hàng giảm

#### Test Case 6.4: Áp dụng voucher

**Bước thực hiện:**

1. Nhập mã voucher: SALE10
2. Click "Áp dụng"

**Kết quả mong đợi:**

- ✅ Nếu hợp lệ: Hiển thị "Áp dụng thành công"
- ✅ Giảm giá được tính vào tổng tiền
- ✅ Hiển thị số tiền giảm
- ✅ Nếu không hợp lệ: "Mã không hợp lệ hoặc đã hết hạn"

#### Test Case 6.5: Giỏ hàng rỗng

**Bước thực hiện:**

1. Xóa tất cả sản phẩm khỏi giỏ

**Kết quả mong đợi:**

- ✅ Hiển thị "Giỏ hàng trống"
- ✅ Icon giỏ hàng hiển thị 0
- ✅ Button "Tiếp tục mua sắm"

---

### TC-007: Thanh Toán

#### Test Case 7.1: Thanh toán thành công

**Bước thực hiện:**

1. Thêm sản phẩm vào giỏ
2. Truy cập giỏ hàng
3. Click "Thanh toán"
4. Chọn địa chỉ giao hàng (hoặc thêm mới)
5. Chọn phương thức thanh toán: COD
6. Nhập ghi chú (optional)
7. Click "Đặt hàng"

**Kết quả mong đợi:**

- ✅ Chuyển đến trang "Đặt hàng thành công"
- ✅ Hiển thị mã đơn hàng
- ✅ Giỏ hàng được xóa sạch
- ✅ Email xác nhận được gửi
- ✅ Đơn hàng xuất hiện trong "Đơn hàng của tôi"

#### Test Case 7.2: Thanh toán khi chưa đăng nhập

**Bước thực hiện:**

1. Chưa đăng nhập
2. Click "Thanh toán"

**Kết quả mong đợi:**

- ✅ Chuyển đến trang đăng nhập
- ✅ Sau khi đăng nhập → Quay lại checkout
- ✅ Giỏ hàng vẫn giữ nguyên

#### Test Case 7.3: Thêm địa chỉ mới trong checkout

**Bước thực hiện:**

1. Click "Thêm địa chỉ mới"
2. Điền form:
   - Họ tên: Nguyễn Văn A
   - SĐT: 0912345678
   - Địa chỉ: 123 Đường ABC
   - Phường/Xã: Phường 1
   - Quận/Huyện: Quận 1
   - Tỉnh/TP: TP.HCM
3. Check "Đặt làm địa chỉ mặc định"
4. Lưu

**Kết quả mong đợi:**

- ✅ Địa chỉ được lưu
- ✅ Tự động chọn địa chỉ vừa thêm
- ✅ Địa chỉ xuất hiện trong "Địa chỉ của tôi"

#### Test Case 7.4: Áp dụng voucher trong checkout

**Kiểm tra:**

- ✅ Voucher giảm % (10%, 20%)
- ✅ Voucher giảm cố định (50k, 100k)
- ✅ Voucher có điều kiện tối thiểu (VD: đơn từ 500k)
- ✅ Voucher hết hạn → Hiển thị lỗi
- ✅ Voucher đã sử dụng hết số lần → Lỗi

---

### TC-008: Quản Lý Đơn Hàng

#### Test Case 8.1: Xem danh sách đơn hàng

**Bước thực hiện:**

1. Đăng nhập
2. Truy cập "Đơn hàng của tôi"

**Kết quả mong đợi:**

- ✅ Hiển thị tất cả đơn hàng (mới nhất trước)
- ✅ Mỗi đơn có: Mã, ngày, tổng tiền, trạng thái
- ✅ Các trạng thái: Chờ xác nhận, Đang xử lý, Đang giao, Hoàn thành, Đã hủy

#### Test Case 8.2: Xem chi tiết đơn hàng

**Bước thực hiện:**

1. Click vào 1 đơn hàng

**Kết quả mong đợi:**

- ✅ Hiển thị đầy đủ:
  - Mã đơn, ngày đặt
  - Danh sách sản phẩm (hình, tên, size, SL, giá)
  - Địa chỉ giao hàng
  - Phương thức thanh toán
  - Tình trạng đơn hàng
  - Timeline/Tracking (nếu có)

#### Test Case 8.3: Hủy đơn hàng

**Bước thực hiện:**

1. Vào đơn hàng "Chờ xác nhận"
2. Click "Hủy đơn"
3. Nhập lý do hủy
4. Xác nhận

**Kết quả mong đợi:**

- ✅ Trạng thái chuyển sang "Đã hủy"
- ✅ Không thể hủy đơn đã "Đang giao" hoặc "Hoàn thành"
- ✅ Stock sản phẩm được hoàn lại

---

### TC-009: Tài Khoản & Hồ Sơ

#### Test Case 9.1: Xem thông tin cá nhân

**Bước thực hiện:**

1. Đăng nhập
2. Truy cập "Tài khoản"

**Kết quả mong đợi:**

- ✅ Hiển thị: Họ tên, Email, SĐT, Ngày sinh, Giới tính
- ✅ Điểm tích lũy, hạng thành viên
- ✅ Button "Chỉnh sửa"

#### Test Case 9.2: Cập nhật thông tin

**Bước thực hiện:**

1. Click "Chỉnh sửa"
2. Thay đổi tên, SĐT
3. Click "Lưu"

**Kết quả mong đợi:**

- ✅ Thông tin cập nhật thành công
- ✅ Hiển thị thông báo "Cập nhật thành công"

#### Test Case 9.3: Đổi mật khẩu

**Bước thực hiện:**

1. Click "Đổi mật khẩu"
2. Nhập mật khẩu cũ: Test@123456
3. Nhập mật khẩu mới: NewPass@2026
4. Xác nhận mật khẩu mới: NewPass@2026
5. Lưu

**Kết quả mong đợi:**

- ✅ Mật khẩu được cập nhật
- ✅ Đăng xuất và yêu cầu đăng nhập lại
- ✅ Đăng nhập với mật khẩu mới thành công

#### Test Case 9.4: Đổi mật khẩu sai

**Test cases:**

- Mật khẩu cũ sai → Lỗi
- Mật khẩu mới yếu → Lỗi validation
- Xác nhận không khớp → Lỗi

---

### TC-010: Quản Lý Địa Chỉ

#### Test Case 10.1: Thêm địa chỉ mới

**Bước thực hiện:**

1. Truy cập "Địa chỉ của tôi"
2. Click "Thêm địa chỉ"
3. Điền form đầy đủ
4. Lưu

**Kết quả mong đợi:**

- ✅ Địa chỉ được thêm vào danh sách
- ✅ Validation cho các trường bắt buộc

#### Test Case 10.2: Đặt địa chỉ mặc định

**Bước thực hiện:**

1. Click "Đặt làm mặc định" ở địa chỉ

**Kết quả mong đợi:**

- ✅ Địa chỉ được đánh dấu "Mặc định"
- ✅ Địa chỉ cũ bỏ mặc định
- ✅ Checkout tự động chọn địa chỉ này

#### Test Case 10.3: Sửa địa chỉ

**Kết quả mong đợi:**

- ✅ Form hiển thị data cũ
- ✅ Cập nhật thành công

#### Test Case 10.4: Xóa địa chỉ

**Kết quả mong đợi:**

- ✅ Confirm trước khi xóa
- ✅ Không cho xóa địa chỉ đang dùng trong đơn hàng đang xử lý

---

### TC-011: Voucher & Khuyến Mãi

#### Test Case 11.1: Xem danh sách voucher

**Bước thực hiện:**

1. Truy cập "Voucher của tôi"

**Kết quả mong đợi:**

- ✅ Hiển thị voucher có thể dùng
- ✅ Hiển thị voucher đã dùng (grayed out)
- ✅ Hiển thị voucher hết hạn
- ✅ Mỗi voucher có: Code, % giảm hoặc số tiền, điều kiện, hạn sử dụng

#### Test Case 11.2: Lưu voucher

**Bước thực hiện:**

1. Truy cập trang khuyến mãi
2. Click "Lưu" voucher

**Kết quả mong đợi:**

- ✅ Voucher được thêm vào "Voucher của tôi"
- ✅ Có thể dùng trong checkout

---

### TC-012: Membership

#### Test Case 12.1: Xem thông tin hội viên

**Bước thực hiện:**

1. Truy cập "Thành viên"

**Kết quả mong đợi:**

- ✅ Hiển thị hạng hiện tại (Bronze/Silver/Gold/Platinum)
- ✅ Điểm tích lũy
- ✅ Điểm cần để lên hạng
- ✅ Lợi ích của từng hạng
- ✅ Lịch sử tích điểm

#### Test Case 12.2: Tích điểm từ đơn hàng

**Bước thực hiện:**

1. Đặt đơn hàng 500k
2. Đơn hoàn thành

**Kết quả mong đợi:**

- ✅ Tích điểm = 500 (1k = 1 điểm)
- ✅ Điểm cộng vào tài khoản
- ✅ Nếu đủ điểm → Tự động lên hạng

---

### TC-013: Live Chat

#### Test Case 13.1: Gửi tin nhắn

**Bước thực hiện:**

1. Click icon Chat
2. Nhập tin nhắn: "Tôi muốn hỏi về sản phẩm X"
3. Gửi

**Kết quả mong đợi:**

- ✅ Tin nhắn hiển thị trong chat box
- ✅ Timestamp chính xác
- ✅ Admin nhận được tin nhắn (kiểm tra ở admin panel)

#### Test Case 13.2: Nhận tin nhắn từ admin

**Kết quả mong đợi:**

- ✅ Tin nhắn hiển thị realtime
- ✅ Notification icon có badge

---

## 🔐 PHẦN 2: KIỂM TRA CHỨC NĂNG ADMIN

### TC-014: Đăng Nhập Admin

#### Test Case 14.1: Đăng nhập admin

**Bước thực hiện:**

1. Truy cập /admin/login
2. Nhập: admin@clothesshop.com / Admin@123456
3. Đăng nhập

**Kết quả mong đợi:**

- ✅ Chuyển đến Dashboard admin
- ✅ Menu admin hiển thị đầy đủ

#### Test Case 14.2: User thường không truy cập admin

**Bước thực hiện:**

1. Đăng nhập với tài khoản customer
2. Truy cập /admin/dashboard

**Kết quả mong đợi:**

- ✅ Redirect về trang chủ hoặc 403 Forbidden

---

### TC-015: Dashboard

#### Test Case 15.1: Xem dashboard

**Kết quả mong đợi:**

- ✅ Hiển thị thống kê:
  - Tổng doanh thu (hôm nay, tháng, năm)
  - Số đơn hàng (mới, đang xử lý, hoàn thành)
  - Số sản phẩm
  - Số khách hàng
- ✅ Biểu đồ doanh thu theo thời gian
- ✅ Top sản phẩm bán chạy
- ✅ Đơn hàng mới cần xử lý

---

### TC-016: Quản Lý Sản Phẩm

#### Test Case 16.1: Xem danh sách sản phẩm

**Kết quả mong đợi:**

- ✅ Hiển thị table: ID, Hình, Tên, Danh mục, Giá, Stock, Trạng thái
- ✅ Phân trang
- ✅ Search
- ✅ Filter theo category

#### Test Case 16.2: Thêm sản phẩm mới

**Bước thực hiện:**

1. Click "Thêm sản phẩm"
2. Điền form:
   - Tên: Áo Thun Basic Trắng
   - Danh mục: NAM
   - Subcategory: Áo thun (dropdown tự động populate)
   - Thương hiệu: CANIFA
   - Giá: 199000
   - Giá gốc: 299000
   - Stock: 100
   - Size: S,M,L,XL
   - Màu: Trắng
   - Chất liệu: Cotton 100%
   - Mô tả: Chi tiết...
3. Upload hình chính
4. Upload 3 hình phụ
5. Check "Sản phẩm mới"
6. Lưu

**Kết quả mong đợi:**

- ✅ Sản phẩm được tạo thành công
- ✅ Hiển thị trong danh sách
- ✅ Hình ảnh được upload
- ✅ Hiển thị đúng ở trang khách

#### Test Case 16.3: Upload file không hợp lệ

**Test cases:**

- File > 5MB → Lỗi "File quá lớn"
- File .exe, .php → Lỗi "File không hợp lệ"
- Upload > 3 hình phụ → Lỗi hoặc chỉ lấy 3 đầu

#### Test Case 16.4: Validation form sản phẩm

**Test:**

- Bỏ trống tên → Lỗi
- Giá âm → Lỗi
- Stock âm → Lỗi
- Không chọn category → Lỗi

#### Test Case 16.5: Sửa sản phẩm

**Bước thực hiện:**

1. Click "Sửa" ở sản phẩm
2. Form hiển thị data cũ
3. Thay đổi giá: 299000 → 199000
4. Lưu

**Kết quả mong đợi:**

- ✅ Cập nhật thành công
- ✅ Giá mới hiển thị ở trang khách

#### Test Case 16.6: Xóa sản phẩm

**Kết quả mong đợi:**

- ✅ Confirm trước khi xóa
- ✅ Sản phẩm bị xóa khỏi database
- ✅ Không hiển thị ở trang khách
- ✅ Không thể xóa sản phẩm trong đơn hàng đang xử lý (optional)

---

### TC-017: Quản Lý Danh Mục

#### Test Case 17.1: Thêm danh mục mới

**Bước thực hiện:**

1. Truy cập "Quản lý danh mục"
2. Click "Thêm danh mục"
3. Điền:
   - Tên: NAM
   - Mô tả: Thời trang nam
   - Subcategories (textarea): Áo thun,Áo polo,Áo sơ mi,Áo khoác,Quần jean,Quần kaki,Quần short
   - Thứ tự: 1
   - Trạng thái: Active
4. Lưu

**Kết quả mong đợi:**

- ✅ Danh mục được tạo
- ✅ Hiển thị trong bảng với subcategories dạng badges
- ✅ Khi tạo sản phẩm → Chọn category NAM → Dropdown subcategory tự động hiển thị 7 options

#### Test Case 17.2: Subcategory dropdown trong product form

**Bước thực hiện:**

1. Tạo/Sửa sản phẩm
2. Chọn category: NAM
3. Kiểm tra dropdown Subcategory

**Kết quả mong đợi:**

- ✅ Dropdown tự động populate với: Áo thun, Áo polo, Áo sơ mi, Áo khoác, Quần jean, Quần kaki, Quần short
- ✅ Chọn category khác → Dropdown update
- ✅ Trong edit mode → Giữ nguyên subcategory đã chọn

#### Test Case 17.3: Sửa subcategories của category

**Bước thực hiện:**

1. Sửa category NAM
2. Thêm subcategory mới: "Áo hoodie"
3. Lưu

**Kết quả mong đợi:**

- ✅ Subcategories update
- ✅ Product form dropdown có thêm "Áo hoodie"

---

### TC-018: Quản Lý Đơn Hàng (Admin)

#### Test Case 18.1: Xem danh sách đơn hàng

**Kết quả mong đợi:**

- ✅ Hiển thị tất cả đơn
- ✅ Filter theo trạng thái
- ✅ Search theo mã đơn, tên khách
- ✅ Sắp xếp theo ngày

#### Test Case 18.2: Xem chi tiết & cập nhật trạng thái

**Bước thực hiện:**

1. Click vào đơn hàng
2. Thay đổi trạng thái: "Chờ xác nhận" → "Đang xử lý"
3. Lưu

**Kết quả mong đợi:**

- ✅ Trạng thái cập nhật
- ✅ Khách hàng nhận thông báo (email/notification)
- ✅ Timeline cập nhật

#### Test Case 18.3: Hủy đơn hàng (Admin)

**Kết quả mong đợi:**

- ✅ Confirm trước khi hủy
- ✅ Yêu cầu nhập lý do
- ✅ Stock được hoàn lại
- ✅ Khách nhận thông báo

---

### TC-019: Quản Lý Người Dùng

#### Test Case 19.1: Xem danh sách user

**Kết quả mong đợi:**

- ✅ Hiển thị: ID, Email, Tên, SĐT, Vai trò, Trạng thái
- ✅ Search
- ✅ Filter theo vai trò (Admin/Customer)

#### Test Case 19.2: Thêm user mới

**Bước thực hiện:**

1. Click "Thêm user"
2. Điền form
3. Chọn vai trò: CUSTOMER hoặc ADMIN
4. Lưu

**Kết quả mong đợi:**

- ✅ User được tạo
- ✅ Password hash an toàn (BCrypt)

#### Test Case 19.3: Khóa/Mở khóa user

**Kết quả mong đợi:**

- ✅ User bị khóa không đăng nhập được
- ✅ Hiển thị thông báo "Tài khoản bị khóa"

---

### TC-020: Quản Lý Voucher

#### Test Case 20.1: Tạo voucher mới

**Bước thực hiện:**

1. Click "Thêm voucher"
2. Điền:
   - Mã: SALE10
   - Loại: Giảm % (10%)
   - Điều kiện tối thiểu: 200000
   - Số lượng: 100
   - Ngày bắt đầu: 01/02/2026
   - Ngày kết thúc: 28/02/2026
3. Lưu

**Kết quả mong đợi:**

- ✅ Voucher được tạo
- ✅ Khách có thể dùng từ ngày 01/02
- ✅ Hết hạn sau 28/02

#### Test Case 20.2: Validation voucher

**Test:**

- Mã trùng → Lỗi
- Ngày kết thúc < Ngày bắt đầu → Lỗi
- % giảm > 100 → Lỗi
- Số lượng < 0 → Lỗi

---

## 🔒 PHẦN 3: KIỂM TRA BẢO MẬT (SECURITY)

### TC-021: Authentication & Authorization

#### Test Case 21.1: SQL Injection

**Test input:**

```
Email: admin' OR '1'='1
Password: anything
```

**Kết quả mong đợi:**

- ✅ Không đăng nhập được
- ✅ Không bị SQL Injection

#### Test Case 21.2: XSS Attack

**Test input trong tên sản phẩm:**

```html
<script>
  alert("XSS");
</script>
```

**Kết quả mong đợi:**

- ✅ Script không thực thi
- ✅ Hiển thị dạng text thuần

#### Test Case 21.3: CSRF Protection

**Kết quả mong đợi:**

- ✅ Tất cả form có CSRF token
- ✅ Request không có token bị reject

#### Test Case 21.4: Session Timeout

**Bước thực hiện:**

1. Đăng nhập
2. Idle 30 phút
3. Thao tác

**Kết quả mong đợi:**

- ✅ Session hết hạn
- ✅ Redirect về login

#### Test Case 21.5: Password Security

**Kiểm tra:**

- ✅ Password được hash (BCrypt)
- ✅ Không lưu plain text
- ✅ Validate độ mạnh password

---

### TC-022: File Upload Security

#### Test Case 22.1: Upload file độc hại

**Test uploads:**

- shell.php
- virus.exe
- malware.bat

**Kết quả mong đợi:**

- ✅ Chỉ cho phép image (jpg, png, gif, webp)
- ✅ Reject tất cả file không phải hình
- ✅ Kiểm tra MIME type, không chỉ extension

#### Test Case 22.2: Path Traversal

**Test filename:**

```
../../etc/passwd.jpg
```

**Kết quả mong đợi:**

- ✅ Filename được sanitize
- ✅ Không cho upload ra ngoài thư mục được phép

---

## 📊 PHẦN 4: KIỂM TRA HIỆU NĂNG (PERFORMANCE)

### TC-023: Load Testing

#### Test Case 23.1: Trang chủ load time

**Kết quả mong đợi:**

- ✅ Load < 3 giây
- ✅ Hình ảnh được optimize
- ✅ Lazy loading cho hình

#### Test Case 23.2: Search performance

**Bước thực hiện:**

1. Database có 10,000+ sản phẩm
2. Search từ khóa

**Kết quả mong đợi:**

- ✅ Kết quả trả về < 1 giây
- ✅ Pagination hoạt động tốt

#### Test Case 23.3: Concurrent users

**Test:**

- 100 users đồng thời
- 1000 users đồng thời

**Kết quả mong đợi:**

- ✅ Server không crash
- ✅ Response time acceptable

---

## 📱 PHẦN 5: KIỂM TRA RESPONSIVE & COMPATIBILITY

### TC-024: Responsive Design

#### Test Case 24.1: Mobile (375px - 767px)

**Kiểm tra:**

- ✅ Menu hamburger hoạt động
- ✅ Tất cả chức năng hoạt động trên mobile
- ✅ Không có horizontal scroll
- ✅ Button đủ lớn để tap
- ✅ Form dễ nhập liệu

#### Test Case 24.2: Tablet (768px - 1024px)

**Kiểm tra:**

- ✅ Layout phù hợp
- ✅ Image scale đúng

#### Test Case 24.3: Desktop (> 1024px)

**Kiểm tra:**

- ✅ Full features
- ✅ Hover effects

---

### TC-025: Browser Compatibility

**Test trên:**

- ✅ Chrome (latest)
- ✅ Firefox (latest)
- ✅ Edge (latest)
- ✅ Safari (macOS/iOS)

**Kiểm tra:**

- Hiển thị đúng
- Chức năng hoạt động
- JavaScript không lỗi

---

## 🐛 MẪU BÁO CÁO LỖI (BUG REPORT)

### Bug ID: BUG-001

**Tiêu đề:** [Tên lỗi ngắn gọn]

**Mức độ:**

- 🔴 Critical (Nghiêm trọng - Ảnh hưởng chính)
- 🟠 Major (Quan trọng - Ảnh hưởng chức năng)
- 🟡 Minor (Nhỏ - Không ảnh hưởng nhiều)
- 🟢 Trivial (Rất nhỏ - UI/UX)

**Mô tả:**
[Mô tả chi tiết lỗi]

**Bước tái hiện:**

1. Bước 1
2. Bước 2
3. Bước 3

**Kết quả thực tế:**
[Điều gì xảy ra]

**Kết quả mong đợi:**
[Điều gì nên xảy ra]

**Screenshot/Video:**
[Đính kèm nếu có]

**Môi trường:**

- Browser: Chrome 120.0
- OS: Windows 11
- Device: Desktop
- Screen: 1920x1080

**Thông tin thêm:**
[Log lỗi, console error, etc.]

---

## ✅ CHECKLIST TỔNG QUAN

### Chức Năng Khách Hàng

- [ ] Đăng ký/Đăng nhập
- [ ] Trang chủ
- [ ] Tìm kiếm & Filter
- [ ] Chi tiết sản phẩm
- [ ] Giỏ hàng
- [ ] Thanh toán
- [ ] Quản lý đơn hàng
- [ ] Tài khoản & Profile
- [ ] Địa chỉ
- [ ] Voucher
- [ ] Membership
- [ ] Live Chat
- [ ] Đánh giá sản phẩm

### Chức Năng Admin

- [ ] Dashboard
- [ ] Quản lý sản phẩm (CRUD)
- [ ] Quản lý danh mục (CRUD + Subcategories)
- [ ] Quản lý đơn hàng
- [ ] Quản lý user
- [ ] Quản lý voucher
- [ ] Thống kê & báo cáo

### Bảo Mật

- [ ] SQL Injection prevention
- [ ] XSS protection
- [ ] CSRF protection
- [ ] File upload security
- [ ] Password hashing (BCrypt)
- [ ] Session management
- [ ] Authorization check

### Performance

- [ ] Page load time
- [ ] Search performance
- [ ] Concurrent users
- [ ] Database optimization

### Responsive & Compatibility

- [ ] Mobile responsive
- [ ] Tablet responsive
- [ ] Desktop layout
- [ ] Cross-browser (Chrome, Firefox, Edge, Safari)

---

## 📝 GHI CHÚ QUAN TRỌNG

1. **Trước khi bắt đầu test:**
   - Backup database
   - Chuẩn bị test data đầy đủ
   - Setup môi trường test riêng

2. **Trong quá trình test:**
   - Ghi chép chi tiết mọi lỗi
   - Screenshot/Video lỗi nghiêm trọng
   - Test cả happy path và edge cases

3. **Ưu tiên test:**
   - 🔴 Critical bugs trước (thanh toán, đăng nhập, mất dữ liệu)
   - 🟠 Major bugs (chức năng chính)
   - 🟡 Minor bugs (UI/UX)

4. **Regression testing:**
   - Test lại tất cả sau mỗi lần fix bug
   - Đảm bảo fix bug không tạo bug mới

---

## 📧 LIÊN HỆ

**Nếu có câu hỏi hoặc cần hỗ trợ:**

- Developer: [Email/Slack]
- Project Manager: [Email/Slack]

**Timeline:**

- Bắt đầu test: [Ngày]
- Deadline: [Ngày]
- Meeting review bugs: [Ngày]

---

**Chúc các bạn test hiệu quả! 🚀**
