package com.example.project.dto.response;

import com.example.project.entity.Employee;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDTO {

    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private Boolean gender;
    private String phone;
    private String email;
    private String position;
    private SchoolInfo school;
    private SubjectInfo subject;
    private List<ClassInfo> homeroomClasses;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.fullName = employee.getFullName();
        this.dateOfBirth = employee.getDateOfBirth();
        this.gender = employee.getGender();
        this.phone = employee.getPhone();
        this.email = employee.getEmail();
        this.position = employee.getPosition();
        if (employee.getSchool() != null) {
            this.school = new SchoolInfo(employee.getSchool().getId(),
                    employee.getSchool().getName(), employee.getSchool().getLevel());
        }
        if (employee.getSubject() != null) {
            this.subject = new SubjectInfo(employee.getSubject().getId(),
                    employee.getSubject().getName());
        }
        if (employee.getHomeroomClasses() != null) {
            this.homeroomClasses = employee.getHomeroomClasses().stream()
                    .map(c -> new ClassInfo(c.getId(), c.getName(), c.getGrade(), c.getSchoolYear()))
                    .collect(Collectors.toList());
        }
    }

    public static class SchoolInfo {
        private Long id;
        private String name;
        private String level;

        public SchoolInfo(Long id, String name, String level) {
            this.id = id;
            this.name = name;
            this.level = level;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getLevel() { return level; }
    }

    public static class SubjectInfo {
        private Long id;
        private String name;

        public SubjectInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }

    public static class ClassInfo {
        private Long id;
        private String name;
        private Integer grade;
        private String schoolYear;

        public ClassInfo(Long id, String name, Integer grade, String schoolYear) {
            this.id = id;
            this.name = name;
            this.grade = grade;
            this.schoolYear = schoolYear;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public Integer getGrade() { return grade; }
        public String getSchoolYear() { return schoolYear; }
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Boolean getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPosition() { return position; }
    public SchoolInfo getSchool() { return school; }
    public SubjectInfo getSubject() { return subject; }
    public List<ClassInfo> getHomeroomClasses() { return homeroomClasses; }
}
