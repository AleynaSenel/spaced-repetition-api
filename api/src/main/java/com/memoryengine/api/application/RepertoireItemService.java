package com.memoryengine.api.application;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.memoryengine.api.domain.ReviewState;
import com.memoryengine.api.domain.SpacedRepetitionAlgorithm;
import com.memoryengine.api.domain.SuperMemo2Calculator;
import com.memoryengine.api.exception.ItemNotFoundException;
import com.memoryengine.api.infrastructure.entity.RepertoireItem;
import com.memoryengine.api.infrastructure.entity.ReviewLog;
import com.memoryengine.api.infrastructure.repository.RepertoireItemRepository;
import com.memoryengine.api.presentation.dto.RepertoireItemRequest;
import com.memoryengine.api.presentation.dto.RepertoireItemResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor     //repositoryleri otomatik olarak bu sınıfa bağlar. 
public class RepertoireItemService {
    
    private final RepertoireItemRepository itemRepository;  

    private final  SpacedRepetitionAlgorithm algorithm = new SuperMemo2Calculator();  //içinde saf matematiksel kodlar olduğu için  spring bootttan bağımsız yaptık

    @Transactional  //Bu metodun içindeki tüm işlemler ya EKSİKSİZ gerçekleşir ya da en ufak bir hatada hepsi iptal edilip (Rollback) eski haline döner
    public RepertoireItemResponse addNewItem(RepertoireItemRequest request){


        // dışarıdan gelen request dtosunu , veritabanındaki entitye çevir
        RepertoireItem item = new RepertoireItem();
        item.setTitle(request.title());
        item.setCategory(request.category());


        //başlangıç algoritma değerlerini yükler
        ReviewState initialState = ReviewState.createInitialState();
        item.setRepetitions(initialState.repetitions());
        item.setEaseFactor(initialState.easeFactor());
        item.setIntervalDays(initialState.intervalDays());

        //yeni eklenen parça bugün çalışılmalı
        item.setNextReviewDate(LocalDate.now());
        
        RepertoireItem savedItem = itemRepository.save(item);

        // Güvenli bir şekilde Response DTO'suna çevirip dış dünyaya (Controller'a) dön
        return new RepertoireItemResponse(
                savedItem.getId(),
                savedItem.getTitle(),
                savedItem.getCategory(),
                savedItem.getNextReviewDate()
        );
    }

    /**
     * İş Akışı 2: Bir parçayı tekrar etmek (Hatırlama zorluğunu girerek algoritmayı çalıştırmak).
     */
    @Transactional
    public RepertoireItemResponse processReview(Long itemId, int quality) {
        // 1. Parçayı veritabanından bul. Eğer yoksa sistemi durdur ve hata fırlat.
        // (İleride bu hatayı "ItemNotFoundException" olarak özelleştireceğiz)
        RepertoireItem item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("ID: " + itemId + " numaralı öğrenme parçası sistemde bulunamadı!"));

        // 2. Veritabanındaki mevcut durumu, algoritmamızın anlayacağı saf 'ReviewState' nesnesine çevir
        ReviewState currentState = new ReviewState(item.getRepetitions(), item.getEaseFactor(), item.getIntervalDays());

        // 3. MATEMATİK ZAMANI! Çekirdek algoritmaya "quality" değerini ver ve yeni tarihi hesaplat
        ReviewState nextState = algorithm.calculateNexReview(currentState, quality);

        // 4. Algoritmanın bulduğu yeni sonuçları Entity'e aktar
        item.setRepetitions(nextState.repetitions());
        item.setEaseFactor(nextState.easeFactor());
        item.setIntervalDays(nextState.intervalDays());
        item.setLastReviewDate(LocalDateTime.now());
        
        // Sıradaki tekrar tarihi = Bugün + Hesaplanan Gün Sayısı
        item.setNextReviewDate(LocalDate.now().plusDays(nextState.intervalDays()));

        // 5. Tarihçe (Log) kaydı oluştur
        ReviewLog log = new ReviewLog();
        log.setRepertoireItem(item); // Bu log hangi parçaya ait? İlişkiyi kur.
        log.setQuality(quality);
        log.setReviewDate(LocalDateTime.now());

        // Logu parçanın listesine ekle
        item.getReviewLogs().add(log); 

        // 6. Veritabanına kaydet 
        // (CascadeType.ALL yaptığımız için item'i kaydetmek, içindeki yeni logu da otomatik kaydedecek)
        RepertoireItem savedItem = itemRepository.save(item);

        // 7. Dış dünyaya DTO olarak dön
        return new RepertoireItemResponse(
                savedItem.getId(),
                savedItem.getTitle(),
                savedItem.getCategory(),
                savedItem.getNextReviewDate()
        );
    }
}
