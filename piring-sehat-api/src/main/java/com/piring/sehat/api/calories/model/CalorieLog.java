package com.piring.sehat.api.calories.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #1: ENKAPSULASI
 * =====================================================================
 * Entitas ini merepresentasikan tabel 'calorie_logs' di database.
 * 
 * Penggunaan anotasi @Data (dari Lombok) akan secara otomatis men-generate
 * getter dan setter untuk field-field private, memastikan Enkapsulasi tetap terjaga
 * tanpa menulis boilerplate code yang panjang.
 */
@Entity
@Table(name = "calorie_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalorieLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()")
    private UUID id;

    // Menyimpan ID pengguna dari tabel user_profiles / auth.users Supabase
    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double calories;

    @Column(columnDefinition = "numeric default 0")
    private Double protein;

    @Column(columnDefinition = "numeric default 0")
    private Double carbs;

    @Column(columnDefinition = "numeric default 0")
    private Double fat;

    @Column(name = "meal_type", nullable = false)
    private String mealType;

    // Menggunakan LocalDate agar mudah difilter berdasarkan tanggal di Kalender
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "created_at", nullable = false, updatable = false, 
            columnDefinition = "timestamptz DEFAULT now()")
    private Instant createdAt = Instant.now();
}

