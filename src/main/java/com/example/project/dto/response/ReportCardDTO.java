package com.example.project.dto.response;

import com.example.project.entity.ReportCard;

public class ReportCardDTO {

    private Long id;
    private String schoolYear;
    private String semester;
    private Float score;
    private StudentInfo student;
    private SubjectInfo subject;

    public ReportCardDTO(ReportCard reportCard) {
        this.id = reportCard.getId();
        this.schoolYear = reportCard.getSchoolYear();
        this.semester = reportCard.getSemester();
        this.score = reportCard.getScore();
        if (reportCard.getStudent() != null) {
            this.student = new StudentInfo(
                reportCard.getStudent().getId(),
                reportCard.getStudent().getFullName()
            );
        }
        if (reportCard.getSubject() != null) {
            this.subject = new SubjectInfo(
                reportCard.getSubject().getId(),
                reportCard.getSubject().getName()
            );
        }
    }

    public static class StudentInfo {
        private Long id;
        private String fullName;

        public StudentInfo(Long id, String fullName) {
            this.id = id;
            this.fullName = fullName;
        }

        public Long getId() { return id; }
        public String getFullName() { return fullName; }
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

    public Long getId() { return id; }
    public String getSchoolYear() { return schoolYear; }
    public String getSemester() { return semester; }
    public Float getScore() { return score; }
    public StudentInfo getStudent() { return student; }
    public SubjectInfo getSubject() { return subject; }
}
