package org.agro.agriculturesystem.knn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class DiseaseDetectionService {

    private KnnAlgorithm knnAlgorithm;
    private DiseaseDataService diseaseDataService;
    
    @Autowired
    public DiseaseDetectionService(KnnAlgorithm knnAlgorithm, DiseaseDataService diseaseDataService) {
        this.knnAlgorithm = knnAlgorithm;
        this.diseaseDataService = diseaseDataService;
    }

    public DiseaseDetectionResult detectDisease(MultipartFile imageFile) throws IOException {
        // 1. Load the image
        BufferedImage image = ImageIO.read(imageFile.getInputStream());

        // 2. Extract features from the image
        double[] features = extractFeatures(image);

        // 3. Use KNN to find the most similar disease
        List<PlantDisease> diseases = diseaseDataService.getAllDiseases();
        PlantDisease detectedDisease = knnAlgorithm.classify(features, diseases);

        // 4. Calculate confidence (simplified for demo)
        int confidence = calculateConfidence(features, detectedDisease.getFeatures());

        // 5. Return the result
        return DiseaseDetectionResult.builder()
                .disease(detectedDisease.getName())
                .confidence(confidence)
                .description(detectedDisease.getDescription())
                .treatment(detectedDisease.getTreatment())
                .preventionTips(detectedDisease.getPreventionTips())
                .build();
    }

    private double[] extractFeatures(BufferedImage image) {
        // Resize image for consistent processing
        BufferedImage resizedImage = resizeImage(image, 100, 100);

        // Extract color histogram features
        double[] colorFeatures = extractColorHistogram(resizedImage);

        // Extract texture features
        double[] textureFeatures = extractTextureFeatures(resizedImage);

        // Combine features
        double[] combinedFeatures = new double[colorFeatures.length + textureFeatures.length];
        System.arraycopy(colorFeatures, 0, combinedFeatures, 0, colorFeatures.length);
        System.arraycopy(textureFeatures, 0, combinedFeatures, colorFeatures.length, textureFeatures.length);

        return combinedFeatures;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private double[] extractColorHistogram(BufferedImage image) {
        // Simple RGB histogram with 8 bins per channel
        int binsPerChannel = 8;
        int totalBins = binsPerChannel * 3; // R, G, B
        double[] histogram = new double[totalBins];

        int width = image.getWidth();
        int height = image.getHeight();
        int pixelCount = width * height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Calculate bin indices
                int rBin = r * binsPerChannel / 256;
                int gBin = g * binsPerChannel / 256;
                int bBin = b * binsPerChannel / 256;

                // Increment histogram bins
                histogram[rBin]++;
                histogram[gBin + binsPerChannel]++;
                histogram[bBin + 2 * binsPerChannel]++;
            }
        }

        // Normalize histogram
        for (int i = 0; i < totalBins; i++) {
            histogram[i] /= pixelCount;
        }

        return histogram;
    }

    private double[] extractTextureFeatures(BufferedImage image) {
        // Simple texture features: standard deviation of pixel intensities in regions
        int regions = 4; // 2x2 grid
        double[] textureFeatures = new double[regions];

        int width = image.getWidth();
        int height = image.getHeight();
        int regionWidth = width / 2;
        int regionHeight = height / 2;

        for (int regionY = 0; regionY < 2; regionY++) {
            for (int regionX = 0; regionX < 2; regionX++) {
                int startX = regionX * regionWidth;
                int startY = regionY * regionHeight;
                int regionIndex = regionY * 2 + regionX;

                // Calculate standard deviation of pixel intensities in this region
                double sum = 0;
                double sumSquared = 0;
                int count = 0;

                for (int y = startY; y < startY + regionHeight && y < height; y++) {
                    for (int x = startX; x < startX + regionWidth && x < width; x++) {
                        int rgb = image.getRGB(x, y);
                        // Convert to grayscale
                        int gray = (int)(0.299 * ((rgb >> 16) & 0xFF) +
                                0.587 * ((rgb >> 8) & 0xFF) +
                                0.114 * (rgb & 0xFF));

                        sum += gray;
                        sumSquared += gray * gray;
                        count++;
                    }
                }

                double mean = sum / count;
                double variance = (sumSquared / count) - (mean * mean);
                textureFeatures[regionIndex] = Math.sqrt(variance); // Standard deviation
            }
        }

        return textureFeatures;
    }

    private int calculateConfidence(double[] queryFeatures, double[] referenceFeatures) {
        // Calculate similarity and convert to confidence percentage
        double distance = knnAlgorithm.calculateDistance(queryFeatures, referenceFeatures);
        double maxDistance = Math.sqrt(queryFeatures.length); // Maximum possible distance

        // Convert distance to similarity (0-1 range)
        double similarity = 1.0 - (distance / maxDistance);

        // Convert to confidence percentage
        int confidence = (int) Math.round(similarity * 100);

        // Add some randomness for demo purposes
        Random random = new Random();
        confidence = Math.min(100, Math.max(60, confidence + random.nextInt(21) - 10));

        return confidence;
    }
}