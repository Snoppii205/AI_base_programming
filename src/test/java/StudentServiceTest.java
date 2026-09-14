import entity.Student;
import dao.StudentDAO;
import dao.MockStudentDAO;
import service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests cho StudentService
 * Áp dụng: JUnit 5, MockDAO (Không phụ thuộc vào Database thật)
 */
public class StudentServiceTest {
    
    private StudentDAO mockDAO;
    private StudentService service;
    
    @BeforeEach
    public void setUp() {
        // Khởi tạo với MockDAO (chạy trên RAM, cực nhanh và độc lập)
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
        assertEquals(initialCount + 1, finalCount, "Số lượng sinh viên phải tăng lên 1");
    }
    
    @Test
    public void testFindStudentsWithHighGPA() throws Exception {
        // Act
        java.util.List<Student> topStudents = mockDAO.findStudentsWithHighGPA(3.5);
        
        // Assert
        assertTrue(topStudents.size() > 0, "Phải tìm thấy ít nhất 1 sinh viên");
        for (Student s : topStudents) {
            assertTrue(s.getGpa() >= 3.5, "Tất cả sinh viên phải có GPA >= 3.5");
        }
    }
    
    @Test
    public void testInvalidGPA() {
        Student student = new Student();
        // GPA > 4.0 sẽ ném ngoại lệ IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(5.0));
    }
    
    @Test
    public void testNegativeGPA() {
        Student student = new Student();
        // GPA < 0.0 sẽ ném ngoại lệ IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(-1.0));
    }
    
    @Test
    public void testInvalidEmail() {
        Student student = new Student();
        // Email không chứa '@' sẽ ném ngoại lệ
        assertThrows(IllegalArgumentException.class, () -> student.setEmail("invalid-email"));
    }
    
    @Test
    public void testSearchByName() throws Exception {
        // Act
        java.util.List<Student> results = mockDAO.findByName("Test");
        
        // Assert
        assertNotNull(results, "Danh sách kết quả không được null");
        assertTrue(results.size() > 0, "Phải tìm thấy các sinh viên có chữ 'Test'");
    }
    
    @Test
    public void testDeleteStudent() throws Exception {
        // Act
        mockDAO.delete(1);
        Student deleted = mockDAO.findById(1);
        
        // Assert
        assertNull(deleted, "Sinh viên có ID 1 phải đã bị xóa");
    }

    @Test
    public void testFindByIdAndFindAll() throws Exception {
        // Act
        java.util.List<Student> all = mockDAO.findAll();
        Student student = mockDAO.findById(1);

        // Assert
        assertNotNull(all, "Danh sách không được null");
        assertTrue(all.size() >= 3, "Phải có ít nhất 3 sinh viên mẫu");
        assertNotNull(student, "Sinh viên ID 1 phải tồn tại");
        assertEquals("Test Student A", student.getName());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        // Arrange
        Student student = mockDAO.findById(1);
        assertNotNull(student);
        student.setName("Updated Student A");
        student.setGpa(3.95);

        // Act
        mockDAO.update(student);
        Student updated = mockDAO.findById(1);

        // Assert
        assertEquals("Updated Student A", updated.getName());
        assertEquals(3.95, updated.getGpa());
    }
}
