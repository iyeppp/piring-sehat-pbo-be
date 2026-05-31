package com.piring.sehat.api.calories.repository;

import com.piring.sehat.api.calories.model.CalorieLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CalorieLogRepository extends JpaRepository<CalorieLog, UUID> {
    
    // Pencarian otomatis (Derived Query) berdasarkan ID User dan Tanggal
    List<CalorieLog> findByUserIdAndEntryDateOrderByCreatedAtAsc(UUID userId, LocalDate entryDate);
    
    // Mencari log spesifik milik user tertentu (untuk validasi sebelum delete)
    CalorieLog findByIdAndUserId(UUID id, UUID userId);

    // Custom query untuk mengambil tanggal-tanggal unik yang memiliki data pada bulan dan tahun tertentu
    @Query("SELECT DISTINCT c.entryDate FROM CalorieLog c WHERE c.userId = :userId AND EXTRACT(YEAR FROM c.entryDate) = :year AND EXTRACT(MONTH FROM c.entryDate) = :month")
    List<LocalDate> findDistinctEntryDatesByUserIdAndMonth(@Param("userId") UUID userId, @Param("year") int year, @Param("month") int month);
}
