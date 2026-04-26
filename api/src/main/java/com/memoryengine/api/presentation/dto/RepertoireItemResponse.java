package com.memoryengine.api.presentation.dto;

import java.time.LocalDate;

// dış dünyaya göndereceğimiz güvenli (kısıtlanmış) veri tipi 

public record RepertoireItemResponse(

    Long id,
    String title,
    String category,
    LocalDate nextReviewDate


) {}
