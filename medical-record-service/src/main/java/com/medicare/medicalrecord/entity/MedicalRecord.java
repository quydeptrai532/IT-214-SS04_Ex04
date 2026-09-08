package com.medicare.medicalrecord.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity MedicalRecord — nằm trong medical-record-service, database riêng.
 * appointmentId chỉ là reference ID (KHÔNG phải FK) vì bảng appointments
 * nằm ở database khác — Database-per-service.
 */
@Entity
@Table(name = "medical_records")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long appointmentId;   // reference → appointments.id (appointment-service)

    private String diagnosis;
    private String treatment;
    private String prescription;

    private LocalDateTime createdAt;
}
