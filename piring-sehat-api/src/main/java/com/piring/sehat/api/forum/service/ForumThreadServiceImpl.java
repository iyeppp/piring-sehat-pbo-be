package com.piring.sehat.api.forum.service;

import com.piring.sehat.api.forum.dto.ForumThreadRequest;
import com.piring.sehat.api.forum.dto.ForumThreadResponse;
import com.piring.sehat.api.forum.model.ForumThread;
import com.piring.sehat.api.forum.repository.ForumThreadRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * =====================================================================
 * PILAR OOP #3: POLIMORFISME (POLYMORPHISM)
 * =====================================================================
 * Kelas ini adalah WUJUD NYATA (Implementasi) dari interface ForumThreadService.
 * Berkat polimorfisme, controller bisa memanggil berbagai metode di bawah ini 
 * hanya dengan menggunakan tipe datanya (Interface), tanpa peduli logika aslinya.
 */
@Service
public class ForumThreadServiceImpl implements ForumThreadService {

    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    private final ForumThreadRepository repository;
    private final com.piring.sehat.api.forum.repository.ForumReplyRepository replyRepository;

    // Dependency Injection (mencegah instance manual dengan keyword 'new')
    public ForumThreadServiceImpl(ForumThreadRepository repository, com.piring.sehat.api.forum.repository.ForumReplyRepository replyRepository) {
        this.repository = repository;
        this.replyRepository = replyRepository;
    }

