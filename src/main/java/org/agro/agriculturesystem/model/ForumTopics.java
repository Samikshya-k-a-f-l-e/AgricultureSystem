package org.agro.agriculturesystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "forum_topics")
public class ForumTopics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "reply_count", nullable = false)
    private Integer replyCount;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private String tags;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private ForumCategory category;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactions = new HashSet<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ForumReply> forumReply = new HashSet<>();

    // Constructors
    public ForumTopics() {}

    // Custom constructor (replaces @AllArgsConstructor)
    public ForumTopics(Long id, String title, String slug, String content, String status, Integer replyCount,
                   LocalDateTime createdAt, LocalDateTime updatedAt, String tags, ForumCategory category,
                   User author, Set<Reaction> reactions, Set<ForumReply> forumReply) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.content = content;
        this.status = status;
        this.replyCount = replyCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tags = tags;
        this.category = category;
        this.author = author;
        this.reactions = reactions;
        this.forumReply = forumReply;
    }

    // Lifecycle methods
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (replyCount == null) replyCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Builder replacement method
    public static ForumTopicsBuilder builder() {
        return new ForumTopicsBuilder();
    }

    // Static Builder class to replace @Builder
    public static class ForumTopicsBuilder {
        private Long id;
        private String title;
        private String slug;
        private String content;
        private String status;
        private Integer replyCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String tags;
        private ForumCategory category;
        private User author;
        private Set<Reaction> reactions = new HashSet<>();
        private Set<ForumReply> forumReply = new HashSet<>();

        public ForumTopicsBuilder id(Long id) { this.id = id; return this; }
        public ForumTopicsBuilder title(String title) { this.title = title; return this; }
        public ForumTopicsBuilder slug(String slug) { this.slug = slug; return this; }
        public ForumTopicsBuilder content(String content) { this.content = content; return this; }
        public ForumTopicsBuilder status(String status) { this.status = status; return this; }
        public ForumTopicsBuilder replyCount(Integer replyCount) { this.replyCount = replyCount; return this; }
        public ForumTopicsBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ForumTopicsBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public ForumTopicsBuilder tags(String tags) { this.tags = tags; return this; }
        public ForumTopicsBuilder category(ForumCategory category) { this.category = category; return this; }
        public ForumTopicsBuilder author(User author) { this.author = author; return this; }
        public ForumTopicsBuilder reactions(Set<Reaction> reactions) { this.reactions = reactions; return this; }
        public ForumTopicsBuilder forumReply(Set<ForumReply> forumReply) { this.forumReply = forumReply; return this; }

        public ForumTopics build() {
            return new ForumTopics(id, title, slug, content, status, replyCount, createdAt, updatedAt, 
                              tags, category, author, reactions, forumReply);
        }
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ForumCategory getCategory() {
        return category;
    }

    public void setCategory(ForumCategory category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }

    public Set<ForumReply> getForumReply() {
        return forumReply;
    }

    public void setForumReply(Set<ForumReply> forumReply) {
        this.forumReply = forumReply;
    }
}
