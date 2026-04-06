package com.example.project.dto.response;

import com.example.project.entity.StudentCard;
import java.time.LocalDate;

public class StudentCardDTO {

    private Long id;
    private String cardCode;
    private String avatar;
    private LocalDate issueDate;
    private StudentInfo student;

    public StudentCardDTO(StudentCard studentCard) {
        this.id = studentCard.getId();
        this.cardCode = studentCard.getCardCode();
        this.avatar = studentCard.getAvatar();
        this.issueDate = studentCard.getIssueDate();
        if (studentCard.getStudent() != null) {
            this.student = new StudentInfo(
                studentCard.getStudent().getId(),
                studentCard.getStudent().getFullName()
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
    public String getCardCode() { return cardCode; }
    public String getAvatar() { return avatar; }
    public LocalDate getIssueDate() { return issueDate; }
    public StudentInfo getStudent() { return student; }
}
