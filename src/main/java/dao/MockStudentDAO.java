package dao;

import entity.Student;
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
        System.out.println("✅ [MOCK] Lưu sinh viên: " + student.getName() + " (ID: " + student.getId() + ")");
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
