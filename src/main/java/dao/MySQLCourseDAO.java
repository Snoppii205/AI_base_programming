package dao;

import entity.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai CourseDAO cho MySQL Database
 * Áp dụng: Kế thừa (implements), Đa hình (Polymorphism)
 * Nguyên tắc SOLID: SRP (chỉ truy cập dữ liệu Course trong MySQL), OCP (dễ mở rộng)
 */
public class MySQLCourseDAO implements CourseDAO {

    private String url;
    private String user;
    private String password;

    public MySQLCourseDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver không tìm thấy: " + e.getMessage());
        }
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void save(Course course) throws Exception {
        String sql = "INSERT INTO courses (name, credits, instructor, department_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getName());
            stmt.setInt(2, course.getCredits());
            stmt.setString(3, course.getInstructor());
            stmt.setInt(4, course.getDepartmentId() > 0 ? course.getDepartmentId() : 1);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        course.setId(rs.getInt(1));
                    }
                }
                System.out.println("✅ Lưu môn học thành công: " + course.getName() + " (ID: " + course.getId() + ")");
            }
        }
    }

    @Override
    public Course findById(int id) throws Exception {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Course> findAll() throws Exception {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }
        return courses;
    }

    @Override
    public List<Course> findByName(String name) throws Exception {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE name LIKE ? ORDER BY name";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        }
        return courses;
    }

    @Override
    public void update(Course course) throws Exception {
        String sql = "UPDATE courses SET name = ?, credits = ?, instructor = ?, department_id = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getName());
            stmt.setInt(2, course.getCredits());
            stmt.setString(3, course.getInstructor());
            stmt.setInt(4, course.getDepartmentId());
            stmt.setInt(5, course.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Cập nhật môn học thành công: " + course.getName());
            }
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Xóa môn học thành công (ID: " + id + ")");
            }
        }
    }

    @Override
    public List<Course> findByCredits(int minCredits) throws Exception {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE credits >= ? ORDER BY credits DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minCredits);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        }
        return courses;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setName(rs.getString("name"));
        course.setCredits(rs.getInt("credits"));
        course.setInstructor(rs.getString("instructor"));
        course.setDepartmentId(rs.getInt("department_id"));
        return course;
    }
}
