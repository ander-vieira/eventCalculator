package com.eventcalculator.controller;

import com.eventcalculator.dto.EventCalculatorRequest;
import com.eventcalculator.service.EventCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventCalculatorController {
    @Autowired
    EventCalculatorService eventCalculatorService;

    @PostMapping("expectedAttempts")
    public double getExpectedAttempts(@RequestBody EventCalculatorRequest request) {
        return eventCalculatorService.getExpectedAttempts(request.getItems(), request.getEvent());
    }
}
