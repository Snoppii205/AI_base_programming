/**
 * ================================================================================
 * FILE: EXAMPLE_CODE.java
 * MỤC ĐÍCH: Ví dụ code hoàn chỉnh cho bài tập DAO Pattern
 * HƯỚNG DẪN: Copy các class dưới đây vào project của bạn
 * ================================================================================
 */

// ============================================================================
// PHẦN 1: ENTITY - Student.java
// ============================================================================

/**
 * Class Student đại diện cho một sinh viên
 * Áp dụng: Đóng gói (Encapsulation), Validation
 */
public class Student {
    private int id;
    private String name;
    private String email;
    private double gpa;
    private String enrollmentDate;
    
    // Constructor mặc định
    public Student() {}
    
    // Constructor với tham số
    public Student(String name, String email, double gpa, String enrollmentDate) {
        this.name = name;
        this.email = email;
        this.gpa = gpa;
        this.enrollmentDate = enrollmentDate;
    }
    
    // ========== GETTERS & SETTERS ==========
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Setter với validation - Đóng gói (Encapsulation)
     * Ngăn chặn dữ liệu không hợp lệ được lưu vào đối tượng
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("❌ Tên không được để trống");
        }
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("❌ Email không hợp lệ");
        }
        this.email = email;
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("❌ GPA phải nằm trong khoảng 0.0 đến 4.0");
        }
        this.gpa = gpa;
    }
    
    public String getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gpa=" + gpa +
                ", enrollmentDate='" + enrollmentDate + '\'' +
                '}';
    }
}


// ============================================================================
// PHẦN 2: DAO INTERFACE - StudentDAO.java
// ============================================================================

import java.util.List;

/**
 * Interface StudentDAO định nghĩa hợp đồng cho tất cả triển khai DAO
 * Áp dụng: Trừu tượng hóa (Abstraction), Nguyên tắc SOLID - DIP
 * 
 * Lợi ích:
 * - Ẩn chi tiết triển khai (MySQL, MongoDB, File, v.v.)
 * - Dễ dàng thay đổi database mà không thay đổi code khác
 * - Dễ dàng testing bằng MockStudentDAO
 */
public interface StudentDAO {
    
    /**
     * Lưu một sinh viên mới vào database
     */
    void save(Student student) throws Exception;
    
    /**
     * Tìm sinh viên theo ID
     */
    Student findById(int id) throws Exception;
    
    /**
     * Lấy danh sách tất cả sinh viên
     */
    List<Student> findAll() throws Exception;
    
    /**
     * Tìm sinh viên theo tên (hỗ trợ tìm kiếm từng phần)
     */
    List<Student> findByName(String name) throws Exception;
    
    /**
     * Cập nhật thông tin sinh viên
     */
    void update(Student student) throws Exception;
    
    /**
     * Xóa sinh viên theo ID
     */
    void delete(int id) throws Exception;
    
    /**
     * Business logic: Tìm các sinh viên có điểm GPA cao
     */
    List<Student> findStudentsWithHighGPA(double minGPA) throws Exception;
}


// ============================================================================
// PHẦN 3: DAO MYSQL IMPLEMENTATION - MySQLStudentDAO.java
// ============================================================================

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai DAO cho MySQL Database
 * Áp dụng: Kế thừa (Inheritance - implements), Đa hình (Polymorphism)
 * 
 * Nguyên tắc SOLID:
 * - Single Responsibility: Chỉ chịu trách nhiệm truy cập MySQL
 * - Open/Closed: Mở cho mở rộng (có thể thêm triển khai khác)
 */
public class MySQLStudentDAO implements StudentDAO {
    
    private String url;
    private String user;
    private String password;
    
