# Hướng Dẫn Implement JWT Authentication

---

## JWT Là Gì?

JWT (JSON Web Token) là một chuẩn mở để truyền thông tin an toàn giữa các bên dưới dạng JSON.

**Cấu trúc JWT gồm 3 phần ngăn cách bởi dấu `.`:**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.abc123
      HEADER                  PAYLOAD          SIGNATURE
```

- **Header** → thuật toán mã hóa (HS256)
- **Payload** → dữ liệu (username, expiration,...)
- **Signature** → chữ ký để verify token không bị giả mạo

---

## Tại Sao Dùng JWT?

| | Session (cũ) | JWT (mới) |
|--|--|--|
| Lưu trữ | Server lưu session | Client lưu token |
| Scale | Khó scale nhiều server | Dễ scale |
| Mobile | Khó dùng | Dễ dùng |
| Stateless | Không | Có |

---

## Dependency Cần Thêm

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

---

## Cấu Trúc File Cần Tạo

```
security/
  ├── JwtUtil.java                → Tạo và validate token
  ├── JwtFilter.java              → Filter kiểm tra token mỗi request
  └── UserDetailsServiceImpl.java → Load user từ DB

config/
  └── SecurityConfig.java         → Cấu hình Spring Security

dto/
  ├── request/AuthRequest.java    → Nhận username/password
  └── response/AuthResponse.java  → Trả token về client

controller/
  └── AuthController.java         → API đăng nhập

repository/
  └── AccountRepository.java      → Truy vấn tài khoản
```

---

## Bước 1: Cấu Hình application.properties

```properties
jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
jwt.expiration=86400000
```

- `jwt.secret` → key bí mật để ký token (phải dài ít nhất 256 bit)
- `jwt.expiration` → thời gian hết hạn (86400000ms = 24h)

---

## Bước 2: Tạo JwtUtil.java

**Chức năng:** Tạo token, giải mã token, kiểm tra token hợp lệ.

```java
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Lấy key để ký token
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Tạo token từ username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)           // lưu username vào token
                .setIssuedAt(new Date())         // thời gian tạo
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // hết hạn
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // ký token
                .compact();
    }

    // Lấy username từ token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Kiểm tra token có hợp lệ không
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // token hết hạn hoặc sai chữ ký
        }
    }
}
```

---

## Bước 3: Tạo UserDetailsServiceImpl.java

**Chức năng:** Load thông tin user từ DB để Spring Security xác thực.

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Tìm account trong DB kèm roles
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy: " + username));

        // Map roles thành GrantedAuthority
        return new User(
                account.getUsername(),
                account.getPassword(),
                account.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }
}
```

**Lưu ý:** Role phải có prefix `ROLE_` → `ROLE_ADMIN`, `ROLE_TEACHER`,...

---

## Bước 4: Tạo JwtFilter.java

**Chức năng:** Chạy trước mỗi request, kiểm tra token trong header.

```java
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Đọc header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Kiểm tra có Bearer token không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // bỏ "Bearer "

            // 3. Validate token
            if (jwtUtil.isTokenValid(token)) {
                String username = jwtUtil.extractUsername(token);

                // 4. Load UserDetails từ DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Set Authentication vào SecurityContext
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 6. Cho request đi tiếp
        filterChain.doFilter(request, response);
    }
}
```

---

## Bước 5: Tạo SecurityConfig.java

**Chức năng:** Cấu hình Spring Security, phân quyền, thêm JwtFilter.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // Cấu hình xác thực với UserDetailsService + BCrypt
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())           // tắt CSRF (dùng JWT không cần)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // không dùng session
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()  // login không cần token
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/students").hasAnyRole("ADMIN", "TEACHER")
                .anyRequest().authenticated()        // còn lại cần đăng nhập
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // thêm JwtFilter
        return http.build();
    }
}
```

---

## Bước 6: Tạo AuthController.java

**Chức năng:** API đăng nhập, trả về token.

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // 1. Xác thực username/password
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword())
        );

        // 2. Tạo token
        String token = jwtUtil.generateToken(auth.getName());

        // 3. Lấy role đầu tiên
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse("");

        // 4. Trả về token
        return ResponseEntity.ok(new AuthResponse(token, auth.getName(), role));
    }
}
```

