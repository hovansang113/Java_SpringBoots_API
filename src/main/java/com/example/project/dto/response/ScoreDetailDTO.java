package com.example.project.dto.response;

import com.example.project.entity.ScoreDetail;
import java.time.LocalDate;

public class ScoreDetailDTO {

    private Long id;
    private String schoolYear;
    private String semester;
    private LocalDate examDate;
    private String examType;
    private Float score;
    private StudentInfo student;
    private SubjectInfo subject;

    public ScoreDetailDTO(ScoreDetail scoreDetail) {
        this.id = scoreDetail.getId();
        this.schoolYear = scoreDetail.getSchoolYear();
        this.semester = scoreDetail.getSemester();
        this.examDate = scoreDetail.getExamDate();
        this.examType = scoreDetail.getExamType();
        this.score = scoreDetail.getScore();
        if (scoreDetail.getStudent() != null) {
            this.student = new StudentInfo(
                scoreDetail.getStudent().getId(),
                scoreDetail.getStudent().getFullName()
            );
        }
        if (scoreDetail.getSubject() != null) {
            this.subject = new SubjectInfo(
                scoreDetail.getSubject().getId(),
                scoreDetail.getSubject().getName()
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
    public LocalDate getExamDate() { return examDate; }
    public String getExamType() { return examType; }
    public Float getScore() { return score; }
    public StudentInfo getStudent() { return student; }
    public SubjectInfo getSubject() { return subject; }
}
