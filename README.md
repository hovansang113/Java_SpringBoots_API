# Hệ Thống Quản Lý Học Sinh

Ứng dụng quản lý thông tin học sinh trường trung học cấp 2 (THCS) và cấp 3 (THPT) sử dụng Spring Boot.

---

## Công Nghệ Sử Dụng

- Java 17
- Spring Boot 4.0.5
- Spring Data JPA / Hibernate
- Spring Security
- MySQL
- Maven

---

## Cấu Trúc Project

```
com.example.project/
├── config/             → Cấu hình Security
├── controller/         → Xử lý HTTP request
├── dto/
│   ├── request/        → Nhận dữ liệu từ client
│   └── response/       → Trả dữ liệu về client
├── entity/             → Các entity ánh xạ với DB
├── exception/          → Xử lý ngoại lệ toàn cục
├── repository/         → Truy vấn DB với JOIN FETCH
├── service/
│   ├── impl/           → Triển khai logic nghiệp vụ
│   └── *.java          → Interface service
└── ProjectApplication.java
```

---

## Mô Hình Cơ Sở Dữ Liệu

### Các Entity

| Entity | Bảng | Mô tả |
|--------|------|-------|
| School | school | Trường học (THCS / THPT) |
| ClassEntity | class | Lớp học thuộc trường |
| Employee | employee | Nhân viên / Giáo viên |
| Student | student | Học sinh |
| Parent | parent | Phụ huynh |
| Subject | subject | Môn học |
| AcademicRecord | academic_record | Sổ học bạ |
| ReportCard | report_card | Bảng điểm tổng kết |
| ScoreDetail | score_detail | Phiếu điểm từng bài kiểm tra |
| LessonLog | lesson_log | Sổ đầu bài |
| StudentCard | student_card | Thẻ học sinh |
| Account | account | Tài khoản đăng nhập |
| Role | role | Vai trò người dùng |
| AccountRole | account_role | Liên kết tài khoản - vai trò |

### Quan Hệ Giữa Các Entity

```
School (1) ──── (n) ClassEntity
School (1) ──── (n) Employee
School (1) ──── (n) Subject
ClassEntity (1) ──── (n) Student
ClassEntity (1) ──── (n) LessonLog
Employee (n) ──── (1) Subject  (1 giáo viên chỉ dạy 1 môn, 1 môn có nhiều giáo viên)
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

## Flow Chạy Của Ứng Dụng

### Flow Request Thành Công
```
Client gửi HTTP Request
        ↓
Controller nhận request
  - @GetMapping / @PostMapping / @PutMapping / @DeleteMapping
  - @RequestBody convert JSON → Request DTO
        ↓
Service xử lý logic nghiệp vụ
  - Validate dữ liệu
  - Gọi Repository truy vấn DB
  - Map Entity → Response DTO
        ↓
Repository truy vấn DB
  - JOIN FETCH load đầy đủ quan hệ (tránh N+1)
  - Trả về Entity
        ↓
Controller trả về ApiResponse
  - Status 200 OK (GET, PUT, DELETE)
  - Status 201 Created (POST)
        ↓
Client nhận response
```

### Format Response Thống Nhất (ApiResponse)
```json
// Success - GET / PUT / DELETE
{
    "status": 200,
    "message": "Success",
    "data": { ... }
}

// Created - POST
{
    "status": 201,
    "message": "Created",
    "data": { ... }
}

// Error - Không tìm thấy dữ liệu
{
    "status": 404,
    "message": "Không tìm thấy học sinh id: 1",
    "data": null
}

// Error - Lỗi hệ thống
{
    "status": 500,
    "message": "Internal Server Error",
    "data": null
}
```

### Flow Xử Lý Ngoại Lệ
```
Service throw ResourceNotFoundException
        ↓
GlobalExceptionHandler bắt exception
  - @ExceptionHandler(ResourceNotFoundException.class) → 404
  - @ExceptionHandler(Exception.class) → 500
        ↓
Trả về ApiResponse(status, message, null)
        ↓
