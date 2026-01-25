-- SAMPLE DATA FOR PRODUCTS
INSERT IGNORE INTO products (id, name, price, description, image_url, category, stock) VALUES 
(1, 'Áo Thun Nữ Phối Màu', 299000, 'Áo thun nữ phối màu trendy, thiết kế trẻ trung năng động. Chất liệu cotton 100% mềm mại, thấm hút mồ hôi tốt, thoải mái khi vận động.', 'https://media.canifa.com/catalog/product/cache/400x/6TS24W003-SJ146-L.webp', 'Nữ', 100),
(2, 'Áo Khoác Nữ Dáng Ngắn', 599000, 'Áo khoác nữ dáng ngắn sành điệu, chất liệu bền đẹp, giữ ấm tốt và dễ dàng phối đồ cho những ngày se lạnh.', 'https://media.canifa.com/catalog/product/cache/400x/6AT24W011-SK010-L.webp', 'Nữ', 50),
(3, 'Áo Len Nữ Cổ Tròn', 359000, 'Áo len nữ cổ tròn ấm áp, chất liệu len mềm mịn, không xù lông, mang lại cảm giác thoải mái suốt cả ngày.', 'https://media.canifa.com/catalog/product/cache/400x/6AL24W001-SK055-L.webp', 'Nữ', 75),
(4, 'Quần Jean Nữ Ống Rộng', 499000, 'Quần jean nữ ống rộng phong cách, tôn dáng cực chuẩn. Chất denim dày dặn, co giãn nhẹ, mang lại sự tự tin đầy cá tính.', 'https://media.canifa.com/catalog/product/cache/400x/6QJ24W001-SB055-L.webp', 'Nữ', 60),
(5, 'Áo Sơ Mi Nữ Tay Dài', 449000, 'Áo sơ mi nữ tay dài thanh lịch, phong cách công sở hiện đại. Chất liệu vải cao cấp, ít nhăn, dễ giặt ủi.', 'https://media.canifa.com/catalog/product/cache/400x/6AS24W001-SW807-L.webp', 'Nữ', 85),
(6, 'Áo Polo Nam Basic', 350000, 'Áo polo nam basic lịch lãm, phù hợp cho cả đi làm và đi chơi. Chất liệu cá sấu thoáng mát, bền màu.', 'https://media.canifa.com/catalog/product/cache/400x/8TP24W001-SB055-L.webp', 'Nam', 120),
(7, 'Quần Kaki Nam Slim Fit', 450000, 'Quần kaki nam dáng slim fit gọn gàng, tôn dáng. Chất vải kaki co giãn, thoải mái khi di chuyển.', 'https://media.canifa.com/catalog/product/cache/400x/8QK24W001-SK055-L.webp', 'Nam', 90),
(8, 'Áo Khoác Gió Nam', 650000, 'Áo khoác gió nam chống nước, cực nhẹ và tiện lợi. Phù hợp cho các hoạt động ngoài trời.', 'https://media.canifa.com/catalog/product/cache/400x/8AG24W001-SK055-L.webp', 'Nam', 40),
(9, 'Bộ Đồ Bé Gái Disney', 250000, 'Bộ đồ bé gái họa tiết Disney dễ thương, chất cotton mềm mại, an toàn cho làn da nhạy cảm của bé.', 'https://media.canifa.com/catalog/product/cache/400x/1TS24W001-SJ146-L.webp', 'Bé Gái', 150),
(10, 'Áo Thun Bé Trai Doraemon', 180000, 'Áo thun bé trai in hình Doraemon sinh động. Bé sẽ rất thích thú khi mặc chiếc áo này đi chơi.', 'https://media.canifa.com/catalog/product/cache/400x/2TS24W001-SJ146-L.webp', 'Bé Trai', 200);

-- SAMPLE DATA FOR USERS
-- Mật khẩu mặc định: admin123 (nếu backend dùng encoder bcript thì đoạn này có thể cần cập nhật hash)
-- Dựa vào PasswordGenerator cũ, admin123 có thể là plaintext hoặc hash.
INSERT IGNORE INTO users (id, username, password, role, full_name, email) VALUES 
(1, 'admin', 'admin123', 'ADMIN', 'Nguyễn Văn Admin', 'admin@canifa.shop'),
(2, 'user1', 'user123', 'USER', 'Trần Thị Khách Hàng', 'user1@gmail.com');

-- SAMPLE DATA FOR ORDERS
INSERT IGNORE INTO orders (id, user_id, total_price, order_date, status) VALUES 
(1, 1, 898000, '2026-01-16 10:00:00', 'DELIVERED'),
(2, 1, 599000, '2026-01-15 14:30:00', 'SHIPPING');

-- SAMPLE DATA FOR ORDER ITEMS
INSERT IGNORE INTO order_items (order_id, product_id, quantity, price) VALUES 
(1, 1, 1, 299000),
(1, 2, 1, 599000),
(2, 2, 1, 599000);

-- SAMPLE DATA FOR CHAT HISTORY
INSERT IGNORE INTO chat_history (user_id, message, response, timestamp) VALUES 
(1, 'Chào shop, tôi muốn hỏi về áo thun phối màu.', 'Chào bạn! Áo thun nữ phối màu hiện đang có sẵn màu Xanh Navy cực hot. Bạn cần tư vấn thêm về size không ạ?', '2026-01-25 10:00:00'),
(1, 'Áo này có co giãn không?', 'Có ạ, áo làm từ 100% cotton co giãn nhẹ, rất thoải mái khi mặc.', '2026-01-25 10:05:00');
