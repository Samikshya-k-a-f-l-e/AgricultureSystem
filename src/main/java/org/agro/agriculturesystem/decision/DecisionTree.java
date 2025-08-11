//package org.agro.agriculturesystem.decision;
//
//import java.util.*;
//
//public class DecisionTree {
//    private Node root;
//    private final int MAX_DEPTH = 6;
//    private final int MIN_SAMPLES_SPLIT = 2;
//
//    // Define feature priority order with usage limits
//    private final List<String> FEATURE_PRIORITY = Arrays.asList("Commodity", "Month", "Year");
//    private final Map<String, Integer> FEATURE_USAGE_LIMITS = Map.of(
//            "Month", 3,
//            "Year", 3
//    );
//
//    public void buildTree(List<Map<String, Object>> data) {
//        if (data == null || data.isEmpty()) {
//            throw new IllegalArgumentException("Cannot build tree with empty data");
//        }
//        List<String> features = new ArrayList<>(data.get(0).keySet());
//        features.remove("Average");
//        System.out.println("Building tree with features: " + features);
//
//        // Initialize feature usage tracking
//        Map<String, Integer> featureUsageCount = new HashMap<>();
//        for (String feature : features) {
//            featureUsageCount.put(feature, 0);
//        }
//
//        root = buildTreeRecursive(data, features, 0, featureUsageCount);
//    }
//
//    private Node buildTreeRecursive(List<Map<String, Object>> data, List<String> features,
//                                    int depth, Map<String, Integer> featureUsageCount) {
//        // Base cases
//        if (depth >= MAX_DEPTH || data.size() < MIN_SAMPLES_SPLIT || features.isEmpty()) {
//            return createLeafNode(data);
//        }
//
//        // Find the best feature and split point with usage tracking
//        Map.Entry<String, Object> bestSplit = findBestSplit(data, features, featureUsageCount);
//        if (bestSplit == null) {
//            return createLeafNode(data);
//        }
//
//        String bestFeature = bestSplit.getKey();
//        Object splitValue = bestSplit.getValue();
//
//        // Create decision node
//        Node node = new Node();
//        node.setFeature(bestFeature);
//        node.setSplitValue(isNumeric(splitValue) ? splitValue : normalizeString(splitValue));
//
//        // Split the data
//        Map<String, List<Map<String, Object>>> splitData = splitData(data, bestFeature, splitValue);
//
//        // Ensure both splits have data
//        if (splitData.get("left").isEmpty() || splitData.get("right").isEmpty()) {
//            return createLeafNode(data);
//        }
//
//        // Update feature usage count
//        Map<String, Integer> updatedUsageCount = new HashMap<>(featureUsageCount);
//        updatedUsageCount.put(bestFeature, updatedUsageCount.get(bestFeature) + 1);
//
//        // Get available features for next level (considering usage limits)
//        List<String> remainingFeatures = getAvailableFeatures(features, updatedUsageCount);
//
//        node.setLeft(buildTreeRecursive(splitData.get("left"), remainingFeatures, depth + 1, updatedUsageCount));
//        node.setRight(buildTreeRecursive(splitData.get("right"), remainingFeatures, depth + 1, updatedUsageCount));
//
//        return node;
//    }
//
//    private List<String> getAvailableFeatures(List<String> allFeatures, Map<String, Integer> usageCount) {
//        List<String> availableFeatures = new ArrayList<>();
//
//        for (String feature : allFeatures) {
//            int currentUsage = usageCount.getOrDefault(feature, 0);
//            Integer usageLimit = FEATURE_USAGE_LIMITS.get(feature);
//
//            // If no usage limit is defined, feature can be used unlimited times
//            if (usageLimit == null || currentUsage < usageLimit) {
//                availableFeatures.add(feature);
//            }
//        }
//
//        return availableFeatures;
//    }
//
//    // Helper method for case normalization
//    private String normalizeString(Object value) {
//        return value.toString().toLowerCase().trim();
//    }
//
//    private Map.Entry<String, Object> findBestSplit(List<Map<String, Object>> data,
//                                                    List<String> features,
//                                                    Map<String, Integer> featureUsageCount) {
//        double bestGain = -1;
//        Map.Entry<String, Object> bestSplit = null;
//
//        // Create ordered list of features based on priority and availability
//        List<String> orderedFeatures = getOrderedAvailableFeatures(features, featureUsageCount);
//
////        System.out.println("Available features for this split: " + orderedFeatures);
////        System.out.println("Current feature usage: " + featureUsageCount);
//
//        // Process features in priority order
//        for (String feature : orderedFeatures) {
//            Map.Entry<String, Object> featureBestSplit = findBestSplitForFeature(data, feature);
//
//            if (featureBestSplit != null) {
//                double gain = calculateInformationGain(data, feature, featureBestSplit.getValue());
//
//                // Get threshold and priority bonus for this feature
//                double threshold = getThresholdForFeature(feature, featureUsageCount.getOrDefault(feature, 0));
//                double priorityBonus = getPriorityBonus(feature, featureUsageCount.getOrDefault(feature, 0));
//
//                // Apply priority bonus to gain
//                double adjustedGain = gain + priorityBonus;
//
//                if (adjustedGain > bestGain && gain > threshold) {
//                    bestGain = adjustedGain;
//                    bestSplit = featureBestSplit;
//
//                    // For high-priority features, accept good splits early
//                    if (isPriorityFeature(feature) && gain > threshold) {
//                        System.out.println("Priority feature split: " + feature + " = " +
//                                bestSplit.getValue() + " (gain: " + gain +
//                                ", adjusted: " + adjustedGain +
//                                ", usage: " + featureUsageCount.getOrDefault(feature, 0) + ")");
//                        return bestSplit;
//                    }
//                }
//            }
//        }
//
//        if (bestSplit != null) {
//            System.out.println("Best split: " + bestSplit.getKey() + " = " + bestSplit.getValue() +
//                    " (gain: " + (bestGain - getPriorityBonus(bestSplit.getKey(),
//                    featureUsageCount.getOrDefault(bestSplit.getKey(), 0))) +
//                    ", usage: " + featureUsageCount.getOrDefault(bestSplit.getKey(), 0) + ")");
//        }
//        return bestSplit;
//    }
//
//    private List<String> getOrderedAvailableFeatures(List<String> availableFeatures,
//                                                     Map<String, Integer> featureUsageCount) {
//        List<String> orderedFeatures = new ArrayList<>();
//
//        // Add priority features first (in order) if they haven't exceeded usage limits
//        for (String priorityFeature : FEATURE_PRIORITY) {
//            if (availableFeatures.contains(priorityFeature)) {
//                int currentUsage = featureUsageCount.getOrDefault(priorityFeature, 0);
//                Integer usageLimit = FEATURE_USAGE_LIMITS.get(priorityFeature);
//
//                if (usageLimit == null || currentUsage < usageLimit) {
//                    orderedFeatures.add(priorityFeature);
//                }
//            }
//        }
//
//        // Add remaining features that haven't exceeded limits
//        for (String feature : availableFeatures) {
//            if (!orderedFeatures.contains(feature)) {
//                int currentUsage = featureUsageCount.getOrDefault(feature, 0);
//                Integer usageLimit = FEATURE_USAGE_LIMITS.get(feature);
//
//                if (usageLimit == null || currentUsage < usageLimit) {
//                    orderedFeatures.add(feature);
//                }
//            }
//        }
//
//        return orderedFeatures;
//    }
//
//    private boolean isPriorityFeature(String feature) {
//        return FEATURE_PRIORITY.contains(feature);
//    }
//
//    private double getThresholdForFeature(String feature, int currentUsage) {
//        // Lower thresholds for priority features, but increase with usage
//        switch (feature) {
//            case "Commodity":
//                return 0.05;  // Always very low threshold
//            case "Month":
//                // Decrease threshold for first few uses (encourage 3-month intervals)
//                return Math.max(0.06 - (currentUsage * 0.01), 0.04);
//            case "Year":
//                // Low threshold for first 2 uses
//                return currentUsage < 2 ? 0.08 : 0.20;
//            case "Weather":
//                return currentUsage < 1 ? 0.10 : 0.25;
//            default:
//                return 0.15;  // Higher threshold for other features
//        }
//    }
//
//    private double getPriorityBonus(String feature, int currentUsage) {
//        // Give bonus to priority features to encourage their selection
//        switch (feature) {
//            case "Commodity":
//                return 0.10;  // Always high bonus
//            case "Month":
//                // Higher bonus for first 3 uses
//                return currentUsage < 3 ? 0.08 - (currentUsage * 0.02) : 0.0;
//            case "Year":
//                // Bonus for first 2 uses
//                return currentUsage < 2 ? 0.06 - (currentUsage * 0.02) : 0.0;
//            case "Weather":
//                // Bonus for first use
//                return currentUsage < 1 ? 0.04 : 0.0;
//            default:
//                return 0.0;  // No bonus for other features
//        }
//    }
//
//    private Map.Entry<String, Object> findBestSplitForFeature(List<Map<String, Object>> data, String feature) {
//        double bestGain = -1;
//        Object bestSplitValue = null;
//
//        boolean isNumeric = data.stream()
//                .anyMatch(d -> d.get(feature) != null && isNumeric(d.get(feature)));
//
//        Set<Object> uniqueValues = new HashSet<>();
//
//        for (Map<String, Object> dataPoint : data) {
//            Object value = dataPoint.get(feature);
//            if (value != null) {
//                uniqueValues.add(isNumeric ? value : normalizeString(value.toString()));
//            }
//        }
//
//        if (uniqueValues.size() < 2) return null;
//
//        if (isNumeric) {
//            List<Double> numericValues = uniqueValues.stream()
//                    .filter(Number.class::isInstance)
//                    .map(v -> ((Number)v).doubleValue())
//                    .sorted()
//                    .toList();
//
//            for (int i = 0; i < numericValues.size() - 1; i++) {
//                double splitValue = (numericValues.get(i) + numericValues.get(i + 1)) / 2;
//                double gain = calculateInformationGain(data, feature, splitValue);
//                if (gain > bestGain) {
//                    bestGain = gain;
//                    bestSplitValue = splitValue;
//                }
//            }
//        } else {
//            for (Object value : uniqueValues) {
//                double gain = calculateInformationGain(data, feature, value);
//                if (gain > bestGain) {
//                    bestGain = gain;
//                    bestSplitValue = value;
//                }
//            }
//        }
//
//        return bestSplitValue != null ? new AbstractMap.SimpleEntry<>(feature, bestSplitValue) : null;
//    }
//
//    private double calculateInformationGain(List<Map<String, Object>> data, String feature, Object splitValue) {
//        // Calculate variance before split
//        double varianceBefore = calculateVariance(data);
//
//        // Split the data
//        Map<String, List<Map<String, Object>>> splitData = splitData(data, feature, splitValue);
//
//        // Calculate weighted variance after split
//        double varianceAfter = 0;
//        for (List<Map<String, Object>> subset : splitData.values()) {
//            if (!subset.isEmpty()) {
//                double weight = (double) subset.size() / data.size();
//                varianceAfter += weight * calculateVariance(subset);
//            }
//        }
//
//        // Information gain is the reduction in variance
//        return varianceBefore - varianceAfter;
//    }
//
//    private double calculateVariance(List<Map<String, Object>> data) {
//        if (data.isEmpty()) return 0;
//
//        // Calculate mean
//        double sum = 0;
//        int validCount = 0;
//        for (Map<String, Object> dataPoint : data) {
//            Object priceObj = dataPoint.get("Average");
//            if (priceObj instanceof Number) {
//                sum += ((Number) priceObj).doubleValue();
//                validCount++;
//            }
//        }
//
//        if (validCount == 0) return 0;
//        double mean = sum / validCount;
//
//        // Calculate variance
//        double variance = 0;
//        for (Map<String, Object> dataPoint : data) {
//            Object priceObj = dataPoint.get("Average");
//            if (priceObj instanceof Number) {
//                double diff = ((Number) priceObj).doubleValue() - mean;
//                variance += diff * diff;
//            }
//        }
//
//        return variance / validCount;
//    }
//
//    private Map<String, List<Map<String, Object>>> splitData(List<Map<String, Object>> data,
//                                                             String feature, Object splitValue) {
//        Map<String, List<Map<String, Object>>> result = new HashMap<>();
//        result.put("left", new ArrayList<>());
//        result.put("right", new ArrayList<>());
//
//        for (Map<String, Object> dataPoint : data) {
//            Object featureValue = dataPoint.get(feature);
//            if (featureValue == null) continue;
//
//            if (isNumeric(splitValue)) {
//                double value = ((Number) featureValue).doubleValue();
//                double threshold = ((Number) splitValue).doubleValue();
//                if (value <= threshold) {
//                    result.get("left").add(dataPoint);
//                } else {
//                    result.get("right").add(dataPoint);
//                }
//            } else {
//                // Special handling for Commodity feature
//                if (feature.equals("Commodity")) {
//                    if (normalizeString(featureValue).equals(normalizeString(splitValue))) {
//                        result.get("left").add(dataPoint);
//                    } else {
//                        result.get("right").add(dataPoint);
//                    }
//                } else {
//                    if (featureValue.equals(splitValue)) {
//                        result.get("left").add(dataPoint);
//                    } else {
//                        result.get("right").add(dataPoint);
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    private Node createLeafNode(List<Map<String, Object>> data) {
//        Node leaf = new Node();
//        leaf.setLeaf(true);
//
//        // Calculate the average price for the leaf node
//        double sum = 0;
//        int count = 0;
//        for (Map<String, Object> dataPoint : data) {
//            Object priceObj = dataPoint.get("Average");
//            if (priceObj instanceof Number) {
//                sum += ((Number) priceObj).doubleValue();
//                count++;
//            }
//        }
//
//        leaf.setPredictedValue(count > 0 ? sum / count : 0);
//        return leaf;
//    }
//
//    public double predict(Map<String, Object> input) {
//        if (input == null) {
//            throw new IllegalArgumentException("Input cannot be null");
//        }
//        if (root == null) {
//            throw new IllegalStateException("Decision tree has not been trained");
//        }
//
//        try {
//            double prediction = predictRecursive(input, root);
//            return Double.isNaN(prediction) ? 0.0 : prediction;
//        } catch (Exception e) {
//            System.err.println("Prediction failed: " + e.getMessage());
//            return 0.0;
//        }
//    }
//
//    private double predictRecursive(Map<String, Object> input, Node node) {
//        if (node.isLeaf()) {
//            System.out.println("Reached leaf node with prediction: " + node.getPredictedValue());
//            return node.getPredictedValue();
//        }
//
//        String feature = node.getFeature();
//        Object splitValue = node.getSplitValue();
//        System.out.println("Evaluating feature: " + feature + " with split value: " + splitValue);
//
//        if (!input.containsKey(feature)) {
//            System.out.println("Feature " + feature + " not found in input, going right");
//            return node.getRight() != null ? predictRecursive(input, node.getRight()) :
//                    (node.getLeft() != null ? predictRecursive(input, node.getLeft()) : 0.0);
//        }
//
//        Object featureValue = input.get(feature);
//        if (featureValue == null) {
//            System.out.println("Feature " + feature + " is null, going right");
//            return node.getRight() != null ? predictRecursive(input, node.getRight()) : 0.0;
//        }
//
//        System.out.println("Input feature value: " + featureValue);
//
//        if (isNumeric(splitValue)) {
//            try {
//                double inputValue = convertToDouble(featureValue);
//                double splitValueNum = convertToDouble(splitValue);
//
//                if (inputValue <= splitValueNum) {
//                    System.out.println("Going left: " + inputValue + " <= " + splitValueNum);
//                    return predictRecursive(input, node.getLeft());
//                } else {
//                    System.out.println("Going right: " + inputValue + " > " + splitValueNum);
//                    return predictRecursive(input, node.getRight());
//                }
//            } catch (NumberFormatException e) {
//                System.err.println("Numeric conversion failed for " + feature);
//                return predictRecursive(input, node.getRight());
//            }
//        } else {
//            if (feature.equals("Commodity")) {
//                if (normalizeString(featureValue).equals(normalizeString(splitValue))) {
//                    System.out.println("Going left: Commodity matches " + splitValue);
//                    return predictRecursive(input, node.getLeft());
//                } else {
//                    System.out.println("Going right: Commodity doesn't match " + splitValue);
//                    return predictRecursive(input, node.getRight());
//                }
//            } else {
//                if (featureValue.equals(splitValue)) {
//                    System.out.println("Going left: " + featureValue + " equals " + splitValue);
//                    return predictRecursive(input, node.getLeft());
//                } else {
//                    System.out.println("Going right: " + featureValue + " does not equal " + splitValue);
//                    return predictRecursive(input, node.getRight());
//                }
//            }
//        }
//    }
//
//    private double convertToDouble(Object value) throws NumberFormatException {
//        if (value instanceof Number) {
//            return ((Number) value).doubleValue();
//        }
//        return Double.parseDouble(value.toString());
//    }
//
//    private boolean isNumeric(Object obj) {
//        return obj instanceof Number;
//    }
//}
