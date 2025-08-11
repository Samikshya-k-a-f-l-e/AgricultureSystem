package org.agro.agriculturesystem.dto;

public class GuideCatDTO {
    private String name;
    private String description;
    private int guidesCount = 0;
    private String status;
    private String iconClass;
    private String slug;

    // Constructors
    public GuideCatDTO() {}

    public GuideCatDTO(String name, String description, int guidesCount, String status, String iconClass, String slug) {
        this.name = name;
        this.description = description;
        this.guidesCount = guidesCount;
        this.status = status;
        this.iconClass = iconClass;
        this.slug = slug;
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

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuideCatDTO that = (GuideCatDTO) o;
        return guidesCount == that.guidesCount &&
               java.util.Objects.equals(name, that.name) &&
               java.util.Objects.equals(description, that.description) &&
               java.util.Objects.equals(status, that.status) &&
               java.util.Objects.equals(iconClass, that.iconClass) &&
               java.util.Objects.equals(slug, that.slug);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, description, guidesCount, status, iconClass, slug);
    }

    @Override
    public String toString() {
        return "GuideCatDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", guidesCount=" + guidesCount +
                ", status='" + status + '\'' +
                ", iconClass='" + iconClass + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
