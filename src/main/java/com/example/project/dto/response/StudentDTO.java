package com.example.project.dto.response;

import com.example.project.entity.Parent;
import com.example.project.entity.Student;
import java.time.LocalDate;

public class StudentDTO {

    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private Boolean gender;
    private String address;
    private String className;
    private String schoolName;
    private ParentInfo parent;

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.fullName = student.getFullName();
        this.dateOfBirth = student.getDateOfBirth();
        this.gender = student.getGender();
        this.address = student.getAddress();
        if (student.getClassEntity() != null) {
            this.className = student.getClassEntity().getName();
            if (student.getClassEntity().getSchool() != null) {
                this.schoolName = student.getClassEntity().getSchool().getName();
            }
        }
        if (student.getParent() != null) {
            this.parent = new ParentInfo(student.getParent());
        }
    }

    public static class ParentInfo {
        private Long id;
        private String fullName;
        private String phone;
        private String relationship;

        public ParentInfo(Parent parent) {
            this.id = parent.getId();
            this.fullName = parent.getFullName();
            this.phone = parent.getPhone();
            this.relationship = parent.getRelationship();
        }

        public Long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getPhone() { return phone; }
        public String getRelationship() { return relationship; }
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Boolean getGender() { return gender; }
    public String getAddress() { return address; }
    public String getClassName() { return className; }
    public String getSchoolName() { return schoolName; }
    public ParentInfo getParent() { return parent; }
}
