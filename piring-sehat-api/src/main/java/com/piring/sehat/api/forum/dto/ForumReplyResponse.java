package com.piring.sehat.api.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) untuk mengembalikan response data balasan (reply) ke Frontend.
 * Abstraksi: Menyembunyikan kompleksitas format tanggal bawaan Java (Instant) 
 * menjadi String yang mudah dibaca oleh React.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumReplyResponse {
    private UUID id;
    private UUID threadId;
    private UUID authorId;
    private String authorUsername;  // Username dari user_profiles
    private String authorAvatarUrl; // Avatar URL dari user_profiles
    private String content;
    private String createdAt;
    private String updatedAt;
}
