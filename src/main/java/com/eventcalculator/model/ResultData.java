package com.eventcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultData {
    private final double expectedValue;
    private DistributionData distribution;

    public static ResultData finished(int maxAttempts) {
        return new ResultData(0.0, DistributionData.delta(maxAttempts));
    }

    public static ResultData empty(int maxAttempts) {
        return new ResultData(Double.POSITIVE_INFINITY, DistributionData.empty(maxAttempts));
    }
}
