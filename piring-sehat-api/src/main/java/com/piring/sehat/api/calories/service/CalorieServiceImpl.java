package com.piring.sehat.api.calories.service;

import com.piring.sehat.api.calories.dto.CalorieRequest;
import com.piring.sehat.api.calories.dto.CalorieResponse;
import com.piring.sehat.api.calories.model.CalorieLog;
import com.piring.sehat.api.calories.repository.CalorieLogRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * =====================================================================
 * PILAR OOP #3: POLIMORFISME (via Implementasi Interface)
 * =====================================================================
 * Kelas ini adalah wujud nyata (implementasi) dari abstraksi CalorieService.
 */
@Service
public class CalorieServiceImpl implements CalorieService {

    private final CalorieLogRepository repository;

    // Dependency Injection via Constructor
    public CalorieServiceImpl(CalorieLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public CalorieResponse addCalorieLog(Jwt jwt, CalorieRequest request) {
        // Ambil UUID dari subject token JWT Supabase
        UUID userId = UUID.fromString(jwt.getSubject());

        CalorieLog log = new CalorieLog();
        log.setUserId(userId);
        log.setName(request.getName());
        log.setCalories(request.getCalories());
        log.setProtein(request.getProtein());
        log.setCarbs(request.getCarbs());
        log.setFat(request.getFat());
        log.setMealType(request.getMealType());
        log.setEntryDate(request.getEntryDate());
        log.setCreatedAt(Instant.now());

        CalorieLog savedLog = repository.save(log);

        return mapToResponse(savedLog);
    }

    @Override
    public List<CalorieResponse> getCalorieLogsByDate(Jwt jwt, LocalDate date) {
        UUID userId = UUID.fromString(jwt.getSubject());
        
        List<CalorieLog> logs = repository.findByUserIdAndEntryDateOrderByCreatedAtAsc(userId, date);
        
        return logs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCalorieLog(Jwt jwt, UUID logId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        
        // Memastikan log yang dihapus benar-benar milik user yang sedang login
        CalorieLog log = repository.findByIdAndUserId(logId, userId);
        if (log != null) {
            repository.delete(log);
        } else {
            throw new IllegalArgumentException("Log tidak ditemukan atau Anda tidak memiliki akses.");
        }
    }

    // Helper method untuk memetakan Model -> DTO
    private CalorieResponse mapToResponse(CalorieLog log) {
        // Format Instant ke bentuk jam (HH:mm:ss) untuk Frontend
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                                        .withZone(ZoneId.systemDefault());
        String formattedTime = formatter.format(log.getCreatedAt());

        return new CalorieResponse(
                log.getId(),
                log.getName(),
                log.getCalories(),
                log.getProtein(),
                log.getCarbs(),
                log.getFat(),
                log.getMealType(),
                log.getEntryDate(),
                formattedTime
        );
    }
}
