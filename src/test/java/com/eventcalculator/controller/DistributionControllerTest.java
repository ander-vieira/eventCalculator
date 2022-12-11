package com.eventcalculator.controller;

import com.eventcalculator.dto.GetPercentileRequest;
import com.eventcalculator.model.DistributionData;
import com.eventcalculator.service.DistributionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DistributionControllerTest {
    @Value("${test.delta}")
    private double DELTA;

    @InjectMocks
    private DistributionController distributionController;
    @Mock
    private DistributionService distributionService;

    @Test
    public void getExpectedValueTest() {
        final int maxAttempts = 100;
        final DistributionData data = new DistributionData(new double[maxAttempts], 1);
        final double expectedValue = 3.5;

        Mockito.doReturn(expectedValue).when(distributionService).getExpectedValue(data);

        double result = distributionController.getExpectedValue(data);

        Assertions.assertEquals(expectedValue, result, DELTA);
    }

    @Test
    public void getDeviationTest() {
        final int maxAttempts = 100;
        final DistributionData data = new DistributionData(new double[maxAttempts], 1);
        final double deviation = 3.5;

        Mockito.doReturn(deviation).when(distributionService).getDeviation(data);

        double result = distributionController.getDeviation(data);

        Assertions.assertEquals(deviation, result, DELTA);
    }

    @Test
    public void getPercentileTest() {
        final int maxAttempts = 100;
        final DistributionData data = new DistributionData(new double[maxAttempts], 1);
        final double value = 0.5;
        final GetPercentileRequest request = new GetPercentileRequest(value, data);
        final double percentile = 3.5;

        Mockito.doReturn(percentile).when(distributionService).getPercentile(data, value);

        double result = distributionController.getPercentile(request);

        Assertions.assertEquals(percentile, result, DELTA);
    }
}
