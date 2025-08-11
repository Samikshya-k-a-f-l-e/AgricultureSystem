//package org.agro.agriculturesystem.decision;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.*;
//
//@Service
//public class PredictionService {
//
//    private DecisionTree decisionTree;
//    private List<Map<String, Object>> historicalData;
//    private static final String CSV_FILE_PATH = "datasets/train.csv";
//
//    @PostConstruct
//    public void init() {
//        try {
//            historicalData = loadHistoricalDataFromCSV();
//
//            if (historicalData.isEmpty()) {
//                throw new IllegalStateException("No valid data loaded from CSV");
//            }
//
//            System.out.println("Building decision tree with " + historicalData.size() + " data points");
//            decisionTree = new DecisionTree();
//            decisionTree.buildTree(historicalData);
//        } catch (Exception e) {
//            System.err.println("Error initializing PredictionService: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Failed to initialize PredictionService: " + e.getMessage(), e);
//        }
//    }
//
//    public double predictCropPrice(PredictionRequest request) {
//
//        Map<String, Object> inputData = new HashMap<>();
//        inputData.put("Commodity", request.getCommodity());
//        inputData.put("Year", request.getYear());
//        inputData.put("Month", request.getMonth());
//        inputData.put("Temp", request.getTemperature());
//        inputData.put("Humidity", request.getHumidity());
//        inputData.put("Precipitation", request.getPrecipitation());
//
//        System.out.println("Input data for prediction: " + inputData);
//
//        return decisionTree.predict(inputData);
//    }
//
//    private List<Map<String, Object>> loadHistoricalDataFromCSV() throws IOException {
//        List<Map<String, Object>> data = new ArrayList<>();
//        ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
//
//        System.out.println("CSV file exists: " + resource.exists());
//
//        try (InputStream inputStream = resource.getInputStream();
//             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//
//            // Read header line
//            String headerLine = reader.readLine();
//            if (headerLine == null) {
//                throw new IOException("CSV file is empty");
//            }
//
//            // Clean headers
//            String[] headers = headerLine.trim().split("\\s*,\\s*");
//            System.out.println("Headers: " + Arrays.toString(headers));
//
//            // Read data lines
//            String line;
//            int lineNum = 1;
//            while ((line = reader.readLine()) != null) {
//                lineNum++;
//                line = line.trim();
//                if (line.isEmpty()) continue;
//
//                String[] values = line.split("\\s*,\\s*");
//                if (values.length != headers.length) {
//                    System.err.println("Skipping malformed line #" + lineNum + ": " + line);
//                    continue;
//                }
//
//                Map<String, Object> dataPoint = new HashMap<>();
//                try {
//                    for (int i = 0; i < headers.length; i++) {
//                        String header = headers[i];
//                        String value = values[i].trim();
//
//                        // Handle numeric fields
//                        if (header.equals("Average") ||
//                                header.equals("Temp") ||
//                                header.equals("Humidity") ||
//                                header.equals("Precipitation") ||
//                                header.equals("Year") ||
//                                header.equals("Month")) {
//                            dataPoint.put(header, value.isEmpty() ? 0.0 : Double.parseDouble(value));
//                        }
//                        else {
//                            dataPoint.put(header, value);
//                        }
//                    }
//                    if (!dataPoint.containsKey("Average")) {
//                        System.err.println("Missing price in line #" + lineNum);
//                        continue;
//                    }
//
//                    data.add(dataPoint);
//                } catch (Exception e) {
//                    System.err.println("Error processing line #" + lineNum + ": " + e.getMessage());
//                }
//            }
//        }
//
//        System.out.println("Successfully loaded " + data.size() + " valid data points");
//        if (!data.isEmpty()) {
//            System.out.println("First data point: " + data.get(0));
//        }
//
//        return data;
//    }
//}