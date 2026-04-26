package com.memoryengine.api.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.memoryengine.api.infrastructure.entity.RepertoireItem;

@Repository
public interface RepertoireItemRepository extends JpaRepository<RepertoireItem, Long> {
    
    // "Derived Query Methods" (Türetilmiş Sorgular)
    // Spring bu metodun ismini okuyup otomatik olarak şu SQL'i yazacak:
    // SELECT * FROM repertoire_items WHERE next_review_date <= ?
    List<RepertoireItem> findByNextReviewDateLessThanEqual(LocalDate date);
}