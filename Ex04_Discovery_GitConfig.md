# Bài Tập 4 — Tạo Discovery Server & chuyển Config Server sang Git backend

## Hệ thống: **MediCare** — Nền tảng quản lý bệnh án

MediCare gồm 5 microservice + 2 hạ tầng:

| Thành phần | Tên dự án | Port | Vai trò |
|-----------|-----------|------|---------|
| Config Server | `config-server` | 8888 | Quản lý cấu hình tập trung từ Git |
| Discovery Server | `discovery-server` | 8761 | Eureka Server — đăng ký/khám phá service |
| Patient Service | `patient-service` | 8081 | Quản lý bệnh nhân |
| Doctor Service | `doctor-service` | 8082 | Quản lý bác sĩ |
| Appointment Service | `appointment-service` | 8083 | Quản lý lịch khám |
| Medical Record Service | `medical-record-service` | 8084 | Quản lý hồ sơ bệnh án |
| Pharmacy Service | `pharmacy-service` | 8085 | Quản lý kê đơn thuốc |

---

## 1. GitHub repository — `medicare-config-repo`

### Cấu trúc thư mục

```
medicare-config-repo/            (repo trên GitHub)
├── application.yml              # cấu hình chung
├── patient-service.yml
├── doctor-service.yml
├── appointment-service.yml
├── medical-record-service.yml
└── pharmacy-service.yml
```

### 1.1. `application.yml` (chung)

```yaml
# Cấu hình chung áp dụng cho tất cả service
spring:
  zipkin:
    base-url: http://localhost:9411
eureka:
  client:
    service-url:
      defaultZone: http://discovery-server:8761/eureka/
```

### 1.2. `patient-service.yml`

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medicare_patient_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

# Cấu hình Eureka Client
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/  # URL của Discovery Server
  instance:
    prefer-ip-address: true
```

### 1.3. `doctor-service.yml`

```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medicare_doctor_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

### 1.4. `appointment-service.yml`

```yaml
server:
  port: 8083

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medicare_appointment_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

### 1.5. `medical-record-service.yml`

```yaml
server:
  port: 8084

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medicare_medical_record_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

### 1.6. `pharmacy-service.yml`

```yaml
server:
  port: 8085

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medicare_pharmacy_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

---

## 2. Chuyển Config Server sang Git backend

### `config-server/src/main/resources/application.yml`

```yaml
server:
  port: 8888

spring:
  application:
    name: config-server

  cloud:
    config:
      server:
        git:
          uri: https://github.com/<your-username>/medicare-config-repo
          default-label: main
          clone-on-start: true
          skip-ssl-validation: true
      fail-fast: true

# Config Server cũng đăng ký lên Eureka để dễ quản lý
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

> Đã xóa thư mục `config-repo/` trong `resources/` — không còn dùng native profile.

---

## 3. Discovery Server (Eureka Server)

### `discovery-server/src/main/java/com/medicare/discoveryserver/DiscoveryServerApplication.java`

```java
package com.medicare.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer    // Kích hoạt Eureka Server
public class DiscoveryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
```

### `discovery-server/src/main/resources/application.yml`

```yaml
server:
  port: 8761

spring:
  application:
    name: discovery-server

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false     # Server không tự đăng ký
    fetch-registry: false           # Server không cần fetch
  server:
    enable-self-preservation: true
```

### `discovery-server/build.gradle`

```gradle
plugins {
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
    id 'java'
}

group = 'com.medicare'
version = '1.0.0'
sourceCompatibility = '17'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:2023.0.0"
    }
}
```

---

## 4. Cấu hình Eureka Client cho 5 microservice

### 4.1. Mỗi microservice cần 2 dependency mới trong `build.gradle`

```gradle
dependencies {
    // ... dependencies cũ ...

    // Config Client — lấy config từ Config Server
    implementation 'org.springframework.cloud:spring-cloud-starter-config'

    // Eureka Client — đăng ký lên Discovery Server
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```

### 4.2. `bootstrap.yml` cho từng microservice

```yaml
# bootstrap.yml của mỗi service
spring:
  application:
    name: patient-service     # đổi thành tên service tương ứng
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
      retry:
        max-attempts: 10
        multiplier: 1.5
```

### 4.3. Annotation `@EnableDiscoveryClient`

```java
@SpringBootApplication
@EnableDiscoveryClient   // Đăng ký service lên Eureka
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
```

---

## 5. Luồng khởi động đúng thứ tự

