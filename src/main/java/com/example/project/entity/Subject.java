package com.example.project.entity;

import java.util.List;

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

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    // 1 môn học có thể có nhiều giáo viên dạy
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Employee> teachers;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReportCard> reportCards;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ScoreDetail> scoreDetails;

    public Subject() {
    }

    public Subject(Long id, String name, School school) {
        this.id = id;
        this.name = name;
        this.school = school;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public List<Employee> getTeachers() { return teachers; }
    public void setTeachers(List<Employee> teachers) { this.teachers = teachers; }

    public List<ReportCard> getReportCards() { return reportCards; }
    public void setReportCards(List<ReportCard> reportCards) { this.reportCards = reportCards; }

    public List<ScoreDetail> getScoreDetails() { return scoreDetails; }
    public void setScoreDetails(List<ScoreDetail> scoreDetails) { this.scoreDetails = scoreDetails; }
}
