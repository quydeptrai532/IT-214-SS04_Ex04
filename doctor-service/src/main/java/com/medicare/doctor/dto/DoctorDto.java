package com.medicare.doctor.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private Long id;
    private String name;
    private String specialty;
    private String email;
    private String phone;
}
