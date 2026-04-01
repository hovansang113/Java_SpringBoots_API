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
├── repository/         → Truy vấn DB
├── service/
│   ├── impl/           → Triển khai logic nghiệp vụ
│   └── *.java          → Interface service
└── ProjectApplication.java
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
  - JOIN FETCH load đầy đủ quan hệ
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

### Flow Save Student
```
POST /api/students
        ↓
StudentController.save(@RequestBody StudentRequest)
        ↓
StudentIplmService.save(request)
  - Tạo Student entity mới
  - Tìm Parent theo parentId → gán vào student
  - Tìm ClassEntity theo classId → gán vào student
  - Lưu student xuống DB
  - Reload student với JOIN FETCH
        ↓
Trả về StudentDTO (có thông tin parent, class, school)
```

### Flow Save Parent
```
POST /api/parents
        ↓
ParentController.save(@RequestBody ParentRequest)
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
ParentController.updateById(@PathVariable id, @RequestBody ParentRequest)
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
| ReportCard | report_card | Bảng điểm |
| ScoreDetail | score_detail | Phiếu điểm |
| LessonLog | lesson_log | Sổ đầu bài |
| StudentCard | student_card | Thẻ học sinh |
| Account | account | Tài khoản đăng nhập |
| Role | role | Vai trò người dùng |
| AccountRole | account_role | Liên kết tài khoản - vai trò |

### Quan Hệ Giữa Các Entity

```
School (1) ──── (n) ClassEntity
School (1) ──── (n) Employee
ClassEntity (1) ──── (n) Student
ClassEntity (1) ──── (n) LessonLog
Employee (n) ──── (1) Subject (giáo viên phụ trách môn)
Employee (1) ──── (n) LessonLog
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
| POST | `/api/students` | Thêm học sinh mới |
| PUT | `/api/students/{id}` | Cập nhật học sinh |
| DELETE | `/api/students/{id}` | Xóa học sinh |

### Parent - Quản lý phụ huynh
| Method | URL | Chức năng |
|--------|-----|-----------|
| GET | `/api/parents` | Lấy danh sách phụ huynh |
| GET | `/api/parents/{id}` | Lấy phụ huynh theo id |
| POST | `/api/parents` | Thêm phụ huynh mới |
| PUT | `/api/parents/{id}` | Cập nhật phụ huynh |
| DELETE | `/api/parents/{id}` | Xóa phụ huynh |

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
    "classId": 1
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

---

## Xử Lý Ngoại Lệ

| Exception | HTTP Status | Mô tả |
|-----------|-------------|-------|
| ResourceNotFoundException | 404 | Không tìm thấy dữ liệu |
| Exception | 500 | Lỗi hệ thống |

---

## Cấu Hình

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student
spring.datasource.username=root
spring.jpa.hibernate.ddl-auto=update
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
