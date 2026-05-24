package com.piring.sehat.api.auth.dto;

/**
 * =====================================================================
 * POLA DESAIN: DATA TRANSFER OBJECT (DTO)
 * =====================================================================
 * UserProfileResponse adalah kelas DTO yang bertanggung jawab untuk
 * membungkus data profil pengguna saat dikirim keluar melalui API HTTP.
 *
 * MENGAPA DTO PENTING:
 * 1. Memisahkan representasi data internal (Model) dari representasi
 *    data yang dikirim ke klien (DTO). Ini disebut "Separation of Concerns".
 * 2. Model domain bisa berisi data sensitif yang TIDAK boleh dikirim
 *    ke klien (misalnya password hash, internal flags). DTO bertindak 
 *    sebagai filter yang hanya mengirim data yang diizinkan.
 * 3. DTO bisa menambahkan field tambahan yang tidak ada di Model, 
 *    seperti 'timestamp' respons dan 'status' verifikasi, tanpa 
 *    mengotori kelas Model domain.
 *
 * PRINSIP OOP YANG DITERAPKAN:
 * - Enkapsulasi: Semua field 'private final' dengan getter publik.
 * - Single Responsibility (SRP): Kelas ini HANYA bertanggung jawab
 *   untuk membawa data respons API, tidak mengurusi logika bisnis.
 */
public class UserProfileResponse {

    // ─── Field Private (Enkapsulasi) ───
    private final String id;
    private final String email;
    private final String username;
    private final String fullName;
    private final String avatarUrl;
    private final String timestamp;  // Field tambahan yang TIDAK ada di Model
    private final String status;     // Field tambahan: status verifikasi JWT

    /**
     * Konstruktor utama untuk instansiasi DTO.
     * Menerima data dari Model domain dan menambahkan metadata respons API.
     */
    public UserProfileResponse(String id, String email, String username,
                                String fullName, String avatarUrl,
                                String timestamp, String status) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.timestamp = timestamp;
        this.status = status;
    }

    // ─── GETTER PUBLIK (Akses Terkontrol) ───

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

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getStatus() {
        return this.status;
    }
}
