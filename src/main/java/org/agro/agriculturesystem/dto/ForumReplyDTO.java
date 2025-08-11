package org.agro.agriculturesystem.dto;

import java.time.LocalDateTime;

public class ForumReplyDTO {
    private Long id;
    private String content;
    private Boolean notifyReplies;
    private Long topicId;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ForumReplyDTO() {}

    public ForumReplyDTO(Long id, String content, Boolean notifyReplies, Long topicId, int userId,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.notifyReplies = notifyReplies;
        this.topicId = topicId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getNotifyReplies() {
        return notifyReplies;
    }

    public void setNotifyReplies(Boolean notifyReplies) {
        this.notifyReplies = notifyReplies;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
