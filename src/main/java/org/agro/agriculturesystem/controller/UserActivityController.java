package org.agro.agriculturesystem.controller;

import org.agro.agriculturesystem.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserActivityController {

    @Autowired
    private UserActivityService userService;

    @GetMapping("/api/user-activity")
    @ResponseBody
    public Map<String, Object> getUserActivityData(
            @RequestParam(required = false, defaultValue = "monthly") String range) {

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> monthlyData = userService.getRegistrationsLast12Months();
        response.put("labels", monthlyData.get("labels"));
        response.put("registrations", monthlyData.get("registrations"));

        return response;
    }
}