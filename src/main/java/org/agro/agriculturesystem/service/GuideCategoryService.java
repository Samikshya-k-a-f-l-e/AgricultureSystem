package org.agro.agriculturesystem.service;

import jakarta.transaction.Transactional;
import org.agro.agriculturesystem.dto.GuideCatDTO;
import org.agro.agriculturesystem.model.GuideCategory;
import org.agro.agriculturesystem.repository.GuideCatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class GuideCategoryService {

    @Autowired
    private GuideCatRepo guideCatRepo;

    @Transactional
    public GuideCategory saveGuideCat(GuideCatDTO guideDTO) {
        if(guideCatRepo.findByName(guideDTO.getName()).isPresent()) {
            throw new RuntimeException("Category name already exists");
        }

        String name = guideDTO.getName();
        String description = guideDTO.getDescription();
        String iconClass = guideDTO.getIconClass();

        // Name validation: at least 2 characters, letters and spaces
        if (name == null || !name.matches("^[A-Za-z]{2}[A-Za-z ]*$")) {
            throw new RuntimeException("Invalid category name! Only letters and spaces, minimum 2 characters.");
        }

        // Description validation: not null or empty, min length
        if (description == null || description.trim().length() < 5) {
            throw new RuntimeException("Description must be at least 5 characters long.");
        }

        if (!description.matches("^[a-zA-Z0-9 ,.]+$")) {
            throw new RuntimeException("Description can only contain letters, numbers, spaces, commas, and periods.");
        }


        // Icon class validation: not empty, must be a valid class format (e.g., "fa fa-icon")
        if (iconClass == null || iconClass.trim().isEmpty()) {
            throw new RuntimeException("Icon class is required.");
        }

        if (!iconClass.matches("^[a-zA-Z\\- ]+$")) {
            throw new RuntimeException("Invalid icon class format.");
        }

        GuideCategory guideCategory = new GuideCategory();
        guideCategory.setName(guideDTO.getName());
        guideCategory.setDescription(guideDTO.getDescription());
        guideCategory.setGuidesCount(0);
        guideCategory.setStatus(Objects.equals(guideDTO.getStatus(), "on") ? "on" : "off");
        guideCategory.setIconClass(guideDTO.getIconClass());
        guideCategory.setCreatedAt(LocalDateTime.now());
        guideCategory.setSlug(generateSlug(guideDTO.getName()));

        return guideCatRepo.save(guideCategory);
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special chars
                .replaceAll("\\s+", "-")          // Replace spaces with hyphens
                .replaceAll("-+", "-");           // Collapse multiple hyphens
    }

    public List<GuideCategory> getAllCategories() {
        return guideCatRepo.findAll();
    }

    @Transactional
    public void deleteGuideCategory(int id) {
        GuideCategory category = getCategoryById(id);

        if (category.getGuidesCount() > 0) {
            throw new RuntimeException("Cannot delete category with existing guides. Move or delete guides first.");
        }

        guideCatRepo.delete(category);
    }

    public GuideCategory getCategoryById(int id) {
        return guideCatRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Transactional
    public GuideCategory updateGuideCategory(int id, GuideCatDTO dto) {
        GuideCategory category = getCategoryById(id);

        String name = dto.getName();
        String description = dto.getDescription();
        String iconClass = dto.getIconClass();

        // Name validation: at least 2 characters, letters and spaces
        if (name == null || !name.matches("^[A-Za-z]{2}[A-Za-z ]*$")) {
            throw new RuntimeException("Invalid category name! Only letters and spaces, minimum 2 characters.");
        }

        // Description validation: not null or empty, min length
        if (description == null || description.trim().length() < 5) {
            throw new RuntimeException("Description must be at least 5 characters long.");
        }

        if (!description.matches("^[a-zA-Z0-9 ,.]+$")) {
            throw new RuntimeException("Description can only contain letters, numbers, spaces, commas, and periods.");
        }

        // Icon class validation: not empty, must be a valid class format (e.g., "fa fa-icon")
        if (iconClass == null || iconClass.trim().isEmpty()) {
            throw new RuntimeException("Icon class is required.");
        }

        if (!iconClass.matches("^[a-zA-Z\\- ]+$")) {
            throw new RuntimeException("Invalid icon class format.");
        }

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setStatus(Objects.equals(dto.getStatus(), "on") ? "on" : "off");
        category.setIconClass(dto.getIconClass());
        category.setCreatedAt(LocalDateTime.now());
        category.setSlug(generateSlug(dto.getName()));


        return guideCatRepo.save(category);
    }

}