Client nhận response lỗi
```

### Flow Save Student (có tự động tạo ReportCard)
```
POST /api/students
        ↓
StudentIplmService.save(request)
  - Tạo Student entity mới
  - Tìm Parent theo parentId → gán vào student
  - Tìm ClassEntity theo classId → gán vào student
  - Lưu student xuống DB
  - Tự động tạo ReportCard cho tất cả môn học
    (nếu request có schoolYear và semester)
  - Reload student với JOIN FETCH
        ↓
Trả về StudentDTO (có thông tin parent, class, school)
```

### Flow Save Parent
```
POST /api/parents
        ↓
ParentIplmService.save(request)
  - Tạo Parent entity mới
  - Lưu parent xuống DB
  - Duyệt qua studentIds → gán parent cho từng student
  - Reload parent với JOIN FETCH
        ↓
Trả về ParentDTO (có danh sách students)
```

### Flow Delete Parent
```
DELETE /api/parents/{id}
        ↓
ParentIplmService.deleteById(id)
  - Tìm parent theo id
  - Set parent_id = null cho tất cả student (student không bị xóa)
  - Xóa parent
        ↓
Trả về ApiResponse(200, "Success", null)
```

### Flow Update Parent
```
PUT /api/parents/{id}
        ↓
ParentIplmService.updateById(id, request)
  - Tìm parent theo id → không tìm thấy → throw ResourceNotFoundException
  - Cập nhật thông tin: fullName, phone, email, address, relationship
  - Lưu parent xuống DB
  - Duyệt qua students cũ → set parent_id = null cho từng student
  - Duyệt qua studentIds mới → gán parent cho từng student
  - Reload parent với JOIN FETCH
        ↓
Trả về ParentDTO (có danh sách students mới)
```

### Flow Save Subject
```
POST /api/subjects
        ↓
SubjectIplmService.save(request)
  - Tạo Subject entity mới
  - Tìm School theo schoolId → gán vào subject
  - Lưu subject xuống DB (có id)
  - Duyệt qua employeeIds → gán subject cho từng employee
  - Reload subject với JOIN FETCH
        ↓
Trả về SubjectDTO (có danh sách teachers)
```

### Flow Save Employee
```
POST /api/employees
        ↓
EmployeeIplmService.save(request)
  - Tạo Employee entity mới
  - Tìm School theo schoolId → gán vào employee
  - Tìm Subject theo subjectId → gán vào employee
  - Lưu employee xuống DB
  - Reload employee với JOIN FETCH
        ↓
Trả về EmployeeDTO (có thông tin school, subject, homeroomClasses)
```

### Flow ReportCard
```
ReportCard được tạo tự động khi tạo Student
  - Tạo 1 ReportCard cho mỗi môn học
  - score = null (chưa có điểm)

Cập nhật điểm:
PUT /api/report-cards/{id}
  - Chỉ cập nhật field score

Xem bảng điểm theo học sinh:
GET /api/report-cards/student/{studentId}
```

### Flow ScoreDetail (Phiếu điểm)
```
POST /api/score-details
        ↓
ScoreDetailIplmService.save(request)
  - Tìm Student theo studentId
  - Tìm Subject theo subjectId
  - Tạo ScoreDetail với schoolYear, semester, examDate, examType, score
  - Lưu xuống DB
  - Tự động tính điểm TB → cập nhật ReportCard.score
        ↓
Trả về ScoreDetailDTO

Xem phiếu điểm theo học sinh và kỳ:
GET /api/score-details/student/{studentId}/semester?schoolYear=2024-2025&semester=HK1
```

### Flow LessonLog (Sổ đầu bài)
```
LessonLog KHÔNG tự động tạo khi tạo lớp
Giáo viên tạo mới sau mỗi buổi dạy

POST /api/lesson-logs
        ↓
LessonLogIplmService.save(request)
  - Tìm ClassEntity theo classId
  - Tìm Employee theo teacherId
  - Tìm Subject theo subjectId (optional)
  - Tạo LessonLog với teachingDate, period, content
  - Lưu xuống DB
        ↓
