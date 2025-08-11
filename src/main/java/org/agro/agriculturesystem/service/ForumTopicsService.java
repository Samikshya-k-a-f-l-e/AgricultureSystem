package org.agro.agriculturesystem.service;

import org.agro.agriculturesystem.dto.ForumTopicsDTO;
import org.agro.agriculturesystem.model.ForumCategory;
import org.agro.agriculturesystem.model.ForumTopics;
import org.agro.agriculturesystem.model.User;
import org.agro.agriculturesystem.repository.ForumCatRepo;
import org.agro.agriculturesystem.repository.ForumTopicsRepo;
import org.agro.agriculturesystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ForumTopicsService {

    private final ForumTopicsRepo topicsRepo;
    private final ForumCatRepo catRepo;
    private final UserRepository userRepo;

    public ForumTopicsService(ForumTopicsRepo topicsRepo, ForumCatRepo catRepo, UserRepository userRepo) {
        this.topicsRepo = topicsRepo;
        this.catRepo = catRepo;
        this.userRepo = userRepo;
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    @Transactional
    public ForumTopics createForumTopic(ForumTopicsDTO dto) {
        String title = dto.getTitle();
        String content = dto.getContent();
        String tags = dto.getTags();

        if (topicsRepo.findByTitle(dto.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Topic title must be unique");
        }

        if (title == null || title.trim().length() < 5 || !title.matches("^[A-Za-z0-9 ,.\\-!?']{5,100}$")) {
            throw new IllegalArgumentException("Invalid title. It must be 5–100 characters long and can include letters, numbers, basic punctuation, and spaces.");
        }

        if (content == null || content.trim().length() < 10) {
            throw new IllegalArgumentException("Content must be at least 10 characters long.");
        }

        if (tags != null && !tags.matches("^[a-zA-Z0-9,\\- ]{0,100}$")) {
            throw new IllegalArgumentException("Tags can only contain letters, numbers, commas, hyphens, and spaces.");
        }

        ForumCategory category = catRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        User author = userRepo.findById(dto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return topicsRepo.save(
                ForumTopics.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .slug(generateSlug(dto.getTitle()))
                        .status("active")
                        .replyCount(0)
                        .tags(dto.getTags())
                        .category(category)
                        .author(author)
                        .build()
        );
    }

    public ForumTopics getTopicById(Long id) {
        return topicsRepo.findById(id).orElseThrow(() -> new RuntimeException("Topic not found"));
    }

    public ForumTopics getTopicBySlug(String slug) {
        return topicsRepo.findBySlug(slug);
    }

    public Page<ForumTopics> getAllTopics(Pageable pageable) {
        return topicsRepo.findAll(pageable);
    }

    public long getTotalTopics() {
        return topicsRepo.count();
    }

    @Transactional
    public void deleteTopic(Long id) {
        topicsRepo.deleteById(id);
    }

    @Transactional
    public void updateForumTopic(Long id, ForumTopicsDTO dto) {
        ForumTopics existingTopic = topicsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with id: " + id));

        String title = dto.getTitle();
        String content = dto.getContent();
        String tags = dto.getTags();

        if (title == null || title.trim().length() < 5 || !title.matches("^[A-Za-z0-9 ,.\\-!?']{5,100}$")) {
            throw new IllegalArgumentException("Invalid title. It must be 5–100 characters long and include allowed characters.");
        }

        if (!existingTopic.getTitle().equals(title) && topicsRepo.findByTitle(title).isPresent()) {
            throw new IllegalArgumentException("Topic title must be unique");
        }

        if (content == null || content.trim().length() < 10) {
            throw new IllegalArgumentException("Content must be at least 10 characters");
        }

        if (tags != null && !tags.matches("^[a-zA-Z0-9,\\- ]{0,100}$")) {
            throw new IllegalArgumentException("Tags must use only allowed characters");
        }

        existingTopic.setTitle(title);
        existingTopic.setContent(content);
        existingTopic.setTags(tags);
        existingTopic.setSlug(generateSlug(title));

        topicsRepo.save(existingTopic);
    }

}
