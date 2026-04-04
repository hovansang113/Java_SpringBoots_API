package com.example.project.dto.request;

public class ReportCardRequest {

    private Long studentId;
    private Long subjectId;
    private String schoolYear;
    private String semester;
    private Float score;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Float getScore() { return score; }
    public void setScore(Float score) { this.score = score; }
}
