package com.eventcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultStats {
    private final double[] distribution;
    private final double remainder;
    private final double expectedValue;
}
