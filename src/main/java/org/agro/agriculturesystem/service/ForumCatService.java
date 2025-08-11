package org.agro.agriculturesystem.service;

import org.agro.agriculturesystem.dto.ForumCatDTO;
import org.agro.agriculturesystem.model.ForumCategory;
import org.agro.agriculturesystem.repository.ForumCatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ForumCatService {

    @Autowired
    private ForumCatRepo forumCatRepo;

    @Transactional
    public ForumCategory addForumCat(ForumCatDTO forumCatDTO) {
        if(forumCatRepo.findByName(forumCatDTO.getName()).isPresent()) {
            throw new RuntimeException("Forum Category already exists");
        }
        String name = forumCatDTO.getName();
        String description = forumCatDTO.getDescription();

        if (name == null || name.trim().length() < 3 || !name.matches("^[A-Za-z][A-Za-z0-9 ]+$")) {
            throw new RuntimeException("Invalid category name. It must be at least 3 characters long and contain only letters, numbers, and spaces.");
        }

        if (description == null || description.trim().length() < 5 || !description.matches("^[a-zA-Z0-9 ,.\\-]{5,200}$")) {
            throw new RuntimeException("Invalid description. It must be at least 5 characters long and contain only letters, numbers, commas, periods, hyphens, and spaces.");
        }

        ForumCategory forumCat = new ForumCategory();
        forumCat.setName(forumCatDTO.getName());
        forumCat.setDescription(forumCatDTO.getDescription());
        forumCat.setTopicsCount(0);
        forumCat.setStatus(Objects.equals(forumCatDTO.getStatus(), "on") ? "on" : "off");
        forumCat.setCreatedAt(LocalDateTime.now());
        forumCat.setUpdatedAt(LocalDateTime.now());

        return forumCatRepo.save(forumCat);
    }

    public List<ForumCategory> getAllCategories() {
        return forumCatRepo.findAll();
    }

    public ForumCategory getCategoryById(int id) {
        return forumCatRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum category not found with id: " + id));
    }

    @Transactional
    public ForumCategory updateForumCategory(int id, ForumCatDTO forumCatDTO) {
        ForumCategory existingCat = getCategoryById(id);

        String name = forumCatDTO.getName();
        String description = forumCatDTO.getDescription();

        if (name == null || name.trim().length() < 3 || !name.matches("^[A-Za-z][A-Za-z0-9 ]+$")) {
            throw new RuntimeException("Invalid category name. It must be at least 3 characters long and contain only letters, numbers, and spaces.");
        }

        if (description == null || description.trim().length() < 5 || !description.matches("^[a-zA-Z0-9 ,.\\-]{5,200}$")) {
            throw new RuntimeException("Invalid description. It must be at least 5 characters long and contain only letters, numbers, commas, periods, hyphens, and spaces.");
        }

        existingCat.setName(forumCatDTO.getName());
        existingCat.setDescription(forumCatDTO.getDescription());
        existingCat.setStatus(Objects.equals(forumCatDTO.getStatus(), "on") ? "on" : "off");
        existingCat.setUpdatedAt(LocalDateTime.now());

        return forumCatRepo.save(existingCat);
    }

    @Transactional
    public void deleteForumCategory(int id) {
        ForumCategory category = getCategoryById(id);

        // Business rule: Can't delete if it has content
        if (category.getTopicsCount() > 0) {
            throw new RuntimeException(
                    "Cannot delete category with existing content. Remove topics first.");
        }

        forumCatRepo.delete(category);
    }
}