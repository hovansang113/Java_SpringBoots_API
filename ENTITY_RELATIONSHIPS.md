# Entity Relationships Documentation

## Tóm tắt các mối quan hệ trong dự án

### 1. Student Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| Student ← Parent | M-1 | Nhiều học sinh có 1 phụ huynh | - |
| Student ← ClassEntity | M-1 | Nhiều học sinh trong 1 lớp | - |
| Student ↔ StudentCard | 1-1 | Một thẻ cho một học sinh | - |
| Student → AcademicRecord | 1-M | 1 học sinh có nhiều học bạ (mỗi năm) | ALL |
| Student → ScoreDetail | 1-M | 1 học sinh có nhiều điểm (nhiều môn, nhiều lần) | ALL |
| Student → ReportCard | 1-M | 1 học sinh có nhiều bảng điểm (mỗi học kỳ) | ALL |

**Mappings:**
- Student.parent ← Parent.students (2 chiều)
- Student.classEntity ← ClassEntity.students (2 chiều)
- Student.studentCard ↔ StudentCard.student (2 chiều)
- Student.academicRecords ← AcademicRecord.student (2 chiều)
- Student.scoreDetails ← ScoreDetail.student (2 chiều)
- Student.reportCards ← ReportCard.student (2 chiều)

---

### 2. Parent Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| Parent ↔ Account | 1-1 | Một tài khoản cho một phụ huynh | - |
| Parent → Student | 1-M | 1 phụ huynh có nhiều con em | ALL |

**Mappings:**
- Parent.account (OWNING SIDE)
- Parent.students ← Student.parent (2 chiều)

---

### 3. Employee Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| Employee ← School | M-1 | Nhiều nhân viên ở 1 trường | - |
| Employee ↔ Subject | 1-1 | 1 giáo viên dạy 1 môn học | - |
| Employee → LessonLog | 1-M | 1 giáo viên ghi nhiều bài dạy | ALL |
| Employee ← ClassEntity | M-1 | Chủ nhiệm của nhiều lớp | - |
| Employee ↔ Account | 1-1 | Một tài khoản cho một nhân viên | - |

**Mappings:**
- Employee.school ← School.employees (2 chiều)
- Employee.subject ↔ Subject.teacher (2 chiều)
- Employee.lessonLogs ← LessonLog.teacher (2 chiều)
- Employee.homeroomClasses ← ClassEntity.homeroomTeacher (2 chiều)
- Employee.account (OWNING SIDE)

---

### 4. ClassEntity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| ClassEntity ← School | M-1 | Nhiều lớp ở 1 trường | - |
| ClassEntity ← Employee | M-1 | Lớp có 1 chủ nhiệm | - |
| ClassEntity → Student | 1-M | 1 lớp có nhiều học sinh | ALL |
| ClassEntity → Subject | 1-M | 1 lớp dạy nhiều môn học | ALL |

**Mappings:**
- ClassEntity.school ← School.classes (2 chiều)
- ClassEntity.homeroomTeacher ← Employee.homeroomClasses (2 chiều)
- ClassEntity.students ← Student.classEntity (2 chiều)
- ClassEntity.subjects ← Subject.classEntity (2 chiều)

---

### 5. Subject Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| Subject ↔ Employee | 1-1 | 1 môn học được dạy bởi 1 giáo viên | - |
| Subject ← ClassEntity | M-1 | Một môn học được dạy ở nhiều lớp | - |
| Subject → ReportCard | 1-M | 1 môn có nhiều bảng điểm | ALL |

**Mappings:**
- Subject.teacher ↔ Employee.subject (2 chiều)
- Subject.classEntity ← ClassEntity.subjects (2 chiều)
- Subject.reportCards ← ReportCard.subject (2 chiều)

---

### 6. School Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| School → Employee | 1-M | 1 trường có nhiều nhân viên | ALL |
| School → ClassEntity | 1-M | 1 trường có nhiều lớp | ALL |

**Mappings:**
- School.employees ← Employee.school (2 chiều)
- School.classes ← ClassEntity.school (2 chiều)

---

### 7. Account Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| Account ↔ Role | M-M | 1 tài khoản có nhiều vai trò, 1 vai trò thuộc nhiều tài khoản | PERSIST, MERGE |

**Mappings:**
- Account.roles ↔ Role.accounts (2 chiều)

---

### 8. Role Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| Role ↔ Account | M-M | 1 vai trò được gán cho nhiều tài khoản | - |

**Mappings:**
- Role.accounts ↔ Account.roles (2 chiều)

