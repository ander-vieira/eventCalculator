package com.eventcalculator.controller;

import com.eventcalculator.dto.EventCalculatorRequest;
import com.eventcalculator.model.ItemModel;
import com.eventcalculator.model.ResultData;
import com.eventcalculator.service.EventCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventCalculatorController {
    @Autowired
    EventCalculatorService eventCalculatorService;

    @PostMapping("/processModel")
    public ResultData processModel(@RequestBody EventCalculatorRequest request) {
        return eventCalculatorService.getResultStats(ItemModel.fromItems(request.getItems()), request.getEvent(), request.getMaxAttempts());
    }
}
