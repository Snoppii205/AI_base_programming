# 📚 Bài Tập DAO Pattern - Danh Sách File

## 📄 Các File Đã Chuẩn Bị

### 1. **bai_tap_DAO_pattern.docx** ⭐ CHÍNH
   - Bài tập chi tiết, định dạng Word
   - Bao gồm yêu cầu, hướng dẫn, tiêu chí đánh giá
   - **Khuyến nghị**: In hoặc đọc file này trước

### 2. **bai_tap_DAO_pattern.md**
   - Cùng nội dung với file .docx, định dạng Markdown
   - Dễ xem trên GitHub hoặc text editor

### 3. **README.md** 📖 HỮU ÍCH
   - Hướng dẫn chi tiết cách cài đặt và chạy project
   - Giải thích các khái niệm OOP và SOLID
   - Bao gồm các lệnh cần chạy
   - Xử lý lỗi thường gặp

### 4. **QUICK_REFERENCE.md** ⚡ NHANH
   - Cheat sheet, tóm tắt nhanh
   - Code template cho CRUD operations
   - Checklist các file cần tạo
   - Tips & tricks

### 5. **EXAMPLE_CODE.java** 💻 VÍ DỤ
   - Code hoàn chỉnh cho tất cả class
   - Copy-paste được ngay
   - Có comment giải thích chi tiết
   - Bao gồm: Student, DAO Interface, MySQLDAO, MockDAO, Service, Tests

### 6. **database.sql** 🗄️ DATABASE
   - Script tạo database MySQL
   - Tạo các bảng: students, courses, departments, grades
   - Insert dữ liệu mẫu
   - Tạo views cho thống kê

### 7. **pom.xml** 🛠️ MAVEN
   - Cấu hình Maven cho project
   - Tất cả dependencies cần thiết
   - Configuration cho test, compile, JAR

### 8. **INDEX.md** (File này)
   - Danh sách và mô tả các file

---

## 🚀 Cách Bắt Đầu

### Bước 1: Đọc Bài Tập
1. Mở **`bai_tap_DAO_pattern.docx`** (bản chính)
2. Hiểu yêu cầu bài tập
3. Xem tiêu chí đánh giá

### Bước 2: Chuẩn Bị Database
1. Mở **`database.sql`**
2. Chạy script trong MySQL
3. Kiểm tra dữ liệu: `SELECT * FROM students;`

### Bước 3: Tạo Project Maven
1. Tạo thư mục `StudentDAO`
2. Copy nội dung từ **`pom.xml`**
3. Tạo cấu trúc thư mục:
   ```
   StudentDAO/
   ├── src/main/java/
   │   ├── entity/
   │   ├── dao/
   │   ├── service/
   │   └── Main.java
   ├── src/test/java/
   └── pom.xml
   ```

### Bước 4: Copy Code Từ Example
1. Mở **`EXAMPLE_CODE.java`**
2. Copy từng class vào project
3. Điều chỉnh theo cần thiết

### Bước 5: Cấu Hình Connection
1. Cập nhật thông tin database trong `MySQLStudentDAO.java`:
   ```java
   new MySQLStudentDAO(
       "jdbc:mysql://localhost:3306/student_management",
       "root",
       "your_password"  // ← Thay bằng password của bạn
   );
   ```

### Bước 6: Test & Run
1. Chạy: `mvn test`
2. Chạy: `mvn exec:java -Dexec.mainClass="Main"`

---

## 📋 Checklist Triển Khai

- [ ] Đọc bài tập từ bai_tap_DAO_pattern.docx
- [ ] Kiểm tra các yêu cầu và mục tiêu
- [ ] Tạo database từ database.sql
- [ ] Tạo project Maven với pom.xml
- [ ] Tạo Entity: Student.java
- [ ] Tạo Interface: StudentDAO.java
- [ ] Tạo Implementation: MySQLStudentDAO.java
- [ ] Tạo Mock: MockStudentDAO.java
- [ ] Tạo Service: StudentService.java
- [ ] Tạo Main: Main.java
- [ ] Tạo Tests: StudentServiceTest.java
- [ ] Test chạy được
- [ ] Tạo Entity bổ sung (nâng cao)
- [ ] Viết README giải thích thiết kế
- [ ] Nộp bài

