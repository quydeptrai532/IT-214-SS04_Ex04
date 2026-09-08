package com.medicare.doctor.controller;

import com.medicare.doctor.dto.DoctorDto;
import com.medicare.doctor.entity.Doctor;
import com.medicare.doctor.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API quản lý bác sĩ.
 * Dùng cho cả việc appointment-service gọi sang kiểm tra và lấy thông tin.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto dto) {
        Doctor doctor = modelMapper.map(dto, Doctor.class);
        Doctor saved = doctorRepository.save(doctor);
        return ResponseEntity.ok(modelMapper.map(saved, DoctorDto.class));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> list = doctorRepository.findAll()
                .stream()
                .map(d -> modelMapper.map(d, DoctorDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(d -> ResponseEntity.ok(modelMapper.map(d, DoctorDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long id, @RequestBody DoctorDto dto) {
        return doctorRepository.findById(id)
                .map(d -> {
                    d.setName(dto.getName());
                    d.setSpecialty(dto.getSpecialty());
                    d.setEmail(dto.getEmail());
                    d.setPhone(dto.getPhone());
                    Doctor saved = doctorRepository.save(d);
                    return ResponseEntity.ok(modelMapper.map(saved, DoctorDto.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
