package com.medicare.pharmacy.controller;

import com.medicare.pharmacy.entity.PharmacyItem;
import com.medicare.pharmacy.repository.PharmacyItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API quản lý tồn kho thuốc (placeholder).
 */
@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyItemRepository pharmacyItemRepository;

    @GetMapping
    public ResponseEntity<List<PharmacyItem>> getAllItems() {
        return ResponseEntity.ok(pharmacyItemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyItem> getItemById(@PathVariable Long id) {
        return pharmacyItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
