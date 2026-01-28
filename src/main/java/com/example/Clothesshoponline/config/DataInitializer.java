package com.example.Clothesshoponline.config;

import com.example.Clothesshoponline.model.Product;
import com.example.Clothesshoponline.model.Review;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.model.Voucher;
import com.example.Clothesshoponline.repository.ProductRepository;
import com.example.Clothesshoponline.repository.ReviewRepository;
import com.example.Clothesshoponline.repository.UserRepository;
import com.example.Clothesshoponline.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private VoucherRepository voucherRepository;

        @Autowired
        private ReviewRepository reviewRepository;

        @Override
        public void run(String... args) throws Exception {
                // NOTE: Products, Categories, and Vouchers are now initialized via data.sql
                // This ensures consistency and prevents duplicate data issues

                // Tạo người dùng quản trị (Admin)
                if (!userRepository.existsByEmail("admin@clothesshop.com")) {
                        User admin = new User();
                        admin.setEmail("admin@clothesshop.com");
                        admin.setUsername("admin");
                        admin.setPassword("admin123");
                        admin.setFullName("Quản trị viên hệ thống");
                        admin.setPhone("0901234567");
                        admin.setRole(User.Role.ADMIN);
                        admin.setEnabled(true);
                        admin.setMemberLevel("VIP");
                        admin.setMemberPoints(1000);
                        userRepository.save(admin);
                        System.out.println("✅ Khởi tạo tài khoản Admin thành công.");
                }

                // Tạo người dùng thử nghiệm
                if (!userRepository.existsByEmail("user@test.com")) {
                        User user = new User();
                        user.setEmail("user@test.com");
                        user.setUsername("khachhang");
                        user.setPassword("123456");
                        user.setFullName("Nguyễn Văn A");
                        user.setPhone("0987654321");
                        user.setRole(User.Role.USER);
                        user.setEnabled(true);
                        user.setMemberLevel("Thành viên mới");
                        user.setMemberPoints(0);
                        userRepository.save(user);
                        System.out.println("✅ Khởi tạo người dùng thử nghiệm thành công.");
                }

                // Display data summary
                System.out.println("📊 Database Summary:");
                System.out.println("   - Products: " + productRepository.count());
                System.out.println("   - Vouchers: " + voucherRepository.count());
                System.out.println("   - Users: " + userRepository.count());
                System.out.println("   - Reviews: " + reviewRepository.count());

                // Tạo sample reviews nếu chưa có
                if (reviewRepository.count() == 0 && productRepository.count() > 0) {
                        createSampleReviews();
                        System.out.println("✅ Sample reviews created");
                }
        }

        private void createOrUpdateSampleProducts() {
                // ==================== DANH MỤC NAM ====================
                saveOrUpdateProduct("Áo Thun Nam Cotton Compact", "Cotton Compact chống nhăn, thoáng mát",
                                new BigDecimal("299000"), new BigDecimal("399000"), 25, "NAM", "COOLMATE",
                                "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=800&q=80",
                                true,
                                true);

                saveOrUpdateProduct("Áo Polo Nam Quick Dry", "Áo polo thể thao nam Quick Dry Active",
                                new BigDecimal("350000"), new BigDecimal("450000"), 22, "NAM", "COOLMATE",
                                "https://images.unsplash.com/photo-1625910513413-5fc7144e40ef?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                saveOrUpdateProduct("Áo Khoác Bomber Thể Thao", "Áo khoác bomber nam phong cách sporty",
                                new BigDecimal("899000"), new BigDecimal("1199000"), 25, "NAM", "COOLMATE",
                                "https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=800&q=80",
                                true,
                                false);

                saveOrUpdateProduct("Quần Jean Nam Slim Fit", "Quần jean nam dáng slim fit trẻ trung",
                                new BigDecimal("599000"), new BigDecimal("799000"), 25, "NAM", "COOLMATE",
                                "https://images.unsplash.com/photo-1542272604-787c3835535d?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                // ==================== DANH MỤC NỮ (Unsplash) ====================
                saveOrUpdateProduct("Váy Nữ Hoa Nhí Spring", "Họa tiết hoa nhí phong cách Hàn Quốc",
                                new BigDecimal("449000"), new BigDecimal("599000"), 25, "NỮ", "GUMAC",
                                "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?auto=format&fit=crop&w=800&q=80",
                                true,
                                false);

                saveOrUpdateProduct("Áo Blazer Công Sở Nữ", "Áo blazer nữ thiết kế thanh lịch",
                                new BigDecimal("899000"), new BigDecimal("1199000"), 25, "NỮ", "BAI04",
                                "https://images.unsplash.com/photo-1591369822096-ffd140ec948f?auto=format&fit=crop&w=800&q=80",
                                false,
                                false);

                saveOrUpdateProduct("Đầm Dạ Hội Cut-out", "Đầm dạ hội thiết kế cut-out tinh tế",
                                new BigDecimal("1299000"), new BigDecimal("1699000"), 24, "NỮ", "ELISE",
                                "https://images.unsplash.com/photo-1566174053879-31528523f8ae?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                saveOrUpdateProduct("Áo Thun Nữ Basic", "Áo thun nữ basic cotton 100% mềm mại",
                                new BigDecimal("199000"), new BigDecimal("299000"), 33, "NỮ", "GUMAC",
                                "https://images.unsplash.com/photo-1521577352947-9bb58764b69a?auto=format&fit=crop&w=800&q=80",
                                true,
                                true);

                saveOrUpdateProduct("Quần Jean Nữ Ống Rộng", "Quần jean nữ ống rộng phong cách Y2K",
                                new BigDecimal("599000"), new BigDecimal("799000"), 25, "NỮ", "ELISE",
                                "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                // ==================== DANH MỤC SCHOOL - BÉ TRAI ====================
                saveOrUpdateProduct("Áo Sơ Mi Học Sinh Nam Túi Bên DPT001",
                                "Chất liệu cotton Nhật cao cấp, ít nhăn, thoáng mát",
                                new BigDecimal("97000"), new BigDecimal("120000"), 19, "SCHOOL", "JADINY",
                                "https://images.unsplash.com/photo-1598033129183-c4f50c736f10?auto=format&fit=crop&w=800&q=80",
                                true,
                                true);

                saveOrUpdateProduct("Quần Tây Học Sinh Nam Xanh Đen TDP011", "Quần tây phom chuẩn quai rẽ lịch sự",
                                new BigDecimal("173000"), new BigDecimal("210000"), 18, "SCHOOL", "JADINY",
                                "https://images.unsplash.com/photo-1473966968600-fa801b869a1a?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                saveOrUpdateProduct("Quần Short Học Sinh Nam TDP002", "Lưng thun thoải mái cho bé vận động",
                                new BigDecimal("89000"), new BigDecimal("110000"), 19, "SCHOOL", "JADINY",
                                "https://images.unsplash.com/photo-1506629082955-511b1aa562c8?auto=format&fit=crop&w=800&q=80",
                                false,
                                false);

                // ==================== DANH MỤC SCHOOL - BÉ GÁI ====================
                saveOrUpdateProduct("Bộ Áo Dài Học Sinh Trắng Tinh GDP061",
                                "Thiết kế chuẩn truyền thống, tôn dáng duyên dáng",
                                new BigDecimal("339000"), new BigDecimal("450000"), 25, "SCHOOL", "JADINY",
                                "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?auto=format&fit=crop&w=800&q=80",
                                true,
                                true);

                saveOrUpdateProduct("Áo Sơ Mi Nữ Học Sinh Bèo Ngực GDP001", "Điểm nhấn bèo nhún dọc cổ áo nữ tính",
                                new BigDecimal("149000"), new BigDecimal("190000"), 22, "SCHOOL", "JADINY",
                                "https://images.unsplash.com/photo-1604695573706-53170668f6a6?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                saveOrUpdateProduct("Chân Váy Học Sinh Nữ Xếp Ly GDP065", "Vải tuyết mưa không nhăn, xếp ly chuẩn đẹp",
                                new BigDecimal("149000"), new BigDecimal("195000"), 24, "SCHOOL", "JADINY",
                                "https://images.unsplash.com/photo-1582142306909-195724d33ffc?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                // ==================== UNISEX / PE ====================
                saveOrUpdateProduct("Quần Thể Dục Học Sinh 3 Sọc TDP025",
                                "Vải thun co giãn tốt, thoải mái cho tiết học PE",
                                new BigDecimal("120000"), new BigDecimal("160000"), 25, "SCHOOL", "JADINY",
                                "https://images.unsplash.com/photo-1552374196-1ab2a1c593e8?auto=format&fit=crop&w=800&q=80",
                                false,
                                false);

                // ==================== DANH MỤC BÉ GÁI (Unsplash) ====================
                saveOrUpdateProduct("Váy Bé Gái Công Chúa Hồng", "Váy bé gái thiết kế công chúa màu hồng",
                                new BigDecimal("399000"), new BigDecimal("549000"), 27, "BÉ GÁI", "CANIFA",
                                "https://images.unsplash.com/photo-1518831959646-742c3a14ebf7?auto=format&fit=crop&w=800&q=80",
                                true,
                                true);

                saveOrUpdateProduct("Đầm Bé Gái Hoa Nhí", "Đầm bé gái họa tiết hoa nhí xinh xắn",
                                new BigDecimal("299000"), new BigDecimal("399000"), 25, "BÉ GÁI", "CANIFA",
                                "https://images.unsplash.com/photo-1518831959646-742c3a14ebf7?auto=format&fit=crop&w=800&q=80",
                                false,
                                false);

                saveOrUpdateProduct("Áo Thun Bé Gái Hoạt Hình", "Áo thun bé gái in hình hoạt hình dễ thương",
                                new BigDecimal("179000"), new BigDecimal("249000"), 28, "BÉ GÁI", "CANIFA",
                                "https://images.unsplash.com/photo-1622290291468-a28f7a7dc6a8?auto=format&fit=crop&w=800&q=80",
                                true,
                                false);

                // ==================== DANH MỤC BÉ TRAI (Unsplash) ====================
                saveOrUpdateProduct("Áo Thun Bé Trai Siêu Nhân", "Áo thun bé trai in hình siêu nhân Marvel",
                                new BigDecimal("199000"), new BigDecimal("279000"), 29, "BÉ TRAI", "CANIFA",
                                "https://images.unsplash.com/photo-1519238263530-99bdd11df2ea?auto=format&fit=crop&w=800&q=80",
                                true,
                                true);

                saveOrUpdateProduct("Quần Short Bé Trai Thể Thao", "Quần short bé trai thể thao năng động",
                                new BigDecimal("149000"), new BigDecimal("199000"), 25, "BÉ TRAI", "CANIFA",
                                "https://images.unsplash.com/photo-1503919545889-aef636e10ad4?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);

                saveOrUpdateProduct("Áo Polo Bé Trai Lịch Sự", "Áo polo bé trai phong cách lịch sự",
                                new BigDecimal("249000"), new BigDecimal("349000"), 29, "BÉ TRAI", "CANIFA",
                                "https://images.unsplash.com/photo-1503944583220-79d8926ad5e2?auto=format&fit=crop&w=800&q=80",
                                true,
                                false);

                saveOrUpdateProduct("Áo Khoác Bé Trai Bomber", "Áo khoác bomber bé trai phong cách, giữ ấm tốt",
                                new BigDecimal("399000"), new BigDecimal("549000"), 27, "BÉ TRAI", "CANIFA",
                                "https://images.unsplash.com/photo-1516627145497-ae6968895b74?auto=format&fit=crop&w=800&q=80",
                                false,
                                true);
        }

        private void saveOrUpdateProduct(String name, String description, BigDecimal price,
                        BigDecimal originalPrice, int discount, String category,
                        String brand, String imageUrl, boolean isNew, boolean isBestseller) {

                Product product = productRepository.findByName(name).orElse(new Product());
                boolean isNewProduct = product.getId() == null;

                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setOriginalPrice(originalPrice);
                product.setDiscountPercent(discount);
                product.setCategory(category);
                product.setBrand(brand);
                product.setImageUrl(imageUrl);
                product.setNew(isNew);
                product.setBestseller(isBestseller);
                product.setAvailableSizes("S,M,L,XL,2XL");

                if (isNewProduct) {
                        product.setStock(100);
                        product.setSold(0);
                        product.setCreatedAt(LocalDateTime.now());
                        System.out.println("➕ Thêm mới: " + name);
                } else {
                        System.out.println("🔄 Cập nhật: " + name);
                }

                productRepository.save(product);
        }

        private void createSampleVouchers() {
                Voucher v1 = new Voucher();
                v1.setCode("SALE2026");
                v1.setTitle("Chào Xuân 2026");
                v1.setDescription("Giảm 30% cho đơn hàng từ 899K");
                v1.setDiscountPercent(30);
                v1.setMinOrderAmount(new BigDecimal("899000"));
                v1.setMaxDiscount(new BigDecimal("300000"));
                v1.setStartDate(LocalDateTime.now());
                v1.setEndDate(LocalDateTime.now().plusMonths(3));
                v1.setUsageLimit(500);
                v1.setUsedCount(0);
                v1.setActive(true);
                voucherRepository.save(v1);

                Voucher v2 = new Voucher();
                v2.setCode("FREESHIP");
                v2.setTitle("Miễn phí vận chuyển");
                v2.setDescription("Miễn phí giao hàng cho đơn từ 499K");
                v2.setDiscountAmount(new BigDecimal("35000"));
                v2.setMinOrderAmount(new BigDecimal("499000"));
                v2.setStartDate(LocalDateTime.now());
                v2.setEndDate(LocalDateTime.now().plusYears(1));
                v2.setUsageLimit(1000);
                v2.setUsedCount(0);
                v2.setActive(true);
                voucherRepository.save(v2);
        }

        private void createSampleReviews() {
                // Lấy user test để gán review
                User testUser = userRepository.findByEmail("user@test.com").orElse(null);
                if (testUser == null)
                        return;

                // Tạo thêm vài user để có nhiều người đánh giá
                User reviewer1 = createOrGetReviewer("reviewer1@test.com", "reviewer1", "Trần Thị Mai");
                User reviewer2 = createOrGetReviewer("reviewer2@test.com", "reviewer2", "Lê Văn Hùng");
                User reviewer3 = createOrGetReviewer("reviewer3@test.com", "reviewer3", "Phạm Thị Lan");

                // Lấy một số sản phẩm để gán review
                List<Product> products = productRepository.findAll();
                if (products.isEmpty())
                        return;

                // Review cho sản phẩm đầu tiên
                if (products.size() > 0) {
                        Product p = products.get(0);
                        createReview(p, testUser, 5,
                                        "Sản phẩm rất đẹp, chất lượng tốt. Giao hàng nhanh, đóng gói cẩn thận. Rất hài lòng!",
                                        true);
                        createReview(p, reviewer1, 4,
                                        "Chất vải mềm mại, form đẹp. Chỉ tiếc là màu hơi khác so với hình một chút.",
                                        true);
                        createReview(p, reviewer2, 5, "Mặc rất thoải mái, giá cả hợp lý. Sẽ ủng hộ shop dài dài.",
                                        false);
                }

                // Review cho sản phẩm thứ hai
                if (products.size() > 1) {
                        Product p = products.get(1);
                        createReview(p, reviewer1, 5,
                                        "Váy đẹp lắm, mặc lên rất xinh. Chất liệu thoáng mát, phù hợp mùa hè.", true);
                        createReview(p, reviewer3, 4, "Sản phẩm ok, giao hàng nhanh. Size đúng với bảng size.", false);
                }

                // Review cho sản phẩm thứ ba
                if (products.size() > 2) {
                        Product p = products.get(2);
                        createReview(p, testUser, 4, "Chân váy đẹp, xếp ly mềm rơi. Dễ phối với áo.", true);
                        createReview(p, reviewer2, 5, "Rất ưng ý, giá rẻ mà chất lượng không tồi.", true);
                        createReview(p, reviewer3, 3, "Sản phẩm tạm ổn, nhưng đường may chưa được tinh tế lắm.", false);
                }

                // Review cho sản phẩm thứ tư
                if (products.size() > 3) {
                        Product p = products.get(3);
                        createReview(p, reviewer1, 5, "Đầm dạ hội sang trọng, mặc lên rất sang. Đáng đồng tiền!", true);
                }

                // Review cho sản phẩm thứ năm
                if (products.size() > 4) {
                        Product p = products.get(4);
                        createReview(p, testUser, 5, "Áo khoác denim đẹp, phong cách Hàn Quốc. Chất vải dày dặn.",
                                        true);
                        createReview(p, reviewer2, 4, "Áo đẹp, mặc thoải mái. Ship hơi lâu nhưng sản phẩm ok.", false);
                }
        }

        private User createOrGetReviewer(String email, String username, String fullName) {
                return userRepository.findByEmail(email).orElseGet(() -> {
                        User user = new User();
                        user.setEmail(email);
                        user.setUsername(username);
                        user.setPassword("123456");
                        user.setFullName(fullName);
                        user.setPhone("098765432" + username.charAt(username.length() - 1));
                        user.setRole(User.Role.USER);
                        user.setEnabled(true);
                        user.setMemberLevel("Thành viên mới");
                        user.setMemberPoints(100);
                        return userRepository.save(user);
                });
        }

        private void createReview(Product product, User user, int rating, String comment, boolean verified) {
                if (reviewRepository.existsByProductIdAndUserId(product.getId(), user.getId())) {
                        return; // Đã có review rồi
                }
                Review review = new Review();
                review.setProduct(product);
                review.setUser(user);
                review.setRating(rating);
                review.setComment(comment);
                review.setVerified(verified);
                review.setCreatedAt(LocalDateTime.now().minusDays((int) (Math.random() * 30)));
                reviewRepository.save(review);
        }
}
