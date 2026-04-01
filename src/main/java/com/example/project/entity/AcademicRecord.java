package com.example.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "academic_record")
public class AcademicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "school_year", length = 20)
    private String schoolYear;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "average_score")
    private Float averageScore;

    @Column(name = "conduct", length = 50)
    private String conduct;

    @Column(name = "teacher_comment", length = 500)
    private String teacherComment;

    @Column(name = "parent_comment", length = 500)
    private String parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public AcademicRecord() {
    }

    public AcademicRecord(String schoolYear, String semester, Float averageScore,
            String conduct, String teacherComment, String parentComment, Student student) {
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.averageScore = averageScore;
        this.conduct = conduct;
        this.teacherComment = teacherComment;
        this.parentComment = parentComment;
        this.student = student;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Float getAverageScore() { return averageScore; }
    public void setAverageScore(Float averageScore) { this.averageScore = averageScore; }

    public String getConduct() { return conduct; }
    public void setConduct(String conduct) { this.conduct = conduct; }

    public String getTeacherComment() { return teacherComment; }
    public void setTeacherComment(String teacherComment) { this.teacherComment = teacherComment; }

    public String getParentComment() { return parentComment; }
    public void setParentComment(String parentComment) { this.parentComment = parentComment; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
