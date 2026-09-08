package com.medicare.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Appointment Service — quản lý đặt lịch khám.
 * Giao tiếp với Patient Service & Doctor Service qua RestTemplate + Eureka discovery.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AppointmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }
}
