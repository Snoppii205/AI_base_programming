package dao;

import entity.Student;
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
