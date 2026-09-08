package com.medicare.medicalrecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Medical Record Service — quản lý hồ sơ bệnh án.
 * Gọi sang Appointment Service để kiểm tra appointment COMPLETED trước khi tạo record.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MedicalRecordServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalRecordServiceApplication.class, args);
    }
}
