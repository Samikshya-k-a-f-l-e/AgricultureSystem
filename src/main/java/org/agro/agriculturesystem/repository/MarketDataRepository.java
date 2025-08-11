package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
}