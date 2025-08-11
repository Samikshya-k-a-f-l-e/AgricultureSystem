package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.PricePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricePredictionRepository extends JpaRepository<PricePrediction, Long> {
    List<PricePrediction> findByCommodityAndYearAndMonth(String commodity, double year, double month);
}