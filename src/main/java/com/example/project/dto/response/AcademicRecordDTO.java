package com.example.project.dto.response;

import com.example.project.entity.AcademicRecord;

public class AcademicRecordDTO {

    private Long id;
    private String schoolYear;
    private String semester;
    private Float averageScore;
    private String conduct;
    private String teacherComment;
    private String parentComment;
    private StudentInfo student;

    public AcademicRecordDTO(AcademicRecord academicRecord) {
        this.id = academicRecord.getId();
        this.schoolYear = academicRecord.getSchoolYear();
        this.semester = academicRecord.getSemester();
        this.averageScore = academicRecord.getAverageScore();
        this.conduct = academicRecord.getConduct();
        this.teacherComment = academicRecord.getTeacherComment();
        this.parentComment = academicRecord.getParentComment();
        if (academicRecord.getStudent() != null) {
            this.student = new StudentInfo(
                academicRecord.getStudent().getId(),
                academicRecord.getStudent().getFullName()
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

    public Long getId() { return id; }
    public String getSchoolYear() { return schoolYear; }
    public String getSemester() { return semester; }
    public Float getAverageScore() { return averageScore; }
    public String getConduct() { return conduct; }
    public String getTeacherComment() { return teacherComment; }
    public String getParentComment() { return parentComment; }
    public StudentInfo getStudent() { return student; }
}