---

### 9. Account Role Relationship (Join Table)

Dùng bảng trung gian `account_role` với mapping `@ManyToMany`. Không cần tạo entity `AccountRole` riêng biệt vì không có thêm thông tin phụ.

---

### 10. ReportCard Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| ReportCard ← Student | M-1 | Nhiều bảng điểm cho 1 học sinh | - |
| ReportCard ← Subject | M-1 | Nhiều bảng điểm cho 1 môn học | - |

**Mappings:**
- ReportCard.student ← Student.reportCards (2 chiều)
- ReportCard.subject ← Subject.reportCards (2 chiều)

---

### 11. ScoreDetail Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| ScoreDetail ← Student | M-1 | Nhiều điểm số cho 1 học sinh | - |

**Mappings:**
- ScoreDetail.student ← Student.scoreDetails (2 chiều)

---

### 12. AcademicRecord Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| AcademicRecord ← Student | M-1 | Một học bạ cho một học sinh (mỗi năm học) | - |

**Mappings:**
- AcademicRecord.student ← Student.academicRecords (2 chiều)

---

### 13. StudentCard Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| StudentCard ↔ Student | 1-1 | Một thẻ cho một học sinh | - |

**Mappings:**
- StudentCard.student ↔ Student.studentCard (2 chiều)

---

### 14. LessonLog Entity

| Quan hệ | Kiểu | Chi tiết | Cascade |
|---------|------|---------|---------|
| LessonLog ← Employee | M-1 | Nhiều bài dạy của 1 giáo viên | - |

**Mappings:**
- LessonLog.teacher ← Employee.lessonLogs (2 chiều)

---

## Hệ Thống CascadeType

### Tại Sao Phải Thêm CascadeType.ALL?

**CascadeType.ALL** đảm bảo rằng tất cả thao tác trên entity cha (parent) sẽ **tự động áp dụng** cho entity con (child). 

#### Các thao tác được Cascade:
1. **Persist** - Khi lưu parent → tự động lưu child
2. **Merge** - Khi cập nhật parent → tự động cập nhật child
3. **Remove** - Khi xóa parent → tự động xóa child
4. **Refresh** - Khi làm mới parent → tự động làm mới child
5. **Detach** - Khi ngắt kết nối parent → tự động ngắt child

#### Ví dụ Thực Tế:

**Không có CascadeType.ALL:**
```java
School school = new School("Trường A", ...);
schoolRepository.save(school);

Employee emp = new Employee("Anh Minh", ...);
emp.setSchool(school);
employeeRepository.save(emp);  // ❌ Phải save riêng

// Khi xóa trường, nhân viên không bị xóa
schoolRepository.delete(school);  // ❌ Nhân viên vẫn tồn tại - mầm bệnh dữ liệu
```

**Có CascadeType.ALL:**
```java
School school = new School("Trường A", ...);
schoolRepository.save(school);

Employee emp = new Employee("Anh Minh", ...);
emp.setSchool(school);
school.getEmployees().add(emp);
schoolRepository.save(school);  // ✅ Nhân viên tự động được save

// Khi xóa trường, nhân viên cũng bị xóa
schoolRepository.delete(school);  // ✅ Nhân viên tự động bị xóa
```

---

### Danh Sách File Cần CascadeType.ALL và Lý Do

#### 1. **Parent.java → Students** 
```java
@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Student> students;
```
**Lý Do:** 
- Khi xóa phụ huynh → học sinh phải bị xóa (vì học sinh phụ thuộc vào phụ huynh)
- Tính nhất quán dữ liệu: Không có phụ huynh "ghost" trong hệ thống

---

#### 2. **School.java → Employees** 
```java
@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Employee> employees;
```
**Lý Do:**
- Khi xóa trường → giáo viên/nhân viên phải bị xóa
- Một trường tắm → tất cả nhân viên phải đi (logic tự nhiên)

---

#### 3. **School.java → Classes** 
```java
@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<ClassEntity> classes;
```
**Lý Do:**
- Khi xóa trường → tất cả lớp phải bị xóa
- Lớp không thể tồn tại độc lập ngoài trường

---

#### 4. **Student.java → AcademicRecord** 
```java
@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<AcademicRecord> academicRecords;
```
**Lý Do:**
- Khi xóa học sinh → học bạ phải bị xóa theo
- Học bạ chỉ có nghĩa khi liên quan đến học sinh

---

