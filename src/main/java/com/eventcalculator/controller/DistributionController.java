package com.eventcalculator.controller;

import com.eventcalculator.dto.GetPercentileRequest;
import com.eventcalculator.model.DistributionData;
import com.eventcalculator.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/distribution")
public class DistributionController {
    @Autowired
    DistributionService distributionService;

    @PostMapping("/expectedValue")
    public double getExpectedValue(@RequestBody DistributionData data) {
        return distributionService.getExpectedValue(data);
    }

    @PostMapping("/deviation")
    public double getDeviation(@RequestBody DistributionData data) {
        return distributionService.getDeviation(data);
    }

    @PostMapping("/percentile")
    public double getPercentile(@RequestBody GetPercentileRequest request) {
        return distributionService.getPercentile(request.getDistribution(), request.getValue());
    }
}
