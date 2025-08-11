package org.agro.agriculturesystem.controller;

import org.agro.agriculturesystem.decision.PredictionRequest;
import org.agro.agriculturesystem.decision.PredictionResponse;
import org.agro.agriculturesystem.service.PricePredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/savePrediction")
public class PricePredictionController {

    @Autowired
    private PricePredictionService pricePredictionService;

    @PostMapping
    public ResponseEntity<String> savePricePrediction(
            @RequestBody PredictionRequest predictionRequest,
            PredictionResponse predictionResponse) {
        pricePredictionService.savePricePrediction(predictionRequest, predictionResponse);

        return ResponseEntity.ok("Price prediction saved successfully");
    }
}