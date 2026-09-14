package dao;

import entity.Course;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Triển khai CourseDAO giả lập trên bộ nhớ (In-Memory)
 * Áp dụng: Đa hình (Polymorphism)
 * Mục đích: Phục vụ Unit Testing và chạy độc lập không phụ thuộc DB
 */
public class MockCourseDAO implements CourseDAO {

    private List<Course> courses = new ArrayList<>();
    private int nextId = 1;

    public MockCourseDAO() {
        initSampleData();
    }

    private void initSampleData() {
        try {
            save(new Course("Java Programming", 3, "Dr. Nguyen", 1));
            save(new Course("Database Design", 3, "Dr. Tran", 1));
            save(new Course("Advanced Algorithms", 4, "Dr. Do", 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Course course) throws Exception {
        course.setId(nextId++);
        courses.add(course);
        System.out.println("📦 [MOCK] Lưu môn học: " + course.getName() + " (ID: " + course.getId() + ")");
    }

    @Override
    public Course findById(int id) throws Exception {
        return courses.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Course> findAll() throws Exception {
        return new ArrayList<>(courses);
    }

    @Override
    public List<Course> findByName(String name) throws Exception {
        return courses.stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Course course) throws Exception {
        courses.stream()
                .filter(c -> c.getId() == course.getId())
                .findFirst()
                .ifPresent(c -> {
                    c.setName(course.getName());
                    c.setCredits(course.getCredits());
                    c.setInstructor(course.getInstructor());
                    c.setDepartmentId(course.getDepartmentId());
                });
    }

    @Override
    public void delete(int id) throws Exception {
        courses.removeIf(c -> c.getId() == id);
        System.out.println("🗑️ [MOCK] Xóa môn học (ID: " + id + ")");
    }

    @Override
    public List<Course> findByCredits(int minCredits) throws Exception {
        return courses.stream()
                .filter(c -> c.getCredits() >= minCredits)
                .sorted((a, b) -> Integer.compare(b.getCredits(), a.getCredits()))
                .collect(Collectors.toList());
    }
}
