package com.piring.sehat.api.calories.service;

import com.piring.sehat.api.calories.dto.CalorieRequest;
import com.piring.sehat.api.calories.dto.CalorieResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #4: ABSTRAKSI
 * =====================================================================
 * Interface ini mendefinisikan "Apa" yang bisa dilakukan oleh Service,
 * tanpa mengekspos "Bagaimana" cara kerjanya. Controller hanya akan 
 * berinteraksi dengan Interface ini.
 */
public interface CalorieService {
    
    CalorieResponse addCalorieLog(Jwt jwt, CalorieRequest request);
    
    List<CalorieResponse> getCalorieLogsByDate(Jwt jwt, LocalDate date);
    
    void deleteCalorieLog(Jwt jwt, UUID logId);
}
