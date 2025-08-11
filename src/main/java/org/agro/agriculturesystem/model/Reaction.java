package org.agro.agriculturesystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "topic_id", "reaction_type"}),
                @UniqueConstraint(columnNames = {"user_id", "reply_id", "reaction_type"})
        })
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reaction_type", nullable = false)
    private String reactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", foreignKey = @ForeignKey(name = "fk_reaction_topic",
            foreignKeyDefinition = "FOREIGN KEY (topic_id) REFERENCES forum_topics(id) ON DELETE CASCADE"))
    private ForumTopics topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", foreignKey = @ForeignKey(name = "fk_reaction_reply",
            foreignKeyDefinition = "FOREIGN KEY (reply_id) REFERENCES forum_replies(id) ON DELETE CASCADE"))
    private ForumReply reply;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Reaction() {
    }
    
    public Reaction(Long id, String reactionType, User user, ForumTopics topic, ForumReply reply, LocalDateTime createdAt) {
        this.id = id;
        this.reactionType = reactionType;
        this.user = user;
        this.topic = topic;
        this.reply = reply;
        this.createdAt = createdAt;
    }
    
    // Builder pattern implementation
    public static ReactionBuilder builder() {
        return new ReactionBuilder();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
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

    public ForumReply getReply() {
        return reply;
    }

    public void setReply(ForumReply reply) {
        this.reply = reply;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Builder class
    public static class ReactionBuilder {
        private Long id;
        private String reactionType;
        private User user;
        private ForumTopics topic;
        private ForumReply reply;
        private LocalDateTime createdAt = LocalDateTime.now();
        
        ReactionBuilder() {
        }
        
        public ReactionBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public ReactionBuilder reactionType(String reactionType) {
            this.reactionType = reactionType;
            return this;
        }
        
        public ReactionBuilder user(User user) {
            this.user = user;
            return this;
        }
        
        public ReactionBuilder topic(ForumTopics topic) {
            this.topic = topic;
            return this;
        }
        
        public ReactionBuilder reply(ForumReply reply) {
            this.reply = reply;
            return this;
        }
        
        public ReactionBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Reaction build() {
            return new Reaction(id, reactionType, user, topic, reply, createdAt);
        }
    }
}