    public MySQLStudentDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    
    /**
     * Thiết lập kết nối với database
     */
    private Connection getConnection() throws SQLException {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver không found: " + e.getMessage());
        }
        return DriverManager.getConnection(url, user, password);
    }
    
    @Override
    public void save(Student student) throws Exception {
        String sql = "INSERT INTO students (name, email, gpa, enrollment_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setDouble(3, student.getGpa());
            stmt.setString(4, student.getEnrollmentDate());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Lưu sinh viên thành công: " + student.getName());
            }
        }
    }
    
    @Override
    public Student findById(int id) throws Exception {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Student> findAll() throws Exception {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }
    
    @Override
    public List<Student> findByName(String name) throws Exception {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }
    
    @Override
    public void update(Student student) throws Exception {
        String sql = "UPDATE students SET name = ?, email = ?, gpa = ?, enrollment_date = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setDouble(3, student.getGpa());
            stmt.setString(4, student.getEnrollmentDate());
            stmt.setInt(5, student.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Cập nhật sinh viên thành công: " + student.getName());
            }
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Xóa sinh viên thành công (ID: " + id + ")");
            }
        }
    }
    
    @Override
    public List<Student> findStudentsWithHighGPA(double minGPA) throws Exception {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE gpa >= ? ORDER BY gpa DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, minGPA);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }
    
    /**
     * Helper method: Chuyển đổi ResultSet thành Student object
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setGpa(rs.getDouble("gpa"));
        student.setEnrollmentDate(rs.getString("enrollment_date"));
        return student;
    }
}


// ============================================================================
// PHẦN 4: MOCK DAO (FOR TESTING) - MockStudentDAO.java
// ============================================================================

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Triển khai DAO cho Testing (không cần database thực)
 * Áp dụng: Đa hình (Polymorphism)
 * 
 * Lợi ích:
 * - Kiểm tra Service mà không cần kết nối database
 * - Test nhanh hơn
 * - Không phải cấu hình database test riêng
 */
public class MockStudentDAO implements StudentDAO {
    
    private List<Student> students = new ArrayList<>();
    private int nextId = 1;
    
