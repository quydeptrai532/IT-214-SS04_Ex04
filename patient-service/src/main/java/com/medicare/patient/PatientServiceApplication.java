package com.medicare.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Patient Service — quản lý thông tin bệnh nhân.
 * Đăng ký lên Eureka để các service khác gọi qua tên "patient-service".
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
