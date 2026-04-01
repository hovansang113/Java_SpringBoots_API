package com.example.project.entity;

import java.time.LocalDate;

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
@Table(name = "lesson_log")
public class LessonLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Sổ đầu bài thuộc về 1 lớp cụ thể
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    // Giáo viên ghi sổ đầu bài
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Employee teacher;

    // Môn học được giảng dạy trong buổi đó
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "teaching_date")
    private LocalDate teachingDate;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "period")
    private Integer period;

    public LessonLog() {
    }

    public LessonLog(ClassEntity classEntity, Employee teacher, Subject subject, LocalDate teachingDate, String content, Integer period) {
        this.classEntity = classEntity;
        this.teacher = teacher;
        this.subject = subject;
        this.teachingDate = teachingDate;
        this.content = content;
        this.period = period;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassEntity getClassEntity() {
        return classEntity;
    }

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
    }

    public Employee getTeacher() {
        return teacher;
    }

    public void setTeacher(Employee teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LocalDate getTeachingDate() {
        return teachingDate;
    }

    public void setTeachingDate(LocalDate teachingDate) {
        this.teachingDate = teachingDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