---

## Bước 7: Tạo AccountRepository.java

```java
public interface AccountRepository extends JpaRepository<Account, Long> {

    // JOIN FETCH để load account kèm roles trong 1 query
    @Query("SELECT a FROM Account a JOIN FETCH a.roles WHERE a.username = :username")
    Optional<Account> findByUsername(@Param("username") String username);
}
```

---

## Bước 8: Chuẩn Bị DB

```sql
-- Tạo roles
INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_TEACHER');
INSERT INTO role (name) VALUES ('ROLE_PARENT');
INSERT INTO role (name) VALUES ('ROLE_STUDENT');

-- Tạo account với password BCrypt của "123456"
INSERT INTO account (username, password, status)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpsl7iKVYDE6i', true);

-- Gán role
INSERT INTO account_role (account_id, role_id) VALUES (1, 1);
```

**Lưu ý quan trọng:** Password trong DB **phải là BCrypt hash**, không phải plain text.

Để tạo BCrypt hash:
```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
System.out.println(encoder.encode("123456"));
```

---

## Flow Hoạt Động

### Đăng Nhập
```
POST /api/auth/login
{ "username": "admin", "password": "123456" }
        ↓
AuthController.login()
        ↓
AuthenticationManager.authenticate()
  → UserDetailsServiceImpl.loadUserByUsername("admin")
  → Tìm account trong DB
  → BCrypt so sánh password
        ↓
Xác thực thành công
        ↓
JwtUtil.generateToken("admin")
  → Tạo token có chứa username + hết hạn 24h
        ↓
Trả về: { "token": "eyJ...", "username": "admin", "role": "ROLE_ADMIN" }
```

### Gọi API Với Token
```
GET /api/students
Header: Authorization: Bearer eyJ...
        ↓
JwtFilter.doFilterInternal()
  → Đọc token từ header
  → isTokenValid() → true
  → extractUsername() → "admin"
  → loadUserByUsername("admin") → UserDetails + roles
  → Set Authentication vào SecurityContext
        ↓
Spring Security kiểm tra quyền
  → hasRole("ADMIN") → cho phép
        ↓
Controller xử lý và trả về response
```

### Token Không Hợp Lệ
```
Header: Authorization: Bearer token_sai
        ↓
JwtFilter → isTokenValid() → false
        ↓
Không set Authentication
        ↓
Spring Security → 401 Unauthorized
```

---

## Các Lỗi Thường Gặp

| Lỗi | Nguyên nhân | Cách sửa |
|-----|-------------|----------|
| `Bad credentials` | Password không phải BCrypt | Update password thành BCrypt hash |
| `401 Unauthorized` | Không có token hoặc token sai | Thêm `Authorization: Bearer <token>` vào header |
| `403 Forbidden` | Không đủ quyền | Kiểm tra role của account |
| `JWT expired` | Token hết hạn | Đăng nhập lại lấy token mới |
| `UsernameNotFoundException` | Username không tồn tại trong DB | Kiểm tra lại username |

---

## Checklist Implement JWT

- [ ] Thêm dependency jjwt vào pom.xml
- [ ] Thêm jwt.secret và jwt.expiration vào application.properties
- [ ] Tạo `JwtUtil.java` - generateToken, extractUsername, isTokenValid
- [ ] Tạo `UserDetailsServiceImpl.java` - loadUserByUsername
- [ ] Tạo `JwtFilter.java` - doFilterInternal
- [ ] Tạo `SecurityConfig.java` - filterChain, authenticationManager, passwordEncoder
- [ ] Tạo `AuthController.java` - POST /api/auth/login
- [ ] Tạo `AccountRepository.java` - findByUsername với JOIN FETCH roles
- [ ] Insert dữ liệu DB với password BCrypt
- [ ] Test đăng nhập lấy token
- [ ] Test gọi API với token
