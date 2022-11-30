package com.eventcalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventCalculatorResponse {
    private final double[] distribution;
    private final double remainder;
    private final double expectedValue;
}
