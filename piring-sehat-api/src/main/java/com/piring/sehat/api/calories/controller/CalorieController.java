package com.piring.sehat.api.calories.controller;

import com.piring.sehat.api.auth.dto.ApiResponse;
import com.piring.sehat.api.calories.dto.CalorieRequest;
import com.piring.sehat.api.calories.dto.CalorieResponse;
import com.piring.sehat.api.calories.service.CalorieService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calories")
public class CalorieController {

    private final CalorieService calorieService;

    // Dependency Injection
    public CalorieController(CalorieService calorieService) {
        this.calorieService = calorieService;
    }

    /**
     * Menyimpan log kalori baru.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CalorieResponse>> addCalorieLog(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CalorieRequest request) {
        
        CalorieResponse response = calorieService.addCalorieLog(jwt, request);
        return ResponseEntity.ok(ApiResponse.success("Log kalori berhasil ditambahkan", response));
    }

    /**
     * Mengambil daftar log kalori pada tanggal tertentu.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CalorieResponse>>> getLogsByDate(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<CalorieResponse> logs = calorieService.getCalorieLogsByDate(jwt, date);
        return ResponseEntity.ok(ApiResponse.success("Log kalori berhasil diambil", logs));
    }

    /**
     * Mengambil daftar tanggal yang memiliki log kalori pada bulan dan tahun tertentu.
     */
    @GetMapping("/active-dates")
    public ResponseEntity<ApiResponse<List<LocalDate>>> getActiveDatesByMonth(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        
        List<LocalDate> activeDates = calorieService.getActiveDatesByMonth(jwt, year, month);
        return ResponseEntity.ok(ApiResponse.success("Tanggal aktif berhasil diambil", activeDates));
    }

    /**
     * Menghapus log kalori berdasarkan ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCalorieLog(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        
        try {
            calorieService.deleteCalorieLog(jwt, id);
            return ResponseEntity.ok(ApiResponse.success("Log kalori berhasil dihapus", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
