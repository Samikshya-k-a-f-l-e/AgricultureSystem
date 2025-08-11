package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.agro.agriculturesystem.dto.ReactionDTO;
import org.agro.agriculturesystem.service.ReactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping
    public ResponseEntity<?> addReaction(@Valid @RequestBody ReactionDTO reactionDTO, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null || userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "You are not logged in"
            ));
        }
        reactionDTO.setUserId(userId);
        try{
            reactionService.addReaction(reactionDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "action", "added",
                    "message", "Reaction added successfully"
            ));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> removeReaction(Long id){
        try{
            reactionService.deleteReaction(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "action", "removed",
                    "message", "Reaction removed successfully"
            ));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

}
