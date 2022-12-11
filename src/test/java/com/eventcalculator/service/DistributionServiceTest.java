package com.eventcalculator.service;

import com.eventcalculator.model.DistributionData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class DistributionServiceTest {
    @Value("${test.delta}")
    private double DELTA;

    @InjectMocks
    DistributionService distributionService;

    @Test
    public void getExpectedValueTest() {
        final double ratio = 0.5;
        final DistributionData data = distributionService.getGeometricDistribution(ratio, 10);
        final double expectedValue = 1/(1-ratio);

        final double result = distributionService.getExpectedValue(data);

        Assertions.assertEquals(expectedValue, result, DELTA);
    }

    @Test
    public void getDeviationTest() {
        final double ratio = 0.5;
        final DistributionData data = distributionService.getGeometricDistribution(ratio, 10);
        final double deviation = Math.sqrt(ratio)/(1-ratio);

        final double result = distributionService.getDeviation(data);

        Assertions.assertEquals(deviation, result, DELTA);
    }

    @Test
    public void getPercentileTest() {
        final double ratio = 0.5;
        final int percentile = 3;
        final DistributionData data = distributionService.getGeometricDistribution(ratio, 10);
        final double value = 1-Math.pow(ratio, percentile)*(2-ratio);

        final double result = distributionService.getPercentile(data, value);

        Assertions.assertEquals(percentile, result, DELTA);
    }

    @Test
    public void getGeometricDistributionTest() {
        final double ratio = 0.5;
        final int maxAttempts = 10;

        final DistributionData result = distributionService.getGeometricDistribution(ratio, maxAttempts);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(maxAttempts, result.getValues().length);
        Assertions.assertEquals(0.0, result.getValues()[0], DELTA);
        Assertions.assertEquals(1-ratio, result.getValues()[1], DELTA);
        Assertions.assertEquals(ratio*(1-ratio), result.getValues()[2], DELTA);
        Assertions.assertEquals(Math.pow(ratio, maxAttempts-1), result.getRemainder(), DELTA);
    }

    @Test
    public void getConvolutionTest() {
        final int maxAttempts = 10;
        final DistributionData data1 = distributionService.getGeometricDistribution(0.5, maxAttempts);
        final DistributionData data2 = DistributionData.delta(maxAttempts);

        final DistributionData result = distributionService.getConvolution(data1, data2);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(maxAttempts, result.getValues().length);
        for(int i = 0 ; i < maxAttempts ; i++) {
            Assertions.assertEquals(data1.getValues()[i], result.getValues()[i], DELTA);
        }
        Assertions.assertEquals(data1.getRemainder(), result.getRemainder(), DELTA);
    }

    @Test
    public void addDistributionTest() {
        final int maxAttempts = 10;
        final double ratio = 0.5;
        final DistributionData oldData = distributionService.getGeometricDistribution(0.5, maxAttempts);
        final DistributionData newData = DistributionData.delta(maxAttempts);

        final DistributionData result = distributionService.addDistribution(oldData, newData, ratio);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(maxAttempts, result.getValues().length);
        for(int i = 0 ; i < maxAttempts ; i++) {
            Assertions.assertEquals(oldData.getValues()[i]*(1-ratio)+newData.getValues()[i]*ratio, result.getValues()[i], DELTA);
        }
        Assertions.assertEquals(oldData.getRemainder()*(1-ratio)+newData.getRemainder()*ratio, result.getRemainder(), DELTA);
        Assertions.assertEquals(1.0, Arrays.stream(result.getValues()).sum()+result.getRemainder(), DELTA);
    }
}