---

## 💡 Sử Dụng QUICK_REFERENCE

Khi bạn cần:
- **Lỗi gặp phải?** → Xem phần "Lỗi Thường Gặp"
- **Quên cấu trúc method?** → Xem "Cấu Trúc Cơ Bản Của Một DAO Method"
- **Viết test?** → Xem "Unit Test Template"
- **Lệnh chạy?** → Xem "Chạy Project - Commands Nhanh"

---

## 🎯 Điểm Cần Nhớ

1. **DAO là gì?** → Tách biệt truy cập dữ liệu khỏi business logic
2. **Tại sao cần Mock?** → Testing mà không cần database thực
3. **SOLID là gì?** → 5 nguyên tắc thiết kế tốt
4. **Dependency Injection?** → Truyền dependency vào thay vì tạo bên trong
5. **Interface vs Implementation?** → Interface là hợp đồng, implementation là chi tiết

---

## 📞 Khi Gặp Vấn Đề

### Lỗi Database
- Kiểm tra MySQL đang chạy: `mysql -u root -p`
- Chạy lại database.sql
- Xem phần "Xử Lý Lỗi" trong README.md

### Lỗi Code
- Xem EXAMPLE_CODE.java
- Kiểm tra Maven: `mvn clean install`
- Xem QUICK_REFERENCE.md phần "Lỗi Thường Gặp"

### Không Hiểu Concept
- Đọc README.md phần "Khái Niệm OOP Được Áp Dụng"
- Xem bai_tap_DAO_pattern.docx phần "Khái Niệm OOP"
- Tìm trên Google: "DAO Pattern in Java"

---

## 🎓 Tài Liệu Tham Khảo

- **DAO Pattern**: https://www.baeldung.com/java-dao-pattern
- **SOLID Principles**: https://en.wikipedia.org/wiki/SOLID
- **JDBC Guide**: https://docs.oracle.com/javase/tutorial/jdbc/
- **Maven**: https://maven.apache.org/guides/

---

## 📝 Tóm Tắt Các File

| File | Loại | Mục Đích | Kích Thước |
|------|------|---------|-----------|
| bai_tap_DAO_pattern.docx | 📄 Word | Bài tập chính | ~100KB |
| bai_tap_DAO_pattern.md | 📝 Markdown | Bài tập (text) | ~50KB |
| README.md | 📖 Guide | Hướng dẫn chi tiết | ~80KB |
| QUICK_REFERENCE.md | ⚡ Cheat Sheet | Tóm tắt nhanh | ~30KB |
| EXAMPLE_CODE.java | 💻 Code | Code mẫu hoàn chỉnh | ~40KB |
| database.sql | 🗄️ SQL | Database schema | ~10KB |
| pom.xml | 🛠️ Config | Maven config | ~3KB |

---

## ✅ Phải Nộp Gì?

Theo yêu cầu trong bai_tap_DAO_pattern.docx:

```
StudentDAO/
├── src/
│   ├── main/java/
│   │   ├── entity/
│   │   │   ├── Student.java
│   │   │   └── [Entity2].java
│   │   ├── dao/
│   │   │   ├── StudentDAO.java
│   │   │   ├── MySQLStudentDAO.java
│   │   │   ├── MockStudentDAO.java
│   │   │   └── [Entity2]DAO.java
│   │   ├── service/
│   │   │   ├── StudentService.java
│   │   │   └── [Entity2]Service.java
│   │   └── Main.java
│   └── test/java/
│       ├── StudentServiceTest.java
│       └── [Entity2]ServiceTest.java
├── pom.xml
├── database.sql
└── README.md
```

---

**Chúc bạn hoàn thành bài tập thành công! 🚀**

*Nếu có câu hỏi, tham khảo các file hướng dẫn hoặc README.md*