Trả về LessonLogDTO

Xem sổ đầu bài của 1 lớp:
GET /api/lesson-logs/class/{classId}
```

### Flow AcademicRecord (Sổ học bạ)
```
POST /api/academic-records
        ↓
AcademicReportIplmService.save(request)
  - Tìm Student theo studentId
  - Tự động tính điểm TB từ ReportCard của học sinh trong kỳ
  - Tạo AcademicRecord với schoolYear, semester, averageScore (tự tính),
    conduct, teacherComment, parentComment
  - Lưu xuống DB
        ↓
Trả về AcademicRecordDTO

Xem sổ học bạ theo học sinh:
GET /api/academic-records/student/{studentId}

Tìm theo năm học và kỳ:
GET /api/academic-records/search?schoolYear=2024-2025&semester=HK1
```

---

## API Endpoints

### School - Quản lý trường học
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/schools` | Lấy danh sách trường |
| GET | `/api/schools/{id}` | Lấy trường theo id |
| POST | `/api/schools` | Thêm trường mới |
| PUT | `/api/schools/{id}` | Cập nhật trường |
| DELETE | `/api/schools/{id}` | Xóa trường |

### Student - Quản lý học sinh
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/students` | Lấy danh sách học sinh |
| GET | `/api/students/{id}` | Lấy học sinh theo id |
| POST | `/api/students` | Thêm học sinh mới + tự động tạo ReportCard |
| PUT | `/api/students/{id}` | Cập nhật học sinh |
| DELETE | `/api/students/{id}` | Xóa học sinh |

### Parent - Quản lý phụ huynh
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/parents` | Lấy danh sách phụ huynh |
| GET | `/api/parents/{id}` | Lấy phụ huynh theo id |
| POST | `/api/parents` | Thêm phụ huynh mới |
| PUT | `/api/parents/{id}` | Cập nhật phụ huynh |
| DELETE | `/api/parents/{id}` | Xóa phụ huynh (student không bị xóa) |

### Employee - Quản lý nhân viên
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/employees` | Lấy danh sách nhân viên |
| GET | `/api/employees/{id}` | Lấy nhân viên theo id |
| POST | `/api/employees` | Thêm nhân viên mới |
| PUT | `/api/employees/{id}` | Cập nhật nhân viên |
| DELETE | `/api/employees/{id}` | Xóa nhân viên |

### Subject - Quản lý môn học
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/subjects` | Lấy danh sách môn học |
| GET | `/api/subjects/{id}` | Lấy môn học theo id |
| GET | `/api/subjects/search?name=Toán` | Tìm môn học theo tên |
| POST | `/api/subjects` | Thêm môn học mới |
| PUT | `/api/subjects/{id}` | Cập nhật môn học |
| DELETE | `/api/subjects/{id}` | Xóa môn học |

### ReportCard - Quản lý bảng điểm tổng kết
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/report-cards` | Lấy tất cả bảng điểm |
| GET | `/api/report-cards/{id}` | Lấy bảng điểm theo id |
| GET | `/api/report-cards/student/{studentId}` | Lấy bảng điểm theo học sinh |
| GET | `/api/report-cards/search?studentName=...` | Tìm bảng điểm theo tên học sinh |
| PUT | `/api/report-cards/{id}` | Cập nhật điểm |
| DELETE | `/api/report-cards/{id}` | Xóa bảng điểm |

### ScoreDetail - Quản lý phiếu điểm
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/score-details` | Lấy tất cả phiếu điểm |
| GET | `/api/score-details/{id}` | Lấy phiếu điểm theo id |
| GET | `/api/score-details/student/{studentId}` | Lấy phiếu điểm theo học sinh |
| GET | `/api/score-details/student/{studentId}/semester?schoolYear=...&semester=...` | Lấy phiếu điểm theo học sinh và kỳ |
| POST | `/api/score-details` | Thêm phiếu điểm mới |
| PUT | `/api/score-details/{id}` | Cập nhật phiếu điểm |
| DELETE | `/api/score-details/{id}` | Xóa phiếu điểm |

