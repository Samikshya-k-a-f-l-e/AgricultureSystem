package org.agro.agriculturesystem.service;

import org.agro.agriculturesystem.decision.PredictionRequest;
import org.agro.agriculturesystem.decision.PredictionResponse;
import org.agro.agriculturesystem.decision.WeatherService;
import org.agro.agriculturesystem.model.PricePrediction;
import org.agro.agriculturesystem.repository.PricePredictionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PricePredictionService {

    private final PricePredictionRepository predictionRepository;
    private final WeatherService weatherService;

    public PricePredictionService(PricePredictionRepository predictionRepository, WeatherService weatherService) {
        this.predictionRepository = predictionRepository;
        this.weatherService = weatherService;
    }

    @Transactional
    public void savePricePrediction(PredictionRequest predictionRequest, PredictionResponse predictionResponse) {
        double year = predictionRequest.getYear();
        double month = predictionRequest.getMonth();
        String commodity = predictionRequest.getCommodity();
        double temperature = predictionRequest.getTemperature();
        double humidity = predictionRequest.getHumidity();
        double precipitation = predictionRequest.getPrecipitation();
        double predictedPrice = predictionResponse.getPredictedPrice();

        List<PricePrediction> existingPredictions = predictionRepository.findByCommodityAndYearAndMonth(commodity, year, month);

        PricePrediction pricePrediction;

        if (!existingPredictions.isEmpty()) {
            pricePrediction = existingPredictions.get(0);
        } else {
            pricePrediction = new PricePrediction();
        }
        pricePrediction.setYear(year);
        pricePrediction.setMonth(month);
        pricePrediction.setCommodity(commodity);
        pricePrediction.setTemperature(temperature);
        pricePrediction.setHumidity(humidity);
        pricePrediction.setPrecipitation(precipitation);
        pricePrediction.setPredictedPrice(predictedPrice);

        predictionRepository.save(pricePrediction);
    }

}
