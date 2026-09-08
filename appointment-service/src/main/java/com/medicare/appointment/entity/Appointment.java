package com.medicare.appointment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity Appointment — nằm trong appointment-service, database riêng.
 * Các trường patientId / doctorId chỉ là reference ID (KHÔNG phải FK
 * vì bảng patients/doctors nằm ở database khác — Database-per-service).
 */
@Entity
@Table(name = "appointments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;       // reference → patients.id (patient-service)
    private Long doctorId;        // reference → doctors.id (doctor-service)
    private Long medicalRecordId; // reference → medical_records.id (medical-record-service)

    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private Status status;        // PENDING, CONFIRMED, COMPLETED, CANCELLED

    public enum Status {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }
}