### LessonLog - Quản lý sổ đầu bài
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/lesson-logs` | Lấy tất cả sổ đầu bài |
| GET | `/api/lesson-logs/{id}` | Lấy sổ đầu bài theo id |
| GET | `/api/lesson-logs/class/{classId}` | Xem sổ đầu bài của 1 lớp |
| POST | `/api/lesson-logs` | Giáo viên ghi sổ sau buổi dạy |
| PUT | `/api/lesson-logs/{id}` | Cập nhật bản ghi |
| DELETE | `/api/lesson-logs/{id}` | Xóa bản ghi |

### AcademicRecord - Quản lý sổ học bạ
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/academic-records` | Lấy tất cả sổ học bạ |
| GET | `/api/academic-records/{id}` | Lấy sổ học bạ theo id |
| GET | `/api/academic-records/student/{studentId}` | Lấy sổ học bạ theo học sinh |
| GET | `/api/academic-records/search/student-name?name=...` | Tìm theo tên học sinh |
| GET | `/api/academic-records/search?schoolYear=...&semester=...` | Tìm theo năm học và kỳ |
| POST | `/api/academic-records` | Thêm sổ học bạ mới |
| PUT | `/api/academic-records/{id}` | Cập nhật sổ học bạ |
| DELETE | `/api/academic-records/{id}` | Xóa sổ học bạ |

---

## Ví Dụ Request Body

### Thêm trường học
```json
{
    "name": "Trường THPT ABC",
    "address": "Hanoi",
    "phone": "0123456789",
    "email": "abc@gmail.com",
    "level": "THPT"
}
```

### Thêm học sinh
```json
{
    "fullName": "Nguyen Van A",
    "dateOfBirth": "2008-03-20",
    "gender": true,
    "address": "Hanoi",
    "parentId": 1,
    "classId": 1,
    "schoolYear": "2024-2025",
    "semester": "HK1"
}
```

### Thêm phụ huynh
```json
{
    "fullName": "Nguyen Van B",
    "phone": "0901234567",
    "email": "parent@gmail.com",
    "address": "Hanoi",
    "relationship": "Cha",
    "studentIds": [1, 2]
}
```

### Thêm nhân viên
```json
{
    "fullName": "Nguyen Van C",
    "dateOfBirth": "1985-05-10",
    "gender": true,
    "phone": "0901234567",
    "email": "teacher@gmail.com",
    "position": "Giáo viên",
    "schoolId": 1,
    "subjectId": 1
}
```

### Thêm môn học
```json
{
    "name": "Toán",
    "schoolId": 1,
    "employeeIds": [1, 2]
}
```

### Thêm phiếu điểm
```json
{
    "studentId": 1,
    "subjectId": 1,
    "schoolYear": "2024-2025",
    "semester": "HK1",
    "examDate": "2024-10-15",
    "examType": "Kiểm tra 15 phút",
    "score": 9.0
}
```

### Ghi sổ đầu bài
```json
{
    "classId": 1,
    "teacherId": 1,
    "subjectId": 1,
    "teachingDate": "2024-10-15",
    "period": 1,
    "content": "Hàm số bậc nhất"
}
```

### Thêm sổ học bạ
```json
{
    "studentId": 1,
    "schoolYear": "2024-2025",
    "semester": "HK1",
    "conduct": "Tốt",
    "teacherComment": "Học sinh chăm chỉ, tiến bộ rõ rệt",
    "parentComment": "Gia đình sẽ tiếp tục hỗ trợ con"
}
```

### Cập nhật điểm bảng điểm
```json
{
    "score": 8.5
}
```

---

## Xử Lý Ngoại Lệ

| Exception | HTTP Status | Mô tả |
|-----------|-------------|-------|
| ResourceNotFoundException | 404 | Không tìm thấy dữ liệu |
| Exception | 500 | Lỗi hệ thống |

---

## Lưu Ý Quan Trọng

