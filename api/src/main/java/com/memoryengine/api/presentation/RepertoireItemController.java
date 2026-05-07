package com.memoryengine.api.presentation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memoryengine.api.application.RepertoireItemService;
import com.memoryengine.api.presentation.dto.RepertoireItemRequest;
import com.memoryengine.api.presentation.dto.RepertoireItemResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController //dışaroya açılan kapı ve veriler JSON formatonda
@RequestMapping("/api/v1/items")    //dışarıdan bu adrese istek atılacak
@RequiredArgsConstructor
public class RepertoireItemController {

    private final RepertoireItemService itemService;

    @PostMapping   //bir veri eklemek
    public ResponseEntity<RepertoireItemResponse> addNewItem(@Valid @RequestBody RepertoireItemRequest request){
        RepertoireItemResponse response = itemService.addNewItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/review")   //veriyi güncellemek için
    public ResponseEntity<RepertoireItemResponse> reviewItem(
            @PathVariable Long id,
            @RequestParam int quality) {
        
        // Servisi çağırıp SM-2 algoritmasını çalıştırıyoruz ve yeni tarihi alıyoruz
        RepertoireItemResponse response = itemService.processReview(id, quality);
        return ResponseEntity.ok(response); // 200 (OK/Başarılı) koduyla dön
    }
    
    
}
