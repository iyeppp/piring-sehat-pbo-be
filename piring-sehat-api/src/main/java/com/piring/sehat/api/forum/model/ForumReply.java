package com.piring.sehat.api.forum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #1: ENKAPSULASI
 * =====================================================================
 * Entitas ini merepresentasikan tabel 'forum_reply' di database Supabase.
 * 
 * Penggunaan anotasi @Data dari Lombok memastikan prinsip enkapsulasi 
 * diterapkan dengan benar secara otomatis, yaitu:
 * - Menyembunyikan data internal (field bersifat private).
 * - Menyediakan akses publik terkontrol (getter & setter).
 * 
 * Dengan ini, variabel internal tidak bisa diubah seenaknya dari luar tanpa
 * melalui metode standar.
 */
@Entity
@Table(name = "forum_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumReply {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    // Relasi ke thread yang dibalas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private ForumThread thread;

    // Menyimpan UUID dari tabel user_profiles / auth.users Supabase
    @Column(name = "author_id", nullable = false, columnDefinition = "uuid")
    private UUID authorId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt = Instant.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
