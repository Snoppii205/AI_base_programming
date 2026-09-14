# BÁO CÁO BÀI TẬP: XÂY DỰNG MÔ HÌNH DAO PATTERN VỚI JAVA VÀ MYSQL
**Môn học**: Lập trình Hướng Đối tượng & Thiết kế Mẫu (OOP & Design Patterns)  
**Đơn vị thực hiện**: Sinh viên thực hiện bài tập thực hành  

---

## I. TỔNG QUAN HỆ THỐNG VÀ KIẾN TRÚC

Hệ thống được thiết kế theo mô hình **3 tầng (3-Tier Architecture)** kết hợp chặt chẽ với mẫu thiết kế **Data Access Object (DAO) Pattern**:

```
 ┌────────────────────────────────────────────────────────┐
 │                   Main Application                     │
 └───────────────────────────┬────────────────────────────┘
                             │ Khởi tạo & Tiêm phụ thuộc (DI)
                             ▼
 ┌────────────────────────────────────────────────────────┐
 │           Service Layer (Business Logic)               │
 │     - StudentService          - CourseService          │
 └───────────────────────────┬────────────────────────────┘
                             │ Phụ thuộc vào Abstraction (DIP)
                             ▼
 ┌────────────────────────────────────────────────────────┐
 │              DAO Interface (Hợp đồng)                  │
 │     - StudentDAO              - CourseDAO              │
 └─────────────┬────────────────────────────┬─────────────┘
               │ implements                 │ implements
               ▼                            ▼
 ┌───────────────────────────┐ ┌──────────────────────────┐
 │  MySQL Implementation     │ │   Mock Implementation    │
 │  - MySQLStudentDAO        │ │   - MockStudentDAO       │
 │  - MySQLCourseDAO         │ │   - MockCourseDAO        │
 └─────────────┬─────────────┘ └────────────┬─────────────┘
               │                            │
               ▼ JDBC                       ▼ RAM
      MySQL Database                     In-Memory
   (student_management)              (Dùng cho Unit Test)
```

---

## II. TRẢ LỜI CÁC CÂU HỎI THẢO LUẬN (MỤC 11 - ĐỀ BÀI)

### Câu 1: Tại sao cần DAO pattern? Nêu rõ lợi ích của việc tách biệt logic truy cập dữ liệu.

**Trả lời:**
DAO (Data Access Object) Pattern là mẫu thiết kế giúp trừu tượng hóa và đóng gói tất cả các thao tác truy cập vào nguồn dữ liệu. Thay vì viết các câu lệnh SQL trực tiếp rải rác trong Service Layer hay Controller, DAO đóng vai trò là một tầng trung gian độc lập.

**Các lợi ích cụ thể của việc tách biệt logic truy cập dữ liệu:**
1. **Phân tách mối quan tâm (Separation of Concerns - SoC):** 
   - Tầng nghiệp vụ (Service Layer) chỉ tập trung vào quy tắc kinh doanh (tính điểm GPA, lọc sinh viên, thẩm định điều kiện).
   - Tầng dữ liệu (DAO) chỉ tập trung vào việc giao tiếp với nguồn dữ liệu (kết nối DB, chuẩn bị PreparedStatement, map ResultSet thành Object).
2. **Khả năng kiểm thử độc lập (Testability):** 
   - Nhờ có DAO interface, ta có thể tạo `MockStudentDAO` / `MockCourseDAO` chạy trên RAM để viết Unit Test cho Service Layer mà không cần kết nối mạng hay cài đặt cơ sở dữ liệu thật.
3. **Dễ bảo trì và mở rộng (Maintainability & Extensibility):** 
   - Khi cấu trúc bảng hoặc câu lệnh SQL thay đổi (ví dụ tối ưu index, đổi tên cột), lập trình viên chỉ cần sửa trong lớp DAO mà không làm ảnh hưởng đến bất kỳ dòng code nào ở tầng Service.
4. **Tái sử dụng mã nguồn (Reusability):** 
   - Các thao tác dữ liệu chuẩn (`save`, `findById`, `findAll`, `update`, `delete`) được viết một lần trong DAO và có thể được gọi từ nhiều Service khác nhau.

---

### Câu 2: Làm thế nào DAO pattern áp dụng các nguyên tắc SOLID? Giải thích cụ thể từng nguyên tắc.

