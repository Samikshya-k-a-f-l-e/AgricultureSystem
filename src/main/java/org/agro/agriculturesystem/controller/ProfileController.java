package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import org.agro.agriculturesystem.dto.ProfileDTO;
import org.agro.agriculturesystem.model.ForumTopics;
import org.agro.agriculturesystem.model.Profile;
import org.agro.agriculturesystem.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<?> addProfile(@ModelAttribute ProfileDTO profileDTO, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "User not logged in"
            ));
        }

        profileDTO.setUserId(userId);
        try {
            profileService.addProfile(profileDTO);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Profile created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("status", "error", "message", "Failed to create profile"));
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateProfile(@ModelAttribute ProfileDTO profileDTO, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "error", "message", "User not logged in"));
        }

        profileDTO.setUserId(userId);
        try {
            profileService.updateProfile(profileDTO);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Profile updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("status", "error", "message", "Failed to update profile"));
        }
    }
}
