package entity;

/**
 * Class Student đại diện cho một sinh viên
 * Áp dụng: Đóng gói (Encapsulation), Validation
 */
public class Student {
    private int id;
    private String name;
    private String email;
    private double gpa;
    private String enrollmentDate;
    
    // Constructor mặc định
    public Student() {}
    
    // Constructor với tham số
    public Student(String name, String email, double gpa, String enrollmentDate) {
        setName(name);
        setEmail(email);
        setGpa(gpa);
        this.enrollmentDate = enrollmentDate;
    }
    
    // Constructor đầy đủ
    public Student(int id, String name, String email, double gpa, String enrollmentDate) {
        this.id = id;
        setName(name);
        setEmail(email);
        setGpa(gpa);
        this.enrollmentDate = enrollmentDate;
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
    
    /**
     * Setter với validation - Đóng gói (Encapsulation)
     * Ngăn chặn dữ liệu không hợp lệ được lưu vào đối tượng
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Tên không được để trống");
        }
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("❌ Email không hợp lệ");
        }
        this.email = email;
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("❌ GPA phải nằm trong khoảng 0.0 đến 4.0");
        }
        this.gpa = gpa;
    }
    
    public String getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gpa=" + gpa +
                ", enrollmentDate='" + enrollmentDate + '\'' +
                '}';
    }
}
