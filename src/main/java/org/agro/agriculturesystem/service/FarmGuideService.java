package org.agro.agriculturesystem.service;

import jakarta.persistence.EntityNotFoundException;
import org.agro.agriculturesystem.dto.FarmGuideDTO;
import org.agro.agriculturesystem.model.FarmGuide;
import org.agro.agriculturesystem.repository.FarmGuideRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FarmGuideService {

    @Autowired
    private FarmGuideRepo farmGuideRepo;

    @Transactional
    public FarmGuide addFarmGuide(FarmGuideDTO farmGuideDTO, Integer userId) {
        try {
            // Title
            String title = farmGuideDTO.getTitle();
            if (title == null || title.trim().length() < 5 || !title.matches("^[a-zA-Z0-9\\s]+$")) {
                throw new IllegalArgumentException("Invalid title. Must be at least 5 characters and contain only letters, numbers, and spaces.");
            }

            // Content
            String content = farmGuideDTO.getContent();
            if (content == null || content.trim().length() < 20) {
                throw new IllegalArgumentException("Content must be at least 20 characters long.");
            }

            // Category
            if (farmGuideDTO.getCategory() == null || farmGuideDTO.getCategory().trim().isEmpty()) {
                throw new IllegalArgumentException("Category is required.");
            }

            // Status
            String status = farmGuideDTO.getStatus();
            if (status != null && !isValidStatus(status)) {
                throw new IllegalArgumentException("Invalid status. Allowed values: draft, published");
            }

            // Excerpt (optional)
            String excerpt = farmGuideDTO.getExcerpt();
            if (excerpt != null && (excerpt.trim().length() < 10 || !excerpt.matches("^[a-zA-Z0-9 ,.\\-]+$"))) {
                throw new IllegalArgumentException("Excerpt must be at least 10 characters and contain only valid characters.");
            }

            // Meta Title
            String metaTitle = farmGuideDTO.getMetaTitle();
            if (metaTitle != null && !metaTitle.matches("^[a-zA-Z0-9 \\-]{5,100}$")) {
                throw new IllegalArgumentException("Meta title must be 5-100 characters and contain only letters, numbers, hyphens and spaces.");
            }

            // Meta Description
            String metaDesc = farmGuideDTO.getMetaDescription();
            if (metaDesc != null && !metaDesc.matches("^[a-zA-Z0-9 ,.\\-]{10,200}$")) {
                throw new IllegalArgumentException("Meta description must be 10-200 characters and contain only valid characters.");
            }

            // Featured Image
            if (farmGuideDTO.getFeaturedImage() == null || farmGuideDTO.getFeaturedImage().isEmpty()) {
                throw new IllegalArgumentException("Featured image is required.");
            }



            FarmGuide farmGuide = new FarmGuide();
            farmGuide.setTitle(farmGuideDTO.getTitle());
            farmGuide.setSlug(generateSlug(farmGuideDTO.getTitle()));
            farmGuide.setExcerpt(farmGuideDTO.getExcerpt());
            farmGuide.setContent(farmGuideDTO.getContent());
            farmGuide.setCategory(farmGuideDTO.getCategory());
            farmGuide.setSubcategory(farmGuideDTO.getSubcategory());
            farmGuide.setAuthorId(userId);
            farmGuide.setMetaTitle(farmGuideDTO.getMetaTitle());
            farmGuide.setMetaDescription(farmGuideDTO.getMetaDescription());
            farmGuide.setKeywords(farmGuideDTO.getKeywords());
            farmGuide.setFeaturedPosition(1);
            farmGuide.setSourceUrl(farmGuideDTO.getSourceUrl());
            farmGuide.setSourceName(farmGuideDTO.getSourceName());

            Boolean isFeatured = farmGuideDTO.getFeatured() != null ? farmGuideDTO.getFeatured() : false;
            farmGuide.setFeatured(isFeatured);

            farmGuide.setStatus(FarmGuide.GuideStatus.valueOf(farmGuideDTO.getStatus()));
//            farmGuide.setViewCount(0);
            farmGuide.setCreatedAt(LocalDateTime.now());
            farmGuide.setUpdatedAt(LocalDateTime.now());

            String storedFileName = saveImage(farmGuideDTO.getFeaturedImage());
            farmGuide.setFeaturedImage(storedFileName);

            FarmGuide savedGuide = farmGuideRepo.save(farmGuide);
            return savedGuide;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create guide: " + e.getMessage(), e);
        }
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special chars
                .replaceAll("\\s+", "-")          // Replace spaces with hyphens
                .replaceAll("-+", "-");           // Collapse multiple hyphens
    }

    private boolean isValidStatus(String status) {
        return status.equals("draft") ||
                status.equals("published");
    }

    public List<FarmGuide> getAllFarmGuides() {
        return farmGuideRepo.findAll();
    }

   @Transactional
    public void deleteFarmGuide(int id) {
       FarmGuide f = farmGuideRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("FarmGuide not found with id: " + id));
       farmGuideRepo.delete(f);

    }

    public FarmGuide getFarmGuideById(int id) {
        return farmGuideRepo.findById(id).orElseThrow(() -> new RuntimeException("Guide not found"));
    }

    @Transactional
    public void updateFarmGuide(FarmGuideDTO dto, int id, Integer userId) {
        FarmGuide guide = farmGuideRepo.findById(id).orElseThrow(() -> new RuntimeException("Guide not found"));

        String title = dto.getTitle();
        if (title == null || title.trim().length() < 5 || !title.matches("^[a-zA-Z0-9\\s]+$")) {
            throw new IllegalArgumentException("Title must be at least 5 characters and contain only letters, numbers, and spaces.");
        }

        String content = dto.getContent();
        if (content == null || content.trim().length() < 20) {
            throw new IllegalArgumentException("Content must be at least 20 characters long.");
        }

        if (dto.getCategory() == null || dto.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }

        if (dto.getExcerpt() != null && (dto.getExcerpt().trim().length() < 10 || !dto.getExcerpt().matches("^[a-zA-Z0-9 ,.\\-]+$"))) {
            throw new IllegalArgumentException("Excerpt must be at least 10 characters and contain only valid characters.");
        }

        String metaTitle = dto.getMetaTitle();
        if (metaTitle != null && !metaTitle.matches("^[a-zA-Z0-9 \\-]{5,100}$")) {
            throw new IllegalArgumentException("Meta title must be 5-100 characters and contain only letters, numbers, hyphens, and spaces.");
        }

        String metaDesc = dto.getMetaDescription();
        if (metaDesc != null && !metaDesc.matches("^[a-zA-Z0-9 ,.\\-]{10,200}$")) {
            throw new IllegalArgumentException("Meta description must be 10-200 characters and contain only valid characters.");
        }

        if (dto.getStatus() != null && !isValidStatus(dto.getStatus())) {
            throw new IllegalArgumentException("Invalid status. Allowed values: draft, published");
        }

        if (dto.getFeaturedImage() != null && !dto.getFeaturedImage().isEmpty()) {
            if (guide.getFeaturedImage() != null) {
                deleteImage(guide.getFeaturedImage());
            }
            String newImageFileName = saveImage(dto.getFeaturedImage());
            guide.setFeaturedImage(newImageFileName);
        }

        guide.setTitle(dto.getTitle());
        guide.setSlug(generateSlug(dto.getTitle()));
        guide.setExcerpt(dto.getExcerpt());
        guide.setContent(dto.getContent());
        guide.setCategory(dto.getCategory());
        guide.setSubcategory(dto.getSubcategory());
        guide.setAuthorId(userId);
        guide.setMetaTitle(dto.getMetaTitle());
        guide.setMetaDescription(dto.getMetaDescription());
        guide.setUpdatedAt(LocalDateTime.now());
        guide.setSourceUrl(dto.getSourceUrl());
        guide.setSourceName(dto.getSourceName());

        if (dto.getFeatured() != null) {
            guide.setFeatured(dto.getFeatured());
        } else {
            guide.setFeatured(false);
        }

        guide.setFeaturedPosition(dto.getFeaturedPosition());
        guide.setKeywords(dto.getKeywords());

        if (dto.getStatus() != null && !isValidStatus(dto.getStatus())) {
            throw new IllegalArgumentException("Invalid status");
        }
        guide.setStatus(FarmGuide.GuideStatus.valueOf(dto.getStatus()));


        farmGuideRepo.save(guide);
    }

    public FarmGuide getFarmGuideBySlug(String slug) {
        return farmGuideRepo.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException("Guide not found"));
    }

    public String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            String uploadDir = "uploads/farmguides/";

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = imageFile.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;

            Path filePath = Paths.get(uploadDir, fileName);

            Files.write(filePath, imageFile.getBytes());

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteImage(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        try {
            Path filePath = Paths.get("uploads/farmguides/", filename);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }

    public Page<FarmGuide> getPaginatedFarmGuides(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return farmGuideRepo.findAll(pageable);
    }

    public void save(FarmGuide guide) {
        farmGuideRepo.save(guide);
    }

    public List<FarmGuide> getAllFeatured() {
        return farmGuideRepo.findByFeaturedTrueOrderByFeaturedPositionAsc();
    }

}