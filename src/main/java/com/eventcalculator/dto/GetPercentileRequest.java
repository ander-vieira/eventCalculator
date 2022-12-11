package com.eventcalculator.dto;

import com.eventcalculator.model.DistributionData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPercentileRequest {
    private double value;
    private DistributionData distribution;
}
