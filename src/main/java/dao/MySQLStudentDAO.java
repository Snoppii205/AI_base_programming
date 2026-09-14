package dao;

import entity.Student;
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
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setDouble(3, student.getGpa());
            stmt.setString(4, student.getEnrollmentDate());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setId(rs.getInt(1));
                    }
                }
                System.out.println("✅ Lưu sinh viên thành công: " + student.getName() + " (ID: " + student.getId() + ")");
            }
        }
    }
    
    @Override
    public Student findById(int id) throws Exception {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
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
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
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
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
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
