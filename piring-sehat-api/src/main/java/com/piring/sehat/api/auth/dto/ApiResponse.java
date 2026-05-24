package com.piring.sehat.api.auth.dto;

import java.time.LocalDateTime;

/**
 * =====================================================================
 * POLA DESAIN: GENERIC API RESPONSE WRAPPER (Pembungkus Generik)
 * =====================================================================
 * Kelas ApiResponse<T> adalah pembungkus respons generik standar untuk semua
 * endpoint API. Kelas ini memastikan setiap respons dari backend mengikuti
 * format yang seragam dan konsisten, sehingga frontend mudah memproses datanya.
 *
 * PRINSIP OOP YANG DITERAPKAN:
 * - Generics (Tipe Parametrik): Menggunakan parameter tipe <T> untuk menerima
 *   tipe data apapun sebagai payload (UserProfileResponse, String, List, dll).
 * - Enkapsulasi: Field private dengan getter publik.
 * - Open/Closed Principle (OCP): Format respons ini bisa diperluas tanpa
 *   memodifikasi kelas yang sudah ada.
 *
 * CONTOH FORMAT JSON YANG DIHASILKAN:
 * {
 *   "success": true,
 *   "message": "Profil berhasil dimuat",
 *   "data": { ... },
 *   "timestamp": "2026-05-21T17:50:00"
 * }
 */
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final String timestamp;

    /**
     * Konstruktor private yang digunakan oleh metode factory statis di bawah.
     * Pola Factory Method memungkinkan pembuatan objek melalui pemanggilan
     * metode yang lebih deskriptif daripada konstruktor langsung.
     */
    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }

    // ─── FACTORY METHODS (Pola Desain: Factory Method) ───

    /**
     * Membuat respons sukses dengan pesan dan data payload.
     *
     * @param message Pesan deskriptif untuk klien
     * @param data    Payload data generik bertipe T
     * @return ApiResponse terenkapsulasi bertipe sukses
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Membuat respons error dengan pesan kesalahan deskriptif.
     *
     * @param message Pesan error deskriptif untuk klien
     * @return ApiResponse terenkapsulasi bertipe error (tanpa data payload)
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // ─── GETTER PUBLIK ───

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public String getTimestamp() {
        return this.timestamp;
    }
}
