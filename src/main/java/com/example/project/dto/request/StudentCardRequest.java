package com.example.project.dto.request;

import java.time.LocalDate;

public class StudentCardRequest {

    private Long studentId;
    private String avatar;
    private LocalDate issueDate;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
}
