package org.agro.agriculturesystem.dto;

import java.time.LocalDateTime;

public class ForumCatDTO {
    private String name;
    private String description;
    private int topicsCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ForumCatDTO() {}

    public ForumCatDTO(String name, String description, int topicsCount, String status, 
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.description = description;
        this.topicsCount = topicsCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTopicsCount() {
        return topicsCount;
    }

    public void setTopicsCount(int topicsCount) {
        this.topicsCount = topicsCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumCatDTO that = (ForumCatDTO) o;
        return topicsCount == that.topicsCount &&
               java.util.Objects.equals(name, that.name) &&
               java.util.Objects.equals(description, that.description) &&
               java.util.Objects.equals(status, that.status) &&
               java.util.Objects.equals(createdAt, that.createdAt) &&
               java.util.Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, description, topicsCount, status, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "ForumCatDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", topicsCount=" + topicsCount +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