Dự án áp dụng trọn vẹn cả 5 nguyên tắc **SOLID**:

| Nguyên tắc | Biểu hiện cụ thể trong mã nguồn của bài tập |
| :--- | :--- |
| **S - Single Responsibility (Đơn trách nhiệm)** | Mỗi class chỉ nắm giữ duy nhất một trách nhiệm:<br>• `Student`/`Course`: Chỉ đóng gói dữ liệu và kiểm tra tính hợp lệ (Validation).<br>• `MySQLStudentDAO`/`MySQLCourseDAO`: Chỉ chịu trách nhiệm kết nối và thực thi SQL.<br>• `StudentService`/`CourseService`: Chỉ thực thi logic nghiệp vụ (thống kê, xếp loại sinh viên). |
| **O - Open/Closed (Mở để mở rộng, Đóng để sửa đổi)** | Hệ thống dễ dàng mở rộng nguồn lưu trữ dữ liệu mới (ví dụ: tạo `MongoStudentDAO` hoặc `PostgresStudentDAO` bằng cách `implements StudentDAO`) mà **hoàn toàn không cần sửa đổi một dòng code nào** trong `StudentService`. |
| **L - Liskov Substitution (Thay thế Liskov)** | Bất kỳ implementation nào của `StudentDAO` (`MySQLStudentDAO` hay `MockStudentDAO`) đều có thể thay thế cho nhau hoàn hảo trong `StudentService` mà không làm thay đổi tính đúng đắn của chương trình. |
| **I - Interface Segregation (Phân tách Interface)** | Thay vì tạo một Interface khổng lồ dùng chung cho tất cả các đối tượng, dự án tách riêng `StudentDAO` và `CourseDAO`. Mỗi interface chỉ chứa các thao tác liên quan trực tiếp đến nghiệp vụ của entity đó. |
| **D - Dependency Inversion (Đảo ngược phụ thuộc)** | `StudentService` và `CourseService` **không phụ thuộc trực tiếp** vào class cụ thể (`MySQLStudentDAO`), mà phụ thuộc vào trừu tượng (`StudentDAO` interface). Đối tượng DAO cụ thể được truyền vào thông qua **Constructor Injection**. |

---

### Câu 3: Nếu chuyển sang database khác (MongoDB, PostgreSQL), cần thay đổi gì? Lợi thế của thiết kế DAO của bạn là gì?

**Trả lời:**

#### Những gì cần thay đổi:
1. **Thêm Driver/Dependency:** Khai báo driver tương ứng trong `pom.xml` (ví dụ `mongodb-driver-sync` hoặc `postgresql`).
2. **Tạo lớp DAO mới:** Tạo class mới hiện thực interface tương ứng:
   - Với PostgreSQL: Tạo `PostgreSQLStudentDAO implements StudentDAO`.
   - Với MongoDB: Tạo `MongoDBStudentDAO implements StudentDAO` (sử dụng `MongoCollection<Document>`).
3. **Thay đổi cấu hình khởi tạo:** Tại `Main.java` (hoặc cấu hình Dependency Injection / Spring), đổi khởi tạo từ `new MySQLStudentDAO(...)` sang `new MongoDBStudentDAO(...)`.

#### Những gì KHÔNG CẦN THAY ĐỔI (Lợi thế vượt trội của thiết kế):
- **Interface `StudentDAO` giữ nguyên 100%.**
- **Toàn bộ tầng `StudentService` giữ nguyên 100%**, không cần sửa đổi hay biên dịch lại bất kỳ phương thức nghiệp vụ nào.
- **Tất cả các bộ Unit Test hiện tại vẫn nguyên vẹn giá trị**, đảm bảo hệ thống không bị hồi quy (regression).

---

### Câu 4: So sánh MockStudentDAO vs MySQLStudentDAO. Tại sao cần cả hai?

| Tiêu chí | `MySQLStudentDAO` | `MockStudentDAO` |
| :--- | :--- | :--- |
| **Môi trường sử dụng** | Production / Môi trường thật | Development / Unit Testing / CI-CD |
| **Nguồn dữ liệu** | Cơ sở dữ liệu MySQL thật (Disk/Network) | Bộ nhớ RAM (`ArrayList`, `HashMap`) |
| **Tốc độ thực thi** | Chậm hơn (do kết nối mạng, I/O đĩa, phân tích câu lệnh SQL) | Cực nhanh (tính bằng mili-giây trên RAM) |
| **Phụ thuộc bên ngoài** | Cần dịch vụ MySQL đang chạy, port 3306, đúng tài khoản/mật khẩu, database đã import | **Không phụ thuộc bất kỳ phần mềm bên ngoài nào**, độc lập 100% |
| **Trạng thái dữ liệu** | Lưu trữ vĩnh viễn (Persistent) | Dữ liệu tạm thời, tự làm mới sau mỗi lần test |