#### 5. **Student.java → StudentCard** 
```java
@OneToOne(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
private StudentCard studentCard;
```
**Lý Do:**
- Khi xóa học sinh → thẻ học sinh phải bị xóa
- 1-1 quan hệ mạnh: Thẻ chỉ có ý nghĩa khi có học sinh

---

#### 6. **Student.java → ScoreDetail** 
```java
@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<ScoreDetail> scoreDetails;
```
**Lý Do:**
- Khi xóa học sinh → tất cả điểm số phải bị xóa
- Điểm số không có ý nghĩa không có học sinh

---

#### 7. **ClassEntity.java → Students** 
```java
@OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Student> students;
```
**Lý Do:**
- Khi xóa lớp → học sinh trong lớp phải bị xóa
- Học sinh gắn với lớp cụ thể

---

#### 8. **ClassEntity.java → Subjects** 
```java
@OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Subject> subjects;
```
**Lý Do:**
- Khi xóa lớp → môn học được dạy ở lớp đó phải bị xóa
- Môn học trong lớp phụ thuộc vào lớp

---

#### 9. **Employee.java → LessonLogs** 
```java
@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<LessonLog> lessonLogs;
```
**Lý Do:**
- Khi xóa giáo viên → nhật ký dạy phải bị xóa
- Nhật ký chỉ liên quan đến giáo viên cụ thể

---

#### 10. **Employee.java → HomeroomClasses** 
```java
@OneToMany(mappedBy = "homeroomTeacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<ClassEntity> homeroomClasses;
```
**Lý Do:**
- Khi xóa giáo viên chủ nhiệm → lớp phải bị gỡ bỏ chủ nhiệm
- **Lưu ý:** Có thể sửa thành `CascadeType.DETACH` để không xóa lớp, chỉ gỡ liên kết

---

#### 11. **Subject.java → ReportCards** 
```java
@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<ReportCard> reportCards;
```
**Lý Do:**
- Khi xóa môn học → bảng điểm của môn đó phải bị xóa
- Bảng điểm chỉ liên quan đến môn cụ thể

---

#### 12. **Account.java → Roles** 
```java
@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(
    name = "account_role",
    joinColumns = @JoinColumn(name = "account_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
)
private List<Role> roles;
```
**Lý Do:**
- Khi lưu/cập nhật tài khoản → các vai trò gán kèm sẽ được xử lý.
- Không dùng `CascadeType.REMOVE` vì không muốn xóa Role khi xóa Account.

---

### Các Quan Hệ KHÔNG Cần CascadeType.ALL

| Quan hệ | Lý Do Không Cascade |
|---------|-------------------|
| Student ← Parent (M-1) | Parent có thể quản lý nhiều student, không phải xóa cha vì con tồn tại |
| Student ← ClassEntity (M-1) | Lớp có thể có nhiều học sinh, không xóa lớp vì học sinh tồn tại |
| Student ← Account (1-1) | Account độc lập, không liên quan trực tiếp |
| Employee ← School (M-1) | Trường xóa không xóa nhân viên tự động (sửa sang trường khác) |
| ReportCard ← Student/Subject (M-1) | Điểm số không phải xóa khi student/subject bị xóa |
| Account ↔ Role (M-M) | Xóa tài khoản không được xóa vai trò trong hệ thống |

---

## Tóm Tắt CascadeType Được Thêm

**Parent View (13 files):**
1. ✅ Parent.students → CascadeType.ALL
2. ✅ School.employees → CascadeType.ALL
3. ✅ School.classes → CascadeType.ALL
4. ✅ Employee.lessonLogs → CascadeType.ALL
5. ✅ Employee.homeroomClasses → CascadeType.ALL
6. ✅ ClassEntity.students → CascadeType.ALL
7. ✅ ClassEntity.subjects → CascadeType.ALL
8. ✅ Student.academicRecords → CascadeType.ALL
9. ✅ Student.studentCard → CascadeType.ALL
10. ✅ Student.scoreDetails → CascadeType.ALL
11. ✅ Subject.reportCards → CascadeType.ALL
12. ✅ Account.roles → PERSIST, MERGE

---

## Kết Luận

**CascadeType.ALL là cần thiết vì:**
- ✅ **Tính nhất quán dữ liệu** - Không có orphan records (bản ghi mồ côi)
- ✅ **Tiết kiệm code** - Không phải save/delete từng entity
- ✅ **Logic tự động** - "Xóa trường ghi xóa tất cả lớp" là tự nhiên
- ✅ **Đáp ứng yêu cầu kinh doanh** - Phù hợp với thực tế quản lý trường học

- **CascadeType.CascadeType
