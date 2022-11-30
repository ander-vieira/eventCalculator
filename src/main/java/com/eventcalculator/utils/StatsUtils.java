package com.eventcalculator.utils;

import com.eventcalculator.model.ResultStats;
import org.springframework.stereotype.Component;

@Component
public class StatsUtils {
    public ResultStats addResults(ResultStats target, ResultStats origin, double ratio) {
        double[] distribution = new double[target.getDistribution().length];
        for(int index = 0 ; index < distribution.length ; index++) {
            distribution[index] = target.getDistribution()[index] + origin.getDistribution()[index]*ratio;
        }
        double remainder = target.getRemainder()+origin.getRemainder()*ratio;
        double expectedValue = target.getExpectedValue()+origin.getExpectedValue()*ratio;

        return new ResultStats(distribution, remainder, expectedValue);
    }

    public ResultStats geometricExpansion(ResultStats origin, double ratio) {
        double[] distribution = new double[origin.getDistribution().length];
        double remainder = 1;

        if(ratio != 0) {
            double totalRatio = ratio;
            for(int offset = 0 ; offset < distribution.length && totalRatio != 0 ; offset++) {
                for(int index = 0 ; index < distribution.length - offset ; index++) {
                    distribution[index + offset] += origin.getDistribution()[index]*totalRatio;
                }

                totalRatio *= (1 - ratio);
            }

            remainder = origin.getRemainder();
            double remainderRatio = 1 - ratio;
            for(int index = distribution.length - 1 ; index >= 0 ; index--) {
                remainder += origin.getDistribution()[index]*remainderRatio;
                remainderRatio *= (1 - ratio);
            }
        }

        return new ResultStats(distribution, remainder, origin.getExpectedValue());
    }
}
