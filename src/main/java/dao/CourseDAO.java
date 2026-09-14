package dao;

import entity.Course;
import java.util.List;

/**
 * Interface CourseDAO định nghĩa hợp đồng cho tất cả triển khai DAO của môn học
 * Áp dụng: Trừu tượng hóa (Abstraction), Nguyên tắc SOLID - DIP
 */
public interface CourseDAO {

    /**
     * Lưu một môn học mới vào cơ sở dữ liệu
     */
    void save(Course course) throws Exception;

    /**
     * Tìm môn học theo ID
     */
    Course findById(int id) throws Exception;

    /**
     * Lấy danh sách tất cả các môn học
     */
    List<Course> findAll() throws Exception;

    /**
     * Tìm môn học theo tên (hỗ trợ tìm kiếm gần đúng LIKE)
     */
    List<Course> findByName(String name) throws Exception;

    /**
     * Cập nhật thông tin môn học
     */
    void update(Course course) throws Exception;

    /**
     * Xóa môn học theo ID
     */
    void delete(int id) throws Exception;

    /**
     * Lọc các môn học có số tín chỉ tối thiểu
     */
    List<Course> findByCredits(int minCredits) throws Exception;
}
