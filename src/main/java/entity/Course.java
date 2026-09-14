package entity;

/**
 * Class Course đại diện cho một môn học / khóa học
 * Áp dụng: Đóng gói (Encapsulation), Kiểm tra tính toàn vẹn dữ liệu (Validation)
 */
public class Course {
    private int id;
    private String name;
    private int credits;
    private String instructor;
    private int departmentId;

    // Constructor mặc định
    public Course() {}

    // Constructor không có id và departmentId
    public Course(String name, int credits, String instructor) {
        setName(name);
        setCredits(credits);
        setInstructor(instructor);
        this.departmentId = 1; // Default department (CNTT)
    }

    // Constructor đầy đủ
    public Course(String name, int credits, String instructor, int departmentId) {
        setName(name);
        setCredits(credits);
        setInstructor(instructor);
        this.departmentId = departmentId;
    }

    // Constructor với id (phục vụ map từ DB)
    public Course(int id, String name, int credits, String instructor, int departmentId) {
        this.id = id;
        setName(name);
        setCredits(credits);
        setInstructor(instructor);
        this.departmentId = departmentId;
    }

    // ========== GETTERS & SETTERS ==========

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Tên khóa học không được để trống");
        }
        this.name = name.trim();
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if (credits <= 0 || credits > 10) {
            throw new IllegalArgumentException("❌ Số tín chỉ phải từ 1 đến 10");
        }
        this.credits = credits;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        if (instructor == null || instructor.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Tên giảng viên không được để trống");
        }
        this.instructor = instructor.trim();
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", instructor='" + instructor + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}
