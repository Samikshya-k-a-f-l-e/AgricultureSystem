package org.agro.agriculturesystem.knn;

import java.util.List;

public class DiseaseDetectionResult {
    private String disease;
    private int confidence;
    private String description;
    private String treatment;
    private List<String> preventionTips;
    
    // Constructors
    public DiseaseDetectionResult() {
    }
    
    public DiseaseDetectionResult(String disease, int confidence, String description, String treatment, List<String> preventionTips) {
        this.disease = disease;
        this.confidence = confidence;
        this.description = description;
        this.treatment = treatment;
        this.preventionTips = preventionTips;
    }
    
    // Builder pattern implementation
    public static DiseaseDetectionResultBuilder builder() {
        return new DiseaseDetectionResultBuilder();
    }
    
    // Getters and Setters
    public String getDisease() {
        return disease;
    }
    
    public void setDisease(String disease) {
        this.disease = disease;
    }
    
    public int getConfidence() {
        return confidence;
    }
    
    public void setConfidence(int confidence) {
        this.confidence = confidence;
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
    
    // Builder class
    public static class DiseaseDetectionResultBuilder {
        private String disease;
        private int confidence;
        private String description;
        private String treatment;
        private List<String> preventionTips;
        
        DiseaseDetectionResultBuilder() {
        }
        
        public DiseaseDetectionResultBuilder disease(String disease) {
            this.disease = disease;
            return this;
        }
        
        public DiseaseDetectionResultBuilder confidence(int confidence) {
            this.confidence = confidence;
            return this;
        }
        
        public DiseaseDetectionResultBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        public DiseaseDetectionResultBuilder treatment(String treatment) {
            this.treatment = treatment;
            return this;
        }
        
        public DiseaseDetectionResultBuilder preventionTips(List<String> preventionTips) {
            this.preventionTips = preventionTips;
            return this;
        }
        
        public DiseaseDetectionResult build() {
            return new DiseaseDetectionResult(disease, confidence, description, treatment, preventionTips);
        }
    }
}