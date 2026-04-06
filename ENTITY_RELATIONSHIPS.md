# Quan Hệ Giữa Các Entity

## Sơ Đồ Tổng Quan

```
School (1) ──── (n) ClassEntity
School (1) ──── (n) Employee
School (1) ──── (n) Subject
ClassEntity (1) ──── (n) Student
ClassEntity (1) ──── (n) LessonLog
Employee (n) ──── (1) Subject  → 1 giáo viên chỉ dạy 1 môn, 1 môn có nhiều giáo viên
Employee (1) ──── (n) LessonLog
Employee (1) ──── (n) ClassEntity (giáo viên chủ nhiệm)
Student (n) ──── (1) Parent
Student (1) ──── (1) StudentCard
Student (1) ──── (n) AcademicRecord
Student (1) ──── (n) ReportCard
Student (1) ──── (n) ScoreDetail
Subject (1) ──── (n) ReportCard
Subject (1) ──── (n) ScoreDetail
Account (1) ──── (1) Employee / Student / Parent
Account (n) ──── (n) Role  [qua bảng account_role]
```

---

## Chi Tiết Từng Entity

### School
| Quan hệ | Kiểu | Cascade | Ghi chú |
|---------|------|---------|---------|
| School → ClassEntity | 1-n | ALL | Xóa trường → xóa lớp |
| School → Employee | 1-n | ALL | Xóa trường → xóa nhân viên |
| School → Subject | 1-n | ALL | Xóa trường → xóa môn học |

### ClassEntity
| Quan hệ | Kiểu | Cascade | Ghi chú |
|---------|------|---------|---------|
| ClassEntity → Student | 1-n | ALL | Xóa lớp → xóa học sinh |
| ClassEntity → LessonLog | 1-n | ALL | Xóa lớp → xóa sổ đầu bài |
| ClassEntity ← School | n-1 | - | FK: school_id |
| ClassEntity ← Employee | n-1 | - | FK: homeroom_teacher_id |

### Employee
| Quan hệ | Kiểu | Cascade | Ghi chú |
|---------|------|---------|---------|
| Employee ← School | n-1 | - | FK: school_id |
| Employee ← Subject | n-1 | - | FK: subject_id (1 GV chỉ dạy 1 môn) |
| Employee → LessonLog | 1-n | ALL | Xóa GV → xóa sổ đầu bài |
| Employee → ClassEntity | 1-n | - | Giáo viên chủ nhiệm, không cascade |
| Employee ← Account | 1-1 | - | FK: account_id |

### Student
| Quan hệ | Kiểu | Cascade | Ghi chú |
|---------|------|---------|---------|
| Student ← Parent | n-1 | - | FK: parent_id |
| Student ← ClassEntity | n-1 | - | FK: class_id |
| Student ← Account | 1-1 | - | FK: account_id |
| Student → StudentCard | 1-1 | ALL | Xóa HS → xóa thẻ |
| Student → AcademicRecord | 1-n | ALL | Xóa HS → xóa học bạ |
| Student → ReportCard | 1-n | ALL | Xóa HS → xóa bảng điểm |
| Student → ScoreDetail | 1-n | ALL | Xóa HS → xóa phiếu điểm |

### Parent
| Quan hệ | Kiểu | Cascade | Ghi chú |
|---------|------|---------|---------|
| Parent → Student | 1-n | - | Xóa PH → set parent_id = null, không xóa HS |
| Parent ← Account | 1-1 | - | FK: account_id |

### Subject
| Quan hệ | Kiểu | Cascade | Ghi chú |
|---------|------|---------|---------|
| Subject ← School | n-1 | - | FK: school_id |
| Subject → Employee | 1-n | - | 1 môn có nhiều GV, mappedBy = "subject" |
| Subject → ReportCard | 1-n | ALL | Xóa môn → xóa bảng điểm |
| Subject → ScoreDetail | 1-n | ALL | Xóa môn → xóa phiếu điểm |

### Account & Role
| Quan hệ | Kiểu | Cascade | Ghi chú |
|---------|------|---------|---------|
| Account ↔ Role | n-n | - | Bảng trung gian: account_role |

---

## Cascade Rules

| Xóa | Kết Quả |
|-----|---------|
| School | → ClassEntity, Employee, Subject bị xóa |
| ClassEntity | → Student, LessonLog bị xóa |
| Student | → StudentCard, AcademicRecord, ReportCard, ScoreDetail bị xóa |
| Parent | → Student **không bị xóa**, chỉ set parent_id = null |
| Subject | → ReportCard, ScoreDetail bị xóa |
| Employee | → LessonLog bị xóa |

---

## Lưu Ý Quan Trọng

- `Employee.subject` → `@ManyToOne` (nhiều GV dạy 1 môn, 1 GV chỉ dạy 1 môn)
- `Parent.students` → không có `CascadeType.ALL` để tránh xóa học sinh khi xóa phụ huynh
- `Employee.homeroomClasses` → không cascade để tránh xóa lớp khi xóa giáo viên
- Bảng `account_role` được quản lý bởi `@ManyToMany` trong `Account`, không cần entity riêng
