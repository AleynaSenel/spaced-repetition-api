package com.memoryengine.api.infrastructure.repository;

import com.memoryengine.api.infrastructure.entity.ReviewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLogRepository extends JpaRepository<ReviewLog, Long> {
   
}