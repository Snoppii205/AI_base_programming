package service;

import entity.Course;
import dao.CourseDAO;
import java.util.List;

/**
 * Service Layer cho Course (Môn học)
 * Áp dụng: Dependency Injection, Nguyên tắc SOLID (SRP, DIP)
 */
public class CourseService {

    private CourseDAO courseDAO;

    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public void registerCourse(String name, int credits, String instructor) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("❌ Tên khóa học không được để trống");
            return;
        }

        Course course = new Course(name, credits, instructor);
        courseDAO.save(course);
        System.out.println("✅ Đã đăng ký khóa học: " + name + " (" + credits + " tín chỉ)");
    }

    public void displayAllCourses() throws Exception {
        List<Course> courses = courseDAO.findAll();

        if (courses.isEmpty()) {
            System.out.println("📭 Danh sách môn học trống");
            return;
        }

        System.out.println("\n📖 === Danh sách tất cả môn học ===");
        courses.forEach(System.out::println);
    }

    public void searchCourse(String name) throws Exception {
        List<Course> results = courseDAO.findByName(name);

        if (results.isEmpty()) {
            System.out.println("❌ Không tìm thấy môn học nào có tên chứa: " + name);
            return;
        }

        System.out.println("\n🔍 === Kết quả tìm kiếm môn học cho '" + name + "' ===");
        results.forEach(System.out::println);
    }

    public void filterCoursesByCredits(int minCredits) throws Exception {
        List<Course> results = courseDAO.findByCredits(minCredits);

        if (results.isEmpty()) {
            System.out.println("📭 Không có môn học nào có tín chỉ >= " + minCredits);
            return;
        }

        System.out.println("\n🎯 === Môn học có tín chỉ >= " + minCredits + " ===");
        results.forEach(c -> {
            System.out.println("  - " + c.getName() + " (" + c.getCredits() + " tín chỉ) - GV: " + c.getInstructor());
        });
    }

    public void getCourseStats() throws Exception {
        List<Course> courses = courseDAO.findAll();

        if (courses.isEmpty()) {
            System.out.println("📊 Không có dữ liệu môn học để thống kê");
            return;
        }

        double avgCredits = courses.stream()
                .mapToInt(Course::getCredits)
                .average()
                .orElse(0.0);

        int maxCredits = courses.stream()
                .mapToInt(Course::getCredits)
                .max()
                .orElse(0);

        int minCredits = courses.stream()
                .mapToInt(Course::getCredits)
                .min()
                .orElse(0);

        System.out.println("\n📊 === Thống kê môn học ===");
        System.out.println("  - Tổng số môn học: " + courses.size());
        System.out.println(String.format("  - Tín chỉ trung bình: %.2f", avgCredits));
        System.out.println("  - Tín chỉ cao nhất: " + maxCredits);
        System.out.println("  - Tín chỉ thấp nhất: " + minCredits);
    }
}
