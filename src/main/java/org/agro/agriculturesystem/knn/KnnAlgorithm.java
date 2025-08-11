package org.agro.agriculturesystem.knn;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Custom implementation of K-Nearest Neighbors algorithm for plant disease classification
 */
@Component
public class KnnAlgorithm {

    private static final int K = 3; // Number of neighbors to consider

    /**
     * Classifies a plant disease based on extracted features using KNN algorithm
     *
     * @param queryFeatures Features extracted from the uploaded image
     * @param diseases List of known plant diseases with their features
     * @return The most likely plant disease
     */
    public PlantDisease classify(double[] queryFeatures, List<PlantDisease> diseases) {
        if (diseases.isEmpty()) {
            throw new IllegalArgumentException("Disease database is empty");
        }

        // Calculate distances to all known diseases
        List<DiseaseDistance> distances = new ArrayList<>();
        for (PlantDisease disease : diseases) {
            double distance = calculateDistance(queryFeatures, disease.getFeatures());
            distances.add(new DiseaseDistance(disease, distance));
        }

        // Sort by distance (ascending)
        distances.sort(Comparator.comparingDouble(DiseaseDistance::getDistance));

        // Take K nearest neighbors
        int k = Math.min(K, distances.size());
        List<DiseaseDistance> nearestNeighbors = distances.subList(0, k);

        // Count votes for each disease
        Map<String, Integer> votes = new HashMap<>();
        for (DiseaseDistance neighbor : nearestNeighbors) {
            String diseaseName = neighbor.getDisease().getName();
            votes.put(diseaseName, votes.getOrDefault(diseaseName, 0) + 1);
        }

        // Find the disease with the most votes
        String mostVotedDisease = null;
        int maxVotes = 0;
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                mostVotedDisease = entry.getKey();
            }
        }

        // Return the disease with the most votes
        for (PlantDisease disease : diseases) {
            if (disease.getName().equals(mostVotedDisease)) {
                return disease;
            }
        }

        // Fallback to the nearest neighbor if voting fails
        return distances.get(0).getDisease();
    }

    /**
     * Calculates Euclidean distance between two feature vectors
     */
    public double calculateDistance(double[] features1, double[] features2) {
        if (features1.length != features2.length) {
            throw new IllegalArgumentException("Feature vectors must have the same length");
        }

        double sum = 0.0;
        for (int i = 0; i < features1.length; i++) {
            double diff = features1[i] - features2[i];
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }

    /**
     * Helper class to store a disease and its distance
     */
    private static class DiseaseDistance {
        private final PlantDisease disease;
        private final double distance;

        public DiseaseDistance(PlantDisease disease, double distance) {
            this.disease = disease;
            this.distance = distance;
        }

        public PlantDisease getDisease() {
            return disease;
        }

        public double getDistance() {
            return distance;
        }
    }
}