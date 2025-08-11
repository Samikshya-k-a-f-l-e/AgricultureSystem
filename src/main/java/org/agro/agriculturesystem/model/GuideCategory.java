package org.agro.agriculturesystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "guide_categories")
public class GuideCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_name", unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "guides_count")
    private int guidesCount;

    @Column
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "icon_class")
    private String iconClass;

    @Column
    private String slug;
    
    // Constructors
    public GuideCategory() {}

    public GuideCategory(int id, String name, String description, int guidesCount, String status, 
                         LocalDateTime createdAt, String iconClass, String slug) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.guidesCount = guidesCount;
        this.status = status;
        this.createdAt = createdAt;
        this.iconClass = iconClass;
        this.slug = slug;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getGuidesCount() {
        return guidesCount;
    }

    public void setGuidesCount(int guidesCount) {
        this.guidesCount = guidesCount;
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

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}