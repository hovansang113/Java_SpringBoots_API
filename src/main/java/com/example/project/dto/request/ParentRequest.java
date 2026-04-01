package com.example.project.dto.request;

import java.util.List;

public class ParentRequest {

    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String relationship;
    private List<Long> studentIds;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public List<Long> getStudentIds() { return studentIds; }
    public void setStudentIds(List<Long> studentIds) { this.studentIds = studentIds; }
}
