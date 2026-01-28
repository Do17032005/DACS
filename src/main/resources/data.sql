-- Sample Data for Categories
INSERT INTO categories (id, name, description, display_order, is_active, created_at) VALUES
(1, 'NAM', 'Quần áo thời trang nam cao cấp', 1, true, CURRENT_TIMESTAMP),
(2, 'NỮ', 'Quần áo thời trang nữ đa dạng phong cách', 2, true, CURRENT_TIMESTAMP),
(3, 'TRẺ EM', 'Quần áo trẻ em thoải mái, an toàn', 3, true, CURRENT_TIMESTAMP),
(4, 'BÉ TRAI', 'Trang phục bé trai năng động', 4, true, CURRENT_TIMESTAMP),
(5, 'BÉ GÁI', 'Trang phục bé gái dễ thương', 5, true, CURRENT_TIMESTAMP),
(6, 'PHỤ KIỆN', 'Phụ kiện thời trang đa dạng', 6, true, CURRENT_TIMESTAMP),
(7, 'SCHOOL', 'Đồng phục học sinh chất lượng', 7, true, CURRENT_TIMESTAMP);

-- Sample Data for Products
INSERT INTO products (id, name, description, price, original_price, discount_percent, category, brand, material, color, image_url, stock, sold, is_new, is_bestseller, created_at) VALUES
(1, 'Áo Thun Nam Cotton', 'Áo thun nam cotton cao cấp, thoáng mát, thiết kế basic dễ phối đồ', 299000, 399000, 25, 'NAM', 'CANIFA', 'Cotton 100%', 'Trắng', 'https://media.canifa.com/catalog/product/cache/400x/8TS24W001-SW000-L.webp', 100, 50, true, true, CURRENT_TIMESTAMP),
(2, 'Quần Jean Nam Slim Fit', 'Quần jean nam form slim fit trẻ trung, phong cách hiện đại', 599000, 799000, 25, 'NAM', 'CANIFA', 'Denim', 'Xanh đen', 'https://media.canifa.com/catalog/product/cache/400x/8QJ24W001-SB055-L.webp', 80, 30, false, true, CURRENT_TIMESTAMP),
(3, 'Váy Nữ Hoa Nhí', 'Váy nữ họa tiết hoa nhí dễ thương, phù hợp dạo phố', 449000, 599000, 25, 'NỮ', 'CANIFA', 'Polyester', 'Hồng', 'https://media.canifa.com/catalog/product/cache/400x/6TS24W003-SJ146-L.webp', 60, 20, true, false, CURRENT_TIMESTAMP),
(4, 'Áo Khoác Nữ Denim', 'Áo khoác nữ denim phong cách Hàn Quốc, năng động', 799000, 999000, 20, 'NỮ', 'CANIFA', 'Denim', 'Xanh nhạt', 'https://media.canifa.com/catalog/product/cache/400x/6AT24W011-SK010-L.webp', 50, 15, false, true, CURRENT_TIMESTAMP),
(5, 'Đồ Bộ Trẻ Em', 'Đồ bộ trẻ em họa tiết hoạt hình đáng yêu, an toàn cho da bé', 299000, 399000, 25, 'TRẺ EM', 'CANIFA', 'Cotton', 'Nhiều màu', 'https://media.canifa.com/catalog/product/cache/400x/1TS24W001-SJ146-L.webp', 120, 40, true, true, CURRENT_TIMESTAMP),
(6, 'Áo Polo Nam Basic', 'Áo polo nam basic lịch lãm, phù hợp cho cả đi làm và đi chơi', 350000, 450000, 22, 'NAM', 'CANIFA', 'Pique', 'Xanh navy', 'https://media.canifa.com/catalog/product/cache/400x/8TP24W001-SB055-L.webp', 90, 25, false, true, CURRENT_TIMESTAMP),
(7, 'Quần Kaki Nam Slim', 'Quần kaki nam dáng slim fit gọn gàng, tôn dáng', 450000, 550000, 18, 'NAM', 'CANIFA', 'Kaki', 'Be', 'https://media.canifa.com/catalog/product/cache/400x/8QK24W001-SK055-L.webp', 70, 10, false, false, CURRENT_TIMESTAMP),
(8, 'Áo Len Nữ Cổ Tròn', 'Áo len nữ cổ tròn ấm áp, chất liệu len mềm mịn', 359000, 499000, 28, 'NỮ', 'CANIFA', 'Len', 'Hồng pastel', 'https://media.canifa.com/catalog/product/cache/400x/6AL24W001-SK055-L.webp', 65, 18, true, false, CURRENT_TIMESTAMP),
(9, 'Áo Sơ Mi Nữ Tay Dài', 'Áo sơ mi nữ tay dài thanh lịch, phong cách công sở', 449000, 549000, 18, 'NỮ', 'CANIFA', 'Cotton', 'Trắng', 'https://media.canifa.com/catalog/product/cache/400x/6AS24W001-SW807-L.webp', 55, 12, false, false, CURRENT_TIMESTAMP),
(10, 'Áo Thun Bé Trai', 'Áo thun bé trai in hình hoạt hình sinh động', 180000, 250000, 28, 'TRẺ EM', 'CANIFA', 'Cotton', 'Xanh dương', 'https://media.canifa.com/catalog/product/cache/400x/2TS24W001-SJ146-L.webp', 150, 60, true, true, CURRENT_TIMESTAMP);

-- Add available sizes for products
INSERT INTO product_sizes (product_id, size) VALUES
(1, 'S'), (1, 'M'), (1, 'L'), (1, 'XL'),
(2, 'M'), (2, 'L'), (2, 'XL'), (2, 'XXL'),
(3, 'S'), (3, 'M'), (3, 'L'),
(4, 'M'), (4, 'L'), (4, 'XL'),
(5, '2-3'), (5, '4-5'), (5, '6-7'),
(6, 'M'), (6, 'L'), (6, 'XL'),
(7, 'M'), (7, 'L'), (7, 'XL'),
(8, 'S'), (8, 'M'), (8, 'L'),
(9, 'S'), (9, 'M'), (9, 'L'),
(10, '4-5'), (10, '6-7'), (10, '8-9');

-- Sample Data for Vouchers
INSERT INTO vouchers (id, code, title, description, discount_percent, min_order_amount, max_discount, start_date, end_date, usage_limit, used_count, active) VALUES
(1, 'SALE30', 'Giảm 30%', 'Giảm 30% cho đơn hàng từ 899K', 30, 899000, 300000, CURRENT_TIMESTAMP, DATEADD('DAY', 30, CURRENT_TIMESTAMP), 100, 0, true),
(2, 'SALE50', 'Giảm 50%', 'Giảm 50% cho đơn hàng từ 1.499K', 50, 1499000, 500000, CURRENT_TIMESTAMP, DATEADD('DAY', 30, CURRENT_TIMESTAMP), 50, 0, true),
(3, 'FREESHIP', 'Freeship', 'Miễn phí vận chuyển cho đơn từ 499K', 0, 499000, 0, CURRENT_TIMESTAMP, DATEADD('DAY', 30, CURRENT_TIMESTAMP), 200, 0, true);
