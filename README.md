# Bài Tập DAO Pattern - Java & MySQL

## 📚 Giới Thiệu

Bài tập này giúp bạn áp dụng các khái niệm **Lập Trình Hướng Đối Tượng (OOP)** từ Bài Học 4 vào thực tế bằng cách xây dựng một **Data Access Object (DAO) Pattern** sử dụng Java và MySQL.

### 🎯 Mục Tiêu Học Tập

- Hiểu và áp dụng **4 trụ cột OOP**: Đóng gói, Trừu tượng hóa, Kế thừa, Đa hình
- Tuân theo **nguyên tắc SOLID**, đặc biệt là SRP (Single Responsibility) và DIP (Dependency Inversion)
- Áp dụng **DAO Pattern** để tách biệt logic truy cập dữ liệu từ logic nghiệp vụ
- Viết **Unit Tests** để đảm bảo chất lượng code
- Sử dụng **Dependency Injection** để làm code linh hoạt hơn

---

## 🛠️ Yêu Cầu Hệ Thống

### Phần Mềm Cần Cài Đặt
- **Java JDK 11+** - [Download](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **MySQL 5.7+** - [Download](https://dev.mysql.com/downloads/mysql/)
- **Maven 3.6+** (để quản lý dependencies) - [Download](https://maven.apache.org/download.cgi)
- **IDE**: IntelliJ IDEA hoặc Eclipse (khuyến nghị)

### Kiểm Tra Cài Đặt
```bash
# Kiểm tra Java
java -version

# Kiểm tra Maven
mvn -version

# Kiểm tra MySQL
mysql --version
```

---

## 📦 Cấu Trúc Project

```
StudentDAO/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── entity/
│   │       │   ├── Student.java              ← Model class
│   │       │   └── [Entity2].java            ← Entity bổ sung
│   │       ├── dao/
│   │       │   ├── StudentDAO.java           ← Interface (trừu tượng hóa)
│   │       │   ├── MySQLStudentDAO.java      ← Triển khai MySQL
│   │       │   ├── MockStudentDAO.java       ← Triển khai giả (testing)
│   │       │   └── [Entity2]DAO.java         ← DAO bổ sung
│   │       ├── service/
│   │       │   ├── StudentService.java       ← Business logic
│   │       │   └── [Entity2]Service.java     ← Service bổ sung
│   │       └── Main.java                     ← Entry point
│   └── test/
│       └── java/
│           ├── StudentServiceTest.java       ← Unit tests
│           └── [Entity2]ServiceTest.java     ← Test bổ sung
├── pom.xml                                   ← Maven configuration
├── database.sql                              ← Script tạo database
└── README.md                                 ← File này
```

---

## 🚀 Bước 1: Cài Đặt Database

### 1.1 Khởi Động MySQL
```bash
# Trên Windows
mysql -u root -p

# Trên Linux/Mac
sudo mysql -u root -p
```

### 1.2 Tạo Database
Chạy script `database.sql`:

```bash
# Từ thư mục project
mysql -u root -p < database.sql

# Hoặc từ bên trong MySQL CLI
source database.sql;
```

### 1.3 Kiểm Tra Dữ Liệu
```sql
USE student_management;
SELECT * FROM students;
SELECT * FROM courses;
SELECT * FROM departments;
```

---

## 🔧 Bước 2: Cấu Hình Project

### 2.1 Cập Nhật Thông Tin Database
Mở file `MySQLStudentDAO.java` và cập nhật thông tin kết nối:

```java
StudentDAO studentDAO = new MySQLStudentDAO(
    "jdbc:mysql://localhost:3306/student_management",
    "root",           // ← Thay bằng username của bạn
    "your_password"   // ← Thay bằng password của bạn
);
```

### 2.2 Tải Dependencies
```bash
# Tại thư mục project
mvn clean install

# Hoặc chỉ download dependencies
mvn dependency:resolve
```

---

## ▶️ Bước 3: Chạy Project

### 3.1 Chạy Main Application
```bash
# Compile
mvn compile

# Run
mvn exec:java -Dexec.mainClass="Main"

# Hoặc chạy từ IDE (IntelliJ/Eclipse)
# Click chuột phải Main.java → Run
```

### 3.2 Output Mong Đợi
```
📚 All Students:
Student{id=1, name='Nguyễn Văn A', email='a@university.edu', gpa=3.5, enrollmentDate='2023-09-01'}
Student{id=2, name='Trần Thị B', email='b@university.edu', gpa=3.8, enrollmentDate='2023-09-01'}
...

🔍 Search results for 'Nguyễn':
Student{id=1, name='Nguyễn Văn A', email='a@university.edu', gpa=3.5, enrollmentDate='2023-09-01'}

🌟 Students with GPA >= 3.5:
Student{id=2, name='Trần Thị B', email='b@university.edu', gpa=3.8, enrollmentDate='2023-09-01'}
...
```

---

## ✅ Bước 4: Chạy Unit Tests

### 4.1 Chạy Tất Cả Tests
```bash
mvn test
```

### 4.2 Chạy Test Cụ Thể
```bash
# Chạy một class test
mvn test -Dtest=StudentServiceTest

# Chạy một method test cụ thể
mvn test -Dtest=StudentServiceTest#testRegisterStudent
```

### 4.3 Output Tests
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running StudentServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.123 s
[INFO] BUILD SUCCESS
```

---

## 📖 Khái Niệm OOP Được Áp Dụng

### 1. **Đóng Gói (Encapsulation)**
```java
// Thuộc tính private
private double gpa;

// Chỉ truy cập qua getters/setters với validation
public void setGpa(double gpa) {
    if (gpa < 0.0 || gpa > 4.0) {
        throw new IllegalArgumentException("Invalid GPA");
    }
    this.gpa = gpa;
}
```

### 2. **Trừu Tượng Hóa (Abstraction)**
```java
// Interface định nghĩa hợp đồng, ẩn chi tiết triển khai
public interface StudentDAO {
    void save(Student student) throws Exception;
    Student findById(int id) throws Exception;
    // ...
}
```

### 3. **Kế Thừa (Inheritance)**
```java
// MySQLStudentDAO triển khai StudentDAO
public class MySQLStudentDAO implements StudentDAO {
    // Triển khai tất cả phương thức từ interface
}
```

### 4. **Đa Hình (Polymorphism)**
```java
// Cùng một interface, hai triển khai khác nhau
StudentDAO mysqlDAO = new MySQLStudentDAO(...);
StudentDAO mockDAO = new MockStudentDAO();

// Cả hai có thể sử dụng giống nhau nhưng hoạt động khác
mysqlDAO.save(student);  // Lưu vào database
mockDAO.save(student);   // Lưu vào memory
```

---

## 🎯 Nguyên Tắc SOLID

### 1. **Single Responsibility Principle (SRP)**
- `Student`: Chỉ đại diện cho dữ liệu sinh viên
- `MySQLStudentDAO`: Chỉ truy cập database MySQL
- `StudentService`: Chỉ xử lý business logic

### 2. **Open/Closed Principle (OCP)**
- Có thể thêm `PostgreSQLStudentDAO` mà không thay đổi code cũ
- Interface `StudentDAO` mở cho mở rộng, đóng cho sửa đổi

### 3. **Liskov Substitution Principle (LSP)**
- Có thể thay `MySQLStudentDAO` bằng `MockStudentDAO` mà không có gì bị hỏng

### 4. **Interface Segregation Principle (ISP)**
- Interface `StudentDAO` chỉ chứa các phương thức liên quan đến Student
- Không bắt buộc implement các phương thức không cần

### 5. **Dependency Inversion Principle (DIP)**
- `StudentService` phụ thuộc vào `StudentDAO` (abstraction)
- Không phụ thuộc vào `MySQLStudentDAO` (implementation)

```java
// ✅ Đúng - DIP
public StudentService(StudentDAO studentDAO) {
    this.studentDAO = studentDAO;  // Nhận abstraction
}

// ❌ Sai - Violates DIP
public StudentService() {
    this.studentDAO = new MySQLStudentDAO(...);  // Tạo instance cụ thể
}
```

---

## 🔍 DAO Pattern Là Gì?

### Định Nghĩa
**Data Access Object (DAO)** là một mẫu thiết kế cô lập logic truy cập dữ liệu từ logic nghiệp vụ.

### Lợi Ích
1. **Tách biệt mối quan tâm**: Nghiệp vụ không cần biết chi tiết database
2. **Dễ thay đổi database**: Chỉ cần tạo DAO mới, không thay đổi Service
3. **Dễ testing**: Có thể mock DAO để testing Service mà không cần database thực
4. **Tái sử dụng**: Cùng một interface có thể có nhiều triển khai

### Sơ Đồ
```
┌─────────────┐
│   Main      │
└──────┬──────┘
       │
       ▼
┌──────────────────┐
│  StudentService  │  (Business Logic)
└──────┬───────────┘
       │
       ▼ (depends on)
┌──────────────────┐
│  StudentDAO      │  (Interface - Abstraction)
└──────┬───────────┘
       │
       ├─────────────────┬──────────────┐
       ▼                 ▼              ▼
┌────────────────┐ ┌──────────────┐ ┌──────────┐
│ MySQLDAO       │ │ MongoDAO     │ │ MockDAO  │
│ (Database)     │ │ (MongoDB)    │ │ (Testing)│
└────────────────┘ └──────────────┘ └──────────┘
```

---

## 🧪 Dependency Injection (Tiêm Phụ Thuộc)

### Lợi Ích
- Làm code linh hoạt hơn
- Dễ testing hơn
- Dễ thay đổi implementation

### Ví Dụ
```java
// ✅ Tốt - Dependency Injection
StudentDAO mockDAO = new MockStudentDAO();
StudentService service = new StudentService(mockDAO);  // Tiêm vào

// ❌ Xấu - Hard Dependency
public StudentService() {
    this.studentDAO = new MySQLStudentDAO(...);  // Gắn chặt
}
```

---

## 📝 Hướng Dẫn Mở Rộng (Phần Nâng Cao)

### 1. Thêm Entity Mới
Tạo `Course.java` tương tự `Student.java`:
```java
public class Course {
    private int id;
    private String name;
    private int credits;
    private String instructor;
    // Getters, setters, validation...
}
```

Tạo interface `CourseDAO.java`:
```java
public interface CourseDAO {
    void save(Course course) throws Exception;
    Course findById(int id) throws Exception;
    // ...
}
```

### 2. Triển Khai Relationships
Cập nhật `Student.java` để chứa danh sách Course:
```java
public class Student {
    // ...
    private List<Course> enrolledCourses;
    
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }
}
```

### 3. Connection Pooling
Thay vì `DriverManager.getConnection()`, sử dụng HikariCP:
```java
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/student_management");
config.setUsername("root");
config.setPassword("password");
HikariDataSource ds = new HikariDataSource(config);

Connection conn = ds.getConnection();
```

### 4. Transaction Management
```java
public void transferCourse(int studentId, int courseId) throws Exception {
    Connection conn = getConnection();
    try {
        conn.setAutoCommit(false);
        
        // Thực hiện các thao tác
        enrollStudent(conn, studentId, courseId);
        updateStudentStatus(conn, studentId);
        
        conn.commit();
    } catch (Exception e) {
        conn.rollback();
        throw e;
    } finally {
        conn.close();
    }
}
```

---

## 🐛 Xử Lý Lỗi Thường Gặp

### Lỗi: "No suitable driver found"
```
com.mysql.cj.jdbc.Driver not found
```
**Giải pháp**: Kiểm tra Maven đã tải MySQL driver
```bash
mvn dependency:resolve
mvn clean install
```

### Lỗi: "Connection refused"
```
java.net.ConnectException: Connection refused
```
**Giải pháp**: Kiểm tra MySQL có đang chạy không
```bash
# Windows
mysql -u root -p

# Linux
sudo systemctl start mysql
```

### Lỗi: "Access denied for user"
```
Access denied for user 'root'@'localhost'
```
**Giải pháp**: Kiểm tra username/password trong MySQLStudentDAO.java

### Lỗi: "Table 'student_management.students' doesn't exist"
**Giải pháp**: Chạy database.sql
```bash
mysql -u root -p < database.sql
```

---

## 📚 Tài Liệu Tham Khảo

- **DAO Pattern**: https://www.baeldung.com/java-dao-pattern
- **SOLID Principles**: Clean Code by Robert C. Martin
- **JDBC Best Practices**: https://docs.oracle.com/javase/tutorial/jdbc/
- **MySQL JDBC Driver**: https://dev.mysql.com/doc/connector-j/en/
- **JUnit 4 Guide**: https://junit.org/junit4/
- **Maven Guide**: https://maven.apache.org/guides/getting-started/

---

## 🎓 Câu Hỏi Tự Kiểm Tra

1. Tại sao cần DAO pattern? Liệt kê ít nhất 3 lợi ích.
2. Sự khác biệt giữa `MySQLStudentDAO` và `MockStudentDAO` là gì?
3. Nếu chuyển sang MongoDB, cần thay đổi gì?
4. Giải thích nguyên tắc DIP và cách áp dụng trong project.
5. Tại sao interface `StudentDAO` lại quan trọng?
6. Làm thế nào để kiểm tra lớp `StudentService` mà không cần database thực?

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề:
1. Kiểm tra lại các bước cài đặt
2. Xem phần "Xử Lý Lỗi Thường Gặp"
3. Kiểm tra log của MySQL: `/var/log/mysql/error.log`

---

**Chúc bạn hoàn thành bài tập thành công! 🚀**

---

*Bài tập này được tạo cho Bài Học 4: Lập Trình Hướng Đối Tượng - Đại Học Phú Xuân*
