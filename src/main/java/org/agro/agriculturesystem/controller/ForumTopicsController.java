package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.agro.agriculturesystem.dto.ForumTopicsDTO;
import org.agro.agriculturesystem.model.ForumTopics;
import org.agro.agriculturesystem.model.User;
import org.agro.agriculturesystem.service.ForumTopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/forum/topics")
public class ForumTopicsController {

    private final ForumTopicsService forumTopicsService;

    @Autowired
    public ForumTopicsController(ForumTopicsService forumTopicsService) {
        this.forumTopicsService = forumTopicsService;
    }

    @PostMapping
    public ResponseEntity<?> createTopic(@RequestBody @Valid ForumTopicsDTO dto, HttpSession session) {
        Integer current = (Integer) session.getAttribute("userId");
        if (current == null || current == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error", "message", "User not logged in"
            ));
        }
        dto.setAuthorId(current);

        try {
            ForumTopics created = forumTopicsService.createForumTopic(dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Topic created successfully",
                    "redirectUrl", "/forum"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTopic(@PathVariable Long id, @RequestBody ForumTopicsDTO dto) {
        try {
            forumTopicsService.updateForumTopic(id, dto);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Topic updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("status", "error", "message", "Failed to update topic"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumTopicsDTO> getTopicById(@PathVariable Long id) {
        try {
            ForumTopics topic = forumTopicsService.getTopicById(id);
            ForumTopicsDTO dto = mapToDto(topic);
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ForumTopics>> getAllTopics(Pageable pageable) {
        return ResponseEntity.ok(forumTopicsService.getAllTopics(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        forumTopicsService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    private ForumTopicsDTO mapToDto(ForumTopics topic) {
        ForumTopicsDTO dto = new ForumTopicsDTO();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setContent(topic.getContent());
        dto.setTags(topic.getTags());
        dto.setSlug(topic.getSlug());
        dto.setStatus(topic.getStatus());
        dto.setUpdatedAt(topic.getUpdatedAt());

        if (topic.getCategory() != null) {
            dto.setCategoryId(topic.getCategory().getId());
        }

        if (topic.getAuthor() != null) {
            dto.setAuthorId(topic.getAuthor().getId());
        }

        return dto;
    }
}
