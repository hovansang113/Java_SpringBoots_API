package com.example.project.entity;

import java.time.LocalDate;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "address", length = 255)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AcademicRecord> academicRecords;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReportCard> reportCards;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private StudentCard studentCard;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ScoreDetail> scoreDetails;

    public Student() {
    }

    public Student(Long id, String fullName, LocalDate dateOfBirth, Boolean gender, String address,
            Parent parent, Account account, ClassEntity classEntity) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.parent = parent;
        this.account = account;
        this.classEntity = classEntity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Boolean getGender() { return gender; }
    public void setGender(Boolean gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Parent getParent() { return parent; }
    public void setParent(Parent parent) { this.parent = parent; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public ClassEntity getClassEntity() { return classEntity; }
    public void setClassEntity(ClassEntity classEntity) { this.classEntity = classEntity; }

    public List<AcademicRecord> getAcademicRecords() { return academicRecords; }
    public void setAcademicRecords(List<AcademicRecord> academicRecords) { this.academicRecords = academicRecords; }

    public List<ReportCard> getReportCards() { return reportCards; }
    public void setReportCards(List<ReportCard> reportCards) { this.reportCards = reportCards; }

    public StudentCard getStudentCard() { return studentCard; }
    public void setStudentCard(StudentCard studentCard) { this.studentCard = studentCard; }

    public List<ScoreDetail> getScoreDetails() { return scoreDetails; }
    public void setScoreDetails(List<ScoreDetail> scoreDetails) { this.scoreDetails = scoreDetails; }
}
