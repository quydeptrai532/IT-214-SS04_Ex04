package com.medicare.doctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Doctor Service — quản lý thông tin bác sĩ.
 * Đăng ký lên Eureka để các service khác gọi qua tên "doctor-service".
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DoctorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorServiceApplication.class, args);
    }
}
