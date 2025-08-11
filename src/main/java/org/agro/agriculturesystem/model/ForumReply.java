package org.agro.agriculturesystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "forum_replies")
public class ForumReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "notify_replies")
    private Boolean notifyReplies = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", foreignKey = @ForeignKey(name = "fk_reaction_topic",
            foreignKeyDefinition = "FOREIGN KEY (topic_id) REFERENCES forum_topics(id) ON DELETE CASCADE"))
    private ForumTopics topic;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactions = new HashSet<>();

    // Constructors
    public ForumReply() {}

    public ForumReply(Long id, String content, Boolean notifyReplies, LocalDateTime createdAt, 
                  LocalDateTime updatedAt, User user, ForumTopics topic, Set<Reaction> reactions) {
        this.id = id;
        this.content = content;
        this.notifyReplies = notifyReplies;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.topic = topic;
        this.reactions = reactions;
    }

    // Builder pattern to replace @Builder
    public static ForumReplyBuilder builder() {
        return new ForumReplyBuilder();
    }

    // Static Builder class
    public static class ForumReplyBuilder {
        private Long id;
        private String content;
        private Boolean notifyReplies = true;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();
        private User user;
        private ForumTopics topic;
        private Set<Reaction> reactions = new HashSet<>();

        public ForumReplyBuilder id(Long id) { this.id = id; return this; }
        public ForumReplyBuilder content(String content) { this.content = content; return this; }
        public ForumReplyBuilder notifyReplies(Boolean notifyReplies) { this.notifyReplies = notifyReplies; return this; }
        public ForumReplyBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ForumReplyBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public ForumReplyBuilder user(User user) { this.user = user; return this; }
        public ForumReplyBuilder topic(ForumTopics topic) { this.topic = topic; return this; }
        public ForumReplyBuilder reactions(Set<Reaction> reactions) { this.reactions = reactions; return this; }

        public ForumReply build() {
            return new ForumReply(id, content, notifyReplies, createdAt, updatedAt, user, topic, reactions);
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ForumTopics getTopic() {
        return topic;
    }

    public void setTopic(ForumTopics topic) {
        this.topic = topic;
    }

    public Set<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }
}
