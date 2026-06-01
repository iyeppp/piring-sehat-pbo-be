package com.piring.sehat.api.auth.model;

/**
 * =====================================================================
 * PILAR OOP #1: ENKAPSULASI (ENCAPSULATION)
 * =====================================================================
 * 
 * Enkapsulasi adalah teknik membungkus data (variabel) dan metode 
 * yang mengakses data tersebut ke dalam satu unit kelas, lalu 
 * membatasi akses langsung dari luar menggunakan access modifier.
 * 
 * PENERAPAN DI KELAS INI:
 * 1. Semua variabel menggunakan 'private' → tidak bisa diakses langsung dari luar
 * 2. Menggunakan 'final' → nilai tidak bisa diubah setelah objek dibuat (Immutable)
 * 3. Menyediakan 'getter' publik → akses baca yang terkontrol
 * 4. TIDAK ada 'setter' → data tidak bisa dimodifikasi sembarangan
 * 
 * ANALOGI SEDERHANA:
 * Bayangkan KTP. Data di KTP (nama, alamat, dll) tidak bisa diubah 
 * sembarangan oleh orang lain. Anda hanya bisa MEMBACA informasinya,
 * tapi untuk mengubahnya harus lewat prosedur resmi (Disdukcapil).
 */
public class UserProfile {
    
    // ─── Variabel Private (Enkapsulasi: Akses Dibatasi) ───
    private final String id;        // ID unik pengguna
    private final String email;     // Alamat email pengguna
    private final String username;  // Nama pengguna
    private final String fullName;  // Nama lengkap
    private final String avatarUrl; // URL foto profil
    private final String role;      // Role dari database (moderator / dll)

    /**
     * Konstruktor: Satu-satunya cara untuk mengisi data objek ini.
     * Setelah objek dibuat, nilainya tidak bisa diubah lagi (Immutable Object).
     */
    public UserProfile(String id, String email, String username, String fullName, String avatarUrl, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    // ─── GETTER PUBLIK: Akses Baca yang Terkontrol ───
    // Ini adalah satu-satunya cara kelas lain bisa membaca data dari objek ini.

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getRole() {
        return this.role;
    }
}
