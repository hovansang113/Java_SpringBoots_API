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
@Table(name = "employee")
public class Employee {

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

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "position", length = 100)
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    // 1 giáo viên chỉ dạy 1 môn, nhưng 1 môn có thể có nhiều giáo viên
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LessonLog> lessonLogs;

    @OneToMany(mappedBy = "homeroomTeacher", fetch = FetchType.LAZY)
    private List<ClassEntity> homeroomClasses;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    public Employee() {
    }

    public Employee(Long id, String fullName, LocalDate dateOfBirth, Boolean gender, String phone, String email,
            String position, School school, Account account) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.position = position;
        this.school = school;
        this.account = account;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Boolean getGender() { return gender; }
    public void setGender(Boolean gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public List<LessonLog> getLessonLogs() { return lessonLogs; }
    public void setLessonLogs(List<LessonLog> lessonLogs) { this.lessonLogs = lessonLogs; }

    public List<ClassEntity> getHomeroomClasses() { return homeroomClasses; }
    public void setHomeroomClasses(List<ClassEntity> homeroomClasses) { this.homeroomClasses = homeroomClasses; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
