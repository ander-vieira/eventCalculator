package com.eventcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DistributionData {
    private double[] values;
    private double remainder;

    public static DistributionData delta(int maxAttempts) {
        final double[] values = new double[maxAttempts];

        values[0] = 1;

        return new DistributionData(values, 0.0);
    }

    public static DistributionData empty(int maxAttempts) {
        return new DistributionData(new double[maxAttempts], 1.0);
    }
}
