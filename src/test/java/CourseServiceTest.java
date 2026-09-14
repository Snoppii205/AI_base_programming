import entity.Course;
import dao.CourseDAO;
import dao.MockCourseDAO;
import service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit Tests cho CourseService và CourseDAO
 * Áp dụng: JUnit 5, MockDAO (In-memory, chạy trên RAM nhanh và độc lập)
 */
public class CourseServiceTest {

    private CourseDAO mockDAO;
    private CourseService service;

    @BeforeEach
    public void setUp() {
        mockDAO = new MockCourseDAO();
        service = new CourseService(mockDAO);
    }

    @Test
    public void testRegisterCourse() throws Exception {
        // Arrange
        int initialCount = mockDAO.findAll().size();

        // Act
        service.registerCourse("Software Architecture", 4, "Dr. Lee");

        // Assert
        int finalCount = mockDAO.findAll().size();
        assertEquals(initialCount + 1, finalCount, "Số lượng khóa học phải tăng lên 1");
    }

    @Test
    public void testFindByIdAndFindAll() throws Exception {
        // Act
        List<Course> all = mockDAO.findAll();
        Course course1 = mockDAO.findById(1);

        // Assert
        assertNotNull(all, "Danh sách không được null");
        assertTrue(all.size() >= 3, "Phải có ít nhất 3 khóa học mẫu ban đầu");
        assertNotNull(course1, "Khóa học có ID 1 phải tồn tại");
        assertEquals("Java Programming", course1.getName());
    }

    @Test
    public void testUpdateCourse() throws Exception {
        // Arrange
        Course course = mockDAO.findById(1);
        assertNotNull(course);
        course.setName("Java Advanced Programming");
        course.setCredits(4);

        // Act
        mockDAO.update(course);
        Course updated = mockDAO.findById(1);

        // Assert
        assertEquals("Java Advanced Programming", updated.getName());
        assertEquals(4, updated.getCredits());
    }

    @Test
    public void testDeleteCourse() throws Exception {
        // Act
        mockDAO.delete(2);
        Course deleted = mockDAO.findById(2);

        // Assert
        assertNull(deleted, "Khóa học có ID 2 phải đã bị xóa");
    }

    @Test
    public void testInvalidCredits() {
        Course course = new Course();
        // Tín chỉ <= 0 hoặc > 10 phải ném IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> course.setCredits(0));
        assertThrows(IllegalArgumentException.class, () -> course.setCredits(-2));
        assertThrows(IllegalArgumentException.class, () -> course.setCredits(12));
    }

    @Test
    public void testEmptyName() {
        Course course = new Course();
        // Tên rỗng hoặc null phải ném IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> course.setName(""));
        assertThrows(IllegalArgumentException.class, () -> course.setName(null));
    }

    @Test
    public void testFilterCoursesByCredits() throws Exception {
        // Act (Khóa học mẫu có Advanced Algorithms: 4 tín chỉ)
        List<Course> results = mockDAO.findByCredits(4);

        // Assert
        assertNotNull(results);
        assertTrue(results.size() > 0, "Phải tìm thấy ít nhất 1 khóa học có tín chỉ >= 4");
        for (Course c : results) {
            assertTrue(c.getCredits() >= 4, "Tất cả khóa học tìm thấy phải có tín chỉ >= 4");
        }
    }
}
