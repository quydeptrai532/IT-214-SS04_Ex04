package com.medicare.pharmacy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Pharmacy Service — quản lý kê đơn thuốc, tồn kho.
 * Đăng ký lên Eureka để các service khác gọi qua tên "pharmacy-service".
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PharmacyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PharmacyServiceApplication.class, args);
    }
}
