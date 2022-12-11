package com.eventcalculator.service;

import com.eventcalculator.model.DistributionData;
import org.springframework.stereotype.Component;

@Component
public class DistributionService {
    /**
     * Calculate the expected value of a given probability distribution
     * E(P(n)) = sum(n*P(n), -inf, +inf)
     * For the remainder, assumes the distribution is geometric for n >= n_max
     *
     * @param data The probability distribution (P(n))
     * @return The expected value of the distribution (E(P(n)))
     */
    public double getExpectedValue(DistributionData data) {
        final int maxAttempts = data.getValues().length;
        double expectedValue = 0;

        for(int index = 0 ; index < maxAttempts ; index++) {
            expectedValue += index*data.getValues()[index];
        }

        //Correction introduced for the remainder
        //These formulas are obtained by assuming a geometric distribution for values n >= n_max
        //This geometric distribution adds up to the correct remainder value
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

    /**
     * Calculate the standard deviation of a given probability distribution
     * sigma(P(n)) = sum((n-E(P(n)))^2*P(n), -inf, +inf)
     * For the remainder, assumes the distribution is geometric for n >= n_max
     *
     * @param data The probability distribution (P(n))
     * @return The standard deviation of the distribution (sigma(P(n)))
     */
    public double getDeviation(DistributionData data) {
        final int maxAttempts = data.getValues().length;
        final double expectedValue = this.getExpectedValue(data);
        double variance = 0;

        for(int index = 0 ; index < maxAttempts ; index++) {
            variance += Math.pow(index-expectedValue, 2)*data.getValues()[index];
        }

        //Correction introduced for the remainder
        //These formulas are obtained by assuming a geometric distribution for values n >= n_max
        //This geometric distribution adds up to the correct remainder value
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

    /**
     * Calculate a given percentile of a given probability distribution
     * per(P(n), r) = the smallest m such that sum(P(n), -inf, m) >= r
     *
     * @param data The probability distribution (P(n))
     * @param value The percentage value to calculate the percentile for (r)
     * @return The standard deviation of the distribution (per(P(n), r))
     */
    public double getPercentile(DistributionData data, double value) {
        final int maxAttempts = data.getValues().length;

        double cumulativeChance = 0;
        for(int index = 0 ; index < maxAttempts ; index++) {
            cumulativeChance += data.getValues()[index];

            if(cumulativeChance >= value) {
                return index;
            }
        }

        //TODO Add a correction for the remainder instead of setting the percentile to n_max

        return maxAttempts;
    }

    /**
     * Generate a geometric probability distribution with the given ratio.
     * The formula for the distribution is P(n) = (1-r)*r^(n-1), for n >= 1.
     * This is capped to values n < n_max, so the remainder is R = r^n_max.
     * This distribution arises when a state has a chance to be unchanged by an event.
     *
     * @param ratio The ratio of the geometric distribution (r)
     * @param maxAttempts The maximum number of attempts to calculate the distribution for (n_max)
     * @return The geometric probability distribution (P(n))
     */
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

    /**
     * Do a convolution operation between two probability distributions
     * P(n) = (P_1 * P_2)(n), where * represents convolution, not product
     *
     * @param data1 A probability distribution (P_1(n))
     * @param data2 The other probability distribution (P_2(n))
     * @return The result of the convolution (P(n))
     */
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

    /**
     * Add two probability distributions together, applying weights.
     * The formula for the result is P(n) = (1-r)*P_old(n) + r*P_new(n).
     * Thus the result is always a probability distribution.
     *
     * @param oldData The probability distribution to add to (P_old(n))
     * @param newData The new probability distribution to add (P_new(n))
     * @param ratio The weight of the new distribution (r)
     * @return The resulting probability distribution (P(n))
     */
    public DistributionData addDistribution(DistributionData oldData, DistributionData newData, double ratio) {
        final int maxAttempts = oldData.getValues().length;
        final double[] values = new double[maxAttempts];

        for(int index = 0 ; index < maxAttempts ; index++) {
            values[index] = oldData.getValues()[index]*(1-ratio)+newData.getValues()[index]*ratio;
        }

        final double remainder = oldData.getRemainder()*(1-ratio)+ newData.getRemainder()*ratio;

        return new DistributionData(values, remainder);
    }
}
