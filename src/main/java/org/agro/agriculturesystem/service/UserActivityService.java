package org.agro.agriculturesystem.service;

import org.agro.agriculturesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserActivityService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getRegistrationsLast12Months() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusMonths(11); // 12 months inclusive

        // Get data from repository
        List<Object[]> rawData = userRepository.countRegistrationsByMonthWithYear(
                startDate,
                now
        );

        // Create a map of all months in the range
        Map<String, Integer> resultMap = new LinkedHashMap<>();
        for (int i = 0; i < 12; i++) {
            LocalDate month = startDate.plusMonths(i);
            String monthKey = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            resultMap.put(monthKey, 0); // Initialize with 0
        }

        // Fill with actual data
        for (Object[] row : rawData) {
            String month = (String) row[0];
            Number count = (Number) row[1]; // Could be Long or Integer depending on DB
            resultMap.put(month, count.intValue());
        }

        // Prepare response with formatted labels
        Map<String, Object> response = new HashMap<>();
        response.put("labels", resultMap.keySet().stream()
                .map(key -> {
                    LocalDate date = LocalDate.parse(key + "-01");
                    return date.format(DateTimeFormatter.ofPattern("MMM yyyy"));
                })
                .collect(Collectors.toList()));
        response.put("registrations", new ArrayList<>(resultMap.values()));

        return response;
    }
}