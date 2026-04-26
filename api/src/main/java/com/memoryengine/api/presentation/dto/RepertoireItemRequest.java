package com.memoryengine.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Kullanıcıdan yeni bir parça eklemesi istendiğinde kabul edeceğimiz veri formatı.
 */
public record RepertoireItemRequest(
    @NotBlank(message = "Parça adı boş olamaz")  // spring boot initialzr da "validation" bağımlılığı 
    String title, 
    
    @NotBlank(message = "Kategori boş olamaz") 
    String category
) {}