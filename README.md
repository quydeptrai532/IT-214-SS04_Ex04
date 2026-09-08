# MediCare Microservice System — Discovery Server & Config Server (Git backend)

## Tổng quan

Hệ thống MediCare gồm **2 hạ tầng** + **5 microservice**:

| Thành phần | Port | Mô tả |
|-----------|------|-------|
| `config-server` | 8888 | Cung cấp config tập trung qua GitHub repo |
| `discovery-server` | 8761 | Eureka Server — registry & discovery |
| `patient-service` | 8081 | CRUD bệnh nhân |
| `doctor-service` | 8082 | CRUD bác sĩ |
| `appointment-service` | 8083 | CRUD lịch khám + inter-service calls |
| `medical-record-service` | 8084 | CRUD hồ sơ bệnh án + gọi Appointment Service |
| `pharmacy-service` | 8085 | Quản lý tồn kho thuốc |

## Cấu trúc dự án

```
Session04/Ex04/
├── config-repo/                    # 5 file config YAML (push lên GitHub: medicare-config-repo)
│   ├── application.yml
│   ├── patient-service.yml
│   ├── doctor-service.yml
│   ├── appointment-service.yml
│   ├── medical-record-service.yml
│   └── pharmacy-service.yml
├── config-server/                  # dự án Gradle Config Server
│   ├── build.gradle
│   ├── settings.gradle
│   └── src/main/resources/application.yml
├── discovery-server/               # dự án Gradle Eureka Server
│   ├── build.gradle
│   ├── settings.gradle
│   ├── src/main/resources/application.yml
│   └── src/main/java/.../DiscoveryServerApplication.java
├── patient-service/                # 5 microservice Gradle projects
├── doctor-service/
├── appointment-service/
├── medical-record-service/
├── pharmacy-service/
├── README.md
└── screenshots/                    # ảnh Eureka Dashboard (do sinh viên chụp vào)
```

## GitHub repo cấu hình

Repository: `medicare-config-repo` (do sinh viên tạo riêng — tại đây chỉ có file template)

```
medicare-config-repo/
├── application.yml
├── patient-service.yml
├── doctor-service.yml
├── appointment-service.yml
├── medical-record-service.yml
└── pharmacy-service.yml
```

## Thứ tự khởi động

1. **Config Server** (port 8888)
   ```bash
   cd config-server
   ./gradlew bootRun
   ```
2. **Discovery Server** (port 8761)
   ```bash
   cd discovery-server
   ./gradlew bootRun
   ```
3. **5 Microservice** (chạy đồng thời)
   ```bash
   # patient-service
   cd patient-service && ./gradlew bootRun
   # doctor-service
   cd doctor-service && ./gradlew bootRun
   # appointment-service
   cd appointment-service && ./gradlew bootRun
   # medical-record-service
   cd medical-record-service && ./gradlew bootRun
   # pharmacy-service
   cd pharmacy-service && ./gradlew bootRun
   ```

## Truy cập

- Config Server: http://localhost:8888/patient-service/default
- Eureka Dashboard: http://localhost:8761
- Patient API: http://localhost:8081/api/patients
- Doctor API: http://localhost:8082/api/doctors
- Appointment API: http://localhost:8083/api/appointments
- Medical Record API: http://localhost:8084/api/medical-records
- Pharmacy API: http://localhost:8085/api/pharmacy

## Luồng khởi động (Startup Flow)

```
1. Config Server (8888)
   - Clone repo Git (medicare-config-repo)
   - Chuẩn bị cấu hình cho tất cả service

2. Discovery Server (8761)
   - Eureka Server khởi động, sẵn sàng nhận đăng ký

3. 5 Microservice (8081-8085)
   - Mỗi service fetch config từ Config Server
   - Đăng ký chính mình lên Discovery Server
   - Client khác gọi qua tên service (discovery-based routing)
```

## Bài tập liên quan

- **Ex05**: Giao tiếp inter-service bằng RestTemplate + @LoadBalanced (trong folder Ex05)
