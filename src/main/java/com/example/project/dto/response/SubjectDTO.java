package com.example.project.dto.response;

import com.example.project.entity.Subject;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectDTO {

    private Long id;
    private String name;
    private String schoolName;
    private List<TeacherInfo> teachers;

    public SubjectDTO(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        if (subject.getSchool() != null) {
            this.schoolName = subject.getSchool().getName();
        }
        if (subject.getTeachers() != null) {
            this.teachers = subject.getTeachers().stream()
                    .map(e -> new TeacherInfo(e.getId(), e.getFullName(), e.getPhone()))
                    .collect(Collectors.toList());
        }
    }

    public static class TeacherInfo {
        private Long id;
        private String fullName;
        private String phone;

        public TeacherInfo(Long id, String fullName, String phone) {
            this.id = id;
            this.fullName = fullName;
            this.phone = phone;
        }

        public Long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getPhone() { return phone; }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSchoolName() { return schoolName; }
    public List<TeacherInfo> getTeachers() { return teachers; }
}