### N+1 Problem
Tất cả repository đều dùng `JOIN FETCH` thay vì `findAll()` mặc định để tránh N+1:
```java
@Query("SELECT DISTINCT s FROM Student s " +
       "LEFT JOIN FETCH s.classEntity c " +
       "LEFT JOIN FETCH c.school " +
       "LEFT JOIN FETCH s.parent")
List<Student> findAllWithDetails();
```

### Cascade
- `School` xóa → `ClassEntity`, `Employee`, `Subject` bị xóa theo
- `ClassEntity` xóa → `Student` bị xóa theo
- `Parent` xóa → `Student` **không bị xóa**, chỉ set `parent_id = null`
- `Subject` xóa → `Employee.subject_id` set null trước khi xóa

### ReportCard
- Tự động tạo khi tạo Student (1 ReportCard cho mỗi môn học)
- Điểm TB tự động cập nhật khi thêm/sửa/xóa ScoreDetail
- Không tạo thủ công qua API POST

### ScoreDetail
- Thêm phiếu điểm → tự động tính TB → cập nhật ReportCard
- Có thể lọc theo học sinh + kỳ học

### LessonLog
- Không tự động tạo khi tạo lớp
- Giáo viên tạo mới sau mỗi buổi dạy
- Mỗi bản ghi = 1 buổi học (ngày, tiết, môn, nội dung)

### AcademicRecord
- Ghi lại kết quả học tập tổng hợp của học sinh theo kỳ
- `averageScore` tự động tính từ `ReportCard` của học sinh trong kỳ đó
- Bao gồm: điểm TB (tự tính), hạnh kiểm, nhận xét giáo viên, nhận xét phụ huynh
- Tạo thủ công qua API POST (không cần nhập averageScore)

---

## Cấu Hình

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student
spring.datasource.username=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
server.port=8888
```

---

## Hướng Dẫn Chạy

1. Tạo database MySQL:
```sql
CREATE DATABASE student;
```

2. Cập nhật thông tin kết nối trong `application.properties`

3. Chạy ứng dụng:
```bash
mvn spring-boot:run
```

4. Truy cập API tại: `http://localhost:8888`

---

## Xác Thực và Phân Quyền (JWT)

### Các File JWT

| File | Package | Chức năng |
|------|---------|-----------|
| `JwtUtil.java` | security | Tạo và validate JWT token |
| `JwtFilter.java` | security | Filter kiểm tra token mỗi request |
| `UserDetailsServiceImpl.java` | security | Load user từ DB |
| `SecurityConfig.java` | config | Cấu hình Spring Security + JWT |
| `AuthRequest.java` | dto/request | Nhận username/password từ client |
| `AuthResponse.java` | dto/response | Trả token về cho client |
| `AuthController.java` | controller | API đăng nhập |
| `AccountRepository.java` | repository | Truy vấn tài khoản từ DB |

---

### Giải Thích Từng File

#### 1. `JwtUtil.java`
Chịu trách nhiệm tạo và kiểm tra JWT token.

- `generateToken(username)` → Tạo token chứa username, thời gian tạo, thời gian hết hạn, ký bằng secret key
- `extractUsername(token)` → Giải mã token lấy username
- `isTokenValid(token)` → Kiểm tra token có hợp lệ không (chưa hết hạn, chữ ký đúng)

```java
// Token được tạo với thông tin:
// - Subject: username
// - IssuedAt: thời gian tạo
// - Expiration: thời gian hết hạn (86400000ms = 24h)
// - SignWith: HS256 + secret key
```

#### 2. `UserDetailsServiceImpl.java`
Load thông tin user từ DB để Spring Security xác thực.

- `loadUserByUsername(username)` → Tìm `Account` theo username trong DB, lấy danh sách `Role` → trả về `UserDetails` cho Spring Security

```java
// Flow:
// username → AccountRepository.findByUsername()
//          → JOIN FETCH account_role → role
//          → map role thành GrantedAuthority (ROLE_ADMIN, ROLE_TEACHER,...)
```

#### 3. `JwtFilter.java`
Filter chạy trước mỗi request để kiểm tra token.

