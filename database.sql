-- Script tạo Database cho bài tập DAO Pattern
-- Sử dụng MySQL

CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Bảng Students (Sinh viên)
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    gpa DECIMAL(3, 2) CHECK (gpa >= 0 AND gpa <= 4.0),
    enrollment_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_gpa (gpa)
);

-- Bảng Courses (Khóa học) - Cho phần nâng cao
CREATE TABLE IF NOT EXISTS courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    credits INT DEFAULT 3,
    instructor VARCHAR(100),
    department_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

-- Bảng Grades (Điểm số) - Cho phần nâng cao
CREATE TABLE IF NOT EXISTS grades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    grade CHAR(2),
    semester VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_course_semester (student_id, course_id, semester),
    INDEX idx_student (student_id),
    INDEX idx_course (course_id)
);

-- Bảng Departments (Bộ môn) - Cho phần nâng cao
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    building VARCHAR(50),
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

-- Insert dữ liệu mẫu vào bảng Students
INSERT INTO students (name, email, gpa, enrollment_date) VALUES
('Nguyễn Văn A', 'a@university.edu', 3.5, '2023-09-01'),
('Trần Thị B', 'b@university.edu', 3.8, '2023-09-01'),
('Phạm Công C', 'c@university.edu', 3.2, '2023-09-15'),
('Đỗ Thị D', 'd@university.edu', 3.9, '2023-10-01'),
('Hoàng Minh E', 'e@university.edu', 2.8, '2023-10-15');

-- Insert dữ liệu mẫu vào bảng Departments (cho phần nâng cao)
INSERT INTO departments (name, building, phone_number) VALUES
('Công Nghệ Thông Tin', 'Building A', '0123456789'),
('Kinh Tế', 'Building B', '0123456788'),
('Kỹ Thuật', 'Building C', '0123456787');

-- Insert dữ liệu mẫu vào bảng Courses (cho phần nâng cao)
INSERT INTO courses (name, credits, instructor, department_id) VALUES
('Java Programming', 3, 'Dr. Nguyen', 1),
('Database Design', 3, 'Dr. Tran', 1),
('Microeconomics', 3, 'Dr. Pham', 2),
('Advanced Algorithms', 4, 'Dr. Do', 1);

-- Insert dữ liệu mẫu vào bảng Grades (cho phần nâng cao)
INSERT INTO grades (student_id, course_id, grade, semester) VALUES
(1, 1, 'A', '2023-Fall'),
(1, 2, 'B+', '2023-Fall'),
(2, 1, 'A', '2023-Fall'),
(2, 3, 'A+', '2024-Spring'),
(3, 2, 'C+', '2023-Fall'),
(4, 1, 'A', '2023-Fall'),
(4, 4, 'A+', '2024-Spring');

-- Tạo view để hiển thị thông tin chi tiết sinh viên
CREATE OR REPLACE VIEW student_summary AS
SELECT 
    s.id,
    s.name,
    s.email,
    s.gpa,
    COUNT(g.id) as total_courses,
    AVG(CASE 
        WHEN g.grade = 'A+' THEN 4.0
        WHEN g.grade = 'A' THEN 3.7
        WHEN g.grade = 'B+' THEN 3.3
        WHEN g.grade = 'B' THEN 3.0
        WHEN g.grade = 'C+' THEN 2.3
        WHEN g.grade = 'C' THEN 2.0
        WHEN g.grade = 'D+' THEN 1.3
        WHEN g.grade = 'D' THEN 1.0
        ELSE 0
    END) as average_grade
FROM students s
LEFT JOIN grades g ON s.id = g.student_id
GROUP BY s.id, s.name, s.email, s.gpa;

-- Hiển thị dữ liệu
SELECT '=== STUDENTS ===' as '';
SELECT * FROM students;

SELECT '=== COURSES ===' as '';
SELECT * FROM courses;

SELECT '=== DEPARTMENTS ===' as '';
SELECT * FROM departments;

SELECT '=== GRADES ===' as '';
SELECT * FROM grades;

SELECT '=== STUDENT SUMMARY ===' as '';
SELECT * FROM student_summary;