    @Override
    public List<ForumThreadResponse> getAllThreads(String category) {
        List<ForumThread> threads;
        if (category != null && !category.trim().isEmpty()) {
            threads = repository.findByCategoryOrderByCreatedAtDesc(category);
        } else {
            threads = repository.findAllByOrderByCreatedAtDesc();
        }
        
        return threads.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ForumThreadResponse getThreadById(UUID id) {
        ForumThread thread = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Forum tidak ditemukan"));
        return mapToResponse(thread);
    }

    @Override
    public ForumThreadResponse createThread(Jwt jwt, ForumThreadRequest request) {
        UUID authorId = UUID.fromString(jwt.getSubject()); // Ekstrak UUID user dari token Supabase

        ForumThread thread = new ForumThread();
        thread.setId(UUID.randomUUID()); // Generate UUID secara manual
        thread.setAuthorId(authorId);
        thread.setTitle(request.getTitle());
        thread.setContent(request.getContent());
        thread.setCategory(request.getCategory() != null ? request.getCategory() : "general");
        
        ForumThread savedThread = repository.save(thread);
        return mapToResponse(savedThread);
    }

    @Override
    public ForumThreadResponse updateThread(Jwt jwt, UUID id, ForumThreadRequest request) {
        UUID authorId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        
        String role = null;
        try {
            role = repository.getUserRole(authorId);
        } catch (Exception e) {
            // Fail-safe jika kolom role belum ditambahkan di database
        }
        
        boolean isModerator = "moderator".equalsIgnoreCase(role) || "moderator@piringsehat.com".equalsIgnoreCase(email);
        
        ForumThread existingThread = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Forum tidak ditemukan"));
                
        // PENCEGAHAN EDIT POSTINGAN TERMODERASI:
        // Jika konten bernilai "[Removed by Moderator]", postingan ini telah disensor oleh admin
        // dan tidak boleh diubah oleh siapapun (termasuk author aslinya).
        if ("[Removed by Moderator]".equals(existingThread.getContent())) {
            throw new IllegalArgumentException("Postingan ini telah dihapus oleh moderator dan tidak dapat diubah.");
        }
                
        // HAK AKSES EDIT: Memastikan forum tersebut milik author atau yang login adalah moderator
        if (!isModerator && !existingThread.getAuthorId().equals(authorId)) {
            throw new IllegalArgumentException("Anda tidak memiliki akses untuk mengubah forum ini.");
        }
        
        existingThread.setTitle(request.getTitle());
        existingThread.setContent(request.getContent());
        if (request.getCategory() != null) {
            existingThread.setCategory(request.getCategory());
        }
        
        ForumThread updatedThread = repository.save(existingThread);
        return mapToResponse(updatedThread);
    }

    @Override
    public void deleteThread(Jwt jwt, UUID id) {
        UUID authorId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        
        // Membaca role pengguna secara dinamis dari tabel database public.user_profiles
        String role = null;
        try {
            role = repository.getUserRole(authorId);
        } catch (Exception e) {
            // Fail-safe jika kolom role tidak ditemukan atau database offline
        }
        
        boolean isModerator = "moderator".equalsIgnoreCase(role) || "moderator@piringsehat.com".equalsIgnoreCase(email);
        
        ForumThread existingThread = repository.findById(id).orElse(null);
        if (existingThread != null) {
            if (isModerator) {
                // REDDIT-STYLE SOFT-DELETION OLEH MODERATOR:
                // Jangan hapus baris dari database agar data historis tetap tersimpan rapi.
                // Cukup sensor judul & isi kontennya menjadi penanda khusus.
                existingThread.setTitle("[Dihapus oleh Moderator]");
                existingThread.setContent("[Removed by Moderator]");
                repository.save(existingThread);
            } else if (existingThread.getAuthorId().equals(authorId)) {
                // Pemilik asli menghapus -> diperbolehkan langsung hapus permanen dari database (Hard Delete)
                repository.delete(existingThread);
            } else {
                throw new IllegalArgumentException("Anda tidak memiliki akses untuk menghapusnya.");
            }
        } else {
            throw new IllegalArgumentException("Forum tidak ditemukan.");
        }
    }

    @Override
    public com.piring.sehat.api.forum.dto.ForumReplyResponse createReply(Jwt jwt, UUID threadId, com.piring.sehat.api.forum.dto.ForumReplyRequest request) {
        UUID authorId = UUID.fromString(jwt.getSubject());
        
        ForumThread thread = repository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Forum tidak ditemukan"));
                
        com.piring.sehat.api.forum.model.ForumReply reply = new com.piring.sehat.api.forum.model.ForumReply();
        reply.setId(UUID.randomUUID());
        reply.setThread(thread);
        reply.setAuthorId(authorId);
        reply.setContent(request.getContent());
        
        com.piring.sehat.api.forum.model.ForumReply savedReply = replyRepository.save(reply);
        return mapReplyToResponse(savedReply);
    }
    
    @Override
    public List<com.piring.sehat.api.forum.dto.ForumReplyResponse> getRepliesByThreadId(UUID threadId) {
        List<com.piring.sehat.api.forum.model.ForumReply> replies = replyRepository.findByThreadIdOrderByCreatedAtAsc(threadId);
        return replies.stream().map(this::mapReplyToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteReply(Jwt jwt, UUID replyId) {
        UUID authorId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        
        // Membaca role secara dinamis dari tabel user_profiles database
        String role = null;
        try {
            role = repository.getUserRole(authorId);
        } catch (Exception e) {
            // Fail-safe jika kolom role belum ditambahkan di database
        }
        
        boolean isModerator = "moderator".equalsIgnoreCase(role) || "moderator@piringsehat.com".equalsIgnoreCase(email);
        
        com.piring.sehat.api.forum.model.ForumReply existingReply = replyRepository.findById(replyId).orElse(null);
        if (existingReply != null) {
            if (isModerator) {
                // REDDIT-STYLE SOFT-DELETION UNTUK REPLY:
                // Jangan hapus baris reply dari database agar tidak memutus rantai percakapan (tree view).
                // Cukup sensor isi konten reply dengan teks penanda khusus.
                existingReply.setContent("[Removed by Moderator]");
                replyRepository.save(existingReply);
            } else if (existingReply.getAuthorId().equals(authorId)) {
                // Pemilik asli menghapus -> boleh langsung hapus permanen dari database (Hard Delete)
                replyRepository.delete(existingReply);
            } else {
                throw new IllegalArgumentException("Anda tidak memiliki akses untuk menghapusnya.");
            }
        } else {
            throw new IllegalArgumentException("Balasan tidak ditemukan.");
        }
    }

    @Override
    public com.piring.sehat.api.forum.dto.ForumReplyResponse updateReply(Jwt jwt, UUID replyId, com.piring.sehat.api.forum.dto.ForumReplyRequest request) {
        UUID authorId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        
        String role = null;
        try {
            role = repository.getUserRole(authorId);
        } catch (Exception e) {
            // Fail-safe jika database bermasalah
        }
        
        boolean isModerator = "moderator".equalsIgnoreCase(role) || "moderator@piringsehat.com".equalsIgnoreCase(email);
        
        com.piring.sehat.api.forum.model.ForumReply existingReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Balasan tidak ditemukan"));
                
        // PENCEGAHAN EDIT BALASAN TERMODERASI:
        // Balasan yang telah dihapus oleh moderator tidak boleh diubah kembali oleh author aslinya.
        if ("[Removed by Moderator]".equals(existingReply.getContent())) {
            throw new IllegalArgumentException("Balasan ini telah dihapus oleh moderator dan tidak dapat diubah.");
        }
                
        // HAK AKSES EDIT: Memastikan milik author asli atau login sebagai moderator
        if (!isModerator && !existingReply.getAuthorId().equals(authorId)) {
            throw new IllegalArgumentException("Anda tidak memiliki akses untuk mengubah balasan ini.");
        }
        
        existingReply.setContent(request.getContent());
        com.piring.sehat.api.forum.model.ForumReply updatedReply = replyRepository.save(existingReply);
        return mapReplyToResponse(updatedReply);
    }

    // Record/Class internal untuk membawa info profil author secara terstruktur
    private static class AuthorProfile {
        private final String username;
        private final String avatarUrl;

        public AuthorProfile(String username, String avatarUrl) {
            this.username = username;
            this.avatarUrl = avatarUrl;
        }

        public String getUsername() { return username; }
        public String getAvatarUrl() { return avatarUrl; }
    }

    // Mengambil profil author secara efisien (mengurangi redundant database queries)
    private AuthorProfile fetchAuthorProfile(UUID authorId) {
        try {
            ForumThreadRepository.AuthorProfileProjection profile = repository.findAuthorProfileById(authorId);
            if (profile != null) {
                return new AuthorProfile(profile.getUsername(), profile.getAvatarUrl());
            }
        } catch (Exception e) {
            // Jika gagal mengambil profil, biarkan kosong (fallback ditangani di frontend)
        }
        return new AuthorProfile(null, null);
    }

    // Helper method untuk memetakan Model -> DTO
    private ForumThreadResponse mapToResponse(ForumThread thread) {
        AuthorProfile profile = fetchAuthorProfile(thread.getAuthorId());
                                        
        ForumThreadResponse response = new ForumThreadResponse();
        response.setId(thread.getId());
        response.setAuthorId(thread.getAuthorId());
        response.setAuthorUsername(profile.getUsername());
        response.setAuthorAvatarUrl(profile.getAvatarUrl());
        response.setTitle(thread.getTitle());
        response.setContent(thread.getContent());
        response.setCategory(thread.getCategory());
        response.setCreatedAt(DATE_FORMATTER.format(thread.getCreatedAt()));
        response.setUpdatedAt(DATE_FORMATTER.format(thread.getUpdatedAt()));
        return response;
    }

    private com.piring.sehat.api.forum.dto.ForumReplyResponse mapReplyToResponse(com.piring.sehat.api.forum.model.ForumReply reply) {
        AuthorProfile profile = fetchAuthorProfile(reply.getAuthorId());

        com.piring.sehat.api.forum.dto.ForumReplyResponse response = new com.piring.sehat.api.forum.dto.ForumReplyResponse();
        response.setId(reply.getId());
        response.setThreadId(reply.getThread().getId());
        response.setAuthorId(reply.getAuthorId());
        response.setAuthorUsername(profile.getUsername());
        response.setAuthorAvatarUrl(profile.getAvatarUrl());
        response.setContent(reply.getContent());
        response.setCreatedAt(DATE_FORMATTER.format(reply.getCreatedAt()));
        response.setUpdatedAt(DATE_FORMATTER.format(reply.getUpdatedAt()));
        return response;
    }
}