- `doFilterInternal()` → Đọc header `Authorization: Bearer <token>` → validate token → set Authentication vào SecurityContext

```java
// Flow mỗi request:
// Header: Authorization: Bearer eyJhbGc...
//         ↓
// Tách lấy token (bỏ "Bearer ")
//         ↓
// isTokenValid() → true
//         ↓
// extractUsername() → lấy username
//         ↓
// loadUserByUsername() → lấy UserDetails
//         ↓
// Set Authentication vào SecurityContextHolder
//         ↓
// Cho phép request đi tiếp
```

#### 4. `SecurityConfig.java`
Cấu hình Spring Security.

- `filterChain()` → Tắt CSRF, dùng Stateless session, cho phép `/api/auth/**` không cần token, các API khác cần token, thêm `JwtFilter` vào trước `UsernamePasswordAuthenticationFilter`
- `authenticationManager()` → Bean để xác thực username/password
- `passwordEncoder()` → Bean BCrypt để mã hóa password

#### 5. `AuthController.java`
API đăng nhập duy nhất.

- `POST /api/auth/login` → Nhận username/password → xác thực → tạo token → trả về

#### 6. `AccountRepository.java`
- `findByUsername()` → Query JOIN FETCH lấy account kèm roles từ 3 bảng `account`, `account_role`, `role`

---

### Vai Trò Trong Hệ Thống

| Vai trò | Mô tả | Quyền hạn |
|---------|-------|-----------|
| `ROLE_ADMIN` | Quản trị viên | Toàn quyền CRUD tất cả |
| `ROLE_TEACHER` | Giáo viên | Ghi sổ đầu bài, nhập điểm, xem học sinh |
| `ROLE_PARENT` | Phụ huynh | Xem thông tin con, điểm, học bạ |
| `ROLE_STUDENT` | Học sinh | Xem thông tin cá nhân, điểm |

---

### Flow Đăng Nhập
```
POST /api/auth/login
{ "username": "admin", "password": "123456" }
        ↓
AuthController.login()
        ↓
AuthenticationManager.authenticate()
  - UserDetailsServiceImpl.loadUserByUsername()
  - Tìm Account trong DB → JOIN FETCH roles
  - So sánh password với BCrypt
        ↓
Xác thực thành công
        ↓
JwtUtil.generateToken(username)
  - Tạo token chứa username + hết hạn 24h
        ↓
Trả về AuthResponse
{
    "status": 200,
    "message": "Success",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "username": "admin",
        "role": "ROLE_ADMIN"
    }
}
```

### Flow Gọi API Sau Khi Đăng Nhập
```
Client gửi request kèm token
Header: Authorization: Bearer eyJhbGc...
        ↓
JwtFilter.doFilterInternal()
  - Đọc token từ header
  - isTokenValid() → kiểm tra token hợp lệ
  - extractUsername() → lấy username từ token
  - loadUserByUsername() → lấy UserDetails + roles
  - Set Authentication vào SecurityContext
        ↓
Request đi tiếp vào Controller
        ↓
Trả về response
```

### Flow Token Không Hợp Lệ
```
Client gửi request với token sai/hết hạn
        ↓
JwtFilter → isTokenValid() → false
        ↓
Không set Authentication
        ↓
Spring Security → 401 Unauthorized
```

---

### Dữ Liệu Mẫu DB

```sql
-- Thêm roles
INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_TEACHER');
INSERT INTO role (name) VALUES ('ROLE_PARENT');
INSERT INTO role (name) VALUES ('ROLE_STUDENT');

-- Thêm account (password = BCrypt của "123456")
INSERT INTO account (username, password, status)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpsl7iKVYDE6i', true);

-- Gán role cho account
INSERT INTO account_role (account_id, role_id) VALUES (1, 1);
```

### Test Đăng Nhập
```
POST http://localhost:8888/api/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

### Dùng Token Gọi API
```
GET http://localhost:8888/api/students
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### Cấu Hình JWT trong application.properties
```properties
jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
jwt.expiration=86400000
```
