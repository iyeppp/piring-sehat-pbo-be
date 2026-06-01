package com.piring.sehat.api.forum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * =====================================================================
 * POLA DESAIN: DATA TRANSFER OBJECT (DTO)
 * =====================================================================
 * ForumReplyRequest adalah DTO yang digunakan untuk menangkap payload
 * pengiriman atau perubahan balasan (reply) dari sisi klien.
 * 
 * KENAPA BUTUH DTO? (Pertanyaan Umum Presentasi)
 * - Mencegah "Mass Assignment Vulnerability" (Hacker bisa saja menyisipkan field authorId buatan mereka sendiri).
 * - Menangani Validasi input sebelum mencapai layer service atau database.
 */
@Data
public class ForumReplyRequest {

    @NotBlank(message = "Konten balasan tidak boleh kosong")
    private String content;

}
