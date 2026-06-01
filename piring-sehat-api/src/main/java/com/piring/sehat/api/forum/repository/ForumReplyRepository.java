package com.piring.sehat.api.forum.repository;

import com.piring.sehat.api.forum.model.ForumReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #2: PEWARISAN (INHERITANCE)
 * =====================================================================
 * Interface ini mewarisi (extends) JpaRepository.
 * Dengan pewarisan ini, ForumReplyRepository otomatis memiliki semua metode dasar 
 * seperti save(), findAll(), findById(), delete() tanpa harus kita tulis kodenya.
 */
@Repository
public interface ForumReplyRepository extends JpaRepository<ForumReply, UUID> {
    List<ForumReply> findByThreadIdOrderByCreatedAtAsc(UUID threadId);
}
