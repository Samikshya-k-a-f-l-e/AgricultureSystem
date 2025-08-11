package org.agro.agriculturesystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "guides")
public class FarmGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true ,nullable = false)
    private String slug;

    @Column
    private String excerpt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name ="featured_image")
    private String featuredImage;

    @Column(nullable = true)
    private String category;

    @Column
    private String subcategory;

    private int authorId;

//    @Column(name = "view_count")
//    private int viewCount;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_featured")
    private Boolean featured;

    @Column(name = "featured_position")
    private int featuredPosition;

    public enum GuideStatus { published, draft }
    @Enumerated(EnumType.STRING)
    private GuideStatus status;

    @Column(name = "keywords")
    private String keywords;

    private String sourceUrl;
    private String sourceName;
    
    // Constructors
    public FarmGuide() {}

    public FarmGuide(int id, String title, String slug, String excerpt, String content, String featuredImage,
                 String category, String subcategory, int authorId, String metaTitle, String metaDescription,
                 LocalDateTime createdAt, LocalDateTime updatedAt, Boolean featured, int featuredPosition,
                 GuideStatus status, String keywords, String sourceUrl, String sourceName) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.excerpt = excerpt;
        this.content = content;
        this.featuredImage = featuredImage;
        this.category = category;
        this.subcategory = subcategory;
        this.authorId = authorId;
        this.metaTitle = metaTitle;
        this.metaDescription = metaDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.featured = featured;
        this.featuredPosition = featuredPosition;
        this.status = status;
        this.keywords = keywords;
        this.sourceUrl = sourceUrl;
        this.sourceName = sourceName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
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

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public int getFeaturedPosition() {
        return featuredPosition;
    }

    public void setFeaturedPosition(int featuredPosition) {
        this.featuredPosition = featuredPosition;
    }

    public GuideStatus getStatus() {
        return status;
    }

    public void setStatus(GuideStatus status) {
        this.status = status;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}