**Tại sao hệ thống chuyên nghiệp luôn cần cả hai?**
- **Cần `MySQLStudentDAO`** để ứng dụng thực sự hoạt động trong đời thực, lưu trữ dữ liệu an toàn và bền vững cho người dùng cuối.
- **Cần `MockStudentDAO`** để:
  1. Viết Unit Test cho tầng Service độc lập: Khi test Service bị lỗi, ta khẳng định ngay lỗi nằm ở logic của Service chứ không phải do rớt mạng hay sập MySQL.
  2. Giúp quy trình CI/CD (GitHub Actions, Jenkins) tự động build và chạy test mà không bắt buộc phải dựng sẵn MySQL server.
  3. Lập trình viên phía Frontend hoặc Service có thể tiếp tục công việc ngay cả khi Database server chưa sẵn sàng.

---

### Câu 5: Nêu 3 cải tiến có thể thêm vào DAO pattern này.

Dưới đây là 3 cải tiến quan trọng trong các hệ thống phần mềm quy mô lớn (Enterprise):

#### 1. Connection Pooling (Quản lý Hồ kết nối - ví dụ HikariCP)
- **Vấn đề hiện tại:** Mỗi thao tác DAO mở một kết nối mới qua `DriverManager.getConnection()` rồi đóng lại, tiêu tốn rất nhiều tài nguyên handshake CPU và mạng.
- **Cải tiến:** Sử dụng thư viện **HikariCP** (đã cấu hình sẵn trong `pom.xml`). Hồ kết nối sẽ duy trì sẵn một số lượng kết nối đang mở; khi một luồng cần thao tác DB, nó mượn kết nối từ hồ và trả lại ngay sau đó, giúp tăng thông lượng xử lý lên gấp nhiều lần.

#### 2. Phân trang (Pagination) và Lọc nâng cao
- **Vấn đề hiện tại:** Phương thức `findAll()` tải toàn bộ bảng về RAM. Khi bảng có hàng triệu bản ghi, ứng dụng sẽ bị tràn bộ nhớ (`OutOfMemoryError`).
- **Cải tiến:** Thêm tham số phân trang vào interface DAO:
  ```java
  List<Student> findAll(int page, int pageSize);
  ```
  Trong MySQL triển khai câu lệnh: `SELECT * FROM students LIMIT ? OFFSET ?`.

#### 3. Quản lý Giao dịch (Transaction Management)
- **Vấn đề hiện tại:** Mỗi lệnh trong DAO thực thi độc lập (Auto-commit). Khi thực hiện nghiệp vụ liên quan nhiều bảng (ví dụ: đăng ký môn học và trừ học phí), nếu bước 1 thành công mà bước 2 lỗi, dữ liệu sẽ bị bất nhất.
- **Cải tiến:** Bổ sung cơ chế Transaction (`beginTransaction()`, `commit()`, `rollback()` hoặc Unit of Work Pattern) để đảm bảo tính trọn vẹn **ACID**: Hoặc tất cả đều thành công, hoặc không thao tác nào được ghi nhận.

---

## III. TỔNG KẾT KẾT QUẢ KIỂM THỬ

- **Unit Tests:** Đạt **16/16 test cases thành công (100% Passed)**:
  - `StudentServiceTest`: 9 test cases (Bao phủ Thêm, Sửa, Xóa, Tìm kiếm theo ID, Lọc GPA, Bắt ngoại lệ GPA/Email).
  - `CourseServiceTest`: 7 test cases (Bao phủ Đăng ký, Đọc danh sách, Cập nhật, Xóa, Lọc tín chỉ, Bắt ngoại lệ tín chỉ/tên rỗng).
- **Thực thi luồng chính (`Main.java`):** Chạy thành công toàn bộ 9 bước nghiệp vụ trên MySQL và toàn bộ các bước trên Mock DAO độc lập.
