package com.piring.sehat.api.auth.controller;

import com.piring.sehat.api.auth.dto.ApiResponse;
import com.piring.sehat.api.auth.dto.UserProfileResponse;
import com.piring.sehat.api.auth.model.UserProfile;
import com.piring.sehat.api.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * =====================================================================
 * PRINSIP SOLID: DEPENDENCY INVERSION (DIP) & SINGLE RESPONSIBILITY (SRP)
 * =====================================================================
 * AuthController bertindak sebagai pengontrol lalu lintas rute HTTP API (Presenter Layer).
 * 
 * Sesuai prinsip OOP & SOLID:
 * 1. Single Responsibility (SRP): Kelas ini HANYA bertanggung jawab untuk menangani 
 *    HTTP request-response (routing / endpoint), tidak mengurusi cara membongkar token.
 * 2. Dependency Inversion (DIP): Kelas ini bergantung pada Interface Abstraksi 'AuthService',
 *    bukan kelas implementasinya secara langsung.
 * 3. Constructor Injection: Menggunakan konstruktor untuk melakukan injeksi dependensi,
 *    membuat kode sangat mudah ditest dan dipelihara.
 * 4. Pola DTO: Controller mengonversi Model domain → DTO sebelum mengirim respons.
 *    Ini memisahkan representasi data internal dari data yang dikirim ke klien.
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    // Dependensi bertipe Interface Abstraksi (Bukan kelas konkret)
    private final AuthService authService;

    // Injeksi Dependensi melalui Konstruktor (OOP & Spring Best Practice)
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint untuk memeriksa sesi JWT user aktif dan mengembalikan data profil
     * dalam format DTO (Data Transfer Object) yang terstandarisasi.
     * 
     * Rute: GET /api/auth/me
     * Hak Akses: Terproteksi (Harus login & menyertakan token JWT valid)
     * 
     * ALUR DATA:
     * 1. Spring Security memvalidasi JWT dan menyuntikkan objek Jwt yang terverifikasi
     * 2. AuthService mengekstrak JWT → Model domain (UserProfile)
     * 3. Controller mengonversi Model → DTO (UserProfileResponse)
     * 4. DTO dibungkus dalam ApiResponse generik lalu dikirim ke klien sebagai JSON
     */
    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe(@AuthenticationPrincipal Jwt jwt) {
        
        // Langkah 1: Delegasikan ekstraksi token kepada Service Layer (Pilar Abstraksi & SRP)
        UserProfile profile = authService.extractUserProfile(jwt);
        
        // Langkah 2: Validasi kelayakan objek profil
        if (profile == null) {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("Gagal mengekstrak profil dari token JWT."));
        }
        
        // Langkah 3: Konversi Model → DTO (Data Transfer Object)
        // DTO menambahkan field tambahan seperti 'timestamp' dan 'status' yang
        // TIDAK ada di Model domain asli — membuktikan manfaat pemisahan ini.
        UserProfileResponse dto = new UserProfileResponse(
            profile.getId(),
            profile.getEmail(),
            profile.getUsername(),
            profile.getFullName(),
            profile.getAvatarUrl(),
            profile.getRole(),
            LocalDateTime.now().toString(),     // Metadata waktu respons
            "JWT_VERIFIED"                       // Status verifikasi
        );
        
        // Langkah 4: Bungkus DTO dalam ApiResponse generik dan kirim (HTTP 200 OK)
        return ResponseEntity.ok(ApiResponse.success("Profil berhasil dimuat.", dto));
    }
}
