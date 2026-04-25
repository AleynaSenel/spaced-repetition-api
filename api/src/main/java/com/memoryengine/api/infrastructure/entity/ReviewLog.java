package com.memoryengine.api.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Review_Logs")
@Getter
@Setter
@NoArgsConstructor
public class ReviewLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FetchType.LAZY: Logu çektiğimizde parçayı hemen veritabanından getirme, sadece ihtiyaç olduğunda getir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repertoire_item_id") // Veritabanında ilişkiyi kuracak kolonun adı
    private RepertoireItem repertoireItem;

    private int quality; // Kullanıcının o anki tekrarına verdiği zorluk puanı (0-5)
    private LocalDateTime reviewDate; // Tekrarın yapıldığı an
}
