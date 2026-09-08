package com.medicare.patient.controller;

import com.medicare.patient.dto.PatientDto;
import com.medicare.patient.entity.Patient;
import com.medicare.patient.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API quản lý bệnh nhân.
 * Dùng cho cả việc appointment-service gọi sang kiểm tra và lấy thông tin.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto dto) {
        Patient patient = modelMapper.map(dto, Patient.class);
        Patient saved = patientRepository.save(patient);
        return ResponseEntity.ok(modelMapper.map(saved, PatientDto.class));
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> list = patientRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PatientDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(p -> ResponseEntity.ok(modelMapper.map(p, PatientDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @RequestBody PatientDto dto) {
        return patientRepository.findById(id)
                .map(p -> {
                    p.setName(dto.getName());
                    p.setPhone(dto.getPhone());
                    p.setEmail(dto.getEmail());
                    Patient saved = patientRepository.save(p);
                    return ResponseEntity.ok(modelMapper.map(saved, PatientDto.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
