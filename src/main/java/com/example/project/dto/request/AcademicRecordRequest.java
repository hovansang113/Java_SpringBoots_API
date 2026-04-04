package com.example.project.dto.request;

public class AcademicRecordRequest {

    private Long studentId;
    private String schoolYear;
    private String semester;
    private String conduct;
    private String teacherComment;
    private String parentComment;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getConduct() { return conduct; }
    public void setConduct(String conduct) { this.conduct = conduct; }

    public String getTeacherComment() { return teacherComment; }
    public void setTeacherComment(String teacherComment) { this.teacherComment = teacherComment; }

    public String getParentComment() { return parentComment; }
    public void setParentComment(String parentComment) { this.parentComment = parentComment; }
}