    public MockStudentDAO() {
        // Khởi tạo với một số dữ liệu mẫu
        try {
            save(new Student("Test Student A", "a@test.com", 3.5, "2024-01-01"));
            save(new Student("Test Student B", "b@test.com", 3.9, "2024-01-01"));
            save(new Student("Test Student C", "c@test.com", 3.2, "2024-01-01"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void save(Student student) throws Exception {
        student.setId(nextId++);
        students.add(student);
        System.out.println("✅ [MOCK] Lưu sinh viên: " + student.getName());
    }
    
    @Override
    public Student findById(int id) throws Exception {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Student> findAll() throws Exception {
        return new ArrayList<>(students);
    }
    
    @Override
    public List<Student> findByName(String name) throws Exception {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public void update(Student student) throws Exception {
        students.stream()
                .filter(s -> s.getId() == student.getId())
                .findFirst()
                .ifPresent(s -> {
                    s.setName(student.getName());
                    s.setEmail(student.getEmail());
                    s.setGpa(student.getGpa());
                    s.setEnrollmentDate(student.getEnrollmentDate());
                });
        System.out.println("✅ [MOCK] Cập nhật sinh viên: " + student.getName());
    }
    
    @Override
    public void delete(int id) throws Exception {
        students.removeIf(s -> s.getId() == id);
        System.out.println("✅ [MOCK] Xóa sinh viên (ID: " + id + ")");
    }
    
    @Override
    public List<Student> findStudentsWithHighGPA(double minGPA) throws Exception {
        return students.stream()
                .filter(s -> s.getGpa() >= minGPA)
                .sorted((a, b) -> Double.compare(b.getGpa(), a.getGpa()))
                .collect(Collectors.toList());
    }
}


// ============================================================================
// PHẦN 5: SERVICE LAYER - StudentService.java
// ============================================================================

import java.util.List;

/**
 * Business Logic Layer
 * Áp dụng: Dependency Injection, Nguyên tắc SOLID - SRP, DIP
 * 
 * Trách nhiệm:
 * - Xử lý business logic
 * - Không trực tiếp tương tác với database
 * - Phụ thuộc vào DAO interface (không phải implementation)
 */
public class StudentService {
    
    private StudentDAO studentDAO;
    
    /**
     * Constructor nhận DAO qua Dependency Injection
     * Lợi ích: Có thể pass MySQLStudentDAO hoặc MockStudentDAO
     */
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }
    
    public void registerStudent(String name, String email, double gpa, String enrollmentDate) throws Exception {
        // Validation
        if (name == null || name.isEmpty()) {
            System.out.println("❌ Tên sinh viên không được để trống");
            return;
        }
        
        // Tạo object và lưu vào database
        Student student = new Student(name, email, gpa, enrollmentDate);
        studentDAO.save(student);
    }
    
    public void promoteHighPerformers(double minGPA) throws Exception {
        List<Student> topStudents = studentDAO.findStudentsWithHighGPA(minGPA);
        
        if (topStudents.isEmpty()) {
            System.out.println("📭 Không có sinh viên nào có GPA >= " + minGPA);
            return;
        }
        
        System.out.println("\n🌟 === Danh sách sinh viên xuất sắc (GPA >= " + minGPA + ") ===");
        topStudents.forEach(s -> {
            System.out.println("  - " + s.getName() + " (" + s.getEmail() + "): GPA " + s.getGpa());
        });
    }
    
    public void displayAllStudents() throws Exception {
        List<Student> students = studentDAO.findAll();
        
        if (students.isEmpty()) {
            System.out.println("📭 Danh sách sinh viên trống");
            return;
        }
        
        System.out.println("\n📚 === Danh sách tất cả sinh viên ===");
        students.forEach(System.out::println);
    }
    
    public void searchStudent(String name) throws Exception {
        List<Student> results = studentDAO.findByName(name);
        
        if (results.isEmpty()) {
            System.out.println("❌ Không tìm thấy sinh viên nào có tên chứa: " + name);
            return;
        }
        
        System.out.println("\n🔍 === Kết quả tìm kiếm cho '" + name + "' ===");
        results.forEach(System.out::println);
    }
    
    public void getStudentStats() throws Exception {
        List<Student> students = studentDAO.findAll();
        
        if (students.isEmpty()) {
            System.out.println("📊 Không có dữ liệu để thống kê");
            return;
        }
        
        double avgGPA = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
        
        double maxGPA = students.stream()
                .mapToDouble(Student::getGpa)
                .max()
                .orElse(0.0);
        
        double minGPA = students.stream()
                .mapToDouble(Student::getGpa)
                .min()
                .orElse(0.0);
        
        System.out.println("\n📊 === Thống kê sinh viên ===");
        System.out.println("  - Tổng số: " + students.size());
        System.out.println(String.format("  - GPA trung bình: %.2f", avgGPA));
        System.out.println(String.format("  - GPA cao nhất: %.2f", maxGPA));
        System.out.println(String.format("  - GPA thấp nhất: %.2f", minGPA));
    }
}


// ============================================================================
// PHẦN 6: MAIN APPLICATION - Main.java
// ============================================================================

/**
 * Entry point của ứng dụng
 * Áp dụng: Dependency Injection
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            System.out.println("🚀 === DEMO DAO PATTERN ===\n");
            
            // ===== OPTION 1: Sử dụng MySQL (Production) =====
            System.out.println("📌 Sử dụng MySQL Database:");
            StudentDAO mysqlDAO = new MySQLStudentDAO(
                "jdbc:mysql://localhost:3306/student_management",
                "root",
                "your_password"  // Thay bằng password của bạn
            );
            StudentService serviceMySQL = new StudentService(mysqlDAO);
            
            // Demo các chức năng
            serviceMySQL.displayAllStudents();
            serviceMySQL.searchStudent("Nguyễn");
            serviceMySQL.promoteHighPerformers(3.5);
            
            // ===== OPTION 2: Sử dụng Mock DAO (Testing) =====
            System.out.println("\n\n📌 Sử dụng Mock DAO (Testing):");
            StudentDAO mockDAO = new MockStudentDAO();
            StudentService serviceMock = new StudentService(mockDAO);
            
            serviceMock.displayAllStudents();
            serviceMock.registerStudent("Sinh Viên Mới", "new@test.com", 3.7, "2024-02-01");
            serviceMock.displayAllStudents();
            serviceMock.getStudentStats();
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


// ============================================================================
// PHẦN 7: UNIT TESTS - StudentServiceTest.java
// ============================================================================

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Tests cho StudentService
 * Áp dụng: Testing, MockDAO
 */
public class StudentServiceTest {
    
    private StudentDAO mockDAO;
    private StudentService service;
    
    @Before
    public void setUp() {
        // Khởi tạo với MockDAO (không cần database thực)
        mockDAO = new MockStudentDAO();
        service = new StudentService(mockDAO);
    }
    
    @Test
    public void testRegisterStudent() throws Exception {
        // Arrange
        int initialCount = mockDAO.findAll().size();
        
        // Act
        service.registerStudent("Test Student", "test@test.com", 3.5, "2024-01-01");
        
        // Assert
        int finalCount = mockDAO.findAll().size();
        assertEquals("Số lượng sinh viên phải tăng lên 1", initialCount + 1, finalCount);
    }
    
    @Test
    public void testFindStudentsWithHighGPA() throws Exception {
        // Act
        java.util.List<Student> topStudents = mockDAO.findStudentsWithHighGPA(3.5);
        
        // Assert
        assertTrue("Phải tìm thấy ít nhất 1 sinh viên", topStudents.size() > 0);
        topStudents.forEach(s -> {
            assertTrue("Tất cả sinh viên phải có GPA >= 3.5", s.getGpa() >= 3.5);
        });
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGPA() throws Exception {
        Student student = new Student();
        student.setGpa(5.0); // GPA > 4.0 sẽ throw exception
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail() throws Exception {
        Student student = new Student();
        student.setEmail("invalid-email");
    }
    
    @Test
    public void testSearchByName() throws Exception {
        // Act
        java.util.List<Student> results = mockDAO.findByName("Test");
        
        // Assert
        assertTrue("Phải tìm thấy các sinh viên", results.size() > 0);
    }
}


// ============================================================================
// GIẢI THÍCH KIẾN TRÚC DAO PATTERN
// ============================================================================

/*
 * KIẾN TRÚC:
 * 
 *   ┌─────────────────────────────┐
 *   │        Main Application     │
 *   └──────────────┬──────────────┘
 *                  │ tạo ra
 *                  ▼
 *   ┌─────────────────────────────┐
 *   │    StudentService           │  (Business Logic)
 *   │  (Sử dụng StudentDAO)       │
 *   └──────────────┬──────────────┘
 *                  │ depends on
 *                  ▼
 *   ┌─────────────────────────────┐
 *   │    StudentDAO (Interface)   │  (Abstraction)
 *   │  (CRUD operations contract) │
 *   └──┬──────────────────────┬───┘
 *      │                      │
 *      ▼ implements           ▼ implements
 * ┌──────────────┐      ┌──────────────┐
 * │MySQLStudentDAO│    │MockStudentDAO│
 * │(Production)  │     │(Testing)     │
 * └──────────────┘     └──────────────┘
 *        │                    │
 *        ▼ uses               ▼ uses
 *   MySQL DB                 In-memory
 *
 * LỢI ÍCH:
 * ✅ Tách biệt mối quan tâm (Separation of Concerns)
 * ✅ Dễ thay đổi database
 * ✅ Dễ testing (MockDAO)
 * ✅ Tuân theo SOLID principles
 * ✅ Code dễ bảo trì và mở rộng
 */

// ============================================================================
// CÁC NGUYÊN TẮC SOLID ĐƯỢC ÁP DỤNG
// ============================================================================

/*
 * S - Single Responsibility Principle (SRP)
 *   ✅ Student: Chỉ đại diện dữ liệu
 *   ✅ MySQLStudentDAO: Chỉ truy cập MySQL
 *   ✅ StudentService: Chỉ xử lý business logic
 *
 * O - Open/Closed Principle (OCP)
 *   ✅ Mở cho mở rộng: Có thể thêm PostgreSQLStudentDAO
 *   ✅ Đóng cho sửa đổi: Không cần thay đổi StudentService
 *
 * L - Liskov Substitution Principle (LSP)
 *   ✅ StudentService hoạt động với bất kỳ StudentDAO nào
 *   ✅ MockStudentDAO có thể thay MySQLStudentDAO mà không cần thay code
 *
 * I - Interface Segregation Principle (ISP)
 *   ✅ StudentDAO interface chỉ chứa các phương thức liên quan Student
 *   ✅ Không bắt buộc implement những phương thức không cần
 *
 * D - Dependency Inversion Principle (DIP)
 *   ✅ StudentService phụ thuộc vào StudentDAO (abstraction)
 *   ✅ Không phụ thuộc vào MySQLStudentDAO (concretion)
 *   ✅ Dependency Injection qua constructor
 */

// ============================================================================
// KẾT THÚC EXAMPLE CODE
// ============================================================================
