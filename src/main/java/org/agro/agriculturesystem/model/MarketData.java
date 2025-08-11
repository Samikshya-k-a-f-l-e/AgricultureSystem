package org.agro.agriculturesystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "market_data")
public class MarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commodity_name", nullable = false)
    private String commodity;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;
    
    // Constructors
    public MarketData() {
    }

    public MarketData(Long id, String commodity, double currentPrice, LocalDate lastUpdated) {
        this.id = id;
        this.commodity = commodity;
        this.currentPrice = currentPrice;
        this.lastUpdated = lastUpdated;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
