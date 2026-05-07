package com.memoryengine.api.application;

import com.memoryengine.api.infrastructure.entity.RepertoireItem;
import com.memoryengine.api.infrastructure.repository.RepertoireItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Lombok'un bize konsola şık ve renkli yazılar (Log) yazdırmasını sağlayan aracı
public class ReviewReminderJob {

    private final RepertoireItemRepository repository;

    
    @Async // Ana sistemi yavaşlatmadan, kendi ayrı paralel yolunda çalıştırır
    @Scheduled(fixedRate = 10000) 
    public void checkItemsForReview() {
        log.info("--- ZAMANLAYICI UYANDI ---");
        log.info("Bugün tekrar edilmesi gereken çalışmalar aranıyor...");

        // Veritabanına gidip tarihi 'Bugün' veya 'Bugünden Önce' olanları çekiyoruz
        List<RepertoireItem> itemsToReview = repository.findByNextReviewDateLessThanEqual(LocalDate.now());

        if (itemsToReview.isEmpty()) {
            log.info("Bugün tekrar edilecek çalışma yok. Özgürsün!");
            log.info("--------------------------");
            return;
        }

        log.info("Bugün tam {} adet çalışmayı tekrar etmen gerekiyor:", itemsToReview.size());
        
        for (RepertoireItem item : itemsToReview) {
            log.info("-> Çalışma: {} | Kategori: {}", item.getTitle(), item.getCategory());
        }
        log.info("--------------------------");
    }
}