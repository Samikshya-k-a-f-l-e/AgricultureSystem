package org.agro.agriculturesystem.knn;

import java.util.List;

public class PlantDisease {
    private String name;
    private String description;
    private String treatment;
    private List<String> preventionTips;
    private double[] features; // Color and texture features for KNN comparison
    
    // Constructors
    public PlantDisease() {
    }
    
    public PlantDisease(String name, String description, String treatment, List<String> preventionTips, double[] features) {
        this.name = name;
        this.description = description;
        this.treatment = treatment;
        this.preventionTips = preventionTips;
        this.features = features;
    }
    
    // Builder pattern implementation
    public static PlantDiseaseBuilder builder() {
        return new PlantDiseaseBuilder();
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
    
    public String getTreatment() {
        return treatment;
    }
    
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
    
    public List<String> getPreventionTips() {
        return preventionTips;
    }
    
    public void setPreventionTips(List<String> preventionTips) {
        this.preventionTips = preventionTips;
    }
    
    public double[] getFeatures() {
        return features;
    }
    
    public void setFeatures(double[] features) {
        this.features = features;
    }
    
    // Builder class
    public static class PlantDiseaseBuilder {
        private String name;
        private String description;
        private String treatment;
        private List<String> preventionTips;
        private double[] features;
        
        PlantDiseaseBuilder() {
        }
        
        public PlantDiseaseBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public PlantDiseaseBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        public PlantDiseaseBuilder treatment(String treatment) {
            this.treatment = treatment;
            return this;
        }
        
        public PlantDiseaseBuilder preventionTips(List<String> preventionTips) {
            this.preventionTips = preventionTips;
            return this;
        }
        
        public PlantDiseaseBuilder features(double[] features) {
            this.features = features;
            return this;
        }
        
        public PlantDisease build() {
            return new PlantDisease(name, description, treatment, preventionTips, features);
        }
    }
}