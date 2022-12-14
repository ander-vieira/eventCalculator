package com.eventcalculator.controller;

import com.eventcalculator.dto.ProcessModelRequest;
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
    public ResultData processModel(@RequestBody ProcessModelRequest request) {
        return eventCalculatorService.getResultData(ItemModel.fromItems(request.getItems()), request.getEvent(), request.getMaxAttempts());
    }
}
