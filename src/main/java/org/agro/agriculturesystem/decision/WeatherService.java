package org.agro.agriculturesystem.decision;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getCurrentWeather(String city) {
        try {
//            [apiUrl]?q=[city]&appid=[apiKey]&units=metric
            String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
            String response = restTemplate.getForObject(url, String.class);

            JsonNode jsonNode = objectMapper.readTree(response);

            Map<String, Object> weatherData = new HashMap<>();
            weatherData.put("Temperature", jsonNode.path("main").path("temp").asDouble());
            weatherData.put("Humidity", jsonNode.path("main").path("humidity").asDouble());
            weatherData.put("Precipitation", jsonNode.path("main").path("precipitation").asDouble());
            weatherData.put("City", jsonNode.path("name").asText());
            System.out.println("Current weather for " + city + ": " + weatherData);
            return weatherData;
        } catch (Exception e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
            return Map.of();
        }
    }
}
