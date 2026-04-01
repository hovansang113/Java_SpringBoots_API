package com.example.project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "class")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "school_year", length = 20)
    private String schoolYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id")
    private Employee homeroomTeacher;

    @OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LessonLog> lessonLogs;

    public ClassEntity() {
    }

    public ClassEntity(Long id, String name, Integer grade, String schoolYear, School school, Employee homeroomTeacher) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.schoolYear = schoolYear;
        this.school = school;
        this.homeroomTeacher = homeroomTeacher;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public Employee getHomeroomTeacher() { return homeroomTeacher; }
    public void setHomeroomTeacher(Employee homeroomTeacher) { this.homeroomTeacher = homeroomTeacher; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public List<LessonLog> getLessonLogs() { return lessonLogs; }
    public void setLessonLogs(List<LessonLog> lessonLogs) { this.lessonLogs = lessonLogs; }
}
