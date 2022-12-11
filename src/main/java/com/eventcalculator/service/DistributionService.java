package com.eventcalculator.service;

import com.eventcalculator.model.DistributionData;
import org.springframework.stereotype.Component;

@Component
public class DistributionService {
    public DistributionData getGeometricDistribution(double ratio, int maxAttempts) {
        final double[] values = new double[maxAttempts];
        final double r = 1-ratio;
        double remainder = 1;
        double totalRatio = ratio;

        for(int index = 1 ; index < maxAttempts ; index++) {
            values[index] = totalRatio;

            totalRatio *= r;
            remainder *= r;
        }

        return new DistributionData(values, remainder);
    }

    public DistributionData getConvolution(DistributionData data1, DistributionData data2) {
        final int maxAttempts = data1.getValues().length;
        final double[] values = new double[maxAttempts];
        double remainder = 0;

        for(int index1 = 0 ; index1 < maxAttempts ; index1++) {
            for(int index2 = 0 ; index2 < maxAttempts ; index2++) {
                if(index1+index2 < maxAttempts) {
                    values[index1+index2] += data1.getValues()[index1]*data2.getValues()[index2];
                } else {
                    remainder += data1.getValues()[index1]*data2.getValues()[index2];
                }
            }

            remainder += data1.getValues()[index1]*data2.getRemainder();

            remainder += data1.getRemainder()*data2.getValues()[index1];
        }

        remainder += data1.getRemainder()*data2.getRemainder();

        return new DistributionData(values, remainder);
    }

    public DistributionData addDistribution(DistributionData oldData, DistributionData newData, double ratio) {
        final int maxAttempts = oldData.getValues().length;
        final double[] values = new double[maxAttempts];

        for(int index = 0 ; index < maxAttempts ; index++) {
            values[index] = oldData.getValues()[index]*(1-ratio)+newData.getValues()[index]*ratio;
        }

        final double remainder = oldData.getRemainder()*(1-ratio)+ newData.getRemainder()*ratio;

        return new DistributionData(values, remainder);
    }

    public double getExpectedValue(DistributionData data) {
        final int maxAttempts = data.getValues().length;
        double expectedValue = 0;

        for(int index = 0 ; index < maxAttempts ; index++) {
            expectedValue += index*data.getValues()[index];
        }

        final double Plast = data.getValues()[maxAttempts-1];
        final double rem = data.getRemainder();
        if(rem > 0) {
            expectedValue += maxAttempts * rem;

            if(Plast > 0) {
                expectedValue += Math.pow(rem, 2) / Plast;
            }
        }

        return expectedValue;
    }

    public double getDeviation(DistributionData data) {
        final int maxAttempts = data.getValues().length;
        final double expectedValue = this.getExpectedValue(data);
        double variance = 0;

        for(int index = 0 ; index < maxAttempts ; index++) {
            variance += Math.pow(index-expectedValue, 2)*data.getValues()[index];
        }

        final double Plast = data.getValues()[maxAttempts-1];
        final double rem = data.getRemainder();
        if(rem > 0) {
            variance += Math.pow(maxAttempts-expectedValue, 2) * rem;

            if(Plast > 0) {
                variance += 2 * (maxAttempts-expectedValue) * Math.pow(rem, 2) / Plast;
                variance += Math.pow(rem, 2) * (Plast + 2*rem) / Math.pow(Plast, 2);
            }
        }

        return Math.sqrt(variance);
    }

    public double getPercentile(DistributionData data, double value) {
        final int maxAttempts = data.getValues().length;

        double cumulativeChance = 0;
        for(int index = 0 ; index < maxAttempts ; index++) {
            cumulativeChance += data.getValues()[index];

            if(cumulativeChance >= value) {
                return index;
            }
        }

        return maxAttempts;
    }
}
