package com.piring.sehat.api.auth.controller;

import com.piring.sehat.api.auth.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * PublicController menyediakan endpoint yang TIDAK memerlukan autentikasi (Public API).
 * Endpoint ini bisa diakses oleh siapa saja tanpa perlu mengirimkan token JWT.
 * 
 * Berguna untuk mengecek apakah backend sudah berjalan dengan benar sebelum login.
 * Menggunakan DTO ApiResponse untuk respons API yang seragam dan konsisten.
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    /**
     * Endpoint Health Check: Mengecek apakah server backend aktif dan sehat.
     * 
     * Rute: GET /api/public/health
     * Hak Akses: Publik (Tidak perlu login)
     */
    @GetMapping("/health")
    public ApiResponse<Map<String, String>> healthCheck() {
        Map<String, String> info = Map.of(
            "application", "Piring Sehat Backend",
            "version", "1.0.0",
            "framework", "Spring Boot 4.0.6"
        );
        return ApiResponse.success("Backend Spring Boot berjalan dengan baik!", info);
    }
}
