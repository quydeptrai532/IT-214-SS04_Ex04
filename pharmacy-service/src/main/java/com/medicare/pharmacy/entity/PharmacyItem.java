package com.medicare.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity PharmacyItem — quản lý tồn kho thuốc.
 */
@Entity
@Table(name = "pharmacy_items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer quantity;
    private Double price;
}
