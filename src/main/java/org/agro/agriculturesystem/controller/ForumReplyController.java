package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.agro.agriculturesystem.dto.ForumReplyDTO;
import org.agro.agriculturesystem.service.ForumReplyService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/forum/replies")
public class ForumReplyController {
    private final ForumReplyService forumReplyService;

    public ForumReplyController(ForumReplyService forumReplyService) {
        this.forumReplyService = forumReplyService;
    }

    @PostMapping
    public ResponseEntity<?> addForumReply(@Valid @RequestBody ForumReplyDTO forumReplyDTO, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "User not logged in"
            ));
        }
        forumReplyDTO.setUserId(userId);

        try {
            ForumReplyDTO reply = forumReplyService.createReply(forumReplyDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Reply created successfully",
                    "data", reply,
                    "redirectUrl", "/forum/topic/" + forumReplyDTO.getTopicId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long replyId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "User not logged in"
            ));
        }
        try {
            forumReplyService.deleteReply(replyId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Reply deleted successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

}