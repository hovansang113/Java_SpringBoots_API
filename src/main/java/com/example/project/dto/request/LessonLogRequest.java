package com.example.project.dto.request;

import java.time.LocalDate;

public class LessonLogRequest {
    private Long teacherId;
    private Long subjectId;
    private Long classId;
    private Integer period;
    private String content;
    private LocalDate teachingDate;

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public Integer getPeriod() { return period; }
    public void setPeriod(Integer period) { this.period = period; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDate getTeachingDate() { return teachingDate; }
    public void setTeachingDate(LocalDate teachingDate) { this.teachingDate = teachingDate; }
    
}