```
Bước 1: [Config Server]      →  http://localhost:8888
         - Khởi động đầu tiên vì mọi service phụ thuộc lấy config từ đây

Bước 2: [Discovery Server]   →  http://localhost:8761
         - Được config riêng (không phụ thuộc Config Server nếu dùng bootstrap)
         - Hoặc có thể để Config Server cũng fetch từ chính mình

Bước 3: [5 Microservice]     →  8081, 8082, 8083, 8084, 8085
         - Mỗi service fetch config từ Config Server
         - Đăng ký chính mình lên Discovery Server
```

> 🔥 **Lưu ý quan trọng**: Config Server và Discovery Server nên khởi động **trước** các microservice vì chúng cung cấp thông tin kết nối.

---

## 6. Kiểm thử (Testing)

### 6.1. Config Server lấy config từ Git

Mở trình duyệt truy cập:
- `http://localhost:8888/patient-service/default` → trả về nội dung config từ GitHub repo
- `http://localhost:8888/appointment-service/default` → tương tự

### 6.2. Eureka Dashboard

Truy cập `http://localhost:8761` → trang dashboard hiển thị:

```
Instances:
  PATIENT-SERVICE   (1 instance)
  DOCTOR-SERVICE    (1 instance)
  APPOINTMENT-SERVICE (1 instance)
  MEDICAL-RECORD-SERVICE (1 instance)
  PHARMACY-SERVICE  (1 instance)
  CONFIG-SERVER     (1 instance)
```

### 6.3. Test CRUD trên Postman

- POST/GET/PUT/DELETE trên mỗi service vẫn hoạt động bình thường
- Các service giao tiếp qua tên service thông qua Eureka (không hardcode URL)

---

## 7. README.md

```markdown
# MediCare Microservice System

## Prerequisites
- Java 17+
- Gradle 8+
- MySQL 8+
- Git
- Tài khoản GitHub

## Tạo database
```sql
CREATE DATABASE medicare_patient_db;
CREATE DATABASE medicare_doctor_db;
CREATE DATABASE medicare_appointment_db;
CREATE DATABASE medicare_medical_record_db;
CREATE DATABASE medicare_pharmacy_db;
```

## Thứ tự khởi động
1. **Config Server** (`cd config-server && ./gradlew bootRun`)
2. **Discovery Server** (`cd discovery-server && ./gradlew bootRun`)
3. **5 Microservice** (chạy đồng thời)

## Truy cập
- Config Server: http://localhost:8888
- Eureka Dashboard: http://localhost:8761
- Patient API: http://localhost:8081/api/patients
- Doctor API: http://localhost:8082/api/doctors
- Appointment API: http://localhost:8083/api/appointments
- Medical Record API: http://localhost:8084/api/medical-records
- Pharmacy API: http://localhost:8085/api/pharmacy
```

---

## 8. Kiến trúc tổng thể

```
  Developers
      │ push configs
      ▼
┌──────────────────┐
│ medicare-config  │  (GitHub repo)
│ -repo            │  patient-service.yml, doctor-service.yml, ...
└────────┬─────────┘
         │ clone
         ▼
┌──────────────────────────────┐
│        CONFIG SERVER (:8888)  │
│  Spring Cloud Config (Git)   │
└────────┬───────┬──────┬──────┬─┘
         │ config │ config │ config │ config
         ▼        │        │        │
┌──────────────────────────┐ │
│   DISCOVERY SERVER      │ ◄─┘
│   Eureka Server (:8761) │
└────────┬────┬────┬─────┬─┘
         │reg │reg │reg │reg
         ▼    ▼    ▼    ▼

┌────────────┐ ┌────────────┐ ┌──────────────────┐ ┌──────────────────┐ ┌────────────┐
│Patient-Svc │ │Doctor-Svc  │ │Appointment-Svc   │ │MedicalRecord-Svc │ │Pharmacy-Svc│
│(:8081)     │ │(:8082)     │ │(:8083)           │ │(:8084)           │ │(:8085)     │
└────────────┘ └────────────┘ └──────────────────┘ └──────────────────┘ └────────────┘
```

---

## 9. Tổng kết

| Thành phần | Trạng thái |
|------------|-----------|
| GitHub repo `medicare-config-repo` | ✅ 5 file config + application.yml |
| Config Server (Git backend) | ✅ `spring.cloud.config.server.git.uri` |
| Discovery Server (Eureka) | ✅ `@EnableEurekaServer`, port 8761 |
| 5 microservice đăng ký Eureka | ✅ `@EnableDiscoveryClient`, bootstrap.yml |
| Eureka Dashboard | ✅ Hiển thị 5 service |
| Startup order | ✅ Config → Discovery → Services |
