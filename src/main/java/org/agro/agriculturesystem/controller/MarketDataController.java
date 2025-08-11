package org.agro.agriculturesystem.controller;

import org.agro.agriculturesystem.dto.MarketDataDTO;
import org.agro.agriculturesystem.service.MarketDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/market-data")
@Validated
public class MarketDataController {
    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @PostMapping
    public ResponseEntity<MarketDataDTO> createMarketData(@Valid @RequestBody MarketDataDTO marketDataDto) {
        try {
            MarketDataDTO createdMarketData = marketDataService.createMarketData(marketDataDto);
            return new ResponseEntity<>(createdMarketData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketDataDTO> getMarketDataById(@PathVariable Long id) {
        try {
            MarketDataDTO marketDataDto = marketDataService.getMarketDataById(id);
            return ResponseEntity.ok(marketDataDto);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<MarketDataDTO>> getAllMarketData() {
        try {
            List<MarketDataDTO> marketDataList = marketDataService.getAllMarketDataAsList();
            return ResponseEntity.ok(marketDataList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketDataDTO> updateMarketData(
            @PathVariable Long id,
            @Valid @RequestBody MarketDataDTO marketDataDto) {
        try {
            MarketDataDTO updatedMarketData = marketDataService.updateMarketData(id, marketDataDto);
            return ResponseEntity.ok(updatedMarketData);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketData(@PathVariable Long id) {
        try {
            marketDataService.deleteMarketData(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}