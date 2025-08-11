package org.agro.agriculturesystem.model;

import jakarta.persistence.*;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String phone;

    private String experience;

    @Column(name = "profile_image")
    private String profileImage;

    private String specialization;
    
    private String bio;
    
    private String location;
    
    private String website;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    
    public Profile() {
    }
    
    public static ProfileBuilder builder() {
        return new ProfileBuilder();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public static class ProfileBuilder {
        private Long id;
        private String fullName;
        private String phone;
        private String experience;
        private String profileImage;
        private String specialization;
        private String bio;
        private String location;
        private String website;
        private User user;
        
        public ProfileBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public ProfileBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        
        public ProfileBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public ProfileBuilder experience(String experience) {
            this.experience = experience;
            return this;
        }
        
        public ProfileBuilder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }
        
        public ProfileBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }
        
        public ProfileBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }
        
        public ProfileBuilder location(String location) {
            this.location = location;
            return this;
        }
        
        public ProfileBuilder website(String website) {
            this.website = website;
            return this;
        }
        
        public ProfileBuilder user(User user) {
            this.user = user;
            return this;
        }
        
        public Profile build() {
            Profile profile = new Profile();
            profile.id = this.id;
            profile.fullName = this.fullName;
            profile.phone = this.phone;
            profile.experience = this.experience;
            profile.profileImage = this.profileImage;
            profile.specialization = this.specialization;
            profile.bio = this.bio;
            profile.location = this.location;
            profile.website = this.website;
            profile.user = this.user;
            return profile;
        }
    }
}
