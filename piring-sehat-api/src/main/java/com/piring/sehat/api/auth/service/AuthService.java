package com.piring.sehat.api.auth.service;

import com.piring.sehat.api.auth.model.UserProfile;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * =====================================================================
 * PILAR OOP #2: ABSTRAKSI (ABSTRACTION)
 * + PRINSIP SOLID: DEPENDENCY INVERSION PRINCIPLE (DIP)
 * =====================================================================
 * 
 * Abstraksi adalah teknik menyembunyikan detail implementasi yang kompleks
 * dan hanya menampilkan fungsionalitas penting kepada pengguna kelas.
 * Di Java, abstraksi diwujudkan melalui 'interface' atau 'abstract class'.
 * 
 * PENERAPAN DI FILE INI:
 * 1. Interface ini mendefinisikan KONTRAK (apa yang harus dilakukan),
 *    tanpa menjelaskan BAGAIMANA caranya dilakukan.
 * 2. Kelas lain (seperti Controller) cukup tahu bahwa ada method
 *    'extractUserProfile()', tanpa perlu tahu cara kerjanya di dalam.
 * 
 * HUBUNGAN DENGAN PRINSIP SOLID - DIP:
 * Dependency Inversion Principle mengatakan: "Bergantunglah pada abstraksi,
 * bukan pada implementasi konkret."
 * → Controller kita nanti akan menggunakan tipe 'AuthService' (interface ini),
 *   BUKAN tipe 'AuthServiceImpl' (kelas konkretnya).
 * 
 * ANALOGI SEDERHANA:
 * Bayangkan remote TV. Anda cukup tahu tombol "Power" untuk menyalakan TV.
 * Anda TIDAK perlu tahu rangkaian elektronik di dalam remote tersebut.
 * Interface ini adalah "tombol-tombol di remote" — sederhana dan jelas.
 */
public interface AuthService {
    
    /**
     * Kontrak: Mengekstrak data pengguna dari token yang diterima.
     * 
     * @param jwt Token yang berisi informasi pengguna
     * @return UserProfile objek profil pengguna yang terenkapsulasi
     */
    UserProfile extractUserProfile(Jwt jwt);
}
