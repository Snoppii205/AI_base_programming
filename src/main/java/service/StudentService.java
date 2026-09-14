package service;

import entity.Student;
import dao.StudentDAO;
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
        if (name == null || name.trim().isEmpty()) {
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
