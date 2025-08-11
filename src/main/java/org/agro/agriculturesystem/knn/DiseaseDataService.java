package org.agro.agriculturesystem.knn;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Service to manage plant disease data
 * In a real application, this would connect to a database
 */
@Service
public class DiseaseDataService {

    private List<PlantDisease> diseases = new ArrayList<>();
    private Random random = new Random();

    @PostConstruct
    public void initializeDiseaseData() {
        // Create mock disease data with features
        // In a real application, these features would be extracted from training images

        // Late Blight (Potato/Tomato)
        diseases.add(PlantDisease.builder()
                .name("Late Blight")
                .description("Late blight is a destructive disease affecting potatoes and tomatoes. It's caused by the fungus-like organism Phytophthora infestans and can destroy entire fields within days under favorable conditions.")
                .treatment("Apply copper-based fungicides as soon as symptoms are detected. Remove and destroy infected plant parts. In severe cases, consider using systemic fungicides recommended by local agricultural extension services.")
                .preventionTips(Arrays.asList(
                        "Plant resistant varieties when available",
                        "Ensure good air circulation by proper spacing",
                        "Avoid overhead irrigation",
                        "Practice crop rotation",
                        "Remove volunteer plants that may harbor the disease"
                ))
                .features(generateMockFeatures(0))
                .build());

        // Powdery Mildew
        diseases.add(PlantDisease.builder()
                .name("Powdery Mildew")
                .description("Powdery mildew is a fungal disease that appears as white powdery spots on leaves, stems, and sometimes fruit. It thrives in warm, humid conditions and can reduce yield and quality.")
                .treatment("Apply sulfur-based fungicides or neem oil at first sign of infection. Prune affected areas to improve air circulation. For organic options, try a solution of 1 tablespoon baking soda, 1 teaspoon mild soap, and 1 gallon of water.")
                .preventionTips(Arrays.asList(
                        "Plant resistant varieties",
                        "Ensure proper spacing for good air circulation",
                        "Avoid excessive nitrogen fertilization",
                        "Water at the base of plants, not on foliage",
                        "Clean up plant debris at the end of the season"
                ))
                .features(generateMockFeatures(1))
                .build());

        // Bacterial Leaf Spot
        diseases.add(PlantDisease.builder()
                .name("Bacterial Leaf Spot")
                .description("Bacterial leaf spot causes water-soaked lesions on leaves that eventually turn brown with yellow halos. The disease is caused by various species of bacteria and can spread rapidly in wet conditions.")
                .treatment("Remove and destroy infected plant parts. Apply copper-based bactericides according to label instructions. Avoid working with plants when they're wet to prevent spreading the bacteria.")
                .preventionTips(Arrays.asList(
                        "Use disease-free seeds and transplants",
                        "Rotate crops with non-susceptible plants",
                        "Avoid overhead irrigation",
                        "Disinfect garden tools regularly",
                        "Maintain proper plant spacing"
                ))
                .features(generateMockFeatures(2))
                .build());

        // Leaf Rust
        diseases.add(PlantDisease.builder()
                .name("Leaf Rust")
                .description("Leaf rust is a fungal disease characterized by orange-brown pustules on leaf surfaces. It affects various crops including wheat, corn, and beans, reducing photosynthesis and yield.")
                .treatment("Apply fungicides containing triazoles or strobilurins at first sign of infection. For organic management, use sulfur-based products or neem oil. Remove severely infected plants to prevent spread.")
                .preventionTips(Arrays.asList(
                        "Plant rust-resistant varieties",
                        "Increase plant spacing for better air circulation",
                        "Avoid working in fields when plants are wet",
                        "Practice crop rotation with non-host plants",
                        "Remove plant debris after harvest"
                ))
                .features(generateMockFeatures(3))
                .build());

        // Anthracnose
        diseases.add(PlantDisease.builder()
                .name("Anthracnose")
                .description("Anthracnose is a fungal disease that causes dark, sunken lesions on leaves, stems, fruits, and flowers. It thrives in warm, wet conditions and can affect a wide range of crops including mango, banana, and legumes.")
                .treatment("Apply copper-based fungicides or commercial fungicides containing mancozeb. Prune infected parts and ensure proper sanitation. Improve air circulation around plants.")
                .preventionTips(Arrays.asList(
                        "Use disease-free seeds and planting material",
                        "Maintain proper plant spacing",
                        "Avoid overhead irrigation",
                        "Practice crop rotation",
                        "Apply balanced fertilization"
                ))
                .features(generateMockFeatures(4))
                .build());

        // Mosaic Virus
        diseases.add(PlantDisease.builder()
                .name("Mosaic Virus")
                .description("Mosaic viruses cause mottled patterns of yellow and green on leaves, stunted growth, and deformed fruits. They affect many crops including tomatoes, cucumbers, and tobacco, and are spread by insects like aphids.")
                .treatment("There is no cure for viral diseases. Remove and destroy infected plants to prevent spread. Control insect vectors using appropriate insecticides or organic alternatives like neem oil.")
                .preventionTips(Arrays.asList(
                        "Use virus-free certified seeds",
                        "Control insect vectors like aphids and leafhoppers",
                        "Wash hands and disinfect tools between handling plants",
                        "Remove weeds that may harbor the virus",
                        "Use reflective mulches to repel insects"
                ))
                .features(generateMockFeatures(5))
                .build());
    }

    /**
     * Generate mock feature vectors for demonstration purposes
     * In a real application, these would be extracted from training images
     */
    private double[] generateMockFeatures(int diseaseIndex) {
        // Create feature vector with 28 elements (24 for color histogram + 4 for texture)
        double[] features = new double[28];

        // Set base values based on disease index to make them distinct
        for (int i = 0; i < features.length; i++) {
            // Base value depends on disease index
            double baseValue = (diseaseIndex * 0.1) + (i * 0.01);
            // Add some randomness
            features[i] = baseValue + (random.nextDouble() * 0.05);
        }

        return features;
    }

    /**
     * Returns all available plant diseases
     */
    public List<PlantDisease> getAllDiseases() {
        return diseases;
    }
}