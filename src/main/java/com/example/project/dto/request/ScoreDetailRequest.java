package com.example.project.dto.request;

import java.time.LocalDate;

public class ScoreDetailRequest {

    private Long studentId;
    private Long subjectId;
    private String schoolYear;
    private String semester;
    private LocalDate examDate;
    private String examType;
    private Float score;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    public Float getScore() { return score; }
    public void setScore(Float score) { this.score = score; }
}
