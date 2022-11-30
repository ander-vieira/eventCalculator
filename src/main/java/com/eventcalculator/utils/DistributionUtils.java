package com.eventcalculator.utils;

import org.springframework.stereotype.Component;

@Component
public class DistributionUtils {
    public void addArray(double[] target, double[] origin) {
        for(int index = 0 ; index < target.length ; index++) {
                target[index] += origin[index];
        }
    }

    public double[] geometricExpansion(double[] origin, double ratio) {
        double[] result = new double[origin.length];

        double totalRatio = 1;
        for(int offset = 0 ; offset < result.length && totalRatio != 0 ; offset++) {
            for(int index = offset ; index < result.length ; index++) {
                result[index] += origin[index - offset]*totalRatio;
            }

            totalRatio *= ratio;
        }

        return result;
    }
}
