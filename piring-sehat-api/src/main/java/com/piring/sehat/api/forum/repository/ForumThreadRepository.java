package com.piring.sehat.api.forum.repository;

import com.piring.sehat.api.forum.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #2: PEWARISAN (INHERITANCE)
 * =====================================================================
 * Interface ini mewarisi (extends) JpaRepository.
 * Dengan pewarisan ini, ForumThreadRepository otomatis memiliki semua metode dasar 
 * seperti save(), findAll(), findById(), delete() tanpa harus kita tulis kodenya.
 */
@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, UUID> {
    
    // Pencarian otomatis berdasarkan kategori dan diurutkan berdasarkan waktu terbaru
    List<ForumThread> findByCategoryOrderByCreatedAtDesc(String category);
    
    // Pencarian semua forum diurutkan dari yang terbaru
    List<ForumThread> findAllByOrderByCreatedAtDesc();
    
    // Mengecek spesifik forum milik author tertentu (berguna untuk update/delete agar aman)
    ForumThread findByIdAndAuthorId(UUID id, UUID authorId);

    // Mengambil role pengguna dari tabel public.user_profiles berdasarkan UUID
    @org.springframework.data.jpa.repository.Query(
        value = "SELECT role FROM public.user_profiles WHERE id = :userId",
        nativeQuery = true
    )
    String getUserRole(java.util.UUID userId);

    // Projection untuk efisiensi pengambilan profile pengguna (username & avatar_url)
    interface AuthorProfileProjection {
        String getUsername();
        String getAvatarUrl();
    }

    // Mengambil username dan avatar_url pengguna dalam satu kali query (Menghindari N+1 redundant queries)
    @org.springframework.data.jpa.repository.Query(
        value = "SELECT username, avatar_url AS avatarUrl FROM public.user_profiles WHERE id = :userId",
        nativeQuery = true
    )
    AuthorProfileProjection findAuthorProfileById(java.util.UUID userId);
}
