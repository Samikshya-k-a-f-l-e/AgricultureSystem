package org.agro.agriculturesystem.service;

import jakarta.transaction.Transactional;
import org.agro.agriculturesystem.dto.ProfileDTO;
import org.agro.agriculturesystem.model.Profile;
import org.agro.agriculturesystem.model.User;
import org.agro.agriculturesystem.repository.ProfileRepo;
import org.agro.agriculturesystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepo profileRepo;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepo profileRepo, UserRepository userRepository) {
        this.profileRepo = profileRepo;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addProfile(ProfileDTO profileDTO) {
        try {
            User user = userRepository.findById(profileDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            String storeFileName = saveImage(profileDTO.getProfileImage());

            System.out.println("full name "+profileDTO.getFullName());
            Profile profile = Profile.builder()
                    .fullName(profileDTO.getFullName())
                    .phone(profileDTO.getPhone())
                    .experience(profileDTO.getExperience())
                    .profileImage(storeFileName)
                    .specialization(profileDTO.getSpecialization())
                    .bio(profileDTO.getBio())
                    .location(profileDTO.getLocation())
                    .website(profileDTO.getWebsite())
                    .user(user)
                    .build();
            profileRepo.save(profile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create profile: " + e.getMessage());
        }
    }

    @Transactional
    public void updateProfile(ProfileDTO profileDTO) {
        try {
            Profile existingProfile = profileRepo.findByUserId(profileDTO.getUserId());

            if (profileDTO.getPhone() != null) {
                existingProfile.setPhone(profileDTO.getPhone());
            }

            if (profileDTO.getFullName() != null) {
                existingProfile.setFullName(profileDTO.getFullName());
            }

            if (profileDTO.getExperience() != null) {
                existingProfile.setExperience(profileDTO.getExperience());
            }

            if (profileDTO.getSpecialization() != null) {
                existingProfile.setSpecialization(profileDTO.getSpecialization());
            }
            
            if (profileDTO.getBio() != null) {
                existingProfile.setBio(profileDTO.getBio());
            }
            
            if (profileDTO.getLocation() != null) {
                existingProfile.setLocation(profileDTO.getLocation());
            }
            
            if (profileDTO.getWebsite() != null) {
                existingProfile.setWebsite(profileDTO.getWebsite());
            }


            // Handle image update if new image is provided
            if (profileDTO.getProfileImage() != null && !profileDTO.getProfileImage().isEmpty()) {
                // Delete old image file if exists
                if (existingProfile.getProfileImage() != null) {
                    deleteImage(existingProfile.getProfileImage());
                }
                // Save new image
                String newFileName = saveImage(profileDTO.getProfileImage());
                existingProfile.setProfileImage(newFileName);
            }

            profileRepo.save(existingProfile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update profile: " + e.getMessage());
        }
    }

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String uploadDir = "uploads/profile/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;

            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, file.getBytes());

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    public void deleteImage(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        try {
            Path filePath = Paths.get("uploads/profile/", filename);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }

    public Profile getProfileByUserId(Integer userId) {
        return profileRepo.findByUserId(userId);
    }
}