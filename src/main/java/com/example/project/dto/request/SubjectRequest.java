package com.example.project.dto.request;

import java.util.List;

public class SubjectRequest {
    private String name;
    private Long schoolId;
    private List<Long> employeeIds;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getSchoolId() { return schoolId; }
    public void setSchoolId(Long schoolId) { this.schoolId = schoolId; }

    public List<Long> getEmployeeIds() { return employeeIds; }
    public void setEmployeeIds(List<Long> employeeIds) { this.employeeIds = employeeIds; }
}
