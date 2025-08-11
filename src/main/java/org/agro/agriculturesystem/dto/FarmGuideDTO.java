package org.agro.agriculturesystem.dto;

import org.agro.agriculturesystem.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class FarmGuideDTO {
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private MultipartFile featuredImage;
    private String category;
    private String subcategory;
    private int authorId;
    private String metaTitle;
    private String metaDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean featured;
    private int featuredPosition = 1;
    private String status;
    private String keywords;
    private String sourceUrl;
    private String sourceName;

    // Constructors
    public FarmGuideDTO() {}

    public FarmGuideDTO(String title, String slug, String excerpt, String content, MultipartFile featuredImage,
                    String category, String subcategory, int authorId, String metaTitle, String metaDescription,
                    LocalDateTime createdAt, LocalDateTime updatedAt, Boolean featured, int featuredPosition,
                    String status, String keywords, String sourceUrl, String sourceName) {
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

    public MultipartFile getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(MultipartFile featuredImage) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FarmGuideDTO that = (FarmGuideDTO) o;
        return authorId == that.authorId &&
               featuredPosition == that.featuredPosition &&
               java.util.Objects.equals(title, that.title) &&
               java.util.Objects.equals(slug, that.slug) &&
               java.util.Objects.equals(excerpt, that.excerpt) &&
               java.util.Objects.equals(content, that.content) &&
               java.util.Objects.equals(featuredImage, that.featuredImage) &&
               java.util.Objects.equals(category, that.category) &&
               java.util.Objects.equals(subcategory, that.subcategory) &&
               java.util.Objects.equals(metaTitle, that.metaTitle) &&
               java.util.Objects.equals(metaDescription, that.metaDescription) &&
               java.util.Objects.equals(createdAt, that.createdAt) &&
               java.util.Objects.equals(updatedAt, that.updatedAt) &&
               java.util.Objects.equals(featured, that.featured) &&
               java.util.Objects.equals(status, that.status) &&
               java.util.Objects.equals(keywords, that.keywords) &&
               java.util.Objects.equals(sourceUrl, that.sourceUrl) &&
               java.util.Objects.equals(sourceName, that.sourceName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(title, slug, excerpt, content, featuredImage, category, subcategory, 
                              authorId, metaTitle, metaDescription, createdAt, updatedAt, featured, 
                              featuredPosition, status, keywords, sourceUrl, sourceName);
    }

    @Override
    public String toString() {
        return "FarmGuideDTO{" +
                "title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", content='" + content + '\'' +
                ", featuredImage=" + featuredImage +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", authorId=" + authorId +
                ", metaTitle='" + metaTitle + '\'' +
                ", metaDescription='" + metaDescription + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", featured=" + featured +
                ", featuredPosition=" + featuredPosition +
                ", status='" + status + '\'' +
                ", keywords='" + keywords + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", sourceName='" + sourceName + '\'' +
                '}';
    }
}
