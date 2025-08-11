package org.agro.agriculturesystem.decision;

public class PredictionResponse {
    private double predictedPrice;
    private String message;

    public PredictionResponse() {}

    public PredictionResponse(double predictedPrice, String message) {
        this.predictedPrice = predictedPrice;
        this.message = message;
    }

    public double getPredictedPrice() {
        return predictedPrice;
    }

    public void setPredictedPrice(double predictedPrice) {
        this.predictedPrice = predictedPrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
