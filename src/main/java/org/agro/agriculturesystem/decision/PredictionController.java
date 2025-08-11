//package org.agro.agriculturesystem.decision;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "*")
//public class PredictionController {
//
//    @Autowired
//    private PredictionService predictionService;
//
//    @Autowired
//    private WeatherService weatherService;
//
//    @PostMapping("/predict")
//    public ResponseEntity<?> predictPrice(@RequestBody PredictionRequest request) {
//        try {
//            if (request.getCommodity() == null || request.getCommodity().isEmpty()) {
//                return ResponseEntity.badRequest().body("Commodity is required");
//            }
//
//            if (request.getYear() <= 0 || request.getMonth() <= 0 || request.getMonth() > 12) {
//                return ResponseEntity.badRequest().body("Valid year and month (1-12) are required");
//            }
//
//            Map<String, Object> currentWeather = weatherService.getCurrentWeather(request.getLocation());
//            request.setTemperature((Double) currentWeather.get("Temperature"));
//            request.setHumidity((Double) currentWeather.get("Humidity"));
//            request.setPrecipitation((Double) currentWeather.get("Precipitation"));
//
//            System.out.println("Using current weather data from " + currentWeather.get("City"));
//
//            System.out.println("Prediction request received: Commodity=" + request.getCommodity() +
//                    ", Year=" + request.getYear() + ", Month=" + request.getMonth() +
//                    ", Temp=" + request.getTemperature() + "°C" +
//                    ", Humidity=" + request.getHumidity() + "%" +
//                    ", Precipitation=" + request.getPrecipitation() + "mm");
//
//            double predictedPrice = predictionService.predictCropPrice(request);
//
//            // Check for NaN
//            if (Double.isNaN(predictedPrice)) {
//                System.err.println("Prediction returned NaN for input: " + request);
//                return ResponseEntity.internalServerError().body("Unable to generate prediction");
//            }
//
//            // Log successful prediction
//            System.out.println("Predicted price: " + predictedPrice);
//
//            PredictionResponse response = new PredictionResponse();
//            response.setPredictedPrice(predictedPrice);
//            response.setMessage("Prediction successful using current weather conditions");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            System.err.println("Prediction error: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Prediction failed: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/weather")
//    public ResponseEntity<?> getCurrentWeather(@RequestParam(defaultValue = "Kathmandu,NP") String location) {
//        try {
//            Map<String, Object> weather = weatherService.getCurrentWeather(location);
//            return ResponseEntity.ok(weather);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Failed to fetch weather data: " + e.getMessage());
//        }
//    }
//}
