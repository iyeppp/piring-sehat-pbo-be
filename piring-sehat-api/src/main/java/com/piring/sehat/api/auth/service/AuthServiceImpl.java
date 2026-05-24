package com.piring.sehat.api.auth.service;

import com.piring.sehat.api.auth.model.UserProfile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * =====================================================================
 * PILAR OOP #3: POLIMORFISME (POLYMORPHISM)
 * + PRINSIP SOLID: SINGLE RESPONSIBILITY PRINCIPLE (SRP)
 * =====================================================================
 * 
 * Polimorfisme berarti "banyak bentuk". Dalam konteks OOP, artinya
 * satu interface bisa memiliki banyak implementasi yang berbeda-beda.
 * Kelas ini adalah SALAH SATU bentuk implementasi dari interface AuthService.
 * 
 * PENERAPAN DI KELAS INI:
 * 1. Menggunakan 'implements AuthService' → menunjukkan hubungan polimorfis
 * 2. Menggunakan '@Override' → menandai bahwa method ini mengimplementasikan
 *    kontrak dari interface induknya
 * 3. Jika suatu saat ingin mengganti cara kerja ekstraksi data (misalnya
 *    dari provider lain), cukup buat kelas implementasi baru tanpa
 *    mengubah interface atau controller yang sudah ada
 * 
 * PRINSIP SRP (Single Responsibility):
 * Kelas ini HANYA bertanggung jawab untuk satu hal:
 * → Mengekstrak dan mengolah data dari token menjadi objek UserProfile.
 * Tidak mengurusi routing HTTP, tidak mengurusi format respons API.
 * 
 * ANALOGI SEDERHANA:
 * Interface 'AuthService' adalah cetakan kue (kontrak bentuknya).
 * Kelas ini adalah kue yang sudah jadi (implementasi konkret).
 * Bisa dibuat kue lain dengan cetakan yang sama tapi rasa berbeda.
 */
@Service // Menandai kelas ini sebagai Service Bean yang dikelola Spring
public class AuthServiceImpl implements AuthService {

    /**
     * Implementasi konkret dari kontrak interface AuthService.
     * '@Override' menandakan ini adalah Polimorfisme — method ini
     * "menimpa" definisi abstrak di interface induknya.
     */
    @Override
    public UserProfile extractUserProfile(Jwt jwt) {
        // Keamanan: Cegah error jika token kosong
        if (jwt == null) {
            return null;
        }

        // 1. Ambil data dasar dari token (ID pengguna dan Email)
        String id = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        // 2. Ambil data tambahan dari metadata pengguna
        Map<String, Object> userMetadata = jwt.getClaim("user_metadata");
        
        String username = "";
        String fullName = "";
        String avatarUrl = "";

        if (userMetadata != null) {
            username = (String) userMetadata.get("username");
            fullName = (String) userMetadata.get("full_name");
            
            avatarUrl = (String) userMetadata.get("avatar_url");
            if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
                avatarUrl = (String) userMetadata.get("picture");
            }
        }

        // 3. Logika Fallback: Isi data cadangan jika ada yang kosong
        if (username == null || username.trim().isEmpty()) {
            username = (email != null) ? email.split("@")[0] : "user";
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            fullName = username;
        }

        // 4. Buat dan kembalikan objek Model yang terenkapsulasi (Pilar #1: Enkapsulasi)
        return new UserProfile(id, email, username, fullName, avatarUrl);
    }
}
