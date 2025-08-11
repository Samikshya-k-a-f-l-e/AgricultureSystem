package org.agro.agriculturesystem.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class ForumTopicsDTO {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String status;
    private Integer replyCount;
    private int categoryId;
    private int authorId;
    private String tags;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ForumTopicsDTO() {}
    
    public ForumTopicsDTO(Long id, String title, String slug, String content, String status, 
                       Integer replyCount, int categoryId, int authorId, String tags, 
                       LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.content = content;
        this.status = status;
        this.replyCount = replyCount;
        this.categoryId = categoryId;
        this.authorId = authorId;
        this.tags = tags;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
