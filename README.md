# Clothes Shop Online

Hệ thống thương mại điện tử bán quần áo trực tuyến.

## 🚀 Setup Project

### 1. Clone repository

```bash
git clone <repository-url>
cd Clothesshoponline_v2
```

### 2. Cấu hình Database

Tạo file `.env` từ template `.env.example`:

```bash
# Windows
copy .env.example .env

# Linux/Mac
cp .env.example .env
```

Sau đó mở file `.env` và thay thế các giá trị với thông tin database của bạn:

```properties
DB_URL=jdbc:mysql://your-database-host:port/database-name?ssl-mode=REQUIRED&createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8
DB_USERNAME=your-database-username
DB_PASSWORD=your-database-password
```

### 3. Build và chạy project

```bash
# Clean và compile
./mvnw clean compile

# Chạy application
./mvnw spring-boot:run
```

Hoặc trên Windows:

```bash
mvnw.cmd clean compile
mvnw.cmd spring-boot:run
```

### 4. Truy cập application

- Website: http://localhost:8080
- Admin panel: http://localhost:8080/admin

## 🔒 Security Note

**QUAN TRỌNG**:

- ⚠️ KHÔNG BAO GIỜ commit file `.env` lên Git
- ✅ File `.env` đã được thêm vào `.gitignore`
- ✅ Chỉ commit file `.env.example` (không chứa thông tin nhạy cảm)

## 📝 Environment Variables

| Variable       | Mô tả                        | Bắt buộc |
| -------------- | ---------------------------- | -------- |
| DB_URL         | Database connection URL      | ✅       |
| DB_USERNAME    | Database username            | ✅       |
| DB_PASSWORD    | Database password            | ✅       |
| ADMIN_USERNAME | Admin username (development) | ❌       |
| ADMIN_PASSWORD | Admin password (development) | ❌       |

## 🛠️ Technologies

- Java 17
- Spring Boot 3.5.9
- MySQL
- Thymeleaf
- Spring Security
- Spring Data JPA

## 📦 Build

```bash
./mvnw clean package
```

## 🆘 Troubleshooting

### Lỗi: "Could not load .env file"

**Nguyên nhân:** File `.env` chưa được tạo

**Giải pháp:**

```bash
# Windows
copy .env.example .env

# Linux/Mac
cp .env.example .env
```

Sau đó chỉnh sửa file `.env` với thông tin database của bạn.

### Lỗi: "Access denied for user"

**Nguyên nhân:** Sai username/password hoặc database không cho phép kết nối

**Giải pháp:**

1. Kiểm tra lại `DB_USERNAME` và `DB_PASSWORD` trong file `.env`
2. Đảm bảo database server đang chạy
3. Kiểm tra firewall/security group cho phép kết nối

### Lỗi: "Unknown database"

**Nguyên nhân:** Database chưa tồn tại

**Giải pháp:**

- Thêm `createDatabaseIfNotExist=true` vào `DB_URL` trong file `.env`
- Hoặc tạo database thủ công:
  ```sql
  CREATE DATABASE clothesshoponline CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```

### App không kết nối được database khi chạy trên máy khác

**Nguyên nhân:** Mỗi máy cần có file `.env` riêng

**Giải pháp:**

1. Mỗi developer tạo file `.env` với credentials của họ
2. File `.env` không được commit lên Git (đã có trong `.gitignore`)
3. Sử dụng file `.env.example` làm template

## 👥 Làm việc nhóm

### Khi có member mới join team:

1. **Clone repository**
2. **Tạo file `.env`** từ `.env.example`
3. **Xin credentials database** từ team lead (hoặc dùng database riêng)
4. **Run `mvnw spring-boot:run`**

### Khi deploy lên server:

- **Development/Staging:** Tạo file `.env` trên server
- **Production:** Dùng environment variables của hosting platform (Heroku Config Vars, AWS Parameter Store, Azure Key Vault, etc.)

## 📚 Documentation

- [Setup Environment Variables](./ENVIRONMENT_SETUP.md) - Hướng dẫn chi tiết về cấu hình env vars
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

