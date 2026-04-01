package com.example.project.dto.response;

import com.example.project.entity.Parent;
import java.util.List;
import java.util.stream.Collectors;

public class ParentDTO {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String relationship;
    private List<StudentInfo> students;

    public ParentDTO() {
    }

    public ParentDTO(Parent parent) {
        this.id = parent.getId();
        this.fullName = parent.getFullName();
        this.phone = parent.getPhone();
        this.email = parent.getEmail();
        this.address = parent.getAddress();
        this.relationship = parent.getRelationship();
        if (parent.getStudents() != null) {
            this.students = parent.getStudents().stream()
                    .map(StudentInfo::new)
                    .collect(Collectors.toList());
        }
    }

    public static class StudentInfo {
        private Long id;
        private String fullName;
        private String className;
        private String schoolName;

        public StudentInfo(com.example.project.entity.Student student) {
            this.id = student.getId();
            this.fullName = student.getFullName();
            if (student.getClassEntity() != null) {
                this.className = student.getClassEntity().getName();
                if (student.getClassEntity().getSchool() != null) {
                    this.schoolName = student.getClassEntity().getSchool().getName();
                }
            }
        }

        public Long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getClassName() { return className; }
        public String getSchoolName() { return schoolName; }
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getRelationship() { return relationship; }
    public List<StudentInfo> getStudents() { return students; }
}
