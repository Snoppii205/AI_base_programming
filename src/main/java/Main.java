import entity.Student;
import entity.Course;
import dao.StudentDAO;
import dao.MySQLStudentDAO;
import dao.MockStudentDAO;
import dao.CourseDAO;
import dao.MySQLCourseDAO;
import dao.MockCourseDAO;
import service.StudentService;
import service.CourseService;

/**
 * Entry point của ứng dụng
 * Áp dụng: Dependency Injection, DAO Pattern, Nguyên tắc SOLID
 */
public class Main {
    
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String dbUser = "root";
        String dbPass = "";

        try {
            System.out.println("==================================================");
            System.out.println("🚀 === DEMO DAO PATTERN & SOLID PRINCIPLES ===");
            System.out.println("==================================================\n");
            
            // ===== OPTION 1: Sử dụng MySQL (Production) =====
            System.out.println("--------------------------------------------------");
            System.out.println("📌 OPTION 1: SỬ DỤNG MYSQL DATABASE (Thực tế)");
            System.out.println("--------------------------------------------------");
            try {
                // --- PHẦN 1.1: STUDENT SERVICE (MySQL) ---
                StudentDAO mysqlStudentDAO = new MySQLStudentDAO(jdbcUrl, dbUser, dbPass);
                StudentService studentServiceMySQL = new StudentService(mysqlStudentDAO);
                
                System.out.println("\n[1.1] Hiển thị danh sách sinh viên từ MySQL:");
                studentServiceMySQL.displayAllStudents();
                
                System.out.println("\n[1.2] Tìm kiếm sinh viên theo tên ('Nguyễn'):");
                studentServiceMySQL.searchStudent("Nguyễn");
                
                System.out.println("\n[1.3] Lọc sinh viên xuất sắc (GPA >= 3.5):");
                studentServiceMySQL.promoteHighPerformers(3.5);
                
                System.out.println("\n[1.4] Thêm sinh viên mới vào MySQL:");
                for (Student s : mysqlStudentDAO.findByName("Trương Gia Bảo")) {
                    mysqlStudentDAO.delete(s.getId());
                }
                studentServiceMySQL.registerStudent("Trương Gia Bảo", "bao@university.edu", 3.85, "2024-09-01");
                
                System.out.println("\n[1.5] Thống kê điểm sinh viên từ MySQL:");
                studentServiceMySQL.getStudentStats();

                // --- PHẦN 1.2: COURSE SERVICE (MySQL) ---
                System.out.println("\n--------------------------------------------------");
                System.out.println("📚 Quản lý Môn học (Courses) với MySQL DAO:");
                System.out.println("--------------------------------------------------");
                CourseDAO mysqlCourseDAO = new MySQLCourseDAO(jdbcUrl, dbUser, dbPass);
                CourseService courseServiceMySQL = new CourseService(mysqlCourseDAO);

                System.out.println("\n[1.6] Hiển thị danh sách môn học từ MySQL:");
                courseServiceMySQL.displayAllCourses();

                System.out.println("\n[1.7] Lọc các môn học có số tín chỉ >= 4:");
                courseServiceMySQL.filterCoursesByCredits(4);

                System.out.println("\n[1.8] Thêm môn học mới vào MySQL:");
                for (Course c : mysqlCourseDAO.findByName("Cloud Computing")) {
                    mysqlCourseDAO.delete(c.getId());
                }
                courseServiceMySQL.registerCourse("Cloud Computing", 3, "Dr. Vu");

                System.out.println("\n[1.9] Thống kê môn học từ MySQL:");
                courseServiceMySQL.getCourseStats();
                
            } catch (Exception e) {
                System.out.println("⚠️ Không thể kết nối hoặc truy vấn MySQL: " + e.getMessage());
                System.out.println("💡 Gợi ý: Đảm bảo MySQL (XAMPP) đang chạy và đã import file database.sql.");
            }
            
            // ===== OPTION 2: Sử dụng Mock DAO (Testing / In-Memory) =====
            System.out.println("\n==================================================");
            System.out.println("📌 OPTION 2: SỬ DỤNG MOCK DAO (In-Memory / Testing)");
            System.out.println("--------------------------------------------------");
            System.out.println("👉 Không cần Database thật, chạy hoàn toàn trên RAM!");
            
            // Student Mock
            StudentDAO mockStudentDAO = new MockStudentDAO();
            StudentService studentServiceMock = new StudentService(mockStudentDAO);
            
            System.out.println("\n--- [Student Mock] Danh sách ban đầu: ---");
            studentServiceMock.displayAllStudents();
            
            System.out.println("\n--- [Student Mock] Thêm sinh viên mới: ---");
            studentServiceMock.registerStudent("Vũ Hải Đăng", "dang@mock.edu", 3.75, "2024-02-01");
            studentServiceMock.getStudentStats();
            
            // Course Mock
            CourseDAO mockCourseDAO = new MockCourseDAO();
            CourseService courseServiceMock = new CourseService(mockCourseDAO);

            System.out.println("\n--- [Course Mock] Danh sách môn học ban đầu: ---");
            courseServiceMock.displayAllCourses();

            System.out.println("\n--- [Course Mock] Thêm môn học mới: ---");
            courseServiceMock.registerCourse("Machine Learning", 4, "Dr. Pham");

            System.out.println("\n--- [Course Mock] Lọc môn học có tín chỉ >= 4: ---");
            courseServiceMock.filterCoursesByCredits(4);

            System.out.println("\n--- [Course Mock] Thống kê môn học: ---");
            courseServiceMock.getCourseStats();

            System.out.println("\n==================================================");
            System.out.println("✅ CHƯƠNG TRÌNH ĐÃ CHẠY THÀNH CÔNG TẤT CẢ CÁC PHẦN!");
            System.out.println("==================================================");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Dọn dẹp background thread của MySQL JDBC driver khi chạy trong maven exec
            try {
                com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            } catch (Throwable ignored) {}
        }
    }
}
