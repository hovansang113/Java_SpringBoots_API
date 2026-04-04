package com.example.project.dto.response;

import com.example.project.entity.LessonLog;
import java.time.LocalDate;

public class LessonLogDTO {

    private Long id;
    private LocalDate teachingDate;
    private String content;
    private Integer period;
    private ClassInfo classEntity;
    private TeacherInfo teacher;
    private SubjectInfo subject;

    public LessonLogDTO(LessonLog lessonLog) {
        this.id = lessonLog.getId();
        this.teachingDate = lessonLog.getTeachingDate();
        this.content = lessonLog.getContent();
        this.period = lessonLog.getPeriod();
        if (lessonLog.getClassEntity() != null) {
            this.classEntity = new ClassInfo(
                lessonLog.getClassEntity().getId(),
                lessonLog.getClassEntity().getName()
            );
        }
        if (lessonLog.getTeacher() != null) {
            this.teacher = new TeacherInfo(
                lessonLog.getTeacher().getId(),
                lessonLog.getTeacher().getFullName()
            );
        }
        if (lessonLog.getSubject() != null) {
            this.subject = new SubjectInfo(
                lessonLog.getSubject().getId(),
                lessonLog.getSubject().getName()
            );
        }
    }

    public static class ClassInfo {
        private Long id;
        private String name;

        public ClassInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }

    public static class TeacherInfo {
        private Long id;
        private String fullName;

        public TeacherInfo(Long id, String fullName) {
            this.id = id;
            this.fullName = fullName;
        }

        public Long getId() { return id; }
        public String getFullName() { return fullName; }
    }

    public static class SubjectInfo {
        private Long id;
        private String name;

        public SubjectInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }

    public Long getId() { return id; }
    public LocalDate getTeachingDate() { return teachingDate; }
    public String getContent() { return content; }
    public Integer getPeriod() { return period; }
    public ClassInfo getClassEntity() { return classEntity; }
    public TeacherInfo getTeacher() { return teacher; }
    public SubjectInfo getSubject() { return subject; }
}
