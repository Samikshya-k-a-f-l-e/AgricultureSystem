package org.agro.agriculturesystem.service;

import org.agro.agriculturesystem.dto.MarketDataDTO;
import org.agro.agriculturesystem.model.MarketData;
import org.agro.agriculturesystem.repository.MarketDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketDataService {
    private final MarketDataRepository marketDataRepository;

    public MarketDataService(MarketDataRepository marketDataRepository) {
        this.marketDataRepository = marketDataRepository;
    }

    @Transactional
    public MarketDataDTO createMarketData(MarketDataDTO marketDataDto) {
        MarketData marketData = new MarketData();
        marketData.setCommodity(marketDataDto.getCommodity());
        marketData.setCurrentPrice(marketDataDto.getCurrentPrice());
        marketData.setLastUpdated(LocalDate.now());

        MarketData savedMarketData = marketDataRepository.save(marketData);
        return convertToDto(savedMarketData);
    }

    public MarketDataDTO getMarketDataById(Long id) {
        MarketData marketData = marketDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MarketData not found with id: " + id));
        return convertToDto(marketData);
    }

    public Page<MarketData> getAllMarketData(Pageable pageable) {
        return marketDataRepository.findAll(pageable);
    }

    public List<MarketDataDTO> getAllMarketDataAsList() {
        List<MarketData> marketDataList = marketDataRepository.findAll();
        return marketDataList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MarketDataDTO updateMarketData(Long id, MarketDataDTO marketDataDto) {
        MarketData marketData = marketDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MarketData not found with id: " + id));

        marketData.setCommodity(marketDataDto.getCommodity());
        marketData.setCurrentPrice(marketDataDto.getCurrentPrice());
        marketData.setLastUpdated(LocalDate.now());

        MarketData updatedMarketData = marketDataRepository.save(marketData);
        return convertToDto(updatedMarketData);
    }

    @Transactional
    public void deleteMarketData(Long id) {
        if (!marketDataRepository.existsById(id)) {
            throw new IllegalArgumentException("MarketData not found with id: " + id);
        }
        marketDataRepository.deleteById(id);
    }

    private MarketDataDTO convertToDto(MarketData marketData) {
        MarketDataDTO dto = new MarketDataDTO();
        dto.setId(marketData.getId());
        dto.setCommodity(marketData.getCommodity());
        dto.setCurrentPrice(marketData.getCurrentPrice());
        dto.setLastUpdated(marketData.getLastUpdated());
        return dto;
    }

    public MarketDataDTO convertEntityToDto(MarketData marketData) {
        return convertToDto(marketData);
    }
}