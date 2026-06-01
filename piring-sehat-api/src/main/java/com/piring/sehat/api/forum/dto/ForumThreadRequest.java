package com.piring.sehat.api.forum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * =====================================================================
 * POLA DESAIN: DATA TRANSFER OBJECT (DTO)
 * ARSITEKTUR MVC: CONTROLLER-TO-SERVICE BOUNDARY LAYER
 * PRINSIP SOLID: SINGLE RESPONSIBILITY PRINCIPLE (SRP)
 * PILAR OOP: ENKAPSULASI (ENCAPSULATION)
 * =====================================================================
 * Data Transfer Object (DTO) untuk menangkap request pembuatan/pembaruan forum.
 * 
 * PENERAPAN PRINSIP & PILAR:
 * 1. SINGLE RESPONSIBILITY (SRP): Kelas ini HANYA bertanggung jawab
 *    untuk membawa data input dari HTTP request. Ia terpisah dari entity 
 *    tabel database asli guna menangkal "Mass Assignment Vulnerability".
 * 2. ENKAPSULASI (OOP): Properti internal dibungkus rapat dan diakses
 *    melalui getter/setter dinamis yang dihasilkan oleh Lombok @Data.
 * 3. PEMBAGIAN LAPISAN MVC: Bertindak sebagai objek perantara yang melintasi
 *    batas antara View (Klien) dan Controller sebelum diproses oleh Service.
 * 
 * PEMBAGIAN BEBAN VALIDASI (CLIENT vs SERVER):
 * - Level Server (DTO): Memastikan field-field krusial tidak dikirim kosong (`@NotBlank`).
 * - Level Client (Frontend): Mengecek batasan panjang minimal judul (5 karakter) dan isi (10 karakter).
 *   Ini menghindarkan server dari keharusan membuang resource CPU untuk memproses input tidak layak.
 */
@Data
public class ForumThreadRequest {

    @NotBlank(message = "Judul forum tidak boleh kosong")
    private String title;

    @NotBlank(message = "Isi forum tidak boleh kosong")
    private String content;

    private String category;
}
