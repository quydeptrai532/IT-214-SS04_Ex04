package com.medicare.pharmacy.repository;

import com.medicare.pharmacy.entity.PharmacyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyItemRepository extends JpaRepository<PharmacyItem, Long> {
}
