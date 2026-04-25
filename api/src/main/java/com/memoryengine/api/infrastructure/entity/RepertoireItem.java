package com.memoryengine.api.infrastructure.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity    //BU sınıfın bir veritabnı olduğunu Spring Boot a söyler
@Table(name = "Repertoire_Item")  //tablonun ismi
@Getter  //
@Setter
@NoArgsConstructor
public class RepertoireItem {
    
    @Id   //bu değişken tablonun primary key 'i 
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //id nin 1,2,3 diye otomatik artmasını sağlar. 
    private Long id;

    private String title;
    private String category;

    private int repetitions;
    private double easeFactor;
    private int intervalDays;

    private LocalDate nextReviewDate;   //bir sonraki tekrarın yapılacağı tarih
    private LocalDateTime lastReviewDate;   //en son yapılan tekrarın tarihi ve saati 

    // cascade = CascadeType.ALL: Parça silinirse, ona ait tüm geçmiş logları da silinsin.
    // orphanRemoval = true: Boşta kalan logları veritabanından temizle.
    @OneToMany(mappedBy = "repertoireItem", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLog> reviewLogs = new ArrayList<>();
}
